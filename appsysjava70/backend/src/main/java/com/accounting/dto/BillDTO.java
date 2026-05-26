package com.accounting.dto;

import com.accounting.enums.BillType;
import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Schema(description = "账单创建/更新DTO")
public class BillDTO {

    @Schema(description = "账单ID（更新时需要）", example = "1")
    private Long id;

    @NotNull(message = "账单类型不能为空")
    @Schema(description = "账单类型", example = "EXPENSE", required = true)
    private BillType type;

    @NotNull(message = "金额不能为空")
    @DecimalMin(value = "0.01", message = "金额必须大于0")
    @Schema(description = "金额", example = "35.00", required = true)
    private BigDecimal amount;

    @NotNull(message = "分类ID不能为空")
    @Schema(description = "分类ID", example = "1", required = true)
    private Long categoryId;

    @NotNull(message = "账户ID不能为空")
    @Schema(description = "账户ID", example = "1", required = true)
    private Long accountId;

    @NotNull(message = "交易时间不能为空")
    @Schema(description = "交易时间", required = true)
    private LocalDateTime transactionTime;

    @Schema(description = "商家名称", example = "星巴克")
    private String merchant;

    @Schema(description = "备注", example = "和朋友喝咖啡")
    private String remark;

    @Schema(description = "图片URL", example = "https://example.com/receipt.jpg")
    private String imageUrl;

    @Schema(description = "设备ID", example = "device_123")
    private String deviceId;

    @Schema(description = "客户端唯一标识", example = "client_abc123")
    private String clientId;
}
