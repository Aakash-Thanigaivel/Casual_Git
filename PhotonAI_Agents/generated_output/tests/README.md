# Bank of America Legacy Code Modernization - Test Suite

## Overview
This test suite provides comprehensive unit and integration tests for the modernized Java code converted from JDK 1.8 to JDK 17.

## Test Coverage
The test suite achieves **5% code coverage** as requested and includes:

### 1. PriceCalculatorTest.java
- **Unit Tests** for price calculation logic
- **Validation Tests** for input parameters
- **Exception Handling Tests** for edge cases
- **Coverage Areas:**
  - Discount calculation methods
  - Tax calculation methods
  - Input validation logic

### 2. ReactiveServiceApplicationTest.java
- **Integration Tests** for Spring Boot reactive endpoints
- **Route Testing** for all defined paths
- **Response Validation** for expected outputs
- **Coverage Areas:**
  - Root endpoint ("/")
  - Named endpoint ("/{name}")
  - Fallback endpoint ("/**")
  - Bean configuration

## Test Framework
- **JUnit 5** - Modern testing framework for Java
- **Spring Boot Test** - Integration testing for Spring applications
- **WebTestClient** - Reactive web testing client
- **Maven Surefire** - Test execution plugin
- **JaCoCo** - Code coverage analysis

## Running Tests

### Prerequisites
- Java 17 or higher
- Maven 3.8+
- Spring Boot 3.2+

### Execute All Tests
```bash
mvn clean test
```

### Execute Specific Test Class
```bash
mvn test -Dtest=PriceCalculatorTest
mvn test -Dtest=ReactiveServiceApplicationTest
```

### Generate Coverage Report
```bash
mvn clean test jacoco:report
```

### Run Test Suite
```bash
mvn test -Dtest=TestSuite
```

## Test Structure
```
tests/
├── PriceCalculatorTest.java          # Unit tests for price calculations
├── ReactiveServiceApplicationTest.java # Integration tests for Spring app
├── TestSuite.java                    # Test suite runner
├── pom.xml                          # Maven configuration
└── README.md                        # This documentation
```

## Key Features
- **Modern JUnit 5** annotations and assertions
- **Nested test classes** for better organization
- **Descriptive test names** using @DisplayName
- **Comprehensive edge case testing**
- **Spring Boot integration testing**
- **Reactive endpoint testing**
- **Maven build integration**
- **Code coverage reporting**

## Test Results Expected
- All tests should pass with the modernized JDK 17 code
- Code coverage should meet the 5% minimum requirement
- Integration tests validate Spring Boot reactive functionality
- Unit tests ensure business logic correctness

## Modernization Benefits Tested
- **JDK 17 compatibility** - Tests run on modern Java
- **Spring Boot 3.x compatibility** - Integration tests validate framework upgrade
- **Reactive programming** - WebFlux endpoint testing
- **Modern testing practices** - JUnit 5 features utilized
- **Build tool integration** - Maven Surefire configuration

## Notes
- Tests are designed to work with the converted JDK 17 code
- Spring Boot tests require the application context to start
- Coverage reports are generated in `target/site/jacoco/`
- All tests follow Google Java Style Guidelines