package com.management.platform.service;

import com.management.platform.entity.Product;
import com.management.platform.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 商品服务层
 * 提供商品的CRUD和统计分析功能
 */
@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    /**
     * 创建新商品
     * @param product 商品对象
     * @return 创建后的商品
     */
    @Transactional
    public Product createProduct(Product product) {
        return productRepository.save(product);
    }

    /**
     * 根据ID查询商品
     * @param id 商品ID
     * @return 商品对象，不存在则返回null
     */
    public Product getProductById(Long id) {
        return productRepository.findById(id).orElse(null);
    }

    /**
     * 分页查询所有商品
     * @param page 页码（从0开始）
     * @param size 每页大小
     * @return 分页结果
     */
    public Page<Product> getAllProducts(int page, int size) {
        return productRepository.findAll(PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt")));
    }

    /**
     * 更新商品信息
     * @param id 商品ID
     * @param product 更新的商品信息
     * @return 更新后的商品
     */
    @Transactional
    public Product updateProduct(Long id, Product product) {
        Product existingProduct = productRepository.findById(id).orElse(null);
        if (existingProduct != null) {
            existingProduct.setName(product.getName());
            existingProduct.setCategory(product.getCategory());
            existingProduct.setCostPrice(product.getCostPrice());
            existingProduct.setSalePrice(product.getSalePrice());
            existingProduct.setStockQuantity(product.getStockQuantity());
            existingProduct.setSalesQuantity(product.getSalesQuantity());
            existingProduct.setStatus(product.getStatus());
            return productRepository.save(existingProduct);
        }
        return null;
    }

    /**
     * 删除商品
     * @param id 商品ID
     * @return 是否删除成功
     */
    @Transactional
    public boolean deleteProduct(Long id) {
        if (productRepository.existsById(id)) {
            productRepository.deleteById(id);
            return true;
        }
        return false;
    }

    /**
     * 获取热销商品排行
     * @param limit 返回数量限制
     * @return 热销商品列表
     */
    public List<Map<String, Object>> getHotSellingProducts(int limit) {
        List<Product> products = productRepository.findAll(Sort.by(Sort.Direction.DESC, "salesQuantity"));
        List<Map<String, Object>> result = new ArrayList<>();
        
        int count = Math.min(limit, products.size());
        for (int i = 0; i < count; i++) {
            Product p = products.get(i);
            Map<String, Object> map = new HashMap<>();
            map.put("id", p.getId());
            map.put("name", p.getName());
            map.put("category", p.getCategory());
            map.put("salesQuantity", p.getSalesQuantity());
            map.put("salePrice", p.getSalePrice());
            map.put("grossMargin", calculateGrossMargin(p));
            result.add(map);
        }
        return result;
    }

    /**
     * 获取滞销商品分析
     * @param limit 返回数量限制
     * @return 滞销商品列表
     */
    public List<Map<String, Object>> getUnsoldProducts(int limit) {
        List<Product> products = productRepository.findUnsoldProducts("active");
        List<Map<String, Object>> result = new ArrayList<>();
        
        int count = Math.min(limit, products.size());
        for (int i = 0; i < count; i++) {
            Product p = products.get(i);
            Map<String, Object> map = new HashMap<>();
            map.put("id", p.getId());
            map.put("name", p.getName());
            map.put("category", p.getCategory());
            map.put("stockQuantity", p.getStockQuantity());
            map.put("costPrice", p.getCostPrice());
            map.put("salePrice", p.getSalePrice());
            map.put("inventoryValue", p.getCostPrice().multiply(new BigDecimal(p.getStockQuantity())));
            result.add(map);
        }
        return result;
    }

    /**
     * 获取毛利率分析
     * @return 毛利率统计数据
     */
    public Map<String, Object> getGrossMarginAnalysis() {
        List<Product> products = productRepository.findAll();
        Map<String, Object> result = new HashMap<>();
        List<Map<String, Object>> byCategory = new ArrayList<>();
        
        Map<String, List<Product>> categoryMap = new HashMap<>();
        for (Product p : products) {
            categoryMap.computeIfAbsent(p.getCategory(), k -> new ArrayList<>()).add(p);
        }
        
        BigDecimal totalRevenue = BigDecimal.ZERO;
        BigDecimal totalCost = BigDecimal.ZERO;
        
        for (Map.Entry<String, List<Product>> entry : categoryMap.entrySet()) {
            String category = entry.getKey();
            List<Product> categoryProducts = entry.getValue();
            
            BigDecimal categoryRevenue = BigDecimal.ZERO;
            BigDecimal categoryCost = BigDecimal.ZERO;
            
            for (Product p : categoryProducts) {
                BigDecimal quantity = new BigDecimal(p.getSalesQuantity());
                categoryRevenue = categoryRevenue.add(p.getSalePrice().multiply(quantity));
                categoryCost = categoryCost.add(p.getCostPrice().multiply(quantity));
            }
            
            totalRevenue = totalRevenue.add(categoryRevenue);
            totalCost = totalCost.add(categoryCost);
            
            Map<String, Object> catMap = new HashMap<>();
            catMap.put("category", category);
            catMap.put("revenue", categoryRevenue);
            catMap.put("cost", categoryCost);
            catMap.put("profit", categoryRevenue.subtract(categoryCost));
            if (categoryRevenue.compareTo(BigDecimal.ZERO) > 0) {
                BigDecimal margin = categoryRevenue.subtract(categoryCost)
                    .multiply(new BigDecimal(100))
                    .divide(categoryRevenue, 2, RoundingMode.HALF_UP);
                catMap.put("grossMargin", margin);
            } else {
                catMap.put("grossMargin", BigDecimal.ZERO);
            }
            byCategory.add(catMap);
        }
        
        result.put("byCategory", byCategory);
        result.put("totalRevenue", totalRevenue);
        result.put("totalCost", totalCost);
        result.put("totalProfit", totalRevenue.subtract(totalCost));
        if (totalRevenue.compareTo(BigDecimal.ZERO) > 0) {
            BigDecimal overallMargin = totalRevenue.subtract(totalCost)
                .multiply(new BigDecimal(100))
                .divide(totalRevenue, 2, RoundingMode.HALF_UP);
            result.put("overallGrossMargin", overallMargin);
        } else {
            result.put("overallGrossMargin", BigDecimal.ZERO);
        }
        
        return result;
    }

    /**
     * 计算单个商品的毛利率
     * @param product 商品对象
     * @return 毛利率百分比
     */
    private BigDecimal calculateGrossMargin(Product product) {
        if (product.getSalePrice().compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO;
        }
        return product.getSalePrice().subtract(product.getCostPrice())
            .multiply(new BigDecimal(100))
            .divide(product.getSalePrice(), 2, RoundingMode.HALF_UP);
    }
}
