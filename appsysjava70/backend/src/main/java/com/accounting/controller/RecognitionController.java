package com.accounting.controller;

import com.accounting.dto.ApiResponse;
import com.accounting.dto.OcrRecognitionDTO;
import com.accounting.dto.VoiceRecognitionDTO;
import com.accounting.service.RecognitionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/recognition")
@RequiredArgsConstructor
@Tag(name = "智能识别", description = "语音识别和OCR识别接口")
public class RecognitionController {

    private final RecognitionService recognitionService;

    @PostMapping("/voice")
    @Operation(summary = "语音记账识别", description = "解析语音文本，自动识别金额、分类、商家、时间等信息。" +
            "示例输入：'午餐花费35元'、'昨天打车花了50元'、'星巴克咖啡68元'")
    public ApiResponse<VoiceRecognitionDTO> recognizeVoice(
            @Parameter(description = "语音转换后的文本", required = true, example = "午餐花费35元")
            @RequestParam String text) {
        log.info("API: 语音识别: {}", text);
        return ApiResponse.success("识别成功", recognitionService.recognizeVoice(text));
    }

    @PostMapping("/ocr")
    @Operation(summary = "拍照记账识别", description = "上传小票图片，OCR识别商家名称、金额、日期等信息，自动匹配分类。" +
            "当前为模拟实现，返回随机小票数据")
    public ApiResponse<OcrRecognitionDTO> recognizeOcr(
            @Parameter(description = "小票图片URL", required = true)
            @RequestParam String imageUrl) {
        log.info("API: OCR识别: {}", imageUrl);
        return ApiResponse.success("识别成功", recognitionService.recognizeOcr(imageUrl));
    }
}
