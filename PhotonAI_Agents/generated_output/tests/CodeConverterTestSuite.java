import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

/**
 * Test suite for all code converter output tests.
 * 
 * <p>This test suite runs all unit tests for the converted Java files
 * providing comprehensive coverage for the code conversion output.
 */
@Suite
@SelectClasses({
    PriceCalculatorTest.class,
    ReactiveServiceApplicationTest.class
})
public class CodeConverterTestSuite {
    // Test suite configuration - no additional code needed
}