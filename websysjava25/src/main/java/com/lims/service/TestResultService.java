package com.lims.service;

import com.lims.entity.TestResult;
import com.lims.repository.TestResultRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * 检验检查结果业务逻辑层
 * 实现技师工作站功能：记录操作过程、录入结果
 *
 * @author LIMS Team
 * @version 1.0.0
 */
@Service
public class TestResultService {

    @Autowired
    private TestResultRepository testResultRepository;

    /**
     * 查询所有结果
     */
    public List<TestResult> findAll() {
        return testResultRepository.findAll();
    }

    /**
     * 根据ID查询结果
     */
    public Optional<TestResult> findById(Long id) {
        return testResultRepository.findById(id);
    }

    /**
     * 根据申请ID查询结果
     */
    public List<TestResult> findByApplicationId(Long applicationId) {
        return testResultRepository.findByApplicationId(applicationId);
    }

    /**
     * 根据技师ID查询结果
     */
    public List<TestResult> findByTechnicianId(Long technicianId) {
        return testResultRepository.findByTechnicianId(technicianId);
    }

    /**
     * 新增检验结果
     */
    public TestResult save(TestResult testResult) {
        return testResultRepository.save(testResult);
    }

    /**
     * 更新检验结果
     */
    public TestResult update(TestResult testResult) {
        return testResultRepository.save(testResult);
    }

    /**
     * 开始处理检验/检查
     */
    public TestResult startTest(Long id) {
        Optional<TestResult> optional = testResultRepository.findById(id);
        if (optional.isPresent()) {
            TestResult result = optional.get();
            result.setStartTime(LocalDateTime.now());
            return testResultRepository.save(result);
        }
        return null;
    }

    /**
     * 完成检验/检查并录入结果
     */
    public TestResult completeTest(Long id, String resultValue, String resultDescription, String abnormalFlag) {
        Optional<TestResult> optional = testResultRepository.findById(id);
        if (optional.isPresent()) {
            TestResult result = optional.get();
            result.setResultValue(resultValue);
            result.setResultDescription(resultDescription);
            result.setAbnormalFlag(abnormalFlag);
            result.setCompleteTime(LocalDateTime.now());
            return testResultRepository.save(result);
        }
        return null;
    }

    /**
     * 从设备自动采集结果
     */
    public TestResult collectFromDevice(Long id, String deviceCode, String resultValue) {
        Optional<TestResult> optional = testResultRepository.findById(id);
        if (optional.isPresent()) {
            TestResult result = optional.get();
            result.setDeviceCode(deviceCode);
            result.setResultSource("DEVICE");
            result.setResultValue(resultValue);
            result.setCompleteTime(LocalDateTime.now());
            return testResultRepository.save(result);
        }
        return null;
    }

    /**
     * 删除结果
     */
    public void deleteById(Long id) {
        testResultRepository.deleteById(id);
    }
}
