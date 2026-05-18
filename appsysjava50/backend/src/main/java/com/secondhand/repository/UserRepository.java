package com.secondhand.repository;

import com.secondhand.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * 用户Repository接口
 *
 * @author secondhand
 * @version 1.0.0
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {

    Optional<User> findByUsernameAndIsDeletedFalse(String username);

    Optional<User> findByOpenidAndIsDeletedFalse(String openid);

    Optional<User> findByIdAndIsDeletedFalse(Long id);

    boolean existsByUsernameAndIsDeletedFalse(String username);

}
