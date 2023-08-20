package com.example.codelytic.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.example.codelytic.auth.JwtRequestFilter;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity

public class SecurityConfiguration {

    @Autowired
    private JwtRequestFilter jwtRequestFilter;

    @Autowired
    private final AuthenticationProvider authenticationProvider;

    /**
     * @param http
     * @return
     * @throws Exception
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        /*
         * Disable csrf
         */
        http = http.csrf(csrf -> csrf.disable());
        http = http.cors(cors -> cors.configurationSource(corsConfigurationSource()));

        /*
         * Set session to STATELESS
         */
        http = http.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        /*
         * Set unauthorized requests exception handler
         */
        // http = http.exceptionHandling(
        // exceptionHandling ->
        // exceptionHandling.authenticationEntryPoint(jwtAuthenticationEntryPoint));
        /*
         * Set permissions on endpoints
         */

        http.authorizeHttpRequests(
                authorizeRequests -> authorizeRequests
                        .requestMatchers("/authenticate").permitAll()
                        .requestMatchers("/swagger-ui/**").permitAll()
                        .requestMatchers("/api-docs/**").permitAll()
                        .requestMatchers("/user/register").permitAll()
                        .requestMatchers("/course/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/post/**").permitAll()
                        // .requestMatchers("/tags/**").permitAll()
                        // .requestMatchers("/**").permitAll()
                        .anyRequest().authenticated());
        http.authenticationProvider(authenticationProvider);

        /*
         * Add a filter to validate the tokens with every request
         */
        http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    CorsConfigurationSource corsConfigurationSource() {
        final var configuration = new CorsConfiguration();

        configuration.setAllowedOrigins(List.of("*"));
        configuration.setAllowedMethods(List.of("GET",
                "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setExposedHeaders(List.of("*"));

        final var source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }

}