package com.example.Tatkal.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }



    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable) // stateless JSON API; re-enable if you add cookie sessions
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.POST, "/api/users").permitAll()   // signup
                        .requestMatchers("/api/paymentSuccess").permitAll()          // gateway webhook, signature-checked instead
                        .requestMatchers(HttpMethod.GET, "/api/trains/**", "/api/stations/**", "/api/trips/**").permitAll()
                        .requestMatchers(HttpMethod.DELETE, "/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/trains/**", "/api/stations/**",
                                "/api/trips/**", "/api/coaches/**", "/api/seats/**",
                                "/api/trainstops/**").hasRole("ADMIN")
                        .anyRequest().authenticated())
                .httpBasic(withDefaults -> {}); // swap for JWT once you add a login endpoint

        return http.build();
    }
}
