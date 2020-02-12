package com.stockmanagement.domain.users;

import com.stockmanagement.infra.exception.StockManagementException;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Responsible class for the registration of a new user
 *
 * @author frfontoura
 * @version 1.0 24/12/2019
 */
@Service
@AllArgsConstructor
public class SignUpService {

    private final UserRepository repository;
    private final BCryptPasswordEncoder bCrypt;

    /**
     * Registers a new user and the password will be encrypted
     *
     * @param user User information
     * @return user - User with id
     */
    public User signUp(@NonNull final User user) {
        repository.findByUsername(user.getUsername()).ifPresent(u -> { throw new StockManagementException("User already exist"); });
        repository.findByEmail(user.getEmail()).ifPresent(u -> { throw new StockManagementException("User already exist"); });

        user.setPassword(bCrypt.encode(user.getPassword()));
        user.setRole(Role.REGULAR_USER);
        return repository.save(user);
    }
}
