import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for PriceCalculator class.
 * Tests cover discount calculation and tax application functionality.
 */
public class PriceCalculatorTest {

  @Test
  @DisplayName("Should calculate correct discounted price with valid discount")
  void testCalculateDiscountedPrice_ValidDiscount() {
    // Given
    double originalPrice = 100.0;
    double discountPercentage = 20.0;
    
    // When
    double result = PriceCalculator.calculateDiscountedPrice(originalPrice, discountPercentage);
    
    // Then
    assertEquals(80.0, result, 0.01);
  }

  @Test
  @DisplayName("Should throw exception for negative discount percentage")
  void testCalculateDiscountedPrice_NegativeDiscount() {
    // Given
    double originalPrice = 100.0;
    double discountPercentage = -10.0;
    
    // When & Then
    assertThrows(IllegalArgumentException.class, () -> {
      PriceCalculator.calculateDiscountedPrice(originalPrice, discountPercentage);
    });
  }

  @Test
  @DisplayName("Should throw exception for discount percentage over 100")
  void testCalculateDiscountedPrice_DiscountOver100() {
    // Given
    double originalPrice = 100.0;
    double discountPercentage = 110.0;
    
    // When & Then
    assertThrows(IllegalArgumentException.class, () -> {
      PriceCalculator.calculateDiscountedPrice(originalPrice, discountPercentage);
    });
  }

  @Test
  @DisplayName("Should calculate correct final price with tax")
  void testCalculateFinalPriceWithTax_ValidTax() {
    // Given
    double price = 100.0;
    double taxRate = 0.05; // 5% tax
    
    // When
    double result = PriceCalculator.calculateFinalPriceWithTax(price, taxRate);
    
    // Then
    assertEquals(105.0, result, 0.01);
  }

  @Test
  @DisplayName("Should throw exception for negative tax rate")
  void testCalculateFinalPriceWithTax_NegativeTax() {
    // Given
    double price = 100.0;
    double taxRate = -0.05;
    
    // When & Then
    assertThrows(IllegalArgumentException.class, () -> {
      PriceCalculator.calculateFinalPriceWithTax(price, taxRate);
    });
  }
}