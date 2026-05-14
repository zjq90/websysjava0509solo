package com.appsys.repository;

import com.appsys.entity.MemberLevel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MemberLevelRepository extends JpaRepository<MemberLevel, Long> {

    List<MemberLevel> findByStatusOrderByLevelAsc(Integer status);

    Optional<MemberLevel> findByLevel(Integer level);

    Optional<MemberLevel> findFirstByRequiredGrowthLessThanEqualOrderByLevelDesc(Integer growth);
}
