# Test Execution Script for Modernized Application

#!/bin/bash

# Script to run all tests with code coverage for the modernized application
# This script demonstrates the test execution process for CI/CD integration

echo "=========================================="
echo "Running Tests for Modernized Application"
echo "=========================================="

# Set Java version to 17
export JAVA_HOME=/usr/lib/jvm/java-17-openjdk
export PATH=$JAVA_HOME/bin:$PATH

echo "Java Version:"
java -version

# Clean previous build artifacts
echo "Cleaning previous build..."
mvn clean

# Run all unit tests
echo "Running unit tests..."
mvn test

# Generate code coverage report
echo "Generating code coverage report..."
mvn jacoco:report

# Run specific test suites
echo "Running specific test suites..."

# Core application tests
mvn test -Dtest=ModernizedApplicationTest

# Controller tests
mvn test -Dtest=SampleControllerTest

# Service layer tests
mvn test -Dtest=ModernServiceTest,ModernServiceExtendedTest,UserServiceTest

# Configuration tests
mvn test -Dtest=JBossConfigurationTest,ApplicationPropertiesTest

# Run tests with specific Spring profile
echo "Running tests with JBoss profile..."
mvn test -Dspring.profiles.active=jboss

# Check code coverage threshold
echo "Checking code coverage threshold (20%)..."
mvn jacoco:check

# Generate test report summary
echo "=========================================="
echo "Test Execution Summary"
echo "=========================================="

# Display coverage summary
if [ -f target/site/jacoco/index.html ]; then
    echo "Code coverage report generated at: target/site/jacoco/index.html"
    # Extract coverage percentage (simplified)
    grep -o '[0-9]*%' target/site/jacoco/index.html | head -1
fi

# Display test results
if [ -f target/surefire-reports/TEST-*.xml ]; then
    echo "Test results available in: target/surefire-reports/"
    # Count total tests, failures, and errors
    grep -h testcase target/surefire-reports/TEST-*.xml | wc -l | xargs echo "Total tests:"
    grep -h failure target/surefire-reports/TEST-*.xml | wc -l | xargs echo "Failures:"
    grep -h error target/surefire-reports/TEST-*.xml | wc -l | xargs echo "Errors:"
fi

echo "=========================================="
echo "Test execution completed!"
echo "==========================================" 