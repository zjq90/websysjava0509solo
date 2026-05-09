package com.inventory.repository;

import com.inventory.entity.Variety;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VarietyRepository extends JpaRepository<Variety, Long> {
    Optional<Variety> findByVarietyCode(String varietyCode);
    List<Variety> findByCategoryId(Long categoryId);
}
