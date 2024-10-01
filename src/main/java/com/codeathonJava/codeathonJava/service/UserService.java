package com.codeathonJava.codeathonJava.service;

import com.codeathonJava.codeathonJava.model.MyAppUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import jakarta.annotation.PostConstruct;



import java.util.List;

@Service
public class UserService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    // Method to create the user table on application startup
    public void init() {
        String createTableSQL = "CREATE TABLE IF NOT EXISTS users (" +
                "id INT AUTO_INCREMENT PRIMARY KEY, " +
                "username VARCHAR(50) NOT NULL, " +
                "email VARCHAR(225) NOT NULL," +
                "password VARCHAR(50) NOT NULL" +
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
