package com.personal.accounting.service;

import com.personal.accounting.dto.NetWorthDTO;
import com.personal.accounting.entity.Asset;
import com.personal.accounting.entity.Debt;
import com.personal.accounting.entity.Investment;
import com.personal.accounting.repository.AssetRepository;
import com.personal.accounting.repository.DebtRepository;
import com.personal.accounting.repository.InvestmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class NetWorthService {

    private final AssetRepository assetRepository;
    private final DebtRepository debtRepository;
    private final InvestmentRepository investmentRepository;

    public NetWorthDTO calculateNetWorth(Long userId) {
        NetWorthDTO dto = new NetWorthDTO();

        List<Asset> assets = assetRepository.findByUserId(userId);
        List<Debt> debts = debtRepository.findByUserIdAndIsPaidOff(userId, false);
        List<Investment> investments = investmentRepository.findByUserId(userId);

        BigDecimal totalAssets = BigDecimal.ZERO;
        BigDecimal totalLiabilities = BigDecimal.ZERO;
        BigDecimal totalInvestments = BigDecimal.ZERO;
        BigDecimal totalInvestmentProfit = BigDecimal.ZERO;

        Map<String, BigDecimal> assetBreakdown = new LinkedHashMap<>();
        for (Asset asset : assets) {
            totalAssets = totalAssets.add(asset.getCurrentValue());
            String type = asset.getAssetType().name();
            assetBreakdown.put(type, assetBreakdown.getOrDefault(type, BigDecimal.ZERO).add(asset.getCurrentValue()));
        }

        Map<String, BigDecimal> liabilityBreakdown = new LinkedHashMap<>();
        for (Debt debt : debts) {
            totalLiabilities = totalLiabilities.add(debt.getRemainingAmount());
            String type = debt.getDebtType().name();
            liabilityBreakdown.put(type, liabilityBreakdown.getOrDefault(type, BigDecimal.ZERO).add(debt.getRemainingAmount()));
        }

        for (Investment inv : investments) {
            totalInvestments = totalInvestments.add(inv.getMarketValue() != null ? inv.getMarketValue() : BigDecimal.ZERO);
            totalInvestmentProfit = totalInvestmentProfit.add(inv.getProfitLoss() != null ? inv.getProfitLoss() : BigDecimal.ZERO);
        }

        totalAssets = totalAssets.add(totalInvestments);

        dto.setTotalAssets(totalAssets);
        dto.setTotalLiabilities(totalLiabilities);
        dto.setNetWorth(totalAssets.subtract(totalLiabilities));
        dto.setAssetBreakdown(assetBreakdown);
        dto.setLiabilityBreakdown(liabilityBreakdown);
        dto.setTotalInvestments(totalInvestments);
        dto.setTotalInvestmentProfit(totalInvestmentProfit);

        return dto;
    }

    public NetWorthDTO calculateFamilyNetWorth(Long familyId, List<Long> userIds) {
        NetWorthDTO dto = new NetWorthDTO();

        List<Asset> assets = assetRepository.findByUserIdIn(userIds);
        List<Debt> debts = debtRepository.findByUserIdInAndIsPaidOffFalse(userIds);
        List<Investment> investments = investmentRepository.findByUserIdIn(userIds);

        BigDecimal totalAssets = BigDecimal.ZERO;
        BigDecimal totalLiabilities = BigDecimal.ZERO;
        BigDecimal totalInvestments = BigDecimal.ZERO;
        BigDecimal totalInvestmentProfit = BigDecimal.ZERO;

        Map<String, BigDecimal> assetBreakdown = new LinkedHashMap<>();
        for (Asset asset : assets) {
            totalAssets = totalAssets.add(asset.getCurrentValue());
            String type = asset.getAssetType().name();
            assetBreakdown.put(type, assetBreakdown.getOrDefault(type, BigDecimal.ZERO).add(asset.getCurrentValue()));
        }

        Map<String, BigDecimal> liabilityBreakdown = new LinkedHashMap<>();
        for (Debt debt : debts) {
            totalLiabilities = totalLiabilities.add(debt.getRemainingAmount());
            String type = debt.getDebtType().name();
            liabilityBreakdown.put(type, liabilityBreakdown.getOrDefault(type, BigDecimal.ZERO).add(debt.getRemainingAmount()));
        }

        for (Investment inv : investments) {
            totalInvestments = totalInvestments.add(inv.getMarketValue() != null ? inv.getMarketValue() : BigDecimal.ZERO);
            totalInvestmentProfit = totalInvestmentProfit.add(inv.getProfitLoss() != null ? inv.getProfitLoss() : BigDecimal.ZERO);
        }

        totalAssets = totalAssets.add(totalInvestments);

        dto.setTotalAssets(totalAssets);
        dto.setTotalLiabilities(totalLiabilities);
        dto.setNetWorth(totalAssets.subtract(totalLiabilities));
        dto.setAssetBreakdown(assetBreakdown);
        dto.setLiabilityBreakdown(liabilityBreakdown);
        dto.setTotalInvestments(totalInvestments);
        dto.setTotalInvestmentProfit(totalInvestmentProfit);

        return dto;
    }
}
