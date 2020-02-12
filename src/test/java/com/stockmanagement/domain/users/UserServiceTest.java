package com.stockmanagement.domain.users;

import com.stockmanagement.infra.exception.StockManagementException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * @author frfontoura
 * @version 1.0 24/12/2019
 */
@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository repository;

    @InjectMocks
    private UserService service;

    @Test
    @DisplayName("should find a user by username")
    void findByUsername() {
        final String username = "someUsername";
        final User user = User.builder()
                .id(123)
                .username(username)
                .build();
        when(repository.findByUsername(username)).thenReturn(Optional.of(user));

        final Optional<User> actual = service.findByUsername(username);
        assertNotNull(actual);
        assertEquals(user, actual.get());
        verify(repository, times(1)).findByUsername(username);
    }

    @Test
    @DisplayName("should find a user by id")
    void findByIdTest() {
        final Integer userId = 30;
        final User user = User.builder()
                .id(userId)
                .username("username")
                .build();
        when(repository.findById(userId)).thenReturn(Optional.of(user));

        final User actual = service.findById(userId);
        assertNotNull(actual);
        assertEquals(user, actual);
        verify(repository, times(1)).findById(userId);
    }

    @Test
    @DisplayName("should return error when searching for a nonexistent user")
    void findByIdNonExistentTest() {
        final Integer userId = 789;
        when(repository.findById(userId)).thenReturn(Optional.ofNullable(null));

        assertThrows(StockManagementException.class, () -> {
            service.findById(userId);
        });

        verify(repository, times(1)).findById(userId);
    }

}