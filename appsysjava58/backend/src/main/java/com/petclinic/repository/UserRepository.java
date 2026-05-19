package com.petclinic.repository;

import com.petclinic.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 用户数据访问接口
 * 
 * @author PetClinic Team
 * @version 1.0.0
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsernameAndDeletedFalse(String username);

    Optional<User> findByPhoneAndDeletedFalse(String phone);

    List<User> findByRoleAndDeletedFalse(String role);

    List<User> findByNicknameContainingAndDeletedFalse(String nickname);

    boolean existsByUsernameAndDeletedFalse(String username);

    boolean existsByPhoneAndDeletedFalse(String phone);
}