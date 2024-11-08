package com.onlybuns.onlybuns.domain.serviceinterfaces;

import java.util.List;

import com.onlybuns.onlybuns.core.misc.Result;
import com.onlybuns.onlybuns.presentation.dtos.requests.UpdateProfileDto;
import com.onlybuns.onlybuns.presentation.dtos.responses.ProfileDto;

public interface ProfileServiceInterface {
    public Result<ProfileDto> getProfile(String username);
    public Result<ProfileDto> updateProfile(String username, UpdateProfileDto updateProfileDto);
    public Result<List<ProfileDto>> getVerifiedProfiles(String username,
            String name,
            String surname,
            String email,
            Integer minActivePosts,
            Integer maxActivePosts,
            String sortBy);
}
