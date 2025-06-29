package com.example.modernized.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for UserService.
 * Tests the modernized service converted from WebSphere EJB.
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("UserService Tests")
class UserServiceTest {

    @Mock
    private DataSource mockDataSource;

    @Mock
    private Connection mockConnection;

    @Mock
    private PreparedStatement mockPreparedStatement;

    @Mock
    private ResultSet mockResultSet;

    private UserService userService;

    @BeforeEach
    void setUp() throws SQLException {
        userService = new UserService(mockDataSource);
        when(mockDataSource.getConnection()).thenReturn(mockConnection);
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockConnection.prepareStatement(anyString(), anyInt())).thenReturn(mockPreparedStatement);
    }

    @Test
    @DisplayName("Should create User record with valid data")
    void testUserRecordCreation() {
        // When
        UserService.User user = new UserService.User(1L, "testuser", "test@example.com", UserService.UserStatus.ACTIVE);
        
        // Then
        assertEquals(1L, user.id());
        assertEquals("testuser", user.username());
        assertEquals("test@example.com", user.email());
        assertEquals(UserService.UserStatus.ACTIVE, user.status());
    }

    @Test
    @DisplayName("Should throw exception for null username")
    void testUserRecordNullUsername() {
        // When & Then
        assertThrows(IllegalArgumentException.class, () -> {
            new UserService.User(1L, null, "test@example.com", UserService.UserStatus.ACTIVE);
        });
    }

    @Test
    @DisplayName("Should throw exception for invalid email")
    void testUserRecordInvalidEmail() {
        // When & Then
        assertThrows(IllegalArgumentException.class, () -> {
            new UserService.User(1L, "testuser", "invalid-email", UserService.UserStatus.ACTIVE);
        });
    }

    @Test
    @DisplayName("Should find user by ID successfully")
    void testFindByIdSuccess() throws SQLException {
        // Given
        Long userId = 1L;
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true);
        when(mockResultSet.getLong("id")).thenReturn(userId);
        when(mockResultSet.getString("username")).thenReturn("testuser");
        when(mockResultSet.getString("email")).thenReturn("test@example.com");
        when(mockResultSet.getString("status")).thenReturn("ACTIVE");
        
        // When
        Optional<UserService.User> result = userService.findById(userId);
        
        // Then
        assertTrue(result.isPresent());
        assertEquals(userId, result.get().id());
        assertEquals("testuser", result.get().username());
        verify(mockPreparedStatement).setLong(1, userId);
    }

    @Test
    @DisplayName("Should return empty when user not found")
    void testFindByIdNotFound() throws SQLException {
        // Given
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(false);
        
        // When
        Optional<UserService.User> result = userService.findById(999L);
        
        // Then
        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("Should find all active users")
    void testFindActiveUsers() throws SQLException {
        // Given
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true, true, false);
        when(mockResultSet.getLong("id")).thenReturn(1L, 2L);
        when(mockResultSet.getString("username")).thenReturn("user1", "user2");
        when(mockResultSet.getString("email")).thenReturn("user1@example.com", "user2@example.com");
        when(mockResultSet.getString("status")).thenReturn("ACTIVE", "ACTIVE");
        
        // When
        List<UserService.User> users = userService.findActiveUsers();
        
        // Then
        assertEquals(2, users.size());
        assertEquals("user1", users.get(0).username());
        assertEquals("user2", users.get(1).username());
        verify(mockPreparedStatement).setString(1, "ACTIVE");
    }

    @Test
    @DisplayName("Should update user status successfully")
    void testUpdateUserStatusSuccess() throws SQLException {
        // Given
        when(mockPreparedStatement.executeUpdate()).thenReturn(1);
        
        // When
        boolean result = userService.updateUserStatus(1L, UserService.UserStatus.INACTIVE);
        
        // Then
        assertTrue(result);
        verify(mockPreparedStatement).setString(1, "INACTIVE");
        verify(mockPreparedStatement).setLong(2, 1L);
    }

    @Test
    @DisplayName("Should return false when user not found for update")
    void testUpdateUserStatusNotFound() throws SQLException {
        // Given
        when(mockPreparedStatement.executeUpdate()).thenReturn(0);
        
        // When
        boolean result = userService.updateUserStatus(999L, UserService.UserStatus.ACTIVE);
        
        // Then
        assertFalse(result);
    }

    @Test
    @DisplayName("Should activate users in batch")
    void testActivateUsersBatch() throws SQLException {
        // Given
        List<Long> userIds = Arrays.asList(1L, 2L, 3L);
        when(mockPreparedStatement.executeUpdate()).thenReturn(1, 0, 1);
        
        // When
        UserService.BatchResult result = userService.activateUsers(userIds);
        
        // Then
        assertEquals(2, result.successCount());
        assertEquals(1, result.failureCount());
        assertEquals(1, result.errors().size());
        assertTrue(result.errors().get(0).contains("User not found: 2"));
    }

    @Test
    @DisplayName("Should verify UserStatus enum values")
    void testUserStatusEnum() {
        // Then
        assertEquals("Active", UserService.UserStatus.ACTIVE.getDisplayName());
        assertEquals("Inactive", UserService.UserStatus.INACTIVE.getDisplayName());
        assertEquals("Suspended", UserService.UserStatus.SUSPENDED.getDisplayName());
        assertEquals("Pending Activation", UserService.UserStatus.PENDING.getDisplayName());
    }
}