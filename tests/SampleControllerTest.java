package com.example.modernized.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for SampleController.
 * Tests REST endpoints and JDK 17 features.
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("SampleController Tests")
class SampleControllerTest {

    private SampleController controller;

    @BeforeEach
    void setUp() {
        controller = new SampleController();
    }

    @Test
    @DisplayName("Should return sample with ACTIVE status for ID 1")
    void testGetSampleWithId1() {
        // When
        ResponseEntity<SampleController.SampleResponse> response = controller.getSample(1L);
        
        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1L, response.getBody().id());
        assertEquals("Sample 1", response.getBody().name());
        assertEquals("ACTIVE", response.getBody().status());
    }

    @Test
    @DisplayName("Should return sample with PENDING status for ID 2")
    void testGetSampleWithId2() {
        // When
        ResponseEntity<SampleController.SampleResponse> response = controller.getSample(2L);
        
        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("PENDING", response.getBody().status());
    }

    @Test
    @DisplayName("Should return sample with UNKNOWN status for other IDs")
    void testGetSampleWithUnknownId() {
        // When
        ResponseEntity<SampleController.SampleResponse> response = controller.getSample(99L);
        
        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("UNKNOWN", response.getBody().status());
    }

    @Test
    @DisplayName("Should create sample successfully")
    void testCreateSample() {
        // Given
        List<String> tags = Arrays.asList("tag1", "tag2", "tag3");
        SampleController.SampleRequest request = new SampleController.SampleRequest(
            "Test Sample",
            "Test Description",
            tags
        );
        
        // When
        ResponseEntity<SampleController.SampleResponse> response = controller.createSample(request);
        
        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1L, response.getBody().id());
        assertEquals("Test Sample", response.getBody().name());
        assertEquals("CREATED", response.getBody().status());
    }

    @Test
    @DisplayName("Should create SampleRequest record with all fields")
    void testSampleRequestRecord() {
        // Given
        List<String> tags = Arrays.asList("java", "spring", "boot");
        
        // When
        SampleController.SampleRequest request = new SampleController.SampleRequest(
            "Sample Name",
            "Sample Description",
            tags
        );
        
        // Then
        assertEquals("Sample Name", request.name());
        assertEquals("Sample Description", request.description());
        assertEquals(3, request.tags().size());
        assertTrue(request.tags().contains("java"));
    }
}