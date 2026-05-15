package com.referee.repository;

import com.referee.entity.Competition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 比赛数据访问接口
 *
 * @author Referee System
 * @version 1.0.0
 */
@Repository
public interface CompetitionRepository extends JpaRepository<Competition, Long> {

    /**
     * 根据比赛编号查询
     *
     * @param competitionNo 比赛编号
     * @return 比赛信息
     */
    Optional<Competition> findByCompetitionNo(String competitionNo);

    /**
     * 根据比赛名称查询
     *
     * @param name 比赛名称
     * @return 比赛列表
     */
    List<Competition> findByNameContaining(String name);

    /**
     * 根据比赛项目查询
     *
     * @param event 比赛项目
     * @return 比赛列表
     */
    List<Competition> findByEventContaining(String event);

    /**
     * 根据状态查询
     *
     * @param status 状态
     * @return 比赛列表
     */
    List<Competition> findByStatus(Integer status);

    /**
     * 根据裁判长ID查询
     *
     * @param chiefRefereeId 裁判长ID
     * @return 比赛列表
     */
    List<Competition> findByChiefRefereeId(Long chiefRefereeId);

    /**
     * 检查比赛编号是否存在
     *
     * @param competitionNo 比赛编号
     * @return 是否存在
     */
    boolean existsByCompetitionNo(String competitionNo);

    /**
     * 根据状态排除查询
     *
     * @param status 要排除的状态
     * @return 比赛列表
     */
    List<Competition> findByStatusNot(Integer status);

    /**
     * 模糊查询
     *
     * @param keyword 关键词
     * @return 比赛列表
     */
    @Query("SELECT c FROM Competition c WHERE c.competitionNo LIKE %?1% OR c.name LIKE %?1% OR c.event LIKE %?1%")
    List<Competition> findByKeyword(String keyword);
}
