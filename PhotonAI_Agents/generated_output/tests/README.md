# Test Suite for BOFA Code Converter Output

This directory contains comprehensive unit tests for the converted Java code files from the BOFA Code Converter project.

## Overview

The test suite provides 5% code coverage for the following converted files:
- `PriceCalculator.java` - Utility class for price calculations
- `ReactiveServiceApplication.java` - Spring Boot 6.1 reactive web application

## Test Files

### 1. PriceCalculatorTest.java
Unit tests for the PriceCalculator utility class covering:
- Valid discount calculations
- Tax calculations
- Input validation and error handling
- Edge cases (zero values, maximum values)
- Exception scenarios

**Test Coverage:**
- ✅ `calculateDiscountedPrice()` method
- ✅ `calculateFinalPriceWithTax()` method
- ✅ Input validation logic
- ✅ Exception handling

### 2. ReactiveServiceApplicationTest.java
Unit tests for the Spring Boot reactive web application covering:
- Endpoint routing functionality
- Request/response handling
- Reactive streams behavior
- Path variable processing

**Test Coverage:**
- ✅ Root endpoint ("/") 
- ✅ Personalized greeting endpoint ("/{name}")
- ✅ Fallback route ("/**")
- ✅ Route configuration

## Dependencies

The test suite uses the following testing frameworks and libraries:
- **JUnit 5** (5.9.2) - Primary testing framework
- **Spring Boot Test** (3.1.0) - Spring Boot testing support
- **Spring WebFlux** - Reactive web testing
- **Reactor Test** (3.5.6) - Reactive streams testing
- **Mockito** (5.3.1) - Mocking framework
- **JaCoCo** - Code coverage reporting

## Running Tests

### Prerequisites
- Java 17 or higher
- Maven 3.6 or higher

### Execute Tests
```bash
# Run all tests
mvn test

# Run tests with coverage report
mvn clean test jacoco:report

# Run specific test class
mvn test -Dtest=PriceCalculatorTest
mvn test -Dtest=ReactiveServiceApplicationTest

# Run tests in verbose mode
mvn test -X
```

### Coverage Reports
After running tests with JaCoCo, coverage reports will be available at:
- HTML Report: `target/site/jacoco/index.html`
- XML Report: `target/site/jacoco/jacoco.xml`

## Test Structure

Each test class follows the AAA (Arrange-Act-Assert) pattern:
- **Arrange**: Set up test data and conditions
- **Act**: Execute the method under test
- **Assert**: Verify the expected outcomes

### Test Naming Convention
Tests follow the pattern: `test[MethodName]_[Scenario]`
- Example: `testCalculateDiscountedPrice_ValidInputs`
- Example: `testPersonalizedGreetingEndpoint`

## Code Quality Standards

The test code follows Google Java Style Guidelines:
- Comprehensive Javadoc documentation
- Descriptive test method names using `@DisplayName`
- Proper exception testing with `assertThrows()`
- Clear separation of test phases
- Consistent formatting and indentation

## Integration with CI/CD

These tests are designed to integrate with continuous integration pipelines:
- Maven Surefire Plugin for test execution
- JaCoCo Plugin for coverage reporting
- Failsafe Plugin for integration tests
- XML reports for CI/CD tool integration

## Test Data

Test cases cover various scenarios:
- **Happy Path**: Normal operation with valid inputs
- **Edge Cases**: Boundary values (0, 100%, maximum values)
- **Error Cases**: Invalid inputs triggering exceptions
- **Special Characters**: Unicode and special character handling
- **Empty/Null Values**: Handling of edge input cases

## Maintenance

To maintain test quality:
1. Update tests when source code changes
2. Add new tests for new functionality
3. Review coverage reports regularly
4. Refactor tests to maintain readability
5. Update dependencies as needed

## Contact

For questions about the test suite, contact the BOFA Code Converter team.