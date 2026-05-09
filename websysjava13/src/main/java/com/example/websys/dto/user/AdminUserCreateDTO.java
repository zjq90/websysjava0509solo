package com.example.websys.dto.user;

import com.example.websys.validator.ValidPassword;
import com.example.websys.validator.ValidPhone;
import com.example.websys.validator.ValidUsername;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * 新增用户数据传输对象
 * 包含完整的后端校验注解
 */
public class AdminUserCreateDTO {

    @NotBlank(message = "用户名不能为空")
    @ValidUsername(message = "用户名格式不正确，必须以字母开头，只能包含字母、数字和下划线，长度3-20个字符")
    @Size(min = 3, max = 20, message = "用户名长度必须在3-20个字符之间")
    private String username;

    @NotBlank(message = "密码不能为空")
    @ValidPassword(minLength = 6, maxLength = 20, required = true, 
                   message = "密码格式不正确，长度6-20个字符，必须包含至少一个字母和一个数字")
    private String password;

    @Size(max = 50, message = "姓名长度不能超过50个字符")
    private String realName;

    @Email(message = "邮箱格式不正确")
    @Size(max = 100, message = "邮箱长度不能超过100个字符")
    private String email;

    @ValidPhone(required = false, message = "手机号格式不正确，请输入11位有效的手机号（如：13812345678）")
    private String phone;

    @NotBlank(message = "角色不能为空")
    private String role;

    private Integer status = 1;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
