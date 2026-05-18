package com.flower.service;

import com.flower.entity.Product;
import com.flower.repository.ProductRepository;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * 商品服务类
 */
@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    /**
     * 分页查询商品
     */
    public Page<Product> findAll(Pageable pageable) {
        return productRepository.findAll(pageable);
    }

    /**
     * 根据ID查询商品
     */
    public Product findById(Long id) {
        return productRepository.findById(id).orElse(null);
    }

    /**
     * 保存商品
     */
    @Transactional
    public Product save(Product product) {
        return productRepository.save(product);
    }

    /**
     * 删除商品
     */
    @Transactional
    public void delete(Long id) {
        productRepository.deleteById(id);
    }

    /**
     * 商品上下架
     */
    @Transactional
    public Product updateStatus(Long id, Integer status) {
        Product product = findById(id);
        if (product != null) {
            product.setStatus(status);
            return productRepository.save(product);
        }
        return null;
    }

    /**
     * 获取库存预警商品列表
     */
    public List<Product> getStockWarningProducts() {
        return productRepository.findByStockLessThanEqualAndStatus(10, 1);
    }

    /**
     * 生成Excel导入模板
     */
    public byte[] generateTemplate() throws IOException {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("商品导入模板");

        Row headerRow = sheet.createRow(0);
        String[] headers = {"商品名称*", "价格*", "库存*", "最低库存阈值", "描述", "分类", "是否需要定制(0否1是)"};
        
        CellStyle headerStyle = workbook.createCellStyle();
        headerStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(headerStyle);
            sheet.setColumnWidth(i, 5000);
        }

        Row exampleRow = sheet.createRow(1);
        exampleRow.createCell(0).setCellValue("红玫瑰");
        exampleRow.createCell(1).setCellValue(99.00);
        exampleRow.createCell(2).setCellValue(100);
        exampleRow.createCell(3).setCellValue(10);
        exampleRow.createCell(4).setCellValue("新鲜红玫瑰，11支装");
        exampleRow.createCell(5).setCellValue("玫瑰花");
        exampleRow.createCell(6).setCellValue(0);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        workbook.write(outputStream);
        workbook.close();
        return outputStream.toByteArray();
    }

    /**
     * 批量导入商品
     */
    @Transactional
    public List<Product> importProducts(MultipartFile file) throws IOException {
        List<Product> products = new ArrayList<>();
        Workbook workbook = new XSSFWorkbook(file.getInputStream());
        Sheet sheet = workbook.getSheetAt(0);

        for (int i = 1; i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);
            if (row == null) continue;

            Product product = new Product();
            product.setName(getCellStringValue(row.getCell(0)));
            product.setPrice(BigDecimal.valueOf(getCellNumericValue(row.getCell(1))));
            product.setStock((int) getCellNumericValue(row.getCell(2)));
            product.setMinStock((int) getCellNumericValue(row.getCell(3)));
            product.setDescription(getCellStringValue(row.getCell(4)));
            product.setCategory(getCellStringValue(row.getCell(5)));
            product.setCustomFlag((int) getCellNumericValue(row.getCell(6)));
            product.setStatus(1);

            if (product.getName() != null && !product.getName().isEmpty()) {
                products.add(productRepository.save(product));
            }
        }

        workbook.close();
        return products;
    }

    private String getCellStringValue(Cell cell) {
        if (cell == null) return "";
        return cell.getStringCellValue();
    }

    private double getCellNumericValue(Cell cell) {
        if (cell == null) return 0;
        try {
            return cell.getNumericCellValue();
        } catch (Exception e) {
            try {
                return Double.parseDouble(cell.getStringCellValue());
            } catch (Exception ex) {
                return 0;
            }
        }
    }
}
