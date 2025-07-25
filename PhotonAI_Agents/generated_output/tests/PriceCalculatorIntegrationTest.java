import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.InputStream;

/**
 * Integration tests for PriceCalculator main method functionality.
 * 
 * <p>This test class focuses on testing the console interface and
 * user interaction aspects of the PriceCalculator application.
 */
class PriceCalculatorIntegrationTest {

    private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();
    private final ByteArrayOutputStream errorStreamCaptor = new ByteArrayOutputStream();
    private final PrintStream standardOut = System.out;
    private final PrintStream standardErr = System.err;
    private final InputStream standardIn = System.in;

    @BeforeEach
    void setUp() {
        System.setOut(new PrintStream(outputStreamCaptor));
        System.setErr(new PrintStream(errorStreamCaptor));
    }

    @AfterEach
    void tearDown() {
        System.setOut(standardOut);
        System.setErr(standardErr);
        System.setIn(standardIn);
    }

    @Test
    @DisplayName("Should handle valid input flow successfully")
    void testValidInputFlow() {
        // Simulate user input: price=100, discount=20, tax=0.05
        String input = "100.0\n20.0\n0.05\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        // Execute main method
        PriceCalculator.main(new String[]{});

        String output = outputStreamCaptor.toString();
        
        // Verify prompts are displayed
        assertTrue(output.contains("Enter the original price:"));
        assertTrue(output.contains("Enter the discount percentage:"));
        assertTrue(output.contains("Enter the tax rate"));
        
        // Verify calculations are displayed
        assertTrue(output.contains("Price after 20.00% discount: ₹80.00"));
        assertTrue(output.contains("Final price with tax: ₹84.00"));
    }

    @Test
    @DisplayName("Should handle invalid discount percentage gracefully")
    void testInvalidDiscountPercentage() {
        // Simulate user input with invalid discount
        String input = "100.0\n150.0\n0.05\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        // Execute main method
        PriceCalculator.main(new String[]{});

        String errorOutput = errorStreamCaptor.toString();
        
        // Verify error message is displayed
        assertTrue(errorOutput.contains("Error:") || 
                  outputStreamCaptor.toString().contains("Error:"));
    }

    @Test
    @DisplayName("Should handle invalid tax rate gracefully")
    void testInvalidTaxRate() {
        // Simulate user input with invalid tax rate
        String input = "100.0\n20.0\n-0.05\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        // Execute main method
        PriceCalculator.main(new String[]{});

        String errorOutput = errorStreamCaptor.toString();
        
        // Verify error message is displayed
        assertTrue(errorOutput.contains("Error:") || 
                  outputStreamCaptor.toString().contains("Error:"));
    }

    @Test
    @DisplayName("Should handle non-numeric input gracefully")
    void testNonNumericInput() {
        // Simulate user input with non-numeric values
        String input = "abc\n20.0\n0.05\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        // Execute main method
        PriceCalculator.main(new String[]{});

        String errorOutput = errorStreamCaptor.toString();
        
        // Verify error handling
        assertTrue(errorOutput.contains("Error:") || 
                  outputStreamCaptor.toString().contains("Error:"));
    }

    @Test
    @DisplayName("Should handle zero values correctly")
    void testZeroValues() {
        // Simulate user input with zero values
        String input = "0.0\n0.0\n0.0\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        // Execute main method
        PriceCalculator.main(new String[]{});

        String output = outputStreamCaptor.toString();
        
        // Verify zero calculations
        assertTrue(output.contains("Price after 0.00% discount: ₹0.00"));
        assertTrue(output.contains("Final price with tax: ₹0.00"));
    }

    @Test
    @DisplayName("Should handle maximum discount correctly")
    void testMaximumDiscount() {
        // Simulate user input with 100% discount
        String input = "100.0\n100.0\n0.05\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        // Execute main method
        PriceCalculator.main(new String[]{});

        String output = outputStreamCaptor.toString();
        
        // Verify 100% discount calculation
        assertTrue(output.contains("Price after 100.00% discount: ₹0.00"));
        assertTrue(output.contains("Final price with tax: ₹0.00"));
    }

    @Test
    @DisplayName("Should handle decimal inputs correctly")
    void testDecimalInputs() {
        // Simulate user input with decimal values
        String input = "99.99\n15.5\n0.075\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        // Execute main method
        PriceCalculator.main(new String[]{});

        String output = outputStreamCaptor.toString();
        
        // Verify decimal calculations are handled
        assertTrue(output.contains("Price after 15.50% discount:"));
        assertTrue(output.contains("Final price with tax:"));
    }

    @Test
    @DisplayName("Should display currency symbol correctly")
    void testCurrencyDisplay() {
        // Simulate valid input
        String input = "50.0\n10.0\n0.08\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        // Execute main method
        PriceCalculator.main(new String[]{});

        String output = outputStreamCaptor.toString();
        
        // Verify currency symbol is displayed
        assertTrue(output.contains("₹"));
    }

    private void assertTrue(boolean condition) {
        if (!condition) {
            throw new AssertionError("Expected condition to be true");
        }
    }
}