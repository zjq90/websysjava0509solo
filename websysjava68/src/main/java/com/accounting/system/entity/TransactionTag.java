package com.accounting.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 交易标签关联实体类
 * 多对多关系：一个交易可以有多个标签，一个标签可以属于多个交易
 */
@Data
@TableName("transaction_tag")
@ApiModel(value = "交易标签关联")
public class TransactionTag implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键ID")
    @TableId(type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "交易记录ID", example = "1")
    private Long transactionId;

    @ApiModelProperty(value = "标签ID", example = "1")
    private Long tagId;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;
}
