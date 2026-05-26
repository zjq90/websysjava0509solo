package com.accounting.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Schema(description = "OCR识别结果DTO")
public class OcrRecognitionDTO {

    @Schema(description = "识别到的金额", example = "68.00")
    private BigDecimal amount;

    @Schema(description = "识别到的商家名称", example = "星巴克咖啡")
    private String merchant;

    @Schema(description = "识别到的交易时间")
    private LocalDateTime transactionTime;

    @Schema(description = "识别到的分类", example = "餐饮/咖啡")
    private String category;

    @Schema(description = "原始OCR文本")
    private String originalText;

    @Schema(description = "识别置信度", example = "0.92")
    private Double confidence;
}
