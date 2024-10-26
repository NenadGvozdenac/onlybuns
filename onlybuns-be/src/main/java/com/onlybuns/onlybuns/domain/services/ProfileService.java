package com.onlybuns.onlybuns.domain.services;

import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.onlybuns.onlybuns.core.misc.Result;
import com.onlybuns.onlybuns.domain.serviceinterfaces.ProfileServiceInterface;
import com.onlybuns.onlybuns.infrastructure.interfaces.UserRepository;
import com.onlybuns.onlybuns.presentation.dtos.requests.AddressDto;
import com.onlybuns.onlybuns.presentation.dtos.requests.UpdateProfileDto;
import com.onlybuns.onlybuns.presentation.dtos.responses.CommentDto;
import com.onlybuns.onlybuns.presentation.dtos.responses.ImageDto;
import com.onlybuns.onlybuns.presentation.dtos.responses.PostDto;
import com.onlybuns.onlybuns.presentation.dtos.responses.ProfileDto;
import com.onlybuns.onlybuns.presentation.dtos.responses.UserDto;

import jakarta.transaction.Transactional;

@Service
public class ProfileService implements ProfileServiceInterface {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BloomFilterService bloomFilterService;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Override
    public Result<ProfileDto> getProfile(String username) {
        // Check if username exists
        if (!bloomFilterService.contains(username)) {
            return Result.failure("User not found.", 404);
        }

        var userOptional = userRepository.findByUsername(username);

        if (userOptional.isEmpty()) {
            return Result.failure("User not found.", 404);
        }

        var user = userOptional.get();

        var profile = new ProfileDto();
        profile.setUsername(user.getUsername());
        profile.setName(user.getName());
        profile.setSurname(user.getSurname());
        profile.setEmail(user.getEmail());

        profile.setAddress(new AddressDto(user.getAddress().getStreet(),
                user.getAddress().getNumber(),
                user.getAddress().getCity(),
                user.getAddress().getCountry()));

        profile.setActivePosts(user.getPosts().stream().map(post -> new PostDto(post.getId(),
                new ImageDto(post.getImage().getData(), post.getImage().getMimetype(), post.getImage().getUploadedAt()),
                post.getDateOfCreation(),
                post.getDescription(),
                post.getNumberOfLikes(),
                post.getComments().stream().map(comment -> {
                    CommentDto commentDto = new CommentDto();
                    commentDto.setId(comment.getId());
                    commentDto.setComment(comment.getComment());
                    commentDto.setCommentedAt(comment.getCommentedAt());
                    return commentDto;
                }).collect(Collectors.toList()),
                post.getUsersThatLiked().stream()
                        .map(user1 -> {
                            UserDto userDto = new UserDto();
                            userDto.setUsername(user1.getUsername());
                            return userDto;
                        }).collect(Collectors.toList())))
                .collect(Collectors.toList()));

        profile.setFollowing(user.getFollowing().stream().map(follower -> new UserDto(follower.getUsername(),
                follower.getName(),
                follower.getSurname(),
                follower.getEmail(),
                new AddressDto(follower.getAddress().getStreet(),
                        follower.getAddress().getNumber(),
                        follower.getAddress().getCity(),
                        follower.getAddress().getCountry())))
                .collect(Collectors.toList()));

        profile.setFollowers(user.getFollowers().stream().map(follower -> new UserDto(follower.getUsername(),
                follower.getName(),
                follower.getSurname(),
                follower.getEmail(),
                new AddressDto(follower.getAddress().getStreet(),
                        follower.getAddress().getNumber(),
                        follower.getAddress().getCity(),
                        follower.getAddress().getCountry())))
                .collect(Collectors.toList()));

        return Result.success(profile);
    }

    @Transactional
    @Override
    public Result<ProfileDto> updateProfile(String username, UpdateProfileDto updateProfileDto) {
        // Check if username exists
        if (!bloomFilterService.contains(username)) {
            return Result.failure("Username doesn't exist.", 404);
        }

        var userOptional = userRepository.findByUsername(username);

        if (userOptional.isEmpty()) {
            return Result.failure("User not found.", 404);
        }

        if (!passwordEncoder.matches(updateProfileDto.getPassword(), userOptional.get().getPassword())) {
            return Result.failure("Invalid password.", 400);
        }

        var user = userOptional.get();

        var address = user.getAddress();

        address.setCity(updateProfileDto.getAddress().getCity());
        address.setCountry(updateProfileDto.getAddress().getCountry());
        address.setStreet(updateProfileDto.getAddress().getStreet());
        address.setNumber(updateProfileDto.getAddress().getNumber());

        user.setName(updateProfileDto.getName());
        user.setSurname(updateProfileDto.getSurname());
        user.setPassword(passwordEncoder.encode(updateProfileDto.getPassword()));

        var addressDto = updateProfileDto.getAddress();

        user.getAddress().setCity(addressDto.getCity());
        user.getAddress().setCountry(addressDto.getCountry());
        user.getAddress().setStreet(addressDto.getStreet());
        user.getAddress().setNumber(addressDto.getNumber());

        userRepository.save(user);

        return getProfile(username);
    }
}