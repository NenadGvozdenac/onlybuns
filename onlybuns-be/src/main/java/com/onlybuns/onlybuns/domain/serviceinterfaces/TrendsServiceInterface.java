package com.onlybuns.onlybuns.domain.serviceinterfaces;

import com.onlybuns.onlybuns.core.misc.Result;
import com.onlybuns.onlybuns.presentation.dtos.responses.TrendsDto;

public interface TrendsServiceInterface {
    public Result<TrendsDto> getTrends();
}
