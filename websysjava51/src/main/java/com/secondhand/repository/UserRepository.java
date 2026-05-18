package com.secondhand.repository;

import com.secondhand.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {

    User findByUsername(String username);

    List<User> findByRoleId(Long roleId);

    List<User> findByStatus(String status);

    @Query("SELECT COUNT(u) FROM User u WHERE u.createTime BETWEEN :start AND :end")
    Long countByCreateTimeBetween(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

}