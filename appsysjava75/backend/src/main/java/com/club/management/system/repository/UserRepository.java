package com.club.management.system.repository;

import com.club.management.system.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 用户Repository
 *
 * @author club-management
 * @version 1.0.0
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {

    User findByUsername(String username);

    User findByStudentNo(String studentNo);

    List<User> findByUserType(Integer userType);
}
