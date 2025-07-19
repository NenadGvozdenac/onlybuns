package com.onlybuns.onlybuns;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import org.springframework.context.annotation.Import;
import com.onlybuns.onlybuns.config.TestRedisConfiguration;

import com.onlybuns.onlybuns.domain.services.UserService;
import com.onlybuns.onlybuns.presentation.dtos.requests.AddressDto;
import com.onlybuns.onlybuns.presentation.dtos.requests.RegisterUserDto;
import com.onlybuns.onlybuns.presentation.dtos.responses.UserDto;
import com.onlybuns.onlybuns.core.misc.Result;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Import(TestRedisConfiguration.class)
@EnableAutoConfiguration(exclude = {
    org.springframework.boot.autoconfigure.data.redis.RedisRepositoriesAutoConfiguration.class,
    org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration.class
})
public class UserRegistrationConcurrencyTest {

    @Autowired
    private UserService userService;

    @Test
    public void testConcurrentUserRegistration() throws InterruptedException {
        // Prepare a common username and email for concurrent registration
        String username = "testuser" + System.currentTimeMillis();
        String email = username + "@example.com";

        // Create RegisterUserDto for the first user
        RegisterUserDto firstUserDto = new RegisterUserDto();
        firstUserDto.setUsername(username);
        firstUserDto.setEmail(email);
        firstUserDto.setPassword("Password123!");
        firstUserDto.setConfirmPassword("Password123!");
        firstUserDto.setName("Test");
        firstUserDto.setSurname("User");
        
        // Create address for the user
        AddressDto addressDto = new AddressDto();
        addressDto.setCity("Test City");
        addressDto.setCountry("Test Country");
        addressDto.setStreet("Test Street");
        addressDto.setNumber("123");
        addressDto.setLatitude(0.0);
        addressDto.setLongitude(0.0);
        firstUserDto.setAddress(addressDto);

        // Setup for concurrent execution
        int threadCount = 2;
        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(threadCount);
        AtomicInteger successCount = new AtomicInteger(0);
        AtomicInteger failureCount = new AtomicInteger(0);

        // Concurrent registration attempts
        for (int i = 0; i < threadCount; i++) {
            executorService.submit(() -> {
                try {
                    Result<UserDto> result = userService.registerUser(firstUserDto);
                    
                    if (result.isSuccess()) {
                        successCount.incrementAndGet();
                    } else {
                        failureCount.incrementAndGet();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    failureCount.incrementAndGet();
                } finally {
                    latch.countDown();
                }
            });
        }

        // Wait for all threads to complete
        latch.await();
        executorService.shutdown();

        // Assert that exactly one registration succeeded
        assertEquals(1, successCount.get(), "Only one user registration should succeed");
        assertEquals(1, failureCount.get(), "One user registration should fail");
    }
}