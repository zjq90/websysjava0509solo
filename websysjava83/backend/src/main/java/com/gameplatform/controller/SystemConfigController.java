package com.gameplatform.controller;

import com.gameplatform.common.Result;
import com.gameplatform.dto.SystemConfigDTO;
import com.gameplatform.service.SystemConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/system-config")
public class SystemConfigController {

    @Autowired
    private SystemConfigService systemConfigService;

    @GetMapping
    public Result<SystemConfigDTO> getSystemConfig() {
        return Result.success(systemConfigService.getSystemConfig());
    }

    @PutMapping
    public Result<SystemConfigDTO> updateSystemConfig(@RequestBody SystemConfigDTO configDTO) {
        return Result.success(systemConfigService.updateSystemConfig(configDTO));
    }
}
