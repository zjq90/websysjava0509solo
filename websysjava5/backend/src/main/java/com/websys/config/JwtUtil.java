package com.websys.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * JWT工具类
 * 
 * @author websys
 * @version 1.0.0
 */
@Component
public class JwtUtil {

    @Value("${app.jwt.secret}")
    private String secret;

    @Value("${app.jwt.expiration}")
    private Long expiration;

    /**
     * 从令牌中获取用户名
     * 
     * @param token JWT令牌
     * @return 用户名
     */
    public String getUsernameFromToken(String token) {
        return getClaimsFromToken(token).getSubject();
    }

    /**
     * 从令牌中获取用户ID
     * 
     * @param token JWT令牌
     * @return 用户ID
     */
    public Long getUserIdFromToken(String token) {
        Claims claims = getClaimsFromToken(token);
        return claims.get("userId", Long.class);
    }

    /**
     * 从令牌中获取角色类型
     * 
     * @param token JWT令牌
     * @return 角色类型
     */
    public String getRoleTypeFromToken(String token) {
        Claims claims = getClaimsFromToken(token);
        return claims.get("roleType", String.class);
    }

    /**
     * 从令牌中获取代理商ID
     * 
     * @param token JWT令牌
     * @return 代理商ID
     */
    public Long getAgentIdFromToken(String token) {
        Claims claims = getClaimsFromToken(token);
        return claims.get("agentId", Long.class);
    }

    /**
     * 从令牌中获取过期时间
     * 
     * @param token JWT令牌
     * @return 过期时间
     */
    public Date getExpirationDateFromToken(String token) {
        return getClaimsFromToken(token).getExpiration();
    }

    /**
     * 从令牌中获取声明信息
     * 
     * @param token JWT令牌
     * @return 声明信息
     */
    private Claims getClaimsFromToken(String token) {
        return Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * 判断令牌是否过期
     * 
     * @param token JWT令牌
     * @return 是否过期
     */
    private Boolean isTokenExpired(String token) {
        Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    /**
     * 生成令牌
     * 
     * @param username 用户名
     * @param userId 用户ID
     * @param roleType 角色类型
     * @param agentId 代理商ID
     * @return JWT令牌
     */
    public String generateToken(String username, Long userId, String roleType, Long agentId) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", userId);
        claims.put("roleType", roleType);
        if (agentId != null) {
            claims.put("agentId", agentId);
        }
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }

    /**
     * 验证令牌
     * 
     * @param token JWT令牌
     * @param username 用户名
     * @return 是否有效
     */
    public Boolean validateToken(String token, String username) {
        String tokenUsername = getUsernameFromToken(token);
        return (tokenUsername.equals(username) && !isTokenExpired(token));
    }
}
