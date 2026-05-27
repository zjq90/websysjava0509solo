package com.club.service;

import com.club.dto.PageQuery;
import com.club.entity.Club;
import com.club.entity.ClubApplication;
import com.club.entity.AnnualRegistration;
import com.club.entity.ViolationRecord;
import com.club.enums.ApprovalStatus;
import com.club.enums.ClubStatus;
import com.club.enums.ViolationType;
import com.club.repository.ClubRepository;
import com.club.repository.ClubApplicationRepository;
import com.club.repository.AnnualRegistrationRepository;
import com.club.repository.ViolationRecordRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.Predicate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 社团管理服务类
 * 包含社团CRUD、成立申请审核、年度注册、违规处理等业务逻辑
 *
 * @author Club Management System
 * @version 1.0.0
 */
@Service
public class ClubService {

    private final ClubRepository clubRepository;
    private final ClubApplicationRepository clubApplicationRepository;
    private final AnnualRegistrationRepository annualRegistrationRepository;
    private final ViolationRecordRepository violationRecordRepository;

    public ClubService(ClubRepository clubRepository,
                      ClubApplicationRepository clubApplicationRepository,
                      AnnualRegistrationRepository annualRegistrationRepository,
                      ViolationRecordRepository violationRecordRepository) {
        this.clubRepository = clubRepository;
        this.clubApplicationRepository = clubApplicationRepository;
        this.annualRegistrationRepository = annualRegistrationRepository;
        this.violationRecordRepository = violationRecordRepository;
    }

    // ==================== 社团基本信息管理 ====================

    /**
     * 分页查询社团列表
     */
    public Page<Club> getClubList(PageQuery query) {
        Pageable pageable = PageRequest.of(query.getPageNum() - 1, query.getPageSize());
        
        Specification<Club> spec = (root, criteriaQuery, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            
            if (StringUtils.hasText(query.getKeyword())) {
                predicates.add(criteriaBuilder.or(
                    criteriaBuilder.like(root.get("name"), "%" + query.getKeyword() + "%"),
                    criteriaBuilder.like(root.get("shortName"), "%" + query.getKeyword() + "%")
                ));
            }
            if (StringUtils.hasText(query.getStatus())) {
                predicates.add(criteriaBuilder.equal(root.get("status"), ClubStatus.valueOf(query.getStatus())));
            }
            if (StringUtils.hasText(query.getType())) {
                predicates.add(criteriaBuilder.equal(root.get("type"), query.getType()));
            }
            
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
        
        return clubRepository.findAll(spec, pageable);
    }

    /**
     * 根据ID查询社团
     */
    public Club getClubById(Long id) {
        return clubRepository.findById(id).orElse(null);
    }

    /**
     * 新增社团
     */
    public Club createClub(Club club) {
        if (clubRepository.existsByName(club.getName())) {
            throw new RuntimeException("社团名称已存在");
        }
        return clubRepository.save(club);
    }

    /**
     * 更新社团信息
     */
    public Club updateClub(Long id, Club club) {
        Club existing = getClubById(id);
        if (existing == null) {
            throw new RuntimeException("社团不存在");
        }
        club.setId(id);
        return clubRepository.save(club);
    }

    /**
     * 删除社团
     */
    public void deleteClub(Long id) {
        clubRepository.deleteById(id);
    }

    /**
     * 获取社团统计数据
     */
    public Map<String, Object> getClubStatistics() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalClubs", clubRepository.count());
        stats.put("totalMembers", clubRepository.sumMemberCount());
        stats.put("totalActivities", clubRepository.sumAnnualActivityCount());
        stats.put("byType", clubRepository.countByType());
        stats.put("byDepartment", clubRepository.countByDepartment());
        stats.put("byStatus", clubRepository.countByStatus());
        return stats;
    }

    // ==================== 社团成立申请管理 ====================

    /**
     * 分页查询社团成立申请
     */
    public Page<ClubApplication> getApplicationList(PageQuery query) {
        Pageable pageable = PageRequest.of(query.getPageNum() - 1, query.getPageSize());
        
        Specification<ClubApplication> spec = (root, criteriaQuery, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            
            if (StringUtils.hasText(query.getKeyword())) {
                predicates.add(criteriaBuilder.like(root.get("clubName"), "%" + query.getKeyword() + "%"));
            }
            if (StringUtils.hasText(query.getStatus())) {
                predicates.add(criteriaBuilder.equal(root.get("status"), ApprovalStatus.valueOf(query.getStatus())));
            }
            
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
        
        return clubApplicationRepository.findAll(spec, pageable);
    }

    /**
     * 根据ID查询申请
     */
    public ClubApplication getApplicationById(Long id) {
        return clubApplicationRepository.findById(id).orElse(null);
    }

    /**
     * 审核社团成立申请
     */
    public ClubApplication approveApplication(Long id, boolean approved, String opinion, Long approverId, String approverName) {
        ClubApplication application = getApplicationById(id);
        if (application == null) {
            throw new RuntimeException("申请不存在");
        }
        if (application.getStatus() != ApprovalStatus.PENDING) {
            throw new RuntimeException("该申请已处理");
        }
        
        application.setStatus(approved ? ApprovalStatus.APPROVED : ApprovalStatus.REJECTED);
        application.setApproveOpinion(opinion);
        application.setApproverId(approverId);
        application.setApproverName(approverName);
        application.setApproveTime(LocalDateTime.now());
        
        // 如果审核通过，创建社团
        if (approved) {
            Club club = new Club();
            club.setName(application.getClubName());
            club.setType(com.club.enums.ClubType.valueOf(application.getClubType()));
            club.setDepartment(application.getDepartment());
            club.setDescription(application.getDescription());
            club.setConstitution(application.getConstitution());
            club.setLeaderName(application.getInitiatorName());
            club.setLeaderPhone(application.getInitiatorPhone());
            club.setMemberCount(application.getInitiatorCount());
            club.setStatus(ClubStatus.NORMAL);
            club.setAnnualRegistered(false);
            Club savedClub = clubRepository.save(club);
            application.setClubId(savedClub.getId());
        }
        
        return clubApplicationRepository.save(application);
    }

    // ==================== 年度注册管理 ====================

    /**
     * 分页查询年度注册申请
     */
    public Page<AnnualRegistration> getAnnualRegistrationList(PageQuery query) {
        Pageable pageable = PageRequest.of(query.getPageNum() - 1, query.getPageSize());
        
        Specification<AnnualRegistration> spec = (root, criteriaQuery, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            
            if (StringUtils.hasText(query.getKeyword())) {
                predicates.add(criteriaBuilder.like(root.get("clubName"), "%" + query.getKeyword() + "%"));
            }
            if (StringUtils.hasText(query.getStatus())) {
                predicates.add(criteriaBuilder.equal(root.get("status"), ApprovalStatus.valueOf(query.getStatus())));
            }
            
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
        
        return annualRegistrationRepository.findAll(spec, pageable);
    }

    /**
     * 审核年度注册
     */
    public AnnualRegistration approveAnnualRegistration(Long id, boolean approved, String opinion, Long approverId, String approverName) {
        AnnualRegistration registration = annualRegistrationRepository.findById(id).orElse(null);
        if (registration == null) {
            throw new RuntimeException("注册申请不存在");
        }
        if (registration.getStatus() != ApprovalStatus.PENDING) {
            throw new RuntimeException("该申请已处理");
        }
        
        registration.setStatus(approved ? ApprovalStatus.APPROVED : ApprovalStatus.REJECTED);
        registration.setApproveOpinion(opinion);
        registration.setApproverId(approverId);
        registration.setApproverName(approverName);
        registration.setApproveTime(LocalDateTime.now());
        
        // 如果审核通过，更新社团注册状态
        if (approved) {
            Club club = getClubById(registration.getClubId());
            if (club != null) {
                club.setAnnualRegistered(true);
                club.setRegisterYear(registration.getRegisterYear());
                clubRepository.save(club);
            }
        }
        
        return annualRegistrationRepository.save(registration);
    }

    // ==================== 违规处理管理 ====================

    /**
     * 分页查询违规记录
     */
    public Page<ViolationRecord> getViolationRecordList(PageQuery query) {
        Pageable pageable = PageRequest.of(query.getPageNum() - 1, query.getPageSize());
        
        Specification<ViolationRecord> spec = (root, criteriaQuery, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            
            if (StringUtils.hasText(query.getKeyword())) {
                predicates.add(criteriaBuilder.like(root.get("clubName"), "%" + query.getKeyword() + "%"));
            }
            
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
        
        return violationRecordRepository.findAll(spec, pageable);
    }

    /**
     * 创建违规记录并处理社团
     */
    public ViolationRecord handleViolation(ViolationRecord record, Long handlerId, String handlerName) {
        record.setHandlerId(handlerId);
        record.setHandlerName(handlerName);
        record.setHandleTime(LocalDateTime.now());
        ViolationRecord saved = violationRecordRepository.save(record);
        
        // 更新社团状态
        Club club = getClubById(record.getClubId());
        if (club != null) {
            club.setStatus(record.getHandleType());
            clubRepository.save(club);
        }
        
        return saved;
    }

    /**
     * 标记违规已整改
     */
    public ViolationRecord markRectified(Long id, String note) {
        ViolationRecord record = violationRecordRepository.findById(id).orElse(null);
        if (record == null) {
            throw new RuntimeException("违规记录不存在");
        }
        record.setRectified(true);
        record.setRectificationNote(note);
        
        // 如果已整改，恢复社团状态为正常
        Club club = getClubById(record.getClubId());
        if (club != null && club.getStatus() == ClubStatus.WARNING) {
            club.setStatus(ClubStatus.NORMAL);
            clubRepository.save(club);
        }
        
        return violationRecordRepository.save(record);
    }

    /**
     * 获取待处理违规数量
     */
    public Map<String, Long> getPendingCounts() {
        Map<String, Long> counts = new HashMap<>();
        counts.put("clubApplications", clubApplicationRepository.countByStatus(ApprovalStatus.PENDING));
        counts.put("annualRegistrations", annualRegistrationRepository.countByStatus(ApprovalStatus.PENDING));
        counts.put("violations", violationRecordRepository.countByRectifiedFalse());
        return counts;
    }

    /**
     * 获取所有社团列表（不分页）
     */
    public List<Club> getAllClubs() {
        return clubRepository.findAll();
    }

    /**
     * 处理违规记录
     */
    public ViolationRecord handleViolationRecord(Long id, String action, String handlerOpinion, Long handlerId, String handlerName) {
        ViolationRecord record = violationRecordRepository.findById(id).orElse(null);
        if (record == null) {
            throw new RuntimeException("违规记录不存在");
        }
        
        ClubStatus handleType = null;
        if ("WARNING".equals(action)) {
            handleType = ClubStatus.WARNING;
        } else if ("SUSPEND".equals(action)) {
            handleType = ClubStatus.SUSPENDED;
        } else if ("CANCEL".equals(action)) {
            handleType = ClubStatus.CANCELLED;
        }
        
        record.setHandleType(handleType);
        record.setHandleOpinion(handlerOpinion);
        record.setHandlerId(handlerId);
        record.setHandlerName(handlerName);
        record.setHandleTime(LocalDateTime.now());
        record.setRectified(true);
        
        // 根据处理措施更新社团状态
        Club club = getClubById(record.getClubId());
        if (club != null && handleType != null) {
            club.setStatus(handleType);
            clubRepository.save(club);
        }
        
        return violationRecordRepository.save(record);
    }

    /**
     * 删除违规记录
     */
    public void deleteViolationRecord(Long id) {
        violationRecordRepository.deleteById(id);
    }
}
