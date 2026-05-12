package com.lims.service;

import com.lims.entity.User;
import com.lims.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * 用户业务逻辑层
 *
 * @author LIMS Team
 * @version 1.0.0
 */
@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    /**
     * 查询所有用户
     */
    public List<User> findAll() {
        return userRepository.findAll();
    }

    /**
     * 根据ID查询用户
     */
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    /**
     * 根据用户名查询
     */
    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    /**
     * 根据角色查询
     */
    public List<User> findByRole(String role) {
        return userRepository.findByRole(role);
    }

    /**
     * 根据科室ID查询
     */
    public List<User> findByDepartmentId(Long departmentId) {
        return userRepository.findByDepartmentId(departmentId);
    }

    /**
     * 根据科室ID和角色查询
     */
    public List<User> findByDepartmentIdAndRole(Long departmentId, String role) {
        return userRepository.findByDepartmentIdAndRole(departmentId, role);
    }

    /**
     * 新增用户
     */
    public User save(User user) {
        return userRepository.save(user);
    }

    /**
     * 更新用户
     */
    public User update(User user) {
        return userRepository.save(user);
    }

    /**
     * 删除用户
     */
    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }
}
