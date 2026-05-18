package com.secondhand.service;

import com.secondhand.entity.Product;
import com.secondhand.repository.ProductRepository;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.criteria.Predicate;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Value("${application.inventory.warning-threshold:10}")
    private Integer defaultWarningThreshold;

    public Page<Product> findAll(String name, String category, String status, Long sellerId, Pageable pageable) {
        Specification<Product> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (name != null && !name.isEmpty()) {
                predicates.add(cb.like(root.get("name"), "%" + name + "%"));
            }
            if (category != null && !category.isEmpty()) {
                predicates.add(cb.equal(root.get("category"), category));
            }
            if (status != null && !status.isEmpty()) {
                predicates.add(cb.equal(root.get("status"), status));
            }
            if (sellerId != null) {
                predicates.add(cb.equal(root.get("sellerId"), sellerId));
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };
        return productRepository.findAll(spec, pageable);
    }

    public Optional<Product> findById(Long id) {
        return productRepository.findById(id);
    }

    @Transactional
    public Product save(Product product) {
        if (product.getId() == null) {
            product.setCreateTime(LocalDateTime.now());
        }
        product.setUpdateTime(LocalDateTime.now());
        return productRepository.save(product);
    }

    @Transactional
    public void deleteById(Long id) {
        productRepository.deleteById(id);
    }

    @Transactional
    public int batchOffShelf(List<Long> ids) {
        return productRepository.batchUpdateStatus(ids, "OFF_SHELF");
    }

    @Transactional
    public int batchDelete(List<Long> ids) {
        return productRepository.batchDelete(ids);
    }

    public List<Product> findLowStockProducts() {
        return productRepository.findLowStockProducts(defaultWarningThreshold);
    }

    @Transactional
    public List<Product> importFromExcel(MultipartFile file, Long sellerId) throws IOException {
        List<Product> products = new ArrayList<>();
        try (InputStream inputStream = file.getInputStream()) {
            Workbook workbook = new XSSFWorkbook(inputStream);
            Sheet sheet = workbook.getSheetAt(0);
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) continue;
                Product product = new Product();
                product.setName(getCellStringValue(row.getCell(0)));
                product.setDescription(getCellStringValue(row.getCell(1)));
                product.setPrice(BigDecimal.valueOf(getCellNumericValue(row.getCell(2))));
                product.setStock((int) getCellNumericValue(row.getCell(3)));
                product.setCategory(getCellStringValue(row.getCell(4)));
                product.setCondition(getCellStringValue(row.getCell(5)));
                product.setSellerId(sellerId);
                product.setStatus("ON_SALE");
                products.add(product);
            }
        }
        return productRepository.saveAll(products);
    }

    private String getCellStringValue(Cell cell) {
        if (cell == null) return "";
        cell.setCellType(CellType.STRING);
        return cell.getStringCellValue();
    }

    private double getCellNumericValue(Cell cell) {
        if (cell == null) return 0;
        if (cell.getCellType() == CellType.NUMERIC) {
            return cell.getNumericCellValue();
        }
        try {
            return Double.parseDouble(cell.getStringCellValue());
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    public byte[] exportToExcel(List<Product> products) throws IOException {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("商品列表");
            Row headerRow = sheet.createRow(0);
            String[] headers = {"ID", "商品名称", "描述", "价格", "库存", "分类", "成色", "状态", "创建时间"};
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
            }
            int rowNum = 1;
            for (Product product : products) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(product.getId());
                row.createCell(1).setCellValue(product.getName());
                row.createCell(2).setCellValue(product.getDescription());
                row.createCell(3).setCellValue(product.getPrice().doubleValue());
                row.createCell(4).setCellValue(product.getStock());
                row.createCell(5).setCellValue(product.getCategory());
                row.createCell(6).setCellValue(product.getCondition());
                row.createCell(7).setCellValue(product.getStatus());
                row.createCell(8).setCellValue(product.getCreateTime().toString());
            }
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            workbook.write(outputStream);
            return outputStream.toByteArray();
        }
    }

}