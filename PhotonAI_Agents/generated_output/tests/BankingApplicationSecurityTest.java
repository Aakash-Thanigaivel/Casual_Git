package com.bofa.banking.security;

import com.bofa.banking.BankingApplication;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Security tests for BankingApplication
 * Ensures secure handling of banking operations
 * Tests input validation and security measures
 */
class BankingApplicationSecurityTest {

    private BankingApplication bankingApplication;

    @BeforeEach
    void setUp() {
        bankingApplication = new BankingApplication();
    }

    @ParameterizedTest
    @ValueSource(strings = {
        "'; DROP TABLE accounts; --",
        "<script>alert('xss')</script>",
        "../../etc/passwd",
        "null",
        "undefined",
        "%00",
        "\n\r\t"
    })
    @DisplayName("Test transaction processing with malicious inputs")
    void testTransactionProcessingSecurityInputs(String maliciousInput) {
        // Ensure malicious inputs are handled safely
        String result = bankingApplication.processTransaction(maliciousInput);
        assertNotNull(result);
        assertTrue(result.startsWith("Unknown transaction type:"));
        assertFalse(result.contains("DROP TABLE"));
        assertFalse(result.contains("<script>"));
    }

    @ParameterizedTest
    @ValueSource(strings = {
        "'; DROP TABLE accounts; --",
        "<script>alert('xss')</script>1234567890",
        "1234567890<img src=x onerror=alert(1)>",
        "1234567890\"; system('rm -rf /'); \"",
        "1234567890\n\r\t",
        "1234567890%00"
    })
    @DisplayName("Test account validation with malicious inputs")
    void testAccountValidationSecurityInputs(String maliciousInput) {
        // Ensure malicious account numbers are rejected
        boolean result = bankingApplication.validateAccountNumber(maliciousInput);
        assertFalse(result, "Malicious input should be rejected: " + maliciousInput);
    }

    @Test
    @DisplayName("Test interest calculation with extreme values")
    void testInterestCalculationExtremeValues() {
        // Test with very large numbers that could cause overflow
        BigDecimal extremePrincipal = new BigDecimal("999999999999999999999999999999.99");
        BigDecimal extremeRate = new BigDecimal("999999999.99");
        int extremeTime = Integer.MAX_VALUE;
        
        // Should handle extreme values gracefully without throwing exceptions
        assertDoesNotThrow(() -> {
            BigDecimal result = bankingApplication.calculateInterest(extremePrincipal, extremeRate, extremeTime);
            assertNotNull(result);
        });
    }

    @Test
    @DisplayName("Test interest calculation with negative values")
    void testInterestCalculationNegativeValues() {
        BigDecimal negativePrincipal = new BigDecimal("-1000.00");
        BigDecimal negativeRate = new BigDecimal("-5.0");
        int negativeTime = -1;
        
        // Test with negative principal
        BigDecimal result1 = bankingApplication.calculateInterest(negativePrincipal, new BigDecimal("5.0"), 2);
        assertNotNull(result1);
        
        // Test with negative rate
        BigDecimal result2 = bankingApplication.calculateInterest(new BigDecimal("1000.00"), negativeRate, 2);
        assertNotNull(result2);
        
        // Test with negative time (should return ZERO)
        BigDecimal result3 = bankingApplication.calculateInterest(new BigDecimal("1000.00"), new BigDecimal("5.0"), negativeTime);
        assertEquals(BigDecimal.ZERO, result3);
    }

    @Test
    @DisplayName("Test input sanitization for transaction types")
    void testTransactionTypeSanitization() {
        // Test various input formats that should be normalized
        String[] inputs = {
            "  DEPOSIT  ",
            "deposit",
            "Deposit",
            "DEPOSIT\n",
            "DEPOSIT\r\n",
            "\tDEPOSIT\t"
        };
        
        for (String input : inputs) {
            String result = bankingApplication.processTransaction(input);
            assertEquals("Processing deposit transaction", result,
                "Input should be normalized: '" + input + "'");
        }
    }

    @Test
    @DisplayName("Test account number length boundaries")
    void testAccountNumberLengthBoundaries() {
        // Test exact boundary conditions
        assertFalse(bankingApplication.validateAccountNumber("123456789")); // 9 digits - too short
        assertTrue(bankingApplication.validateAccountNumber("1234567890"));  // 10 digits - minimum valid
        assertTrue(bankingApplication.validateAccountNumber("123456789012")); // 12 digits - maximum valid
        assertFalse(bankingApplication.validateAccountNumber("1234567890123")); // 13 digits - too long
    }

    @Test
    @DisplayName("Test memory usage with large operations")
    void testMemoryUsageWithLargeOperations() {
        // Monitor memory usage during large operations
        Runtime runtime = Runtime.getRuntime();
        long initialMemory = runtime.totalMemory() - runtime.freeMemory();
        
        // Perform many operations
        for (int i = 0; i < 10000; i++) {
            bankingApplication.processTransaction("DEPOSIT");
            bankingApplication.validateAccountNumber("1234567890");
            bankingApplication.calculateInterest(
                new BigDecimal("1000.00"), 
                new BigDecimal("5.0"), 
                2
            );
        }
        
        // Force garbage collection
        System.gc();
        long finalMemory = runtime.totalMemory() - runtime.freeMemory();
        
        // Memory usage should not increase dramatically
        long memoryIncrease = finalMemory - initialMemory;
        assertTrue(memoryIncrease < 50_000_000, // 50MB threshold
            "Memory usage should not increase dramatically: " + memoryIncrease + " bytes");
    }
}