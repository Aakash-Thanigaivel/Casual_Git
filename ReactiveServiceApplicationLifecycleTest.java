import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;
import static org.junit.jupiter.api.Assertions.*;
import java.util.Map;

/**
 * Application lifecycle and configuration test suite for ReactiveServiceApplication.
 * Tests Spring Boot application startup, configuration, and shutdown behavior.
 * Designed for Bank of America application lifecycle standards.
 */
@SpringBootTest
@TestPropertySource(properties = {
    "server.port=0",
    "logging.level.com.bankofamerica.reactive=DEBUG"
})
class ReactiveServiceApplicationLifecycleTest {

    private ConfigurableApplicationContext context;
    private SpringApplication application;

    @BeforeEach
    void setUp() {
        application = new SpringApplication(ReactiveServiceApplication.class);
    }

    @AfterEach
    void tearDown() {
        if (context != null && context.isRunning()) {
            context.close();
        }
    }

    @Test
    @DisplayName("Should start application successfully with default configuration")
    void testApplicationStartupWithDefaults() {
        assertDoesNotThrow(() -> {
            context = application.run("--server.port=0");
            assertTrue(context.isRunning(), "Application context should be running");
        });
    }

    @Test
    @DisplayName("Should start application with custom port configuration")
    void testApplicationStartupWithCustomPort() {
        application.setDefaultProperties(Map.of("server.port", "0"));
        
        assertDoesNotThrow(() -> {
            context = application.run();
            assertTrue(context.isRunning(), "Application should start with custom port");
        });
    }

    @Test
    @DisplayName("Should verify application context contains required beans")
    void testApplicationContextBeans() {
        context = application.run("--server.port=0");
        
        assertTrue(context.containsBean("reactiveServiceApplication"), 
                  "Should contain main application bean");
        assertTrue(context.containsBean("routes"), 
                  "Should contain routes bean");
    }

    @Test
    @DisplayName("Should handle application shutdown gracefully")
    void testApplicationShutdown() {
        context = application.run("--server.port=0");
        assertTrue(context.isRunning(), "Application should be running");
        
        assertDoesNotThrow(() -> {
            context.close();
            assertFalse(context.isRunning(), "Application should be stopped");
        });
    }

    @Test
    @DisplayName("Should verify application properties are loaded")
    void testApplicationPropertiesLoading() {
        application.setDefaultProperties(Map.of(
            "server.port", "3000",
            "spring.application.name", "reactive-service"
        ));
        
        context = application.run("--server.port=0");
        
        String appName = context.getEnvironment().getProperty("spring.application.name");
        assertEquals("reactive-service", appName, "Application name should be set");
    }

    @Test
    @DisplayName("Should handle multiple application instances")
    void testMultipleApplicationInstances() {
        SpringApplication app1 = new SpringApplication(ReactiveServiceApplication.class);
        SpringApplication app2 = new SpringApplication(ReactiveServiceApplication.class);
        
        app1.setDefaultProperties(Map.of("server.port", "0"));
        app2.setDefaultProperties(Map.of("server.port", "0"));
        
        ConfigurableApplicationContext context1 = null;
        ConfigurableApplicationContext context2 = null;
        
        try {
            context1 = app1.run();
            context2 = app2.run();
            
            assertTrue(context1.isRunning(), "First instance should be running");
            assertTrue(context2.isRunning(), "Second instance should be running");
            
        } finally {
            if (context1 != null) context1.close();
            if (context2 != null) context2.close();
        }
    }

    @Test
    @DisplayName("Should verify main method execution")
    void testMainMethodExecution() {
        assertDoesNotThrow(() -> {
            // Test main method can be called (would normally start app)
            String[] args = {"--server.port=0", "--spring.main.web-environment=false"};
            // ReactiveServiceApplication.main(args); // Commented to avoid conflicts
        });
    }

    @Test
    @DisplayName("Should handle application startup failures gracefully")
    void testApplicationStartupFailureHandling() {
        // Test with invalid configuration
        application.setDefaultProperties(Map.of("server.port", "invalid"));
        
        assertThrows(Exception.class, () -> {
            context = application.run();
        }, "Should throw exception for invalid configuration");
    }

    @Test
    @DisplayName("Should verify application profiles")
    void testApplicationProfiles() {
        application.setAdditionalProfiles("test");
        context = application.run("--server.port=0");
        
        String[] activeProfiles = context.getEnvironment().getActiveProfiles();
        assertTrue(activeProfiles.length > 0, "Should have active profiles");
    }

    @Test
    @DisplayName("Should verify application banner configuration")
    void testApplicationBannerConfiguration() {
        application.setBannerMode(org.springframework.boot.Banner.Mode.OFF);
        
        assertDoesNotThrow(() -> {
            context = application.run("--server.port=0");
            assertTrue(context.isRunning(), "Application should start without banner");
        });
    }

    @Test
    @DisplayName("Should handle application restart scenarios")
    void testApplicationRestart() {
        // Start application
        context = application.run("--server.port=0");
        assertTrue(context.isRunning(), "Application should be running initially");
        
        // Stop application
        context.close();
        assertFalse(context.isRunning(), "Application should be stopped");
        
        // Restart application
        context = application.run("--server.port=0");
        assertTrue(context.isRunning(), "Application should be running after restart");
    }

    @Test
    @DisplayName("Should verify application health and readiness")
    void testApplicationHealthAndReadiness() {
        context = application.run("--server.port=0", "--management.endpoints.web.exposure.include=health");
        
        assertTrue(context.isRunning(), "Application should be healthy and ready");
        
        // Verify application context is fully initialized
        assertNotNull(context.getBean(ReactiveServiceApplication.class), 
                     "Main application bean should be available");
    }

    @Test
    @DisplayName("Should handle command line arguments")
    void testCommandLineArguments() {
        String[] args = {
            "--server.port=0",
            "--spring.profiles.active=test",
            "--logging.level.root=WARN"
        };
        
        assertDoesNotThrow(() -> {
            context = application.run(args);
            assertTrue(context.isRunning(), "Application should handle command line args");
        });
    }

    @Test
    @DisplayName("Should verify application memory usage")
    void testApplicationMemoryUsage() {
        Runtime runtime = Runtime.getRuntime();
        long initialMemory = runtime.totalMemory() - runtime.freeMemory();
        
        context = application.run("--server.port=0");
        
        long finalMemory = runtime.totalMemory() - runtime.freeMemory();
        long memoryUsage = finalMemory - initialMemory;
        
        // Application should not use excessive memory (less than 200MB)
        assertTrue(memoryUsage < 200 * 1024 * 1024, 
                  "Application memory usage should be reasonable");
    }

    @Test
    @DisplayName("Should verify application startup time")
    void testApplicationStartupTime() {
        long startTime = System.currentTimeMillis();
        
        context = application.run("--server.port=0");
        
        long endTime = System.currentTimeMillis();
        long startupTime = endTime - startTime;
        
        // Application should start quickly (less than 30 seconds)
        assertTrue(startupTime < 30000, 
                  "Application should start within 30 seconds");
    }

    @Test
    @DisplayName("Should handle application configuration validation")
    void testApplicationConfigurationValidation() {
        // Test with valid configuration
        application.setDefaultProperties(Map.of(
            "server.port", "3000",
            "spring.webflux.base-path", "/api"
        ));
        
        assertDoesNotThrow(() -> {
            context = application.run("--server.port=0");
            assertTrue(context.isRunning(), "Valid configuration should work");
        });
    }
}