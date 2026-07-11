package com.devops.springboot_app.service;

import com.devops.springboot_app.dto.EmployeeRequest;
import com.devops.springboot_app.dto.EmployeeResponse;
import com.devops.springboot_app.entity.Employee;
import com.devops.springboot_app.exception.DuplicateEmployeeException;
import com.devops.springboot_app.exception.EmployeeNotFoundException;
import com.devops.springboot_app.mapper.EmployeeMapper;
import com.devops.springboot_app.repository.EmployeeRepository;
import com.devops.springboot_app.service.impl.EmployeeServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmployeeServiceTest {

    @Mock
    private EmployeeRepository repository;

    @Mock
    private EmployeeMapper mapper;

    @InjectMocks
    private EmployeeServiceImpl service;

    private Employee employee;
    private EmployeeRequest request;
    private EmployeeResponse response;

    @BeforeEach
    void setUp() {

        employee = new Employee();
        employee.setEmployeeId("EMP001");
        employee.setFirstName("Nagaraju");
        employee.setLastName("Dandu");
        employee.setEmail("nagaraju@example.com");
        employee.setDepartment("DevOps");
        employee.setDesignation("Engineer");
        employee.setSalary(new BigDecimal("50000"));

        request = new EmployeeRequest();
        request.setEmployeeId("EMP001");
        request.setFirstName("Nagaraju");
        request.setLastName("Dandu");
        request.setEmail("nagaraju@example.com");
        request.setDepartment("DevOps");
        request.setDesignation("Engineer");
        request.setSalary(new BigDecimal("50000"));

        response = new EmployeeResponse();
        response.setEmployeeId("EMP001");
        response.setFirstName("Nagaraju");
        response.setLastName("Dandu");
        response.setEmail("nagaraju@example.com");
    }

    @Test
    void shouldCreateEmployee() {

        when(repository.existsByEmail(request.getEmail())).thenReturn(false);

        when(repository.existsByEmployeeId(request.getEmployeeId())).thenReturn(false);

        when(mapper.toEntity(request)).thenReturn(employee);

        when(repository.save(employee)).thenReturn(employee);

        when(mapper.toResponse(employee)).thenReturn(response);

        EmployeeResponse result = service.createEmployee(request);

        assertNotNull(result);

        assertEquals("EMP001", result.getEmployeeId());

        verify(repository).save(employee);
    }

    @Test
    void shouldThrowDuplicateEmployeeExceptionWhenEmailExists() {

        when(repository.existsByEmail(request.getEmail())).thenReturn(true);

        assertThrows(
            DuplicateEmployeeException.class,
            () -> service.createEmployee(request)
        );

        verify(repository, never()).save(any());
    }
    
    @Test
    void shouldReturnEmployeeById() {

        when(repository.findById(1L))
            .thenReturn(Optional.of(employee));

        when(mapper.toResponse(employee))
            .thenReturn(response);

        EmployeeResponse result =
            service.getEmployeeById(1L);

        assertNotNull(result);

        assertEquals("EMP001", result.getEmployeeId());
    }

    @Test
    void shouldThrowEmployeeNotFoundException() {

        when(repository.findById(1L))
            .thenReturn(Optional.empty());

        assertThrows(
            EmployeeNotFoundException.class,
            () -> service.getEmployeeById(1L)
        );
    }

}