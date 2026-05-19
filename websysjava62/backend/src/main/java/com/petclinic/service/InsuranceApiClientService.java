package com.petclinic.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.petclinic.dto.ClaimCalculateResult;
import com.petclinic.dto.ClaimRequest;
import com.petclinic.dto.ClaimResponse;
import com.petclinic.entity.InsuranceClaim;
import com.petclinic.entity.InsuranceCompany;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * 保险公司API客户端服务
 * 模拟对接各大保险公司的理赔接口
 * 
 * @author Pet Clinic Team
 * @version 1.0.0
 */
@Slf4j
@Service
public class InsuranceApiClientService {

    /**
     * 模拟提交理赔申请到保险公司
     * @param company 保险公司配置
     * @param request 理赔请求
     * @param claim 理赔记录
     * @return 理赔响应
     */
    public ClaimResponse submitClaim(InsuranceCompany company, ClaimRequest request, InsuranceClaim claim) {
        log.info("正在向保险公司[{}]提交理赔申请，保单号：{}", company.getCompanyName(), request.getPolicyNo());

        // 模拟API调用延迟
        simulateApiDelay();

        // 模拟保险公司响应
        try {
            // 1. 验证保单有效性
            boolean policyValid = validatePolicy(company, request.getPolicyNo());
            if (!policyValid) {
                log.warn("保单验证失败，保单号：{}", request.getPolicyNo());
                return ClaimResponse.error("保单验证失败，请检查保单号是否正确或保单是否在有效期内");
            }

            // 2. 计算理赔金额
            ClaimCalculateResult calculateResult = calculateClaimAmount(company, request.getTotalFee());

            // 3. 模拟提交到保险公司
            String transactionId = generateTransactionId(company.getCompanyCode());
            log.info("理赔申请提交成功，交易号：{}，理赔金额：{}", transactionId, calculateResult.getClaimAmount());

            // 构建成功响应
            ClaimResponse response = ClaimResponse.success(claim.getClaimNo(), claim.getConsultationNo());
            response.setClaimId(claim.getId());
            response.setTransactionId(transactionId);
            response.setTotalFee(calculateResult.getTotalFee());
            response.setDeductible(calculateResult.getDeductible());
            response.setClaimRate(calculateResult.getClaimRate());
            response.setClaimAmount(calculateResult.getClaimAmount());
            response.setSelfPayAmount(calculateResult.getSelfPayAmount());
            response.setMessage("理赔申请已成功提交至" + company.getCompanyName() + "，预计24小时内到账");

            return response;
        } catch (Exception e) {
            log.error("提交理赔申请失败", e);
            return ClaimResponse.error("保险公司接口调用失败：" + e.getMessage());
        }
    }

    /**
     * 验证保单有效性（模拟）
     */
    public boolean validatePolicy(InsuranceCompany company, String policyNo) {
        if (policyNo == null || policyNo.trim().isEmpty()) {
            return false;
        }

        // 模拟保单验证规则：保单号必须以特定前缀开头
        String validPrefix = company.getCompanyCode() != null ? company.getCompanyCode().toUpperCase() : "POLICY";
        if (!policyNo.toUpperCase().startsWith(validPrefix)) {
            // 为了测试方便，只要保单号不为空就通过
            return true;
        }

        // 模拟验证通过
        return true;
    }

    /**
     * 计算理赔金额
     */
    public ClaimCalculateResult calculateClaimAmount(InsuranceCompany company, BigDecimal totalFee) {
        ClaimCalculateResult result = new ClaimCalculateResult();
        result.setInsuranceCompanyId(company.getId());
        result.setInsuranceCompanyName(company.getCompanyName());
        result.setTotalFee(totalFee);
        result.setDeductible(company.getDeductible());
        result.setClaimRate(company.getDefaultClaimRate());
        result.setMaxClaimAmount(company.getMaxClaimAmount());

        // 计算可理赔金额 = 总费用 - 免赔额
        BigDecimal claimableAmount = totalFee.subtract(company.getDeductible());
        if (claimableAmount.compareTo(BigDecimal.ZERO) <= 0) {
            claimableAmount = BigDecimal.ZERO;
        }
        result.setClaimableAmount(claimableAmount);

        // 计算理赔金额 = 可理赔金额 * 理赔比例
        BigDecimal rate = company.getDefaultClaimRate().divide(new BigDecimal("100"), 4, RoundingMode.HALF_UP);
        BigDecimal claimAmount = claimableAmount.multiply(rate).setScale(2, RoundingMode.HALF_UP);

        // 检查是否超过最高理赔限额
        if (claimAmount.compareTo(company.getMaxClaimAmount()) > 0) {
            claimAmount = company.getMaxClaimAmount();
            result.setDescription("已达到最高理赔限额" + company.getMaxClaimAmount() + "元");
        } else {
            result.setDescription(String.format("理赔计算：(%.2f - %.2f) * %.2f%% = %.2f",
                    totalFee, company.getDeductible(), company.getDefaultClaimRate(), claimAmount));
        }

        result.setClaimAmount(claimAmount);
        result.setSelfPayAmount(totalFee.subtract(claimAmount));

        return result;
    }

    /**
     * 查询理赔状态（模拟）
     */
    public String queryClaimStatus(InsuranceCompany company, String claimNo) {
        log.info("向保险公司[{}]查询理赔状态，理赔号：{}", company.getCompanyName(), claimNo);
        simulateApiDelay();

        // 模拟不同的状态
        long hourOfDay = LocalDateTime.now().getHour();
        if (hourOfDay < 6) {
            return "COMPLETED"; // 早上6点前查询返回已完成
        } else if (hourOfDay < 12) {
            return "APPROVED"; // 中午前返回已批准
        } else if (hourOfDay < 18) {
            return "PENDING"; // 下午返回处理中
        } else {
            return "REJECTED"; // 晚上返回拒绝（测试用）
        }
    }

    /**
     * 取消理赔申请（模拟）
     */
    public boolean cancelClaim(InsuranceCompany company, String claimNo) {
        log.info("向保险公司[{}]取消理赔申请，理赔号：{}", company.getCompanyName(), claimNo);
        simulateApiDelay();
        return true;
    }

    /**
     * 生成交易编号
     */
    private String generateTransactionId(String companyCode) {
        String prefix = companyCode != null ? companyCode.toUpperCase() : "TXN";
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        String uuid = UUID.randomUUID().toString().replace("-", "").substring(0, 8).toUpperCase();
        return prefix + "-" + timestamp + "-" + uuid;
    }

    /**
     * 模拟API调用延迟
     */
    private void simulateApiDelay() {
        try {
            // 模拟500-1500ms的网络延迟
            long delay = 500 + (long) (Math.random() * 1000);
            Thread.sleep(delay);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    /**
     * 模拟获取保险公司实时报价（用于前端展示）
     */
    public Map<String, Object> getInsuranceQuote(InsuranceCompany company, String petType, int age) {
        Map<String, Object> quote = new HashMap<>();
        quote.put("companyId", company.getId());
        quote.put("companyName", company.getCompanyName());
        quote.put("petType", petType);
        quote.put("age", age);

        // 根据宠物类型和年龄计算保费
        BigDecimal basePremium;
        if ("DOG".equalsIgnoreCase(petType)) {
            basePremium = new BigDecimal("399.00");
        } else if ("CAT".equalsIgnoreCase(petType)) {
            basePremium = new BigDecimal("299.00");
        } else {
            basePremium = new BigDecimal("199.00");
        }

        // 年龄调整（年龄越大保费越高）
        if (age > 5) {
            basePremium = basePremium.multiply(new BigDecimal("1.2"));
        }
        if (age > 10) {
            basePremium = basePremium.multiply(new BigDecimal("1.3"));
        }

        quote.put("annualPremium", basePremium.setScale(2, RoundingMode.HALF_UP));
        quote.put("deductible", company.getDeductible());
        quote.put("claimRate", company.getDefaultClaimRate() + "%");
        quote.put("maxClaimAmount", company.getMaxClaimAmount());
        quote.put("coverage", company.getCoverageDescription());

        return quote;
    }
}
