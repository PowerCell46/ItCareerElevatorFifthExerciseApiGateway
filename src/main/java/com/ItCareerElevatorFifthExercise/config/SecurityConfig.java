package com.ItCareerElevatorFifthExercise.config;

import com.ItCareerElevatorFifthExercise.util.auth.JsonAccessDeniedHandler;
import com.ItCareerElevatorFifthExercise.util.auth.JwtRequestFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private final JwtRequestFilter jwtRequestFilter;
    private final JsonAccessDeniedHandler jsonAccessDeniedHandler;

    public SecurityConfig(
            @Lazy JwtRequestFilter jwtRequestFilter,
            JsonAccessDeniedHandler jsonAccessDeniedHandler
    ) {
        this.jwtRequestFilter = jwtRequestFilter;
        this.jsonAccessDeniedHandler = jsonAccessDeniedHandler;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                                .requestMatchers(
                                        "/api/auth/register",
                                        "/api/auth/login",
                                        "/internal/deliverMessageToReceiverThroughWebSocket", // TODO: Not sure if that's ok (can be accessed from outside also)
                                        "/internal/deliverMessageToReceiverThroughEmail" // TODO: Not sure if that's ok (can be accessed from outside also)
                                ).permitAll() // ! Also have to be added in JwtRequestFilter -> PUBLIC_ENDPOINTS
                                .requestMatchers(
                                        "/api/auth/assign-roles"
                                ).hasRole("ADMIN") // expects "ROLE_ADMIN"
//                        .requestMatchers(
//                                "/api/manage/**"
//                        ).hasAnyRole("MANAGER", "ADMIN") // expects ROLE_MANAGER or ROLE_ADMIN
                                .requestMatchers(
                                        "/ws-endpoint/**",
                                        "/ws/**",
                                        "/topic/**"
                                ).authenticated()
                                .anyRequest().authenticated()
                )
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .exceptionHandling(ex ->
                        ex.accessDeniedHandler(jsonAccessDeniedHandler)
                );

        http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
