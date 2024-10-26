package com.onlybuns.onlybuns.core.config;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.onlybuns.onlybuns.domain.services.IpLoggingService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@Component
public class IpLoggingFilter extends OncePerRequestFilter {

    @Autowired
    private final IpLoggingService ipLogService;

    private List<String> loggingUris = List.of("/auth/login");

    public IpLoggingFilter(IpLoggingService ipLogService) {
        this.ipLogService = ipLogService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
                
        String uri = request.getRequestURI();

        if (loggingUris.contains(uri)) {
            ipLogService.logIpAddress(request.getRemoteAddr());
        }

        // If user has tried to access 5 endpoints in the last minute, block them
        if (ipLogService.isBlocked(request.getRemoteAddr())) {
            response.setStatus(429);
            return;
        }

        filterChain.doFilter(request, response);
    }
}