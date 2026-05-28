package com.club.management.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 登录VO
 *
 * @author club-management
 * @since 2024-01-01
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("登录返回结果")
public class LoginVO {

    @ApiModelProperty("Token")
    private String token;

    @ApiModelProperty("用户信息")
    private SysUserVO user;
}
