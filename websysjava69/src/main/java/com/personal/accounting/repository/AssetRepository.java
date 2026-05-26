package com.personal.accounting.repository;

import com.personal.accounting.entity.Asset;
import com.personal.accounting.enums.AssetType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface AssetRepository extends JpaRepository<Asset, Long> {
    List<Asset> findByUserId(Long userId);
    List<Asset> findByUserIdAndAssetType(Long userId, AssetType assetType);
    
    @Query("SELECT COALESCE(SUM(a.currentValue), 0) FROM Asset a WHERE a.userId = :userId")
    BigDecimal getTotalAssetValueByUserId(Long userId);
    
    List<Asset> findByUserIdIn(List<Long> userIds);
}
