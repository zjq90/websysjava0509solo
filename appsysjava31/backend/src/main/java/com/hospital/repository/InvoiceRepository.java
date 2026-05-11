package com.hospital.repository;

import com.hospital.entity.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 票据Repository接口
 * 提供票据数据访问层的基本操作
 * 
 * @author hospital
 * @version 1.0.0
 */
@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, Long>, JpaSpecificationExecutor<Invoice> {

    /**
     * 根据票据编号查询
     * 
     * @param invoiceNo 票据编号
     * @return 票据对象
     */
    Optional<Invoice> findByInvoiceNo(String invoiceNo);

    /**
     * 查询患者的票据列表
     * 
     * @param patientId 患者ID
     * @return 票据列表
     */
    List<Invoice> findByPatientIdOrderByInvoiceDateDesc(Long patientId);

    /**
     * 按业务ID查询票据
     * 
     * @param businessId 业务ID
     * @param businessType 业务类型
     * @return 票据列表
     */
    List<Invoice> findByBusinessIdAndBusinessType(Long businessId, String businessType);
}
