package com.stockmanagement.infra.security;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;

public class JwtAuthorizationFilter extends BasicAuthenticationFilter {

	private final JwtUtils jwtUtil;
	private final UserDetailsService userDetailsService;

	public JwtAuthorizationFilter(final AuthenticationManager authenticationManager, final JwtUtils jwtUtil,
								  final UserDetailsService userDetailsService) {
		super(authenticationManager);
		this.jwtUtil = jwtUtil;
		this.userDetailsService = userDetailsService;
	}

	@Override
	protected void doFilterInternal(final HttpServletRequest request, final HttpServletResponse response, final FilterChain chain)
			throws IOException, ServletException {

		if(request.getCookies() != null) {
			Arrays.asList(request.getCookies()).stream()
					.filter(cookie -> cookie.getName().equals("Authorization"))
					.findFirst()
					.ifPresent(cookie -> {
						final String header = cookie.getValue();
						final UsernamePasswordAuthenticationToken auth = getAuthentication(header);
						if (auth != null) {
							SecurityContextHolder.getContext().setAuthentication(auth);
						}
					});
		}

		chain.doFilter(request, response);
	}

	private UsernamePasswordAuthenticationToken getAuthentication(final String token) {
		if (jwtUtil.isValidToken(token)) {
			final String username = jwtUtil.getUsername(token);
			final UserDetails user = userDetailsService.loadUserByUsername(username);
			return new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
		}
		return null;
	}
}