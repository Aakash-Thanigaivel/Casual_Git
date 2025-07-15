import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for PriceCalculator class
 * Modernized for JDK 17 with comprehensive test coverage
 * Bank of America Legacy Code Modernization Project
 */
@DisplayName("PriceCalculator Unit Tests")
class PriceCalculatorTest {

    @Nested
    @DisplayName("Discount Calculation Tests")
    class DiscountCalculationTests {

        @Test
        @DisplayName("Should calculate correct discounted price for valid inputs")
        void shouldCalculateCorrectDiscountedPrice() {
            // Given
            var originalPrice = 100.0;
            var discountPercentage = 20.0;
            var expectedDiscountedPrice = 80.0;

            // When
            var actualDiscountedPrice = PriceCalculator.calculateDiscountedPrice(originalPrice, discountPercentage);

            // Then
            assertEquals(expectedDiscountedPrice, actualDiscountedPrice, 0.01);
        }

        @Test
        @DisplayName("Should handle zero discount percentage")
        void shouldHandleZeroDiscount() {
            // Given
            var originalPrice = 100.0;
            var discountPercentage = 0.0;

            // When
            var actualDiscountedPrice = PriceCalculator.calculateDiscountedPrice(originalPrice, discountPercentage);

            // Then
            assertEquals(originalPrice, actualDiscountedPrice, 0.01);
        }

        @Test
        @DisplayName("Should handle maximum discount percentage")
        void shouldHandleMaximumDiscount() {
            // Given
            var originalPrice = 100.0;
            var discountPercentage = 100.0;
            var expectedDiscountedPrice = 0.0;

            // When
            var actualDiscountedPrice = PriceCalculator.calculateDiscountedPrice(originalPrice, discountPercentage);

            // Then
            assertEquals(expectedDiscountedPrice, actualDiscountedPrice, 0.01);
        }

        @Test
        @DisplayName("Should throw exception for negative discount percentage")
        void shouldThrowExceptionForNegativeDiscount() {
            // Given
            var originalPrice = 100.0;
            var negativeDiscountPercentage = -10.0;

            // When & Then
            var exception = assertThrows(IllegalArgumentException.class, 
                () -> PriceCalculator.calculateDiscountedPrice(originalPrice, negativeDiscountPercentage));
            
            assertEquals("Discount percentage must be between 0 and 100.", exception.getMessage());
        }

        @Test
        @DisplayName("Should throw exception for discount percentage over 100")
        void shouldThrowExceptionForDiscountOver100() {
            // Given
            var originalPrice = 100.0;
            var excessiveDiscountPercentage = 150.0;

            // When & Then
            var exception = assertThrows(IllegalArgumentException.class, 
                () -> PriceCalculator.calculateDiscountedPrice(originalPrice, excessiveDiscountPercentage));
            
            assertEquals("Discount percentage must be between 0 and 100.", exception.getMessage());
        }

        @Test
        @DisplayName("Should handle decimal discount percentages")
        void shouldHandleDecimalDiscountPercentages() {
            // Given
            var originalPrice = 100.0;
            var discountPercentage = 12.5;
            var expectedDiscountedPrice = 87.5;

            // When
            var actualDiscountedPrice = PriceCalculator.calculateDiscountedPrice(originalPrice, discountPercentage);

            // Then
            assertEquals(expectedDiscountedPrice, actualDiscountedPrice, 0.01);
        }
    }

    @Nested
    @DisplayName("Tax Calculation Tests")
    class TaxCalculationTests {

        @Test
        @DisplayName("Should calculate correct final price with tax")
        void shouldCalculateCorrectFinalPriceWithTax() {
            // Given
            var price = 100.0;
            var taxRate = 0.05; // 5%
            var expectedFinalPrice = 105.0;

            // When
            var actualFinalPrice = PriceCalculator.calculateFinalPriceWithTax(price, taxRate);

            // Then
            assertEquals(expectedFinalPrice, actualFinalPrice, 0.01);
        }

        @Test
        @DisplayName("Should handle zero tax rate")
        void shouldHandleZeroTaxRate() {
            // Given
            var price = 100.0;
            var taxRate = 0.0;

            // When
            var actualFinalPrice = PriceCalculator.calculateFinalPriceWithTax(price, taxRate);

            // Then
            assertEquals(price, actualFinalPrice, 0.01);
        }

        @Test
        @DisplayName("Should handle high tax rate")
        void shouldHandleHighTaxRate() {
            // Given
            var price = 100.0;
            var taxRate = 0.25; // 25%
            var expectedFinalPrice = 125.0;

            // When
            var actualFinalPrice = PriceCalculator.calculateFinalPriceWithTax(price, taxRate);

            // Then
            assertEquals(expectedFinalPrice, actualFinalPrice, 0.01);
        }

        @Test
        @DisplayName("Should throw exception for negative tax rate")
        void shouldThrowExceptionForNegativeTaxRate() {
            // Given
            var price = 100.0;
            var negativeTaxRate = -0.05;

            // When & Then
            var exception = assertThrows(IllegalArgumentException.class, 
                () -> PriceCalculator.calculateFinalPriceWithTax(price, negativeTaxRate));
            
            assertEquals("Tax rate cannot be negative.", exception.getMessage());
        }

        @Test
        @DisplayName("Should handle fractional tax rates")
        void shouldHandleFractionalTaxRates() {
            // Given
            var price = 100.0;
            var taxRate = 0.075; // 7.5%
            var expectedFinalPrice = 107.5;

            // When
            var actualFinalPrice = PriceCalculator.calculateFinalPriceWithTax(price, taxRate);

            // Then
            assertEquals(expectedFinalPrice, actualFinalPrice, 0.01);
        }
    }

    @Nested
    @DisplayName("Integration Tests")
    class IntegrationTests {

        @Test
        @DisplayName("Should calculate complete price flow correctly")
        void shouldCalculateCompletePriceFlow() {
            // Given
            var originalPrice = 200.0;
            var discountPercentage = 10.0; // 10% discount
            var taxRate = 0.08; // 8% tax

            // When
            var discountedPrice = PriceCalculator.calculateDiscountedPrice(originalPrice, discountPercentage);
            var finalPrice = PriceCalculator.calculateFinalPriceWithTax(discountedPrice, taxRate);

            // Then
            assertEquals(180.0, discountedPrice, 0.01); // 200 - 20 = 180
            assertEquals(194.4, finalPrice, 0.01); // 180 + (180 * 0.08) = 194.4
        }

        @Test
        @DisplayName("Should handle edge case with very small amounts")
        void shouldHandleSmallAmounts() {
            // Given
            var originalPrice = 0.01;
            var discountPercentage = 50.0;
            var taxRate = 0.1;

            // When
            var discountedPrice = PriceCalculator.calculateDiscountedPrice(originalPrice, discountPercentage);
            var finalPrice = PriceCalculator.calculateFinalPriceWithTax(discountedPrice, taxRate);

            // Then
            assertEquals(0.005, discountedPrice, 0.001);
            assertEquals(0.0055, finalPrice, 0.0001);
        }

        @Test
        @DisplayName("Should handle large amounts correctly")
        void shouldHandleLargeAmounts() {
            // Given
            var originalPrice = 1000000.0;
            var discountPercentage = 15.0;
            var taxRate = 0.12;

            // When
            var discountedPrice = PriceCalculator.calculateDiscountedPrice(originalPrice, discountPercentage);
            var finalPrice = PriceCalculator.calculateFinalPriceWithTax(discountedPrice, taxRate);

            // Then
            assertEquals(850000.0, discountedPrice, 0.01);
            assertEquals(952000.0, finalPrice, 0.01);
        }
    }

    @Nested
    @DisplayName("Boundary Value Tests")
    class BoundaryValueTests {

        @Test
        @DisplayName("Should handle boundary discount values")
        void shouldHandleBoundaryDiscountValues() {
            // Test boundary values: 0, 1, 99, 100
            var price = 100.0;
            
            assertEquals(100.0, PriceCalculator.calculateDiscountedPrice(price, 0.0), 0.01);
            assertEquals(99.0, PriceCalculator.calculateDiscountedPrice(price, 1.0), 0.01);
            assertEquals(1.0, PriceCalculator.calculateDiscountedPrice(price, 99.0), 0.01);
            assertEquals(0.0, PriceCalculator.calculateDiscountedPrice(price, 100.0), 0.01);
        }

        @Test
        @DisplayName("Should handle boundary tax values")
        void shouldHandleBoundaryTaxValues() {
            // Test boundary values: 0, very small positive, large positive
            var price = 100.0;
            
            assertEquals(100.0, PriceCalculator.calculateFinalPriceWithTax(price, 0.0), 0.01);
            assertEquals(100.001, PriceCalculator.calculateFinalPriceWithTax(price, 0.00001), 0.001);
            assertEquals(200.0, PriceCalculator.calculateFinalPriceWithTax(price, 1.0), 0.01);
        }
    }
}