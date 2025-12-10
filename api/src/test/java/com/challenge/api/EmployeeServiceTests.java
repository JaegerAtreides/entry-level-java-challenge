package com.challenge.api;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.challenge.api.controller.EmployeeController;
import com.challenge.api.model.EmployeesRUsEmployee;
import com.challenge.api.repository.EmployeeRepository;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class EmployeeServiceTests {

    @Mock
    private EmployeeRepository employeeRepository;

    @InjectMocks
    private EmployeeController employeeController;

    private EmployeesRUsEmployee testEmployee;
    private UUID testUuid;

    @BeforeEach
    void setUp() {
        testUuid = UUID.randomUUID();
        testEmployee = new EmployeesRUsEmployee();
        testEmployee.setUuid(testUuid);
        testEmployee.setFirstName("John");
        testEmployee.setLastName("Doe");
        testEmployee.setFullName("John Doe");
        testEmployee.setSalary(80000);
        testEmployee.setAge(30);
        testEmployee.setJobTitle("Software Engineer");
        testEmployee.setEmail("john.doe@example.com");
        testEmployee.setContractHireDate(Instant.now());
    }

    // --------------------------------------------------------------------------------------
    // GET ALL EMPLOYEES TESTS

    /**
     * Verifies that getAllEmployees returns a list with three employees when the repository contains three employees.
     * Tests the happy path for retrieving multiple employees.
     */
    @Test
    void testGetAllEmployeesReturnsThreeEmployees_Success() {
        List<EmployeesRUsEmployee> employees = List.of(testEmployee, testEmployee, testEmployee);
        when(employeeRepository.findAll()).thenReturn((List) employees);

        List<?> result = employeeController.getAllEmployees();

        assertNotNull(result);
        assertEquals(3, result.size());
    }

    /**
     * Verifies that getAllEmployees returns an empty list when no employees exist in the repository.
     * Tests the edge case of an empty database.
     */
    @Test
    void testGetAllEmployeesReturnsEmptyListWhenNoEmployees() {
        when(employeeRepository.findAll()).thenReturn(List.of());

        List<?> result = employeeController.getAllEmployees();

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }
    // --------------------------------------------------------------------------------------
    // GET EMPLOYEE BY UUID TESTS

    /**
     * Verifies that getEmployeeByUuid successfully returns an employee when a matching UUID is found.
     * Tests the happy path for retrieving a single employee by ID.
     */
    @Test
    void testGetEmployeeByUuid_Success() {
        when(employeeRepository.findEmployeeByUuid(testUuid)).thenReturn(Optional.of(testEmployee));

        EmployeesRUsEmployee result = (EmployeesRUsEmployee) employeeController.getEmployeeByUuid(testUuid);

        assertNotNull(result);
        assertEquals("John", result.getFirstName());
        assertEquals("Doe", result.getLastName());
        assertEquals(testUuid, result.getUuid());
    }

    /**
     * Verifies that getEmployeeByUuid throws an exception when no employee with the given UUID is found.
     * Tests the error handling for non-existent employees.
     */
    @Test
    void testGetEmployeeByUuid_NotFound() {
        UUID nonExistentUuid = UUID.randomUUID();
        when(employeeRepository.findEmployeeByUuid(nonExistentUuid)).thenReturn(Optional.empty());

        assertThrows(Exception.class, () -> employeeController.getEmployeeByUuid(nonExistentUuid));
    }
    // --------------------------------------------------------------------------------------
    // CREATE EMPLOYEE TESTS

    /**
     * Verifies that createEmployee successfully creates a new employee with required fields.
     * Tests the basic path for employee creation.
     */
    @Test
    void testCreateEmployee_Success() {
        EmployeesRUsEmployee newEmployee = new EmployeesRUsEmployee();
        newEmployee.setFirstName("Jane");
        newEmployee.setLastName("Smith");
        newEmployee.setFullName("Jane Smith");
        newEmployee.setSalary(60000);
        newEmployee.setAge(28);
        newEmployee.setJobTitle("Product Manager");
        newEmployee.setEmail("jane.smith@example.com");
        newEmployee.setContractHireDate(Instant.now());

        when(employeeRepository.save(newEmployee)).thenReturn(newEmployee);

        EmployeesRUsEmployee result = (EmployeesRUsEmployee) employeeController.createEmployee(newEmployee);

        assertNotNull(result);
        assertNotNull(result.getUuid());
        assertEquals("Jane", result.getFirstName());
        assertEquals("Smith", result.getLastName());
    }

    /**
     * Verifies that createEmployee preserves all populated fields during creation.
     * Tests that no data is lost when creating an employee with all fields set.
     */
    @Test
    void testCreateEmployee_AllFieldsPopulated() {
        EmployeesRUsEmployee newEmployee = new EmployeesRUsEmployee();
        newEmployee.setFirstName("Bob");
        newEmployee.setLastName("Johnson");
        newEmployee.setFullName("Bob Johnson");
        newEmployee.setSalary(75000);
        newEmployee.setAge(35);
        newEmployee.setJobTitle("Senior Developer");
        newEmployee.setEmail("bob.johnson@example.com");
        newEmployee.setContractHireDate(Instant.now());

        when(employeeRepository.save(newEmployee)).thenReturn(newEmployee);

        EmployeesRUsEmployee result = (EmployeesRUsEmployee) employeeController.createEmployee(newEmployee);

        assertEquals("Bob", result.getFirstName());
        assertEquals("Johnson", result.getLastName());
        assertEquals("Bob Johnson", result.getFullName());
        assertEquals(75000, result.getSalary());
        assertEquals(35, result.getAge());
        assertEquals("Senior Developer", result.getJobTitle());
        assertEquals("bob.johnson@example.com", result.getEmail());
        assertNotNull(result.getContractHireDate());
    }

    // --------------------------------------------------------------------------------------
    // CONTRACT TERMINATION DATE TESTS

    /**
     * Verifies that createEmployee handles employees with null contractTerminationDate.
     * Tests that active employees (not yet terminated) can be created without a termination date.
     */
    @Test
    void testCreateEmployee_ContractTerminationDateNull() {
        EmployeesRUsEmployee newEmployee = new EmployeesRUsEmployee();
        newEmployee.setFirstName("Alice");
        newEmployee.setLastName("Williams");
        newEmployee.setFullName("Alice Williams");
        newEmployee.setSalary(70000);
        newEmployee.setAge(32);
        newEmployee.setJobTitle("Data Analyst");
        newEmployee.setEmail("alice.williams@example.com");
        newEmployee.setContractHireDate(Instant.now());
        newEmployee.setContractTerminationDate(null);

        when(employeeRepository.save(newEmployee)).thenReturn(newEmployee);

        EmployeesRUsEmployee result = (EmployeesRUsEmployee) employeeController.createEmployee(newEmployee);

        assertNotNull(result);
        assertNull(result.getContractTerminationDate());
        assertEquals("Alice", result.getFirstName());
    }

    /**
     * Verifies that createEmployee correctly preserves contractTerminationDate when provided.
     * Tests that terminated employees can be created with a termination date.
     */
    @Test
    void testCreateEmployee_ContractTerminationDatePopulated() {
        Instant hireDate = Instant.now();
        Instant terminationDate = hireDate.plusSeconds(86400 * 365);

        EmployeesRUsEmployee newEmployee = new EmployeesRUsEmployee();
        newEmployee.setFirstName("Charlie");
        newEmployee.setLastName("Brown");
        newEmployee.setFullName("Charlie Brown");
        newEmployee.setSalary(55000);
        newEmployee.setAge(26);
        newEmployee.setJobTitle("Junior Developer");
        newEmployee.setEmail("charlie.brown@example.com");
        newEmployee.setContractHireDate(hireDate);
        newEmployee.setContractTerminationDate(terminationDate);

        when(employeeRepository.save(newEmployee)).thenReturn(newEmployee);

        EmployeesRUsEmployee result = (EmployeesRUsEmployee) employeeController.createEmployee(newEmployee);

        assertNotNull(result);
        assertNotNull(result.getContractTerminationDate());
        assertEquals(terminationDate, result.getContractTerminationDate());
        assertEquals("Charlie", result.getFirstName());
    }

    // --------------------------------------------------------------------------------------
    // UUID VALIDATION TESTS

    /**
     * Verifies that setUuid successfully stores a valid UUID.
     * Tests the happy path for UUID assignment.
     */
    @Test
    void testSetUuid_SucceedsWithValidUuid() {
        EmployeesRUsEmployee employee = new EmployeesRUsEmployee();
        UUID validUuid = UUID.randomUUID();

        employee.setUuid(validUuid);

        assertEquals(validUuid, employee.getUuid());
    }

    /**
     * Verifies that the controller automatically assigns a UUID to new employees.
     * Tests that the controller generates and sets a UUID before saving.
     */
    @Test
    void testCreateEmployee_UuidAssignedByController() {
        EmployeesRUsEmployee newEmployee = new EmployeesRUsEmployee();
        newEmployee.setFirstName("David");
        newEmployee.setLastName("Miller");
        newEmployee.setFullName("David Miller");
        newEmployee.setSalary(65000);
        newEmployee.setAge(29);
        newEmployee.setJobTitle("QA Engineer");
        newEmployee.setEmail("david.miller@example.com");
        newEmployee.setContractHireDate(Instant.now());

        when(employeeRepository.save(any(EmployeesRUsEmployee.class))).thenAnswer(invocation -> {
            EmployeesRUsEmployee arg = invocation.getArgument(0);
            return arg;
        });

        EmployeesRUsEmployee result = (EmployeesRUsEmployee) employeeController.createEmployee(newEmployee);

        assertNotNull(result.getUuid());
    }
}
