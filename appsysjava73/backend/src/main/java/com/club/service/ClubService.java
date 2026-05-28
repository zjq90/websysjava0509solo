package com.club.service;

import com.club.common.PageResult;
import com.club.entity.*;
import com.club.entity.enums.ClubCategoryEnum;
import com.club.exception.BusinessException;
import com.club.repository.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class ClubService {

    @Autowired
    private ClubRepository clubRepository;

    @Autowired
    private ClubFollowRepository clubFollowRepository;

    @Autowired
    private ClubMemberRepository clubMemberRepository;

    @Autowired
    private ActivityPhotoRepository activityPhotoRepository;

    @Autowired
    private ClubHonorRepository clubHonorRepository;

    @Autowired
    private ClubMilestoneRepository clubMilestoneRepository;

    @Autowired
    private PastPresidentRepository pastPresidentRepository;

    @Autowired
    private ClubActivityRepository clubActivityRepository;

    public PageResult<Club> getClubList(Integer pageNum, Integer pageSize, String category, Long schoolId, String keyword) {
        log.info("查询社团列表 - pageNum: {}, pageSize: {}, category: {}, schoolId: {}, keyword: {}", pageNum, pageSize, category, schoolId, keyword);

        Pageable pageable = PageRequest.of(pageNum - 1, pageSize);
        Page<Club> page;

        ClubCategoryEnum categoryEnum = category != null ? ClubCategoryEnum.valueOf(category) : null;

        if (keyword != null && !keyword.isEmpty()) {
            page = clubRepository.findByNameContainingAndStatusAndDeletedFalseOrderByViewCountDesc(keyword, 1, pageable);
        } else if (categoryEnum != null && schoolId != null) {
            page = clubRepository.findByCategoryAndSchoolIdAndStatusAndDeletedFalseOrderByViewCountDesc(categoryEnum, schoolId, 1, pageable);
        } else if (categoryEnum != null) {
            page = clubRepository.findByCategoryAndStatusAndDeletedFalseOrderByViewCountDesc(categoryEnum, 1, pageable);
        } else if (schoolId != null) {
            page = clubRepository.findBySchoolIdAndStatusAndDeletedFalseOrderByViewCountDesc(schoolId, 1, pageable);
        } else {
            page = clubRepository.findByStatusAndDeletedFalseOrderByViewCountDesc(1, pageable);
        }

        return PageResult.of(page);
    }

    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> getClubDetail(Long clubId) {
        log.info("获取社团详情 - clubId: {}", clubId);

        Club club = clubRepository.findByIdAndDeletedFalse(clubId)
                .orElseThrow(() -> new BusinessException("社团不存在"));

        clubRepository.incrementViewCount(clubId);

        Map<String, Object> result = new HashMap<>();
        result.put("club", club);

        boolean isMember = clubMemberRepository.existsByClubIdAndUserIdAndDeletedFalse(clubId, club.getLeaderId());
        result.put("isMember", isMember);

        List<ActivityPhoto> photos = activityPhotoRepository.findByClubIdAndDeletedFalseOrderBySortOrderAsc(clubId);
        result.put("photos", photos);

        List<ClubHonor> honors = clubHonorRepository.findByClubIdAndDeletedFalseOrderBySortOrderAsc(clubId);
        result.put("honors", honors);

        List<ClubActivity> activities = clubActivityRepository.findByClubIdAndDeletedFalseOrderByStartTimeDesc(clubId);
        result.put("recentActivities", activities.size() > 5 ? activities.subList(0, 5) : activities);

        return result;
    }

    @Transactional(rollbackFor = Exception.class)
    public Club createClub(Club club, Long userId) {
        log.info("创建社团 - name: {}, schoolId: {}, userId: {}", club.getName(), club.getSchoolId(), userId);

        if (clubRepository.existsByNameAndDeletedFalse(club.getName())) {
            throw new BusinessException("社团名称已存在");
        }

        club.setStatus(1);
        club.setMemberCount(1);
        club.setFollowCount(0);
        club.setViewCount(0);
        club.setRecruiting(0);
        club.setLeaderId(userId);

        Club saved = clubRepository.save(club);
        log.info("社团创建成功 - clubId: {}", saved.getId());
        return saved;
    }

    @Transactional(rollbackFor = Exception.class)
    public Club updateClub(Long clubId, Club club, Long userId) {
        log.info("更新社团信息 - clubId: {}, userId: {}", clubId, userId);

        Club existClub = clubRepository.findByIdAndDeletedFalse(clubId)
                .orElseThrow(() -> new BusinessException("社团不存在"));

        if (club.getName() != null && !club.getName().equals(existClub.getName())) {
            if (clubRepository.existsByNameAndDeletedFalse(club.getName())) {
                throw new BusinessException("社团名称已存在");
            }
            existClub.setName(club.getName());
        }

        if (club.getLogo() != null) existClub.setLogo(club.getLogo());
        if (club.getCategory() != null) existClub.setCategory(club.getCategory());
        if (club.getDescription() != null) existClub.setDescription(club.getDescription());
        if (club.getPurpose() != null) existClub.setPurpose(club.getPurpose());
        if (club.getContactPhone() != null) existClub.setContactPhone(club.getContactPhone());
        if (club.getContactEmail() != null) existClub.setContactEmail(club.getContactEmail());
        if (club.getQqGroup() != null) existClub.setQqGroup(club.getQqGroup());
        if (club.getWechat() != null) existClub.setWechat(club.getWechat());
        if (club.getEstablishDate() != null) existClub.setEstablishDate(club.getEstablishDate());

        Club saved = clubRepository.save(existClub);
        log.info("社团信息更新成功 - clubId: {}", clubId);
        return saved;
    }

    public List<Club> getMyManagedClubs(Long userId) {
        log.info("获取我管理的社团 - userId: {}", userId);
        return clubRepository.findByLeaderIdAndDeletedFalse(userId);
    }

    public PageResult<Club> getHotClubs(Integer pageNum, Integer pageSize) {
        log.info("获取热门社团 - pageNum: {}, pageSize: {}", pageNum, pageSize);
        Pageable pageable = PageRequest.of(pageNum - 1, pageSize);
        Page<Club> page = clubRepository.findByStatusAndDeletedFalseOrderByViewCountDesc(1, pageable);
        return PageResult.of(page);
    }

    public List<ActivityPhoto> getClubPhotos(Long clubId) {
        log.info("获取社团相册 - clubId: {}", clubId);
        return activityPhotoRepository.findByClubIdAndDeletedFalseOrderBySortOrderAsc(clubId);
    }

    public List<ClubHonor> getClubHonors(Long clubId) {
        log.info("获取社团荣誉 - clubId: {}", clubId);
        return clubHonorRepository.findByClubIdAndDeletedFalseOrderBySortOrderAsc(clubId);
    }

    public List<ClubMilestone> getClubMilestones(Long clubId) {
        log.info("获取社团大事记 - clubId: {}", clubId);
        return clubMilestoneRepository.findByClubIdAndDeletedFalseOrderByEventDateDescSortOrderAsc(clubId);
    }

    public List<PastPresident> getPastPresidents(Long clubId) {
        log.info("获取历任社长 - clubId: {}", clubId);
        return pastPresidentRepository.findByClubIdAndDeletedFalseOrderBySortOrderAsc(clubId);
    }
}
