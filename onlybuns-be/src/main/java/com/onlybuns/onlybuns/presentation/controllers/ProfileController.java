package com.onlybuns.onlybuns.presentation.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.onlybuns.onlybuns.domain.serviceinterfaces.UserServiceInterface;
import com.onlybuns.onlybuns.presentation.dtos.requests.LoginUserDto;
import com.onlybuns.onlybuns.presentation.dtos.requests.RegisterUserDto;
import com.onlybuns.onlybuns.presentation.dtos.responses.UserDto;
import com.onlybuns.onlybuns.presentation.dtos.responses.UserLoginDto;

@RestController
@RequestMapping
public class ProfileController extends BaseController {
    private final UserServiceInterface userService;

    @Autowired
    public ProfileController(UserServiceInterface userService) {
        this.userService = userService;
    }
    
    /***
     * Registers a new user
     * Codes: 
     * - 200: User registered successfully
     * - 400: Invalid user data
     * - 409: User with the same email or username already exists
     * @param registerUserDto RegisterUserDto
     * @return UserLoginDto
     */
    @PostMapping("/auth/register")
    public ResponseEntity<UserLoginDto> registerUser(@RequestBody RegisterUserDto registerUserDto) {
        var result = userService.registerUser(registerUserDto);
        return createResponse(result);
    }

    /***
     * Logs in a user
     * Codes: 
     * - 200: User logged in successfully
     * - 404: User not found
     * - 401: Incorrect password
     * @param loginUserDto LoginUserDto
     * @return UserLoginDto
     */
    @PostMapping("/auth/login")
    public ResponseEntity<UserLoginDto> loginUser(@RequestBody LoginUserDto loginUserDto) {
        var result = userService.loginUser(loginUserDto);
        return createResponse(result);
    }

    /***
     * Gets a user by id
     * Codes: 
     * - 200: User found
     * - 404: User not found
     * @param id Long
     * @return UserDto
     */
    @GetMapping("/user/{id}")
    public ResponseEntity<UserDto> getUser(@PathVariable Long id) {
        var result = userService.getUser(id);
        return createResponse(result);
    }
}
