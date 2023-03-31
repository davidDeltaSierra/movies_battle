package com.ada.tech.movies_battle.common;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
class SpringSecurityConfig {
    private final JwtHandler jwtHandler;

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http.csrf().disable()
                .cors().disable()
                .authorizeHttpRequests()
                .requestMatchers("/public/**").permitAll()
                .and()
                .authorizeHttpRequests()
                .anyRequest().authenticated()
                .and()
                .addFilterAfter(new JwtAuthFilter(jwtHandler), BasicAuthenticationFilter.class)
                .build();
    }
}
