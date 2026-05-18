package com.secondhand.repository;

import com.secondhand.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 订单Repository接口
 *
 * @author secondhand
 * @version 1.0.0
 */
@Repository
public interface OrderRepository extends JpaRepository<Order, Long>, JpaSpecificationExecutor<Order> {

    Optional<Order> findByOrderNoAndIsDeletedFalse(String orderNo);

    List<Order> findByUserIdAndIsDeletedFalseOrderByCreateTimeDesc(Long userId);

    Optional<Order> findByIdAndIsDeletedFalse(Long id);

}
