package com.platform.management.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.platform.management.dto.ExceptionOrderHandleDTO;
import com.platform.management.entity.ExceptionOrder;
import com.platform.management.entity.Order;
import com.platform.management.mapper.ExceptionOrderMapper;
import com.platform.management.mapper.OrderMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 异常订单Service
 * 
 * @author platform
 * @version 1.0.0
 */
@Service
public class ExceptionOrderService extends ServiceImpl<ExceptionOrderMapper, ExceptionOrder> {

    @Autowired
    private OrderMapper orderMapper;

    /**
     * 分页查询异常订单列表
     * 
     * @param pageNum 页码
     * @param pageSize 每页条数
     * @param orderNo 订单号（可选）
     * @param exceptionType 异常类型（可选）
     * @param handleStatus 处理状态（可选）
     * @return 分页结果
     */
    public Page<ExceptionOrder> getPage(Integer pageNum, Integer pageSize, String orderNo, 
                                         String exceptionType, String handleStatus) {
        Page<ExceptionOrder> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<ExceptionOrder> wrapper = new LambdaQueryWrapper<>();
        
        if (StringUtils.hasText(orderNo)) {
            wrapper.like(ExceptionOrder::getOrderNo, orderNo);
        }
        if (StringUtils.hasText(exceptionType)) {
            wrapper.eq(ExceptionOrder::getExceptionType, exceptionType);
        }
        if (StringUtils.hasText(handleStatus)) {
            wrapper.eq(ExceptionOrder::getHandleStatus, handleStatus);
        }
        wrapper.orderByDesc(ExceptionOrder::getCreateTime);
        
        Page<ExceptionOrder> result = this.page(page, wrapper);
        
        for (ExceptionOrder exceptionOrder : result.getRecords()) {
            Order order = orderMapper.selectById(exceptionOrder.getOrderId());
            exceptionOrder.setOrder(order);
        }
        
        return result;
    }

    /**
     * 获取异常订单详情
     * 
     * @param id 异常订单ID
     * @return 异常订单详情
     */
    public ExceptionOrder getDetail(Long id) {
        ExceptionOrder exceptionOrder = this.getById(id);
        if (exceptionOrder != null) {
            Order order = orderMapper.selectById(exceptionOrder.getOrderId());
            exceptionOrder.setOrder(order);
        }
        return exceptionOrder;
    }

    /**
     * 处理异常订单
     * 
     * @param dto 处理参数
     * @return 处理结果
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean handleException(ExceptionOrderHandleDTO dto) {
        ExceptionOrder exceptionOrder = this.getById(dto.getId());
        if (exceptionOrder == null) {
            throw new RuntimeException("异常订单不存在");
        }
        
        exceptionOrder.setHandleType(dto.getHandleType());
        exceptionOrder.setHandleRemark(dto.getHandleRemark());
        exceptionOrder.setHandleStatus("RESOLVED");
        exceptionOrder.setHandleTime(LocalDateTime.now());
        exceptionOrder.setHandler(StringUtils.hasText(dto.getHandler()) ? dto.getHandler() : "系统管理员");
        
        boolean updated = this.updateById(exceptionOrder);
        
        if (updated) {
            Order order = orderMapper.selectById(exceptionOrder.getOrderId());
            if (order != null) {
                if ("REFUND".equals(dto.getHandleType())) {
                    order.setPayStatus("REFUNDED");
                } else if ("RESHIP".equals(dto.getHandleType())) {
                    order.setShipStatus("SHIPPED");
                }
                order.setOrderStatus("NORMAL");
                orderMapper.updateById(order);
            }
        }
        
        return updated;
    }

    /**
     * 创建异常订单
     * 
     * @param exceptionOrder 异常订单信息
     * @return 是否成功
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean createExceptionOrder(ExceptionOrder exceptionOrder) {
        exceptionOrder.setHandleStatus("PENDING");
        boolean saved = this.save(exceptionOrder);
        
        if (saved) {
            Order order = orderMapper.selectById(exceptionOrder.getOrderId());
            if (order != null) {
                order.setOrderStatus("EXCEPTION");
                orderMapper.updateById(order);
            }
        }
        
        return saved;
    }

    /**
     * 删除异常订单
     * 
     * @param id 异常订单ID
     * @return 是否成功
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteExceptionOrder(Long id) {
        ExceptionOrder exceptionOrder = this.getById(id);
        if (exceptionOrder != null) {
            Order order = orderMapper.selectById(exceptionOrder.getOrderId());
            if (order != null) {
                order.setOrderStatus("NORMAL");
                orderMapper.updateById(order);
            }
        }
        return this.removeById(id);
    }

    /**
     * 批量删除异常订单
     * 
     * @param ids ID列表
     * @return 是否成功
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean batchDelete(List<Long> ids) {
        for (Long id : ids) {
            deleteExceptionOrder(id);
        }
        return true;
    }
}
