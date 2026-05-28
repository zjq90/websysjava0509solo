package com.club.management.resource.service;

import com.club.management.common.exception.BusinessException;
import com.club.management.common.result.PageResult;
import com.club.management.resource.entity.CooperationApply;
import com.club.management.resource.entity.EnterpriseCooperation;
import com.club.management.resource.entity.ResourceShare;
import com.club.management.resource.repository.CooperationApplyRepository;
import com.club.management.resource.repository.EnterpriseCooperationRepository;
import com.club.management.resource.repository.ResourceShareRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 资源共享Service
 *
 * @author club-management
 * @version 1.0.0
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ResourceService {

    private final ResourceShareRepository resourceShareRepository;
    private final EnterpriseCooperationRepository enterpriseCooperationRepository;
    private final CooperationApplyRepository cooperationApplyRepository;

    public PageResult<ResourceShare> getResourceList(Integer category, Integer pageNum, Integer pageSize) {
        log.info("获取公开资源列表，分类：{}", category);
        Pageable pageable = PageRequest.of(pageNum - 1, pageSize);
        Page<ResourceShare> page;
        if (category != null) {
            page = resourceShareRepository.findByCategoryAndIsPublicAndStatusOrderByIsTopDescCreateTimeDesc(
                    category, 1, 0, pageable);
        } else {
            page = resourceShareRepository.findByIsPublicAndStatusOrderByIsTopDescCreateTimeDesc(
                    1, 0, pageable);
        }
        return new PageResult<>(page.getContent(), page.getTotalElements(), pageNum, pageSize);
    }

    public PageResult<ResourceShare> getClubResourceList(Long clubId, Integer pageNum, Integer pageSize) {
        log.info("获取社团资源列表，社团ID：{}", clubId);
        Pageable pageable = PageRequest.of(pageNum - 1, pageSize);
        Page<ResourceShare> page = resourceShareRepository.findByClubIdAndStatusOrderByIsTopDescCreateTimeDesc(
                clubId, 0, pageable);
        return new PageResult<>(page.getContent(), page.getTotalElements(), pageNum, pageSize);
    }

    public PageResult<ResourceShare> getMyResourceList(Long uploaderId, Integer pageNum, Integer pageSize) {
        log.info("获取我的资源列表，上传者ID：{}", uploaderId);
        Pageable pageable = PageRequest.of(pageNum - 1, pageSize);
        Page<ResourceShare> page = resourceShareRepository.findByUploaderIdOrderByCreateTimeDesc(uploaderId, pageable);
        return new PageResult<>(page.getContent(), page.getTotalElements(), pageNum, pageSize);
    }

    @Transactional(rollbackFor = Exception.class)
    public ResourceShare uploadResource(ResourceShare resource) {
        log.info("上传资源，标题：{}，上传者ID：{}", resource.getTitle(), resource.getUploaderId());
        resource.setDownloadCount(0);
        resource.setViewCount(0);
        resource.setFavoriteCount(0);
        resource.setLikeCount(0);
        resource.setRating(0.0);
        resource.setRatingCount(0);
        resource.setIsEssence(0);
        resource.setIsTop(0);
        resource.setStatus(0);
        ResourceShare saved = resourceShareRepository.save(resource);
        log.info("资源上传成功，资源ID：{}", saved.getId());
        return saved;
    }

    @Transactional(rollbackFor = Exception.class)
    public void increaseResourceView(Long resourceId) {
        resourceShareRepository.increaseViewCount(resourceId);
    }

    @Transactional(rollbackFor = Exception.class)
    public void increaseResourceDownload(Long resourceId) {
        resourceShareRepository.increaseDownloadCount(resourceId);
    }

    public ResourceShare getResourceDetail(Long resourceId) {
        log.info("获取资源详情，资源ID：{}", resourceId);
        return resourceShareRepository.findById(resourceId)
                .orElseThrow(() -> new BusinessException("资源不存在"));
    }

    @Transactional(rollbackFor = Exception.class)
    public void deleteResource(Long resourceId, Long userId) {
        log.info("删除资源，资源ID：{}，用户ID：{}", resourceId, userId);
        ResourceShare resource = resourceShareRepository.findById(resourceId)
                .orElseThrow(() -> new BusinessException("资源不存在"));
        if (!resource.getUploaderId().equals(userId)) {
            throw new BusinessException("无权限删除");
        }
        resource.setStatus(1);
        resourceShareRepository.save(resource);
    }

    public PageResult<EnterpriseCooperation> getCooperationList(Integer type, Integer pageNum, Integer pageSize) {
        log.info("获取校企对接列表，类型：{}", type);
        Pageable pageable = PageRequest.of(pageNum - 1, pageSize);
        Page<EnterpriseCooperation> page;
        if (type != null) {
            page = enterpriseCooperationRepository.findByTypeAndStatusOrderByIsTopDescCreateTimeDesc(
                    type, 0, pageable);
        } else {
            page = enterpriseCooperationRepository.findByStatusOrderByIsTopDescCreateTimeDesc(0, pageable);
        }
        return new PageResult<>(page.getContent(), page.getTotalElements(), pageNum, pageSize);
    }

    @Transactional(rollbackFor = Exception.class)
    public EnterpriseCooperation publishCooperation(EnterpriseCooperation cooperation) {
        log.info("发布校企对接信息，标题：{}，类型：{}", cooperation.getTitle(), cooperation.getType());
        cooperation.setViewCount(0);
        cooperation.setApplyCount(0);
        cooperation.setFavoriteCount(0);
        cooperation.setIsRecommend(0);
        cooperation.setIsTop(0);
        cooperation.setStatus(0);
        EnterpriseCooperation saved = enterpriseCooperationRepository.save(cooperation);
        log.info("校企对接信息发布成功，ID：{}", saved.getId());
        return saved;
    }

    @Transactional(rollbackFor = Exception.class)
    public void increaseCooperationView(Long cooperationId) {
        enterpriseCooperationRepository.increaseViewCount(cooperationId);
    }

    public EnterpriseCooperation getCooperationDetail(Long cooperationId) {
        log.info("获取校企对接详情，ID：{}", cooperationId);
        return enterpriseCooperationRepository.findById(cooperationId)
                .orElseThrow(() -> new BusinessException("信息不存在"));
    }

    public PageResult<CooperationApply> getCooperationApplyList(Long cooperationId, Integer pageNum, Integer pageSize) {
        log.info("获取校企对接申请列表，对接ID：{}", cooperationId);
        Pageable pageable = PageRequest.of(pageNum - 1, pageSize);
        Page<CooperationApply> page = cooperationApplyRepository.findByCooperationIdOrderByCreateTimeDesc(cooperationId, pageable);
        return new PageResult<>(page.getContent(), page.getTotalElements(), pageNum, pageSize);
    }

    public PageResult<CooperationApply> getMyApplyList(Long applicantId, Integer pageNum, Integer pageSize) {
        log.info("获取我的申请列表，申请人ID：{}", applicantId);
        Pageable pageable = PageRequest.of(pageNum - 1, pageSize);
        Page<CooperationApply> page = cooperationApplyRepository.findByApplicantIdOrderByCreateTimeDesc(applicantId, pageable);
        return new PageResult<>(page.getContent(), page.getTotalElements(), pageNum, pageSize);
    }

    @Transactional(rollbackFor = Exception.class)
    public CooperationApply submitCooperationApply(CooperationApply apply) {
        log.info("提交校企对接申请，对接ID：{}，申请人ID：{}", apply.getCooperationId(), apply.getApplicantId());
        apply.setStatus(0);
        CooperationApply saved = cooperationApplyRepository.save(apply);

        enterpriseCooperationRepository.increaseApplyCount(apply.getCooperationId());

        log.info("校企对接申请提交成功，申请ID：{}", saved.getId());
        return saved;
    }

    @Transactional(rollbackFor = Exception.class)
    public CooperationApply auditCooperationApply(Long applyId, Integer status, Long auditorId, String auditorName, String auditOpinion) {
        log.info("审批校企对接申请，申请ID：{}，状态：{}，审批人：{}", applyId, status, auditorName);
        CooperationApply apply = cooperationApplyRepository.findById(applyId)
                .orElseThrow(() -> new BusinessException("申请不存在"));

        apply.setStatus(status);
        apply.setAuditorId(auditorId);
        apply.setAuditorName(auditorName);
        apply.setAuditTime(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        apply.setAuditOpinion(auditOpinion);

        CooperationApply saved = cooperationApplyRepository.save(apply);
        log.info("校企对接申请审批完成，申请ID：{}，状态：{}", saved.getId(), status);
        return saved;
    }

    public CooperationApply getCooperationApplyDetail(Long applyId) {
        log.info("获取校企对接申请详情，申请ID：{}", applyId);
        return cooperationApplyRepository.findById(applyId)
                .orElseThrow(() -> new BusinessException("申请不存在"));
    }
}
