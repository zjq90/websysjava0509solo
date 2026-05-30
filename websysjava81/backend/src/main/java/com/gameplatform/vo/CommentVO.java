package com.gameplatform.vo;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class CommentVO {
    private String commentId;
    private String gameId;
    private String gameName;
    private String userId;
    private String nickname;
    private String content;
    private Integer auditStatus;
    private Integer hasSensitiveWord;
    private LocalDateTime createTime;
}
