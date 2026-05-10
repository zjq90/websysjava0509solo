package com.websys.dto;

import lombok.Data;

/**
 * 登录响应DTO
 * 
 * @author websys
 * @version 1.0.0
 */
@Data
public class LoginResponse {

    /**
     * JWT令牌
     */
    private String token;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 用户名
     */
    private String username;

    /**
     * 真实姓名
     */
    private String realName;

    /**
     * 角色类型
     */
    private String roleType;

    /**
     * 代理商ID
     */
    private Long agentId;
}
