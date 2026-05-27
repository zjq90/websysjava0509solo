package com.club.repository;

import com.club.entity.Club;
import com.club.enums.ClubStatus;
import com.club.enums.ClubType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 社团数据访问接口
 *
 * @author Club Management System
 * @version 1.0.0
 */
@Repository
public interface ClubRepository extends JpaRepository<Club, Long>, JpaSpecificationExecutor<Club> {

    /**
     * 根据社团名称查询
     */
    Club findByName(String name);

    /**
     * 根据状态查询社团列表
     */
    List<Club> findByStatus(ClubStatus status);

    /**
     * 根据类型查询社团列表
     */
    List<Club> findByType(ClubType type);

    /**
     * 根据院系查询社团列表
     */
    List<Club> findByDepartment(String department);

    /**
     * 统计各类型社团数量
     */
    @Query("SELECT c.type, COUNT(c) FROM Club c GROUP BY c.type")
    List<Object[]> countByType();

    /**
     * 统计各院系社团数量
     */
    @Query("SELECT c.department, COUNT(c) FROM Club c GROUP BY c.department")
    List<Object[]> countByDepartment();

    /**
     * 统计各状态社团数量
     */
    @Query("SELECT c.status, COUNT(c) FROM Club c GROUP BY c.status")
    List<Object[]> countByStatus();

    /**
     * 计算总成员数
     */
    @Query("SELECT SUM(c.memberCount) FROM Club c")
    Integer sumMemberCount();

    /**
     * 计算年度活动总数
     */
    @Query("SELECT SUM(c.annualActivityCount) FROM Club c")
    Integer sumAnnualActivityCount();

    /**
     * 检查社团名称是否存在
     */
    boolean existsByName(String name);
}
