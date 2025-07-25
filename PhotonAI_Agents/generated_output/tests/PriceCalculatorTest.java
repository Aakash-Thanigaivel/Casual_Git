import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test cases for PriceCalculator utility class.
 * 
 * <p>This test class provides comprehensive unit tests for the PriceCalculator
 * class methods with 5% code coverage focusing on critical functionality.
 */
@DisplayName("PriceCalculator Tests")
class PriceCalculatorTest {

    @Nested
    @DisplayName("Discount Calculation Tests")
    class DiscountCalculationTests {

        @Test
        @DisplayName("Should calculate correct discounted price for valid discount percentage")
        void shouldCalculateCorrectDiscountedPrice() {
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
        @DisplayName("Should throw IllegalArgumentException for negative discount percentage")
        void shouldThrowExceptionForNegativeDiscount() {
            // Given
            double originalPrice = 100.0;
            double invalidDiscountPercentage = -5.0;

            // When & Then
            IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> PriceCalculator.calculateDiscountedPrice(originalPrice, invalidDiscountPercentage)
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
            double basePrice = 100.0;
            double taxRate = 0.05; // 5%
            double expectedFinalPrice = 105.0;

            // When
            double actualFinalPrice = PriceCalculator.calculateFinalPriceWithTax(basePrice, taxRate);

            // Then
            assertEquals(expectedFinalPrice, actualFinalPrice, 0.01);
        }

        @Test
        @DisplayName("Should throw IllegalArgumentException for negative tax rate")
        void shouldThrowExceptionForNegativeTaxRate() {
            // Given
            double basePrice = 100.0;
            double invalidTaxRate = -0.05;

            // When & Then
            IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> PriceCalculator.calculateFinalPriceWithTax(basePrice, invalidTaxRate)
            );
            assertEquals("Tax rate cannot be negative.", exception.getMessage());
        }
    }

    @Nested
    @DisplayName("Complete Price Calculation Tests")
    class CompletePriceCalculationTests {

        @Test
        @DisplayName("Should calculate complete price breakdown correctly")
        void shouldCalculateCompletePriceBreakdown() {
            // Given
            double originalPrice = 100.0;
            double discountPercentage = 10.0;
            double taxRate = 0.08; // 8%

            // When
            PriceCalculator.PriceCalculationResult result = 
                PriceCalculator.calculateCompletePrice(originalPrice, discountPercentage, taxRate);

            // Then
            assertNotNull(result);
            assertEquals(originalPrice, result.originalPrice(), 0.01);
            assertEquals(discountPercentage, result.discountPercentage(), 0.01);
            assertEquals(90.0, result.discountedPrice(), 0.01); // 100 - 10%
            assertEquals(taxRate, result.taxRate(), 0.01);
            assertEquals(97.2, result.finalPrice(), 0.01); // 90 + 8% tax
        }
    }
}