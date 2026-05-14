package com.broadband.repository;

import com.broadband.entity.Bill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 账单数据访问层
 * 
 * @author broadband
 * @version 1.0.0
 */
@Repository
public interface BillRepository extends JpaRepository<Bill, Long> {

    List<Bill> findByUserIdOrderByCreateTimeDesc(Long userId);

    List<Bill> findByUserIdAndStatusOrderByCreateTimeDesc(Long userId, Integer status);

    Bill findByBillNo(String billNo);
}
