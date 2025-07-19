package daggerok;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.junit.jupiter.params.provider.CsvSource;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for PriceCalculator class.
 * 
 * <p>This test class provides comprehensive coverage for the PriceCalculator
 * utility methods including discount calculations, tax calculations, and
 * edge case validation.
 * 
 * @author Bank of America Code Modernization Team
 * @version 1.0 (JDK 17)
 */
class PriceCalculatorTest {

    @Nested
    @DisplayName("Discount Calculation Tests")
    class DiscountCalculationTests {

        @Test
        @DisplayName("Should calculate correct discounted price for valid inputs")
        void shouldCalculateCorrectDiscountedPrice() {
            // Given
            double originalPrice = 100.0;
            double discountPercentage = 20.0;
            
            // When
            double result = PriceCalculator.calculateDiscountedPrice(originalPrice, discountPercentage);
            
            // Then
            assertEquals(80.0, result, 0.01, "Discounted price should be 80.0");
        }

        @ParameterizedTest
        @CsvSource({
            "100.0, 0.0, 100.0",
            "100.0, 50.0, 50.0", 
            "100.0, 100.0, 0.0",
            "200.0, 25.0, 150.0"
        })
        @DisplayName("Should calculate discount for various valid percentage values")
        void shouldCalculateDiscountForValidPercentages(double price, double discount, double expected) {
            double result = PriceCalculator.calculateDiscountedPrice(price, discount);
            assertEquals(expected, result, 0.01);
        }

        @ParameterizedTest
        @ValueSource(doubles = {-1.0, -10.0, 101.0, 150.0})
        @DisplayName("Should throw exception for invalid discount percentages")
        void shouldThrowExceptionForInvalidDiscountPercentages(double invalidDiscount) {
            IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> PriceCalculator.calculateDiscountedPrice(100.0, invalidDiscount)
            );
            assertTrue(exception.getMessage().contains("Discount percentage must be between"));
        }
    }

    @Nested
    @DisplayName("Tax Calculation Tests")
    class TaxCalculationTests {

        @Test
        @DisplayName("Should calculate correct final price with tax")
        void shouldCalculateCorrectFinalPriceWithTax() {
            // Given
            double basePrice = 100.0;
            double taxRate = 0.05; // 5%
            
            // When
            double result = PriceCalculator.calculateFinalPriceWithTax(basePrice, taxRate);
            
            // Then
            assertEquals(105.0, result, 0.01, "Final price with 5% tax should be 105.0");
        }

        @ParameterizedTest
        @CsvSource({
            "100.0, 0.0, 100.0",
            "100.0, 0.10, 110.0",
            "200.0, 0.08, 216.0",
            "50.0, 0.15, 57.5"
        })
        @DisplayName("Should calculate tax for various valid tax rates")
        void shouldCalculateTaxForValidRates(double price, double taxRate, double expected) {
            double result = PriceCalculator.calculateFinalPriceWithTax(price, taxRate);
            assertEquals(expected, result, 0.01);
        }

        @ParameterizedTest
        @ValueSource(doubles = {-0.01, -0.1, -1.0})
        @DisplayName("Should throw exception for negative tax rates")
        void shouldThrowExceptionForNegativeTaxRates(double negativeTaxRate) {
            IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> PriceCalculator.calculateFinalPriceWithTax(100.0, negativeTaxRate)
            );
            assertEquals("Tax rate cannot be negative.", exception.getMessage());
        }
    }

    @Nested
    @DisplayName("Constructor Tests")
    class ConstructorTests {

        @Test
        @DisplayName("Should throw exception when trying to instantiate utility class")
        void shouldThrowExceptionWhenInstantiating() {
            assertThrows(
                UnsupportedOperationException.class,
                () -> {
                    var constructor = PriceCalculator.class.getDeclaredConstructor();
                    constructor.setAccessible(true);
                    constructor.newInstance();
                },
                "Should not be able to instantiate utility class"
            );
        }
    }

    @Nested
    @DisplayName("Edge Case Tests")
    class EdgeCaseTests {

        @Test
        @DisplayName("Should handle zero price correctly")
        void shouldHandleZeroPriceCorrectly() {
            double result = PriceCalculator.calculateDiscountedPrice(0.0, 50.0);
            assertEquals(0.0, result, 0.01);
            
            double taxResult = PriceCalculator.calculateFinalPriceWithTax(0.0, 0.1);
            assertEquals(0.0, taxResult, 0.01);
        }

        @Test
        @DisplayName("Should handle very large numbers correctly")
        void shouldHandleLargeNumbersCorrectly() {
            double largePrice = 1_000_000.0;
            double result = PriceCalculator.calculateDiscountedPrice(largePrice, 10.0);
            assertEquals(900_000.0, result, 0.01);
        }
    }
}