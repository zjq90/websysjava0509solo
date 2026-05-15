package com.referee.service;

import com.referee.common.Result;
import com.referee.entity.Athlete;
import com.referee.entity.Competition;
import com.referee.entity.CompetitionAthlete;
import com.referee.repository.AthleteRepository;
import com.referee.repository.CompetitionAthleteRepository;
import com.referee.repository.CompetitionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * 比赛服务类
 *
 * @author Referee System
 * @version 1.0.0
 */
@Service
public class CompetitionService {

    @Autowired
    private CompetitionRepository competitionRepository;

    @Autowired
    private AthleteRepository athleteRepository;

    @Autowired
    private CompetitionAthleteRepository competitionAthleteRepository;

    /**
     * 获取所有比赛列表（排除已删除/已取消的比赛）
     *
     * @return 比赛列表
     */
    public Result<List<Competition>> getAllCompetitions() {
        return Result.success(competitionRepository.findByStatusNot(3));
    }

    /**
     * 根据ID查询比赛
     *
     * @param id 比赛ID
     * @return 比赛信息
     */
    public Result<Competition> getCompetitionById(Long id) {
        Optional<Competition> competition = competitionRepository.findById(id);
        return competition.map(Result::success).orElseGet(() -> Result.error("比赛不存在"));
    }

    /**
     * 新增比赛
     *
     * @param competition 比赛信息
     * @return 新增结果
     */
    public Result<Competition> addCompetition(Competition competition) {
        if (competitionRepository.existsByCompetitionNo(competition.getCompetitionNo())) {
            return Result.error("比赛编号已存在");
        }
        return Result.success(competitionRepository.save(competition));
    }

    /**
     * 更新比赛信息
     *
     * @param id          比赛ID
     * @param competition 比赛信息
     * @return 更新结果
     */
    public Result<Competition> updateCompetition(Long id, Competition competition) {
        if (!competitionRepository.existsById(id)) {
            return Result.error("比赛不存在");
        }
        competition.setId(id);
        return Result.success(competitionRepository.save(competition));
    }

    /**
     * 删除比赛
     *
     * @param id 比赛ID
     * @return 删除结果
     */
    @Transactional
    public Result<String> deleteCompetition(Long id) {
        if (!competitionRepository.existsById(id)) {
            return Result.error("比赛不存在");
        }

        List<CompetitionAthlete> athletes = competitionAthleteRepository.findByCompetitionId(id);
        for (CompetitionAthlete ca : athletes) {
            ca.setStatus(0);
            competitionAthleteRepository.save(ca);
        }

        Competition competition = competitionRepository.findById(id).get();
        competition.setStatus(3);
        competitionRepository.save(competition);

        return Result.success("删除成功");
    }

    /**
     * 添加运动员到比赛
     *
     * @param competitionId 比赛ID
     * @param athleteId     运动员ID
     * @return 添加结果
     */
    @Transactional
    public Result<String> addAthleteToCompetition(Long competitionId, Long athleteId) {
        if (!competitionRepository.existsById(competitionId)) {
            return Result.error("比赛不存在");
        }
        if (!athleteRepository.existsById(athleteId)) {
            return Result.error("运动员不存在");
        }
        if (competitionAthleteRepository.existsByCompetitionIdAndAthleteId(competitionId, athleteId)) {
            return Result.error("该运动员已报名此比赛");
        }

        Competition competition = competitionRepository.findById(competitionId).get();
        Athlete athlete = athleteRepository.findById(athleteId).get();

        CompetitionAthlete competitionAthlete = new CompetitionAthlete();
        competitionAthlete.setCompetitionId(competitionId);
        competitionAthlete.setCompetitionNo(competition.getCompetitionNo());
        competitionAthlete.setCompetitionName(competition.getName());
        competitionAthlete.setAthleteId(athleteId);
        competitionAthlete.setAthleteNo(athlete.getAthleteNo());
        competitionAthlete.setAthleteName(athlete.getName());
        competitionAthlete.setCheckInStatus(0);
        competitionAthlete.setStatus(1);

        competitionAthleteRepository.save(competitionAthlete);
        return Result.success("添加成功");
    }

    /**
     * 移除比赛中的运动员
     *
     * @param competitionId 比赛ID
     * @param athleteId     运动员ID
     * @return 移除结果
     */
    @Transactional
    public Result<String> removeAthleteFromCompetition(Long competitionId, Long athleteId) {
        Optional<CompetitionAthlete> competitionAthlete = 
            competitionAthleteRepository.findByCompetitionIdAndAthleteId(competitionId, athleteId);
        
        if (!competitionAthlete.isPresent()) {
            return Result.error("该运动员未报名此比赛");
        }

        CompetitionAthlete ca = competitionAthlete.get();
        ca.setStatus(0);
        competitionAthleteRepository.save(ca);

        return Result.success("移除成功");
    }

    /**
     * 获取比赛的参赛运动员列表
     *
     * @param competitionId 比赛ID
     * @return 运动员列表
     */
    public Result<List<CompetitionAthlete>> getCompetitionAthletes(Long competitionId) {
        return Result.success(competitionAthleteRepository.findByCompetitionIdAndStatus(competitionId, 1));
    }

    /**
     * 获取运动员的参赛比赛列表
     *
     * @param athleteId 运动员ID
     * @return 比赛列表
     */
    public Result<List<CompetitionAthlete>> getAthleteCompetitions(Long athleteId) {
        return Result.success(competitionAthleteRepository.findByAthleteIdAndStatus(athleteId, 1));
    }

    /**
     * 签到
     *
     * @param competitionId 比赛ID
     * @param athleteId     运动员ID
     * @return 签到结果
     */
    @Transactional
    public Result<String> checkIn(Long competitionId, Long athleteId) {
        Optional<CompetitionAthlete> competitionAthlete = 
            competitionAthleteRepository.findByCompetitionIdAndAthleteId(competitionId, athleteId);
        
        if (!competitionAthlete.isPresent()) {
            return Result.error("该运动员未报名此比赛");
        }

        CompetitionAthlete ca = competitionAthlete.get();
        ca.setCheckInStatus(1);
        ca.setCheckInTime(new Date());
        competitionAthleteRepository.save(ca);

        return Result.success("签到成功");
    }

    /**
     * 模糊查询比赛
     *
     * @param keyword 关键词
     * @return 比赛列表
     */
    public Result<List<Competition>> searchCompetitions(String keyword) {
        return Result.success(competitionRepository.findByKeyword(keyword));
    }
}
