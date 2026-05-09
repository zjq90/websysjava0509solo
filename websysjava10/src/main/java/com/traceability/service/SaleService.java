package com.traceability.service;

import com.traceability.entity.Sale;
import com.traceability.repository.SaleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * 销售业务逻辑服务类
 */
@Service
@Transactional
public class SaleService {

    @Autowired
    private SaleRepository saleRepository;

    public List<Sale> findAll() {
        return saleRepository.findAll();
    }

    public Page<Sale> findAll(Pageable pageable) {
        return saleRepository.findAll(pageable);
    }

    public Optional<Sale> findById(Long id) {
        return saleRepository.findById(id);
    }

    public List<Sale> findByBatchNo(String batchNo) {
        return saleRepository.findByBatchNo(batchNo);
    }

    public List<Sale> findByPackageNo(String packageNo) {
        return saleRepository.findByPackageNo(packageNo);
    }

    public Optional<Sale> findBySaleNo(String saleNo) {
        return saleRepository.findBySaleNo(saleNo);
    }

    public Sale save(Sale sale) {
        if (sale.getSaleNo() == null || sale.getSaleNo().isEmpty()) {
            sale.setSaleNo("SAL" + System.currentTimeMillis());
        }
        return saleRepository.save(sale);
    }

    public Sale update(Sale sale) {
        return saleRepository.save(sale);
    }

    public void deleteById(Long id) {
        saleRepository.deleteById(id);
    }
}
