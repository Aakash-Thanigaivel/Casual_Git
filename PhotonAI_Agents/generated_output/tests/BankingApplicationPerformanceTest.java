package com.bofa.banking.performance;

import com.bofa.banking.BankingApplication;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Timeout;

import java.math.BigDecimal;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Performance tests for BankingApplication
 * Ensures modernized code meets performance requirements
 * Tests JDK 17 performance improvements
 */
class BankingApplicationPerformanceTest {

    private BankingApplication bankingApplication;

    @BeforeEach
    void setUp() {
        bankingApplication = new BankingApplication();
    }

    @Test
    @DisplayName("Transaction processing performance test")
    @Timeout(value = 100, unit = TimeUnit.MILLISECONDS)
    void testTransactionProcessingPerformance() {
        // Test that transaction processing completes within 100ms
        long startTime = System.nanoTime();
        
        for (int i = 0; i < 1000; i++) {
            String result = bankingApplication.processTransaction("DEPOSIT");
            assertNotNull(result);
        }
        
        long endTime = System.nanoTime();
        long duration = (endTime - startTime) / 1_000_000; // Convert to milliseconds
        
        assertTrue(duration < 100, "Transaction processing should complete within 100ms");
    }

    @RepeatedTest(5)
    @DisplayName("Account validation performance test")
    @Timeout(value = 50, unit = TimeUnit.MILLISECONDS)
    void testAccountValidationPerformance() {
        // Test account validation performance with repeated executions
        String validAccount = "1234567890";
        
        for (int i = 0; i < 500; i++) {
            boolean result = bankingApplication.validateAccountNumber(validAccount);
            assertTrue(result);
        }
    }

    @Test
    @DisplayName("Interest calculation performance with large numbers")
    @Timeout(value = 200, unit = TimeUnit.MILLISECONDS)
    void testInterestCalculationPerformance() {
        BigDecimal largePrincipal = new BigDecimal("999999999.99");
        BigDecimal rate = new BigDecimal("5.5");
        int time = 10;
        
        // Perform multiple calculations to test performance
        for (int i = 0; i < 100; i++) {
            BigDecimal result = bankingApplication.calculateInterest(largePrincipal, rate, time);
            assertNotNull(result);
            assertTrue(result.compareTo(BigDecimal.ZERO) > 0);
        }
    }

    @Test
    @DisplayName("Concurrent transaction processing test")
    void testConcurrentTransactionProcessing() {
        // Test thread safety of transaction processing
        String[] transactionTypes = {"DEPOSIT", "WITHDRAWAL", "TRANSFER", "BALANCE_INQUIRY"};
        
        // Run concurrent transactions
        java.util.concurrent.CompletableFuture<?>[] futures = 
            new java.util.concurrent.CompletableFuture[10];
        
        for (int i = 0; i < 10; i++) {
            final int index = i;
            futures[i] = java.util.concurrent.CompletableFuture.runAsync(() -> {
                String type = transactionTypes[index % transactionTypes.length];
                String result = bankingApplication.processTransaction(type);
                assertNotNull(result);
            });
        }
        
        // Wait for all futures to complete
        java.util.concurrent.CompletableFuture.allOf(futures).join();
    }
}