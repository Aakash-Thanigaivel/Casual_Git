package com.example.modernized;

import org.junit.platform.suite.api.SelectPackages;
import org.junit.platform.suite.api.Suite;
import org.junit.platform.suite.api.SuiteDisplayName;

/**
 * Test suite runner for all modernized application tests.
 * Aggregates all test classes for comprehensive test execution.
 */
@Suite
@SuiteDisplayName("Modernized Application Test Suite")
@SelectPackages({
    "com.example.modernized",
    "com.example.modernized.controller",
    "com.example.modernized.service",
    "com.example.modernized.config"
})
public class ModernizedApplicationTestSuite {
    // This class serves as a test suite runner
    // No implementation needed - JUnit 5 will discover and run all tests
    // in the specified packages
}