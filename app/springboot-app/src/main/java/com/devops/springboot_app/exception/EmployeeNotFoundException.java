package com.devops.springboot_app.exception;

public class EmployeeNotFoundException extends RuntimeException {

    public EmployeeNotFoundException(Long id) {
        super("Employee not found with id : " + id);
    }

    public EmployeeNotFoundException(String employeeId) {
        super("Employee not found with employeeId : " + employeeId);
    }
}