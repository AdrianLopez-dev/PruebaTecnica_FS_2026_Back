package org.example.infrastructure.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class ApiTokenFilter extends OncePerRequestFilter {

    private static final String API_PATH_PREFIX = "/api/";
    private static final String UNAUTHORIZED_BODY = "{\"error\":\"Unauthorized\"}";

    private final ApiProperties.Security security;

    public ApiTokenFilter(ApiProperties properties) {
        this.security = properties.security();
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        if (HttpMethod.OPTIONS.matches(request.getMethod())
                || !request.getRequestURI().startsWith(API_PATH_PREFIX)) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = request.getHeader(security.headerName());
        if (token == null || !token.equals(security.token())) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.setHeader(HttpHeaders.WWW_AUTHENTICATE, security.headerName());
            response.getWriter().write(UNAUTHORIZED_BODY);
            return;
        }

        filterChain.doFilter(request, response);
    }
}
