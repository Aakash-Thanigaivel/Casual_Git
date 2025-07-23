package daggerok;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for PriceCalculator class
 * Tests cover discount calculations, tax calculations, and edge cases
 */
public class PriceCalculatorTest {

    @Nested
    @DisplayName("Discount Calculation Tests")
    class DiscountCalculationTests {

        @Test
        @DisplayName("Should calculate correct discounted price for valid inputs")
        void shouldCalculateCorrectDiscountedPrice() {
            // Given
            double originalPrice = 100.0;
            double discountPercentage = 20.0;
            double expected = 80.0;

            // When
            double result = PriceCalculator.calculateDiscountedPrice(originalPrice, discountPercentage);

            // Then
            assertEquals(expected, result, 0.01);
        }

        @Test
        @DisplayName("Should throw exception for negative discount percentage")
        void shouldThrowExceptionForNegativeDiscount() {
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

        @Test
        @DisplayName("Should throw exception for discount percentage over 100")
        void shouldThrowExceptionForDiscountOver100() {
            // Given
            double originalPrice = 100.0;
            double excessiveDiscount = 150.0;

            // When & Then
            IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> PriceCalculator.calculateDiscountedPrice(originalPrice, excessiveDiscount)
            );
            assertEquals("Discount percentage must be between 0 and 100.", exception.getMessage());
        }
    }

    @Nested
    @DisplayName("Tax Calculation Tests")
    class TaxCalculationTests {

        @Test
        @DisplayName("Should calculate correct final price with tax")
        void shouldCalculateCorrectFinalPriceWithTax() {
            // Given
            double price = 100.0;
            double taxRate = 0.05; // 5%
            double expected = 105.0;

            // When
            double result = PriceCalculator.calculateFinalPriceWithTax(price, taxRate);

            // Then
            assertEquals(expected, result, 0.01);
        }

        @Test
        @DisplayName("Should throw exception for negative tax rate")
        void shouldThrowExceptionForNegativeTaxRate() {
            // Given
            double price = 100.0;
            double negativeTaxRate = -0.05;

            // When & Then
            IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> PriceCalculator.calculateFinalPriceWithTax(price, negativeTaxRate)
            );
            assertEquals("Tax rate cannot be negative.", exception.getMessage());
        }
    }
}