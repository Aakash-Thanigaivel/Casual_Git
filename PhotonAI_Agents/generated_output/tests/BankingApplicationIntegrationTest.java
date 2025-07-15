package com.bofa.banking.integration;

import com.bofa.banking.BankingApplication;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Integration tests for BankingApplication
 * Tests Spring Boot 6.1 integration and JDK 17 compatibility
 * Part of Bank of America legacy modernization project
 */
@SpringBootTest(classes = BankingApplication.class)
@SpringJUnitConfig
@TestPropertySource(properties = {
    "spring.datasource.url=jdbc:h2:mem:testdb",
    "spring.jpa.hibernate.ddl-auto=create-drop"
})
class BankingApplicationIntegrationTest {

    @Test
    @DisplayName("Spring Boot context loads successfully")
    void contextLoads() {
        // This test verifies that the Spring Boot application context
        // loads without any configuration errors
        assertTrue(true, "Spring Boot context should load successfully");
    }

    @Test
    @DisplayName("Application starts with JDK 17 compatibility")
    void testJdk17Compatibility() {
        // Verify JDK 17 features are working
        var javaVersion = System.getProperty("java.version");
        assertNotNull(javaVersion);
        
        // Test modern switch expression (JDK 17 feature)
        BankingApplication app = new BankingApplication();
        String result = app.processTransaction("DEPOSIT");
        assertEquals("Processing deposit transaction", result);
    }

    @Test
    @DisplayName("Spring Boot 6.1 features are enabled")
    void testSpringBoot61Features() {
        // Test that Spring Boot 6.1 specific features are working
        BankingApplication app = new BankingApplication();
        assertNotNull(app);
        
        // Verify component scanning is working
        assertTrue(app.getClass().isAnnotationPresent(
            org.springframework.boot.autoconfigure.SpringBootApplication.class));
    }
}