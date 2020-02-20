package com.stockmanagement.infra.security;

import com.stockmanagement.domain.users.User;
import com.stockmanagement.domain.users.UserService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
@AllArgsConstructor
public class JwtUserDetailsService implements UserDetailsService {

	private final UserService userService;

	@Override
	public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {
		final User user = userService.findByUsername(username)
				.orElseThrow(() -> new UsernameNotFoundException(username));

		return JwtUserDetails.builder()
				.user(user)
                .authorities(Arrays.asList(new SimpleGrantedAuthority("ROLE_" + user.getRole().name())))
                .build();
	}
}