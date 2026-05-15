package com.referee.repository;

import com.referee.entity.Score;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 评分数据访问接口
 *
 * @author Referee System
 * @version 1.0.0
 */
@Repository
public interface ScoreRepository extends JpaRepository<Score, Long> {

    /**
     * 根据比赛ID查询评分列表
     *
     * @param competitionId 比赛ID
     * @return 评分列表
     */
    List<Score> findByCompetitionId(Long competitionId);

    /**
     * 根据运动员ID查询评分列表
     *
     * @param athleteId 运动员ID
     * @return 评分列表
     */
    List<Score> findByAthleteId(Long athleteId);

    /**
     * 根据裁判ID查询评分列表
     *
     * @param refereeId 裁判ID
     * @return 评分列表
     */
    List<Score> findByRefereeId(Long refereeId);

    /**
     * 根据状态查询评分列表
     *
     * @param status 状态
     * @return 评分列表
     */
    List<Score> findByStatus(Integer status);

    /**
     * 根据比赛ID和运动员ID查询评分
     *
     * @param competitionId 比赛ID
     * @param athleteId     运动员ID
     * @return 评分列表
     */
    List<Score> findByCompetitionIdAndAthleteId(Long competitionId, Long athleteId);

    /**
     * 根据比赛ID、运动员ID和裁判ID查询
     *
     * @param competitionId 比赛ID
     * @param athleteId     运动员ID
     * @param refereeId     裁判ID
     * @return 评分信息
     */
    Optional<Score> findByCompetitionIdAndAthleteIdAndRefereeId(Long competitionId, Long athleteId, Long refereeId);

    /**
     * 根据审核状态查询
     *
     * @param auditStatus 审核状态
     * @return 评分列表
     */
    List<Score> findByAuditStatus(Integer auditStatus);

    /**
     * 根据比赛ID和审核状态查询
     *
     * @param competitionId 比赛ID
     * @param auditStatus   审核状态
     * @return 评分列表
     */
    List<Score> findByCompetitionIdAndAuditStatus(Long competitionId, Integer auditStatus);

    /**
     * 查询某比赛某运动员的最终得分（已审核通过的平均分）
     *
     * @param competitionId 比赛ID
     * @param athleteId     运动员ID
     * @return 最终得分
     */
    @Query("SELECT AVG(s.totalScore) FROM Score s WHERE s.competitionId = ?1 AND s.athleteId = ?2 AND s.auditStatus = 1 AND s.status = 1")
    Double findFinalScoreByCompetitionIdAndAthleteId(Long competitionId, Long athleteId);

    /**
     * 检查是否已存在该裁判对该运动员该比赛的评分
     *
     * @param competitionId 比赛ID
     * @param athleteId     运动员ID
     * @param refereeId     裁判ID
     * @return 是否已评分
     */
    boolean existsByCompetitionIdAndAthleteIdAndRefereeId(Long competitionId, Long athleteId, Long refereeId);
}
