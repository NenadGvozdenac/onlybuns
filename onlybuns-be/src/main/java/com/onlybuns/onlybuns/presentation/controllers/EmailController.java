package com.onlybuns.onlybuns.presentation.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
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
    @ApiResponse(responseCode = "410", description = "User already verified")
    @ApiResponse(responseCode = "500", description = "Internal server error")
    @PostMapping("/verify")
    public ResponseEntity<String> verifyUser(@RequestBody EmailDto emailDto) {
        var result = emailService.verifyEmail(emailDto);
        return createResponse(result);
    }

    @Operation(summary = "Verify a user by their token", 
    description = "This endpoint allows for the verification of a user by their email. "
                + "The request body should contain the user's email. "
                + "Returns a message indicating the result of the verification.")
    @ApiResponse(responseCode = "200", description = "User registered successfully")
    @ApiResponse(responseCode = "400", description = "Invalid email provided")
    @ApiResponse(responseCode = "410", description = "User already verified")
    @ApiResponse(responseCode = "500", description = "Internal server error")
    @PostMapping("/verify/{email_token}")
    public ResponseEntity<String> verifyUser(@PathVariable String email_token) {
        var result = emailService.verifyEmail(email_token);
        return createResponse(result);
    }


    @Operation(summary = "Send a spam email",
    description = "This endpoint allows for sending a spam email to a user. "
                + "The request body should contain the user's email, subject, and message. "
                + "Returns a message indicating the result of the email sending.")
    @ApiResponse(responseCode = "200", description = "Email sent successfully")
    @ApiResponse(responseCode = "400", description = "Invalid email provided")
    @ApiResponse(responseCode = "500", description = "Internal server error")
    @PostMapping("/sendSpamEmail")
    public ResponseEntity<String> sendSpamEmail() {
        var result = emailService.sendSpamEmail("vojnicnemanjaa@gmail.com","New posts from your followers!","Hello " + "nigga" + ",\n\nThere have been " + "666" +
                " new posts by your followers. Check them out!");
        return createResponse(result);
    }
}