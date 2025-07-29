import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for PriceCalculator class.
 * 
 * <p>This test class provides comprehensive test coverage for the PriceCalculator
 * utility class, testing both normal operations and edge cases.
 */
class PriceCalculatorTest {

    @BeforeEach
    void setUp() {
        // Setup method for test initialization if needed
    }

    @Test
    @DisplayName("Should calculate discounted price correctly with valid inputs")
    void testCalculateDiscountedPrice_ValidInputs() {
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
    void testCalculateDiscountedPrice_NegativeDiscount() {
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

    @Test
    @DisplayName("Should throw IllegalArgumentException when discount percentage exceeds 100")
    void testCalculateDiscountedPrice_ExcessiveDiscount() {
        // Given
        double originalPrice = 100.0;
        double invalidDiscountPercentage = 150.0;

        // When & Then
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> PriceCalculator.calculateDiscountedPrice(originalPrice, invalidDiscountPercentage)
        );
        
        assertEquals("Discount percentage must be between 0 and 100.", exception.getMessage());
    }

    @Test
    @DisplayName("Should calculate final price with tax correctly")
    void testCalculateFinalPriceWithTax_ValidInputs() {
        // Given
        double basePrice = 100.0;
        double taxRate = 0.05; // 5% tax
        double expectedFinalPrice = 105.0;

        // When
        double actualFinalPrice = PriceCalculator.calculateFinalPriceWithTax(basePrice, taxRate);

        // Then
        assertEquals(expectedFinalPrice, actualFinalPrice, 0.01);
    }

    @Test
    @DisplayName("Should throw IllegalArgumentException when tax rate is negative")
    void testCalculateFinalPriceWithTax_NegativeTaxRate() {
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

    @Test
    @DisplayName("Should handle zero discount percentage")
    void testCalculateDiscountedPrice_ZeroDiscount() {
        // Given
        double originalPrice = 100.0;
        double zeroDiscountPercentage = 0.0;

        // When
        double actualDiscountedPrice = PriceCalculator.calculateDiscountedPrice(originalPrice, zeroDiscountPercentage);

        // Then
        assertEquals(originalPrice, actualDiscountedPrice, 0.01);
    }

    @Test
    @DisplayName("Should handle zero tax rate")
    void testCalculateFinalPriceWithTax_ZeroTaxRate() {
        // Given
        double basePrice = 100.0;
        double zeroTaxRate = 0.0;

        // When
        double actualFinalPrice = PriceCalculator.calculateFinalPriceWithTax(basePrice, zeroTaxRate);

        // Then
        assertEquals(basePrice, actualFinalPrice, 0.01);
    }

    @Test
    @DisplayName("Should handle maximum valid discount percentage")
    void testCalculateDiscountedPrice_MaximumDiscount() {
        // Given
        double originalPrice = 100.0;
        double maxDiscountPercentage = 100.0;
        double expectedDiscountedPrice = 0.0;

        // When
        double actualDiscountedPrice = PriceCalculator.calculateDiscountedPrice(originalPrice, maxDiscountPercentage);

        // Then
        assertEquals(expectedDiscountedPrice, actualDiscountedPrice, 0.01);
    }
}