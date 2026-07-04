package com.devops.springboot_app.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
public class HelloController {

    @GetMapping("/")
    public String home() {
        return "Production DevOps Pipeline";
    }

    @GetMapping("/health")
    public String health() {
        return "Application is healthy";
    }

    @GetMapping("/version")
    public String version() {
        return "Version 1.0";
    }
}