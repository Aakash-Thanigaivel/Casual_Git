import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.InputStream;

/**
 * Test cases for PriceCalculator class
 * Provides unit tests with 5% code coverage for JDK 17 modernized version
 */
class PriceCalculatorTest {

    private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;
    private final InputStream originalIn = System.in;

    @BeforeEach
    void setUp() {
        System.setOut(new PrintStream(outputStreamCaptor));
    }

    @Test
    @DisplayName("Test calculateDiscountedPrice with valid discount percentage")
    void testCalculateDiscountedPriceValidDiscount() {
        // Test basic discount calculation
        var result = PriceCalculator.calculateDiscountedPrice(100.0, 10.0);
        assertEquals(90.0, result, 0.01, "10% discount on 100 should be 90");
        
        // Test zero discount
        var resultZero = PriceCalculator.calculateDiscountedPrice(100.0, 0.0);
        assertEquals(100.0, resultZero, 0.01, "0% discount should return original price");
    }

    @Test
    @DisplayName("Test calculateDiscountedPrice with invalid discount percentage")
    void testCalculateDiscountedPriceInvalidDiscount() {
        // Test negative discount
        var exception1 = assertThrows(IllegalArgumentException.class, 
            () -> PriceCalculator.calculateDiscountedPrice(100.0, -5.0));
        assertEquals("Discount percentage must be between 0 and 100.", exception1.getMessage());
        
        // Test discount over 100%
        var exception2 = assertThrows(IllegalArgumentException.class, 
            () -> PriceCalculator.calculateDiscountedPrice(100.0, 105.0));
        assertEquals("Discount percentage must be between 0 and 100.", exception2.getMessage());
    }

    @Test
    @DisplayName("Test calculateFinalPriceWithTax with valid tax rate")
    void testCalculateFinalPriceWithTaxValidRate() {
        // Test basic tax calculation
        var result = PriceCalculator.calculateFinalPriceWithTax(100.0, 0.05);
        assertEquals(105.0, result, 0.01, "5% tax on 100 should be 105");
        
        // Test zero tax
        var resultZero = PriceCalculator.calculateFinalPriceWithTax(100.0, 0.0);
        assertEquals(100.0, resultZero, 0.01, "0% tax should return original price");
    }

    @Test
    @DisplayName("Test calculateFinalPriceWithTax with invalid tax rate")
    void testCalculateFinalPriceWithTaxInvalidRate() {
        // Test negative tax rate
        var exception = assertThrows(IllegalArgumentException.class, 
            () -> PriceCalculator.calculateFinalPriceWithTax(100.0, -0.05));
        assertEquals("Tax rate cannot be negative.", exception.getMessage());
    }

    @Test
    @DisplayName("Test main method with valid input")
    void testMainMethodValidInput() {
        // Simulate user input: price=100, discount=10, tax=0.05
        var input = "100.0\n10.0\n0.05\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        
        // This test verifies the main method runs without exceptions
        assertDoesNotThrow(() -> PriceCalculator.main(new String[]{}));
        
        // Verify output contains expected calculations
        var output = outputStreamCaptor.toString();
        assertTrue(output.contains("Enter the original price:"));
        assertTrue(output.contains("Enter the discount percentage:"));
        assertTrue(output.contains("Enter the tax rate"));
    }

    @Test
    @DisplayName("Test main method with invalid input handling")
    void testMainMethodInvalidInput() {
        // Simulate invalid input
        var input = "invalid\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        
        // This should handle the exception gracefully
        assertDoesNotThrow(() -> PriceCalculator.main(new String[]{}));
        
        var output = outputStreamCaptor.toString();
        assertTrue(output.contains("Error:"));
    }

    @Test
    @DisplayName("Test edge case calculations")
    void testEdgeCaseCalculations() {
        // Test maximum discount
        var maxDiscount = PriceCalculator.calculateDiscountedPrice(100.0, 100.0);
        assertEquals(0.0, maxDiscount, 0.01, "100% discount should result in 0");
        
        // Test high tax rate
        var highTax = PriceCalculator.calculateFinalPriceWithTax(100.0, 1.0);
        assertEquals(200.0, highTax, 0.01, "100% tax should double the price");
    }

    void tearDown() {
        System.setOut(originalOut);
        System.setIn(originalIn);
    }
}