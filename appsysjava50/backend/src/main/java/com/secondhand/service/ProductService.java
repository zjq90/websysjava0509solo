package com.secondhand.service;

import com.secondhand.entity.Product;
import com.secondhand.entity.User;
import com.secondhand.repository.ProductRepository;
import com.secondhand.repository.UserRepository;
import com.secondhand.util.DistanceUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 商品服务类
 *
 * @author secondhand
 * @version 1.0.0
 */
@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;

    /**
     * 创建商品
     */
    public Product createProduct(Product product) {
        product.setViewCount(0);
        product.setLikeCount(0);
        product.setStatus("ON_SALE");
        return productRepository.save(product);
    }

    /**
     * 获取商品详情
     */
    public Product getProductById(Long id, Double userLat, Double userLon) {
        Product product = productRepository.findByIdAndIsDeletedFalse(id).orElse(null);
        if (product != null) {
            product.setViewCount(product.getViewCount() + 1);
            productRepository.save(product);

            if (userLat != null && userLon != null && product.getLatitude() != null && product.getLongitude() != null) {
                double distance = DistanceUtil.calculateDistance(userLat, userLon, product.getLatitude(), product.getLongitude());
                product.setDistance(distance);
            }

            User seller = userRepository.findByIdAndIsDeletedFalse(product.getUserId()).orElse(null);
            product.setSeller(seller);
        }
        return product;
    }

    /**
     * 更新商品
     */
    public Product updateProduct(Long id, Product product) {
        Product existProduct = productRepository.findByIdAndIsDeletedFalse(id).orElse(null);
        if (existProduct == null) {
            throw new RuntimeException("商品不存在");
        }

        if (product.getTitle() != null) existProduct.setTitle(product.getTitle());
        if (product.getDescription() != null) existProduct.setDescription(product.getDescription());
        if (product.getPrice() != null) existProduct.setPrice(product.getPrice());
        if (product.getCategory() != null) existProduct.setCategory(product.getCategory());
        if (product.getCondition() != null) existProduct.setCondition(product.getCondition());
        if (product.getImages() != null) existProduct.setImages(product.getImages());
        if (product.getLatitude() != null) existProduct.setLatitude(product.getLatitude());
        if (product.getLongitude() != null) existProduct.setLongitude(product.getLongitude());
        if (product.getAddress() != null) existProduct.setAddress(product.getAddress());
        if (product.getStatus() != null) existProduct.setStatus(product.getStatus());

        return productRepository.save(existProduct);
    }

    /**
     * 删除商品
     */
    public void deleteProduct(Long id) {
        Product product = productRepository.findByIdAndIsDeletedFalse(id).orElse(null);
        if (product == null) {
            throw new RuntimeException("商品不存在");
        }
        product.setIsDeleted(true);
        productRepository.save(product);
    }

    /**
     * 获取附近商品列表
     */
    public List<Product> getNearbyProducts(Double lat, Double lon, Double maxDistance) {
        List<Product> products = productRepository.findByStatusAndIsDeletedFalseOrderByCreateTimeDesc("ON_SALE");

        if (lat == null || lon == null) {
            return products;
        }

        return products.stream()
                .filter(p -> p.getLatitude() != null && p.getLongitude() != null)
                .peek(p -> {
                    double distance = DistanceUtil.calculateDistance(lat, lon, p.getLatitude(), p.getLongitude());
                    p.setDistance(distance);
                    User seller = userRepository.findByIdAndIsDeletedFalse(p.getUserId()).orElse(null);
                    p.setSeller(seller);
                })
                .filter(p -> maxDistance == null || p.getDistance() <= maxDistance)
                .sorted(Comparator.comparingDouble(Product::getDistance))
                .collect(Collectors.toList());
    }

    /**
     * 获取用户的商品列表
     */
    public List<Product> getUserProducts(Long userId) {
        return productRepository.findByUserIdAndIsDeletedFalseOrderByCreateTimeDesc(userId);
    }

    /**
     * 获取最新商品
     */
    public List<Product> getLatestProducts() {
        return productRepository.findLatestProducts();
    }

    /**
     * 获取热门商品
     */
    public List<Product> getHotProducts() {
        return productRepository.findHotProducts();
    }

}
