package daggerok;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for PriceCalculator utility class.
 * 
 * <p>This test class provides comprehensive test coverage for price calculation
 * functionality including discount calculations and tax applications.
 * 
 * <p>Following Google Java Style Guidelines and JUnit 5 best practices.
 */
class PriceCalculatorTest {

    @Test
    @DisplayName("Should calculate discounted price correctly with valid inputs")
    void testCalculateDiscountedPriceValidInputs() {
        // Given
        double originalPrice = 100.0;
        double discountPercentage = 20.0;
        double expectedDiscountedPrice = 80.0;
        
        // When
        double actualDiscountedPrice = PriceCalculator.calculateDiscountedPrice(originalPrice, discountPercentage);
        
        // Then
        assertEquals(expectedDiscountedPrice, actualDiscountedPrice, 0.01);
    }

    @Test
    @DisplayName("Should throw IllegalArgumentException when discount percentage is negative")
    void testCalculateDiscountedPriceNegativeDiscount() {
        // Given
        double originalPrice = 100.0;
        double negativeDiscount = -5.0;
        
        // When & Then
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> PriceCalculator.calculateDiscountedPrice(originalPrice, negativeDiscount)
        );
        
        assertEquals("Discount percentage must be between 0 and 100.", exception.getMessage());
    }

    @ParameterizedTest
    @ValueSource(doubles = {101.0, 150.0, 200.0})
    @DisplayName("Should throw IllegalArgumentException when discount percentage exceeds 100")
    void testCalculateDiscountedPriceExcessiveDiscount(double excessiveDiscount) {
        // Given
        double originalPrice = 100.0;
        
        // When & Then
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> PriceCalculator.calculateDiscountedPrice(originalPrice, excessiveDiscount)
        );
        
        assertEquals("Discount percentage must be between 0 and 100.", exception.getMessage());
    }

    @Test
    @DisplayName("Should calculate final price with tax correctly")
    void testCalculateFinalPriceWithTaxValidInputs() {
        // Given
        double basePrice = 100.0;
        double taxRate = 0.05; // 5%
        double expectedFinalPrice = 105.0;
        
        // When
        double actualFinalPrice = PriceCalculator.calculateFinalPriceWithTax(basePrice, taxRate);
        
        // Then
        assertEquals(expectedFinalPrice, actualFinalPrice, 0.01);
    }

    @Test
    @DisplayName("Should throw IllegalArgumentException when tax rate is negative")
    void testCalculateFinalPriceWithTaxNegativeTaxRate() {
        // Given
        double basePrice = 100.0;
        double negativeTaxRate = -0.05;
        
        // When & Then
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> PriceCalculator.calculateFinalPriceWithTax(basePrice, negativeTaxRate)
        );
        
        assertEquals("Tax rate cannot be negative.", exception.getMessage());
    }
}