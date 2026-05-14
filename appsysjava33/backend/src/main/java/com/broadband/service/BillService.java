package com.broadband.service;

import com.broadband.entity.Bill;
import com.broadband.repository.BillRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 账单服务
 * 处理账单管理、支付等功能
 * 
 * @author broadband
 * @version 1.0.0
 */
@Service
public class BillService {

    @Autowired
    private BillRepository billRepository;

    public List<Bill> getUserBills(Long userId) {
        return billRepository.findByUserIdOrderByCreateTimeDesc(userId);
    }

    public List<Bill> getUserBillsByStatus(Long userId, Integer status) {
        return billRepository.findByUserIdAndStatusOrderByCreateTimeDesc(userId, status);
    }

    public Bill getBillById(Long billId) {
        return billRepository.findById(billId).orElseThrow(() -> new RuntimeException("账单不存在"));
    }

    public Bill createBill(Long userId, Long userPackageId, String broadbandNumber,
                            BigDecimal packageFee, String billPeriod) {
        Bill bill = new Bill();
        bill.setBillNo("B" + System.currentTimeMillis());
        bill.setUserId(userId);
        bill.setUserPackageId(userPackageId);
        bill.setBroadbandNumber(broadbandNumber);
        bill.setBillPeriod(billPeriod);
        bill.setStartDate(LocalDate.now().withDayOfMonth(1));
        bill.setEndDate(LocalDate.now().withDayOfMonth(LocalDate.now().lengthOfMonth()));
        bill.setPackageFee(packageFee);
        bill.setExtraFee(BigDecimal.ZERO);
        bill.setOtherFee(BigDecimal.ZERO);
        bill.setDiscountFee(BigDecimal.ZERO);
        bill.setTotalAmount(packageFee);
        bill.setPaidAmount(BigDecimal.ZERO);
        bill.setUnpaidAmount(packageFee);
        bill.setDueDate(LocalDate.now().plusMonths(1).withDayOfMonth(10));
        bill.setStatus(0);
        bill.setCreateTime(LocalDateTime.now());
        bill.setUpdateTime(LocalDateTime.now());
        return billRepository.save(bill);
    }

    public Bill payBill(Long billId, BigDecimal amount) {
        Bill bill = getBillById(billId);
        if (bill.getStatus() != 0) {
            throw new RuntimeException("账单状态不正确");
        }
        bill.setPaidAmount(bill.getPaidAmount().add(amount));
        bill.setUnpaidAmount(bill.getUnpaidAmount().subtract(amount));
        if (bill.getUnpaidAmount().compareTo(BigDecimal.ZERO) <= 0) {
            bill.setStatus(1);
            bill.setPayTime(LocalDateTime.now());
        }
        bill.setUpdateTime(LocalDateTime.now());
        return billRepository.save(bill);
    }
}
