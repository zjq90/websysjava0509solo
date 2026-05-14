package com.ops.service;

import com.itextpdf.text.*;
import com.itextpdf.text.Font;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.ops.entity.WorkOrder;
import com.ops.repository.WorkOrderRepository;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

/**
 * 报表导出服务类
 * 提供PDF和Excel格式的报表导出功能
 * 
 * @author ops-admin
 */
@Service
public class ReportService {

    @Autowired
    private WorkOrderRepository workOrderRepository;

    @Autowired
    private AnalyticsService analyticsService;

    /**
     * 生成PDF格式的日报
     */
    public byte[] generateDailyReport(LocalDateTime date) throws Exception {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Document document = new Document(PageSize.A4);
        PdfWriter.getInstance(document, outputStream);
        
        document.open();
        
        Font titleFont = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD);
        Font headerFont = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD);
        Font normalFont = new Font(Font.FontFamily.HELVETICA, 10);
        
        Paragraph title = new Paragraph("运营日报", titleFont);
        title.setAlignment(Element.ALIGN_CENTER);
        title.setSpacingAfter(20);
        document.add(title);
        
        Paragraph datePara = new Paragraph("日期: " + date.toLocalDate().toString(), normalFont);
        datePara.setSpacingAfter(15);
        document.add(datePara);
        
        LocalDateTime start = date.withHour(0).withMinute(0).withSecond(0);
        LocalDateTime end = start.plusDays(1);
        Map<String, Object> metrics = analyticsService.getServiceMetrics(start, end);
        
        document.add(new Paragraph("一、服务效能指标", headerFont));
        document.add(new Paragraph("工单总数: " + metrics.get("totalOrders"), normalFont));
        document.add(new Paragraph("平均响应时长(分钟): " + metrics.get("avgResponseTime"), normalFont));
        document.add(new Paragraph("一次修复率(%): " + metrics.get("firstFixRate"), normalFont));
        document.add(new Paragraph("平均满意度: " + metrics.get("avgSatisfaction"), normalFont));
        document.add(new Paragraph(" "));
        
        document.add(new Paragraph("二、工单状态统计", headerFont));
        document.add(new Paragraph("待处理工单数: " + metrics.get("pendingOrders"), normalFont));
        document.add(new Paragraph("进行中工单数: " + metrics.get("inProgressOrders"), normalFont));
        
        document.close();
        return outputStream.toByteArray();
    }

    /**
     * 生成Excel格式的工单数据导出
     */
    public byte[] generateWorkOrderExcel() throws Exception {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("工单数据");
        
        Row headerRow = sheet.createRow(0);
        String[] headers = {"工单编号", "用户姓名", "用户电话", "装维人员", "状态", "创建时间", "完成时间"};
        
        CellStyle headerStyle = workbook.createCellStyle();
        headerStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(headerStyle);
        }
        
        List<WorkOrder> orders = workOrderRepository.findAll();
        int rowNum = 1;
        for (WorkOrder order : orders) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(order.getOrderNo());
            row.createCell(1).setCellValue(order.getUserName() != null ? order.getUserName() : "");
            row.createCell(2).setCellValue(order.getUserPhone() != null ? order.getUserPhone() : "");
            row.createCell(3).setCellValue(order.getTechnicianName() != null ? order.getTechnicianName() : "");
            row.createCell(4).setCellValue(order.getStatus());
            row.createCell(5).setCellValue(order.getCreateTime() != null ? 
                order.getCreateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) : "");
            row.createCell(6).setCellValue(order.getCompleteTime() != null ? 
                order.getCompleteTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) : "");
        }
        
        for (int i = 0; i < headers.length; i++) {
            sheet.autoSizeColumn(i);
        }
        
        workbook.write(outputStream);
        workbook.close();
        
        return outputStream.toByteArray();
    }
}
