package com.traceability.service;

import com.traceability.entity.QrTrace;
import com.traceability.repository.QrTraceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * 二维码追踪业务逻辑服务类
 */
@Service
@Transactional
public class QrTraceService {

    @Autowired
    private QrTraceRepository qrTraceRepository;

    public List<QrTrace> findAll() {
        return qrTraceRepository.findAll();
    }

    public Page<QrTrace> findAll(Pageable pageable) {
        return qrTraceRepository.findAll(pageable);
    }

    public Optional<QrTrace> findById(Long id) {
        return qrTraceRepository.findById(id);
    }

    public List<QrTrace> findByQrCode(String qrCode) {
        return qrTraceRepository.findByQrCode(qrCode);
    }

    public List<QrTrace> findByPackageNo(String packageNo) {
        return qrTraceRepository.findByPackageNo(packageNo);
    }

    public List<QrTrace> findByBatchNo(String batchNo) {
        return qrTraceRepository.findByBatchNo(batchNo);
    }

    public QrTrace save(QrTrace qrTrace) {
        return qrTraceRepository.save(qrTrace);
    }

    public QrTrace update(QrTrace qrTrace) {
        return qrTraceRepository.save(qrTrace);
    }

    public void deleteById(Long id) {
        qrTraceRepository.deleteById(id);
    }
}
