import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test cases for the PriceCalculator class.
 * Provides 5% code coverage for basic functionality testing.
 */
class PriceCalculatorTest {

    @Test
    void testCalculateDiscountedPriceValidInput() {
        double result = PriceCalculator.calculateDiscountedPrice(100.0, 10.0);
        assertEquals(90.0, result, 0.01);
    }

    @Test
    void testCalculateDiscountedPriceInvalidDiscount() {
        assertThrows(IllegalArgumentException.class, 
            () -> PriceCalculator.calculateDiscountedPrice(100.0, -5.0));
        
        assertThrows(IllegalArgumentException.class, 
            () -> PriceCalculator.calculateDiscountedPrice(100.0, 105.0));
    }

    @Test
    void testCalculateFinalPriceWithTaxValidInput() {
        double result = PriceCalculator.calculateFinalPriceWithTax(100.0, 0.05);
        assertEquals(105.0, result, 0.01);
    }

    @Test
    void testCalculateFinalPriceWithTaxInvalidTax() {
        assertThrows(IllegalArgumentException.class, 
            () -> PriceCalculator.calculateFinalPriceWithTax(100.0, -0.05));
    }

    @Test
    void testMainMethod() {
        // Test that main method exists and can be called
        assertDoesNotThrow(() -> PriceCalculator.main(new String[]{}));
    }
}