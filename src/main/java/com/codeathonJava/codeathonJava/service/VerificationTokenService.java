package com.codeathonJava.codeathonJava.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class VerificationTokenService {
    @Autowired
    JdbcTemplate jdbcTemplate;

    public void saveToken(String token, Integer userId) {
        String insertTokenSQL = "INSERT INTO verification_tokens (token, user_id, expiry_date) VALUES (?, ?, ?)";
        jdbcTemplate.update(insertTokenSQL, token, userId, LocalDateTime.now().plusHours(1));
    }
    public Integer findUserByToken(String token) { // Change return type to Integer
        String selectUserSQL = "SELECT user_id FROM verification_tokens WHERE token = ?";
        return jdbcTemplate.queryForObject(selectUserSQL, new Object[]{token}, Integer.class); // Return Integer instead of String
    }
    public boolean isTokenExpired(String token) {
        String selectExpirySQL = "SELECT expiry_date FROM verification_tokens WHERE token = ?";
        LocalDateTime expiryDate = jdbcTemplate.queryForObject(selectExpirySQL, new Object[]{token}, LocalDateTime.class);
        return expiryDate.isBefore(LocalDateTime.now());
    }
}
