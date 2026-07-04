package com.devops.springboot_app.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    private static final Logger logger =
            LoggerFactory.getLogger(HelloController.class);

    @GetMapping("/")
    public String home() {

        logger.info("Home API Called");

        return "Production DevOps Pipeline";
    }

    @GetMapping("/health")
    public String health() {

        logger.info("Health API Called");

        return "Application is healthy";
    }

    @GetMapping("/version")
    public String version() {

        logger.info("Version API Called");

        return "Version 1.0";
    }
}