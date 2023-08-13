package ar.juarce.webapp.filters;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class ResponseLoggerFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(ResponseLoggerFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // Format: "METHOD PATH: STATUS"
        logger.info("{} {}: {}", request.getMethod(), request.getRequestURI(), HttpStatus.resolve(response.getStatus()));
        filterChain.doFilter(request, response);
    }
}
