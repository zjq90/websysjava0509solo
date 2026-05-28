package com.club.management.util;

import com.club.management.entity.Registration;
import com.club.management.entity.SignIn;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Excel导出工具类
 * 
 * @author club-management
 * @version 1.0.0
 */
@Slf4j
public class ExcelUtil {

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private ExcelUtil() {
    }

    /**
     * 导出报名名单
     */
    public static byte[] exportRegistrationList(List<Registration> registrations) {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("报名名单");

            CellStyle headerStyle = createHeaderStyle(workbook);
            CellStyle dataStyle = createDataStyle(workbook);

            String[] headers = {"序号", "学号", "姓名", "院系", "专业", "班级", "手机号", "报名状态", "报名时间", "审核时间", "审核状态", "审核意见"};

            Row headerRow = sheet.createRow(0);
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(headerStyle);
                sheet.setColumnWidth(i, 15 * 256);
            }

            int rowNum = 1;
            for (Registration reg : registrations) {
                Row row = sheet.createRow(rowNum);
                createCell(row, 0, rowNum, dataStyle);
                createCell(row, 1, reg.getStudentNo(), dataStyle);
                createCell(row, 2, reg.getRealName(), dataStyle);
                createCell(row, 3, reg.getDepartment(), dataStyle);
                createCell(row, 4, reg.getMajor(), dataStyle);
                createCell(row, 5, reg.getClassName(), dataStyle);
                createCell(row, 6, reg.getPhone(), dataStyle);
                createCell(row, 7, getStatusText(reg.getStatus().name()), dataStyle);
                createCell(row, 8, reg.getCreateTime() != null ? reg.getCreateTime().format(DATE_TIME_FORMATTER) : "", dataStyle);
                createCell(row, 9, reg.getAuditTime() != null ? reg.getAuditTime().format(DATE_TIME_FORMATTER) : "", dataStyle);
                createCell(row, 10, getStatusText(reg.getStatus().name()), dataStyle);
                createCell(row, 11, reg.getAuditRemark(), dataStyle);
                rowNum++;
            }

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            workbook.write(outputStream);
            return outputStream.toByteArray();
        } catch (IOException e) {
            log.error("导出报名名单失败", e);
            throw new RuntimeException("导出Excel失败", e);
        }
    }

    /**
     * 导出签到表
     */
    public static byte[] exportSignInList(List<SignIn> signInList) {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("签到表");

            CellStyle headerStyle = createHeaderStyle(workbook);
            CellStyle dataStyle = createDataStyle(workbook);

            String[] headers = {"序号", "学号", "姓名", "院系", "签到状态", "签到时间", "签到方式", "补签原因"};

            Row headerRow = sheet.createRow(0);
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(headerStyle);
                sheet.setColumnWidth(i, 15 * 256);
            }

            int rowNum = 1;
            for (SignIn signIn : signInList) {
                Row row = sheet.createRow(rowNum);
                createCell(row, 0, rowNum, dataStyle);
                createCell(row, 1, signIn.getStudentNo(), dataStyle);
                createCell(row, 2, signIn.getRealName(), dataStyle);
                createCell(row, 3, signIn.getDepartment(), dataStyle);
                createCell(row, 4, getSignInStatusText(signIn.getStatus().name()), dataStyle);
                createCell(row, 5, signIn.getSignInTime() != null ? signIn.getSignInTime().format(DATE_TIME_FORMATTER) : "", dataStyle);
                createCell(row, 6, getSignInMethodText(signIn.getSignInMethod()), dataStyle);
                createCell(row, 7, signIn.getMakeUpReason(), dataStyle);
                rowNum++;
            }

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            workbook.write(outputStream);
            return outputStream.toByteArray();
        } catch (IOException e) {
            log.error("导出签到表失败", e);
            throw new RuntimeException("导出Excel失败", e);
        }
    }

    private static void createCell(Row row, int column, Object value, CellStyle style) {
        Cell cell = row.createCell(column);
        if (value instanceof Number) {
            cell.setCellValue(((Number) value).doubleValue());
        } else if (value instanceof String) {
            cell.setCellValue((String) value);
        }
        cell.setCellStyle(style);
    }

    private static CellStyle createHeaderStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        font.setFontHeightInPoints((short) 12);
        style.setFont(font);
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        return style;
    }

    private static CellStyle createDataStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        return style;
    }

    private static String getStatusText(String status) {
        return switch (status) {
            case "PENDING" -> "待审核";
            case "APPROVED" -> "已通过";
            case "REJECTED" -> "已拒绝";
            case "CANCELLED" -> "已取消";
            default -> status;
        };
    }

    private static String getSignInStatusText(String status) {
        return switch (status) {
            case "NOT_SIGNED" -> "未签到";
            case "SIGNED" -> "已签到";
            case "LATE" -> "迟到";
            case "EARLY_LEAVE" -> "早退";
            case "ABSENT" -> "缺席";
            case "MAKE_UP" -> "补签";
            default -> status;
        };
    }

    private static String getSignInMethodText(String method) {
        if (method == null) {
            return "";
        }
        return switch (method) {
            case "QR_CODE" -> "扫码签到";
            case "MANUAL" -> "手动补签";
            default -> method;
        };
    }
}
