import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;
import org.junit.platform.suite.api.SuiteDisplayName;

/**
 * Comprehensive test suite for all Spring Boot Reactive Service tests.
 * Aggregates all test classes for complete testing coverage.
 * Designed for Bank of America code quality and testing standards.
 */
@Suite
@SuiteDisplayName("Bank of America - Spring Boot Reactive Service Test Suite")
@SelectClasses({
    // Core functionality tests
    ReactiveServiceApplicationTest.class,
    ReactiveServiceApplicationIntegrationTest.class,
    
    // Reactive-specific tests
    ReactiveServiceApplicationReactiveTest.class,
    
    // Application lifecycle tests
    ReactiveServiceApplicationLifecycleTest.class,
    
    // Performance and load tests
    ReactiveServiceApplicationPerformanceTest.class,
    
    // Original price calculator tests (for complete coverage)
    PriceCalculatorTest.class,
    PriceCalculatorIntegrationTest.class,
    PriceCalculatorPerformanceTest.class,
    PriceCalculatorTestSuite.class
})
class ComprehensiveTestSuite {
    // This class remains empty, it is used only as a holder for the above annotations
}