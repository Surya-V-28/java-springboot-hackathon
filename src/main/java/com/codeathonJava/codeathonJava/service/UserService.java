package com.codeathonJava.codeathonJava.service;

import com.codeathonJava.codeathonJava.model.MyAppUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import jakarta.annotation.PostConstruct;



import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

@Service
public class UserService {
    @Autowired
    private JdbcTemplate jdbcTemplate;
    // Method to create the user table on application startup working
    @PostConstruct
    public void init() {
        String createTableSQL = "CREATE TABLE IF NOT EXISTS users (" +
                "id INT AUTO_INCREMENT PRIMARY KEY, " +
                "username VARCHAR(50) NOT NULL, " +
                "email VARCHAR(225) NOT NULL," +
                "password VARCHAR(50) NOT NULL, " +
                "is_verified BOOLEAN DEFAULT FALSE" +  // New column for email verification status
                ");";
        jdbcTemplate.execute(createTableSQL);
        System.out.println("Table created successfully!");

        // new table for the email verification of the user
        String createTokenTableSQL = "CREATE TABLE IF NOT EXISTS verification_tokens (" +
                "id INT AUTO_INCREMENT PRIMARY KEY, " +
                "token VARCHAR(255) NOT NULL, " +
                "user_id INT NOT NULL, " +
                "expiry_date TIMESTAMP NOT NULL, " +
                "FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE" + // Ensures referential integrity
                ");";
        jdbcTemplate.execute(createTokenTableSQL);
        System.out.println("Verification tokens table created successfully!");
    }

    // Method to insert a user
    public void saveUser(MyAppUser user) {
        String insertDataSQL = "INSERT INTO users (username, email, password,is_verified ) VALUES (?, ?, ?, ?)";
        jdbcTemplate.update(insertDataSQL, user.getUsername(), user.getEmail(), user.getPassword(),false);
        System.out.println("User inserted successfully: " + user.getUsername());
    }

    public Integer findUserIdByEmail(String email) {
        String selectSQL = "SELECT id FROM users WHERE email = ?";
        return jdbcTemplate.query(selectSQL, new Object[]{email}, rs -> {
            if (rs.next()) {
                return rs.getInt("id");
            }
            return 0;
        });
    }

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


    public MyAppUser findUserById(Integer userId) {
        String selectSQL = "SELECT * FROM users WHERE id = ?";
        return jdbcTemplate.query(selectSQL, new Object[]{userId}, rs -> {
            if (rs.next()) {
                MyAppUser user = new MyAppUser();
                user.setId(rs.getInt("id"));
                user.setUsername(rs.getString("username"));
                user.setEmail(rs.getString("email"));
                user.setPassword(rs.getString("password"));
                user.setIs_verified(rs.getBoolean("is_verified"));
                return user; // Return the user if found
            }
            return null; // Return null if no user found
        });
    }

    public void updateUserById(Integer userId, MyAppUser updatedUser) {
        String updateSQL = "UPDATE users SET username = ?, email = ?, password = ?, is_verified = ? WHERE id = ?";

        int rowsAffected = jdbcTemplate.update(updateSQL,
                updatedUser.getUsername(),
                updatedUser.getEmail(),
                updatedUser.getPassword(),
                updatedUser.getIs_verified(),
                userId);

        if (rowsAffected > 0) {
            System.out.println("User updated successfully for ID: " + userId);
        } else {
            System.out.println("No user found with ID: " + userId);
        }
    }

    public boolean verifyUserCredentials(String username, String rawPassword) {
        Optional<MyAppUser> optionalUser = findByUsername(username);
        if (optionalUser.isPresent()) {
            MyAppUser user = optionalUser.get();
            return rawPassword.equals(user.getPassword()); // Simple string comparison
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
