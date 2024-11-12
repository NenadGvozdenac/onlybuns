package com.onlybuns.onlybuns.presentation.controllers;

import org.springframework.context.annotation.Description;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Home Controller")
@Description("Endpoints for public resources")
@RestController
@RequestMapping("/public")
public class HomeController extends BaseController {
    
    @GetMapping
    @Description("Endpoint for public resources")
    public String home() {
        return "This is a public resource!";
    }

    @GetMapping("/protected")
    @Description("Endpoint for protected resources")
    public String protectedHome() {
        return "Authentication and authorization successful! Welcome back, " + super.getLoggedInUsername() + "!";
    }
}