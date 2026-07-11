package com.devops.springboot_app.controller;

import com.devops.springboot_app.dto.EmployeeRequest;
import com.devops.springboot_app.dto.EmployeeResponse;
import com.devops.springboot_app.exception.DuplicateEmployeeException;
import com.devops.springboot_app.exception.EmployeeNotFoundException;
import com.devops.springboot_app.service.EmployeeService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(EmployeeController.class)
class EmployeeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private EmployeeService employeeService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldCreateEmployee() throws Exception {

        EmployeeRequest request = new EmployeeRequest();

        request.setEmployeeId("EMP001");
        request.setFirstName("Nagaraju");
        request.setLastName("Dandu");
        request.setEmail("nagaraju@example.com");
        request.setDepartment("DevOps");
        request.setDesignation("Engineer");
        request.setSalary(new BigDecimal("50000"));

        EmployeeResponse response = new EmployeeResponse();

        response.setEmployeeId("EMP001");
        response.setFirstName("Nagaraju");

        when(employeeService.createEmployee(any()))
                .thenReturn(response);

        mockMvc.perform(post("/api/employees")

                        .contentType(MediaType.APPLICATION_JSON)

                        .content(objectMapper.writeValueAsString(request)))

                .andExpect(status().isOk())

                .andExpect(jsonPath("$.success").value(true))

                .andExpect(jsonPath("$.data.employeeId").value("EMP001"));
    }

    @Test
    void shouldGetAllEmployees() throws Exception {

        EmployeeResponse response = new EmployeeResponse();

        response.setEmployeeId("EMP001");
        response.setFirstName("Nagaraju");

        when(employeeService.getAllEmployees())
                .thenReturn(List.of(response));

        mockMvc.perform(get("/api/employees"))

                .andExpect(status().isOk())

                .andExpect(jsonPath("$.success").value(true))

                .andExpect(jsonPath("$.data[0].employeeId").value("EMP001"));
    }
    @Test
    void shouldGetEmployeeById() throws Exception {

        EmployeeResponse response = new EmployeeResponse();
        response.setEmployeeId("EMP001");
        response.setFirstName("Nagaraju");

        when(employeeService.getEmployeeById(1L))
            .thenReturn(response);

        mockMvc.perform(get("/api/employees/1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.data.employeeId").value("EMP001"));
    }
    @Test
    void shouldUpdateEmployee() throws Exception {

        EmployeeRequest request = new EmployeeRequest();

        request.setEmployeeId("EMP001");
        request.setFirstName("Updated");
        request.setLastName("Dandu");
        request.setEmail("nagaraju@example.com");
        request.setDepartment("DevOps");
        request.setDesignation("Senior Engineer");
        request.setSalary(new BigDecimal("70000"));

        EmployeeResponse response = new EmployeeResponse();

        response.setEmployeeId("EMP001");
        response.setFirstName("Updated");

        when(employeeService.updateEmployee(eq(1L), any(EmployeeRequest.class)))
            .thenReturn(response);

        mockMvc.perform(put("/api/employees/1")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.data.firstName").value("Updated"));
    }
    @Test
    void shouldDeleteEmployee() throws Exception {

        doNothing().when(employeeService).deleteEmployee(1L);

        mockMvc.perform(delete("/api/employees/1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.message")
                    .value("Employee deleted successfully"));
    }
    @Test
    void shouldReturnBadRequestForInvalidEmployee() throws Exception {

        EmployeeRequest request = new EmployeeRequest();

        request.setEmployeeId("");
        request.setFirstName("");
        request.setEmail("invalid-email");
        request.setSalary(new BigDecimal("-100"));

        mockMvc.perform(post("/api/employees")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturnNotFoundWhenEmployeeDoesNotExist() throws Exception {

        when(employeeService.getEmployeeById(100L))
            .thenThrow(new EmployeeNotFoundException(100L));

        mockMvc.perform(get("/api/employees/100"))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.success").value(false));
    }
    @Test
    void shouldReturnConflictWhenDuplicateEmployeeExists() throws Exception {

        EmployeeRequest request = new EmployeeRequest();

        request.setEmployeeId("EMP001");
        request.setFirstName("Nagaraju");
        request.setLastName("Dandu");
        request.setEmail("nagaraju@example.com");
        request.setDepartment("DevOps");
        request.setDesignation("Engineer");
        request.setSalary(new BigDecimal("50000"));

        when(employeeService.createEmployee(any(EmployeeRequest.class)))
            .thenThrow(new DuplicateEmployeeException(
                    "Email",
                    "nagaraju@example.com"));

        mockMvc.perform(post("/api/employees")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isConflict())
            .andExpect(jsonPath("$.success").value(false));
    }

}