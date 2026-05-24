package com.plate.controller;

import com.plate.common.Result;
import com.plate.entity.BroadcastMessage;
import com.plate.service.BroadcastService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/broadcast")
@Tag(name = "广播管理", description = "紧急广播相关接口")
public class BroadcastController {
    @Autowired
    private BroadcastService broadcastService;

    @PostMapping("/send")
    @Operation(summary = "发送紧急广播")
    public Result<BroadcastMessage> sendBroadcast(@RequestBody Map<String, String> body) {
        String content = body.get("content");
        String sentBy = body.getOrDefault("sentBy", "admin");
        return Result.success(broadcastService.sendBroadcast(content, sentBy));
    }

    @GetMapping("/history")
    @Operation(summary = "获取广播历史")
    public Result<List<BroadcastMessage>> getHistory() {
        return Result.success(broadcastService.getAllMessages());
    }
}
