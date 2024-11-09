package com.onlybuns.onlybuns.presentation.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.onlybuns.onlybuns.domain.models.HospitalInformation;
import com.onlybuns.onlybuns.domain.serviceinterfaces.HospitalServiceInterface;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Hospital Controller", description = "Endpoints for hospital management")
@RestController
@RequestMapping("/api/fetch-vets")
public class HospitalController extends BaseController {
    @Autowired
    private HospitalServiceInterface hospitalService;

    public HospitalController(HospitalServiceInterface hospitalService) {
        this.hospitalService = hospitalService;
    }

    @Operation(summary = "Open connection to node server", 
                description = "This endpoint allows all users to connect to node server")
    @GetMapping
    public ResponseEntity<List<HospitalInformation>> connectToNodeServer() {
        var connection = hospitalService.connectToNodeServer();
        return createResponse(connection);
    }
}
