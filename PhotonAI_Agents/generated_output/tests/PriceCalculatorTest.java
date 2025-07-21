package daggerok;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.junit.jupiter.params.provider.CsvSource;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for PriceCalculator class.
 * Tests cover discount calculations, tax calculations, and edge cases.
 */
@DisplayName("Price Calculator Tests")
class PriceCalculatorTest {

    @Test
    @DisplayName("Should calculate discounted price correctly with valid inputs")
    void testCalculateDiscountedPriceValidInputs() {
        // Given
        double price = 100.0;
        double discountPercentage = 20.0;
        
        // When
        double result = PriceCalculator.calculateDiscountedPrice(price, discountPercentage);
        
        // Then
        assertEquals(80.0, result, 0.01, "Price after 20% discount should be 80.0");
    }

    @ParameterizedTest
    @CsvSource({
        "100.0, 0.0, 100.0",
        "100.0, 50.0, 50.0", 
        "200.0, 25.0, 150.0",
        "150.0, 100.0, 0.0"
    })
    @DisplayName("Should calculate discounted price for various discount percentages")
    void testCalculateDiscountedPriceParameterized(double price, double discount, double expected) {
        double result = PriceCalculator.calculateDiscountedPrice(price, discount);
        assertEquals(expected, result, 0.01);
    }

    @Test
    @DisplayName("Should throw exception for negative discount percentage")
    void testCalculateDiscountedPriceNegativeDiscount() {
        // Given
        double price = 100.0;
        double discountPercentage = -10.0;
        
        // When & Then
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> PriceCalculator.calculateDiscountedPrice(price, discountPercentage)
        );
        assertEquals("Discount percentage must be between 0 and 100.", exception.getMessage());
    }

    @Test
    @DisplayName("Should throw exception for discount percentage over 100")
    void testCalculateDiscountedPriceOverHundredPercent() {
        // Given
        double price = 100.0;
        double discountPercentage = 150.0;
        
        // When & Then
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> PriceCalculator.calculateDiscountedPrice(price, discountPercentage)
        );
        assertEquals("Discount percentage must be between 0 and 100.", exception.getMessage());
    }

    @Test
    @DisplayName("Should calculate final price with tax correctly")
    void testCalculateFinalPriceWithTaxValidInputs() {
        // Given
        double price = 100.0;
        double taxRate = 0.05; // 5%
        
        // When
        double result = PriceCalculator.calculateFinalPriceWithTax(price, taxRate);
        
        // Then
        assertEquals(105.0, result, 0.01, "Price with 5% tax should be 105.0");
    }

    @ParameterizedTest
    @ValueSource(doubles = {0.0, 0.05, 0.10, 0.15, 0.20})
    @DisplayName("Should calculate tax for various tax rates")
    void testCalculateFinalPriceWithTaxParameterized(double taxRate) {
        double price = 100.0;
        double expected = price + (price * taxRate);
        
        double result = PriceCalculator.calculateFinalPriceWithTax(price, taxRate);
        
        assertEquals(expected, result, 0.01);
    }

    @Test
    @DisplayName("Should throw exception for negative tax rate")
    void testCalculateFinalPriceWithTaxNegativeRate() {
        // Given
        double price = 100.0;
        double taxRate = -0.05;
        
        // When & Then
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> PriceCalculator.calculateFinalPriceWithTax(price, taxRate)
        );
        assertEquals("Tax rate cannot be negative.", exception.getMessage());
    }
}