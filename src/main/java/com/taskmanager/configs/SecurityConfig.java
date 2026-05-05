package com.taskmanager.configs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    @Autowired
    private JwtAuthFilter jwtAuthFilter;

    private static final String[] PUBLIC_PAGES = {
        "/", "/login", "/register", "/dashboard",
        "/login.html", "/register.html", "/index.html",
        "/css/**", "/js/**", "/images/*"
    };

    private static final String[] PUBLIC_API = {
        "/auth/login", "/auth/register"
    };

    private static final String[] SWAGGER_PATHS = {
        "/docs", "/docs/", "/docs/**", "/v3/api-docs", "/v3/api-docs/**",
        "/swagger-ui/**", "/swagger-ui.html", "/webjars/**"
    };

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .sessionManagement(session ->
                session.sessionCreationPolicy(
                    org.springframework.security.config.http.SessionCreationPolicy.STATELESS)
            )
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(PUBLIC_PAGES).permitAll()
                .requestMatchers(PUBLIC_API).permitAll()
                .requestMatchers(SWAGGER_PATHS).permitAll()
                .requestMatchers("/tasks/**").authenticated()
                .anyRequest().authenticated()
            )
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
