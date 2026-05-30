package com.gameplatform.repository;

import com.gameplatform.entity.AdSlot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AdSlotRepository extends JpaRepository<AdSlot, Long> {
    Optional<AdSlot> findByCode(String code);
    List<AdSlot> findByEnabled(Boolean enabled);
}
