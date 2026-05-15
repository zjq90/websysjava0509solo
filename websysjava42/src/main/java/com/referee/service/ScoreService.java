package com.referee.service;

import com.referee.common.Result;
import com.referee.entity.Score;
import com.referee.repository.AthleteRepository;
import com.referee.repository.CompetitionRepository;
import com.referee.repository.RefereeRepository;
import com.referee.repository.ScoreRepository;
import lombok.var;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * 评分服务类
 *
 * @author Referee System
 * @version 1.0.0
 */
@Service
public class ScoreService {

    @Autowired
    private ScoreRepository scoreRepository;

    @Autowired
    private CompetitionRepository competitionRepository;

    @Autowired
    private AthleteRepository athleteRepository;

    @Autowired
    private RefereeRepository refereeRepository;

    /**
     * 获取所有有效评分列表
     *
     * @return 评分列表
     */
    public Result<List<Score>> getAllScores() {
        return Result.success(scoreRepository.findByStatus(1));
    }

    /**
     * 根据ID查询评分
     *
     * @param id 评分ID
     * @return 评分信息
     */
    public Result<Score> getScoreById(Long id) {
        Optional<Score> score = scoreRepository.findById(id);
        return score.map(Result::success).orElseGet(() -> Result.error("评分不存在"));
    }

    /**
     * 提交评分
     *
     * @param score 评分信息
     * @return 提交结果
     */
    @Transactional
    public Result<Score> submitScore(Score score) {
        if (!competitionRepository.existsById(score.getCompetitionId())) {
            return Result.error("比赛不存在");
        }
        if (!athleteRepository.existsById(score.getAthleteId())) {
            return Result.error("运动员不存在");
        }
        if (!refereeRepository.existsById(score.getRefereeId())) {
            return Result.error("裁判不存在");
        }
        if (scoreRepository.existsByCompetitionIdAndAthleteIdAndRefereeId(
                score.getCompetitionId(), score.getAthleteId(), score.getRefereeId())) {
            return Result.error("该裁判已对该运动员评分过");
        }

        score.setScoreTime(new Date());
        score.setAuditStatus(0);
        score.setStatus(1);

        if (score.getTechnicalScore() == null) score.setTechnicalScore(0.0);
        if (score.getPerformanceScore() == null) score.setPerformanceScore(0.0);
        if (score.getTotalScore() == null) {
            score.setTotalScore(score.getTechnicalScore() + score.getPerformanceScore());
        }

        var competition = competitionRepository.findById(score.getCompetitionId()).get();
        var athlete = athleteRepository.findById(score.getAthleteId()).get();
        var referee = refereeRepository.findById(score.getRefereeId()).get();

        score.setCompetitionNo(competition.getCompetitionNo());
        score.setCompetitionName(competition.getName());
        score.setAthleteNo(athlete.getAthleteNo());
        score.setAthleteName(athlete.getName());
        score.setRefereeNo(referee.getRefereeNo());
        score.setRefereeName(referee.getName());

        return Result.success(scoreRepository.save(score));
    }

    /**
     * 更新评分
     *
     * @param id    评分ID
     * @param score 评分信息
     * @return 更新结果
     */
    @Transactional
    public Result<Score> updateScore(Long id, Score score) {
        Optional<Score> existingScoreOptional = scoreRepository.findById(id);
        if (!existingScoreOptional.isPresent()) {
            return Result.error("评分不存在");
        }

        Score existingScore = existingScoreOptional.get();
        if (existingScore.getAuditStatus() == 1) {
            return Result.error("已审核通过的评分不能修改");
        }

        existingScore.setTechnicalScore(score.getTechnicalScore() != null ? score.getTechnicalScore() : existingScore.getTechnicalScore());
        existingScore.setPerformanceScore(score.getPerformanceScore() != null ? score.getPerformanceScore() : existingScore.getPerformanceScore());
        existingScore.setComment(score.getComment() != null ? score.getComment() : existingScore.getComment());
        
        if (existingScore.getTechnicalScore() == null) existingScore.setTechnicalScore(0.0);
        if (existingScore.getPerformanceScore() == null) existingScore.setPerformanceScore(0.0);
        existingScore.setTotalScore(existingScore.getTechnicalScore() + existingScore.getPerformanceScore());
        
        existingScore.setAuditStatus(0);
        existingScore.setScoreTime(new Date());

        return Result.success(scoreRepository.save(existingScore));
    }

    /**
     * 审核评分
     *
     * @param id           评分ID
     * @param auditStatus  审核状态 1-通过 2-驳回
     * @param auditorId    审核人ID
     * @param auditComment 审核意见
     * @return 审核结果
     */
    @Transactional
    public Result<String> auditScore(Long id, Integer auditStatus, Long auditorId, String auditComment) {
        Optional<Score> scoreOptional = scoreRepository.findById(id);
        if (!scoreOptional.isPresent()) {
            return Result.error("评分不存在");
        }

        Score score = scoreOptional.get();
        if (score.getAuditStatus() == 1) {
            return Result.error("该评分已审核通过，不能重复审核");
        }

        score.setAuditStatus(auditStatus);
        score.setAuditorId(auditorId);
        score.setAuditTime(new Date());
        score.setAuditComment(auditComment);

        var referee = refereeRepository.findById(auditorId);
        referee.ifPresent(value -> score.setAuditorName(value.getName()));

        scoreRepository.save(score);

        return Result.success(auditStatus == 1 ? "审核通过" : "审核驳回");
    }

    /**
     * 根据比赛ID查询评分
     *
     * @param competitionId 比赛ID
     * @return 评分列表
     */
    public Result<List<Score>> getScoresByCompetition(Long competitionId) {
        return Result.success(scoreRepository.findByCompetitionId(competitionId));
    }

    /**
     * 根据运动员ID查询评分
     *
     * @param athleteId 运动员ID
     * @return 评分列表
     */
    public Result<List<Score>> getScoresByAthlete(Long athleteId) {
        return Result.success(scoreRepository.findByAthleteId(athleteId));
    }

    /**
     * 根据裁判ID查询评分
     *
     * @param refereeId 裁判ID
     * @return 评分列表
     */
    public Result<List<Score>> getScoresByReferee(Long refereeId) {
        return Result.success(scoreRepository.findByRefereeId(refereeId));
    }

    /**
     * 获取待审核评分列表
     *
     * @return 评分列表
     */
    public Result<List<Score>> getPendingAuditScores() {
        return Result.success(scoreRepository.findByAuditStatus(0));
    }

    /**
     * 获取运动员最终得分
     *
     * @param competitionId 比赛ID
     * @param athleteId     运动员ID
     * @return 最终得分
     */
    public Result<Double> getFinalScore(Long competitionId, Long athleteId) {
        Double finalScore = scoreRepository.findFinalScoreByCompetitionIdAndAthleteId(competitionId, athleteId);
        return Result.success(finalScore != null ? finalScore : 0.0);
    }
}
