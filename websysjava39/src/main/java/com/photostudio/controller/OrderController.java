package com.photostudio.controller;

import com.photostudio.common.Result;
import com.photostudio.entity.Order;
import com.photostudio.exception.ResourceNotFoundException;
import com.photostudio.repository.OrderRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 订单控制器
 * 提供订单的增删改查API接口
 * 
 * @author Photo Studio Team
 * @version 1.0.0
 */
@RestController
@RequestMapping("/api/orders")
@CrossOrigin(origins = "*")
@Tag(name = "订单管理", description = "订单CRUD API")
public class OrderController {

    @Autowired
    private OrderRepository orderRepository;

    /**
     * 获取所有订单
     */
    @GetMapping
    @Operation(summary = "获取所有订单", description = "获取所有订单列表")
    public Result<List<Order>> getAllOrders() {
        return Result.success(orderRepository.findAll());
    }

    /**
     * 根据ID获取订单
     */
    @GetMapping("/{id}")
    @Operation(summary = "获取订单详情", description = "根据ID获取订单详情")
    public Result<Order> getOrderById(@PathVariable Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("订单不存在: " + id));
        return Result.success(order);
    }

    /**
     * 创建订单
     */
    @PostMapping
    @Operation(summary = "创建订单", description = "创建新的订单")
    public Result<Order> createOrder(@RequestBody Order order) {
        return Result.success(orderRepository.save(order));
    }

    /**
     * 更新订单
     */
    @PutMapping("/{id}")
    @Operation(summary = "更新订单", description = "根据ID更新订单信息")
    public Result<Order> updateOrder(@PathVariable Long id, @RequestBody Order orderDetails) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("订单不存在: " + id));
        
        order.setOrderNo(orderDetails.getOrderNo());
        order.setCustomer(orderDetails.getCustomer());
        order.setAPackage(orderDetails.getAPackage());
        order.setStore(orderDetails.getStore());
        order.setSales(orderDetails.getSales());
        order.setPhotographer(orderDetails.getPhotographer());
        order.setMakeupArtist(orderDetails.getMakeupArtist());
        order.setPhotoSelector(orderDetails.getPhotoSelector());
        order.setPhotoEditor(orderDetails.getPhotoEditor());
        order.setStatus(orderDetails.getStatus());
        order.setAmount(orderDetails.getAmount());
        order.setDepositAmount(orderDetails.getDepositAmount());
        order.setBalanceAmount(orderDetails.getBalanceAmount());
        order.setShootDate(orderDetails.getShootDate());
        order.setDeliverDate(orderDetails.getDeliverDate());
        order.setCustomerRating(orderDetails.getCustomerRating());
        order.setCustomerFeedback(orderDetails.getCustomerFeedback());
        
        return Result.success(orderRepository.save(order));
    }

    /**
     * 删除订单
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "删除订单", description = "根据ID删除订单")
    public Result<Void> deleteOrder(@PathVariable Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("订单不存在: " + id));
        
        orderRepository.delete(order);
        return Result.success();
    }
}
