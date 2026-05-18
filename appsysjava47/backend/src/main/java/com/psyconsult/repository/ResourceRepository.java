package com.psyconsult.repository;

import com.psyconsult.entity.Resource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ResourceRepository extends JpaRepository<Resource, Long> {
    List<Resource> findByTypeAndEnabledTrueOrderByCreateTimeDesc(String type);
    List<Resource> findByCategoryAndEnabledTrueOrderByCreateTimeDesc(String category);
    List<Resource> findByEnabledTrueOrderByViewCountDesc();
}
