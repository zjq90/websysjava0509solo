package com.medical.appointment.repository;

import com.medical.appointment.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
    Optional<User> findByPhone(String phone);
    
    Optional<User> findByWechatOpenId(String wechatOpenId);
    
    Optional<User> findByWechatUnionId(String wechatUnionId);
    
    boolean existsByPhone(String phone);
    
    boolean existsByWechatOpenId(String wechatOpenId);
}
