
package com.codeathonJava.codeathonJava.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ContentController {

    @GetMapping("/req/login")
    public String login(){
        return "login";
    }

    @GetMapping("/req/signup")
    public String signup(){
        return "signup";
    }
    @GetMapping("/index")
    public String home(){
        return "index";
    }
    @GetMapping("/")
    public String root() {
        return "hello worlds"; // Return the name of the view (e.g., "index.html")
    }

}
