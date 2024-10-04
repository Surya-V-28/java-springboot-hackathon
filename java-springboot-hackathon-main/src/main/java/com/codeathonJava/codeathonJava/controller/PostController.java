package com.codeathonJava.codeathonJava.controller;

import com.codeathonJava.codeathonJava.model.LoginRequest;
import com.codeathonJava.codeathonJava.model.MyAppUser;
import com.codeathonJava.codeathonJava.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
@RestController
@RequestMapping("/req")
public class PostController {

    private final UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public PostController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping(value = "/signup", consumes = "application/json", produces = "application/json")
    public ResponseEntity<MyAppUser> createUser(@RequestBody MyAppUser user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userService.saveUser(user);
        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }

    @PostMapping(value = "/login", consumes = "application/json", produces = "application/json")
    public ResponseEntity<String> loginUser(@RequestBody LoginRequest loginRequest) {
        boolean isValidUser = userService.verifyUserCredentials(loginRequest.getUsername(), loginRequest.getPassword());

        if (isValidUser) {
            return new ResponseEntity<>("Login successful", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Invalid credentials", HttpStatus.UNAUTHORIZED);
        }
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception ex) {
        return new ResponseEntity<>("An error occurred: " + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
