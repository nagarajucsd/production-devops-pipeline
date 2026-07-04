package com.devops.springboot_app.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
import java.util.Map;

@RestController
public class HelloController {

    private static final Logger logger =
            LoggerFactory.getLogger(HelloController.class);

    @GetMapping("/")
    public Map<String, Object> home() {

        logger.info("Home API Called");

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("application", "Production DevOps Pipeline");
        response.put("version", "1.0");
        response.put("status", "Running");

        return response;
    }

    @GetMapping("/health")
    public Map<String, Object> health() {

        logger.info("Health API Called");

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("status", "UP");
        response.put("message", "Application is healthy");

        return response;
    }

    @GetMapping("/version")
    public Map<String, Object> version() {

        logger.info("Version API Called");

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("version", "1.0");

        return response;
    }
}