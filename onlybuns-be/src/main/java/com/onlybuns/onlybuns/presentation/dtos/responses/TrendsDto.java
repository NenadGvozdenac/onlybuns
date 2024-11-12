package com.onlybuns.onlybuns.presentation.dtos.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class TrendsDto implements Serializable {
    private Long numberOfUsers;
    private Long numberOfPosts;
    private Long numberOfPostsInTheLastMonth;
    private List<PostDto> mostPopularPostsLastSevenDays; // 5 most popular posts in the last 7 days
    private List<PostDto> mostPopularPosts;     // 10 most popular posts
    private List<UserDto> usersThatMostLiked;   // 10 users that have liked the most posts
}
