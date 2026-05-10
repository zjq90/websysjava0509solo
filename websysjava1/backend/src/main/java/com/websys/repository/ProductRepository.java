package com.websys.repository;

import com.websys.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;

/**
 * 商品数据访问接口
 * 提供商品的增删改查操作
 */
@Repository
public interface ProductRepository extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product> {

    /**
     * 根据商品编码查找商品
     * @param productCode 商品编码
     * @return 商品对象
     */
    Optional<Product> findByProductCode(String productCode);

    /**
     * 判断商品编码是否存在
     * @param productCode 商品编码
     * @return 是否存在
     */
    boolean existsByProductCode(String productCode);

    /**
     * 根据商品类别查找商品列表
     * @param category 商品类别
     * @return 商品列表
     */
    List<Product> findByCategory(String category);

    /**
     * 根据商品状态查找
     * @param status 商品状态
     * @return 商品列表
     */
    List<Product> findByStatus(Integer status);
}
