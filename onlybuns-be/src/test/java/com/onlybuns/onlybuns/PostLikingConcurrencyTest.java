package com.onlybuns.onlybuns;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import com.onlybuns.onlybuns.domain.services.PostService;
import com.onlybuns.onlybuns.domain.services.UserService;
import com.onlybuns.onlybuns.domain.services.ImageService;
import com.onlybuns.onlybuns.domain.models.Image;
import com.onlybuns.onlybuns.presentation.dtos.requests.AddressDto;
import com.onlybuns.onlybuns.presentation.dtos.requests.RegisterUserDto;
import com.onlybuns.onlybuns.presentation.dtos.responses.PostDto;
import com.onlybuns.onlybuns.presentation.dtos.responses.UserDto;
import com.onlybuns.onlybuns.core.misc.Result;

import java.time.LocalDateTime;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@ActiveProfiles("test")
@TestPropertySource(locations = "classpath:application-test.properties")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
public class PostLikingConcurrencyTest {

    @Autowired
    private PostService postService;

    @Autowired
    private UserService userService;

    @MockBean
    private ImageService imageService;

    private Long testPostId;
    private String postOwnerUsername;

    @BeforeEach
    public void setUp() {
        // Mock image service behavior
        Image mockImage = new Image();
        mockImage.setId(1L);
        mockImage.setMimetype("image/jpeg");
        mockImage.setUploadedAt(LocalDateTime.now());
        
        when(imageService.saveImage(any(MockMultipartFile.class)))
            .thenReturn(Result.success(mockImage));
        when(imageService.getImageBase64(anyLong()))
            .thenReturn(Result.success("base64encodedstring"));

        // Create post owner user
        postOwnerUsername = "postowner" + System.currentTimeMillis();
        RegisterUserDto ownerDto = createTestUserDto(postOwnerUsername, postOwnerUsername + "@example.com");
        Result<UserDto> ownerResult = userService.registerUser(ownerDto);
        assertTrue(ownerResult.isSuccess());

        // Create test post
        MockMultipartFile imageFile = new MockMultipartFile(
            "image", 
            "test.jpg", 
            "image/jpeg", 
            "test image content".getBytes()
        );
        
        AddressDto addressDto = createTestAddressDto();
        Result<PostDto> postResult = postService.createPost(
            "Test post for concurrency testing", 
            imageFile, 
            addressDto, 
            postOwnerUsername
        );
        assertTrue(postResult.isSuccess());
        testPostId = postResult.getData().getId();
    }

    @Test
    public void testConcurrentPostLikes() throws InterruptedException {
        // Create multiple users that will try to like the same post concurrently
        int threadCount = 5;
        String[] usernames = new String[threadCount];
        
        // Create users for concurrent liking
        for (int i = 0; i < threadCount; i++) {
            String username = "likeuser" + i + "_" + System.currentTimeMillis();
            usernames[i] = username;
            RegisterUserDto userDto = createTestUserDto(username, username + "@example.com");
            Result<UserDto> userResult = userService.registerUser(userDto);
            assertTrue(userResult.isSuccess());
        }

        // Setup for concurrent execution
        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(threadCount);
        AtomicInteger successCount = new AtomicInteger(0);
        AtomicInteger failureCount = new AtomicInteger(0);

        // Concurrent liking attempts - different users should all succeed
        for (int i = 0; i < threadCount; i++) {
            final String username = usernames[i];
            executorService.submit(() -> {
                try {
                    Result<PostDto> result = postService.likePost(testPostId, username);
                    
                    if (result.isSuccess()) {
                        successCount.incrementAndGet();
                        System.out.println("User " + username + " successfully liked post. Like count: " + 
                                         result.getData().getNumberOfLikes());
                    } else {
                        failureCount.incrementAndGet();
                        System.out.println("User " + username + " failed to like post: " + result.getMessage());
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

        // Assert that all different users succeeded in liking the post
        assertEquals(threadCount, successCount.get(), "All users should succeed in liking the post");
        assertEquals(0, failureCount.get(), "No like attempts should fail when different users are liking");
    }

    @Test
    public void testConcurrentSameUserLikes() throws InterruptedException {
        // Create one user who will try to like the same post multiple times concurrently
        String username = "sameuser" + System.currentTimeMillis();
        RegisterUserDto userDto = createTestUserDto(username, username + "@example.com");
        Result<UserDto> userResult = userService.registerUser(userDto);
        assertTrue(userResult.isSuccess());

        // Setup for concurrent execution
        int threadCount = 3;
        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(threadCount);
        AtomicInteger successCount = new AtomicInteger(0);
        AtomicInteger failureCount = new AtomicInteger(0);

        // Concurrent liking attempts by the same user - only one should succeed
        for (int i = 0; i < threadCount; i++) {
            executorService.submit(() -> {
                try {
                    Result<PostDto> result = postService.likePost(testPostId, username);
                    
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

        // Assert that exactly one like succeeded and others failed
        assertEquals(1, successCount.get(), "Only one like attempt should succeed for the same user");
        assertEquals(threadCount - 1, failureCount.get(), 
                    "Other like attempts should fail due to duplicate like prevention");
    }

    @Test
    public void testConcurrentLikeAndUnlike() throws InterruptedException {
        // Create a user who will like and unlike the post concurrently
        String username = "likeunlikeuser" + System.currentTimeMillis();
        RegisterUserDto userDto = createTestUserDto(username, username + "@example.com");
        Result<UserDto> userResult = userService.registerUser(userDto);
        assertTrue(userResult.isSuccess());

        // First, like the post
        Result<PostDto> initialLike = postService.likePost(testPostId, username);
        assertTrue(initialLike.isSuccess());

        // Setup for concurrent execution
        int threadCount = 4; // 2 likes, 2 unlikes
        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(threadCount);
        AtomicInteger likeSuccessCount = new AtomicInteger(0);
        AtomicInteger unlikeSuccessCount = new AtomicInteger(0);
        AtomicInteger failureCount = new AtomicInteger(0);

        // Concurrent like and unlike attempts
        for (int i = 0; i < threadCount; i++) {
            final boolean shouldLike = i % 2 == 0; // alternating like/unlike
            executorService.submit(() -> {
                try {
                    Result<PostDto> result;
                    if (shouldLike) {
                        result = postService.likePost(testPostId, username);
                        if (result.isSuccess()) {
                            likeSuccessCount.incrementAndGet();
                        } else {
                            failureCount.incrementAndGet();
                        }
                    } else {
                        result = postService.unlikePost(testPostId, username);
                        if (result.isSuccess()) {
                            unlikeSuccessCount.incrementAndGet();
                        } else {
                            failureCount.incrementAndGet();
                        }
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

        // The total successful operations should be consistent
        // Either the post ends up liked or unliked, but operations should be atomic
        int totalSuccessful = likeSuccessCount.get() + unlikeSuccessCount.get();
        assertTrue(totalSuccessful > 0, "At least some operations should succeed");
        assertTrue(failureCount.get() > 0, "Some operations should fail due to state conflicts");
        
        System.out.println("Like successes: " + likeSuccessCount.get());
        System.out.println("Unlike successes: " + unlikeSuccessCount.get());
        System.out.println("Failures: " + failureCount.get());
    }

    private RegisterUserDto createTestUserDto(String username, String email) {
        RegisterUserDto userDto = new RegisterUserDto();
        userDto.setUsername(username);
        userDto.setEmail(email);
        userDto.setPassword("Password123!");
        userDto.setConfirmPassword("Password123!");
        userDto.setName("Test");
        userDto.setSurname("User");
        userDto.setAddress(createTestAddressDto());
        return userDto;
    }

    private AddressDto createTestAddressDto() {
        AddressDto addressDto = new AddressDto();
        addressDto.setCity("Test City");
        addressDto.setCountry("Test Country");
        addressDto.setStreet("Test Street");
        addressDto.setNumber("123");
        addressDto.setLatitude(44.8176);
        addressDto.setLongitude(20.4633);
        return addressDto;
    }
}
