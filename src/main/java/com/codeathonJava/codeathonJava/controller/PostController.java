package com.codeathonJava.codeathonJava.controller;

import com.codeathonJava.codeathonJava.model.MyAppUser;
import org.springframework.web.bind.annotation.*;

@RestController
public class PostController {

    @PostMapping(value = "/req/signup", consumes = "application/json", produces = "application/json")
    public MyAppUser createUser(@RequestBody MyAppUser user) {

        System.out.println("Received User: " + user);


        return user;
    }
}

