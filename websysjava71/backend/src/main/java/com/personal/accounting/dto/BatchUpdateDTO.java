package com.personal.accounting.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 批量更新数据传输对象
 * 
 * @author Personal Accounting
 * @version 1.0.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "批量更新数据")
public class BatchUpdateDTO {

    @NotEmpty(message = "交易ID列表不能为空")
    @Schema(description = "交易ID列表", required = true)
    private List<Long> transactionIds;

    @Schema(description = "新的分类ID")
    private Long categoryId;

    @Schema(description = "新的标签（逗号分隔）")
    private String tags;
}
