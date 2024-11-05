package com.onlybuns.onlybuns.presentation.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.onlybuns.onlybuns.domain.serviceinterfaces.PostServiceInterface;
import com.onlybuns.onlybuns.presentation.dtos.requests.UpdatePostDto;
import com.onlybuns.onlybuns.presentation.dtos.responses.GetAllPostDto;
import com.onlybuns.onlybuns.presentation.dtos.responses.PostAndLocationDto;
import com.onlybuns.onlybuns.presentation.dtos.responses.PostDto;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Post Controller", description = "Endpoints for posts management")
@RestController
@RequestMapping("/post")
public class PostController extends BaseController {
    
    private final PostServiceInterface postService;

    @Autowired
    public PostController(PostServiceInterface postService){
        this.postService = postService;
    }

    @Operation(summary = "Like a post", 
               description = "This endpoint allows a user to like a post")
    @ApiResponse(responseCode = "403", description = "Post not found.")
    @ApiResponse(responseCode = "400", description = "User already liked this post.")
    @ApiResponse(responseCode = "404", description = "User that is supposed to do the liking is not found.")
    @PostMapping("/{id}/like")
    public ResponseEntity<PostDto> likePost(@PathVariable Long id){
        var result = postService.likePost(id, getLoggedInUsername());
        return createResponse(result);
    }

    @Operation(summary = "Get all posts in date order descending", 
               description = "This endpoint allows all users to get posts on their homepage")
    @ApiResponse(responseCode = "404", description = "Posts not found.")
    @GetMapping("/all")
    public ResponseEntity<List<GetAllPostDto>> getAllPosts(){
        var result = postService.getAllPosts();
        return createResponse(result);
    }

    @Operation(summary = "Get my posts in date order descending", 
               description = "This endpoint allows users to get posts their on their homepage")
    @ApiResponse(responseCode = "404", description = "Posts not found.")
    @GetMapping("/myposts")
    public ResponseEntity<List<GetAllPostDto>> getMyPosts(){
        var result = postService.getMyPosts(getLoggedInUsername());
        return createResponse(result);
    }

    @Operation(summary = "Delete post by id", 
               description = "This endpoint allows authors of the post to delete their post")
    @ApiResponse(responseCode = "404", description = "Posts doesn't exist")
    @ApiResponse(responseCode = "409", description = "User that wants to delete doesn't exist")
    @ApiResponse(responseCode = "403", description = "User is not the owner of the post")   
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePost(@PathVariable Long id){
        var result = postService.deletePost(id, getLoggedInUsername());
        return createResponse(result);
    }

    @Operation(summary = "Update post", 
               description = "This endpoint allows authors of the post to delete their post")
    @ApiResponse(responseCode = "404", description = "Posts doesn't exist")
    @ApiResponse(responseCode = "409", description = "User that wants to update post doesn't exist")
    @ApiResponse(responseCode = "403", description = "User is not the owner of the post")   
    @PutMapping
    public ResponseEntity<PostDto> updatePost(@RequestBody UpdatePostDto updatePostDto){
        var result = postService.updatePost(updatePostDto, getLoggedInUsername());
        return createResponse(result);
    }

    @Operation(summary = "Get nearby posts", 
               description = "This endpoint allows users to get posts nearby their location. Radius is default to 5 kms.")
    @GetMapping("/nearby")
    public ResponseEntity<List<PostAndLocationDto>> getNearbyPosts(
        @Parameter(description = "Latitude of the user's current location") @RequestParam double latitude, 
        @Parameter(description = "Longitude of the user's current location") @RequestParam double longitude,
        @Parameter(description = "Search radius in kilometers, default is 5 km") @RequestParam(required = false, defaultValue = "5") Double radius) {
    
        var result = postService.getNearbyPosts(latitude, longitude, radius);
        return createResponse(result);
    }
}
