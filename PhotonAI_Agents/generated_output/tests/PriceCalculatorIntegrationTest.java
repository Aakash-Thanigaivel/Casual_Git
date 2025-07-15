import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import static org.junit.jupiter.api.Assertions.*;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.InputStream;

/**
 * Integration test suite for PriceCalculator main method.
 * Tests the complete user interaction flow for Bank of America code standards.
 */
class PriceCalculatorIntegrationTest {

    private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();
    private final PrintStream standardOut = System.out;
    private final InputStream standardIn = System.in;

    @BeforeEach
    void setUp() {
        System.setOut(new PrintStream(outputStreamCaptor));
    }

    @AfterEach
    void tearDown() {
        System.setOut(standardOut);
        System.setIn(standardIn);
    }

    @Test
    @DisplayName("Should handle complete valid user input flow")
    void testMainMethod_ValidInputFlow() {
        String input = "100.0\n10.0\n0.05\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        assertDoesNotThrow(() -> PriceCalculator.main(new String[]{}));
        
        String output = outputStreamCaptor.toString();
        assertTrue(output.contains("Enter the original price:"));
        assertTrue(output.contains("Enter the discount percentage:"));
        assertTrue(output.contains("Enter the tax rate"));
        assertTrue(output.contains("Price after 10.00% discount: ₹90.00"));
        assertTrue(output.contains("Final price with tax: ₹94.50"));
    }

    @Test
    @DisplayName("Should handle invalid discount input gracefully")
    void testMainMethod_InvalidDiscountInput() {
        String input = "100.0\n-5.0\n0.05\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        assertDoesNotThrow(() -> PriceCalculator.main(new String[]{}));
        
        String output = outputStreamCaptor.toString();
        assertTrue(output.contains("Error:"));
    }

    @Test
    @DisplayName("Should handle invalid tax input gracefully")
    void testMainMethod_InvalidTaxInput() {
        String input = "100.0\n10.0\n-0.05\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        assertDoesNotThrow(() -> PriceCalculator.main(new String[]{}));
        
        String output = outputStreamCaptor.toString();
        assertTrue(output.contains("Error:"));
    }

    @ParameterizedTest
    @CsvSource({
        "100.0, 10.0, 0.05, 94.50",
        "200.0, 20.0, 0.10, 176.00",
        "50.0, 0.0, 0.08, 54.00",
        "1000.0, 25.0, 0.15, 862.50"
    })
    @DisplayName("Should calculate correct final prices for various inputs")
    void testMainMethod_VariousValidInputs(double price, double discount, double tax, double expected) {
        String input = price + "\n" + discount + "\n" + tax + "\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        assertDoesNotThrow(() -> PriceCalculator.main(new String[]{}));
        
        String output = outputStreamCaptor.toString();
        assertTrue(output.contains(String.format("Final price with tax: ₹%.2f", expected)));
    }

    @Test
    @DisplayName("Should handle non-numeric input gracefully")
    void testMainMethod_NonNumericInput() {
        String input = "abc\n10.0\n0.05\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        assertDoesNotThrow(() -> PriceCalculator.main(new String[]{}));
        
        String output = outputStreamCaptor.toString();
        assertTrue(output.contains("Error:"));
    }
}