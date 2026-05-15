package com.referee.service;

import com.referee.common.Result;
import com.referee.entity.Referee;
import com.referee.entity.User;
import com.referee.repository.RefereeRepository;
import com.referee.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * 裁判服务类
 *
 * @author Referee System
 * @version 1.0.0
 */
@Service
public class RefereeService {

    @Autowired
    private RefereeRepository refereeRepository;

    @Autowired
    private UserRepository userRepository;

    /**
     * 获取所有在职裁判列表
     *
     * @return 裁判列表
     */
    public Result<List<Referee>> getAllReferees() {
        return Result.success(refereeRepository.findByStatus(1));
    }

    /**
     * 根据ID查询裁判
     *
     * @param id 裁判ID
     * @return 裁判信息
     */
    public Result<Referee> getRefereeById(Long id) {
        Optional<Referee> referee = refereeRepository.findById(id);
        return referee.map(Result::success).orElseGet(() -> Result.error("裁判不存在"));
    }

    /**
     * 新增裁判
     *
     * @param referee 裁判信息
     * @return 新增结果
     */
    @Transactional
    public Result<Referee> addReferee(Referee referee) {
        if (refereeRepository.existsByRefereeNo(referee.getRefereeNo())) {
            return Result.error("裁判编号已存在");
        }

        Referee savedReferee = refereeRepository.save(referee);

        User user = new User();
        user.setUsername("referee_" + savedReferee.getRefereeNo());
        user.setPassword("123456");
        user.setRealName(savedReferee.getName());
        user.setRole(savedReferee.getIsChief() == 1 ? "CHIEF_REFEREE" : "REFEREE");
        user.setPhone(savedReferee.getPhone());
        user.setEmail(savedReferee.getEmail());
        user.setStatus(1);
        user.setRefereeId(savedReferee.getId());
        userRepository.save(user);

        return Result.success(savedReferee);
    }

    /**
     * 更新裁判信息
     *
     * @param id      裁判ID
     * @param referee 裁判信息
     * @return 更新结果
     */
    @Transactional
    public Result<Referee> updateReferee(Long id, Referee referee) {
        Optional<Referee> existingReferee = refereeRepository.findById(id);
        if (!existingReferee.isPresent()) {
            return Result.error("裁判不存在");
        }

        referee.setId(id);
        Referee updatedReferee = refereeRepository.save(referee);

        Optional<User> userOptional = userRepository.findByRefereeId(id);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            user.setRealName(referee.getName());
            user.setRole(referee.getIsChief() == 1 ? "CHIEF_REFEREE" : "REFEREE");
            user.setPhone(referee.getPhone());
            user.setEmail(referee.getEmail());
            userRepository.save(user);
        }

        return Result.success(updatedReferee);
    }

    /**
     * 删除裁判
     *
     * @param id 裁判ID
     * @return 删除结果
     */
    @Transactional
    public Result<String> deleteReferee(Long id) {
        if (!refereeRepository.existsById(id)) {
            return Result.error("裁判不存在");
        }

        Optional<User> userOptional = userRepository.findByRefereeId(id);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            user.setStatus(0);
            userRepository.save(user);
        }

        Referee referee = refereeRepository.findById(id).get();
        referee.setStatus(0);
        refereeRepository.save(referee);

        return Result.success("删除成功");
    }

    /**
     * 获取在职裁判长列表
     *
     * @return 裁判长列表
     */
    public Result<List<Referee>> getChiefReferees() {
        return Result.success(refereeRepository.findByIsChiefAndStatus(1, 1));
    }

    /**
     * 模糊查询裁判
     *
     * @param keyword 关键词
     * @return 裁判列表
     */
    public Result<List<Referee>> searchReferees(String keyword) {
        return Result.success(refereeRepository.findByKeyword(keyword));
    }
}
