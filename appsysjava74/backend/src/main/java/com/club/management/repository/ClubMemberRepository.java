package com.club.management.repository;

import com.club.management.entity.ClubMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 社团成员数据访问接口
 * 
 * @author club-management
 * @version 1.0.0
 */
@Repository
public interface ClubMemberRepository extends JpaRepository<ClubMember, Long> {

    /**
     * 根据社团ID和用户ID查询成员
     */
    Optional<ClubMember> findByClubIdAndUserIdAndDeletedFalse(Long clubId, Long userId);

    /**
     * 根据社团ID查询成员列表
     */
    List<ClubMember> findByClubIdAndStatusAndDeletedFalse(Long clubId, Integer status);

    /**
     * 根据用户ID查询已加入的社团ID列表
     */
    List<ClubMember> findByUserIdAndStatusAndDeletedFalse(Long userId, Integer status);

    /**
     * 检查用户是否为社团成员
     */
    boolean existsByClubIdAndUserIdAndStatusAndDeletedFalse(Long clubId, Long userId, Integer status);

    /**
     * 统计社团成员数量
     */
    long countByClubIdAndStatusAndDeletedFalse(Long clubId, Integer status);
}
