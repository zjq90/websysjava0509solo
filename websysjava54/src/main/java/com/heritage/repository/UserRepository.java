package com.heritage.repository;

import com.heritage.entity.User;
import com.heritage.enums.AuditStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {

    User findByUsername(String username);

    List<User> findByIsSuspiciousTrue();

    List<User> findByFundsFrozenTrue();

    List<User> findByRealNameAuditStatus(AuditStatus status);

    List<User> findByExpertAuditStatus(AuditStatus status);

    List<User> findByIsExpertTrue();

    List<User> findByProvince(String province);
}
