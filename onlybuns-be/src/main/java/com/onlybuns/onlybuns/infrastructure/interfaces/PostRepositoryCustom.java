package com.onlybuns.onlybuns.infrastructure.interfaces;

import java.util.List;

import com.onlybuns.onlybuns.domain.models.Comment;

public interface PostRepositoryCustom {
    void likePost(Long postId);
    public List<Comment> findAllComments();
}
