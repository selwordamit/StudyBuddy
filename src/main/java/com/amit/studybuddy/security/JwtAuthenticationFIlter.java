package com.amit.studybuddy.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
@Slf4j
// Custom JWT filter that runs once per request to authenticate the user
public class JwtAuthenticationFIlter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsServiceImpl userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        try {
            String token = extractToken(request);

            if (token != null) {
                String username = jwtService.extractUsername(token);

                // Only proceed if username was extracted and no authentication exists yet
                if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                    UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                    if (jwtService.isTokenValid(token, userDetails)) {
                        // Create authentication token based on user's details
                        UsernamePasswordAuthenticationToken authentication =
                                new UsernamePasswordAuthenticationToken(
                                        userDetails,
                                        null,
                                        userDetails.getAuthorities()
                                );
                        authentication.setDetails(
                                new WebAuthenticationDetailsSource().buildDetails(request)
                        );
                        // Set authentication into SecurityContext (makes the user "logged in" for the request)
                        SecurityContextHolder.getContext().setAuthentication(authentication);

                        // Set userId on request so controllers can access it
                        if (userDetails instanceof UserDetailsImpl userDetailsImpl) {
                            request.setAttribute("userId", userDetailsImpl.getUser().getId());
                        }

                        log.info("User authenticated: {}", username);
                    }
                }
            }

        } catch (Exception e) {
            log.warn("JWT authentication failed: {}", e.getMessage());
        }
        // Continue to the next filter in the chain
        filterChain.doFilter(request, response);
    }

    // Extracts the token from the Authorization header, if present
    private String extractToken(HttpServletRequest request) { // helper
        final String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }
        return null;
    }
}