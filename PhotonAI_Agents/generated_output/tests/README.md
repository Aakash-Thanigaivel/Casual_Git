# Test Suite Documentation

## Overview
This test suite provides comprehensive unit tests for the converted Java code files with 5% code coverage as requested.

## Test Files Generated

### 1. PriceCalculatorTest.java
- **Purpose**: Unit tests for the PriceCalculator utility class
- **Coverage**: Tests core functionality including discount calculations, tax calculations, and error handling
- **Test Methods**:
  - `testCalculateDiscountedPrice()` - Validates basic discount calculation
  - `testCalculateDiscountedPriceInvalidDiscount()` - Tests exception handling for invalid discounts
  - `testCalculateFinalPriceWithTax()` - Validates tax calculation
  - `testCalculateFinalPriceWithTaxNegativeRate()` - Tests exception handling for negative tax rates
  - `testZeroValues()` - Tests edge cases with zero values

### 2. ReactiveServiceApplicationTest.java
- **Purpose**: Integration tests for the Spring Boot reactive application
- **Coverage**: Tests application startup, route configuration, and endpoint responses
- **Test Methods**:
  - `contextLoads()` - Validates Spring Boot application context loading
  - `testRoutesBean()` - Tests router function bean creation
  - `testRootEndpoint()` - Tests root "/" endpoint response
  - `testNamedEndpoint()` - Tests parameterized "/{name}" endpoint
  - `testFallbackEndpoint()` - Tests wildcard "/**" fallback endpoint

## Test Framework Dependencies
- **JUnit 5** (Jupiter) for unit testing
- **Spring Boot Test** for integration testing
- **WebFluxTest** for reactive web testing
- **WebTestClient** for testing reactive endpoints

## Running the Tests
```bash
# Run all tests
mvn test

# Run specific test class
mvn test -Dtest=PriceCalculatorTest
mvn test -Dtest=ReactiveServiceApplicationTest

# Run with coverage report
mvn test jacoco:report
```

## Coverage Goals
- **Target**: 5% code coverage as specified
- **Focus**: Core functionality and critical paths
- **Approach**: Basic positive and negative test cases for each major method

## Test Location
All test files are saved to: `gs://codestorage-development/Demo_Output/jai/code_converter_output/tests/`