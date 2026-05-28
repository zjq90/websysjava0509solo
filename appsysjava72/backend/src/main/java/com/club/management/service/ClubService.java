package com.club.management.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.club.management.common.PageResult;
import com.club.management.dto.ClubApplicationDTO;
import com.club.management.entity.Club;
import com.club.management.entity.ClubApplication;
import com.club.management.entity.ClubCategory;

import java.util.List;

/**
 * 社团服务接口
 *
 * @author club-management
 * @since 2024-01-01
 */
public interface ClubService {

    /**
     * 获取社团分类列表
     */
    List<ClubCategory> getCategoryList();

    /**
     * 分页查询社团列表
     */
    PageResult<Club> getClubPage(Page<Club> page, Long categoryId, String keyword);

    /**
     * 获取社团详情
     */
    Club getClubDetail(Long id);

    /**
     * 提交入团申请
     */
    void submitApplication(ClubApplicationDTO applicationDTO);

    /**
     * 获取我的入团申请列表
     */
    List<ClubApplication> getMyApplications();

    /**
     * 获取申请详情
     */
    ClubApplication getApplicationDetail(Long id);

    /**
     * 获取我加入的社团列表
     */
    List<Club> getMyClubs();

    /**
     * 检查是否已加入社团
     */
    boolean isJoinedClub(Long clubId);

    /**
     * 退出社团
     */
    void quitClub(Long clubId);
}
