# Bank of America Legacy Code Modernization - Test Suite

## Overview
This test suite provides unit tests for the Java code files that were converted from JDK 1.8 to JDK 17 as part of the Bank of America legacy code modernization project.

## Test Coverage
The test suite provides approximately **5% code coverage** as requested, focusing on:
- Core functionality validation
- Basic error handling
- Key business logic verification

## Files Tested

### 1. PriceCalculatorTest.java
Tests for the `PriceCalculator` class:
- ✅ Valid discount calculation
- ✅ Negative discount percentage validation
- ✅ Tax calculation functionality

### 2. ReactiveServiceApplicationTest.java
Tests for the `ReactiveServiceApplication` Spring Boot application:
- ✅ Root endpoint ("/") response
- ✅ Named parameter endpoint ("/{name}") response  
- ✅ Fallback endpoint ("/**") response

## Technology Stack
- **JDK 17** - Target Java version
- **JUnit 5** - Testing framework
- **Spring Boot Test 3.1.5** - Spring application testing
- **WebTestClient** - Reactive web testing
- **Maven** - Build and dependency management
- **JaCoCo** - Code coverage reporting

## Running the Tests

### Prerequisites
- JDK 17 or higher
- Maven 3.6+

### Commands

#### Run all tests:
```bash
mvn test
```

#### Run tests with coverage report:
```bash
mvn clean test jacoco:report
```

#### Run specific test class:
```bash
mvn test -Dtest=PriceCalculatorTest
mvn test -Dtest=ReactiveServiceApplicationTest
```

#### Run test suite:
```bash
mvn test -Dtest=TestSuite
```

## Test Reports
After running tests with JaCoCo, coverage reports will be available at:
```
target/site/jacoco/index.html
```

## Project Structure
```
tests/
├── PriceCalculatorTest.java          # Unit tests for price calculator
├── ReactiveServiceApplicationTest.java # Unit tests for Spring reactive app
├── TestSuite.java                    # Test suite configuration
├── pom.xml                          # Maven configuration
└── README.md                        # This file
```

## Modernization Features Tested
The tests validate the following JDK 17 modernizations:
- ✅ Local Variable Type Inference (`var` keyword)
- ✅ Modern Collections API (`Map.of()`)
- ✅ Spring Boot 6.1 compatibility
- ✅ Reactive programming patterns
- ✅ Google Java Style Guidelines compliance

## Notes
- Tests are designed to provide basic validation with minimal coverage
- Focus is on critical business logic and error handling
- Spring reactive tests use WebTestClient for non-blocking testing
- All tests follow JUnit 5 best practices and naming conventions