package com.referee.repository;

import com.referee.entity.Athlete;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 运动员数据访问接口
 *
 * @author Referee System
 * @version 1.0.0
 */
@Repository
public interface AthleteRepository extends JpaRepository<Athlete, Long> {

    /**
     * 根据运动员编号查询
     *
     * @param athleteNo 运动员编号
     * @return 运动员信息
     */
    Optional<Athlete> findByAthleteNo(String athleteNo);

    /**
     * 根据姓名查询
     *
     * @param name 姓名
     * @return 运动员列表
     */
    List<Athlete> findByNameContaining(String name);

    /**
     * 根据学校查询
     *
     * @param school 学校
     * @return 运动员列表
     */
    List<Athlete> findBySchoolContaining(String school);

    /**
     * 根据状态查询
     *
     * @param status 状态
     * @return 运动员列表
     */
    List<Athlete> findByStatus(Integer status);

    /**
     * 根据参赛项目查询
     *
     * @param event 参赛项目
     * @return 运动员列表
     */
    List<Athlete> findByEventContaining(String event);

    /**
     * 检查运动员编号是否存在
     *
     * @param athleteNo 运动员编号
     * @return 是否存在
     */
    boolean existsByAthleteNo(String athleteNo);

    /**
     * 模糊查询
     *
     * @param keyword 关键词
     * @return 运动员列表
     */
    @Query("SELECT a FROM Athlete a WHERE a.athleteNo LIKE %?1% OR a.name LIKE %?1% OR a.school LIKE %?1%")
    List<Athlete> findByKeyword(String keyword);
}
