package com.devops.springboot_app.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/employees")
public class EmployeeController {

    private static final Logger logger =
            LoggerFactory.getLogger(EmployeeController.class);

    @GetMapping
    public List<String> getEmployees() {

        logger.info("Employees API Called");

        return List.of(
                "Alice",
                "Bob",
                "Charlie"
        );
    }
}