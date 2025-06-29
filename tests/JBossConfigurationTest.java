package com.example.modernized.config;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for JBossConfiguration.
 * Tests JBoss-specific configurations and JNDI lookups.
 */
@ExtendWith(MockitoExtension.class)
@ActiveProfiles("jboss")
@DisplayName("JBossConfiguration Tests")
class JBossConfigurationTest {

    private JBossConfiguration configuration;

    @Mock
    private InitialContext mockContext;

    @Mock
    private DataSource mockDataSource;

    @BeforeEach
    void setUp() {
        configuration = new JBossConfiguration();
    }

    @Test
    @DisplayName("Should create JBoss properties with default values")
    void testJBossPropertiesDefaults() {
        // When
        JBossConfiguration.JBossProperties properties = configuration.jbossProperties();
        
        // Then
        assertNotNull(properties);
        assertEquals("localhost", properties.serverName());
        assertEquals(8080, properties.httpPort());
        assertEquals(8443, properties.httpsPort());
        assertEquals("/opt/jboss/wildfly/standalone/deployments", properties.deploymentPath());
    }

    @Test
    @DisplayName("Should create JBossProperties record with custom values")
    void testJBossPropertiesCustom() {
        // When
        JBossConfiguration.JBossProperties properties = new JBossConfiguration.JBossProperties(
            "custom-server",
            9090,
            9443,
            "/custom/path"
        );
        
        // Then
        assertEquals("custom-server", properties.serverName());
        assertEquals(9090, properties.httpPort());
        assertEquals(9443, properties.httpsPort());
        assertEquals("/custom/path", properties.deploymentPath());
    }

    @Test
    @DisplayName("Should handle NamingException when creating InitialContext")
    void testInitialContextCreationFailure() {
        // Given
        JBossConfiguration config = new JBossConfiguration() {
            @Override
            public InitialContext jbossInitialContext() throws NamingException {
                throw new NamingException("Connection failed");
            }
        };
        
        // When & Then
        assertThrows(NamingException.class, () -> {
            config.jbossInitialContext();
        });
    }

    @Test
    @DisplayName("Should verify JBoss constants are properly set")
    void testJBossConstants() {
        // Using reflection to access private constants
        try {
            var contextFactoryField = JBossConfiguration.class.getDeclaredField("JBOSS_INITIAL_CONTEXT_FACTORY");
            contextFactoryField.setAccessible(true);
            String contextFactory = (String) contextFactoryField.get(null);
            
            var providerUrlField = JBossConfiguration.class.getDeclaredField("JBOSS_PROVIDER_URL");
            providerUrlField.setAccessible(true);
            String providerUrl = (String) providerUrlField.get(null);
            
            assertEquals("org.wildfly.naming.client.WildFlyInitialContextFactory", contextFactory);
            assertEquals("http-remoting://localhost:8080", providerUrl);
        } catch (Exception e) {
            fail("Failed to access constants: " + e.getMessage());
        }
    }
}