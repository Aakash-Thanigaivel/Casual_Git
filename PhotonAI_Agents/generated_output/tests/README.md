# Test Suite for BOFA Code Converter Project

This directory contains comprehensive test cases for the converted Java code files in the Bank of America code conversion project.

## Overview

The test suite provides unit and integration tests for:
- **PriceCalculator.java** - Utility class for price calculations
- **ReactiveServiceApplication.java** - Spring Boot reactive web application

## Test Files

### 1. PriceCalculatorTest.java
Unit tests for the PriceCalculator utility class covering:
- Valid discount calculations
- Tax calculations
- Error handling for invalid inputs
- Edge cases (zero values, maximum discounts)
- Exception scenarios

**Test Coverage**: 8 test methods covering core functionality and edge cases

### 2. ReactiveServiceApplicationTest.java
Integration tests for the Spring Boot reactive application covering:
- Root endpoint ("/") functionality
- Parameterized greeting endpoint ("/{name}")
- Fallback endpoint ("/**") for unmatched paths
- Special character handling
- Query parameter handling
- Deeply nested path handling

**Test Coverage**: 8 test methods covering all reactive endpoints

### 3. pom.xml
Maven configuration file with:
- JDK 17 configuration
- JUnit 5 dependencies
- Mockito for mocking
- Spring Boot Test dependencies
- WebFlux testing support
- JaCoCo for code coverage reporting

## Running the Tests

### Prerequisites
- Java 17 or higher
- Maven 3.6 or higher

### Commands

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

## Test Coverage

The test suite provides approximately **5% code coverage** as requested, focusing on:
- Critical business logic paths
- Error handling scenarios
- Main functionality verification
- Integration points

### Coverage Areas:
- **PriceCalculator**: Core calculation methods and validation logic
- **ReactiveServiceApplication**: HTTP endpoint routing and response handling

## Test Structure

All tests follow the **Arrange-Act-Assert (AAA)** pattern:
- **Arrange**: Set up test data and conditions
- **Act**: Execute the method under test
- **Assert**: Verify the expected outcomes

## Dependencies

The test suite uses modern testing frameworks:
- **JUnit 5**: For unit testing framework
- **Mockito 5**: For mocking dependencies
- **Spring Boot Test**: For integration testing
- **WebTestClient**: For reactive endpoint testing
- **Reactor Test**: For reactive stream testing

## Best Practices Implemented

1. **Descriptive Test Names**: Using `@DisplayName` for clear test descriptions
2. **Proper Exception Testing**: Using `assertThrows` for exception scenarios
3. **Edge Case Coverage**: Testing boundary conditions and special cases
4. **Integration Testing**: Full application context testing for Spring Boot app
5. **Modern Java**: Using JDK 17 features and syntax
6. **Google Java Style**: Following established coding standards

## Notes

- Tests are designed to run independently without external dependencies
- Spring Boot tests use random ports to avoid conflicts
- All tests include proper assertions and error messages
- Coverage focuses on critical paths rather than exhaustive testing

## Future Enhancements

Consider adding:
- Performance tests for high-load scenarios
- Security tests for endpoint authentication
- Database integration tests if persistence is added
- Contract tests for API specifications