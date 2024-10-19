package com.onlybuns.onlybuns.presentation.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.onlybuns.onlybuns.domain.serviceinterfaces.UserServiceInterface;
import com.onlybuns.onlybuns.presentation.dtos.requests.LoginUserDto;
import com.onlybuns.onlybuns.presentation.dtos.requests.RegisterUserDto;
import com.onlybuns.onlybuns.presentation.dtos.responses.UserDto;
import com.onlybuns.onlybuns.presentation.dtos.responses.UserLoginDto;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Profile Controller", description = "Endpoints for user registration, login, and profile management")
@RestController
@RequestMapping("/auth")
public class ProfileController extends BaseController {

    private final UserServiceInterface userService;

    @Autowired
    public ProfileController(UserServiceInterface userService) {
        this.userService = userService;
    }
    
    @Operation(summary = "Register a new user", 
               description = "This endpoint allows for the registration of a new user. "
                           + "The request body should contain valid user data. "
                           + "Returns user info upon successful registration.")
    @ApiResponse(responseCode = "200", description = "User registered successfully")
    @ApiResponse(responseCode = "400", description = "Invalid user data provided")
    @ApiResponse(responseCode = "409", description = "User with the same email or username already exists")
    @ApiResponse(responseCode = "411", description = "Invalid email")
    @PostMapping("/register")
    public ResponseEntity<UserDto> registerUser(@RequestBody RegisterUserDto registerUserDto) {
        var result = userService.registerUser(registerUserDto);
        return createResponse(result);
    }

    @Operation(summary = "Login a user", 
               description = "This endpoint allows a user to log in by providing valid credentials. "
                           + "A token is returned upon successful authentication.")
    @ApiResponse(responseCode = "200", description = "User logged in successfully")
    @ApiResponse(responseCode = "401", description = "Incorrect password")
    @ApiResponse(responseCode = "403", description = "User is not verified")
    @ApiResponse(responseCode = "409", description = "User is not active")
    @ApiResponse(responseCode = "404", description = "User not found")
    @PostMapping("/login")
    public ResponseEntity<UserLoginDto> loginUser(@RequestBody LoginUserDto loginUserDto) {
        var result = userService.loginUser(loginUserDto);
        return createResponse(result);
    }
}
