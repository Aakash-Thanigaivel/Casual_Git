package com.bofa.banking.edge;

import com.bofa.banking.BankingApplication;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.math.BigDecimal;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Edge case tests for BankingApplication
 * Tests boundary conditions and unusual scenarios
 * Ensures robust handling of edge cases in modernized code
 */
class BankingApplicationEdgeCaseTest {

    private BankingApplication bankingApplication;

    @BeforeEach
    void setUp() {
        bankingApplication = new BankingApplication();
    }

    @Test
    @DisplayName("Test transaction processing with empty string")
    void testTransactionProcessingEmptyString() {
        String result = bankingApplication.processTransaction("");
        assertEquals("Unknown transaction type: ", result);
    }

    @Test
    @DisplayName("Test transaction processing with whitespace only")
    void testTransactionProcessingWhitespaceOnly() {
        String result = bankingApplication.processTransaction("   ");
        assertEquals("Unknown transaction type:    ", result);
    }

    @Test
    @DisplayName("Test transaction processing with mixed case")
    void testTransactionProcessingMixedCase() {
        String[] mixedCaseInputs = {
            "DePosIt",
            "wItHdRaWaL",
            "TrAnSfEr",
            "bAlAnCe_InQuIrY"
        };
        
        String[] expectedOutputs = {
            "Processing deposit transaction",
            "Processing withdrawal transaction", 
            "Processing transfer transaction",
            "Processing balance inquiry"
        };
        
        for (int i = 0; i < mixedCaseInputs.length; i++) {
            String result = bankingApplication.processTransaction(mixedCaseInputs[i]);
            assertEquals(expectedOutputs[i], result);
        }
    }

    @ParameterizedTest
    @MethodSource("provideAccountNumberEdgeCases")
    @DisplayName("Test account validation edge cases")
    void testAccountValidationEdgeCases(String accountNumber, boolean expected, String description) {
        boolean result = bankingApplication.validateAccountNumber(accountNumber);
        assertEquals(expected, result, description);
    }

    static Stream<Arguments> provideAccountNumberEdgeCases() {
        return Stream.of(
            Arguments.of("0000000000", true, "All zeros should be valid"),
            Arguments.of("9999999999", true, "All nines should be valid"),
            Arguments.of("1234567890", true, "Exactly 10 digits should be valid"),
            Arguments.of("123456789012", true, "Exactly 12 digits should be valid"),
            Arguments.of("12345678901", true, "11 digits should be valid"),
            Arguments.of("123456789", false, "9 digits should be invalid"),
            Arguments.of("1234567890123", false, "13 digits should be invalid"),
            Arguments.of("123456789a", false, "Contains letter should be invalid"),
            Arguments.of("123-456-7890", false, "Contains hyphens should be invalid"),
            Arguments.of("123 456 7890", false, "Contains spaces should be invalid"),
            Arguments.of("+1234567890", false, "Contains plus sign should be invalid"),
            Arguments.of("1234567890.", false, "Contains decimal point should be invalid")
        );
    }

    @Test
    @DisplayName("Test interest calculation with zero values")
    void testInterestCalculationZeroValues() {
        BigDecimal zeroPrincipal = BigDecimal.ZERO;
        BigDecimal zeroRate = BigDecimal.ZERO;
        int zeroTime = 0;
        
        // Zero principal
        BigDecimal result1 = bankingApplication.calculateInterest(zeroPrincipal, new BigDecimal("5.0"), 2);
        assertEquals(BigDecimal.ZERO, result1);
        
        // Zero rate
        BigDecimal result2 = bankingApplication.calculateInterest(new BigDecimal("1000.00"), zeroRate, 2);
        assertEquals(BigDecimal.ZERO, result2);
        
        // Zero time
        BigDecimal result3 = bankingApplication.calculateInterest(new BigDecimal("1000.00"), new BigDecimal("5.0"), zeroTime);
        assertEquals(BigDecimal.ZERO, result3);
    }

    @Test
    @DisplayName("Test interest calculation with very small values")
    void testInterestCalculationVerySmallValues() {
        BigDecimal smallPrincipal = new BigDecimal("0.01");
        BigDecimal smallRate = new BigDecimal("0.01");
        int smallTime = 1;
        
        BigDecimal result = bankingApplication.calculateInterest(smallPrincipal, smallRate, smallTime);
        assertNotNull(result);
        assertTrue(result.compareTo(BigDecimal.ZERO) >= 0);
    }

    @Test
    @DisplayName("Test interest calculation precision and rounding")
    void testInterestCalculationPrecisionRounding() {
        BigDecimal principal = new BigDecimal("1000.00");
        BigDecimal rate = new BigDecimal("3.333333333333333"); // Repeating decimal
        int time = 3;
        
        BigDecimal result = bankingApplication.calculateInterest(principal, rate, time);
        assertNotNull(result);
        
        // Check that result is properly rounded (should have reasonable precision)
        assertTrue(result.scale() <= 10, "Result should have reasonable precision");
    }

    @Test
    @DisplayName("Test interest calculation with maximum integer time")
    void testInterestCalculationMaxTime() {
        BigDecimal principal = new BigDecimal("1.00");
        BigDecimal rate = new BigDecimal("0.01");
        int maxTime = Integer.MAX_VALUE;
        
        // Should handle maximum time value without overflow
        assertDoesNotThrow(() -> {
            BigDecimal result = bankingApplication.calculateInterest(principal, rate, maxTime);
            assertNotNull(result);
        });
    }

    @Test
    @DisplayName("Test account validation with Unicode characters")
    void testAccountValidationUnicodeCharacters() {
        String[] unicodeInputs = {
            "123456789０", // Full-width zero
            "１２３４５６７８９０", // Full-width digits
            "1234567890①", // Circled digit
            "1234567890Ⅰ", // Roman numeral
            "1234567890₁", // Subscript digit
        };
        
        for (String input : unicodeInputs) {
            boolean result = bankingApplication.validateAccountNumber(input);
            assertFalse(result, "Unicode characters should be rejected: " + input);
        }
    }

    @Test
    @DisplayName("Test transaction processing with very long strings")
    void testTransactionProcessingVeryLongStrings() {
        // Create a very long string
        StringBuilder longString = new StringBuilder();
        for (int i = 0; i < 10000; i++) {
            longString.append("DEPOSIT");
        }
        
        String result = bankingApplication.processTransaction(longString.toString());
        assertEquals("Processing deposit transaction", result);
    }

    @Test
    @DisplayName("Test BigDecimal edge cases in interest calculation")
    void testBigDecimalEdgeCases() {
        // Test with very high precision BigDecimal
        BigDecimal highPrecisionPrincipal = new BigDecimal("1000.123456789012345678901234567890");
        BigDecimal highPrecisionRate = new BigDecimal("5.987654321098765432109876543210");
        int time = 2;
        
        BigDecimal result = bankingApplication.calculateInterest(highPrecisionPrincipal, highPrecisionRate, time);
        assertNotNull(result);
        assertTrue(result.compareTo(BigDecimal.ZERO) > 0);
    }
}