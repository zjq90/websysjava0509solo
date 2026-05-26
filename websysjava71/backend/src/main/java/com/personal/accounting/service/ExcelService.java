package com.personal.accounting.service;

import com.personal.accounting.dto.TransactionDTO;
import com.personal.accounting.entity.Category;
import com.personal.accounting.entity.enums.CategoryType;
import com.personal.accounting.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * Excel服务类
 * 提供Excel/CSV导入导出功能
 * 
 * @author Personal Accounting
 * @version 1.0.0
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ExcelService {

    private final CategoryRepository categoryRepository;
    private final TransactionService transactionService;

    /**
     * 导出交易记录为Excel
     * 包含自动计算公式
     */
    public byte[] exportTransactionsToExcel(List<TransactionDTO> transactions) throws IOException {
        log.debug("导出 {} 条交易记录到Excel", transactions.size());
        
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("交易记录");
            
            // 创建标题样式
            CellStyle headerStyle = workbook.createCellStyle();
            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerStyle.setFont(headerFont);
            headerStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
            headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            
            // 创建金额样式
            CellStyle currencyStyle = workbook.createCellStyle();
            DataFormat format = workbook.createDataFormat();
            currencyStyle.setDataFormat(format.getFormat("¥#,##0.00"));
            
            // 创建百分比样式
            CellStyle percentStyle = workbook.createCellStyle();
            percentStyle.setDataFormat(format.getFormat("0.00%"));
            
            // 创建表头
            String[] headers = {"ID", "类型", "金额", "分类", "账户", "交易时间", "描述", "备注", "标签", "商家", "位置"};
            Row headerRow = sheet.createRow(0);
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(headerStyle);
            }
            
            // 填充数据
            int rowNum = 1;
            BigDecimal totalIncome = BigDecimal.ZERO;
            BigDecimal totalExpense = BigDecimal.ZERO;
            
            for (TransactionDTO t : transactions) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(t.getId());
                row.createCell(1).setCellValue("INCOME".equals(t.getType()) ? "收入" : "支出");
                
                Cell amountCell = row.createCell(2);
                amountCell.setCellValue(t.getAmount().doubleValue());
                amountCell.setCellStyle(currencyStyle);
                
                row.createCell(3).setCellValue(t.getCategoryName());
                row.createCell(4).setCellValue(t.getAccountName());
                row.createCell(5).setCellValue(t.getTransactionTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
                row.createCell(6).setCellValue(t.getDescription() != null ? t.getDescription() : "");
                row.createCell(7).setCellValue(t.getNotes() != null ? t.getNotes() : "");
                row.createCell(8).setCellValue(t.getTags() != null ? t.getTags() : "");
                row.createCell(9).setCellValue(t.getMerchant() != null ? t.getMerchant() : "");
                row.createCell(10).setCellValue(t.getLocation() != null ? t.getLocation() : "");
                
                if ("INCOME".equals(t.getType())) {
                    totalIncome = totalIncome.add(t.getAmount());
                } else {
                    totalExpense = totalExpense.add(t.getAmount());
                }
            }
            
            // 添加统计行
            int statRow = rowNum + 2;
            
            Row incomeRow = sheet.createRow(statRow);
            incomeRow.createCell(0).setCellValue("总收入:");
            Cell incomeCell = incomeRow.createCell(1);
            incomeCell.setCellFormula("SUMIF(B2:B" + rowNum + ",\"收入\",C2:C" + rowNum + ")");
            incomeCell.setCellStyle(currencyStyle);
            
            Row expenseRow = sheet.createRow(statRow + 1);
            expenseRow.createCell(0).setCellValue("总支出:");
            Cell expenseCell = expenseRow.createCell(1);
            expenseCell.setCellFormula("SUMIF(B2:B" + rowNum + ",\"支出\",C2:C" + rowNum + ")");
            expenseCell.setCellStyle(currencyStyle);
            
            Row netRow = sheet.createRow(statRow + 2);
            netRow.createCell(0).setCellValue("净收入:");
            Cell netCell = netRow.createCell(1);
            netCell.setCellFormula("B" + (statRow + 1) + "-B" + (statRow + 2));
            netCell.setCellStyle(currencyStyle);
            
            // 添加支出占比分析表
            int ratioStart = statRow + 5;
            Row ratioHeader = sheet.createRow(ratioStart);
            ratioHeader.createCell(0).setCellValue("支出分类占比分析");
            ratioHeader.getCell(0).setCellStyle(headerStyle);
            
            Row ratioColHeader = sheet.createRow(ratioStart + 1);
            ratioColHeader.createCell(0).setCellValue("分类");
            ratioColHeader.createCell(1).setCellValue("金额");
            ratioColHeader.createCell(2).setCellValue("占比");
            ratioColHeader.getCell(0).setCellStyle(headerStyle);
            ratioColHeader.getCell(1).setCellStyle(headerStyle);
            ratioColHeader.getCell(2).setCellStyle(headerStyle);
            
            // 自动调整列宽
            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
            }
            
            // 输出到字节数组
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            workbook.write(outputStream);
            return outputStream.toByteArray();
        }
    }

    /**
     * 导入银行CSV文件
     * 自动匹配分类
     */
    public List<TransactionDTO> importBankCSV(MultipartFile file, Long defaultAccountId) throws IOException {
        log.debug("导入银行CSV文件: {}", file.getOriginalFilename());
        
        List<TransactionDTO> imported = new ArrayList<>();
        List<Category> expenseCategories = categoryRepository.findByTypeAndEnabledTrueOrderBySortOrderAsc(CategoryType.EXPENSE);
        List<Category> incomeCategories = categoryRepository.findByTypeAndEnabledTrueOrderBySortOrderAsc(CategoryType.INCOME);
        
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8))) {
            
            String line;
            boolean isFirstLine = true;
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            
            while ((line = reader.readLine()) != null) {
                if (isFirstLine) {
                    isFirstLine = false;
                    continue;
                }
                
                String[] parts = line.split(",");
                if (parts.length < 3) {
                    continue;
                }
                
                try {
                    TransactionDTO dto = new TransactionDTO();
                    
                    // 假设CSV格式: 日期,描述,金额,商家
                    LocalDateTime dateTime = LocalDateTime.parse(parts[0].trim(), formatter);
                    String description = parts[1].trim();
                    BigDecimal amount = new BigDecimal(parts[2].trim());
                    String merchant = parts.length > 3 ? parts[3].trim() : "";
                    
                    // 判断收入还是支出
                    boolean isIncome = amount.compareTo(BigDecimal.ZERO) > 0;
                    dto.setType(isIncome ? "INCOME" : "EXPENSE");
                    dto.setAmount(amount.abs());
                    dto.setTransactionTime(dateTime);
                    dto.setDescription(description);
                    dto.setMerchant(merchant);
                    dto.setAccountId(defaultAccountId);
                    
                    // 自动匹配分类
                    Category matchedCategory = matchCategory(description, merchant, 
                            isIncome ? incomeCategories : expenseCategories);
                    
                    if (matchedCategory != null) {
                        dto.setCategoryId(matchedCategory.getId());
                        dto.setCategoryName(matchedCategory.getName());
                    } else {
                        // 使用第一个分类作为默认
                        List<Category> categories = isIncome ? incomeCategories : expenseCategories;
                        if (!categories.isEmpty()) {
                            dto.setCategoryId(categories.get(0).getId());
                            dto.setCategoryName(categories.get(0).getName());
                        }
                    }
                    
                    imported.add(dto);
                } catch (Exception e) {
                    log.warn("解析CSV行失败: {}", line, e);
                }
            }
        }
        
        log.info("成功解析 {} 条记录", imported.size());
        return imported;
    }

    /**
     * 批量保存导入的交易记录
     */
    public List<TransactionDTO> saveImportedTransactions(List<TransactionDTO> transactions) {
        List<TransactionDTO> saved = new ArrayList<>();
        for (TransactionDTO dto : transactions) {
            try {
                saved.add(transactionService.create(dto));
            } catch (Exception e) {
                log.error("保存交易记录失败: {}", dto, e);
            }
        }
        log.info("成功保存 {} 条导入的交易记录", saved.size());
        return saved;
    }

    /**
     * 根据描述和商家自动匹配分类
     */
    private Category matchCategory(String description, String merchant, List<Category> categories) {
        String content = (description + " " + merchant).toLowerCase();
        
        for (Category category : categories) {
            String categoryName = category.getName().toLowerCase();
            
            // 关键词匹配规则
            if (matchesKeywords(content, categoryName)) {
                return category;
            }
            
            // 餐饮
            if (categoryName.contains("餐饮") || categoryName.contains("食")) {
                if (containsAny(content, "餐", "饭", "菜", "mcdonald", "starbuck", "coffee", "restaurant", "cafe")) {
                    return category;
                }
            }
            
            // 交通
            if (categoryName.contains("交通") || categoryName.contains("行")) {
                if (containsAny(content, "车", "地铁", "公交", "taxi", "uber", "滴滴", "高速", "加油")) {
                    return category;
                }
            }
            
            // 购物
            if (categoryName.contains("购物") || categoryName.contains("买")) {
                if (containsAny(content, "淘宝", "京东", "amazon", "mall", "超市", "商场")) {
                    return category;
                }
            }
            
            // 娱乐
            if (categoryName.contains("娱乐") || categoryName.contains("玩")) {
                if (containsAny(content, "电影", "game", "游戏", "ktv", "唱", "movie", "netflix")) {
                    return category;
                }
            }
        }
        
        return null;
    }

    private boolean matchesKeywords(String content, String categoryName) {
        return content.contains(categoryName) || categoryName.contains(content);
    }

    private boolean containsAny(String content, String... keywords) {
        for (String keyword : keywords) {
            if (content.contains(keyword.toLowerCase())) {
                return true;
            }
        }
        return false;
    }
}
