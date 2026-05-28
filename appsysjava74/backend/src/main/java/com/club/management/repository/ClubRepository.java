package com.club.management.repository;

import com.club.management.entity.Club;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * 社团数据访问接口
 * 
 * @author club-management
 * @version 1.0.0
 */
@Repository
public interface ClubRepository extends JpaRepository<Club, Long> {

    /**
     * 根据社团名称查询社团
     */
    Optional<Club> findByName(String name);

    /**
     * 检查社团名称是否存在
     */
    boolean existsByName(String name);
}
