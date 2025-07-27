package com.onlybuns.onlybuns.domain.serviceinterfaces;

import com.onlybuns.onlybuns.core.misc.Result;
import com.onlybuns.onlybuns.presentation.dtos.responses.AdminAnalyticsDto;

public interface AdminAnalyticsInterface {
    public Result<AdminAnalyticsDto> getAdminAnalytics(String admingUsername);
}
