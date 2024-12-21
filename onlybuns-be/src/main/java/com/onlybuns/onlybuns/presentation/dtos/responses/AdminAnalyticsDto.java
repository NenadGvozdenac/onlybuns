package com.onlybuns.onlybuns.presentation.dtos.responses;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdminAnalyticsDto implements Serializable{
        private int postsPerWeek;
        private int postsPerMonth;
        private int postsPerYear;
        private int commentsPerWeek;
        private int commentsPerMonth;
        private int commentsPerYear;
        private int usersJustPosted;
        private int usersJustCommented;
        private int usersWithoutPostsOrComments;
}
