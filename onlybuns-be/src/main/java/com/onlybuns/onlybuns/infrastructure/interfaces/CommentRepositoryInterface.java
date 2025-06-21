package com.onlybuns.onlybuns.infrastructure.interfaces;

import org.springframework.data.jpa.repository.JpaRepository;

import com.onlybuns.onlybuns.domain.models.Comment;


public interface CommentRepositoryInterface extends JpaRepository<Comment, Long> {

    
}
