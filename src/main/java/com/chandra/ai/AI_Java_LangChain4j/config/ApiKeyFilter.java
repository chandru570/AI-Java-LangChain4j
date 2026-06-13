package com.chandra.ai.AI_Java_LangChain4j.config;

import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.filter.OncePerRequestFilter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class ApiKeyFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(ApiKeyFilter.class);

    @Value("${app.security.api-key:}")
    private String configuredApiKey;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws IOException, jakarta.servlet.ServletException {
        // If configuredApiKey is empty, treat service as "open" (useful for dev). Configure before production.
        if (configuredApiKey != null && !configuredApiKey.isEmpty()) {
            String key = request.getHeader("X-API-KEY");
            if (key == null || !key.equals(configuredApiKey)) {
                logger.warn("Unauthorized request from {} to {}", request.getRemoteAddr(), request.getRequestURI());
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("Unauthorized");
                return;
            }
        }
        filterChain.doFilter(request, response);
    }
}
