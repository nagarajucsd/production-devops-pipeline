package com.devops.springboot_app.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    @GetMapping("/")
    public String home() {
        return "Production DevOps Pipeline is Running!";
    }

    @GetMapping("/health")
    public String health() {
        return "Application is Healthy";
    }

    @GetMapping("/version")
    public String version() {
        return "Version 1.0";
    }
}