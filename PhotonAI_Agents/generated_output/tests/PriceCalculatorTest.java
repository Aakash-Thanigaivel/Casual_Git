import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for PriceCalculator class.
 * Tests cover basic functionality and edge cases for price calculation methods.
 */
@DisplayName("PriceCalculator Tests")
class PriceCalculatorTest {

    @Nested
    @DisplayName("Calculate Discounted Price Tests")
    class CalculateDiscountedPriceTests {

        @Test
        @DisplayName("Should calculate correct discounted price for valid inputs")
        void shouldCalculateCorrectDiscountedPrice() {
            // Given
            double originalPrice = 100.0;
            double discountPercentage = 20.0;
            double expectedPrice = 80.0;

            // When
            double actualPrice = PriceCalculator.calculateDiscountedPrice(originalPrice, discountPercentage);

            // Then
            assertEquals(expectedPrice, actualPrice, 0.01);
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
            double invalidDiscount = 150.0;

            // When & Then
            IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> PriceCalculator.calculateDiscountedPrice(originalPrice, invalidDiscount)
            );
            assertEquals("Discount percentage must be between 0 and 100.", exception.getMessage());
        }
    }

    @Nested
    @DisplayName("Calculate Final Price With Tax Tests")
    class CalculateFinalPriceWithTaxTests {

        @Test
        @DisplayName("Should calculate correct final price with tax")
        void shouldCalculateCorrectFinalPriceWithTax() {
            // Given
            double basePrice = 100.0;
            double taxRate = 0.05; // 5%
            double expectedPrice = 105.0;

            // When
            double actualPrice = PriceCalculator.calculateFinalPriceWithTax(basePrice, taxRate);

            // Then
            assertEquals(expectedPrice, actualPrice, 0.01);
        }

        @Test
        @DisplayName("Should throw exception for negative tax rate")
        void shouldThrowExceptionForNegativeTaxRate() {
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
}