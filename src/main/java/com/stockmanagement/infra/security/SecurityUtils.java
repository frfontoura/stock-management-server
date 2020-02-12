package com.stockmanagement.infra.security;

import com.stockmanagement.domain.users.User;
import com.stockmanagement.domain.users.UserService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class SecurityUtils {

	private final UserService userService;

	/**
	 * Returns the logged user
	 * @return
	 */
	public User getCurrentUser() {
		final JwtUserDetails userDetails = (JwtUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		final String username = userDetails.getUsername();
		return userService.findByUsername(username)
				.orElseThrow(() -> new UsernameNotFoundException(username));
	}

}