# Test Execution Guide
# Bank of America Legacy Code Modernization Project
# Spring 6.1 and JDK 17 Test Suite

## Overview
This document provides instructions for running the comprehensive test suite for the modernized Bank of America legacy codebase.

## Prerequisites
- JDK 17 or later
- Maven 3.8+
- Spring Boot 3.2+
- Internet connection for dependency downloads

## Test Structure
```
tests/
├── PriceCalculatorTest.java              # Unit tests for price calculation logic
├── SpringCodeTest.java                   # Spring framework component tests
├── ReactiveServiceApplicationTest.java   # Reactive web application tests
├── ReactiveServiceIntegrationTest.java   # End-to-end integration tests
├── ReactiveServicePerformanceTest.java   # Performance and load tests
├── LegacyModernizationIntegrationTest.java # Complete modernization validation
├── pom.xml                              # Maven configuration
└── application-test.properties          # Test configuration
```

## Running Tests

### 1. Unit Tests Only
```bash
mvn test -Dtest="*Test" -DfailIfNoTests=false
```

### 2. Integration Tests Only
```bash
mvn test -Dtest="*IntegrationTest" -DfailIfNoTests=false
```

### 3. Performance Tests Only
```bash
mvn test -Dtest="*PerformanceTest" -DfailIfNoTests=false
```

### 4. All Tests
```bash
mvn test
```

### 5. Tests with Coverage Report
```bash
mvn clean test jacoco:report
```

### 6. Specific Test Class
```bash
mvn test -Dtest=PriceCalculatorTest
mvn test -Dtest=ReactiveServiceApplicationTest
```

### 7. Specific Test Method
```bash
mvn test -Dtest=PriceCalculatorTest#shouldCalculateCorrectDiscountedPrice
```

## Test Categories

### Unit Tests (Fast - < 1 second each)
- **PriceCalculatorTest**: Tests business logic for price calculations
- **SpringCodeTest**: Tests Spring component configuration

### Integration Tests (Medium - 1-10 seconds each)
- **ReactiveServiceApplicationTest**: Tests reactive web endpoints
- **ReactiveServiceIntegrationTest**: Tests complete application integration
- **LegacyModernizationIntegrationTest**: Validates modernization features

### Performance Tests (Slow - 10+ seconds each)
- **ReactiveServicePerformanceTest**: Load testing and performance validation

## Coverage Requirements
- **Minimum Coverage**: 5% (configured in pom.xml)
- **Target Coverage**: 80%+ for production readiness
- **Coverage Report**: Generated in `target/site/jacoco/index.html`

## Test Configuration

### Environment Variables
```bash
export SPRING_PROFILES_ACTIVE=test
export SERVER_PORT=0  # Random port for testing
export LOG_LEVEL=INFO
```

### JVM Arguments
```bash
-Xmx2g -XX:+UseG1GC --enable-preview
```

## Continuous Integration

### Maven Surefire Configuration
- Parallel execution enabled
- JDK 17 preview features enabled
- Test timeout: 300 seconds per test
- Memory: 2GB heap space

### Test Execution Order
1. Unit tests (parallel execution)
2. Integration tests (sequential)
3. Performance tests (sequential, resource-intensive)

## Test Data and Mocking

### Test Database
- H2 in-memory database
- Auto-configured for each test
- Cleaned up automatically

### Mock Services
- Mockito for service layer mocking
- WebTestClient for reactive web testing
- TestContainers for external service simulation

## Troubleshooting

### Common Issues

#### 1. Port Conflicts
```bash
# Use random ports
mvn test -Dserver.port=0
```

#### 2. Memory Issues
```bash
# Increase heap size
export MAVEN_OPTS="-Xmx4g"
```

#### 3. Timeout Issues
```bash
# Increase test timeout
mvn test -Dtest.timeout=600
```

#### 4. Dependency Issues
```bash
# Clean and reinstall dependencies
mvn clean dependency:resolve
```

### Performance Test Considerations
- Performance tests may take 5-10 minutes to complete
- Requires adequate system resources (CPU, memory)
- May be affected by system load and network conditions
- Consider running on dedicated test environment

## Reporting

### Test Reports
- **Surefire Reports**: `target/surefire-reports/`
- **Coverage Reports**: `target/site/jacoco/`
- **Performance Metrics**: Console output during test execution

### CI/CD Integration
```yaml
# Example GitHub Actions configuration
- name: Run Tests
  run: mvn clean test jacoco:report
  
- name: Upload Coverage
  uses: codecov/codecov-action@v3
  with:
    file: target/site/jacoco/jacoco.xml
```

## Best Practices

### Test Execution
1. Run unit tests frequently during development
2. Run integration tests before commits
3. Run performance tests before releases
4. Monitor test execution time and optimize slow tests

### Test Maintenance
1. Keep tests independent and isolated
2. Use descriptive test names and documentation
3. Regularly review and update test data
4. Remove obsolete tests and add new ones for new features

### Performance Monitoring
1. Track test execution times
2. Monitor resource usage during tests
3. Set up alerts for test failures
4. Regular performance baseline updates

## Contact and Support
For questions about the test suite or issues with test execution, contact the Bank of America Legacy Modernization Team.