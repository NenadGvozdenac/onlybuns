package com.onlybuns.onlybuns.domain.services;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.onlybuns.onlybuns.core.misc.Result;
import com.onlybuns.onlybuns.domain.models.User;
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

    @Autowired
    private ImageService imageService;
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
                user.getAddress().getCountry(),
                user.getAddress().getLongitude(),
                user.getAddress().getLatitude()));

        profile.setActivePosts(user.getPosts().stream().map(post -> new PostDto(post.getId(),
                new ImageDto(imageService.getImageBase64(post.getImage().getId()).getData(), post.getImage().getMimetype(), post.getImage().getUploadedAt()),
                post.getDateOfCreation(),
                post.getDescription(),
                post.getNumberOfLikes(),
                post.getUser().getUsername(),
                new AddressDto(post.getUser().getAddress().getStreet(),
                        post.getUser().getAddress().getNumber(),
                        post.getUser().getAddress().getCity(),
                        post.getUser().getAddress().getCountry(),
                        post.getUser().getAddress().getLatitude(),
                        post.getUser().getAddress().getLongitude()),
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
                        follower.getAddress().getCountry(),
                        follower.getAddress().getLatitude(),
                        follower.getAddress().getLongitude())))
                .collect(Collectors.toList()));

        profile.setFollowers(user.getFollowers().stream().map(follower -> new UserDto(follower.getUsername(),
                follower.getName(),
                follower.getSurname(),
                follower.getEmail(),
                new AddressDto(follower.getAddress().getStreet(),
                        follower.getAddress().getNumber(),
                        follower.getAddress().getCity(),
                        follower.getAddress().getCountry(),
                        follower.getAddress().getLatitude(),
                        follower.getAddress().getLongitude())))
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

        if (!updateProfileDto.getPassword().equals(updateProfileDto.getConfirmPassword())) {
            return Result.failure("Invalid password.", 400);
        }

        var user = userOptional.get();

        var address = user.getAddress();

        address.setCity(updateProfileDto.getAddress().getCity());
        address.setCountry(updateProfileDto.getAddress().getCountry());
        address.setStreet(updateProfileDto.getAddress().getStreet());
        address.setNumber(updateProfileDto.getAddress().getNumber());
        address.setLatitude(updateProfileDto.getAddress().getLatitude());
        address.setLongitude(updateProfileDto.getAddress().getLongitude());

        user.setName(updateProfileDto.getName());
        user.setSurname(updateProfileDto.getSurname());
        user.setPassword(passwordEncoder.encode(updateProfileDto.getPassword()));

        var addressDto = updateProfileDto.getAddress();

        user.getAddress().setCity(addressDto.getCity());
        user.getAddress().setCountry(addressDto.getCountry());
        user.getAddress().setStreet(addressDto.getStreet());
        user.getAddress().setNumber(addressDto.getNumber());
        user.getAddress().setLatitude(addressDto.getLatitude());
        user.getAddress().setLongitude(addressDto.getLongitude());

        userRepository.save(user);

        return getProfile(username);
    }

    @Override
    public Result<List<ProfileDto>> getVerifiedProfiles(String username,
            String name,
            String surname,
            String email,
            Integer minActivePosts,
            Integer maxActivePosts,
            String sortBy,
            int page) {

        // Ensure the user is an admin
        if (userRepository.findByUsername(username).get().getRole().toString() != "ADMIN") {
            return Result.failure("Unauthorized access.", 401);
        }

        // Retrieve all verified users
        List<User> verifiedUsers = userRepository.findByVerifiedTrue();

        // Check if there are any verified users; if not, return failure result
        if (verifiedUsers.isEmpty()) {
            return Result.failure("No verified users found.", 404);
        }

        // Filter users based on search criteria
        if (name != null) {
            verifiedUsers = verifiedUsers.stream()
                    .filter(user -> user.getName().toLowerCase().contains(name.toLowerCase()))
                    .collect(Collectors.toList());
        }
        if (surname != null) {
            verifiedUsers = verifiedUsers.stream()
                    .filter(user -> user.getSurname().toLowerCase().contains(surname.toLowerCase()))
                    .collect(Collectors.toList());
        }
        if (email != null) {
            verifiedUsers = verifiedUsers.stream()
                    .filter(user -> user.getEmail().toLowerCase().contains(email.toLowerCase()))
                    .collect(Collectors.toList());
        }
        if (minActivePosts != null) {
            verifiedUsers = verifiedUsers.stream()
                    .filter(user -> user.getPosts().size() >= minActivePosts)
                    .collect(Collectors.toList());
        }
        if (maxActivePosts != null) {
            verifiedUsers = verifiedUsers.stream()
                    .filter(user -> user.getPosts().size() <= maxActivePosts)
                    .collect(Collectors.toList());
        }

        // Sort users by the specified parameter
        if ("following".equals(sortBy)) {
            verifiedUsers.sort((user1, user2) -> Integer.compare(user2.getFollowing().size(), user1.getFollowing().size()));
        }
         else if ("email".equals(sortBy)) {
            // Sort emails case-insensitively and handle null emails (if needed)
            verifiedUsers
                    .sort(Comparator.comparing(User::getEmail, Comparator.nullsLast(String.CASE_INSENSITIVE_ORDER)));
        }

        int totalUsers = verifiedUsers.size();
        int fromIndex = (page - 1) * 5;
        int toIndex = Math.min(fromIndex + 5, totalUsers);

        if (fromIndex >= totalUsers) {
            return Result.success(Collections.emptyList());
        }

        List<User> paginatedUsers = verifiedUsers.subList(fromIndex, toIndex);

        // Map each verified user to a ProfileDto
        List<ProfileDto> profiles = paginatedUsers.stream().map(user -> {
            // Create a new ProfileDto for each user
            ProfileDto profile = new ProfileDto();
            profile.setUsername(user.getUsername());
            profile.setName(user.getName());
            profile.setSurname(user.getSurname());
            profile.setEmail(user.getEmail());

            // Set the address
            profile.setAddress(new AddressDto(
                    user.getAddress().getStreet(),
                    user.getAddress().getNumber(),
                    user.getAddress().getCity(),
                    user.getAddress().getCountry(),
                    user.getAddress().getLatitude(),
                    user.getAddress().getLongitude()));

            // Set active posts
            profile.setActivePosts(user.getPosts().stream().map(post -> new PostDto(
                    post.getId(),
                    null, // Skip setting ImageDto by using null
                    post.getDateOfCreation(),
                    post.getDescription(),
                    post.getNumberOfLikes(),
                    post.getUser().getUsername(),
                    null,
                    Collections.emptyList(), // Set comments as an empty list
                    Collections.emptyList() // Set usersThatLiked as an empty list (if needed)
            )).collect(Collectors.toList()));

            // Set following users
            profile.setFollowing(user.getFollowing().stream().map(followingUser -> new UserDto(
                    followingUser.getUsername(),
                    followingUser.getName(),
                    followingUser.getSurname(),
                    followingUser.getEmail(),
                    new AddressDto(
                            followingUser.getAddress().getStreet(),
                            followingUser.getAddress().getNumber(),
                            followingUser.getAddress().getCity(),
                            followingUser.getAddress().getCountry(),
                            followingUser.getAddress().getLatitude(),
                            followingUser.getAddress().getLongitude())))
                    .collect(Collectors.toList()));

            // Set followers
            profile.setFollowers(user.getFollowers().stream().map(follower -> new UserDto(
                    follower.getUsername(),
                    follower.getName(),
                    follower.getSurname(),
                    follower.getEmail(),
                    null)).collect(Collectors.toList()));

            return profile;
        }).collect(Collectors.toList());

        // Return the list of profiles as a success result
        return Result.success(profiles);
    }

    @Override
    public Result<String> followProfile(String loggedInUsername, String usernameToFollow) {

        var loggedInUserOptional = userRepository.findByUsername(loggedInUsername);
        var userToFollowOptional = userRepository.findByUsername(usernameToFollow);

        if (loggedInUserOptional.isEmpty() || userToFollowOptional.isEmpty()) {
            return Result.failure("User(s) not found", 404);
        }

        if (loggedInUsername.equals(usernameToFollow)) {
            return Result.failure("You cannot follow yourself", 409);
        }

        User loggedInUser = loggedInUserOptional.get();
        User userToFollow = userToFollowOptional.get();

        if (loggedInUser.getFollowing().contains(userToFollow)) {
            return Result.failure("You are already following this user", 409);
        }

        loggedInUser.getFollowing().add(userToFollow);
        userToFollow.getFollowers().add(loggedInUser);

        userRepository.save(loggedInUser);
        userRepository.save(userToFollow);

        return Result.success("Successfully followed the user");
    }

    @Override
    public Result<String> unfollowProfile(String loggedInUsername, String usernameToUnfollow) {

        var loggedInUserOptional = userRepository.findByUsername(loggedInUsername);
        var userToUnfollowOptional = userRepository.findByUsername(usernameToUnfollow);

        if (loggedInUserOptional.isEmpty() || userToUnfollowOptional.isEmpty()) {
            return Result.failure("User(s) not found", 404);
        }

        User loggedInUser = loggedInUserOptional.get();
        User userToUnfollow = userToUnfollowOptional.get();

        if (!loggedInUser.getFollowing().contains(userToUnfollow)) {
            return Result.failure("You are not following this user", 409);
        }

        loggedInUser.getFollowing().remove(userToUnfollow);
        userToUnfollow.getFollowers().remove(loggedInUser);


        userRepository.save(loggedInUser);
        userRepository.save(userToUnfollow);

        return Result.success("Successfully unfollowed the user");
    }

}