package com.onlybuns.onlybuns.domain.services;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.onlybuns.onlybuns.core.misc.Result;
import com.onlybuns.onlybuns.domain.models.Address;
import com.onlybuns.onlybuns.domain.models.Comment;
import com.onlybuns.onlybuns.domain.models.Image;
import com.onlybuns.onlybuns.domain.models.Post;
import com.onlybuns.onlybuns.domain.models.User;
import com.onlybuns.onlybuns.domain.serviceinterfaces.PostServiceInterface;
import com.onlybuns.onlybuns.infrastructure.interfaces.AddressRepository;
import com.onlybuns.onlybuns.infrastructure.interfaces.PostRepositoryInterface;
import com.onlybuns.onlybuns.infrastructure.interfaces.UserRepository;
import com.onlybuns.onlybuns.presentation.dtos.requests.AddressDto;
import com.onlybuns.onlybuns.presentation.dtos.requests.UpdatePostDto;
import com.onlybuns.onlybuns.presentation.dtos.responses.CommentDto;
import com.onlybuns.onlybuns.presentation.dtos.responses.GetAllPostDto;
import com.onlybuns.onlybuns.presentation.dtos.responses.ImageDto;
import com.onlybuns.onlybuns.presentation.dtos.responses.PostAndLocationDto;
import com.onlybuns.onlybuns.presentation.dtos.responses.PostDto;
import com.onlybuns.onlybuns.presentation.dtos.responses.UserDto;

import jakarta.transaction.Transactional;

@Service
public class PostService extends BaseService implements PostServiceInterface {

    @Autowired
    private PostRepositoryInterface postRepositoryjpa;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ImageService imageService;
    @Autowired
    private AddressRepository addressRepository;

    @Override
    @Transactional
    @CacheEvict(value = "trends", allEntries = true)
    public Result<PostDto> likePost(Long postId, String userUsername) {

        var postOptional = postRepositoryjpa.findById(postId);

        if (postOptional.isPresent()) {

            Post post = postOptional.get();
            var userOptional = userRepository.findByUsername(userUsername);

            if (userOptional.isPresent()) {
                // provera da li je lajkovao vec
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
                postDto.setDescription(post.getDescription());
                postDto.setDateOfCreation(post.getDateOfCreation());
                var imageDto = new ImageDto(imageService.getImageBase64(post.getImage().getId()).getData(),
                        post.getImage().getMimetype(), post.getImage().getUploadedAt());
                postDto.setImage(imageDto);
                return Result.success(postDto);
            } else {
                return Result.failure("User that is supposed to do the liking is not found.", 404);
            }
        } else {
            return Result.failure("Post not found.", 403);
        }
    }

    @Override
    @CacheEvict(value = "trends", allEntries = true)
    public Result<PostDto> unlikePost(Long postId, String userUsername) {
            
            var postOptional = postRepositoryjpa.findById(postId);
    
            if (postOptional.isPresent()) {
    
                Post post = postOptional.get();
                var userOptional = userRepository.findByUsername(userUsername);
    
                if (userOptional.isPresent()) {
                    User user = userOptional.get();
                    boolean userAlreadyLiked = post.getUsersThatLiked().stream()
                            .anyMatch(us -> us.getId().equals(user.getId()));
    
                    if (!userAlreadyLiked) {
                        return Result.failure("User didn't like this post.", 400);
                    }
    
                    post.getUsersThatLiked().remove(user);
                    post.setNumberOfLikes(post.getNumberOfLikes() - 1);
                    postRepositoryjpa.save(post);
    
                    PostDto postDto = new PostDto();
                    postDto.setNumberOfLikes(post.getNumberOfLikes());
                    postDto.setDescription(post.getDescription());
                    postDto.setDateOfCreation(post.getDateOfCreation());
                    var imageDto = new ImageDto(imageService.getImageBase64(post.getImage().getId()).getData(),
                            post.getImage().getMimetype(), post.getImage().getUploadedAt());
                    postDto.setImage(imageDto);
                    return Result.success(postDto);
                } else {
                    return Result.failure("User that is supposed to do the unliking is not found.", 404);
                }
            } else {
                return Result.failure("Post not found.", 403);
            }
    }

    @Override
    @Transactional
    public Result<List<GetAllPostDto>> getAllPosts() {

        try {
            List<Post> posts = postRepositoryjpa.findAll(Sort.by(Sort.Direction.DESC, "dateOfCreation"));

            List<GetAllPostDto> postDtos = posts.stream()
                    .map(post -> {
                        GetAllPostDto postDto = new GetAllPostDto();
                        postDto.setId(post.getId());
                        postDto.setDescription(post.getDescription());
                        postDto.setDateOfCreation(post.getDateOfCreation());
                        postDto.setNumberOfLikes(post.getNumberOfLikes());
                        postDto.setUsername(post.getUser().getUsername());
                        var imageDto = new ImageDto(imageService.getImageBase64(post.getImage().getId()).getData(),
                                post.getImage().getMimetype(), post.getImage().getUploadedAt());
                        postDto.setImage(imageDto);

                        postDto.setAddress(new AddressDto(post.getLocation().getStreet(),
                                post.getLocation().getNumber(),
                                post.getLocation().getCity(),
                                post.getLocation().getCountry(),
                                post.getLocation().getLongitude(),
                                post.getLocation().getLatitude()));

                        List<UserDto> userDtos = post.getUsersThatLiked().stream()
                                .map(user -> {
                                    UserDto userDto = new UserDto();
                                    userDto.setUsername(user.getUsername());
                                    return userDto;
                                }).collect(Collectors.toList());
                        postDto.setUsers(userDtos);

                        List<CommentDto> commentDtos = post.getComments().stream()
                                .map(comment -> {
                                    CommentDto commentDto = new CommentDto();
                                    commentDto.setId(comment.getId());
                                    commentDto.setComment(comment.getComment());
                                    commentDto.setCommentedAt(comment.getCommentedAt());
                                    return commentDto;
                                })
                                .collect(Collectors.toList());
                        postDto.setComments(commentDtos);

                        return postDto;
                    })
                    .collect(Collectors.toList());

            return Result.success(postDtos);

        } catch (Exception e) {
            return Result.failure("Posts not found.", 404);
        }
    }

    @Override
    @Transactional
    public Result<List<GetAllPostDto>> getMyPosts(String username) {

        try {
            List<Post> posts = postRepositoryjpa.findByUsername(username);

            List<GetAllPostDto> postDtos = posts.stream()
                    .map(post -> {
                        GetAllPostDto postDto = new GetAllPostDto();
                        postDto.setId(post.getId());
                        postDto.setDescription(post.getDescription());
                        postDto.setDateOfCreation(post.getDateOfCreation());
                        postDto.setNumberOfLikes(post.getNumberOfLikes());
                        postDto.setUsername(post.getUser().getUsername());
                        var imageDto = new ImageDto(imageService.getImageBase64(post.getImage().getId()).getData(),
                                post.getImage().getMimetype(), post.getImage().getUploadedAt());
                        postDto.setImage(imageDto);

                        List<UserDto> userDtos = post.getUsersThatLiked().stream()
                                .map(user -> {
                                    UserDto userDto = new UserDto();
                                    userDto.setUsername(user.getUsername());
                                    return userDto;
                                }).collect(Collectors.toList());
                        postDto.setUsers(userDtos);

                        postDto.setAddress(new AddressDto(post.getLocation().getStreet(),
                                post.getLocation().getNumber(),
                                post.getLocation().getCity(),
                                post.getLocation().getCountry(),
                                post.getLocation().getLongitude(),
                                post.getLocation().getLatitude()));

                        List<CommentDto> commentDtos = post.getComments().stream()
                                .map(comment -> {
                                    CommentDto commentDto = new CommentDto();
                                    commentDto.setId(comment.getId());
                                    commentDto.setComment(comment.getComment());
                                    commentDto.setCommentedAt(comment.getCommentedAt());
                                    return commentDto;
                                })
                                .collect(Collectors.toList());
                        postDto.setComments(commentDtos);

                        return postDto;
                    })
                    .collect(Collectors.toList());

            return Result.success(postDtos);

        } catch (Exception e) {
            return Result.failure("Posts not found.", 404);
        }
    }

    @Override
    @Transactional
    @CacheEvict(value = "trends", allEntries = true)
    public Result<String> deletePost(Long postId, String userUsername) {

        var postOptional = postRepositoryjpa.findById(postId);
        if (postOptional.isEmpty()) {
            return Result.failure("Post doesn't exist", 404);
        }

        var userOptional = userRepository.findByUsername(userUsername);
        if (userOptional.isEmpty()) {
            return Result.failure("User doesn't exist", 409);
        }

        Post post = postOptional.get();
        User user = userOptional.get();

        if (user.getId().equals(post.getUser().getId())) {
            postRepositoryjpa.deleteById(post.getId());
            return Result.success("Post deleted successfully");
        } else {
            return Result.failure("User is not the owner of the post", 403);
        }

    }

    @Override
    @Transactional
    @CacheEvict(value = "trends", allEntries = true)
    public Result<PostDto> updatePost(UpdatePostDto updatePostDto, String userUsername) {

        var postOptional = postRepositoryjpa.findById(updatePostDto.getId());
        if (postOptional.isEmpty()) {
            return Result.failure("Post doesn't exist", 404);
        }

        var userOptional = userRepository.findByUsername(userUsername);
        if (userOptional.isEmpty()) {
            return Result.failure("User doesn't exist", 409);
        }

        Post post = postOptional.get();
        User user = userOptional.get();

        if (user.getId().equals(post.getUser().getId())) {
            post.setDescription(updatePostDto.getDescription());
            postRepositoryjpa.save(post);

            // response
            PostDto postDto = new PostDto();
            postDto.setDateOfCreation(post.getDateOfCreation());
            postDto.setDescription(post.getDescription());
            postDto.setNumberOfLikes(post.getNumberOfLikes());
            postDto.setId(post.getId());
            var imageDto = new ImageDto(imageService.getImageBase64(post.getImage().getId()).getData(),
                    post.getImage().getMimetype(), post.getImage().getUploadedAt());
            postDto.setImage(imageDto);
            return Result.success(postDto);
        } else {
            return Result.failure("User is not the owner of the post", 403);
        }

    }

    @Override
    public Result<List<PostAndLocationDto>> getNearbyPosts(double latitude, double longitude, Double radius_km) {
        var allPosts = postRepositoryjpa.findAll();

        List<PostAndLocationDto> nearbyPosts = allPosts.stream()
                .filter(post -> {
                    var postLatitude = post.getLocation().getLatitude();
                    var postLongitude = post.getLocation().getLongitude();

                    // Haversine formula to calculate distance between two points on the Earth
                    final int R = 6371; // Radius of the Earth in kilometers
                    double latDistance = Math.toRadians(postLatitude - latitude);
                    double lonDistance = Math.toRadians(postLongitude - longitude);
                    double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2) +
                            Math.cos(Math.toRadians(latitude)) * Math.cos(Math.toRadians(postLatitude)) *
                                    Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
                    double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
                    double distance = R * c; // Distance in kilometers

                    return distance <= radius_km;
                })
                .map(post -> {
                    PostAndLocationDto postAndLocationDto = new PostAndLocationDto();
                    postAndLocationDto.setId(post.getId());
                    postAndLocationDto.setDescription(post.getDescription());
                    postAndLocationDto.setUsername(post.getUser().getUsername());
                    var imageDto = new ImageDto(imageService.getImageBase64(post.getImage().getId()).getData(),
                            post.getImage().getMimetype(), post.getImage().getUploadedAt());
                    postAndLocationDto.setImage(imageDto);
                    postAndLocationDto.setAddress(new AddressDto(post.getLocation().getStreet(),
                            post.getLocation().getNumber(),
                            post.getLocation().getCity(),
                            post.getLocation().getCountry(),
                            post.getLocation().getLongitude(),
                            post.getLocation().getLatitude()));
                    return postAndLocationDto;
                })
                .collect(Collectors.toList());

        return Result.success(nearbyPosts);
    }

    @Override
    @Transactional
    @CacheEvict(value = "trends", allEntries = true)
    public Result<PostDto> createPost(String description, MultipartFile image, AddressDto address, String username) {
        // Check if user exists
        Optional<User> userOptional = userRepository.findByUsername(username);
        if (userOptional.isEmpty()) {
            System.out.println("User not found: " + username);
            return Result.failure("User doesn't exist", 409);
        }
        // Handle image file (e.g., save the image to file storage or cloud)
        try {
            System.out.println("Image saved ");
            Image savedimage = imageService.saveImage(image).getData();
            Address newaddress = new Address(address.getStreet(),
                    address.getNumber(),
                    address.getCity(),
                    address.getCountry(),
                    address.getLatitude(),
                    address.getLongitude());
            addressRepository.save(newaddress);

            System.out.println("Adress made ");
            // Create a new Post entity
            List<User> usersThatLiked = new ArrayList<User>();
            List<Comment> comments = new ArrayList<Comment>();
            Post post = new Post(0l, userOptional.get(), LocalDateTime.now().plusHours(1), description, 0, false,
                    newaddress, savedimage, usersThatLiked, comments);
            System.out.println("Post made ");

            // Save the post to the database
            var newpost = postRepositoryjpa.save(post);
            PostDto postDto = new PostDto();
            postDto.setId(newpost.getId());
            postDto.setDescription(newpost.getDescription());
            postDto.setDateOfCreation(newpost.getDateOfCreation());
            postDto.setNumberOfLikes(newpost.getNumberOfLikes());
            var imageDto = new ImageDto(imageService.getImageBase64(newpost.getImage().getId()).getData(),
                    newpost.getImage().getMimetype(), newpost.getImage().getUploadedAt());
            postDto.setImage(imageDto);
            postDto.setAddress(new AddressDto(newpost.getLocation().getStreet(),
                    newpost.getLocation().getNumber(),
                    newpost.getLocation().getCity(),
                    newpost.getLocation().getCountry(),
                    newpost.getLocation().getLongitude(),
                    newpost.getLocation().getLatitude()));
            return Result.success(postDto);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.failure("Invalid request data (e.g., missing description, image, or location)", 400);
        }
    }

    @Override
    public Result<List<GetAllPostDto>> getAllPostsByFollowing(String username) {
        try {
            List<Post> posts = postRepositoryjpa.findAll(Sort.by(Sort.Direction.DESC, "dateOfCreation"));

            var userOptional = userRepository.findByUsername(username);
            if (userOptional.isEmpty()) {
                return Result.failure("Not logged in", 409);
            }
            User user = userOptional.get();

            List<Post> postsByFollowing = posts.stream().filter(post -> {
                return post.getUser().getFollowers().stream().anyMatch(follower -> follower.getId().equals(user.getId()));
            }).collect(Collectors.toList());

            List<GetAllPostDto> postDtos = postsByFollowing.stream()
                    .map(post -> {
                        GetAllPostDto postDto = new GetAllPostDto();
                        postDto.setId(post.getId());
                        postDto.setDescription(post.getDescription());
                        postDto.setDateOfCreation(post.getDateOfCreation());
                        postDto.setNumberOfLikes(post.getNumberOfLikes());
                        postDto.setUsername(post.getUser().getUsername());
                        var imageDto = new ImageDto(imageService.getImageBase64(post.getImage().getId()).getData(),
                                post.getImage().getMimetype(), post.getImage().getUploadedAt());
                        postDto.setImage(imageDto);

                        postDto.setAddress(new AddressDto(post.getLocation().getStreet(),
                                post.getLocation().getNumber(),
                                post.getLocation().getCity(),
                                post.getLocation().getCountry(),
                                post.getLocation().getLongitude(),
                                post.getLocation().getLatitude()));

                        List<UserDto> userDtos = post.getUsersThatLiked().stream()
                                .map(userr-> {
                                    UserDto userDto = new UserDto();
                                    userDto.setUsername(userr.getUsername());
                                    return userDto;
                                }).collect(Collectors.toList());
                        postDto.setUsers(userDtos);

                        List<CommentDto> commentDtos = post.getComments().stream()
                                .map(comment -> {
                                    CommentDto commentDto = new CommentDto();
                                    commentDto.setId(comment.getId());
                                    commentDto.setComment(comment.getComment());
                                    commentDto.setCommentedAt(comment.getCommentedAt());
                                    return commentDto;
                                })
                                .collect(Collectors.toList());
                        postDto.setComments(commentDtos);

                        return postDto;
                    })
                    .collect(Collectors.toList());

            return Result.success(postDtos);

        } catch (Exception e) {
            return Result.failure("Posts not found.", 404);
        }
    }

}