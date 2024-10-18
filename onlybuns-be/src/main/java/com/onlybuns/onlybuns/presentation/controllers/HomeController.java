package com.onlybuns.onlybuns.presentation.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/public")
public class HomeController extends BaseController {
    @GetMapping
    public String home() {
        return "This is a public resource!";
    }

    @GetMapping("/protected")
    public String protectedHome() {
        return "Authentication and authorization successful! Welcome back, " + super.getLoggedInUsername() + "!";
    }
}