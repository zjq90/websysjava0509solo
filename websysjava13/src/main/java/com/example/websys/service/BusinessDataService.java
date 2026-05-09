package com.example.websys.service;

import com.example.websys.entity.BusinessData;
import com.example.websys.repository.BusinessDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * 经营数据业务逻辑服务类
 * 提供经营数据的增删改查功能
 */
@Service
@Transactional
public class BusinessDataService {

    @Autowired
    private BusinessDataRepository businessDataRepository;

    /**
     * 保存经营数据
     */
    public BusinessData save(BusinessData businessData) {
        Optional<BusinessData> existing = businessDataRepository
                .findByStatCodeAndDataDate(businessData.getStatCode(), businessData.getDataDate());
        
        if (existing.isPresent()) {
            BusinessData entity = existing.get();
            entity.setDataValue(businessData.getDataValue());
            entity.setDataText(businessData.getDataText());
            entity.setTrendValue(businessData.getTrendValue());
            entity.setTrendType(businessData.getTrendType());
            entity.setCompareValue(businessData.getCompareValue());
            entity.setCompareType(businessData.getCompareType());
            entity.setRemark(businessData.getRemark());
            return businessDataRepository.save(entity);
        }
        
        return businessDataRepository.save(businessData);
    }

    /**
     * 根据ID获取数据
     */
    public Optional<BusinessData> getById(Long id) {
        return businessDataRepository.findById(id);
    }

    /**
     * 获取某日期的所有经营数据
     */
    public List<BusinessData> getByDate(LocalDate date) {
        return businessDataRepository.findByDataDateOrderByIdAsc(date);
    }

    /**
     * 获取指定统计项在日期范围内的数据
     */
    public List<BusinessData> getByStatCodeAndDateRange(String statCode, LocalDate startDate, LocalDate endDate) {
        return businessDataRepository.findByStatCodeAndDateRange(statCode, startDate, endDate);
    }

    /**
     * 批量获取指定统计项在某日期的数据
     */
    public List<BusinessData> getByStatCodesAndDate(List<String> statCodes, LocalDate date) {
        return businessDataRepository.findByStatCodesAndDate(statCodes, date);
    }

    /**
     * 获取指定统计项在某日期的数据
     */
    public Optional<BusinessData> getByStatCodeAndDate(String statCode, LocalDate date) {
        return businessDataRepository.findByStatCodeAndDataDate(statCode, date);
    }

    /**
     * 删除数据
     */
    public void deleteById(Long id) {
        businessDataRepository.deleteById(id);
    }

    /**
     * 获取所有数据
     */
    public List<BusinessData> getAll() {
        return businessDataRepository.findAll();
    }
}
