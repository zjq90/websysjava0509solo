package com.platform.management.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.platform.management.dto.InvoiceAuditDTO;
import com.platform.management.dto.InvoiceIssueDTO;
import com.platform.management.entity.InvoiceApplication;
import com.platform.management.mapper.InvoiceApplicationMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 发票管理Service
 * 
 * @author platform
 * @version 1.0.0
 */
@Service
public class InvoiceService extends ServiceImpl<InvoiceApplicationMapper, InvoiceApplication> {

    /**
     * 分页查询发票申请列表
     * 
     * @param pageNum 页码
     * @param pageSize 每页条数
     * @param orderNo 订单号（可选）
     * @param userName 用户名称（可选）
     * @param invoiceType 发票类型（可选）
     * @param status 状态（可选）
     * @return 分页结果
     */
    public Page<InvoiceApplication> getPage(Integer pageNum, Integer pageSize, String orderNo,
                                             String userName, String invoiceType, String status) {
        Page<InvoiceApplication> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<InvoiceApplication> wrapper = new LambdaQueryWrapper<>();
        
        if (StringUtils.hasText(orderNo)) {
            wrapper.like(InvoiceApplication::getOrderNo, orderNo);
        }
        if (StringUtils.hasText(userName)) {
            wrapper.like(InvoiceApplication::getUserName, userName);
        }
        if (StringUtils.hasText(invoiceType)) {
            wrapper.eq(InvoiceApplication::getInvoiceType, invoiceType);
        }
        if (StringUtils.hasText(status)) {
            wrapper.eq(InvoiceApplication::getStatus, status);
        }
        wrapper.orderByDesc(InvoiceApplication::getCreateTime);
        
        return this.page(page, wrapper);
    }

    /**
     * 获取发票申请详情
     * 
     * @param id 发票申请ID
     * @return 发票申请详情
     */
    public InvoiceApplication getDetail(Long id) {
        return this.getById(id);
    }

    /**
     * 创建发票申请
     * 
     * @param invoice 发票申请信息
     * @return 是否成功
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean createInvoice(InvoiceApplication invoice) {
        invoice.setStatus("PENDING");
        return this.save(invoice);
    }

    /**
     * 审核发票申请
     * 
     * @param dto 审核参数
     * @return 是否成功
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean auditInvoice(InvoiceAuditDTO dto) {
        InvoiceApplication invoice = this.getById(dto.getId());
        if (invoice == null) {
            throw new RuntimeException("发票申请不存在");
        }
        
        if (!"PENDING".equals(invoice.getStatus())) {
            throw new RuntimeException("该发票申请已处理");
        }
        
        if ("APPROVED".equals(dto.getResult())) {
            invoice.setStatus("APPROVED");
        } else if ("REJECTED".equals(dto.getResult())) {
            if (!StringUtils.hasText(dto.getRejectReason())) {
                throw new RuntimeException("拒绝原因不能为空");
            }
            invoice.setStatus("REJECTED");
            invoice.setRejectReason(dto.getRejectReason());
        } else {
            throw new RuntimeException("无效的审核结果");
        }
        
        invoice.setReviewer(StringUtils.hasText(dto.getReviewer()) ? dto.getReviewer() : "财务管理员");
        invoice.setUpdateTime(LocalDateTime.now());
        
        return this.updateById(invoice);
    }

    /**
     * 开具发票
     * 
     * @param dto 开具参数
     * @return 是否成功
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean issueInvoice(InvoiceIssueDTO dto) {
        InvoiceApplication invoice = this.getById(dto.getId());
        if (invoice == null) {
            throw new RuntimeException("发票申请不存在");
        }
        
        if (!"APPROVED".equals(invoice.getStatus())) {
            throw new RuntimeException("只能对已审核通过的申请开具发票");
        }
        
        invoice.setStatus("ISSUED");
        invoice.setInvoiceNo(dto.getInvoiceNo());
        invoice.setInvoiceCode(dto.getInvoiceCode());
        invoice.setIssueTime(LocalDateTime.now());
        invoice.setReviewer(StringUtils.hasText(dto.getReviewer()) ? dto.getReviewer() : "财务管理员");
        invoice.setUpdateTime(LocalDateTime.now());
        
        return this.updateById(invoice);
    }

    /**
     * 删除发票申请
     * 
     * @param id 发票申请ID
     * @return 是否成功
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteInvoice(Long id) {
        return this.removeById(id);
    }

    /**
     * 批量删除发票申请
     * 
     * @param ids ID列表
     * @return 是否成功
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean batchDelete(List<Long> ids) {
        return this.removeByIds(ids);
    }
}
