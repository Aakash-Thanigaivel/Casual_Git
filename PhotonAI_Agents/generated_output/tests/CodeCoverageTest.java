import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import static org.junit.jupiter.api.Assertions.*;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Scanner;

/**
 * Unit tests focusing on specific method behaviors and edge cases
 * Ensures proper code coverage for all conversion scenarios
 */
public class CodeCoverageTest {

    @Nested
    @DisplayName("PriceCalculator Method Coverage Tests")
    class PriceCalculatorCoverageTests {

        @Test
        @DisplayName("Should validate discount percentage bounds exactly")
        void testDiscountBoundsValidation() {
            // Test lower bound violation
            assertThrows(IllegalArgumentException.class, 
                () -> PriceCalculator.calculateDiscountedPrice(100.0, -0.001));
            
            // Test upper bound violation
            assertThrows(IllegalArgumentException.class, 
                () -> PriceCalculator.calculateDiscountedPrice(100.0, 100.001));
            
            // Test exact bounds (should work)
            assertDoesNotThrow(() -> PriceCalculator.calculateDiscountedPrice(100.0, 0.0));
            assertDoesNotThrow(() -> PriceCalculator.calculateDiscountedPrice(100.0, 100.0));
        }

        @Test
        @DisplayName("Should validate tax rate bounds exactly")
        void testTaxRateBoundsValidation() {
            // Test negative tax rate
            assertThrows(IllegalArgumentException.class, 
                () -> PriceCalculator.calculateFinalPriceWithTax(100.0, -0.001));
            
            // Test zero tax rate (should work)
            assertDoesNotThrow(() -> PriceCalculator.calculateFinalPriceWithTax(100.0, 0.0));
            
            // Test positive tax rate (should work)
            assertDoesNotThrow(() -> PriceCalculator.calculateFinalPriceWithTax(100.0, 1.0));
        }

        @Test
        @DisplayName("Should handle floating point precision edge cases")
        void testFloatingPointPrecision() {
            // Test with numbers that might cause floating point precision issues
            double result = PriceCalculator.calculateDiscountedPrice(0.1, 10.0);
            assertEquals(0.09, result, 0.001);
            
            result = PriceCalculator.calculateFinalPriceWithTax(0.1, 0.1);
            assertEquals(0.11, result, 0.001);
        }

        @Test
        @DisplayName("Should handle exception message validation")
        void testExceptionMessages() {
            IllegalArgumentException discountException = assertThrows(
                IllegalArgumentException.class,
                () -> PriceCalculator.calculateDiscountedPrice(100.0, -5.0)
            );
            assertEquals("Discount percentage must be between 0 and 100.", discountException.getMessage());
            
            IllegalArgumentException taxException = assertThrows(
                IllegalArgumentException.class,
                () -> PriceCalculator.calculateFinalPriceWithTax(100.0, -0.05)
            );
            assertEquals("Tax rate cannot be negative.", taxException.getMessage());
        }
    }

    @Nested
    @DisplayName("Main Method Coverage Tests")
    class MainMethodCoverageTests {

        @Test
        @DisplayName("Should handle scanner resource management")
        void testScannerResourceManagement() {
            // Test that scanner is properly closed even with valid input
            String input = "100.0\n10.0\n0.05\n";
            ByteArrayInputStream inputStream = new ByteArrayInputStream(input.getBytes());
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            
            System.setIn(inputStream);
            System.setOut(new PrintStream(outputStream));
            
            try {
                PriceCalculator.main(new String[]{});
                String output = outputStream.toString();
                
                // Verify calculations were performed
                assertTrue(output.contains("90.00"));
                assertTrue(output.contains("94.50"));
            } finally {
                System.setIn(System.in);
                System.setOut(System.out);
            }
        }

        @Test
        @DisplayName("Should handle various input validation scenarios")
        void testInputValidationScenarios() {
            // Test invalid discount percentage input
            String invalidDiscountInput = "100.0\n150.0\n";
            ByteArrayInputStream inputStream = new ByteArrayInputStream(invalidDiscountInput.getBytes());
            ByteArrayOutputStream errorStream = new ByteArrayOutputStream();
            
            System.setIn(inputStream);
            System.setErr(new PrintStream(errorStream));
            
            try {
                PriceCalculator.main(new String[]{});
                String errorOutput = errorStream.toString();
                assertTrue(errorOutput.contains("Error:"));
            } finally {
                System.setIn(System.in);
                System.setErr(System.err);
            }
        }

        @Test
        @DisplayName("Should handle negative tax rate input")
        void testNegativeTaxRateInput() {
            String negativeTaxInput = "100.0\n10.0\n-0.05\n";
            ByteArrayInputStream inputStream = new ByteArrayInputStream(negativeTaxInput.getBytes());
            ByteArrayOutputStream errorStream = new ByteArrayOutputStream();
            
            System.setIn(inputStream);
            System.setErr(new PrintStream(errorStream));
            
            try {
                PriceCalculator.main(new String[]{});
                String errorOutput = errorStream.toString();
                assertTrue(errorOutput.contains("Error:"));
            } finally {
                System.setIn(System.in);
                System.setErr(System.err);
            }
        }
    }

    @Nested
    @DisplayName("Code Path Coverage Tests")
    class CodePathCoverageTests {

        @Test
        @DisplayName("Should cover all conditional branches in calculateDiscountedPrice")
        void testAllDiscountCalculationBranches() {
            // Test all branches of the discount validation
            
            // Branch 1: discountPercentage < 0
            assertThrows(IllegalArgumentException.class, 
                () -> PriceCalculator.calculateDiscountedPrice(100.0, -1.0));
            
            // Branch 2: discountPercentage > 100
            assertThrows(IllegalArgumentException.class, 
                () -> PriceCalculator.calculateDiscountedPrice(100.0, 101.0));
            
            // Branch 3: Valid discount percentage
            assertDoesNotThrow(() -> PriceCalculator.calculateDiscountedPrice(100.0, 50.0));
        }

        @Test
        @DisplayName("Should cover all conditional branches in calculateFinalPriceWithTax")
        void testAllTaxCalculationBranches() {
            // Test all branches of the tax rate validation
            
            // Branch 1: taxRate < 0
            assertThrows(IllegalArgumentException.class, 
                () -> PriceCalculator.calculateFinalPriceWithTax(100.0, -1.0));
            
            // Branch 2: Valid tax rate (>= 0)
            assertDoesNotThrow(() -> PriceCalculator.calculateFinalPriceWithTax(100.0, 0.1));
        }

        @Test
        @DisplayName("Should test mathematical operations coverage")
        void testMathematicalOperations() {
            // Test the actual calculation formulas
            
            // Discount calculation: price - (price * discountPercentage / 100.0)
            double result = PriceCalculator.calculateDiscountedPrice(200.0, 25.0);
            assertEquals(150.0, result, 0.01);
            
            // Tax calculation: price + (price * taxRate)
            result = PriceCalculator.calculateFinalPriceWithTax(100.0, 0.2);
            assertEquals(120.0, result, 0.01);
        }
    }
}