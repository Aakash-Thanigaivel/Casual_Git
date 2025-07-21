# Bank of America Legacy Code Modernization - Test Suite

This directory contains comprehensive unit tests for the modernized Java code that was converted from JDK 1.8 to JDK 17 with Spring Boot 6.1 upgrades.

## Test Coverage

### 1. PriceCalculatorTest.java
**Coverage Target: 5%+ of core functionality**

Tests for the `PriceCalculator` utility class:
- ✅ Valid discount calculations
- ✅ Parameterized tests for various discount percentages (0%, 25%, 50%, 100%)
- ✅ Exception handling for invalid discount percentages (negative, >100%)
- ✅ Valid tax calculations with different tax rates
- ✅ Exception handling for negative tax rates
- ✅ Edge cases and boundary conditions

**Key Test Methods:**
- `testCalculateDiscountedPriceValidInputs()` - Basic discount calculation
- `testCalculateDiscountedPriceParameterized()` - Multiple discount scenarios
- `testCalculateDiscountedPriceNegativeDiscount()` - Exception testing
- `testCalculateFinalPriceWithTaxValidInputs()` - Basic tax calculation
- `testCalculateFinalPriceWithTaxNegativeRate()` - Exception testing

### 2. ReactiveServiceApplicationTest.java
**Coverage Target: 5%+ of endpoint functionality**

Integration tests for the Spring Boot reactive web application:
- ✅ Root endpoint (`/`) returns "hi"
- ✅ Name endpoint (`/{name}`) returns personalized greeting
- ✅ Fallback endpoint (`/**`) handles unknown paths
- ✅ Special characters in path parameters
- ✅ Nested unknown paths routing
- ✅ Multiple name variations testing

**Key Test Methods:**
- `testRootEndpoint()` - Root path functionality
- `testNameEndpoint()` - Personalized greeting
- `testFallbackEndpoint()` - Catch-all routing
- `testNameEndpointWithSpecialCharacters()` - Edge case handling

## Testing Framework

### Technologies Used:
- **JUnit 5** - Modern testing framework for Java
- **Spring Boot Test** - Integration testing support
- **WebTestClient** - Reactive web testing
- **Parameterized Tests** - Data-driven testing
- **AssertJ** - Fluent assertions (available in dependencies)

### Maven Configuration:
- **Surefire Plugin** - Unit test execution
- **Failsafe Plugin** - Integration test execution  
- **JaCoCo Plugin** - Code coverage reporting with 5% minimum threshold

## Running Tests

### Prerequisites:
- Java 17+
- Maven 3.8+
- Spring Boot 6.1 dependencies

### Commands:

```bash
# Run all tests
mvn test

# Run tests with coverage report
mvn clean test jacoco:report

# Run only unit tests
mvn surefire:test

# Run only integration tests
mvn failsafe:integration-test

# Generate coverage report
mvn jacoco:report
```

### Coverage Reports:
After running tests with JaCoCo, coverage reports will be available at:
- `target/site/jacoco/index.html` - HTML coverage report
- `target/site/jacoco/jacoco.xml` - XML coverage report

## Test Structure

```
tests/
├── PriceCalculatorTest.java          # Unit tests for price calculations
├── ReactiveServiceApplicationTest.java # Integration tests for web endpoints
├── pom.xml                           # Maven configuration with test dependencies
└── README.md                         # This documentation
```

## Code Quality Standards

The test suite follows Google Java Style Guidelines and includes:
- ✅ Comprehensive JavaDoc documentation
- ✅ Descriptive test method names with `@DisplayName`
- ✅ Proper test organization with Given-When-Then structure
- ✅ Parameterized tests for data-driven scenarios
- ✅ Exception testing with proper assertions
- ✅ Integration tests using Spring Boot Test framework

## Modernization Features Tested

### JDK 17 Features:
- ✅ `var` keyword usage in test setup
- ✅ Modern exception handling patterns
- ✅ Enhanced readability with proper method extraction

### Spring Boot 6.1 Features:
- ✅ Reactive web testing with WebTestClient
- ✅ Modern Spring Boot test annotations
- ✅ Integration with Spring Boot 3.x test framework

## Continuous Integration

The test suite is designed to integrate with CI/CD pipelines:
- Minimum 5% code coverage requirement
- Fail-fast on test failures
- Comprehensive reporting for quality gates
- Compatible with modern build systems

## Notes

- Tests are designed to provide meaningful coverage while maintaining simplicity
- Focus on critical business logic and edge cases
- Integration tests verify end-to-end functionality
- All tests follow modern Java and Spring Boot testing best practices