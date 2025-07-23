package daggerok;

import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;
import org.junit.platform.suite.api.SuiteDisplayName;

/**
 * Test Suite for Bank of America Legacy Code Modernization Project
 * Runs all unit and integration tests for the converted JDK 17 code
 */
@Suite
@SuiteDisplayName("Bank of America Legacy Code Modernization Test Suite")
@SelectClasses({
    PriceCalculatorTest.class,
    ReactiveServiceApplicationTest.class
})
public class TestSuite {
    // Test suite configuration class
    // All tests are configured via annotations
}