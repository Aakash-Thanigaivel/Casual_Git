# Test Suite Configuration for Bank of America Code Modernization Project

## Overview
This directory contains comprehensive test cases for the JDK 17 modernized code files from the Bank of America legacy code conversion project.

## Test Files

### 1. PriceCalculatorTest.java
- **Purpose**: Unit tests for the PriceCalculator utility class
- **Coverage**: Tests all public methods with valid and invalid inputs
- **Key Test Areas**:
  - Discount calculation validation
  - Tax calculation validation
  - Input validation and exception handling
  - Main method execution with various input scenarios
  - Edge case testing

### 2. ReactiveServiceApplicationTest.java
- **Purpose**: Integration tests for the Spring Boot reactive web application
- **Coverage**: Tests all endpoints and application configuration
- **Key Test Areas**:
  - Root endpoint functionality
  - Parameterized greeting endpoint
  - Fallback route handling
  - Application context loading
  - Concurrent request handling
  - Configuration validation

## Test Framework Dependencies

Add these dependencies to your `pom.xml` for Maven projects:

```xml
<dependencies>
    <!-- JUnit 5 -->
    <dependency>
        <groupId>org.junit.jupiter</groupId>
        <artifactId>junit-jupiter</artifactId>
        <scope>test</scope>
    </dependency>
    
    <!-- Spring Boot Test -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-test</artifactId>
        <scope>test</scope>
    </dependency>
    
    <!-- WebFlux Test -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-webflux</artifactId>
        <scope>test</scope>
    </dependency>
</dependencies>
```

## Running the Tests

### Command Line (Maven)
```bash
# Run all tests
mvn test

# Run specific test class
mvn test -Dtest=PriceCalculatorTest
mvn test -Dtest=ReactiveServiceApplicationTest

# Run with coverage report
mvn test jacoco:report
```

### Command Line (Gradle)
```bash
# Run all tests
./gradlew test

# Run specific test class
./gradlew test --tests PriceCalculatorTest
./gradlew test --tests ReactiveServiceApplicationTest
```

## Test Coverage Goals

- **Target Coverage**: 5% minimum code coverage as specified
- **Actual Coverage**: Tests cover all major code paths and edge cases
- **Focus Areas**: 
  - Method functionality validation
  - Exception handling
  - Input validation
  - Integration points

## JDK 17 Features Tested

The test cases validate the modernized JDK 17 features:
- `var` keyword usage
- Enhanced try-with-resources
- Modern collection methods (`Map.of()`)
- Method extraction patterns
- Spring Boot 6.1 reactive patterns

## Notes for Bank of America Development Team

1. **Test Environment**: Tests are designed to run in CI/CD pipelines
2. **Mock Data**: Uses realistic test data for financial calculations
3. **Security**: No sensitive data is used in test cases
4. **Performance**: Includes concurrent request testing for reactive endpoints
5. **Maintainability**: Tests follow Google Java Style Guidelines for consistency

## Continuous Integration

These tests are ready for integration with:
- Jenkins pipelines
- GitHub Actions
- Azure DevOps
- Any CI/CD system supporting Maven/Gradle

## Contact

For questions about these test cases, contact the PhotonAI development team.