package com.secondhand.service;

import com.secondhand.entity.OperationLog;
import com.secondhand.entity.Role;
import com.secondhand.entity.User;
import com.secondhand.repository.OperationLogRepository;
import com.secondhand.repository.RoleRepository;
import com.secondhand.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.Predicate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class SystemService {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OperationLogRepository operationLogRepository;

    public Page<Role> findAllRoles(String name, String code, Pageable pageable) {
        Specification<Role> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (name != null && !name.isEmpty()) {
                predicates.add(cb.like(root.get("name"), "%" + name + "%"));
            }
            if (code != null && !code.isEmpty()) {
                predicates.add(cb.like(root.get("code"), "%" + code + "%"));
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };
        return roleRepository.findAll(spec, pageable);
    }

    public Optional<Role> findRoleById(Long id) {
        return roleRepository.findById(id);
    }

    public Role findRoleByCode(String code) {
        return roleRepository.findByCode(code);
    }

    @Transactional
    public Role saveRole(Role role) {
        if (role.getId() == null) {
            role.setCreateTime(LocalDateTime.now());
        }
        role.setUpdateTime(LocalDateTime.now());
        return roleRepository.save(role);
    }

    @Transactional
    public void deleteRole(Long id) {
        roleRepository.deleteById(id);
    }

    public Page<User> findAllUsers(String username, String nickname, Long roleId, String status, Pageable pageable) {
        Specification<User> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (username != null && !username.isEmpty()) {
                predicates.add(cb.like(root.get("username"), "%" + username + "%"));
            }
            if (nickname != null && !nickname.isEmpty()) {
                predicates.add(cb.like(root.get("nickname"), "%" + nickname + "%"));
            }
            if (roleId != null) {
                predicates.add(cb.equal(root.get("roleId"), roleId));
            }
            if (status != null && !status.isEmpty()) {
                predicates.add(cb.equal(root.get("status"), status));
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };
        return userRepository.findAll(spec, pageable);
    }

    public Optional<User> findUserById(Long id) {
        return userRepository.findById(id);
    }

    @Transactional
    public User saveUser(User user) {
        if (user.getId() == null) {
            user.setCreateTime(LocalDateTime.now());
            if (user.getStatus() == null) {
                user.setStatus("ACTIVE");
            }
        }
        user.setUpdateTime(LocalDateTime.now());
        return userRepository.save(user);
    }

    @Transactional
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    public Page<OperationLog> findAllOperationLogs(Long operatorId, String module, String type, 
                                                    LocalDateTime startTime, LocalDateTime endTime, Pageable pageable) {
        Specification<OperationLog> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (operatorId != null) {
                predicates.add(cb.equal(root.get("operatorId"), operatorId));
            }
            if (module != null && !module.isEmpty()) {
                predicates.add(cb.equal(root.get("module"), module));
            }
            if (type != null && !type.isEmpty()) {
                predicates.add(cb.equal(root.get("type"), type));
            }
            if (startTime != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("operationTime"), startTime));
            }
            if (endTime != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("operationTime"), endTime));
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };
        return operationLogRepository.findAll(spec, pageable);
    }

    @Transactional
    public OperationLog saveOperationLog(OperationLog log) {
        if (log.getOperationTime() == null) {
            log.setOperationTime(LocalDateTime.now());
        }
        return operationLogRepository.save(log);
    }

    public Optional<OperationLog> findOperationLogById(Long id) {
        return operationLogRepository.findById(id);
    }

}