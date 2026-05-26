package com.personal.accounting.service;

import com.personal.accounting.dto.RepaymentPlanDTO;
import com.personal.accounting.dto.RepaymentSimulationDTO;
import com.personal.accounting.entity.Debt;
import com.personal.accounting.entity.DebtPayment;
import com.personal.accounting.entity.Notification;
import com.personal.accounting.enums.RepaymentMethod;
import com.personal.accounting.repository.DebtPaymentRepository;
import com.personal.accounting.repository.DebtRepository;
import com.personal.accounting.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DebtService {

    private final DebtRepository debtRepository;
    private final DebtPaymentRepository debtPaymentRepository;
    private final NotificationRepository notificationRepository;

    public List<Debt> getUserDebts(Long userId) {
        return debtRepository.findByUserId(userId);
    }

    public List<Debt> getUserActiveDebts(Long userId) {
        return debtRepository.findByUserIdAndIsPaidOff(userId, false);
    }

    public Debt getDebtById(Long id) {
        return debtRepository.findById(id).orElseThrow(() -> new RuntimeException("债务不存在"));
    }

    @Transactional
    public Debt createDebt(Debt debt) {
        if (debt.getRemainingAmount() == null) {
            debt.setRemainingAmount(debt.getPrincipalAmount());
        }
        if (debt.getTotalInterestPaid() == null) {
            debt.setTotalInterestPaid(BigDecimal.ZERO);
        }
        calculateMonthlyPayment(debt);
        return debtRepository.save(debt);
    }

    @Transactional
    public Debt updateDebt(Long id, Debt debt) {
        Debt existingDebt = getDebtById(id);
        existingDebt.setDebtType(debt.getDebtType());
        existingDebt.setDebtName(debt.getDebtName());
        existingDebt.setCreditor(debt.getCreditor());
        existingDebt.setPrincipalAmount(debt.getPrincipalAmount());
        existingDebt.setRemainingAmount(debt.getRemainingAmount());
        existingDebt.setAnnualInterestRate(debt.getAnnualInterestRate());
        existingDebt.setRepaymentMethod(debt.getRepaymentMethod());
        existingDebt.setLoanTermMonths(debt.getLoanTermMonths());
        existingDebt.setStartDate(debt.getStartDate());
        existingDebt.setEndDate(debt.getEndDate());
        existingDebt.setNextPaymentDate(debt.getNextPaymentDate());
        existingDebt.setPaymentDay(debt.getPaymentDay());
        existingDebt.setNotes(debt.getNotes());
        existingDebt.setIsPaidOff(debt.getIsPaidOff());
        calculateMonthlyPayment(existingDebt);
        return debtRepository.save(existingDebt);
    }

    @Transactional
    public void deleteDebt(Long id) {
        debtRepository.deleteById(id);
    }

    @Transactional
    public DebtPayment makePayment(Long debtId, DebtPayment payment) {
        Debt debt = getDebtById(debtId);
        payment.setDebtId(debtId);
        payment.setUserId(debt.getUserId());
        
        calculatePaymentParts(debt, payment);
        
        debt.setRemainingAmount(debt.getRemainingAmount().subtract(payment.getPrincipalPart()));
        debt.setTotalInterestPaid(debt.getTotalInterestPaid().add(payment.getInterestPart()));
        
        if (debt.getRemainingAmount().compareTo(BigDecimal.ZERO) <= 0) {
            debt.setRemainingAmount(BigDecimal.ZERO);
            debt.setIsPaidOff(true);
        }
        
        updateNextPaymentDate(debt);
        debtRepository.save(debt);
        
        return debtPaymentRepository.save(payment);
    }

    public List<DebtPayment> getDebtPayments(Long debtId) {
        return debtPaymentRepository.findByDebtIdOrderByPaymentDateDesc(debtId);
    }

    public RepaymentSimulationDTO simulateRepayment(RepaymentSimulationDTO simulation) {
        BigDecimal principal = simulation.getPrincipal();
        BigDecimal monthlyRate = simulation.getAnnualInterestRate()
                .divide(new BigDecimal("12"), 10, RoundingMode.HALF_UP)
                .divide(new BigDecimal("100"), 10, RoundingMode.HALF_UP);
        int months = simulation.getLoanTermMonths();
        RepaymentMethod method = simulation.getRepaymentMethod();

        List<RepaymentPlanDTO> plans = new ArrayList<>();
        BigDecimal totalPayment = BigDecimal.ZERO;
        BigDecimal totalInterest = BigDecimal.ZERO;
        BigDecimal remainingPrincipal = principal;
        LocalDate paymentDate = LocalDate.now().plusMonths(1);

        if (method == RepaymentMethod.EQUAL_PRINCIPAL_INTEREST) {
            BigDecimal monthlyPayment = calculateEqualPrincipalInterestPayment(principal, monthlyRate, months);
            
            for (int i = 1; i <= months; i++) {
                RepaymentPlanDTO plan = new RepaymentPlanDTO();
                plan.setPeriod(i);
                plan.setPaymentDate(paymentDate.plusMonths(i - 1));
                plan.setPaymentAmount(monthlyPayment);
                
                BigDecimal interestPart = remainingPrincipal.multiply(monthlyRate)
                        .setScale(2, RoundingMode.HALF_UP);
                BigDecimal principalPart = monthlyPayment.subtract(interestPart)
                        .setScale(2, RoundingMode.HALF_UP);
                
                if (i == months) {
                    principalPart = remainingPrincipal;
                    plan.setPaymentAmount(principalPart.add(interestPart));
                }
                
                plan.setPrincipalPart(principalPart);
                plan.setInterestPart(interestPart);
                remainingPrincipal = remainingPrincipal.subtract(principalPart);
                plan.setRemainingPrincipal(remainingPrincipal.max(BigDecimal.ZERO));
                
                totalPayment = totalPayment.add(plan.getPaymentAmount());
                totalInterest = totalInterest.add(interestPart);
                plans.add(plan);
            }
        } else if (method == RepaymentMethod.EQUAL_PRINCIPAL) {
            BigDecimal monthlyPrincipal = principal.divide(new BigDecimal(months), 2, RoundingMode.HALF_UP);
            
            for (int i = 1; i <= months; i++) {
                RepaymentPlanDTO plan = new RepaymentPlanDTO();
                plan.setPeriod(i);
                plan.setPaymentDate(paymentDate.plusMonths(i - 1));
                
                BigDecimal interestPart = remainingPrincipal.multiply(monthlyRate)
                        .setScale(2, RoundingMode.HALF_UP);
                BigDecimal principalPart = (i == months) ? remainingPrincipal : monthlyPrincipal;
                BigDecimal paymentAmount = principalPart.add(interestPart);
                
                plan.setPaymentAmount(paymentAmount);
                plan.setPrincipalPart(principalPart);
                plan.setInterestPart(interestPart);
                remainingPrincipal = remainingPrincipal.subtract(principalPart);
                plan.setRemainingPrincipal(remainingPrincipal.max(BigDecimal.ZERO));
                
                totalPayment = totalPayment.add(paymentAmount);
                totalInterest = totalInterest.add(interestPart);
                plans.add(plan);
            }
        }

        simulation.setRepaymentPlans(plans);
        simulation.setTotalPayment(totalPayment);
        simulation.setTotalInterest(totalInterest);
        return simulation;
    }

    private BigDecimal calculateEqualPrincipalInterestPayment(BigDecimal principal, BigDecimal monthlyRate, int months) {
        if (monthlyRate.compareTo(BigDecimal.ZERO) == 0) {
            return principal.divide(new BigDecimal(months), 2, RoundingMode.HALF_UP);
        }
        BigDecimal temp = monthlyRate.add(BigDecimal.ONE).pow(months);
        return principal.multiply(monthlyRate).multiply(temp)
                .divide(temp.subtract(BigDecimal.ONE), 2, RoundingMode.HALF_UP);
    }

    private void calculateMonthlyPayment(Debt debt) {
        if (debt.getLoanTermMonths() != null && debt.getAnnualInterestRate() != null 
                && debt.getRemainingAmount() != null) {
            RepaymentSimulationDTO sim = new RepaymentSimulationDTO();
            sim.setPrincipal(debt.getRemainingAmount());
            sim.setAnnualInterestRate(debt.getAnnualInterestRate());
            sim.setLoanTermMonths(debt.getLoanTermMonths());
            sim.setRepaymentMethod(debt.getRepaymentMethod());
            
            RepaymentSimulationDTO result = simulateRepayment(sim);
            if (!result.getRepaymentPlans().isEmpty()) {
                debt.setMonthlyPayment(result.getRepaymentPlans().get(0).getPaymentAmount());
            }
        }
    }

    private void calculatePaymentParts(Debt debt, DebtPayment payment) {
        if (payment.getPrincipalPart() == null || payment.getInterestPart() == null) {
            BigDecimal monthlyRate = debt.getAnnualInterestRate()
                    .divide(new BigDecimal("12"), 10, RoundingMode.HALF_UP)
                    .divide(new BigDecimal("100"), 10, RoundingMode.HALF_UP);
            
            BigDecimal interestPart = debt.getRemainingAmount().multiply(monthlyRate)
                    .setScale(2, RoundingMode.HALF_UP);
            BigDecimal principalPart = payment.getPaymentAmount().subtract(interestPart);
            
            if (principalPart.compareTo(debt.getRemainingAmount()) > 0) {
                principalPart = debt.getRemainingAmount();
                interestPart = payment.getPaymentAmount().subtract(principalPart);
            }
            
            payment.setPrincipalPart(principalPart);
            payment.setInterestPart(interestPart);
        }
    }

    private void updateNextPaymentDate(Debt debt) {
        if (debt.getNextPaymentDate() != null) {
            debt.setNextPaymentDate(debt.getNextPaymentDate().plusMonths(1));
        }
    }

    @Scheduled(cron = "0 0 9 * * ?")
    @Transactional
    public void sendPaymentReminders() {
        LocalDate today = LocalDate.now();
        LocalDate reminderDate = today.plusDays(3);
        List<Debt> debts = debtRepository.findByNextPaymentDateBetweenAndIsPaidOffFalse(today, reminderDate);
        
        for (Debt debt : debts) {
            long daysUntil = java.time.temporal.ChronoUnit.DAYS.between(today, debt.getNextPaymentDate());
            
            Notification notification = new Notification();
            notification.setUserId(debt.getUserId());
            notification.setTitle("还款提醒");
            notification.setContent(String.format("您的%s（%s）将于%d天后（%s）到期，应还金额：%.2f元",
                    debt.getDebtName(), debt.getDebtType(), daysUntil, debt.getNextPaymentDate(), debt.getMonthlyPayment()));
            notification.setType("PAYMENT_REMINDER");
            notification.setRelatedId(debt.getId());
            notification.setScheduledAt(LocalDateTime.now());
            
            notificationRepository.save(notification);
        }
    }

    public List<Debt> getDebtsForReminder() {
        LocalDate today = LocalDate.now();
        LocalDate reminderDate = today.plusDays(3);
        return debtRepository.findByNextPaymentDateBetweenAndIsPaidOffFalse(today, reminderDate);
    }
}
