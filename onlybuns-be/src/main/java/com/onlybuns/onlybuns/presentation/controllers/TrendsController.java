package com.onlybuns.onlybuns.presentation.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.onlybuns.onlybuns.domain.serviceinterfaces.TrendsServiceInterface;
import com.onlybuns.onlybuns.presentation.dtos.responses.TrendsDto;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Trends Controller", description = "Endpoints for trends management")
@RequestMapping("/trends")
@RestController
public class TrendsController extends BaseController {
    private TrendsServiceInterface trendsService;

    @Autowired
    public TrendsController(TrendsServiceInterface trendsService) {
        this.trendsService = trendsService;
    }

    @Operation(summary = "Get trends", 
                description = "This endpoint allows for the retrieval of trends.")
    @GetMapping
    public ResponseEntity<TrendsDto> getTrends() {
        var result = trendsService.getTrends();
        return createResponse(result);
    }
}
