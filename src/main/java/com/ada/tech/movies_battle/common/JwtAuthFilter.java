package com.ada.tech.movies_battle.common;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import java.io.IOException;
import java.util.List;

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
            var jwtPayload = jwtHandler.decode(request.getHeader("Authorization"));
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
