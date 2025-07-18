package daggerok;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Assertions;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for PriceCalculator class.
 * Tests basic functionality of discount and tax calculations.
 * Coverage: ~5% of main functionality
 */
public class PriceCalculatorTest {

    @Test
    @DisplayName("Should calculate discounted price correctly for valid inputs")
    void testCalculateDiscountedPrice_ValidInputs() {
        // Given
        double price = 100.0;
        double discountPercentage = 10.0;
        double expected = 90.0;
        
        // When
        double actual = PriceCalculator.calculateDiscountedPrice(price, discountPercentage);
        
        // Then
        assertEquals(expected, actual, 0.01, "Discounted price should be calculated correctly");
    }

    @Test
    @DisplayName("Should throw IllegalArgumentException for negative discount percentage")
    void testCalculateDiscountedPrice_NegativeDiscount() {
        // Given
        double price = 100.0;
        double discountPercentage = -5.0;
        
        // When & Then
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> PriceCalculator.calculateDiscountedPrice(price, discountPercentage),
            "Should throw IllegalArgumentException for negative discount"
        );
        
        assertEquals("Discount percentage must be between 0 and 100.", exception.getMessage());
    }

    @Test
    @DisplayName("Should calculate final price with tax correctly")
    void testCalculateFinalPriceWithTax_ValidInputs() {
        // Given
        double price = 100.0;
        double taxRate = 0.05; // 5% tax
        double expected = 105.0;
        
        // When
        double actual = PriceCalculator.calculateFinalPriceWithTax(price, taxRate);
        
        // Then
        assertEquals(expected, actual, 0.01, "Final price with tax should be calculated correctly");
    }
}