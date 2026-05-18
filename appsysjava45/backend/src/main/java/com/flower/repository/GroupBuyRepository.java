package com.flower.repository;

import com.flower.entity.GroupBuy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface GroupBuyRepository extends JpaRepository<GroupBuy, Long> {
    @Query("SELECT g FROM GroupBuy g WHERE g.enabled = true AND g.startTime <= :now AND g.endTime >= :now")
    List<GroupBuy> findActiveGroupBuys(LocalDateTime now);
}