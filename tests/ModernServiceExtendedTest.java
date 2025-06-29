package com.example.modernized.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Timeout;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Extended unit tests for ModernService including async operations.
 * Tests additional JDK 17 features and CompletableFuture functionality.
 */
@DisplayName("ModernService Extended Tests")
class ModernServiceExtendedTest {

    private ModernService service;

    @BeforeEach
    void setUp() {
        service = new ModernService();
    }

    @Test
    @DisplayName("Should process async operation successfully")
    @Timeout(value = 3, unit = TimeUnit.SECONDS)
    void testAsyncProcessSuccess() throws ExecutionException, InterruptedException {
        // Given
        String input = "test input";
        
        // When
        CompletableFuture<ModernService.ProcessingResult> future = service.asyncProcess(input);
        ModernService.ProcessingResult result = future.get();
        
        // Then
        assertNotNull(result);
        assertInstanceOf(ModernService.SuccessResult.class, result);
        ModernService.SuccessResult successResult = (ModernService.SuccessResult) result;
        assertTrue(successResult.message().contains("Processing completed successfully"));
        assertEquals("TEST INPUT", successResult.data());
    }

    @Test
    @DisplayName("Should handle findAndTransform with matching item")
    void testFindAndTransformWithMatch() {
        // Given
        List<String> items = Arrays.asList("apple", "banana", "cherry");
        String target = "ban";
        
        // When
        var result = service.findAndTransform(items, target);
        
        // Then
        assertTrue(result.isPresent());
        assertEquals("Found: banana", result.get());
    }

    @Test
    @DisplayName("Should return default when no match found")
    void testFindAndTransformNoMatch() {
        // Given
        List<String> items = Arrays.asList("apple", "cherry");
        String target = "ban";
        
        // When
        var result = service.findAndTransform(items, target);
        
        // Then
        assertTrue(result.isPresent());
        assertEquals("Found: DEFAULT", result.get());
    }

    @Test
    @DisplayName("Should handle null items in findAndTransform")
    void testFindAndTransformWithNulls() {
        // Given
        List<String> items = Arrays.asList("apple", null, "banana", null);
        String target = "app";
        
        // When
        var result = service.findAndTransform(items, target);
        
        // Then
        assertTrue(result.isPresent());
        assertEquals("Found: apple", result.get());
    }

    @Test
    @DisplayName("Should handle empty list in findAndTransform")
    void testFindAndTransformEmptyList() {
        // Given
        List<String> items = new ArrayList<>();
        String target = "test";
        
        // When
        var result = service.findAndTransform(items, target);
        
        // Then
        assertTrue(result.isPresent());
        assertEquals("Found: DEFAULT", result.get());
    }

    @Test
    @DisplayName("Should verify ProcessingResult sealed hierarchy")
    void testSealedClassHierarchy() {
        // Verify that ProcessingResult is properly sealed
        Class<?>[] permittedSubclasses = ModernService.ProcessingResult.class.getPermittedSubclasses();
        
        assertEquals(3, permittedSubclasses.length);
        assertTrue(Arrays.asList(permittedSubclasses).contains(ModernService.SuccessResult.class));
        assertTrue(Arrays.asList(permittedSubclasses).contains(ModernService.ErrorResult.class));
        assertTrue(Arrays.asList(permittedSubclasses).contains(ModernService.PendingResult.class));
    }

    @Test
    @DisplayName("Should handle multiple async operations")
    void testMultipleAsyncOperations() throws ExecutionException, InterruptedException {
        // Given
        List<String> inputs = Arrays.asList("first", "second", "third");
        
        // When
        List<CompletableFuture<ModernService.ProcessingResult>> futures = inputs.stream()
            .map(service::asyncProcess)
            .toList();
        
        CompletableFuture<Void> allFutures = CompletableFuture.allOf(
            futures.toArray(new CompletableFuture[0])
        );
        
        allFutures.get(); // Wait for all to complete
        
        // Then
        for (int i = 0; i < futures.size(); i++) {
            ModernService.ProcessingResult result = futures.get(i).get();
            assertInstanceOf(ModernService.SuccessResult.class, result);
            assertEquals(inputs.get(i).toUpperCase(), ((ModernService.SuccessResult) result).data());
        }
    }
}