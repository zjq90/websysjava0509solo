package com.chatsystem.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 发送消息请求DTO
 */
@Data
public class ChatMessageDTO {

    /**
     * 接收者ID
     */
    @NotNull(message = "接收者ID不能为空")
    private Long toUserId;

    /**
     * 消息类型：0-文本，1-图片，2-文件，3-表情
     */
    @NotNull(message = "消息类型不能为空")
    private Integer type;

    /**
     * 消息内容
     */
    private String content;
}
