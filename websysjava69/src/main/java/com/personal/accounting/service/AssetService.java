package com.personal.accounting.service;

import com.personal.accounting.dto.NetWorthDTO;
import com.personal.accounting.entity.Asset;
import com.personal.accounting.enums.AssetType;
import com.personal.accounting.repository.AssetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AssetService {

    private final AssetRepository assetRepository;

    public List<Asset> getUserAssets(Long userId) {
        return assetRepository.findByUserId(userId);
    }

    public List<Asset> getUserAssetsByType(Long userId, AssetType type) {
        return assetRepository.findByUserIdAndAssetType(userId, type);
    }

    public Asset getAssetById(Long id) {
        return assetRepository.findById(id).orElseThrow(() -> new RuntimeException("资产不存在"));
    }

    @Transactional
    public Asset createAsset(Asset asset) {
        return assetRepository.save(asset);
    }

    @Transactional
    public Asset updateAsset(Long id, Asset asset) {
        Asset existingAsset = getAssetById(id);
        existingAsset.setAssetType(asset.getAssetType());
        existingAsset.setAssetName(asset.getAssetName());
        existingAsset.setCurrentValue(asset.getCurrentValue());
        existingAsset.setOriginalValue(asset.getOriginalValue());
        existingAsset.setLocation(asset.getLocation());
        existingAsset.setDescription(asset.getDescription());
        existingAsset.setPurchaseDate(asset.getPurchaseDate());
        return assetRepository.save(existingAsset);
    }

    @Transactional
    public void deleteAsset(Long id) {
        assetRepository.deleteById(id);
    }

    public BigDecimal getTotalAssetValue(Long userId) {
        return assetRepository.getTotalAssetValueByUserId(userId);
    }
}
