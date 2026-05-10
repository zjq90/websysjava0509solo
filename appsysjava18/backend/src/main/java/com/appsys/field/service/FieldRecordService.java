package com.appsys.field.service;

import com.appsys.common.exception.BusinessException;
import com.appsys.common.result.PageResult;
import com.appsys.field.dto.FieldRecordDTO;
import com.appsys.field.entity.FieldRecord;
import com.appsys.field.repository.FieldRecordRepository;
import com.appsys.system.entity.SysUser;
import com.appsys.system.repository.SysUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDate;

/**
 * 田间记录服务类
 * 
 * @author 系统管理员
 * @version 1.0.0
 */
@Service
public class FieldRecordService {

    @Autowired
    private FieldRecordRepository fieldRecordRepository;

    @Autowired
    private SysUserRepository userRepository;

    /**
     * 分页查询田间记录列表
     */
    public PageResult<FieldRecord> list(int page, int size, String keyword, LocalDate startDate, LocalDate endDate) {
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<FieldRecord> recordPage;
        
        if (startDate != null && endDate != null) {
            recordPage = fieldRecordRepository.findByRecordDateBetweenAndDeletedFalseOrderByRecordDateDesc(
                    startDate, endDate, pageable);
        } else if (StringUtils.hasText(keyword)) {
            recordPage = fieldRecordRepository.searchByKeyword(keyword, pageable);
        } else {
            recordPage = fieldRecordRepository.findByDeletedFalseOrderByRecordDateDesc(pageable);
        }
        
        return PageResult.of(recordPage);
    }

    /**
     * 根据ID查询田间记录详情
     */
    public FieldRecord getById(Long id) {
        return fieldRecordRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new BusinessException("田间记录不存在"));
    }

    /**
     * 新增田间记录
     */
    @Transactional
    public FieldRecord create(FieldRecordDTO dto) {
        // 获取当前登录用户
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        SysUser technician = userRepository.findByUsernameAndDeletedFalse(username).orElse(null);

        FieldRecord record = new FieldRecord();
        record.setFieldName(dto.getFieldName());
        record.setLocation(dto.getLocation());
        record.setArea(dto.getArea());
        record.setCropName(dto.getCropName());
        record.setVariety(dto.getVariety());
        record.setRecordDate(dto.getRecordDate());
        record.setWeather(dto.getWeather());
        record.setTemperature(dto.getTemperature());
        record.setHumidity(dto.getHumidity());
        record.setSoilCondition(dto.getSoilCondition());
        record.setPestStatus(dto.getPestStatus());
        record.setFertilization(dto.getFertilization());
        record.setIrrigation(dto.getIrrigation());
        record.setGrowthStatus(dto.getGrowthStatus());
        record.setOperation(dto.getOperation());
        record.setRemark(dto.getRemark());
        record.setImages(dto.getImages());

        if (technician != null) {
            record.setTechnicianId(technician.getId());
            record.setTechnicianName(technician.getRealName() != null ? technician.getRealName() : technician.getUsername());
        }

        return fieldRecordRepository.save(record);
    }

    /**
     * 更新田间记录
     */
    @Transactional
    public FieldRecord update(Long id, FieldRecordDTO dto) {
        FieldRecord record = getById(id);

        record.setFieldName(dto.getFieldName());
        record.setLocation(dto.getLocation());
        record.setArea(dto.getArea());
        record.setCropName(dto.getCropName());
        record.setVariety(dto.getVariety());
        record.setRecordDate(dto.getRecordDate());
        record.setWeather(dto.getWeather());
        record.setTemperature(dto.getTemperature());
        record.setHumidity(dto.getHumidity());
        record.setSoilCondition(dto.getSoilCondition());
        record.setPestStatus(dto.getPestStatus());
        record.setFertilization(dto.getFertilization());
        record.setIrrigation(dto.getIrrigation());
        record.setGrowthStatus(dto.getGrowthStatus());
        record.setOperation(dto.getOperation());
        record.setRemark(dto.getRemark());
        record.setImages(dto.getImages());

        return fieldRecordRepository.save(record);
    }

    /**
     * 删除田间记录（逻辑删除）
     */
    @Transactional
    public void delete(Long id) {
        FieldRecord record = getById(id);
        record.setDeleted(true);
        fieldRecordRepository.save(record);
    }
}
