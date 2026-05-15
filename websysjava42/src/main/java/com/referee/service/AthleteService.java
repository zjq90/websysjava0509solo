package com.referee.service;

import com.referee.common.Result;
import com.referee.entity.Athlete;
import com.referee.entity.User;
import com.referee.repository.AthleteRepository;
import com.referee.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * 运动员服务类
 *
 * @author Referee System
 * @version 1.0.0
 */
@Service
public class AthleteService {

    @Autowired
    private AthleteRepository athleteRepository;

    @Autowired
    private UserRepository userRepository;

    /**
     * 获取所有在职运动员列表
     *
     * @return 运动员列表
     */
    public Result<List<Athlete>> getAllAthletes() {
        return Result.success(athleteRepository.findByStatus(1));
    }

    /**
     * 根据ID查询运动员
     *
     * @param id 运动员ID
     * @return 运动员信息
     */
    public Result<Athlete> getAthleteById(Long id) {
        Optional<Athlete> athlete = athleteRepository.findById(id);
        return athlete.map(Result::success).orElseGet(() -> Result.error("运动员不存在"));
    }

    /**
     * 新增运动员
     *
     * @param athlete 运动员信息
     * @return 新增结果
     */
    @Transactional
    public Result<Athlete> addAthlete(Athlete athlete) {
        if (athleteRepository.existsByAthleteNo(athlete.getAthleteNo())) {
            return Result.error("运动员编号已存在");
        }

        Athlete savedAthlete = athleteRepository.save(athlete);

        User user = new User();
        user.setUsername("athlete_" + savedAthlete.getAthleteNo());
        user.setPassword("123456");
        user.setRealName(savedAthlete.getName());
        user.setRole("ATHLETE");
        user.setPhone(savedAthlete.getPhone());
        user.setEmail(savedAthlete.getEmail());
        user.setStatus(1);
        user.setAthleteId(savedAthlete.getId());
        userRepository.save(user);

        return Result.success(savedAthlete);
    }

    /**
     * 更新运动员信息
     *
     * @param id       运动员ID
     * @param athlete  运动员信息
     * @return 更新结果
     */
    @Transactional
    public Result<Athlete> updateAthlete(Long id, Athlete athlete) {
        Optional<Athlete> existingAthlete = athleteRepository.findById(id);
        if (!existingAthlete.isPresent()) {
            return Result.error("运动员不存在");
        }

        athlete.setId(id);
        Athlete updatedAthlete = athleteRepository.save(athlete);

        Optional<User> userOptional = userRepository.findByAthleteId(id);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            user.setRealName(athlete.getName());
            user.setPhone(athlete.getPhone());
            user.setEmail(athlete.getEmail());
            userRepository.save(user);
        }

        return Result.success(updatedAthlete);
    }

    /**
     * 删除运动员
     *
     * @param id 运动员ID
     * @return 删除结果
     */
    @Transactional
    public Result<String> deleteAthlete(Long id) {
        if (!athleteRepository.existsById(id)) {
            return Result.error("运动员不存在");
        }

        Optional<User> userOptional = userRepository.findByAthleteId(id);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            user.setStatus(0);
            userRepository.save(user);
        }

        Athlete athlete = athleteRepository.findById(id).get();
        athlete.setStatus(0);
        athleteRepository.save(athlete);

        return Result.success("删除成功");
    }

    /**
     * 对运动员进行评价
     *
     * @param id          运动员ID
     * @param evaluation  评价内容
     * @return 评价结果
     */
    @Transactional
    public Result<String> evaluateAthlete(Long id, String evaluation) {
        Optional<Athlete> athleteOptional = athleteRepository.findById(id);
        if (!athleteOptional.isPresent()) {
            return Result.error("运动员不存在");
        }

        Athlete athlete = athleteOptional.get();
        athlete.setEvaluation(evaluation);
        athleteRepository.save(athlete);

        return Result.success("评价成功");
    }

    /**
     * 模糊查询运动员
     *
     * @param keyword 关键词
     * @return 运动员列表
     */
    public Result<List<Athlete>> searchAthletes(String keyword) {
        return Result.success(athleteRepository.findByKeyword(keyword));
    }
}
