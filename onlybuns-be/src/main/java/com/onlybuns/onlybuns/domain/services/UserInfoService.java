package com.onlybuns.onlybuns.domain.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.onlybuns.onlybuns.core.misc.Result;
import com.onlybuns.onlybuns.domain.models.User;
import com.onlybuns.onlybuns.domain.models.UserInfoDetails;
import com.onlybuns.onlybuns.domain.serviceinterfaces.UserServiceInterface;
import com.onlybuns.onlybuns.infrastructure.repositories.UserRepository;
import com.onlybuns.onlybuns.presentation.dtos.requests.LoginUserDto;
import com.onlybuns.onlybuns.presentation.dtos.requests.RegisterUserDto;
import com.onlybuns.onlybuns.presentation.dtos.responses.UserDto;
import com.onlybuns.onlybuns.presentation.dtos.responses.UserLoginDto;

@Service
public class UserInfoService extends BaseService implements UserDetailsService, UserServiceInterface {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtService jwtService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> userDetail = userRepository.findByUsername(username);

        // Converting UserInfo to UserDetails
        return userDetail.map(UserInfoDetails::new)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
    }

    @Override
    public Result<UserLoginDto> registerUser(RegisterUserDto registerUserDto) {
        // Check if password and confirm password match
        if (!registerUserDto.getPassword().equals(registerUserDto.getConfirmPassword())) {
            return Result.failure("Passwords do not match", 400);
        }

        // Check if a user with the same email or username already exists
        if (userRepository.findByEmail(registerUserDto.getEmail()).isPresent() || 
            userRepository.findByUsername(registerUserDto.getUsername()).isPresent()) {
            return Result.failure("User with this email or username already exists", 409);
        }

        // Map RegisterUserDto to the User entity (you can use a mapper here)
        User newUser = new User();
        newUser.setUsername(registerUserDto.getUsername());
        newUser.setEmail(registerUserDto.getEmail());
        newUser.setPassword(registerUserDto.getPassword()); // TODO: Encrypt the password before saving it
        newUser.setName(registerUserDto.getName());
        newUser.setSurname(registerUserDto.getSurname());

        // Save the new user in the repository
        var user = userRepository.save(newUser);

        // Generate JWT access token
        String jwtToken = jwtService.generateToken(user.getUsername());

        // Create a UserLoginDto object to return
        UserLoginDto userLoginDto = new UserLoginDto();
        userLoginDto.setUsername(user.getUsername());
        userLoginDto.setAccessToken(jwtToken); // Set the generated token

        return Result.success(userLoginDto);
    }

    @Override
    public Result<UserLoginDto> loginUser(LoginUserDto loginUserDto) {
        // Check if a user with the given username exists
        var userOptional = userRepository.findByUsername(loginUserDto.getUsername());

        if(userOptional.isEmpty()) {
            return Result.failure("User not found", 404);
        }

        var user = userOptional.get();

        // Check if the password is correct
        if(!user.getPassword().equals(loginUserDto.getPassword())) {
            return Result.failure("Incorrect password", 401);
        }

        // Generate JWT access token
        String jwtToken = jwtService.generateToken(user.getUsername());

        // Create a UserLoginDto object to return
        UserLoginDto userLoginDto = new UserLoginDto();
        userLoginDto.setUsername(user.getUsername());
        userLoginDto.setAccessToken(jwtToken); // Set the generated token

        return Result.success(userLoginDto);
    }

    @Override
    public Result<UserDto> getUser(Long id) {
        // Get the user from the repository
        var user = userRepository.findById(id);

        if(user.isEmpty()) {
            return Result.failure("User not found", 404);
        }

        var userDto = MapToDto(user.get(), UserDto.class);

        return Result.success(userDto);
    }
}