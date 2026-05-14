package com.photostudio.controller;

import com.photostudio.common.Result;
import com.photostudio.service.DataInitService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 数据初始化控制器
 * 提供测试数据初始化接口
 * 
 * @author Photo Studio Team
 * @version 1.0.0
 */
@RestController
@RequestMapping("/api/init")
@CrossOrigin(origins = "*")
@Tag(name = "数据初始化", description = "测试数据初始化接口")
public class DataInitController {

    @Autowired
    private DataInitService dataInitService;

    /**
     * 初始化所有测试数据
     */
    @PostMapping("/all")
    @Operation(summary = "初始化所有测试数据", description = "初始化门店、员工、套餐、客户、订单、收支记录、报销记录、修图记录等所有测试数据")
    public Result<String> initAllData() {
        dataInitService.initAllData();
        return Result.success("数据初始化成功");
    }
}
