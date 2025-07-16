import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Additional unit tests for PriceCalculator to ensure comprehensive coverage
 * Focuses on edge cases and boundary conditions
 */
public class PriceCalculatorAdditionalTest {

    @Test
    @DisplayName("Should handle very small discount percentages")
    void testVerySmallDiscountPercentages() {
        // Test with 0.01% discount
        double result = PriceCalculator.calculateDiscountedPrice(1000.0, 0.01);
        assertEquals(999.9, result, 0.01);
        
        // Test with 0.1% discount
        result = PriceCalculator.calculateDiscountedPrice(500.0, 0.1);
        assertEquals(499.5, result, 0.01);
    }

    @Test
    @DisplayName("Should handle boundary values for discount percentage")
    void testBoundaryDiscountValues() {
        // Test exactly 0%
        assertEquals(100.0, PriceCalculator.calculateDiscountedPrice(100.0, 0.0), 0.01);
        
        // Test exactly 100%
        assertEquals(0.0, PriceCalculator.calculateDiscountedPrice(100.0, 100.0), 0.01);
        
        // Test just below boundary
        assertEquals(0.01, PriceCalculator.calculateDiscountedPrice(100.0, 99.99), 0.01);
    }

    @Test
    @DisplayName("Should handle very small tax rates")
    void testVerySmallTaxRates() {
        // Test with 0.001 tax rate (0.1%)
        double result = PriceCalculator.calculateFinalPriceWithTax(1000.0, 0.001);
        assertEquals(1001.0, result, 0.01);
        
        // Test with 0.0001 tax rate (0.01%)
        result = PriceCalculator.calculateFinalPriceWithTax(500.0, 0.0001);
        assertEquals(500.05, result, 0.01);
    }

    @Test
    @DisplayName("Should handle zero price edge case")
    void testZeroPrice() {
        // Test discount calculation with zero price
        assertEquals(0.0, PriceCalculator.calculateDiscountedPrice(0.0, 50.0), 0.01);
        
        // Test tax calculation with zero price
        assertEquals(0.0, PriceCalculator.calculateFinalPriceWithTax(0.0, 0.1), 0.01);
    }

    @Test
    @DisplayName("Should maintain precision with large numbers")
    void testLargeNumberPrecision() {
        double largePrice = 999999999.99;
        double discountedPrice = PriceCalculator.calculateDiscountedPrice(largePrice, 1.0);
        assertEquals(989999999.99, discountedPrice, 0.01);
        
        double finalPrice = PriceCalculator.calculateFinalPriceWithTax(discountedPrice, 0.01);
        assertEquals(999899999.99, finalPrice, 0.01);
    }
}