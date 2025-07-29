import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test cases for PriceCalculator utility class.
 * 
 * <p>This test class provides unit tests for the PriceCalculator class,
 * covering discount calculations and tax calculations with various scenarios.
 */
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
    assertEquals(80.0, result, 0.01, "Discounted price should be 80.0");
  }

  @Test
  @DisplayName("Should throw exception when discount percentage is negative")
  void testCalculateDiscountedPriceNegativeDiscount() {
    // Given
    double price = 100.0;
    double discountPercentage = -5.0;
    
    // When & Then
    IllegalArgumentException exception = assertThrows(
        IllegalArgumentException.class,
        () -> PriceCalculator.calculateDiscountedPrice(price, discountPercentage)
    );
    assertEquals("Discount percentage must be between 0 and 100.", exception.getMessage());
  }

  @Test
  @DisplayName("Should throw exception when discount percentage exceeds 100")
  void testCalculateDiscountedPriceExcessiveDiscount() {
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
    double taxRate = 0.05; // 5% tax
    
    // When
    double result = PriceCalculator.calculateFinalPriceWithTax(price, taxRate);
    
    // Then
    assertEquals(105.0, result, 0.01, "Final price with tax should be 105.0");
  }

  @Test
  @DisplayName("Should throw exception when tax rate is negative")
  void testCalculateFinalPriceWithTaxNegativeTaxRate() {
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

  @Test
  @DisplayName("Should handle zero discount percentage")
  void testCalculateDiscountedPriceZeroDiscount() {
    // Given
    double price = 100.0;
    double discountPercentage = 0.0;
    
    // When
    double result = PriceCalculator.calculateDiscountedPrice(price, discountPercentage);
    
    // Then
    assertEquals(100.0, result, 0.01, "Price should remain unchanged with zero discount");
  }

  @Test
  @DisplayName("Should handle zero tax rate")
  void testCalculateFinalPriceWithZeroTax() {
    // Given
    double price = 100.0;
    double taxRate = 0.0;
    
    // When
    double result = PriceCalculator.calculateFinalPriceWithTax(price, taxRate);
    
    // Then
    assertEquals(100.0, result, 0.01, "Price should remain unchanged with zero tax");
  }

  @Test
  @DisplayName("Should handle maximum valid discount percentage")
  void testCalculateDiscountedPriceMaxDiscount() {
    // Given
    double price = 100.0;
    double discountPercentage = 100.0;
    
    // When
    double result = PriceCalculator.calculateDiscountedPrice(price, discountPercentage);
    
    // Then
    assertEquals(0.0, result, 0.01, "Price should be zero with 100% discount");
  }
}