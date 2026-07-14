package com.devops.springboot_app.repository;

import com.devops.springboot_app.entity.Employee;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class EmployeeRepositoryTest {

    @Autowired
    private EmployeeRepository repository;

    private Employee buildEmployee() {

        Employee employee = new Employee();

        employee.setEmployeeId("EMP001");
        employee.setFirstName("Nagaraju");
        employee.setLastName("Dandu");
        employee.setEmail("nagaraju@example.com");
        employee.setDepartment("DevOps");
        employee.setDesignation("Engineer");
        employee.setSalary(new BigDecimal("50000"));

        return employee;
    }
    @Test
    @DisplayName("Should save employee")
    void shouldSaveEmployee() {

        Employee saved = repository.save(buildEmployee());

        assertNotNull(saved.getId());

        assertEquals("EMP001", saved.getEmployeeId());
    }
    @Test
    @DisplayName("Should return true when email exists")
    void shouldReturnTrueWhenEmailExists() {

        repository.save(buildEmployee());

        assertTrue(
            repository.existsByEmail("nagaraju@example.com")
        );
    }
    @Test
    @DisplayName("Should return true when employeeId exists")
    void shouldReturnTrueWhenEmployeeIdExists() {

        repository.save(buildEmployee());

        assertTrue(
            repository.existsByEmployeeId("EMP001")
        );
    }
    @Test
    @DisplayName("Should find employee by id")
    void shouldFindEmployeeById() {

        Employee saved = repository.save(buildEmployee());

        Employee employee = repository.findById(saved.getId())
            .orElseThrow();

        assertEquals(
            "EMP001",
            employee.getEmployeeId()
        );
    }

}