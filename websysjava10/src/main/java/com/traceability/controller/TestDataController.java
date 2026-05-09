package com.traceability.controller;

import com.traceability.common.Result;
import com.traceability.service.TestDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 测试数据生成REST API控制器
 */
@RestController
@RequestMapping("/api/test-data")
public class TestDataController {

    @Autowired
    private TestDataService testDataService;

    /**
     * 生成单条完整测试数据
     */
    @PostMapping("/generate-one")
    public Result<String> generateOne() {
        String batchNo = testDataService.generateCompleteTestData();
        return Result.success("测试数据生成成功，批次号：" + batchNo, batchNo);
    }

    /**
     * 批量生成测试数据
     */
    @PostMapping("/generate-batch")
    public Result<Integer> generateBatch(@RequestParam(defaultValue = "5") int count) {
        int generated = testDataService.generateBatchTestData(count);
        return Result.success("成功生成" + generated + "条测试数据", generated);
    }
}
