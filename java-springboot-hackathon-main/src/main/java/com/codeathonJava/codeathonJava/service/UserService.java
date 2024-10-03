package com.codeathonJava.codeathonJava.service;

import com.codeathonJava.codeathonJava.model.MyAppUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // Method to create the user table on application startup
    @PostConstruct
    public void init() {
        String createTableSQL = "CREATE TABLE IF NOT EXISTS users (" +
                "id INT AUTO_INCREMENT PRIMARY KEY, " +
                "username VARCHAR(50) NOT NULL, " +
                "email VARCHAR(225) NOT NULL," +
                "password VARCHAR(60) NOT NULL" + // Note: increase length for hashed passwords
                ");";
        jdbcTemplate.execute(createTableSQL);
        System.out.println("Table created successfully!");
    }

    // Method to insert a user
    public void saveUser(MyAppUser user) {
        String insertDataSQL = "INSERT INTO users (username, email, password) VALUES (?, ?, ?)";
        jdbcTemplate.update(insertDataSQL, user.getUsername(), user.getEmail(), user.getPassword());
        System.out.println("User inserted successfully: " + user.getUsername());
    }

    // Method to find user by username
    public Optional<MyAppUser> findByUsername(String username) {
        String selectSQL = "SELECT * FROM users WHERE username = ?";
        MyAppUser user = jdbcTemplate.queryForObject(selectSQL, new Object[]{username}, (rs, rowNum) -> {
            MyAppUser myAppUser = new MyAppUser();
            myAppUser.setId(rs.getInt("id"));
            myAppUser.setUsername(rs.getString("username"));
            myAppUser.setEmail(rs.getString("email"));
            myAppUser.setPassword(rs.getString("password"));
            return myAppUser;
        });
        return Optional.ofNullable(user);
    }

    // Method to verify user login credentials
    public boolean verifyUserCredentials(String username, String rawPassword) {
        Optional<MyAppUser> optionalUser = findByUsername(username);
        if (optionalUser.isPresent()) {
            MyAppUser user = optionalUser.get();
            return passwordEncoder.matches(rawPassword, user.getPassword()); // Check password
        }
        return false; // User not found
    }

    public List<MyAppUser> getAllUsers() {
        String selectSQL = "SELECT * FROM users";
        return jdbcTemplate.query(selectSQL, (rs, rowNum) -> {
            MyAppUser user = new MyAppUser();
            user.setId(rs.getInt("id"));
            user.setUsername(rs.getString("username"));
            user.setEmail(rs.getString("email"));
            user.setPassword(rs.getString("password"));
            return user;
        });
    }
}
