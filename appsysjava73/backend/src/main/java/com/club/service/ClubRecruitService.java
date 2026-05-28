package com.club.service;

import com.club.common.PageResult;
import com.club.entity.Club;
import com.club.entity.ClubRecruit;
import com.club.entity.ClubRecruitApply;
import com.club.entity.User;
import com.club.entity.enums.ApplyStatusEnum;
import com.club.exception.BusinessException;
import com.club.repository.ClubRecruitApplyRepository;
import com.club.repository.ClubRecruitRepository;
import com.club.repository.ClubRepository;
import com.club.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class ClubRecruitService {

    @Autowired
    private ClubRecruitRepository clubRecruitRepository;

    @Autowired
    private ClubRecruitApplyRepository clubRecruitApplyRepository;

    @Autowired
    private ClubRepository clubRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ClubMemberService clubMemberService;

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Transactional(rollbackFor = Exception.class)
    public ClubRecruit createRecruit(ClubRecruit recruit, Long userId) {
        log.info("开启招新通道 - clubId: {}, title: {}, userId: {}", recruit.getClubId(), recruit.getTitle(), userId);

        Club club = clubRepository.findByIdAndDeletedFalse(recruit.getClubId())
                .orElseThrow(() -> new BusinessException("社团不存在"));

        if (!club.getLeaderId().equals(userId)) {
            throw new BusinessException("只有社长才能开启招新");
        }

        if (clubRecruitRepository.existsByClubIdAndStatusAndDeletedFalse(recruit.getClubId(), 1)) {
            throw new BusinessException("当前已有进行中的招新");
        }

        recruit.setClubName(club.getName());
        recruit.setClubLogo(club.getLogo());
        recruit.setApplyCount(0);
        recruit.setApprovedCount(0);
        recruit.setStatus(1);

        if (recruit.getStartTime() == null) {
            recruit.setStartTime(LocalDateTime.now().format(FORMATTER));
        }
        if (recruit.getEndTime() == null) {
            recruit.setEndTime(LocalDateTime.now().plusDays(30).format(FORMATTER));
        }

        ClubRecruit saved = clubRecruitRepository.save(recruit);

        club.setRecruiting(1);
        clubRepository.save(club);

        log.info("招新通道开启成功 - recruitId: {}", saved.getId());
        return saved;
    }

    @Transactional(rollbackFor = Exception.class)
    public ClubRecruit updateRecruit(Long recruitId, ClubRecruit recruit, Long userId) {
        log.info("更新招新信息 - recruitId: {}, userId: {}", recruitId, userId);

        ClubRecruit existRecruit = clubRecruitRepository.findByIdAndDeletedFalse(recruitId)
                .orElseThrow(() -> new BusinessException("招新信息不存在"));

        Club club = clubRepository.findByIdAndDeletedFalse(existRecruit.getClubId())
                .orElseThrow(() -> new BusinessException("社团不存在"));

        if (!club.getLeaderId().equals(userId)) {
            throw new BusinessException("只有社长才能修改招新信息");
        }

        if (recruit.getTitle() != null) existRecruit.setTitle(recruit.getTitle());
        if (recruit.getIntroduction() != null) existRecruit.setIntroduction(recruit.getIntroduction());
        if (recruit.getRequirements() != null) existRecruit.setRequirements(recruit.getRequirements());
        if (recruit.getQuota() != null) existRecruit.setQuota(recruit.getQuota());
        if (recruit.getStartTime() != null) existRecruit.setStartTime(recruit.getStartTime());
        if (recruit.getEndTime() != null) existRecruit.setEndTime(recruit.getEndTime());
        if (recruit.getContactName() != null) existRecruit.setContactName(recruit.getContactName());
        if (recruit.getContactPhone() != null) existRecruit.setContactPhone(recruit.getContactPhone());

        ClubRecruit saved = clubRecruitRepository.save(existRecruit);
        log.info("招新信息更新成功 - recruitId: {}", recruitId);
        return saved;
    }

    @Transactional(rollbackFor = Exception.class)
    public void closeRecruit(Long recruitId, Long userId) {
        log.info("关闭招新通道 - recruitId: {}, userId: {}", recruitId, userId);

        ClubRecruit recruit = clubRecruitRepository.findByIdAndDeletedFalse(recruitId)
                .orElseThrow(() -> new BusinessException("招新信息不存在"));

        Club club = clubRepository.findByIdAndDeletedFalse(recruit.getClubId())
                .orElseThrow(() -> new BusinessException("社团不存在"));

        if (!club.getLeaderId().equals(userId)) {
            throw new BusinessException("只有社长才能关闭招新");
        }

        recruit.setStatus(0);
        clubRecruitRepository.save(recruit);

        club.setRecruiting(0);
        clubRepository.save(club);

        log.info("招新通道关闭成功 - recruitId: {}", recruitId);
    }

    public PageResult<ClubRecruit> getRecruitList(Integer pageNum, Integer pageSize, Long clubId, Integer status) {
        log.info("获取招新列表 - pageNum: {}, pageSize: {}, clubId: {}, status: {}", pageNum, pageSize, clubId, status);

        Pageable pageable = PageRequest.of(pageNum - 1, pageSize);
        Page<ClubRecruit> page;

        if (clubId != null && status != null) {
            List<ClubRecruit> list = clubRecruitRepository.findByClubIdAndStatusAndDeletedFalseOrderByCreateTimeDesc(clubId, status);
            long total = list.size();
            int start = (pageNum - 1) * pageSize;
            int end = Math.min(start + pageSize, list.size());
            return PageResult.of(list.subList(start, end), total, pageNum, pageSize);
        } else if (clubId != null) {
            page = clubRecruitRepository.findByClubIdAndDeletedFalseOrderByCreateTimeDesc(clubId, pageable);
        } else {
            page = clubRecruitRepository.findByStatusAndDeletedFalseOrderByCreateTimeDesc(status != null ? status : 1, pageable);
        }

        return PageResult.of(page);
    }

    public Map<String, Object> getRecruitDetail(Long recruitId) {
        log.info("获取招新详情 - recruitId: {}", recruitId);

        ClubRecruit recruit = clubRecruitRepository.findByIdAndDeletedFalse(recruitId)
                .orElseThrow(() -> new BusinessException("招新信息不存在"));

        long pendingCount = clubRecruitApplyRepository.countByRecruitIdAndStatusAndDeletedFalse(recruitId, ApplyStatusEnum.PENDING);
        long approvedCount = clubRecruitApplyRepository.countByRecruitIdAndStatusAndDeletedFalse(recruitId, ApplyStatusEnum.APPROVED);
        long rejectedCount = clubRecruitApplyRepository.countByRecruitIdAndStatusAndDeletedFalse(recruitId, ApplyStatusEnum.REJECTED);

        Map<String, Object> result = new HashMap<>();
        result.put("recruit", recruit);
        result.put("totalQuota", recruit.getQuota());
        result.put("applyCount", recruit.getApplyCount());
        result.put("approvedCount", approvedCount);
        result.put("pendingCount", pendingCount);
        result.put("rejectedCount", rejectedCount);
        result.put("remainingQuota", recruit.getQuota() - (int) approvedCount);

        return result;
    }

    @Transactional(rollbackFor = Exception.class)
    public ClubRecruitApply submitApply(ClubRecruitApply apply, Long userId) {
        Long recruitId = apply.getRecruitId();
        log.info("提交入团申请 - recruitId: {}, userId: {}", recruitId, userId);

        ClubRecruit recruit = clubRecruitRepository.findByIdAndDeletedFalse(recruitId)
                .orElseThrow(() -> new BusinessException("招新信息不存在"));

        if (recruit.getStatus() != 1) {
            throw new BusinessException("招新通道已关闭");
        }

        if (clubRecruitApplyRepository.existsByUserIdAndRecruitIdAndDeletedFalse(userId, recruitId)) {
            throw new BusinessException("已提交过申请");
        }

        long approvedCount = clubRecruitApplyRepository.countByRecruitIdAndStatusAndDeletedFalse(recruitId, ApplyStatusEnum.APPROVED);
        if (approvedCount >= recruit.getQuota()) {
            throw new BusinessException("招新名额已满");
        }

        User user = userRepository.findByIdAndDeletedFalse(userId)
                .orElseThrow(() -> new BusinessException("用户不存在"));

        apply.setClubId(recruit.getClubId());
        apply.setClubName(recruit.getClubName());
        apply.setUserId(userId);
        apply.setUsername(user.getUsername());
        apply.setRealName(user.getRealName());
        apply.setAvatar(user.getAvatar());
        apply.setStudentNo(user.getStudentNo());
        apply.setPhone(user.getPhone());
        apply.setCollege(user.getCollege());
        apply.setMajor(user.getMajor());
        apply.setGrade(user.getGrade());
        apply.setStatus(ApplyStatusEnum.PENDING);

        ClubRecruitApply saved = clubRecruitApplyRepository.save(apply);

        recruit.setApplyCount(recruit.getApplyCount() + 1);
        clubRecruitRepository.save(recruit);

        log.info("入团申请提交成功 - applyId: {}", saved.getId());
        return saved;
    }

    @Transactional(rollbackFor = Exception.class)
    public void reviewApply(Long applyId, ApplyStatusEnum status, String reviewMessage, Long userId) {
        log.info("审核申请 - applyId: {}, reviewerId: {}, status: {}", applyId, userId, status);

        ClubRecruitApply apply = clubRecruitApplyRepository.findByIdAndDeletedFalse(applyId)
                .orElseThrow(() -> new BusinessException("申请不存在"));

        Club club = clubRepository.findByIdAndDeletedFalse(apply.getClubId())
                .orElseThrow(() -> new BusinessException("社团不存在"));

        if (!club.getLeaderId().equals(userId)) {
            throw new BusinessException("只有社长才能审核申请");
        }

        if (apply.getStatus() != ApplyStatusEnum.PENDING) {
            throw new BusinessException("该申请已审核");
        }

        apply.setStatus(status);
        apply.setReviewMessage(reviewMessage);
        apply.setReviewerId(userId);
        apply.setReviewTime(LocalDateTime.now().format(FORMATTER));

        User reviewer = userRepository.findByIdAndDeletedFalse(userId).orElse(null);
        if (reviewer != null) {
            apply.setReviewerName(reviewer.getRealName());
        }

        clubRecruitApplyRepository.save(apply);

        if (status == ApplyStatusEnum.APPROVED) {
            ClubRecruit recruit = clubRecruitRepository.findByIdAndDeletedFalse(apply.getRecruitId()).orElse(null);
            if (recruit != null) {
                recruit.setApprovedCount(recruit.getApprovedCount() + 1);
                clubRecruitRepository.save(recruit);
            }

            clubMemberService.addMember(apply.getClubId(), apply.getUserId(), apply.getDepartmentId(), apply.getDepartmentName());
        }

        log.info("申请审核完成 - applyId: {}, status: {}", applyId, status);
    }

    public PageResult<ClubRecruitApply> getApplyList(Integer pageNum, Integer pageSize, Long clubId, ApplyStatusEnum status, Long userId) {
        log.info("获取申请列表 - pageNum: {}, pageSize: {}, clubId: {}, status: {}, userId: {}",
                pageNum, pageSize, clubId, status, userId);

        Pageable pageable = PageRequest.of(pageNum - 1, pageSize);
        Page<ClubRecruitApply> page;

        if (userId != null) {
            page = clubRecruitApplyRepository.findByUserIdAndDeletedFalseOrderByCreateTimeDesc(userId, pageable);
        } else if (clubId != null && status != null) {
            page = clubRecruitApplyRepository.findByRecruitIdAndStatusAndDeletedFalseOrderByCreateTimeDesc(clubId, status, pageable);
        } else if (clubId != null) {
            page = clubRecruitApplyRepository.findByClubIdAndDeletedFalseOrderByCreateTimeDesc(clubId, pageable);
        } else {
            page = clubRecruitApplyRepository.findByUserIdAndDeletedFalseOrderByCreateTimeDesc(userId, pageable);
        }

        return PageResult.of(page);
    }

    public PageResult<ClubRecruitApply> getMyApplyList(Integer pageNum, Integer pageSize, Long userId) {
        log.info("获取我的申请列表 - pageNum: {}, pageSize: {}, userId: {}", pageNum, pageSize, userId);

        Pageable pageable = PageRequest.of(pageNum - 1, pageSize);
        Page<ClubRecruitApply> page = clubRecruitApplyRepository.findByUserIdAndDeletedFalseOrderByCreateTimeDesc(userId, pageable);

        return PageResult.of(page);
    }

    public String exportApplyList(Long clubId, Integer status, Long userId) {
        log.info("导出申请名单 - clubId: {}, status: {}, userId: {}", clubId, status, userId);

        Club club = clubRepository.findByIdAndDeletedFalse(clubId)
                .orElseThrow(() -> new BusinessException("社团不存在"));

        if (!club.getLeaderId().equals(userId)) {
            throw new BusinessException("只有社长才能导出申请名单");
        }

        List<ClubRecruitApply> applies;
        Pageable pageable = PageRequest.of(0, 10000);
        if (status != null) {
            ApplyStatusEnum statusEnum = status == 0 ? ApplyStatusEnum.PENDING :
                    status == 1 ? ApplyStatusEnum.APPROVED : ApplyStatusEnum.REJECTED;
            Page<ClubRecruitApply> page = clubRecruitApplyRepository.findByClubIdAndDeletedFalseOrderByCreateTimeDesc(clubId, pageable);
            applies = page.getContent().stream()
                    .filter(a -> a.getStatus() == statusEnum)
                    .collect(java.util.stream.Collectors.toList());
        } else {
            Page<ClubRecruitApply> page = clubRecruitApplyRepository.findByClubIdAndDeletedFalseOrderByCreateTimeDesc(clubId, pageable);
            applies = page.getContent();
        }

        StringBuilder sb = new StringBuilder();
        sb.append("序号,姓名,学号,院系,专业,手机号,意向部门,申请状态,审核留言\n");
        for (int i = 0; i < applies.size(); i++) {
            ClubRecruitApply a = applies.get(i);
            sb.append(String.format("%d,%s,%s,%s,%s,%s,%s,%s,%s\n",
                    i + 1,
                    a.getRealName() != null ? a.getRealName() : "",
                    a.getStudentNo() != null ? a.getStudentNo() : "",
                    a.getCollege() != null ? a.getCollege() : "",
                    a.getMajor() != null ? a.getMajor() : "",
                    a.getPhone() != null ? a.getPhone() : "",
                    a.getDepartmentName() != null ? a.getDepartmentName() : "",
                    a.getStatus() != null ? a.getStatus().getDesc() : "",
                    a.getReviewMessage() != null ? a.getReviewMessage() : ""
            ));
        }

        log.info("导出申请名单成功 - 共{}条", applies.size());
        return sb.toString();
    }
}
