package com.onlybuns.onlybuns.presentation.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.onlybuns.onlybuns.domain.serviceinterfaces.ProfileServiceInterface;
import com.onlybuns.onlybuns.presentation.dtos.requests.UpdateProfileDto;
import com.onlybuns.onlybuns.presentation.dtos.responses.ProfileDto;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Profile Controller", description = "Endpoints for profile management")
@RequestMapping("/profile")
@RestController
public class ProfileController extends BaseController {
    private ProfileServiceInterface profileService;

    @Autowired
    public ProfileController(ProfileServiceInterface profileService) {
        this.profileService = profileService;
    }

    @Operation(summary = "Get profile", description = "This endpoint allows for the retrieval of a user's profile. "
            + "The request should contain the username of the user whose profile is to be retrieved.")
    @ApiResponse(responseCode = "200", description = "Profile retrieved successfully")
    @ApiResponse(responseCode = "404", description = "User not found")
    @Parameter(name = "username", description = "Username of the user whose profile is to be retrieved", required = false)
    @GetMapping
    public ResponseEntity<ProfileDto> getProfile(@RequestParam(required = false) String username) {
        var result = profileService.getProfile(username == null ? getLoggedInUsername() : username);
        return createResponse(result);
    }

    @Operation(summary = "Update profile", description = "This endpoint allows for the update of a user's profile. "
            + "The request should contain the username of the user whose profile is to be updated.")
    @ApiResponse(responseCode = "200", description = "Profile updated successfully")
    @ApiResponse(responseCode = "400", description = "Invalid password")
    @ApiResponse(responseCode = "404", description = "User not found")
    @PutMapping
    public ResponseEntity<ProfileDto> updateProfile(@RequestBody UpdateProfileDto updateProfileDto) {
        var result = profileService.updateProfile(getLoggedInUsername(), updateProfileDto);
        return createResponse(result);
    }

    @Operation(summary = "Get verified profiles", description = "This endpoint allows for the retrieval of all verified profiles.")
    @ApiResponse(responseCode = "200", description = "Verified profiles retrieved successfully")
    @ApiResponse(responseCode = "401", description = "Unathorized")
    @GetMapping("/verifiedProfiles")
    public ResponseEntity<List<ProfileDto>> getVerifiedProfiles(@RequestParam(required = false) String name,
            @RequestParam(required = false) String surname,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) Integer minActivePosts,
            @RequestParam(required = false) Integer maxActivePosts,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = true) Integer page) {
        var result = profileService.getVerifiedProfiles(getLoggedInUsername(), name, surname, email, minActivePosts,
                maxActivePosts, sortBy, page);
        return createResponse(result);
    }

    @Operation(summary = "Follow user", description = "This endpoint allows for the user to follow another user. "
            + "The request should contain the username of the user to be followed.")
    @ApiResponse(responseCode = "200", description = "User followed successfully")
    @ApiResponse(responseCode = "404", description = "Users not found")
    @ApiResponse(responseCode = "409", description = "User already followed")
    @ApiResponse(responseCode = "429", description = "Too many follows in the last minute")
    @PutMapping("/follow")
    public ResponseEntity<String> followProfile(@RequestParam(required = true) String username) {
        var result = profileService.followProfile(getLoggedInUsername(), username);
        return createResponse(result);
    }

    @Operation(summary = "Unfollow user", description = "This endpoint allows the user to unfollow another user. "
            + "The request should contain the username of the user to be unfollowed.")
    @ApiResponse(responseCode = "200", description = "User unfollowed successfully")
    @ApiResponse(responseCode = "404", description = "Users not found")
    @ApiResponse(responseCode = "409", description = "User not following this user")
    @PutMapping("/unfollow")
    public ResponseEntity<String> unfollowProfile(@RequestParam(required = true) String username) {
        var result = profileService.unfollowProfile(getLoggedInUsername(), username);
        return createResponse(result);
    }
}
