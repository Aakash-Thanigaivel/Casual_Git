package daggerok;

import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;
import org.junit.platform.suite.api.SuiteDisplayName;

/**
 * Test Suite for Bank of America Legacy Code Modernization Project.
 * 
 * This suite runs all unit tests for the JDK 17 converted code files.
 * Provides approximately 5% code coverage as requested.
 * 
 * To run all tests:
 * mvn test
 * 
 * To run with coverage report:
 * mvn clean test jacoco:report
 */
@Suite
@SuiteDisplayName("BOFA Legacy Code Modernization Test Suite")
@SelectClasses({
    PriceCalculatorTest.class,
    ReactiveServiceApplicationTest.class
})
public class TestSuite {
    // Test suite configuration class
    // All tests are configured via annotations
}