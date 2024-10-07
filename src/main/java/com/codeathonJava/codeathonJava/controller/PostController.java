package com.codeathonJava.codeathonJava.controller;

import com.codeathonJava.codeathonJava.model.LoginEntity;
import com.codeathonJava.codeathonJava.model.MyAppUser;
import com.codeathonJava.codeathonJava.service.UserService;
import com.codeathonJava.codeathonJava.service.VerificationTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/req")
public class PostController {

    private final UserService userService;
    private final VerificationTokenService verificationTokenService;

    private  final MailSender mailSender;
    @Autowired
    public PostController(UserService userService, VerificationTokenService verificationTokenService, MailSender mailSender) {
        this.userService = userService;
        this.verificationTokenService = verificationTokenService;
        this.mailSender = mailSender;
    }
    @PostMapping(value = "/signup", consumes = "application/json", produces = "application/json")
    public ResponseEntity<MyAppUser> createUser(@RequestBody MyAppUser user) {
        System.out.println("Received User: " + user);
        userService.saveUser(user); // Save user using UserService
        Integer givenId = userService.findUserIdByEmail(user.getEmail());
        if (givenId == 0) {
            throw new RuntimeException("No user found with the given email.");
        }
        // Generate a random token and assign to this user simple then check for the values in tables
        String token = UUID.randomUUID().toString();
        verificationTokenService.saveToken(token,givenId); // here we are storing the data uid and id of the user in different table for verfications
        //sending the verifications mail
       sendVerificationEmail(user.getEmail(), token);

        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }

    @PostMapping(value = "/login", consumes = "application/json", produces = "application/json")
    public ResponseEntity<String> loginUser(@RequestBody LoginEntity loginRequest) {
        boolean isValidUser = userService.verifyUserCredentials(loginRequest.getUsername(), loginRequest.getPassword());
        if (isValidUser) {
            return new ResponseEntity<>("Login successful", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Invalid credentials", HttpStatus.UNAUTHORIZED);
        }
    }

    private void sendVerificationEmail(String email, String token) {
        String verificationLink = "http://localhost:8080/req/verify?token=" + token; // Local link
        String subject = "Email Verification for JAVA CODE A THON MCA";
        String body = "Click the following link to verify your email: " + verificationLink;
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject(subject);
        message.setText(body);
        mailSender.send(message);
    }
    @GetMapping("/verify")
    public  ResponseEntity<String> verifyUser(@RequestParam String token) {
        Integer userID = verificationTokenService.findUserByToken(token);
        if(userID !=null & !verificationTokenService.isTokenExpired(token)) {
            System.out.println("Since the user is verified we need to update the tables values of the user");
            MyAppUser user = userService.findUserById(userID);
            if(user!=null) {
                user.setIs_verified(true);
                userService.updateUserById(userID,user);
                System.out.println("User data updated");
            } else {
                System.out.println("User was not found in the Tables");
            }

            return  ResponseEntity.ok("Verified the user  , you can login now");
        } else {
            System.out.println("Not a Verified the user  , you cant login now");
            return new ResponseEntity<>("Invalid or expired token!", HttpStatus.BAD_REQUEST);
        }
    }


    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception ex) {
        System.err.println("Error: " + ex.getMessage());
        return new ResponseEntity<>("An error occurred in my handler: " + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
