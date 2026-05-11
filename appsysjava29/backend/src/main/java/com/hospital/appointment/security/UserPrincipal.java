package com.hospital.appointment.security;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用户认证主体
 * 
 * @author hospital
 * @version 1.0.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserPrincipal {
    
    private Long userId;
    private String username;
    private String role;
    
    public static UserPrincipal of(Long userId, String username, String role) {
        return new UserPrincipal(userId, username, role);
    }
}
