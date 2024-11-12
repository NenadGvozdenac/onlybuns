package com.onlybuns.onlybuns.domain.serviceinterfaces;

import com.onlybuns.onlybuns.core.misc.Result;
import com.onlybuns.onlybuns.presentation.dtos.requests.EmailDto;

public interface EmailServiceInterface {
    public Result<String> verifyEmail(EmailDto emailDto);
    public Result<String> verifyEmail(String token);
}
