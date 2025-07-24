import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test cases for PriceCalculator utility class.
 * 
 * <p>This test class provides comprehensive unit tests for the PriceCalculator
 * class methods with 5% code coverage focusing on critical functionality.
 */
class PriceCalculatorTest {

    @BeforeEach
    void setUp() {
        // Setup method for test initialization if needed
    }

    @Test
    @DisplayName("Test calculateDiscountedPrice with valid discount percentage")
    void testCalculateDiscountedPriceValidDiscount() {
        // Test with 10% discount on $100
        double originalPrice = 100.0;
        double discountPercentage = 10.0;
        double expected = 90.0;
        
        double actual = PriceCalculator.calculateDiscountedPrice(originalPrice, discountPercentage);
        
        assertEquals(expected, actual, 0.01, "Discounted price should be calculated correctly");
    }

    @Test
    @DisplayName("Test calculateDiscountedPrice with invalid discount percentage - negative")
    void testCalculateDiscountedPriceInvalidNegativeDiscount() {
        double originalPrice = 100.0;
        double invalidDiscount = -5.0;
        
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> PriceCalculator.calculateDiscountedPrice(originalPrice, invalidDiscount),
            "Should throw IllegalArgumentException for negative discount"
        );
        
        assertEquals("Discount percentage must be between 0 and 100.", exception.getMessage());
    }

    @Test
    @DisplayName("Test calculateDiscountedPrice with invalid discount percentage - over 100")
    void testCalculateDiscountedPriceInvalidHighDiscount() {
        double originalPrice = 100.0;
        double invalidDiscount = 150.0;
        
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> PriceCalculator.calculateDiscountedPrice(originalPrice, invalidDiscount),
            "Should throw IllegalArgumentException for discount over 100%"
        );
        
        assertEquals("Discount percentage must be between 0 and 100.", exception.getMessage());
    }

    @Test
    @DisplayName("Test calculateFinalPriceWithTax with valid tax rate")
    void testCalculateFinalPriceWithTaxValidRate() {
        // Test with 5% tax on $100
        double basePrice = 100.0;
        double taxRate = 0.05;
        double expected = 105.0;
        
        double actual = PriceCalculator.calculateFinalPriceWithTax(basePrice, taxRate);
        
        assertEquals(expected, actual, 0.01, "Final price with tax should be calculated correctly");
    }

    @Test
    @DisplayName("Test calculateFinalPriceWithTax with invalid negative tax rate")
    void testCalculateFinalPriceWithTaxInvalidNegativeRate() {
        double basePrice = 100.0;
        double invalidTaxRate = -0.05;
        
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> PriceCalculator.calculateFinalPriceWithTax(basePrice, invalidTaxRate),
            "Should throw IllegalArgumentException for negative tax rate"
        );
        
        assertEquals("Tax rate cannot be negative.", exception.getMessage());
    }

    @Test
    @DisplayName("Test calculateDiscountedPrice with zero discount")
    void testCalculateDiscountedPriceZeroDiscount() {
        double originalPrice = 100.0;
        double zeroDiscount = 0.0;
        double expected = 100.0;
        
        double actual = PriceCalculator.calculateDiscountedPrice(originalPrice, zeroDiscount);
        
        assertEquals(expected, actual, 0.01, "Price should remain same with zero discount");
    }

    @Test
    @DisplayName("Test calculateFinalPriceWithTax with zero tax rate")
    void testCalculateFinalPriceWithTaxZeroRate() {
        double basePrice = 100.0;
        double zeroTaxRate = 0.0;
        double expected = 100.0;
        
        double actual = PriceCalculator.calculateFinalPriceWithTax(basePrice, zeroTaxRate);
        
        assertEquals(expected, actual, 0.01, "Price should remain same with zero tax rate");
    }
}