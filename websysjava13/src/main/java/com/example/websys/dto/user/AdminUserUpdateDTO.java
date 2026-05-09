package com.example.websys.dto.user;

import com.example.websys.validator.ValidPassword;
import com.example.websys.validator.ValidPhone;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * 更新用户数据传输对象
 * 包含完整的后端校验注解（用户名不可修改）
 */
public class AdminUserUpdateDTO {

    @NotNull(message = "用户ID不能为空")
    private Long id;

    @ValidPassword(minLength = 6, maxLength = 20, required = false,
                   message = "密码格式不正确，长度6-20个字符，必须包含至少一个字母和一个数字")
    private String password;

    @Size(max = 50, message = "姓名长度不能超过50个字符")
    private String realName;

    @Email(message = "邮箱格式不正确")
    @Size(max = 100, message = "邮箱长度不能超过100个字符")
    private String email;

    @ValidPhone(required = false, message = "手机号格式不正确，请输入11位有效的手机号（如：13812345678）")
    private String phone;

    private String role;

    private Integer status;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
