package com.agriculture.service;

import com.agriculture.entity.FieldRecord;
import com.agriculture.repository.FieldRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.Random;

/**
 * 田间记录服务类
 * 提供田间数据采集的业务逻辑
 * 
 * 采集流程：
 * 用户登录 -> 选择"田间记录"模块 -> 定位当前地块 -> 选择作物品种 ->
 * 录入生长阶段 -> 填写观测数据 -> 上传照片 -> 提交
 * 
 * @author Agriculture System
 * @version 1.0.0
 */
@Service
@Transactional
public class FieldRecordService {

    @Autowired
    private FieldRecordRepository fieldRecordRepository;

    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
    private static final Random RANDOM = new Random();

    /**
     * 生成唯一的记录编号
     * 格式：FR + 年月日时分秒 + 4位随机数
     * 
     * @return 记录编号
     */
    public String generateRecordNo() {
        String datePart = LocalDateTime.now().format(DATE_FORMAT);
        int randomPart = RANDOM.nextInt(9000) + 1000;
        String recordNo = "FR" + datePart + randomPart;
        
        while (fieldRecordRepository.existsByRecordNo(recordNo)) {
            randomPart = RANDOM.nextInt(9000) + 1000;
            recordNo = "FR" + datePart + randomPart;
        }
        
        return recordNo;
    }

    /**
     * 创建田间记录
     * 
     * @param fieldRecord 田间记录
     * @return 创建后的记录
     */
    public FieldRecord create(FieldRecord fieldRecord) {
        if (fieldRecord.getRecordNo() == null || fieldRecord.getRecordNo().isEmpty()) {
            fieldRecord.setRecordNo(generateRecordNo());
        }
        return fieldRecordRepository.save(fieldRecord);
    }

    /**
     * 更新田间记录
     * 
     * @param fieldRecord 田间记录
     * @return 更新后的记录
     */
    public FieldRecord update(FieldRecord fieldRecord) {
        if (!fieldRecordRepository.existsById(fieldRecord.getId())) {
            throw new RuntimeException("田间记录不存在");
        }
        return fieldRecordRepository.save(fieldRecord);
    }

    /**
     * 根据ID删除记录
     * 
     * @param id 记录ID
     */
    public void deleteById(Long id) {
        fieldRecordRepository.deleteById(id);
    }

    /**
     * 根据ID查找记录
     * 
     * @param id 记录ID
     * @return 记录对象
     */
    public Optional<FieldRecord> findById(Long id) {
        return fieldRecordRepository.findById(id);
    }

    /**
     * 根据记录编号查找
     * 
     * @param recordNo 记录编号
     * @return 记录对象
     */
    public Optional<FieldRecord> findByRecordNo(String recordNo) {
        return fieldRecordRepository.findByRecordNo(recordNo);
    }

    /**
     * 查询所有记录
     * 
     * @return 记录列表
     */
    public List<FieldRecord> findAll() {
        return fieldRecordRepository.findAll();
    }

    /**
     * 分页查询所有记录
     * 
     * @param pageable 分页参数
     * @return 分页数据
     */
    public Page<FieldRecord> findAll(Pageable pageable) {
        return fieldRecordRepository.findAll(pageable);
    }

    /**
     * 根据地块ID查询记录
     * 
     * @param plotId 地块ID
     * @return 记录列表
     */
    public List<FieldRecord> findByPlotId(Long plotId) {
        return fieldRecordRepository.findByPlotIdOrderByRecordDateDesc(plotId);
    }

    /**
     * 根据作物ID查询记录
     * 
     * @param cropId 作物ID
     * @return 记录列表
     */
    public List<FieldRecord> findByCropId(Long cropId) {
        return fieldRecordRepository.findByCropIdOrderByRecordDateDesc(cropId);
    }

    /**
     * 根据观测人查询记录
     * 
     * @param observerId 观测人ID
     * @return 记录列表
     */
    public List<FieldRecord> findByObserverId(Long observerId) {
        return fieldRecordRepository.findByObserverIdOrderByCreatedAtDesc(observerId);
    }

    /**
     * 根据地块和生长阶段查询
     * 
     * @param plotId 地块ID
     * @param growthStage 生长阶段
     * @return 记录列表
     */
    public List<FieldRecord> findByPlotIdAndGrowthStage(Long plotId, String growthStage) {
        return fieldRecordRepository.findByPlotIdAndGrowthStageOrderByRecordDateDesc(plotId, growthStage);
    }

    /**
     * 查询最新的田间记录
     * 
     * @return 记录列表
     */
    public List<FieldRecord> findLatestRecords() {
        return fieldRecordRepository.findLatestRecords();
    }

    /**
     * 统计用户记录数量
     * 
     * @param observerId 观测人ID
     * @return 记录数量
     */
    public long countByObserverId(Long observerId) {
        return fieldRecordRepository.countByObserverId(observerId);
    }
}
