# ReliaQuest's Entry-Level Java Challenge

Please keep the following in mind while working on this challenge:
* Code implementations will not be graded for **correctness** but rather on practicality
* Articulate clear and concise design methodologies, if necessary
* Use clean coding etiquette
* E.g. avoid liberal use of new-lines, odd variable and method names, random indentation, etc...
* Test cases are not required

## Problem Statement

Your employer has recently purchased a license to top-tier SaaS platform, Employees-R-US, to off-load all employee management responsibilities.
Unfortunately, your company's product has an existing employee management solution that is tightly coupled to other services and therefore 
cannot be replaced whole-cloth. Product and Development leads in your department have decided it would be best to interface
the existing employee management solution with the commercial offering from Employees-R-US for the time being until all employees can be
migrated to the new SaaS platform.

Your ask is to expose employee information as a protected, secure REST API for consumption by Employees-R-US web hooks.
The initial REST API will consist of 3 endpoints, listed in the following section. If for any reason the implementation 
of an endpoint is problematic, the team lead will accept **pseudo-code** and a pertinent description (e.g. java-doc) of intent.

Good luck!

### Project Implementation

This project includes comprehensive JUnit 5 tests for the Employee management system using Mockito for dependency mocking.

### Classes Implemented

**EmployeesRUsEmployee** (`EmployeesRUsEmployee.java`)
- Concrete JPA Entity implementation of the Employee interface
- Persisted with `@Entity` annotation
- UUID field marked as `@Id` and `@Column(nullable = false)`
- Implements all getter/setter methods from Employee interface
- Handles nullable contractTerminationDate for active vs. terminated employees

**EmployeeRepository** (`EmployeeRepository.java`)
- Spring Data JPA repository extending `JpaRepository<Employee, UUID>`
- Provides CRUD operations: save(), findById(), findAll(), delete()
- Custom method: `findEmployeeByUuid(UUID uuid)` - Returns Optional<Employee>
- Automatically generates database queries based on method naming conventions

**EmployeeController** (`EmployeeController.java`)
- REST controller with base path `/api/v1/employee`
- `GET /` - Retrieves all employees
- `GET /{uuid}` - Retrieves employee by UUID (throws 404 if not found)
- `POST /` - Creates new employee with auto-generated UUID

### Test Coverage

**EmployeeServiceTests** (`EmployeeServiceTests.java`)

## Code Formatting

To format code according to style guidelines, you can run **spotlessApply** task.
`./gradlew spotlessApply`

The spotless plugin will also execute check-and-validation tasks as part of the gradle **build** task.
`./gradlew build`
