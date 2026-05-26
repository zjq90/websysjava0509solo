package com.accounting.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Schema(description = "语音识别结果DTO")
public class VoiceRecognitionDTO {

    @Schema(description = "识别到的金额", example = "35.00")
    private BigDecimal amount;

    @Schema(description = "识别到的分类", example = "餐饮")
    private String category;

    @Schema(description = "识别到的商家", example = "午餐")
    private String merchant;

    @Schema(description = "识别到的交易时间")
    private LocalDateTime transactionTime;

    @Schema(description = "原始文本", example = "午餐花费35元")
    private String originalText;

    @Schema(description = "识别置信度", example = "0.95")
    private Double confidence;
}
