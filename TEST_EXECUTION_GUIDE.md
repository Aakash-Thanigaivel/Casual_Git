# Test Execution Guide for Bank of America Price Calculator

## Overview
This document provides instructions for running the comprehensive test suite for the converted PriceCalculator application.

## Prerequisites
- Java 17 or higher
- Maven 3.8.0 or higher
- JUnit 5.9.2

## Test Structure

### Test Classes
1. **PriceCalculatorTest.java** - Core unit tests for business logic
2. **PriceCalculatorIntegrationTest.java** - Integration tests for main method
3. **PriceCalculatorPerformanceTest.java** - Performance and boundary tests
4. **PriceCalculatorTestSuite.java** - Test suite aggregator

### Test Coverage Areas
- ✅ **Method Testing**: All public methods tested
- ✅ **Edge Cases**: Boundary values, zero values, large numbers
- ✅ **Exception Handling**: Invalid inputs and error conditions
- ✅ **Integration Testing**: Complete user workflow
- ✅ **Performance Testing**: Efficiency and consistency
- ✅ **Class Structure**: Utility class pattern validation

## Running Tests

### Command Line Execution

#### Run All Tests
```bash
mvn clean test
```

#### Run Specific Test Class
```bash
mvn test -Dtest=PriceCalculatorTest
mvn test -Dtest=PriceCalculatorIntegrationTest
mvn test -Dtest=PriceCalculatorPerformanceTest
```

#### Run Test Suite
```bash
mvn test -Dtest=PriceCalculatorTestSuite
```

#### Generate Coverage Report
```bash
mvn clean test jacoco:report
```

#### Verify Coverage Threshold (20%)
```bash
mvn clean verify
```

### IDE Execution
- **IntelliJ IDEA**: Right-click on test class → Run
- **Eclipse**: Right-click on test class → Run As → JUnit Test
- **VS Code**: Use Java Test Runner extension

## Test Coverage Report

### Coverage Metrics
- **Line Coverage**: Minimum 20% (as specified)
- **Method Coverage**: 100% of public methods
- **Branch Coverage**: All conditional branches tested

### Coverage Report Location
After running `mvn test jacoco:report`, find the report at:
```
target/site/jacoco/index.html
```

## Test Categories

### 1. Unit Tests (PriceCalculatorTest.java)
- **calculateDiscountedPrice()** method testing
- **calculateFinalPriceWithTax()** method testing
- Exception handling validation
- Parameterized testing for multiple scenarios

### 2. Integration Tests (PriceCalculatorIntegrationTest.java)
- Complete user input flow testing
- System input/output validation
- Error handling in main method
- Various input combinations

### 3. Performance Tests (PriceCalculatorPerformanceTest.java)
- Calculation consistency verification
- Boundary value testing
- Floating point precision handling
- Performance benchmarking
- Mathematical property validation

## Expected Test Results

### Test Execution Summary
```
Tests run: 25+
Failures: 0
Errors: 0
Skipped: 0
Success rate: 100%
```

### Coverage Summary
```
Line Coverage: ≥20%
Method Coverage: 100%
Class Coverage: 100%
```

## Continuous Integration

### Maven Goals for CI/CD
```bash
# Complete test cycle with coverage
mvn clean compile test jacoco:report jacoco:check

# Integration test cycle
mvn clean verify
```

### Quality Gates
- All tests must pass
- Minimum 20% line coverage required
- No compilation errors
- No test failures

## Troubleshooting

### Common Issues
1. **Java Version**: Ensure Java 17+ is installed
2. **Maven Dependencies**: Run `mvn clean install` to resolve dependencies
3. **Test Failures**: Check console output for specific error messages
4. **Coverage Issues**: Verify JaCoCo plugin configuration in pom.xml

### Debug Mode
```bash
mvn test -X -Dtest=PriceCalculatorTest
```

## Bank of America Standards Compliance
- ✅ JDK 17 compatibility
- ✅ Google Java Style Guidelines
- ✅ Comprehensive test coverage
- ✅ Exception handling validation
- ✅ Performance testing
- ✅ Documentation standards
- ✅ Maven build integration