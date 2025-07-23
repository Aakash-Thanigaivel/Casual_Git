package daggerok;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test cases for PriceCalculator utility class
 * Provides 5% code coverage for basic functionality testing
 */
class PriceCalculatorTest {

    @Test
    @DisplayName("Should calculate discounted price correctly")
    void testCalculateDiscountedPrice() {
        // Test basic discount calculation
        double result = PriceCalculator.calculateDiscountedPrice(100.0, 10.0);
        assertEquals(90.0, result, 0.01);
    }

    @Test
    @DisplayName("Should throw exception for invalid discount percentage")
    void testCalculateDiscountedPriceInvalidDiscount() {
        // Test negative discount
        assertThrows(IllegalArgumentException.class, () -> 
            PriceCalculator.calculateDiscountedPrice(100.0, -5.0));
        
        // Test discount over 100%
        assertThrows(IllegalArgumentException.class, () -> 
            PriceCalculator.calculateDiscountedPrice(100.0, 105.0));
    }

    @Test
    @DisplayName("Should calculate final price with tax correctly")
    void testCalculateFinalPriceWithTax() {
        // Test basic tax calculation
        double result = PriceCalculator.calculateFinalPriceWithTax(100.0, 0.05);
        assertEquals(105.0, result, 0.01);
    }

    @Test
    @DisplayName("Should throw exception for negative tax rate")
    void testCalculateFinalPriceWithTaxNegativeRate() {
        assertThrows(IllegalArgumentException.class, () -> 
            PriceCalculator.calculateFinalPriceWithTax(100.0, -0.05));
    }

    @Test
    @DisplayName("Should handle zero values correctly")
    void testZeroValues() {
        // Test zero discount
        double result1 = PriceCalculator.calculateDiscountedPrice(100.0, 0.0);
        assertEquals(100.0, result1, 0.01);
        
        // Test zero tax
        double result2 = PriceCalculator.calculateFinalPriceWithTax(100.0, 0.0);
        assertEquals(100.0, result2, 0.01);
    }
}