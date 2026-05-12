package com.hospital.security;

import com.hospital.entity.SysPermission;
import com.hospital.entity.SysRole;
import com.hospital.entity.SysUser;
import com.hospital.repository.SysUserRepository;
import com.hospital.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * JWT认证过滤器
 * 
 * @author Hospital Management Team
 * @version 1.0.0
 */
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private SysUserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        
        try {
            String jwt = getJwtFromRequest(request);

            if (StringUtils.hasText(jwt) && jwtUtil.validateToken(jwt)) {
                String username = jwtUtil.getUsernameFromToken(jwt);
                
                // 加载用户的权限信息
                List<SimpleGrantedAuthority> authorities = new ArrayList<>();
                Optional<SysUser> userOptional = userRepository.findByUsername(username);
                
                if (userOptional.isPresent()) {
                    SysUser user = userOptional.get();
                    Set<String> permissionCodes = new HashSet<>();
                    
                    // 获取用户角色的所有权限
                    if (user.getRoles() != null) {
                        for (SysRole role : user.getRoles()) {
                            authorities.add(new SimpleGrantedAuthority("ROLE_" + role.getRoleCode()));
                            if (role.getPermissions() != null) {
                                for (SysPermission permission : role.getPermissions()) {
                                    permissionCodes.add(permission.getPermissionCode());
                                }
                            }
                        }
                    }
                    
                    // 添加权限编码到权限列表
                    for (String permissionCode : permissionCodes) {
                        authorities.add(new SimpleGrantedAuthority(permissionCode));
                    }
                }
                
                UsernamePasswordAuthenticationToken authentication = 
                        new UsernamePasswordAuthenticationToken(username, null, authorities);
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (Exception ex) {
            logger.error("Could not set user authentication in security context", ex);
        }

        filterChain.doFilter(request, response);
    }

    /**
     * 从请求中获取JWT令牌
     */
    private String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}
