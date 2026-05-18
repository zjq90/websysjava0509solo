package com.secondhand.repository;

import com.secondhand.entity.InventoryConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InventoryConfigRepository extends JpaRepository<InventoryConfig, Long>, JpaSpecificationExecutor<InventoryConfig> {

    List<InventoryConfig> findByEnabledTrue();

    InventoryConfig findByCategory(String category);

}