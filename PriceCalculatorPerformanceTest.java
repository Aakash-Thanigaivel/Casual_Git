import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import static org.junit.jupiter.api.Assertions.*;
import java.util.stream.Stream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;

/**
 * Performance and boundary test suite for PriceCalculator class.
 * Tests performance, boundary conditions, and class structure for Bank of America standards.
 */
class PriceCalculatorPerformanceTest {

    @Test
    @DisplayName("Should prevent instantiation of utility class")
    void testUtilityClassInstantiation() {
        Constructor<?>[] constructors = PriceCalculator.class.getDeclaredConstructors();
        assertEquals(1, constructors.length, "Should have exactly one constructor");
        
        Constructor<?> constructor = constructors[0];
        assertTrue(Modifier.isPrivate(constructor.getModifiers()), 
                  "Constructor should be private");
        
        constructor.setAccessible(true);
        assertThrows(Exception.class, () -> constructor.newInstance(),
                    "Should not be able to instantiate utility class");
    }

    @Test
    @DisplayName("Should verify class is final")
    void testClassIsFinal() {
        assertTrue(Modifier.isFinal(PriceCalculator.class.getModifiers()),
                  "PriceCalculator class should be final");
    }

    @RepeatedTest(100)
    @DisplayName("Should maintain consistency across multiple calculations")
    void testCalculationConsistency() {
        double price = 100.0;
        double discount = 15.0;
        double taxRate = 0.08;
        
        double discountedPrice = PriceCalculator.calculateDiscountedPrice(price, discount);
        double finalPrice = PriceCalculator.calculateFinalPriceWithTax(discountedPrice, taxRate);
        
        assertEquals(85.0, discountedPrice, 0.001);
        assertEquals(91.8, finalPrice, 0.001);
    }

    @ParameterizedTest
    @MethodSource("provideBoundaryValues")
    @DisplayName("Should handle boundary values correctly")
    void testBoundaryValues(double price, double discount, double tax, boolean shouldThrow) {
        if (shouldThrow) {
            assertThrows(IllegalArgumentException.class, () -> {
                double discountedPrice = PriceCalculator.calculateDiscountedPrice(price, discount);
                PriceCalculator.calculateFinalPriceWithTax(discountedPrice, tax);
            });
        } else {
            assertDoesNotThrow(() -> {
                double discountedPrice = PriceCalculator.calculateDiscountedPrice(price, discount);
                PriceCalculator.calculateFinalPriceWithTax(discountedPrice, tax);
            });
        }
    }

    private static Stream<Arguments> provideBoundaryValues() {
        return Stream.of(
            Arguments.of(Double.MAX_VALUE, 0.0, 0.0, false),
            Arguments.of(Double.MIN_VALUE, 0.0, 0.0, false),
            Arguments.of(100.0, 0.0, 0.0, false),
            Arguments.of(100.0, 100.0, 0.0, false),
            Arguments.of(100.0, -0.1, 0.0, true),
            Arguments.of(100.0, 100.1, 0.0, true),
            Arguments.of(100.0, 10.0, -0.1, true),
            Arguments.of(0.0, 50.0, 0.5, false)
        );
    }

    @Test
    @DisplayName("Should handle floating point precision correctly")
    void testFloatingPointPrecision() {
        double price = 0.1 + 0.2; // This equals 0.30000000000000004 in floating point
        double discount = 10.0;
        
        double result = PriceCalculator.calculateDiscountedPrice(price, discount);
        assertTrue(result > 0.26 && result < 0.28, 
                  "Should handle floating point precision issues");
    }

    @Test
    @DisplayName("Should perform calculations efficiently")
    void testPerformance() {
        long startTime = System.nanoTime();
        
        for (int i = 0; i < 10000; i++) {
            double discountedPrice = PriceCalculator.calculateDiscountedPrice(100.0, 10.0);
            PriceCalculator.calculateFinalPriceWithTax(discountedPrice, 0.05);
        }
        
        long endTime = System.nanoTime();
        long duration = endTime - startTime;
        
        // Should complete 10,000 calculations in less than 100ms
        assertTrue(duration < 100_000_000, 
                  "10,000 calculations should complete in less than 100ms");
    }

    @Test
    @DisplayName("Should handle very small decimal values")
    void testVerySmallDecimals() {
        double price = 0.01;
        double discount = 1.0;
        double taxRate = 0.001;
        
        double discountedPrice = PriceCalculator.calculateDiscountedPrice(price, discount);
        assertEquals(0.0099, discountedPrice, 0.0001);
        
        double finalPrice = PriceCalculator.calculateFinalPriceWithTax(discountedPrice, taxRate);
        assertTrue(finalPrice > 0.0099 && finalPrice < 0.01);
    }

    @Test
    @DisplayName("Should maintain mathematical properties")
    void testMathematicalProperties() {
        double price = 100.0;
        
        // Test that 0% discount returns original price
        assertEquals(price, PriceCalculator.calculateDiscountedPrice(price, 0.0), 0.001);
        
        // Test that 0% tax returns original price
        assertEquals(price, PriceCalculator.calculateFinalPriceWithTax(price, 0.0), 0.001);
        
        // Test that 100% discount returns 0
        assertEquals(0.0, PriceCalculator.calculateDiscountedPrice(price, 100.0), 0.001);
        
        // Test commutative property doesn't apply (discount then tax != tax then discount)
        double discountFirst = PriceCalculator.calculateFinalPriceWithTax(
            PriceCalculator.calculateDiscountedPrice(price, 10.0), 0.1);
        double taxFirst = PriceCalculator.calculateDiscountedPrice(
            PriceCalculator.calculateFinalPriceWithTax(price, 0.1), 10.0);
        
        assertNotEquals(discountFirst, taxFirst, 0.001, 
                       "Order of operations should matter");
    }
}