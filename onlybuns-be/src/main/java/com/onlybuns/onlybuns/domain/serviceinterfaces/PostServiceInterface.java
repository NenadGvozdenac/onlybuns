package com.onlybuns.onlybuns.domain.serviceinterfaces;

import java.util.List;

import com.onlybuns.onlybuns.core.misc.Result;
import com.onlybuns.onlybuns.presentation.dtos.responses.PostDto;
import com.onlybuns.onlybuns.presentation.dtos.responses.UserDto;

public interface PostServiceInterface {
    public Result<PostDto> likePost(Long postId, String userUsername);
    public Result<List<PostDto>> getAllPosts();
}
