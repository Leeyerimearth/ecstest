package com.example.ecstest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    @GetMapping("/home")
    public String home() {
        return "hello world";
    }

    @GetMapping("/")
    public String healthyCheck() {

        return "healthy!";
    }
}
