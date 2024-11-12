package com.onlybuns.onlybuns.domain.serviceinterfaces;

import com.onlybuns.onlybuns.core.misc.Result;
import com.onlybuns.onlybuns.presentation.dtos.requests.LoginUserDto;
import com.onlybuns.onlybuns.presentation.dtos.requests.RegisterUserDto;
import com.onlybuns.onlybuns.presentation.dtos.responses.UserDto;
import com.onlybuns.onlybuns.presentation.dtos.responses.UserLoginDto;

public interface UserServiceInterface {
    public Result<UserDto> registerUser(RegisterUserDto registerUserDto);
    public Result<UserLoginDto> loginUser(LoginUserDto registerUserDto);
}
