package com.referee.repository;

import com.referee.entity.CompetitionAthlete;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 比赛运动员关联数据访问接口
 *
 * @author Referee System
 * @version 1.0.0
 */
@Repository
public interface CompetitionAthleteRepository extends JpaRepository<CompetitionAthlete, Long> {

    /**
     * 根据比赛ID查询参赛运动员列表
     *
     * @param competitionId 比赛ID
     * @return 关联列表
     */
    List<CompetitionAthlete> findByCompetitionId(Long competitionId);

    /**
     * 根据运动员ID查询参赛比赛列表
     *
     * @param athleteId 运动员ID
     * @return 关联列表
     */
    List<CompetitionAthlete> findByAthleteId(Long athleteId);

    /**
     * 根据比赛ID和运动员ID查询
     *
     * @param competitionId 比赛ID
     * @param athleteId     运动员ID
     * @return 关联信息
     */
    Optional<CompetitionAthlete> findByCompetitionIdAndAthleteId(Long competitionId, Long athleteId);

    /**
     * 根据比赛ID和状态查询
     *
     * @param competitionId 比赛ID
     * @param status        状态
     * @return 关联列表
     */
    List<CompetitionAthlete> findByCompetitionIdAndStatus(Long competitionId, Integer status);

    /**
     * 根据运动员ID和状态查询
     *
     * @param athleteId 运动员ID
     * @param status    状态
     * @return 关联列表
     */
    List<CompetitionAthlete> findByAthleteIdAndStatus(Long athleteId, Integer status);

    /**
     * 检查运动员是否已报名该比赛
     *
     * @param competitionId 比赛ID
     * @param athleteId     运动员ID
     * @return 是否已报名
     */
    boolean existsByCompetitionIdAndAthleteId(Long competitionId, Long athleteId);
}
