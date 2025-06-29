package com.example.modernized.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for ModernService.
 * Tests JDK 17 features including sealed classes and pattern matching.
 */
@DisplayName("ModernService Tests")
class ModernServiceTest {

    private ModernService service;

    @BeforeEach
    void setUp() {
        service = new ModernService();
    }

    @Test
    @DisplayName("Should process SuccessResult correctly")
    void testProcessSuccessResult() {
        // Given
        ModernService.SuccessResult result = new ModernService.SuccessResult("Operation completed", "Data123");
        
        // When
        String processed = service.processResult(result);
        
        // Then
        assertEquals("Success: Operation completed, Data: Data123", processed);
    }

    @Test
    @DisplayName("Should process ErrorResult correctly")
    void testProcessErrorResult() {
        // Given
        ModernService.ErrorResult result = new ModernService.ErrorResult("Operation failed", "ERR_001");
        
        // When
        String processed = service.processResult(result);
        
        // Then
        assertEquals("Error [ERR_001]: Operation failed", processed);
    }

    @Test
    @DisplayName("Should process PendingResult correctly")
    void testProcessPendingResult() {
        // Given
        ModernService.PendingResult result = new ModernService.PendingResult("Processing", 120);
        
        // When
        String processed = service.processResult(result);
        
        // Then
        assertEquals("Pending: Processing, ETA: 120 seconds", processed);
    }

    @Test
    @DisplayName("Should filter null and blank items")
    void testProcessItemsFiltering() {
        // Given
        List<String> items = Arrays.asList("test", null, "  ", "valid", "", "   item   ");
        
        // When
        List<String> processed = service.processItems(items);
        
        // Then
        assertEquals(3, processed.size());
        assertTrue(processed.contains("SHORT: test"));
        assertTrue(processed.contains("MEDIUM: valid"));
        assertTrue(processed.contains("SHORT: item"));
    }

    @ParameterizedTest
    @ValueSource(strings = {"a", "ab", "abc"})
    @DisplayName("Should categorize short strings correctly")
    void testProcessItemsShortStrings(String input) {
        // Given
        List<String> items = Arrays.asList(input);
        
        // When
        List<String> processed = service.processItems(items);
        
        // Then
        assertEquals(1, processed.size());
        assertTrue(processed.get(0).startsWith("SHORT: "));
    }

    @ParameterizedTest
    @ValueSource(strings = {"test", "hello", "world123"})
    @DisplayName("Should categorize medium strings correctly")
    void testProcessItemsMediumStrings(String input) {
        // Given
        List<String> items = Arrays.asList(input);
        
        // When
        List<String> processed = service.processItems(items);
        
        // Then
        assertEquals(1, processed.size());
        assertTrue(processed.get(0).startsWith("MEDIUM: "));
    }

    @Test
    @DisplayName("Should categorize long strings correctly")
    void testProcessItemsLongStrings() {
        // Given
        List<String> items = Arrays.asList("verylongstring");
        
        // When
        List<String> processed = service.processItems(items);
        
        // Then
        assertEquals(1, processed.size());
        assertTrue(processed.get(0).startsWith("LONG: "));
    }

    @Test
    @DisplayName("Should handle empty list")
    void testProcessItemsEmptyList() {
        // Given
        List<String> items = new ArrayList<>();
        
        // When
        List<String> processed = service.processItems(items);
        
        // Then
        assertTrue(processed.isEmpty());
    }
}