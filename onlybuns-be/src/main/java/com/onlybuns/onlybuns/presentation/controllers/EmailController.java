package com.onlybuns.onlybuns.presentation.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.onlybuns.onlybuns.domain.services.EmailService;
import com.onlybuns.onlybuns.presentation.dtos.requests.EmailDto;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Email Controller", description = "Endpoints for email management.")
@RestController
@RequestMapping("/email")
public class EmailController extends BaseController {
    private EmailService emailService;

    @Autowired
    public EmailController(EmailService emailService) {
        this.emailService = emailService;
    }

    @Operation(summary = "Verify a user by their email", 
    description = "This endpoint allows for the verification of a user by their email. "
                + "The request body should contain the user's email. "
                + "Returns a message indicating the result of the verification.")
    @ApiResponse(responseCode = "200", description = "User registered successfully")
    @ApiResponse(responseCode = "400", description = "Invalid email provided")
    @ApiResponse(responseCode = "500", description = "Internal server error")
    @PostMapping("/verify")
    public ResponseEntity<String> verifyUser(@RequestBody EmailDto emailDto) {
        var result = emailService.verifyEmail(emailDto);
        return createResponse(result);
    }
}
