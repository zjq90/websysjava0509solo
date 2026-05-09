package com.traceability.service;

import com.traceability.entity.Packaging;
import com.traceability.repository.PackagingRepository;
import com.traceability.util.QrCodeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * 包装业务逻辑服务类
 * 实现"一袋一码"溯源功能
 */
@Service
@Transactional
public class PackagingService {

    @Autowired
    private PackagingRepository packagingRepository;

    public List<Packaging> findAll() {
        return packagingRepository.findAll();
    }

    public Page<Packaging> findAll(Pageable pageable) {
        return packagingRepository.findAll(pageable);
    }

    public Optional<Packaging> findById(Long id) {
        return packagingRepository.findById(id);
    }

    public List<Packaging> findByBatchNo(String batchNo) {
        return packagingRepository.findByBatchNo(batchNo);
    }

    public Optional<Packaging> findByPackageNo(String packageNo) {
        return packagingRepository.findByPackageNo(packageNo);
    }

    public Optional<Packaging> findByQrCode(String qrCode) {
        return packagingRepository.findByQrCode(qrCode);
    }

    /**
     * 保存包装信息，自动生成包装编号和二维码
     */
    public Packaging save(Packaging packaging) {
        if (packaging.getPackageNo() == null || packaging.getPackageNo().isEmpty()) {
            packaging.setPackageNo(QrCodeUtil.generatePackageNo());
        }
        if (packaging.getQrCode() == null || packaging.getQrCode().isEmpty()) {
            packaging.setQrCode(QrCodeUtil.generateUniqueQrCode());
        }
        if (packaging.getBarcode() == null || packaging.getBarcode().isEmpty()) {
            packaging.setBarcode(QrCodeUtil.generateUniqueBarcode());
        }
        return packagingRepository.save(packaging);
    }

    public Packaging update(Packaging packaging) {
        return packagingRepository.save(packaging);
    }

    public void deleteById(Long id) {
        packagingRepository.deleteById(id);
    }

    public boolean existsByPackageNo(String packageNo) {
        return packagingRepository.existsByPackageNo(packageNo);
    }

    public boolean existsByQrCode(String qrCode) {
        return packagingRepository.existsByQrCode(qrCode);
    }
}
