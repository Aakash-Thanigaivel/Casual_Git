package com.example.modernized;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for ModernizedApplication class.
 * Tests Spring Boot 6.1 application startup and bean configuration.
 */
@SpringBootTest
@DisplayName("ModernizedApplication Tests")
class ModernizedApplicationTest {

    @Autowired
    private ApplicationContext context;

    @Test
    @DisplayName("Should load application context successfully")
    void contextLoads() {
        assertNotNull(context);
    }

    @Test
    @DisplayName("Should create ApplicationInfo bean with valid properties")
    void testApplicationInfoBean() {
        // Given
        ModernizedApplication app = new ModernizedApplication();
        
        // When
        ModernizedApplication.ApplicationInfo info = app.applicationInfo();
        
        // Then
        assertNotNull(info);
        assertEquals("Modernized Application", info.name());
        assertEquals("1.0.0", info.version());
        assertNotNull(info.javaVersion());
        assertTrue(info.javaVersion().startsWith("17"));
    }

    @Test
    @DisplayName("Should throw exception for null application name")
    void testApplicationInfoWithNullName() {
        // When & Then
        assertThrows(IllegalArgumentException.class, () -> {
            new ModernizedApplication.ApplicationInfo(null, "1.0.0", "17");
        });
    }

    @Test
    @DisplayName("Should throw exception for blank application name")
    void testApplicationInfoWithBlankName() {
        // When & Then
        assertThrows(IllegalArgumentException.class, () -> {
            new ModernizedApplication.ApplicationInfo("   ", "1.0.0", "17");
        });
    }
}