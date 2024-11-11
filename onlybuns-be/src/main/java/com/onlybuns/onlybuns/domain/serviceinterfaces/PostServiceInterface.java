package com.onlybuns.onlybuns.domain.serviceinterfaces;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.onlybuns.onlybuns.core.misc.Result;
import com.onlybuns.onlybuns.presentation.dtos.requests.AddressDto;
import com.onlybuns.onlybuns.presentation.dtos.requests.UpdatePostDto;
import com.onlybuns.onlybuns.presentation.dtos.responses.GetAllPostDto;
import com.onlybuns.onlybuns.presentation.dtos.responses.PostAndLocationDto;
import com.onlybuns.onlybuns.presentation.dtos.responses.PostDto;

public interface PostServiceInterface {
    public Result<PostDto> likePost(Long postId, String userUsername);
    public Result<PostDto> unlikePost(Long postId, String userUsername);
    public Result<List<GetAllPostDto>> getAllPosts();
    public Result<List<GetAllPostDto>> getMyPosts(String userUsername);
    public Result<String> deletePost(Long postId, String userUsername);
    public Result<PostDto> updatePost(UpdatePostDto updatePostDto, String userUsername);
    public Result<List<PostAndLocationDto>> getNearbyPosts(double latitude, double longitude, Double radius);
    public Result<PostDto> createPost(String description, MultipartFile image, AddressDto address, String username);
}
