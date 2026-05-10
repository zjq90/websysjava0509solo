package com.appsys.finance.service;

import com.appsys.finance.entity.Product;
import com.appsys.finance.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    private static final Pattern BATCH_PATTERN = Pattern.compile("^[A-Za-z0-9]{8}$");

    public Product createProduct(Product product) {
        validateProduct(product);
        if (productRepository.existsByBatchNumber(product.getBatchNumber())) {
            throw new RuntimeException("批次编号已存在，必须全局唯一");
        }
        return productRepository.save(product);
    }

    public Optional<Product> getProductById(Long id) {
        return productRepository.findById(id);
    }

    public Optional<Product> getProductByBatchNumber(String batchNumber) {
        return productRepository.findByBatchNumber(batchNumber);
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Transactional
    public Product updateProduct(Long id, Product productDetails) {
        return productRepository.findById(id).map(product -> {
            validateProduct(productDetails);
            
            if (!product.getBatchNumber().equals(productDetails.getBatchNumber()) &&
                productRepository.existsByBatchNumber(productDetails.getBatchNumber())) {
                throw new RuntimeException("批次编号已存在，必须全局唯一");
            }
            
            product.setName(productDetails.getName());
            product.setBatchNumber(productDetails.getBatchNumber());
            product.setExpiryDate(productDetails.getExpiryDate());
            product.setGerminationRate(productDetails.getGerminationRate());
            product.setUnitPrice(productDetails.getUnitPrice());
            product.setCostPrice(productDetails.getCostPrice());
            product.setStockQuantity(productDetails.getStockQuantity());
            return productRepository.save(product);
        }).orElseThrow(() -> new RuntimeException("产品不存在，ID: " + id));
    }

    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }

    private void validateProduct(Product product) {
        if (product.getBatchNumber() == null || !BATCH_PATTERN.matcher(product.getBatchNumber()).matches()) {
            throw new RuntimeException("批次编号必须为8位数字+字母组合");
        }

        LocalDate minExpiryDate = LocalDate.now().plusMonths(6);
        if (product.getExpiryDate() == null || product.getExpiryDate().isBefore(minExpiryDate)) {
            throw new RuntimeException("保质期不得早于当前日期+6个月");
        }

        if (product.getGerminationRate() == null) {
            throw new RuntimeException("发芽率不能为空");
        }
        if (product.getGerminationRate().compareTo(BigDecimal.ZERO) < 0 ||
            product.getGerminationRate().compareTo(new BigDecimal("100.0")) > 0) {
            throw new RuntimeException("发芽率必须在0-100%范围内");
        }
    }
}
