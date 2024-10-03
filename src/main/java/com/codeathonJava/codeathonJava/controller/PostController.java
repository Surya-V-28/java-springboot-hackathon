package com.codeathonJava.codeathonJava.controller;

import com.codeathonJava.codeathonJava.model.MyAppUser;
import com.codeathonJava.codeathonJava.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/req")
public class PostController {

    private final UserService userService;

    public PostController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping(value = "/signup", consumes = "application/json", produces = "application/json")
    public ResponseEntity<MyAppUser> createUser(@RequestBody MyAppUser user) {
        System.out.println("Received User: " + user);
        userService.saveUser(user); // Save user using UserService
        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception ex) {
        System.err.println("Error: " + ex.getMessage());
        return new ResponseEntity<>("An error occurred: " + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
