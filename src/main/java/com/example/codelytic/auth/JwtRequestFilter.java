package com.example.codelytic.auth;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtRequestFilter extends OncePerRequestFilter {

    private final UserDetailsService userDetailsService;
    @Autowired
    private JWTService jwtTokenUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
            FilterChain chain)
            throws ServletException, IOException {

        String userEmail = null;
        String jwtToken = null;

        final String header = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (header == null) {
            log.info(
                    "JwtRequestFilter.doFilterInternal() - header is null");

        } else if (header.startsWith("Bearer ")) {
            jwtToken = header.substring(7);
            try {
                userEmail = jwtTokenUtil.getUsernameFromToken(jwtToken);
            } catch (IllegalArgumentException e) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Unable to get JWT Token");
                System.out.println(
                        "JwtRequestFilter.doFilterInternal() - Unable to get JWT Token");
                return;
            } catch (ExpiredJwtException e) {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "JWT Token has expired");
                System.out.println(
                        "JwtRequestFilter.doFilterInternal() - JWT Token has expired");
                return;
            } catch (Exception e) {
                response.sendError(
                        HttpServletResponse.SC_UNAUTHORIZED,
                        "unknown error occurred");
                System.out.println(
                        "JwtRequestFilter.doFilterInternal() - unknown error occurred");
                System.out.println(e.getMessage());
                return;
            }
        } else {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "JWT Token does not begin with Bearer String");
            return;
        }
        if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail);
            System.out.println(
                    "JwtRequestFilter.doFilterInternal() - userDetails: " + userDetails.getUsername());
            if (jwtTokenUtil.validateToken(jwtToken, userDetails)) {

                log.info("JwtRequestFilter.doFilterInternal() - userEmail: {}", userEmail);
                log.info("JwtRequestFilter.doFilterInternal() - userRoles: {}", userDetails.getAuthorities());

                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities());

                usernamePasswordAuthenticationToken
                        .setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            }
        }
        try {
            // response.setHeader("Access-Control-Allow-Origin", "*");
            // response.setHeader("Access-Control-Allow-Methods", "*");
            // response.setHeader("Access-Control-Allow-Headers", "*");

            chain.doFilter(request, response);
        } catch (AccessDeniedException e) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Access Denied");
        }

    }

}