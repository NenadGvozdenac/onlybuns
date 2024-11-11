package com.onlybuns.onlybuns.domain.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.onlybuns.onlybuns.core.misc.Result;
import com.onlybuns.onlybuns.domain.serviceinterfaces.TrendsServiceInterface;
import com.onlybuns.onlybuns.infrastructure.interfaces.PostRepositoryInterface;
import com.onlybuns.onlybuns.infrastructure.interfaces.UserRepository;
import com.onlybuns.onlybuns.infrastructure.repositories.PostRepository;
import com.onlybuns.onlybuns.presentation.dtos.requests.AddressDto;
import com.onlybuns.onlybuns.presentation.dtos.responses.CommentDto;
import com.onlybuns.onlybuns.presentation.dtos.responses.ImageDto;
import com.onlybuns.onlybuns.presentation.dtos.responses.PostDto;
import com.onlybuns.onlybuns.presentation.dtos.responses.TrendsDto;
import com.onlybuns.onlybuns.presentation.dtos.responses.UserDto;

import java.util.stream.Collectors;

@Service
public class TrendsService implements TrendsServiceInterface {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private PostRepositoryInterface postRepositoryInterface;

    @Autowired
    private ImageService imageService;

    @Transactional
    @Override
    public Result<TrendsDto> getTrends() {
        try {
            TrendsDto trends = new TrendsDto();

            var numberOfUsers = userRepository.count();
            var numberOfPosts = postRepositoryInterface.count();
            var numberOfPostsInTheLastMonth = postRepository.countPostsInTheLastMonth();
            var mostPopularPostsLastSevenDays = postRepository.findMostPopularPostsLastSevenDays();
            var mostPopularPosts = postRepository.findMostPopularPosts();
            var usersThatMostLiked = postRepository.findUsersThatMostLiked();

            trends.setNumberOfUsers(numberOfUsers);
            trends.setNumberOfPosts(numberOfPosts);
            trends.setNumberOfPostsInTheLastMonth(numberOfPostsInTheLastMonth);

            trends.setMostPopularPostsLastSevenDays(mostPopularPostsLastSevenDays
                    .stream().map(post -> new PostDto(post.getId(),
                            new ImageDto(imageService.getImageBase64(post.getImage().getId()).getData(), post.getImage().getMimetype(),
                                    post.getImage().getUploadedAt()),
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
                            post.getComments().stream()
                                    .map(comment -> new CommentDto(comment.getId(), comment.getComment(),
                                            comment.getCommentedAt()))
                                    .collect(Collectors.toList()),
                            post.getUsersThatLiked().stream()
                                    .map(user -> new UserDto(user.getUsername(), user.getName(), user.getSurname(),
                                            user.getEmail(), null))
                                    .collect(Collectors.toList())))
                    .collect(Collectors.toList()));

            trends.setMostPopularPosts(mostPopularPosts.stream().map(post -> new PostDto(post.getId(),
                    new ImageDto(post.getImage().getPath(), post.getImage().getMimetype(),
                            post.getImage().getUploadedAt()),
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
                    post.getComments().stream()
                            .map(comment -> new CommentDto(comment.getId(), comment.getComment(),
                                    comment.getCommentedAt()))
                            .collect(Collectors.toList()),
                    post.getUsersThatLiked().stream()
                            .map(user -> new UserDto(user.getUsername(), user.getName(), user.getSurname(),
                                    user.getEmail(), null))
                            .collect(Collectors.toList())))
                    .collect(Collectors.toList()));

            trends.setUsersThatMostLiked(usersThatMostLiked.stream().map(
                    user -> new UserDto(user.getUsername(), user.getName(), user.getSurname(), user.getEmail(), null))
                    .collect(Collectors.toList()));

            return Result.success(trends);
        } catch (Exception e) {
            return Result.failure("Failed to get trends", 500);
        }
    }
}
