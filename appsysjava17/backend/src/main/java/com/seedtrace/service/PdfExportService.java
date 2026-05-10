package com.seedtrace.service;

import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.*;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import com.seedtrace.dto.TraceResponse;
import com.seedtrace.entity.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * PDF报告导出服务类
 * 
 * <p>提供溯源报告的PDF生成功能，支持：
 * <ul>
 *   <li>生成完整的溯源报告</li>
 *   <li>包含全链路信息的时间轴</li>
 *   <li>美观的表格布局</li>
 *   <li>二维码位置预留</li>
 * </ul>
 * </p>
 * 
 * @author Seed Trace System
 * @version 1.0.0
 */
@Service
public class PdfExportService {

    private static final Logger logger = LoggerFactory.getLogger(PdfExportService.class);

    @Autowired
    private TraceService traceService;

    /**
     * 生成溯源报告PDF
     * 
     * @param batchCode 批次编号
     * @return PDF字节数组
     */
    public byte[] generateTracePdf(String batchCode) {
        logger.info("开始生成溯源报告PDF，批次号：{}", batchCode);
        
        // 先查询溯源信息
        TraceResponse traceInfo = traceService.getTraceByBatchCode(batchCode, null, null, null);
        
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            // 创建PDF文档
            PdfWriter writer = new PdfWriter(baos);
            PdfDocument pdfDoc = new PdfDocument(writer);
            Document document = new Document(pdfDoc);
            
            // 使用默认字体
            PdfFont font = PdfFontFactory.createFont();
            
            // 1. 标题
            Paragraph title = new Paragraph("种子质量追溯报告")
                    .setFont(font)
                    .setFontSize(24)
                    .setBold()
                    .setTextAlignment(TextAlignment.CENTER)
                    .setMarginBottom(20);
            document.add(title);
            
            // 2. 基本信息
            document.add(new Paragraph("一、批次基本信息")
                    .setFont(font)
                    .setFontSize(16)
                    .setBold()
                    .setMarginTop(10)
                    .setMarginBottom(10));
            
            if (traceInfo.getBatchInfo() != null) {
                SeedBatch batch = traceInfo.getBatchInfo();
                Table infoTable = new Table(UnitValue.createPercentArray(new float[]{1, 1, 1, 1}))
                        .setWidth(UnitValue.createPercentValue(100));
                
                addCell(infoTable, "批次编号", batch.getBatchCode(), font);
                addCell(infoTable, "种子名称", batch.getSeedName(), font);
                addCell(infoTable, "种子品种", batch.getSeedVariety() != null ? batch.getSeedVariety() : "-", font);
                addCell(infoTable, "发芽率", batch.getGerminationRate() + "%", font);
                addCell(infoTable, "纯度", batch.getPurity() != null ? batch.getPurity() + "%" : "-", font);
                addCell(infoTable, "水分含量", batch.getMoistureContent() != null ? batch.getMoistureContent() + "%" : "-", font);
                addCell(infoTable, "生产日期", batch.getProductionDate().toString(), font);
                addCell(infoTable, "保质期至", batch.getShelfLife().toString(), font);
                addCell(infoTable, "数量", batch.getQuantity() + " kg", font);
                addCell(infoTable, "状态", "ACTIVE".equals(batch.getStatus()) ? "正常" : "停用", font);
                
                document.add(infoTable);
            }
            
            // 3. 亲本来源信息
            if (traceInfo.getParentInfo() != null) {
                document.add(new Paragraph("二、亲本来源信息")
                        .setFont(font)
                        .setFontSize(16)
                        .setBold()
                        .setMarginTop(20)
                        .setMarginBottom(10));
                
                ParentInfo parent = traceInfo.getParentInfo();
                Table parentTable = new Table(UnitValue.createPercentArray(new float[]{1, 1, 1, 1}))
                        .setWidth(UnitValue.createPercentValue(100));
                
                addCell(parentTable, "母本编号", parent.getFemaleParentCode() != null ? parent.getFemaleParentCode() : "-", font);
                addCell(parentTable, "母本名称", parent.getFemaleParentName() != null ? parent.getFemaleParentName() : "-", font);
                addCell(parentTable, "母本来源", parent.getFemaleParentOrigin() != null ? parent.getFemaleParentOrigin() : "-", font);
                addCell(parentTable, "父本编号", parent.getMaleParentCode() != null ? parent.getMaleParentCode() : "-", font);
                addCell(parentTable, "父本名称", parent.getMaleParentName() != null ? parent.getMaleParentName() : "-", font);
                addCell(parentTable, "父本来源", parent.getMaleParentOrigin() != null ? parent.getMaleParentOrigin() : "-", font);
                addCell(parentTable, "育种方式", parent.getBreedingMethod() != null ? parent.getBreedingMethod() : "-", font);
                addCell(parentTable, "育种单位", parent.getBreedingOrganization() != null ? parent.getBreedingOrganization() : "-", font);
                
                document.add(parentTable);
            }
            
            // 4. 田间管理记录（时间轴）
            if (traceInfo.getFieldRecords() != null && !traceInfo.getFieldRecords().isEmpty()) {
                document.add(new Paragraph("三、田间管理记录（时间轴）")
                        .setFont(font)
                        .setFontSize(16)
                        .setBold()
                        .setMarginTop(20)
                        .setMarginBottom(10));
                
                List<FieldManagement> fieldRecords = traceInfo.getFieldRecords();
                for (int i = 0; i < fieldRecords.size(); i++) {
                    FieldManagement record = fieldRecords.get(i);
                    
                    String operationTypeDesc = getOperationTypeDesc(record.getOperationType());
                    
                    document.add(new Paragraph((i + 1) + ". [" + record.getOperationDate() + "] " + operationTypeDesc + " - " + record.getOperationName())
                            .setFont(font)
                            .setFontSize(12)
                            .setBold()
                            .setMarginLeft(10));
                    
                    if (record.getSubstanceName() != null) {
                        document.add(new Paragraph("   物资：" + record.getSubstanceName() + 
                                (record.getDosage() != null ? "，用量：" + record.getDosage() : "") +
                                (record.getConcentration() != null ? "，浓度：" + record.getConcentration() : ""))
                                .setFont(font)
                                .setFontSize(10)
                                .setMarginLeft(20));
                    }
                    
                    if (record.getLocation() != null || record.getOperator() != null) {
                        document.add(new Paragraph("   地点：" + (record.getLocation() != null ? record.getLocation() : "-") +
                                "，操作人员：" + (record.getOperator() != null ? record.getOperator() : "-"))
                                .setFont(font)
                                .setFontSize(10)
                                .setMarginLeft(20));
                    }
                    
                    if (record.getRemark() != null) {
                        document.add(new Paragraph("   备注：" + record.getRemark())
                                .setFont(font)
                                .setFontSize(10)
                                .setMarginLeft(20));
                    }
                }
            }
            
            // 5. 加工流程记录
            if (traceInfo.getProcessingRecords() != null && !traceInfo.getProcessingRecords().isEmpty()) {
                document.add(new Paragraph("四、加工流程记录")
                        .setFont(font)
                        .setFontSize(16)
                        .setBold()
                        .setMarginTop(20)
                        .setMarginBottom(10));
                
                Table processTable = new Table(UnitValue.createPercentArray(new float[]{2, 2, 2, 2, 1}))
                        .setWidth(UnitValue.createPercentValue(100));
                
                // 表头
                addHeaderCell(processTable, "加工日期", font);
                addHeaderCell(processTable, "加工步骤", font);
                addHeaderCell(processTable, "设备", font);
                addHeaderCell(processTable, "参数", font);
                addHeaderCell(processTable, "结果", font);
                
                for (ProcessingInfo process : traceInfo.getProcessingRecords()) {
                    processTable.addCell(new Cell().add(new Paragraph(process.getProcessDate().toString()).setFont(font)));
                    processTable.addCell(new Cell().add(new Paragraph(getProcessStepDesc(process.getProcessStep())).setFont(font)));
                    processTable.addCell(new Cell().add(new Paragraph(
                            (process.getEquipmentName() != null ? process.getEquipmentName() : "") + 
                            (process.getEquipmentModel() != null ? " " + process.getEquipmentModel() : "")).setFont(font)));
                    processTable.addCell(new Cell().add(new Paragraph(
                            process.getProcessParameter() != null ? process.getProcessParameter() : "-").setFont(font)));
                    processTable.addCell(new Cell().add(new Paragraph(
                            "PASS".equals(process.getQualityCheckResult()) ? "合格" : 
                            "FAIL".equals(process.getQualityCheckResult()) ? "不合格" : "-").setFont(font)));
                }
                
                document.add(processTable);
            }
            
            // 6. 质检报告
            if (traceInfo.getQualityReports() != null && !traceInfo.getQualityReports().isEmpty()) {
                document.add(new Paragraph("五、质检报告")
                        .setFont(font)
                        .setFontSize(16)
                        .setBold()
                        .setMarginTop(20)
                        .setMarginBottom(10));
                
                for (QualityReport report : traceInfo.getQualityReports()) {
                    document.add(new Paragraph("报告编号：" + (report.getReportNo() != null ? report.getReportNo() : "-"))
                            .setFont(font)
                            .setFontSize(11)
                            .setBold());
                    
                    Table reportTable = new Table(UnitValue.createPercentArray(new float[]{1, 1, 1, 1, 1}))
                            .setWidth(UnitValue.createPercentValue(100));
                    
                    addCell(reportTable, "检验日期", report.getInspectionDate().toString(), font);
                    addCell(reportTable, "检验机构", report.getInspectionOrganization() != null ? report.getInspectionOrganization() : "-", font);
                    addCell(reportTable, "发芽率", report.getGerminationRateTest() != null ? report.getGerminationRateTest() + "%" : "-", font);
                    addCell(reportTable, "纯度", report.getPurityTest() != null ? report.getPurityTest() + "%" : "-", font);
                    addCell(reportTable, "水分", report.getMoistureTest() != null ? report.getMoistureTest() + "%" : "-", font);
                    
                    document.add(reportTable);
                    
                    Paragraph resultPara = new Paragraph("检验结论：" + 
                            ("QUALIFIED".equals(report.getOverallResult()) ? "合格" : "不合格"))
                            .setFont(font)
                            .setFontSize(11)
                            .setBold();
                    if ("QUALIFIED".equals(report.getOverallResult())) {
                        resultPara.setFontColor(ColorConstants.GREEN);
                    } else {
                        resultPara.setFontColor(ColorConstants.RED);
                    }
                    document.add(resultPara);
                    
                    if (report.getConclusion() != null) {
                        document.add(new Paragraph("详细结论：" + report.getConclusion())
                                .setFont(font)
                                .setFontSize(10)
                                .setMarginTop(5));
                    }
                }
            }
            
            // 7. 销售记录
            if (traceInfo.getSalesRecords() != null && !traceInfo.getSalesRecords().isEmpty()) {
                document.add(new Paragraph("六、销售记录")
                        .setFont(font)
                        .setFontSize(16)
                        .setBold()
                        .setMarginTop(20)
                        .setMarginBottom(10));
                
                Table salesTable = new Table(UnitValue.createPercentArray(new float[]{2, 2, 2, 1.5f, 2, 1.5f}))
                        .setWidth(UnitValue.createPercentValue(100));
                
                addHeaderCell(salesTable, "销售日期", font);
                addHeaderCell(salesTable, "客户名称", font);
                addHeaderCell(salesTable, "手机号", font);
                addHeaderCell(salesTable, "数量(kg)", font);
                addHeaderCell(salesTable, "销售渠道", font);
                addHeaderCell(salesTable, "总金额(元)", font);
                
                for (SalesInfo sales : traceInfo.getSalesRecords()) {
                    salesTable.addCell(new Cell().add(new Paragraph(sales.getSalesDate().toString()).setFont(font)));
                    salesTable.addCell(new Cell().add(new Paragraph(sales.getCustomerName()).setFont(font)));
                    salesTable.addCell(new Cell().add(new Paragraph(maskPhone(sales.getCustomerPhone())).setFont(font)));
                    salesTable.addCell(new Cell().add(new Paragraph(sales.getPurchaseQuantity().toString()).setFont(font)));
                    salesTable.addCell(new Cell().add(new Paragraph(sales.getSalesChannel() != null ? sales.getSalesChannel() : "-").setFont(font)));
                    salesTable.addCell(new Cell().add(new Paragraph(sales.getTotalAmount().toString()).setFont(font)));
                }
                
                document.add(salesTable);
            }
            
            // 8. 页脚信息
            document.add(new Paragraph("\n\n查询时间：" + traceInfo.getQueryTime())
                    .setFont(font)
                    .setFontSize(10)
                    .setTextAlignment(TextAlignment.RIGHT));
            document.add(new Paragraph("查询ID：" + traceInfo.getQueryId())
                    .setFont(font)
                    .setFontSize(10)
                    .setTextAlignment(TextAlignment.RIGHT));
            document.add(new Paragraph("本报告由种子质量追溯系统自动生成，数据真实有效。")
                    .setFont(font)
                    .setFontSize(9)
                    .setTextAlignment(TextAlignment.CENTER)
                    .setMarginTop(20)
                    .setFontColor(ColorConstants.GRAY));
            
            document.close();
            logger.info("溯源报告PDF生成成功，批次号：{}", batchCode);
            
            return baos.toByteArray();
            
        } catch (Exception e) {
            logger.error("生成PDF失败", e);
            throw new RuntimeException("生成PDF失败：" + e.getMessage(), e);
        }
    }

    /**
     * 添加表格单元格
     */
    private void addCell(Table table, String label, String value, PdfFont font) {
        Cell labelCell = new Cell().add(new Paragraph(label).setFont(font).setBold());
        Cell valueCell = new Cell().add(new Paragraph(value).setFont(font));
        table.addCell(labelCell);
        table.addCell(valueCell);
    }

    /**
     * 添加表格表头单元格
     */
    private void addHeaderCell(Table table, String text, PdfFont font) {
        Cell cell = new Cell().add(new Paragraph(text).setFont(font).setBold())
                .setBackgroundColor(ColorConstants.LIGHT_GRAY);
        table.addCell(cell);
    }

    /**
     * 获取操作类型描述
     */
    private String getOperationTypeDesc(String type) {
        switch (type) {
            case "SOWING": return "播种";
            case "FERTILIZER": return "施肥";
            case "PESTICIDE": return "打药";
            case "WATERING": return "浇水";
            case "HARVEST": return "收获";
            default: return type;
        }
    }

    /**
     * 获取加工步骤描述
     */
    private String getProcessStepDesc(String step) {
        switch (step) {
            case "CLEANING": return "清选";
            case "GRADING": return "分级";
            case "DRYING": return "干燥";
            case "PACKAGING": return "包装";
            default: return step;
        }
    }

    /**
     * 手机号脱敏显示
     */
    private String maskPhone(String phone) {
        if (phone == null || phone.length() < 11) {
            return "***";
        }
        return phone.substring(0, 3) + "****" + phone.substring(7);
    }
}
