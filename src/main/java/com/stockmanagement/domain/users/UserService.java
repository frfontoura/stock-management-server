package com.stockmanagement.domain.users;

import com.stockmanagement.infra.exception.StockManagementException;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Responsible for services related to the user entity
 *
 * @author frfontoura
 * @version 1.0 24/12/2019
 */
@Service
@AllArgsConstructor
public class UserService {

    private final UserRepository repository;

    /**
     * Search for a user by id
     *
     * @param userId
     * @return
     */
    @PreAuthorize("@userOwner.isOwner(principal, #userId)")
    public User findById(final int userId) {
        return repository.findById(userId).orElseThrow(() -> new StockManagementException("User with id:" + userId + " not found."));
    }

    /**
     * Finds a user by username
     *
     * @param username
     * @return
     */
    public Optional<User> findByUsername(final String username) {
        return repository.findByUsername(username);
    }
}
