package com.onlybuns.onlybuns.domain.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.onlybuns.onlybuns.core.misc.Result;
import com.onlybuns.onlybuns.domain.models.Address;
import com.onlybuns.onlybuns.domain.models.User;
import com.onlybuns.onlybuns.domain.models.UserInfoDetails;
import com.onlybuns.onlybuns.domain.models.UserRole;
import com.onlybuns.onlybuns.domain.serviceinterfaces.UserServiceInterface;
import com.onlybuns.onlybuns.infrastructure.interfaces.AddressRepository;
import com.onlybuns.onlybuns.infrastructure.interfaces.UserRepository;
import com.onlybuns.onlybuns.presentation.dtos.requests.LoginUserDto;
import com.onlybuns.onlybuns.presentation.dtos.requests.RegisterUserDto;
import com.onlybuns.onlybuns.presentation.dtos.responses.UserDto;
import com.onlybuns.onlybuns.presentation.dtos.responses.UserLoginDto;

@Service
public class UserService extends BaseService implements UserDetailsService, UserServiceInterface {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private EmailService emailService;

    @Autowired
    private BloomFilterService bloomFilterService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> userDetail = userRepository.findByUsername(username);

        // Converting UserInfo to UserDetails
        return userDetail.map(UserInfoDetails::new)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
    }

    @Override
    @Transactional
    public Result<UserDto> registerUser(RegisterUserDto registerUserDto) {
        try {
            // Check if password and confirm password match
            if (!registerUserDto.getPassword().equals(registerUserDto.getConfirmPassword())) {
                return Result.failure("Passwords do not match", 400);
            }

            // Lock the user table to prevent concurrent registration of the same username
            userRepository.lockUserTableByUsername(registerUserDto.getUsername());

            // Check if username exists in bloom filter
            if (bloomFilterService.contains(registerUserDto.getUsername())) {
                // Check if a user with the same username exists for real, because bloom filter is not 100% accurate
                if(userRepository.findByUsername(registerUserDto.getUsername()).isPresent()) {
                    return Result.failure("Username already exists", 409);
                }
            }

            // Check if a user with the same email exists
            var userOptional = userRepository.findByEmail(registerUserDto.getEmail());

            if(userOptional.isPresent()) {
                return Result.failure("User with the same email already exists", 409);
            }

            // Check if the email is valid
            if (!emailService.isValidEmail(registerUserDto.getEmail())) {
                return Result.failure("Invalid email: " + registerUserDto.getEmail(), 411);
            }

            var addressDto = registerUserDto.getAddress();

            Address address = new Address();
            
            address.setCity(addressDto.getCity());
            address.setCountry(addressDto.getCountry());
            address.setStreet(addressDto.getStreet());
            address.setNumber(addressDto.getNumber());
            address.setLatitude(addressDto.getLatitude());
            address.setLongitude(addressDto.getLongitude());

            address = addressRepository.save(address);

            // Map RegisterUserDto to the User entity
            User newUser = new User();
            newUser.setUsername(registerUserDto.getUsername());
            newUser.setEmail(registerUserDto.getEmail());
            newUser.setPassword(passwordEncoder.encode(registerUserDto.getPassword()));
            newUser.setName(registerUserDto.getName());
            newUser.setSurname(registerUserDto.getSurname());
            newUser.setRole(UserRole.USER);
            newUser.setActive(true);
            newUser.setVerified(false);
            newUser.setAddress(address);
            newUser.setLastLoggedIn(null);
            newUser.setSentMail(false);
            // Save the new user in the repository
            var user = userRepository.save(newUser);

            // Add the username to the bloom filter
            bloomFilterService.add(user.getUsername());

            // Send a verification email
            emailService.send(user.getEmail(), true);

            // Create a UserDto object to return
            var userDto = new UserDto();
            userDto.setName(user.getName());
            userDto.setSurname(user.getSurname());
            userDto.setUsername(user.getUsername());
            userDto.setEmail(user.getEmail());
            userDto.setAddress(addressDto);

            return Result.success(userDto);
        } catch (Exception e) {
            return Result.failure("An error occurred while registering the user: " + e.getMessage(), 412);
        }
    }

    @Override
    public Result<UserLoginDto> loginUser(LoginUserDto loginUserDto) {
        // Check if a user with the given username exists
        var userOptional = userRepository.findByEmail(loginUserDto.getEmail());

        if(userOptional.isEmpty()) {
            return Result.failure("User not found", 404);
        }

        var user = userOptional.get();

        if(!user.isActive()) {
            return Result.failure("User is not active", 409);
        }

        if(!user.isVerified()) {
            return Result.failure("User is not verified", 403);
        }

        // Check if the password is correct
        if(!passwordEncoder.matches(loginUserDto.getPassword(), user.getPassword())) {
            return Result.failure("Incorrect password", 401);
        }

        // Generate JWT access token
        String jwtToken = jwtService.generateToken(user.getUsername(), user.getRole().toString());

        // Create a UserLoginDto object to return
        UserLoginDto userLoginDto = new UserLoginDto();
        userLoginDto.setUsername(user.getUsername());
        userLoginDto.setAccessToken(jwtToken); // Set the generated token

        return Result.success(userLoginDto);
    }
}