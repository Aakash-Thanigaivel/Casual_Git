import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

/**
 * Test suite runner for all PriceCalculator tests.
 * Aggregates all test classes for comprehensive testing coverage.
 * Designed for Bank of America code quality standards.
 */
@Suite
@SelectClasses({
    PriceCalculatorTest.class,
    PriceCalculatorIntegrationTest.class,
    PriceCalculatorPerformanceTest.class
})
class PriceCalculatorTestSuite {
    // This class remains empty, it is used only as a holder for the above annotations
}