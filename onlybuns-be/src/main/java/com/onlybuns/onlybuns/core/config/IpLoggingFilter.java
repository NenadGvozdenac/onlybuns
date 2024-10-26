package com.onlybuns.onlybuns.core.config;

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

    public IpLoggingFilter(IpLoggingService ipLogService) {
        this.ipLogService = ipLogService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
                
        if ("/auth/login".equals(request.getRequestURI())) {
            String ipAddress = request.getRemoteAddr();
            ipLogService.logIpAddress(ipAddress);
        }

        filterChain.doFilter(request, response);
    }
}