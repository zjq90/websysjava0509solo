package com.photostudio.service;

import com.photostudio.entity.InteractionRecord;
import com.photostudio.repository.InteractionRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * 互动记录服务层
 * 处理客户互动记录相关的业务逻辑
 * 
 * @author PhotoStudio Team
 * @version 1.0.0
 */
@Service
@Transactional
public class InteractionRecordService {

    @Autowired
    private InteractionRecordRepository interactionRecordRepository;

    /**
     * 创建互动记录
     */
    public InteractionRecord createInteractionRecord(InteractionRecord record) {
        return interactionRecordRepository.save(record);
    }

    /**
     * 更新互动记录
     */
    public InteractionRecord updateInteractionRecord(Long id, InteractionRecord recordDetails) {
        InteractionRecord record = interactionRecordRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("互动记录不存在"));

        record.setContent(recordDetails.getContent());
        record.setFollowUpPerson(recordDetails.getFollowUpPerson());
        record.setNextFollowUpTime(recordDetails.getNextFollowUpTime());

        return interactionRecordRepository.save(record);
    }

    /**
     * 删除互动记录（软删除）
     */
    public void deleteInteractionRecord(Long id) {
        InteractionRecord record = interactionRecordRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("互动记录不存在"));
        record.setDeleted(true);
        interactionRecordRepository.save(record);
    }

    /**
     * 根据ID查询互动记录
     */
    @Transactional(readOnly = true)
    public Optional<InteractionRecord> getInteractionRecordById(Long id) {
        return interactionRecordRepository.findById(id).filter(r -> !r.getDeleted());
    }

    /**
     * 查询客户的所有互动记录
     */
    @Transactional(readOnly = true)
    public List<InteractionRecord> getInteractionRecordsByCustomerId(Long customerId) {
        return interactionRecordRepository.findByCustomerIdAndDeletedFalseOrderByCreateTimeDesc(customerId);
    }

    /**
     * 查询所有互动记录
     */
    @Transactional(readOnly = true)
    public List<InteractionRecord> getAllInteractionRecords() {
        return interactionRecordRepository.findAllByDeletedFalse();
    }
}
