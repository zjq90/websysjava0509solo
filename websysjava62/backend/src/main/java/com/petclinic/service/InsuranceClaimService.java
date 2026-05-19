package com.petclinic.service;

import com.petclinic.dto.ClaimCalculateResult;
import com.petclinic.dto.ClaimRequest;
import com.petclinic.dto.ClaimResponse;
import com.petclinic.entity.ConsultationRecord;
import com.petclinic.entity.InsuranceClaim;
import com.petclinic.entity.InsuranceCompany;
import com.petclinic.repository.ConsultationRecordRepository;
import com.petclinic.repository.InsuranceClaimRepository;
import com.petclinic.repository.InsuranceCompanyRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

/**
 * 保险理赔服务
 * 提供完整的理赔流程：预计算、申请、查询、审核等
 * 
 * @author Pet Clinic Team
 * @version 1.0.0
 */
@Slf4j
@Service
public class InsuranceClaimService {

    @Autowired
    private InsuranceClaimRepository insuranceClaimRepository;

    @Autowired
    private InsuranceCompanyRepository insuranceCompanyRepository;

    @Autowired
    private ConsultationRecordRepository consultationRecordRepository;

    @Autowired
    private InsuranceApiClientService insuranceApiClientService;

    // ==================== 保险公司管理 ====================

    /**
     * 查询所有保险公司
     */
    public List<InsuranceCompany> findAllCompanies() {
        return insuranceCompanyRepository.findAll();
    }

    /**
     * 查询启用的保险公司列表
     */
    public List<InsuranceCompany> findActiveCompanies() {
        return insuranceCompanyRepository.findByStatus("ACTIVE");
    }

    /**
     * 根据ID查询保险公司
     */
    public Optional<InsuranceCompany> findCompanyById(Long id) {
        return insuranceCompanyRepository.findById(id);
    }

    /**
     * 保存保险公司
     */
    public InsuranceCompany saveCompany(InsuranceCompany company) {
        return insuranceCompanyRepository.save(company);
    }

    /**
     * 更新保险公司
     */
    public InsuranceCompany updateCompany(Long id, InsuranceCompany company) {
        Optional<InsuranceCompany> optional = insuranceCompanyRepository.findById(id);
        if (optional.isPresent()) {
            InsuranceCompany existing = optional.get();
            existing.setCompanyName(company.getCompanyName());
            existing.setCompanyCode(company.getCompanyCode());
            existing.setApiUrl(company.getApiUrl());
            existing.setApiKey(company.getApiKey());
            existing.setApiSecret(company.getApiSecret());
            existing.setDefaultClaimRate(company.getDefaultClaimRate());
            existing.setMaxClaimAmount(company.getMaxClaimAmount());
            existing.setDeductible(company.getDeductible());
            existing.setCoverageDescription(company.getCoverageDescription());
            existing.setContactPerson(company.getContactPerson());
            existing.setContactPhone(company.getContactPhone());
            existing.setAddress(company.getAddress());
            existing.setStatus(company.getStatus());
            existing.setAutoClaimEnabled(company.getAutoClaimEnabled());
            existing.setRemark(company.getRemark());
            return insuranceCompanyRepository.save(existing);
        }
        return null;
    }

    /**
     * 删除保险公司
     */
    public void deleteCompanyById(Long id) {
        insuranceCompanyRepository.deleteById(id);
    }

    // ==================== 理赔预计算 ====================

    /**
     * 预计算理赔金额
     */
    public ClaimCalculateResult calculateClaim(Long companyId, java.math.BigDecimal totalFee) {
        Optional<InsuranceCompany> companyOpt = insuranceCompanyRepository.findById(companyId);
        if (!companyOpt.isPresent()) {
            throw new RuntimeException("保险公司不存在");
        }

        InsuranceCompany company = companyOpt.get();
        return insuranceApiClientService.calculateClaimAmount(company, totalFee);
    }

    /**
     * 验证保单有效性
     */
    public boolean validatePolicy(Long companyId, String policyNo) {
        Optional<InsuranceCompany> companyOpt = insuranceCompanyRepository.findById(companyId);
        if (!companyOpt.isPresent()) {
            return false;
        }
        return insuranceApiClientService.validatePolicy(companyOpt.get(), policyNo);
    }

    // ==================== 理赔申请 ====================

    /**
     * 创建理赔申请（问诊费用直接理赔）
     */
    @Transactional
    public ClaimResponse createClaim(ClaimRequest request) {
        log.info("开始创建理赔申请，问诊ID：{}", request.getConsultationId());

        // 1. 查询问诊记录
        Optional<ConsultationRecord> consultationOpt = consultationRecordRepository.findById(request.getConsultationId());
        if (!consultationOpt.isPresent()) {
            return ClaimResponse.error("问诊记录不存在");
        }

        ConsultationRecord consultation = consultationOpt.get();

        // 2. 检查是否已经申请过理赔
        List<InsuranceClaim> existingClaims = insuranceClaimRepository.findByConsultationId(request.getConsultationId());
        for (InsuranceClaim claim : existingClaims) {
            if (!"REJECTED".equals(claim.getStatus()) && !"CANCELLED".equals(claim.getStatus())) {
                return ClaimResponse.error("该问诊记录已存在理赔申请，请勿重复提交");
            }
        }

        // 3. 确定保险公司（如果未指定，使用第一个启用的保险公司）
        InsuranceCompany company;
        if (request.getInsuranceCompanyId() != null) {
            Optional<InsuranceCompany> companyOpt = insuranceCompanyRepository.findById(request.getInsuranceCompanyId());
            if (!companyOpt.isPresent()) {
                return ClaimResponse.error("指定的保险公司不存在");
            }
            company = companyOpt.get();
        } else {
            List<InsuranceCompany> activeCompanies = findActiveCompanies();
            if (activeCompanies.isEmpty()) {
                return ClaimResponse.error("没有可用的保险公司");
            }
            company = activeCompanies.get(0);
        }

        // 4. 从问诊记录获取费用信息（如果请求中未提供）
        java.math.BigDecimal totalFee = request.getTotalFee();
        if (totalFee == null && consultation.getConsultationFee() != null) {
            totalFee = consultation.getConsultationFee();
        }
        if (totalFee == null) {
            return ClaimResponse.error("无法获取问诊费用信息");
        }

        // 5. 从问诊记录获取用户信息（如果请求中未提供）
        String ownerName = request.getOwnerName() != null ? request.getOwnerName() : consultation.getOwnerName();
        String ownerPhone = request.getOwnerPhone() != null ? request.getOwnerPhone() : consultation.getOwnerPhone();
        String petName = request.getPetName() != null ? request.getPetName() : consultation.getPetName();

        // 6. 预计算理赔金额
        ClaimCalculateResult calculateResult = insuranceApiClientService.calculateClaimAmount(company, totalFee);
        if (calculateResult.getClaimAmount().compareTo(java.math.BigDecimal.ZERO) <= 0) {
            return ClaimResponse.error("问诊费用未达到免赔额，无法申请理赔");
        }

        // 7. 创建理赔记录
        InsuranceClaim claim = new InsuranceClaim();
        claim.setClaimNo(generateClaimNo());
        claim.setConsultationId(consultation.getId());
        claim.setConsultationNo(consultation.getConsultationNo());
        claim.setPolicyNo(request.getPolicyNo());
        claim.setInsuranceCompany(company.getCompanyName());
        claim.setPetName(petName);
        claim.setOwnerName(ownerName);
        claim.setOwnerPhone(ownerPhone);
        claim.setOwnerIdCard(request.getOwnerIdCard());
        claim.setTotalFee(totalFee);
        claim.setClaimAmount(calculateResult.getClaimAmount());
        claim.setSelfPayAmount(calculateResult.getSelfPayAmount());
        claim.setClaimDescription(request.getClaimDescription());
        claim.setStatus("PENDING");

        InsuranceClaim savedClaim = insuranceClaimRepository.save(claim);

        // 8. 如果需要自动提交到保险公司
        ClaimResponse response;
        if (Boolean.TRUE.equals(request.getAutoSubmit()) && Boolean.TRUE.equals(company.getAutoClaimEnabled())) {
            response = insuranceApiClientService.submitClaim(company, request, savedClaim);
            response.setClaimId(savedClaim.getId());

            // 更新理赔记录中的交易号
            if (response.getSuccess()) {
                savedClaim.setStatus("APPROVED"); // 模拟直接通过
                insuranceClaimRepository.save(savedClaim);

                // 更新问诊记录的理赔状态
                consultation.setClaimed(true);
                consultationRecordRepository.save(consultation);
            }
        } else {
            response = ClaimResponse.success(savedClaim.getClaimNo(), savedClaim.getConsultationNo());
            response.setClaimId(savedClaim.getId());
            response.setClaimAmount(calculateResult.getClaimAmount());
            response.setSelfPayAmount(calculateResult.getSelfPayAmount());
            response.setDeductible(calculateResult.getDeductible());
            response.setClaimRate(calculateResult.getClaimRate());
            response.setTotalFee(totalFee);
            response.setMessage("理赔申请已创建，请等待人工审核");
        }

        log.info("理赔申请创建成功，理赔号：{}", savedClaim.getClaimNo());
        return response;
    }

    /**
     * 根据问诊ID快速申请理赔（一键理赔）
     */
    @Transactional
    public ClaimResponse quickClaim(Long consultationId, String policyNo, Long companyId) {
        ClaimRequest request = new ClaimRequest();
        request.setConsultationId(consultationId);
        request.setPolicyNo(policyNo);
        request.setInsuranceCompanyId(companyId);
        request.setAutoSubmit(true);
        return createClaim(request);
    }

    /**
     * 根据问诊ID快速申请理赔（一键理赔）- 兼容旧版本
     */
    @Transactional
    public ClaimResponse quickClaim(Long consultationId, String policyNo) {
        return quickClaim(consultationId, policyNo, null);
    }

    // ==================== 理赔查询 ====================

    /**
     * 查询所有理赔记录
     */
    public List<InsuranceClaim> findAllClaims() {
        return insuranceClaimRepository.findAll();
    }

    /**
     * 根据ID查询理赔记录
     */
    public Optional<InsuranceClaim> findClaimById(Long id) {
        return insuranceClaimRepository.findById(id);
    }

    /**
     * 根据状态查询理赔记录
     */
    public List<InsuranceClaim> findClaimsByStatus(String status) {
        return insuranceClaimRepository.findByStatus(status);
    }

    /**
     * 根据问诊ID查询理赔记录
     */
    public List<InsuranceClaim> findClaimsByConsultationId(Long consultationId) {
        return insuranceClaimRepository.findByConsultationId(consultationId);
    }

    /**
     * 根据宠物名称搜索理赔记录
     */
    public List<InsuranceClaim> searchClaimsByPetName(String petName) {
        return insuranceClaimRepository.findByPetNameContaining(petName);
    }

    // ==================== 理赔审核/处理 ====================

    /**
     * 审核理赔申请
     */
    @Transactional
    public InsuranceClaim reviewClaim(Long id, String status, String reviewedBy, String rejectReason) {
        Optional<InsuranceClaim> optional = insuranceClaimRepository.findById(id);
        if (!optional.isPresent()) {
            return null;
        }

        InsuranceClaim claim = optional.get();
        claim.setStatus(status);
        claim.setReviewedBy(reviewedBy);
        claim.setReviewedTime(LocalDateTime.now());

        if ("REJECTED".equals(status)) {
            claim.setRejectReason(rejectReason);
        }

        if ("COMPLETED".equals(status)) {
            claim.setCompletedTime(LocalDateTime.now());
        }

        return insuranceClaimRepository.save(claim);
    }

    /**
     * 完成理赔（支付）
     */
    @Transactional
    public InsuranceClaim completeClaim(Long id) {
        return reviewClaim(id, "COMPLETED", "SYSTEM", null);
    }

    /**
     * 拒绝理赔
     */
    @Transactional
    public InsuranceClaim rejectClaim(Long id, String rejectReason, String reviewedBy) {
        return reviewClaim(id, "REJECTED", reviewedBy, rejectReason);
    }

    /**
     * 取消理赔申请
     */
    @Transactional
    public boolean cancelClaim(Long id) {
        Optional<InsuranceClaim> optional = insuranceClaimRepository.findById(id);
        if (!optional.isPresent()) {
            return false;
        }

        InsuranceClaim claim = optional.get();
        if ("COMPLETED".equals(claim.getStatus())) {
            return false; // 已完成的不能取消
        }

        // 尝试调用保险公司接口取消
        Optional<InsuranceCompany> companyOpt = insuranceCompanyRepository.findByCompanyCode(claim.getInsuranceCompany());
        if (companyOpt.isPresent()) {
            insuranceApiClientService.cancelClaim(companyOpt.get(), claim.getClaimNo());
        }

        claim.setStatus("CANCELLED");
        claim.setCompletedTime(LocalDateTime.now());
        insuranceClaimRepository.save(claim);

        // 更新问诊记录状态
        Optional<ConsultationRecord> consultationOpt = consultationRecordRepository.findById(claim.getConsultationId());
        if (consultationOpt.isPresent()) {
            ConsultationRecord consultation = consultationOpt.get();
            consultation.setClaimed(false);
            consultationRecordRepository.save(consultation);
        }

        return true;
    }

    /**
     * 同步理赔状态（从保险公司获取最新状态）
     */
    @Transactional
    public InsuranceClaim syncClaimStatus(Long id) {
        Optional<InsuranceClaim> optional = insuranceClaimRepository.findById(id);
        if (!optional.isPresent()) {
            return null;
        }

        InsuranceClaim claim = optional.get();

        // 查找对应的保险公司
        List<InsuranceCompany> companies = insuranceCompanyRepository.findByCompanyNameContaining(claim.getInsuranceCompany());
        if (!companies.isEmpty()) {
            String newStatus = insuranceApiClientService.queryClaimStatus(companies.get(0), claim.getClaimNo());
            if (!claim.getStatus().equals(newStatus)) {
                claim.setStatus(newStatus);
                if ("COMPLETED".equals(newStatus)) {
                    claim.setCompletedTime(LocalDateTime.now());
                }
                return insuranceClaimRepository.save(claim);
            }
        }

        return claim;
    }

    /**
     * 删除理赔记录
     */
    public void deleteClaimById(Long id) {
        insuranceClaimRepository.deleteById(id);
    }

    /**
     * 生成理赔编号
     */
    private String generateClaimNo() {
        return "LP" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")) +
                String.format("%04d", (int) (Math.random() * 10000));
    }
}
