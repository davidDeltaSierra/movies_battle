package com.ada.tech.movies_battle.common;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import java.io.IOException;
import java.util.List;

import static java.util.Optional.ofNullable;

@Slf4j
@RequiredArgsConstructor
class JwtAuthFilter implements Filter {
    private final JwtHandler jwtHandler;

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws ServletException, IOException {
        auth(servletRequest);
        filterChain.doFilter(servletRequest, servletResponse);
    }

    private void auth(ServletRequest servletRequest) {
        try {
            var request = (HttpServletRequest) servletRequest;
            var ip = ofNullable(request.getHeader("X-Forwarded-For"))
                    .orElse(request.getRemoteAddr());
            log.info("Auth attempt with ip: {}", ip);
            var token = ofNullable(request.getHeader("Authorization"))
                    .orElseThrow(() -> new RuntimeException("Missing token"));
            var jwtPayload = jwtHandler.decode(token);
            log.info("Auth success: {}", jwtPayload);
            SecurityContextHolder.getContext().setAuthentication(
                    new UsernamePasswordAuthenticationToken(
                            jwtPayload,
                            null,
                            List.of()
                    )
            );
        } catch (Exception exception) {
            log.debug("Failed in auth token: {}", exception.getMessage());
        }
    }
}
