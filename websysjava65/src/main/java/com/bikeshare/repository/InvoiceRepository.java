package com.bikeshare.repository;

import com.bikeshare.entity.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 发票数据访问层
 *
 * @author BikeShare Team
 * @version 1.0.0
 */
@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, Long> {

    List<Invoice> findByUserId(Long userId);

    List<Invoice> findByStatus(String status);
}
