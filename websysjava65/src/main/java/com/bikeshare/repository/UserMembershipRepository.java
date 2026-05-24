package com.bikeshare.repository;

import com.bikeshare.entity.UserMembership;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 用户会员数据访问层
 *
 * @author BikeShare Team
 * @version 1.0.0
 */
@Repository
public interface UserMembershipRepository extends JpaRepository<UserMembership, Long> {

    List<UserMembership> findByUserId(Long userId);

    List<UserMembership> findBySource(String source);
}
