package com.hospital;

import com.hospital.util.JwtUtil;
import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class JwtUtilTest {

    @Test
    public void testGenerateToken() {
        String userId = "1";
        String username = "testUser";
        String role = "PATIENT";
        
        String token = JwtUtil.generateToken(userId, username, role);
        assertNotNull(token);
        assertTrue(token.length() > 0);
        
        System.out.println("Generated Token: " + token);
    }

    @Test
    public void testParseToken() {
        String userId = "1001";
        String username = "patient1";
        String role = "PATIENT";
        
        String token = JwtUtil.generateToken(userId, username, role);
        Claims claims = JwtUtil.parseToken(token);
        
        assertNotNull(claims);
        assertEquals(userId, claims.getSubject());
        assertEquals(username, claims.get("username"));
        assertEquals(role, claims.get("role"));
        
        System.out.println("Parsed Claims - Subject: " + claims.getSubject());
        System.out.println("Parsed Claims - Username: " + claims.get("username"));
        System.out.println("Parsed Claims - Role: " + claims.get("role"));
    }

    @Test
    public void testValidateToken() {
        String userId = "1";
        String username = "test";
        String role = "PATIENT";
        
        String token = JwtUtil.generateToken(userId, username, role);
        
        assertTrue(JwtUtil.validateToken(token));
        
        String invalidToken = token + "invalid";
        assertFalse(JwtUtil.validateToken(invalidToken));
    }

    @Test
    public void testGetUsernameFromToken() {
        String username = "testUser123";
        String token = JwtUtil.generateToken("1", username, "PATIENT");
        
        String extractedUsername = JwtUtil.getUsernameFromToken(token);
        assertEquals(username, extractedUsername);
    }

    @Test
    public void testGetRoleFromToken() {
        String role = "ADMIN";
        String token = JwtUtil.generateToken("1", "admin", role);
        
        String extractedRole = JwtUtil.getRoleFromToken(token);
        assertEquals(role, extractedRole);
    }
}
