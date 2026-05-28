package com.club.management.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.club.management.common.PageResult;
import com.club.management.common.ResultCode;
import com.club.management.dto.ClubApplicationDTO;
import com.club.management.entity.Club;
import com.club.management.entity.ClubApplication;
import com.club.management.entity.ClubCategory;
import com.club.management.entity.ClubMember;
import com.club.management.exception.BusinessException;
import com.club.management.mapper.ClubApplicationMapper;
import com.club.management.mapper.ClubCategoryMapper;
import com.club.management.mapper.ClubMapper;
import com.club.management.mapper.ClubMemberMapper;
import com.club.management.service.ClubService;
import com.club.management.utils.UserContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 社团服务实现类
 *
 * @author club-management
 * @since 2024-01-01
 */
@Slf4j
@Service
public class ClubServiceImpl implements ClubService {

    @Autowired
    private ClubMapper clubMapper;

    @Autowired
    private ClubCategoryMapper clubCategoryMapper;

    @Autowired
    private ClubMemberMapper clubMemberMapper;

    @Autowired
    private ClubApplicationMapper clubApplicationMapper;

    @Override
    public List<ClubCategory> getCategoryList() {
        LambdaQueryWrapper<ClubCategory> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByAsc(ClubCategory::getSort);
        List<ClubCategory> categories = clubCategoryMapper.selectList(wrapper);

        log.debug("获取社团分类列表, 数量: {}", categories.size());

        return categories;
    }

    @Override
    public PageResult<Club> getClubPage(Page<Club> page, Long categoryId, String keyword) {
        LambdaQueryWrapper<Club> wrapper = new LambdaQueryWrapper<>();

        if (categoryId != null) {
            wrapper.eq(Club::getCategoryId, categoryId);
        }

        if (StringUtils.hasText(keyword)) {
            wrapper.like(Club::getName, keyword)
                    .or().like(Club::getDescription, keyword);
        }

        wrapper.eq(Club::getStatus, 1);
        wrapper.orderByDesc(Club::getMemberCount);

        Page<Club> clubPage = clubMapper.selectPage(page, wrapper);

        log.debug("分页查询社团列表, 分类ID: {}, 关键词: {}, 总数: {}", categoryId, keyword, clubPage.getTotal());

        return new PageResult<>(clubPage.getTotal(), clubPage.getCurrent(), clubPage.getSize(), clubPage.getRecords());
    }

    @Override
    public Club getClubDetail(Long id) {
        Club club = clubMapper.selectById(id);
        if (club == null) {
            throw new BusinessException(ResultCode.CLUB_NOT_EXIST);
        }

        log.debug("获取社团详情, 社团ID: {}", id);

        return club;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void submitApplication(ClubApplicationDTO applicationDTO) {
        Long userId = UserContext.getUserId();
        Long clubId = applicationDTO.getClubId();

        Club club = clubMapper.selectById(clubId);
        if (club == null) {
            throw new BusinessException(ResultCode.CLUB_NOT_EXIST);
        }

        if (isJoinedClub(clubId)) {
            throw new BusinessException(ResultCode.CLUB_ALREADY_JOINED);
        }

        LambdaQueryWrapper<ClubApplication> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ClubApplication::getClubId, clubId)
                .eq(ClubApplication::getUserId, userId)
                .in(ClubApplication::getStatus, 0, 1);
        if (clubApplicationMapper.selectCount(wrapper) > 0) {
            throw new BusinessException(ResultCode.APPLICATION_ALREADY_EXIST);
        }

        if (club.getMemberCount() >= club.getMaxMembers()) {
            throw new BusinessException(ResultCode.CLUB_FULL);
        }

        ClubApplication application = new ClubApplication();
        application.setClubId(clubId);
        application.setUserId(userId);
        application.setReason(applicationDTO.getReason());
        application.setResume(applicationDTO.getResume());
        application.setWorks(applicationDTO.getWorks());
        application.setStatus(0);

        clubApplicationMapper.insert(application);

        log.info("提交入团申请成功, 用户ID: {}, 社团ID: {}", userId, clubId);
    }

    @Override
    public List<ClubApplication> getMyApplications() {
        Long userId = UserContext.getUserId();

        LambdaQueryWrapper<ClubApplication> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ClubApplication::getUserId, userId);
        wrapper.orderByDesc(ClubApplication::getCreateTime);

        List<ClubApplication> applications = clubApplicationMapper.selectList(wrapper);

        log.debug("获取我的入团申请列表, 用户ID: {}, 数量: {}", userId, applications.size());

        return applications;
    }

    @Override
    public ClubApplication getApplicationDetail(Long id) {
        Long userId = UserContext.getUserId();

        ClubApplication application = clubApplicationMapper.selectById(id);
        if (application == null) {
            throw new BusinessException(ResultCode.APPLICATION_NOT_EXIST);
        }

        if (!application.getUserId().equals(userId)) {
            throw new BusinessException(ResultCode.FORBIDDEN);
        }

        log.debug("获取申请详情, 申请ID: {}", id);

        return application;
    }

    @Override
    public List<Club> getMyClubs() {
        Long userId = UserContext.getUserId();

        LambdaQueryWrapper<ClubMember> memberWrapper = new LambdaQueryWrapper<>();
        memberWrapper.eq(ClubMember::getUserId, userId);
        List<ClubMember> members = clubMemberMapper.selectList(memberWrapper);

        List<Long> clubIds = members.stream()
                .map(ClubMember::getClubId)
                .collect(Collectors.toList());

        if (clubIds.isEmpty()) {
            return List.of();
        }

        LambdaQueryWrapper<Club> clubWrapper = new LambdaQueryWrapper<>();
        clubWrapper.in(Club::getId, clubIds)
                .eq(Club::getStatus, 1);

        List<Club> clubs = clubMapper.selectList(clubWrapper);

        log.debug("获取我加入的社团列表, 用户ID: {}, 数量: {}", userId, clubs.size());

        return clubs;
    }

    @Override
    public boolean isJoinedClub(Long clubId) {
        Long userId = UserContext.getUserId();

        LambdaQueryWrapper<ClubMember> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ClubMember::getClubId, clubId)
                .eq(ClubMember::getUserId, userId);

        return clubMemberMapper.selectCount(wrapper) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void quitClub(Long clubId) {
        Long userId = UserContext.getUserId();

        if (!isJoinedClub(clubId)) {
            throw new BusinessException("未加入该社团");
        }

        LambdaQueryWrapper<ClubMember> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ClubMember::getClubId, clubId)
                .eq(ClubMember::getUserId, userId);
        clubMemberMapper.delete(wrapper);

        Club club = clubMapper.selectById(clubId);
        if (club != null && club.getMemberCount() > 0) {
            club.setMemberCount(club.getMemberCount() - 1);
            clubMapper.updateById(club);
        }

        log.info("退出社团成功, 用户ID: {}, 社团ID: {}", userId, clubId);
    }
}
