package com.heritage.repository;

import com.heritage.entity.AuthenticationRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * 鉴定申请Repository
 * 
 * @author Heritage Team
 * @version 1.0.0
 */
@Repository
public interface AuthenticationRequestRepository extends JpaRepository<AuthenticationRequest, Long> {

    /**
     * 根据用户ID查询鉴定申请
     */
    Page<AuthenticationRequest> findByUserId(Long userId, Pageable pageable);

    /**
     * 根据专家ID查询鉴定申请
     */
    Page<AuthenticationRequest> findByExpertId(Long expertId, Pageable pageable);

    /**
     * 根据状态查询鉴定申请
     */
    Page<AuthenticationRequest> findByStatus(Integer status, Pageable pageable);
}