package com.bikeshare.service;

import com.bikeshare.dto.FinanceDTO;
import com.bikeshare.entity.CostRecord;
import com.bikeshare.entity.Invoice;
import com.bikeshare.entity.PaymentRecord;
import com.bikeshare.entity.Reconciliation;
import com.bikeshare.repository.CostRecordRepository;
import com.bikeshare.repository.InvoiceRepository;
import com.bikeshare.repository.PaymentRecordRepository;
import com.bikeshare.repository.ReconciliationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 财务管理服务
 *
 * @author BikeShare Team
 * @version 1.0.0
 */
@Service
@RequiredArgsConstructor
public class FinanceService {

    private final PaymentRecordRepository paymentRecordRepository;
    private final ReconciliationRepository reconciliationRepository;
    private final InvoiceRepository invoiceRepository;
    private final CostRecordRepository costRecordRepository;

    public List<PaymentRecord> getPaymentRecords(String paymentType) {
        if (paymentType != null && !paymentType.isEmpty()) {
            return paymentRecordRepository.findByPaymentType(paymentType);
        }
        return paymentRecordRepository.findAll();
    }

    public List<Reconciliation> getReconciliations(String status) {
        if (status != null && !status.isEmpty()) {
            return reconciliationRepository.findByStatus(status);
        }
        return reconciliationRepository.findAll();
    }

    @Transactional
    public Reconciliation doReconciliation(LocalDate reconDate, String reconType, BigDecimal actualAmount, String operator) {
        LocalDateTime startTime = reconDate.atStartOfDay();
        LocalDateTime endTime = reconDate.plusDays(1).atStartOfDay();

        List<PaymentRecord> payments = paymentRecordRepository.findByPaymentType("RIDE");
        BigDecimal systemAmount = payments.stream()
                .filter(p -> p.getCreateTime().isAfter(startTime) && p.getCreateTime().isBefore(endTime))
                .map(PaymentRecord::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal diffAmount = systemAmount.subtract(actualAmount);
        String status = diffAmount.compareTo(BigDecimal.ZERO) == 0 ? "MATCHED" : "UNMATCHED";

        Reconciliation reconciliation = new Reconciliation();
        reconciliation.setReconNo("RECON" + System.currentTimeMillis());
        reconciliation.setReconDate(reconDate);
        reconciliation.setReconType(reconType);
        reconciliation.setSystemAmount(systemAmount);
        reconciliation.setActualAmount(actualAmount);
        reconciliation.setDiffAmount(diffAmount);
        reconciliation.setStatus(status);
        reconciliation.setOperator(operator);
        reconciliation.setHandleTime(LocalDateTime.now());

        return reconciliationRepository.save(reconciliation);
    }

    public List<Invoice> getInvoices(String status) {
        if (status != null && !status.isEmpty()) {
            return invoiceRepository.findByStatus(status);
        }
        return invoiceRepository.findAll();
    }

    @Transactional
    public Invoice createInvoice(Invoice invoice) {
        invoice.setInvoiceNo("INV" + System.currentTimeMillis());
        invoice.setIssueTime(LocalDateTime.now());
        invoice.setTaxAmount(invoice.getInvoiceAmount().multiply(new BigDecimal("0.06")));
        invoice.setTotalAmount(invoice.getInvoiceAmount().add(invoice.getTaxAmount()));
        return invoiceRepository.save(invoice);
    }

    @Transactional
    public List<Invoice> batchCreateInvoices(List<Invoice> invoices) {
        List<Invoice> result = new ArrayList<>();
        for (Invoice invoice : invoices) {
            result.add(createInvoice(invoice));
        }
        return result;
    }

    public FinanceDTO.CostAnalysis getCostAnalysis(LocalDate startDate) {
        if (startDate == null) {
            startDate = LocalDate.now().withDayOfMonth(1);
        }

        FinanceDTO.CostAnalysis analysis = new FinanceDTO.CostAnalysis();
        analysis.setTotalDepreciation(BigDecimal.ZERO);
        analysis.setTotalMaintenance(BigDecimal.ZERO);
        analysis.setTotalOperation(BigDecimal.ZERO);

        List<FinanceDTO.CostCategoryDetail> categoryDetails = new ArrayList<>();
        List<Object[]> typeAmounts = costRecordRepository.sumAmountByType(startDate);
        List<Object[]> categoryAmounts = costRecordRepository.sumAmountByCategory(startDate);

        for (Object[] typeAmount : typeAmounts) {
            String type = (String) typeAmount[0];
            BigDecimal amount = (BigDecimal) typeAmount[1];

            switch (type) {
                case "DEPRECIATION":
                    analysis.setTotalDepreciation(amount);
                    break;
                case "MAINTENANCE":
                    analysis.setTotalMaintenance(amount);
                    break;
                case "OPERATION":
                    analysis.setTotalOperation(amount);
                    break;
            }
        }

        for (Object[] categoryAmount : categoryAmounts) {
            FinanceDTO.CostCategoryDetail detail = new FinanceDTO.CostCategoryDetail();
            detail.setCategory((String) categoryAmount[0]);
            detail.setAmount((BigDecimal) categoryAmount[1]);
            categoryDetails.add(detail);
        }

        analysis.setCategoryDetails(categoryDetails);
        return analysis;
    }

    public List<CostRecord> getCostRecords(String costType) {
        if (costType != null && !costType.isEmpty()) {
            return costRecordRepository.findByCostType(costType);
        }
        return costRecordRepository.findAll();
    }
}
