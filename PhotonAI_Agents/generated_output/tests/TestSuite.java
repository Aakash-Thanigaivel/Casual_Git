package daggerok;

import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;
import org.junit.platform.suite.api.SuiteDisplayName;

/**
 * Test suite for Bank of America Code Modernization Project.
 * 
 * <p>This test suite runs all unit tests for the converted JDK 17 code,
 * ensuring comprehensive coverage of both utility classes and Spring Boot
 * reactive applications.
 * 
 * @author Bank of America Code Modernization Team
 * @version 1.0 (JDK 17)
 */
@Suite
@SuiteDisplayName("Bank of America JDK 17 Modernization Test Suite")
@SelectClasses({
    PriceCalculatorTest.class,
    ReactiveServiceApplicationTest.class
})
class TestSuite {
    // Test suite configuration class - no implementation needed
}