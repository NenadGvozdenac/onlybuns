package com.onlybuns.onlybuns.presentation.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.onlybuns.onlybuns.core.misc.Result;

public class BaseController {
    public <T> ResponseEntity<T> createResponse(Result<T> result) {
        // Determine the HTTP status based on the success of the operation
        HttpStatus status = result.isSuccess() ? HttpStatus.OK : HttpStatus.valueOf(result.getCode());

        // Return a ResponseEntity with the Result and the appropriate status
        return new ResponseEntity<>(result.getData(), status);
    }

    public String getLoggedInUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName();
    }
}
