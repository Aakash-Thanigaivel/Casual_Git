package com.example.modernized.config;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Integration tests for application properties configuration.
 * Validates the migration from WebSphere to Spring Boot configuration.
 */
@SpringBootTest
@TestPropertySource(locations = "classpath:application.properties")
@DisplayName("Application Properties Configuration Tests")
class ApplicationPropertiesTest {

    @Autowired
    private Environment env;

    @Test
    @DisplayName("Should load server configuration properties")
    void testServerConfiguration() {
        // Then
        assertEquals("8080", env.getProperty("server.port"));
        assertEquals("/modernized-app", env.getProperty("server.servlet.context-path"));
        assertEquals("true", env.getProperty("server.compression.enabled"));
        assertEquals("true", env.getProperty("server.http2.enabled"));
    }

    @Test
    @DisplayName("Should load Spring application properties")
    void testSpringApplicationProperties() {
        // Then
        assertEquals("modernized-app", env.getProperty("spring.application.name"));
        assertEquals("jboss", env.getProperty("spring.profiles.active"));
    }

    @Test
    @DisplayName("Should load datasource JNDI configuration")
    void testDatasourceConfiguration() {
        // Then
        assertEquals("java:/datasources/ExampleDS", env.getProperty("spring.datasource.jndi-name"));
    }

    @Test
    @DisplayName("Should load JPA configuration properties")
    void testJpaConfiguration() {
        // Then
        assertEquals("validate", env.getProperty("spring.jpa.hibernate.ddl-auto"));
        assertEquals("false", env.getProperty("spring.jpa.show-sql"));
        assertEquals("org.hibernate.dialect.PostgreSQLDialect", 
            env.getProperty("spring.jpa.properties.hibernate.dialect"));
    }

    @Test
    @DisplayName("Should load HikariCP connection pool settings")
    void testHikariConfiguration() {
        // Then
        assertEquals("20", env.getProperty("spring.datasource.hikari.maximum-pool-size"));
        assertEquals("5", env.getProperty("spring.datasource.hikari.minimum-idle"));
        assertEquals("30000", env.getProperty("spring.datasource.hikari.connection-timeout"));
    }

    @Test
    @DisplayName("Should load transaction management settings")
    void testTransactionConfiguration() {
        // Then
        assertEquals("30", env.getProperty("spring.transaction.default-timeout"));
        assertEquals("true", env.getProperty("spring.transaction.rollback-on-commit-failure"));
    }

    @Test
    @DisplayName("Should load logging configuration")
    void testLoggingConfiguration() {
        // Then
        assertEquals("INFO", env.getProperty("logging.level.root"));
        assertEquals("DEBUG", env.getProperty("logging.level.com.example.modernized"));
        assertEquals("DEBUG", env.getProperty("logging.level.org.springframework.web"));
    }

    @Test
    @DisplayName("Should load actuator configuration")
    void testActuatorConfiguration() {
        // Then
        assertEquals("health,info,metrics,prometheus", 
            env.getProperty("management.endpoints.web.exposure.include"));
        assertEquals("always", env.getProperty("management.endpoint.health.show-details"));
        assertEquals("true", env.getProperty("management.metrics.export.prometheus.enabled"));
    }

    @Test
    @DisplayName("Should load JBoss specific properties")
    void testJBossProperties() {
        // Then
        assertEquals("localhost", env.getProperty("jboss.server.name"));
        assertEquals("8080", env.getProperty("jboss.http.port"));
        assertEquals("8443", env.getProperty("jboss.https.port"));
        assertEquals("9990", env.getProperty("jboss.management.port"));
    }

    @Test
    @DisplayName("Should load task execution configuration")
    void testTaskExecutionConfiguration() {
        // Then
        assertEquals("5", env.getProperty("spring.task.execution.pool.core-size"));
        assertEquals("10", env.getProperty("spring.task.execution.pool.max-size"));
        assertEquals("100", env.getProperty("spring.task.execution.pool.queue-capacity"));
    }
}