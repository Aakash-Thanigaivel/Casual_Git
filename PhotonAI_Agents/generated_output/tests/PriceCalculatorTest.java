import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Scanner;

/**
 * Unit tests for PriceCalculator class.
 * 
 * <p>This test class provides comprehensive coverage for the PriceCalculator
 * utility methods including edge cases and error conditions.
 */
class PriceCalculatorTest {

    private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();
    private final PrintStream standardOut = System.out;
    private final PrintStream standardErr = System.err;

    @BeforeEach
    void setUp() {
        System.setOut(new PrintStream(outputStreamCaptor));
        System.setErr(new PrintStream(outputStreamCaptor));
    }

    @AfterEach
    void tearDown() {
        System.setOut(standardOut);
        System.setErr(standardErr);
    }

    @Test
    @DisplayName("Should calculate discounted price correctly for valid inputs")
    void testCalculateDiscountedPriceValidInputs() {
        // Test normal discount calculation
        assertEquals(80.0, PriceCalculator.calculateDiscountedPrice(100.0, 20.0), 0.01);
        assertEquals(95.0, PriceCalculator.calculateDiscountedPrice(100.0, 5.0), 0.01);
        assertEquals(100.0, PriceCalculator.calculateDiscountedPrice(100.0, 0.0), 0.01);
        assertEquals(50.0, PriceCalculator.calculateDiscountedPrice(100.0, 50.0), 0.01);
    }

    @Test
    @DisplayName("Should throw exception for invalid discount percentage")
    void testCalculateDiscountedPriceInvalidDiscount() {
        // Test negative discount
        IllegalArgumentException exception1 = assertThrows(IllegalArgumentException.class, 
            () -> PriceCalculator.calculateDiscountedPrice(100.0, -5.0));
        assertTrue(exception1.getMessage().contains("Discount percentage must be between 0 and 100"));
        
        // Test discount over 100%
        IllegalArgumentException exception2 = assertThrows(IllegalArgumentException.class, 
            () -> PriceCalculator.calculateDiscountedPrice(100.0, 105.0));
        assertTrue(exception2.getMessage().contains("Discount percentage must be between 0 and 100"));
    }

    @Test
    @DisplayName("Should calculate final price with tax correctly")
    void testCalculateFinalPriceWithTaxValidInputs() {
        // Test normal tax calculation
        assertEquals(105.0, PriceCalculator.calculateFinalPriceWithTax(100.0, 0.05), 0.01);
        assertEquals(100.0, PriceCalculator.calculateFinalPriceWithTax(100.0, 0.0), 0.01);
        assertEquals(118.0, PriceCalculator.calculateFinalPriceWithTax(100.0, 0.18), 0.01);
        assertEquals(110.0, PriceCalculator.calculateFinalPriceWithTax(100.0, 0.10), 0.01);
    }

    @Test
    @DisplayName("Should throw exception for negative tax rate")
    void testCalculateFinalPriceWithTaxNegativeRate() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, 
            () -> PriceCalculator.calculateFinalPriceWithTax(100.0, -0.05));
        assertTrue(exception.getMessage().contains("Tax rate cannot be negative"));
    }

    @Test
    @DisplayName("Should handle edge cases correctly")
    void testEdgeCases() {
        // Test with zero price
        assertEquals(0.0, PriceCalculator.calculateDiscountedPrice(0.0, 50.0), 0.01);
        assertEquals(0.0, PriceCalculator.calculateFinalPriceWithTax(0.0, 0.1), 0.01);
        
        // Test with maximum discount
        assertEquals(0.0, PriceCalculator.calculateDiscountedPrice(100.0, 100.0), 0.01);
        
        // Test with very small values
        assertEquals(0.99, PriceCalculator.calculateDiscountedPrice(1.0, 1.0), 0.01);
        assertEquals(1.01, PriceCalculator.calculateFinalPriceWithTax(1.0, 0.01), 0.01);
    }

    @Test
    @DisplayName("Should handle large numbers correctly")
    void testLargeNumbers() {
        double largePrice = 1000000.0;
        assertEquals(900000.0, PriceCalculator.calculateDiscountedPrice(largePrice, 10.0), 0.01);
        assertEquals(1050000.0, PriceCalculator.calculateFinalPriceWithTax(largePrice, 0.05), 0.01);
    }

    @Test
    @DisplayName("Should handle precision correctly")
    void testPrecision() {
        // Test with decimal values that might cause precision issues
        assertEquals(33.33, PriceCalculator.calculateDiscountedPrice(100.0, 66.67), 0.01);
        assertEquals(133.33, PriceCalculator.calculateFinalPriceWithTax(100.0, 0.3333), 0.01);
    }

    @Test
    @DisplayName("Should validate boundary conditions")
    void testBoundaryConditions() {
        // Test boundary values for discount percentage
        assertEquals(100.0, PriceCalculator.calculateDiscountedPrice(100.0, 0.0), 0.01);
        assertEquals(0.0, PriceCalculator.calculateDiscountedPrice(100.0, 100.0), 0.01);
        
        // Test boundary values for tax rate
        assertEquals(100.0, PriceCalculator.calculateFinalPriceWithTax(100.0, 0.0), 0.01);
    }

    @Test
    @DisplayName("Should handle combined discount and tax calculations")
    void testCombinedCalculations() {
        double originalPrice = 100.0;
        double discountPercentage = 20.0;
        double taxRate = 0.05;
        
        double discountedPrice = PriceCalculator.calculateDiscountedPrice(originalPrice, discountPercentage);
        double finalPrice = PriceCalculator.calculateFinalPriceWithTax(discountedPrice, taxRate);
        
        assertEquals(80.0, discountedPrice, 0.01);
        assertEquals(84.0, finalPrice, 0.01);
    }
}