package com.onlybuns.onlybuns.presentation.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.onlybuns.onlybuns.domain.serviceinterfaces.AdminAnalyticsInterface;
import com.onlybuns.onlybuns.presentation.dtos.responses.AdminAnalyticsDto;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Admin Analytics Controller", description = "Endpoints for admin analytics")
@RestController
@RequestMapping("/analytics")
public class AdminAnalyticsController extends BaseController {
    
    @Autowired
    private final AdminAnalyticsInterface adminAnalyticsService;

    public AdminAnalyticsController(AdminAnalyticsInterface adminAnalyticsService) {
        this.adminAnalyticsService = adminAnalyticsService;
    }

    @Operation(summary = "Get admin analytics", description = "This endpoint allows an admin to get analytics")
    @ApiResponse(responseCode = "403", description = "User not found.")
    @ApiResponse(responseCode = "405", description = "User is not an admin.")
    @GetMapping
    public ResponseEntity<AdminAnalyticsDto> getAdminAnalytics() {
        var result = adminAnalyticsService.getAdminAnalytics(getLoggedInUsername());
        return createResponse(result);
    }
}
