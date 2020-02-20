package com.stockmanagement.infra.security;

import org.springframework.stereotype.Component;

/**
 * Class used in the @PreAuthorize annotation, to check if a user owns a resource.
 *
 * @author frfontoura
 * @version 1.0 11/02/2020
 */
@Component
public class UserOwner {

    /**
     * Checks whether the logged in user has the same userId
     *
     * @param principal
     * @param userId
     * @return
     */
    public boolean isOwner(final Object principal, final int userId) {
        return ((JwtUserDetails) principal).getUser().getId().equals(userId);
    }

}