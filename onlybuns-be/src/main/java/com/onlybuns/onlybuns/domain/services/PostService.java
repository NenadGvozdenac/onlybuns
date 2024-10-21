package com.onlybuns.onlybuns.domain.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.onlybuns.onlybuns.core.misc.Result;
import com.onlybuns.onlybuns.domain.models.Post;
import com.onlybuns.onlybuns.domain.models.User;
import com.onlybuns.onlybuns.domain.serviceinterfaces.PostServiceInterface;
import com.onlybuns.onlybuns.infrastructure.interfaces.PostRepositoryInterface;
import com.onlybuns.onlybuns.infrastructure.repositories.PostRepository;
import com.onlybuns.onlybuns.infrastructure.repositories.UserRepository;
import com.onlybuns.onlybuns.presentation.dtos.responses.PostDto;
import com.onlybuns.onlybuns.presentation.dtos.responses.UserDto;

import jakarta.transaction.Transactional;

@Service
public class PostService extends BaseService implements PostServiceInterface {
    
    @Autowired
    private PostRepository postRepository;

    @Autowired
    private PostRepositoryInterface postRepositoryjpa;

    @Autowired
    private UserRepository userRepository;

    @Override
    @Transactional
    public Result<PostDto> likePost(Long postId, String userUsername){

        var postOptional = postRepositoryjpa.findById(postId);

        if(postOptional.isPresent()){

            Post post = postOptional.get();
            var userOptional = userRepository.findByUsername(userUsername);
            
            
            if(userOptional.isPresent()){
                //provera da li je lajkovao vec
                User user = userOptional.get();
                System.out.println(user.getUsername());
                boolean userAlreadyLiked = post.getUsersThatLiked().stream()
                .anyMatch(us -> us.getId().equals(user.getId()));

                if (userAlreadyLiked) {
                    return Result.failure("User already liked this post.", 400);
                }
     
                post.getUsersThatLiked().add(user);
                post.setNumberOfLikes(post.getNumberOfLikes() + 1);
                postRepositoryjpa.save(post);

                PostDto postDto = new PostDto();
                postDto.setNumberOfLikes(post.getNumberOfLikes());
                postDto.setDesctiption(post.getDesctiption());
                postDto.setDateOfCreation(post.getDateOfCreation());

                return Result.success(postDto);       
            }else{
                return Result.failure("User that is supposed to do the liking is not found.", 404);
            }
        }else{
            return Result.failure("Post not found.", 403);
        }
    }

    @Override
    @Transactional
    public Result<List<PostDto>> getAllPosts(){

        try{
            List<Post> posts = postRepositoryjpa.findAll(Sort.by(Sort.Direction.DESC, "dateOfCreation"));

            List<PostDto> postDtos = posts.stream()
            .map(post -> {
                PostDto postDto = new PostDto();
                postDto.setId(post.getId());
                postDto.setDesctiption(post.getDesctiption());
                postDto.setDateOfCreation(post.getDateOfCreation());
                postDto.setNumberOfLikes(post.getNumberOfLikes());
                return postDto;
            })
            .collect(Collectors.toList());

            return Result.success(postDtos);
        
        }catch(Exception e){
            return Result.failure("Posts not found.", 404);
        }        
    }   
}
