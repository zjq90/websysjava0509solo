package com.personal.accounting.service;

import com.personal.accounting.entity.Investment;
import com.personal.accounting.enums.InvestmentType;
import com.personal.accounting.repository.InvestmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class InvestmentService {

    private final InvestmentRepository investmentRepository;

    public List<Investment> getUserInvestments(Long userId) {
        return investmentRepository.findByUserId(userId);
    }

    public List<Investment> getUserInvestmentsByType(Long userId, InvestmentType type) {
        return investmentRepository.findByUserIdAndInvestmentType(userId, type);
    }

    public Investment getInvestmentById(Long id) {
        return investmentRepository.findById(id).orElseThrow(() -> new RuntimeException("投资不存在"));
    }

    @Transactional
    public Investment createInvestment(Investment investment) {
        return investmentRepository.save(investment);
    }

    @Transactional
    public Investment updateInvestment(Long id, Investment investment) {
        Investment existing = getInvestmentById(id);
        existing.setInvestmentType(investment.getInvestmentType());
        existing.setInvestmentName(investment.getInvestmentName());
        existing.setCode(investment.getCode());
        existing.setQuantity(investment.getQuantity());
        existing.setCostPrice(investment.getCostPrice());
        existing.setCurrentPrice(investment.getCurrentPrice());
        existing.setNotes(investment.getNotes());
        existing.setPurchaseDate(investment.getPurchaseDate());
        return investmentRepository.save(existing);
    }

    @Transactional
    public void deleteInvestment(Long id) {
        investmentRepository.deleteById(id);
    }

    public BigDecimal getTotalInvestmentValue(Long userId) {
        return investmentRepository.getTotalInvestmentValueByUserId(userId);
    }

    public BigDecimal getTotalInvestmentProfit(Long userId) {
        return investmentRepository.getTotalInvestmentProfitByUserId(userId);
    }
}
