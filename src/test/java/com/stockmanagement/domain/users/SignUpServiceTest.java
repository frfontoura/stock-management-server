package com.stockmanagement.domain.users;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * @author frfontoura
 * @version 1.0 24/12/2019
 */
@ExtendWith(MockitoExtension.class)
public class SignUpServiceTest {

    @Mock
    private UserRepository repository;

    @Spy
    private final BCryptPasswordEncoder bCrypt = new BCryptPasswordEncoder();

    @InjectMocks
    private SignUpService service;

    @Test
    @DisplayName("Should throw an NullPointerException if the parameter is null")
    public void signUpNullTest() {
        assertThrows(NullPointerException.class, () -> {
            service.signUp(null);
        });
    }

    @Test
    @DisplayName("Should create a new user")
    public void signUpTest() {
        final User user = createUser(null);
        when(repository.save(user)).thenReturn(user);

        final User actual = service.signUp(user);

        assertNotNull(actual);
        assertEquals(user.getUsername(), actual.getUsername());
        assertEquals(user.getEmail(), actual.getEmail());
        assertEquals(Role.REGULAR_USER, actual.getRole());
        assertNotEquals("password", actual.getPassword(), "Password must be encrypted");

        verify(repository, times(1)).save(user);
    }

    private User createUser(final Integer id) {
        return User.builder()
                .id(id)
                .username("Test")
                .email("test@email.com")
                .password("password")
                .build();
    }
}