package com.broadband.repository;

import com.broadband.entity.UserPackage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 用户套餐数据访问层
 * 
 * @author broadband
 * @version 1.0.0
 */
@Repository
public interface UserPackageRepository extends JpaRepository<UserPackage, Long> {

    List<UserPackage> findByUserIdOrderByCreateTimeDesc(Long userId);

    List<UserPackage> findByUserIdAndStatusOrderByCreateTimeDesc(Long userId, Integer status);

    Optional<UserPackage> findByUserIdAndBroadbandNumber(Long userId, String broadbandNumber);
}
