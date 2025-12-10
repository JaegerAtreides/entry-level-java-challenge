package com.challenge.api.repository;

import com.challenge.api.model.Employee;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for Employee persistence operations.
 * Provides database access and CRUD operations for Employee entities.
 *
 * Extends JpaRepository to inherit standard persistence methods:
 * - save(Employee) - Persists or updates an employee
 * - findById(UUID) - Retrieves an employee by UUID
 * - findAll() - Retrieves all employees
 * - delete(Employee) - Removes an employee
 *
 * @see org.springframework.data.jpa.repository.JpaRepository
 */
@Repository
public interface EmployeeRepository extends JpaRepository<Employee, UUID> {

    /**
     * Finds an employee by their UUID.
     *
     * This method uses Spring Data's query derivation to automatically generate
     * a database query based on the method name and parameter.
     *
     * @param uuid the unique identifier of the employee to retrieve
     * @return an Optional containing the employee if found, or an empty Optional if not found
     */
    Optional<Employee> findEmployeeByUuid(UUID uuid);
}
