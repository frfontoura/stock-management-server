package com.stockmanagement.infra.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.stockmanagement.restapi.dto.DefaultErrorDTO;
import com.stockmanagement.restapi.dto.UserDTO;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

@AllArgsConstructor
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtil;
    private final ObjectMapper mapper;

    @Override
    public Authentication attemptAuthentication(final HttpServletRequest req, final HttpServletResponse res) throws AuthenticationException {
        try {
            final JwtRequest creds = new ObjectMapper().readValue(req.getInputStream(), JwtRequest.class);
            final UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(creds.getUsername(), creds.getPassword(), new ArrayList<>());
            final Authentication auth = authenticationManager.authenticate(authToken);
            return auth;
        } catch (final IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void unsuccessfulAuthentication(final HttpServletRequest request, final HttpServletResponse response, final AuthenticationException e) throws IOException, ServletException {
        final DefaultErrorDTO err = DefaultErrorDTO.builder()
                .status(HttpStatus.UNAUTHORIZED)
                .error(e.getMessage())
                .message("Invalid username or password")
                .path(request.getRequestURI())
                .showMessage(true)
                .build();

        final PrintWriter out = response.getWriter();
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        out.print(mapper.writeValueAsString(err));
        out.flush();
    }

    @Override
    protected void successfulAuthentication(final HttpServletRequest req, final HttpServletResponse response, final FilterChain chain, final Authentication auth) throws IOException, ServletException {
        final JwtUserDetails jwtUser = ((JwtUserDetails) auth.getPrincipal());
        final String username = jwtUser.getUsername();
        final String token = jwtUtil.generateToken(username);

        final Cookie cookie = new Cookie("Authorization", token);
        cookie.setHttpOnly(true);
        cookie.setMaxAge(jwtUtil.getExpiration());

        response.addCookie(cookie);

        final PrintWriter out = response.getWriter();
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        out.print(mapper.writeValueAsString(new UserDTO(jwtUser.getUser())));
        out.flush();
    }
}