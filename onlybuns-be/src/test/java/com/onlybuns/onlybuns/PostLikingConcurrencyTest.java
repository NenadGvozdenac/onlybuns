package com.onlybuns.onlybuns;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.context.annotation.Import;

import com.onlybuns.onlybuns.config.TestRedisConfiguration;
import com.onlybuns.onlybuns.domain.services.PostService;
import com.onlybuns.onlybuns.domain.services.ImageService;
import com.onlybuns.onlybuns.presentation.dtos.responses.PostDto;
import com.onlybuns.onlybuns.core.misc.Result;
import com.onlybuns.onlybuns.domain.models.Image;
import com.onlybuns.onlybuns.domain.models.Post;
import com.onlybuns.onlybuns.domain.models.User;
import com.onlybuns.onlybuns.domain.models.UserRole;
import com.onlybuns.onlybuns.infrastructure.interfaces.PostRepositoryInterface;
import com.onlybuns.onlybuns.infrastructure.interfaces.UserRepository;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(properties = {"spring.cache.type=none"})
@ActiveProfiles("test")
@TestPropertySource(locations = "classpath:application-test.properties")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@Import(TestRedisConfiguration.class)
@EnableAutoConfiguration(exclude = {
    org.springframework.boot.autoconfigure.data.redis.RedisRepositoriesAutoConfiguration.class,
    org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration.class
})
public class PostLikingConcurrencyTest {

    @Autowired
    private PostService postService;

    @Autowired
    private PostRepositoryInterface postRepositoryjpa;

    @Autowired
    private UserRepository userRepository;

    @MockBean
    private ImageService imageService;

    private Long testPostId;
    private List<String> testUsernames;

    @BeforeEach
    public void setupTestData() throws Exception {
        // Mock the image service to return test base64 data
        when(imageService.getImageBase64(anyLong())).thenReturn(Result.success("dGVzdCBpbWFnZSBkYXRh")); // "test image data" in base64
        
        testUsernames = new ArrayList<>();
        
        // Create a test user who will create the post directly via repository
        User postCreator = new User();
        postCreator.setUsername("postcreator" + System.currentTimeMillis());
        postCreator.setEmail(postCreator.getUsername() + "@example.com");
        postCreator.setName("Post");
        postCreator.setSurname("Creator");
        postCreator.setPassword("hashedpassword");
        postCreator.setRole(UserRole.USER);
        postCreator.setVerified(true);
        postCreator.setActive(true);
        postCreator = userRepository.save(postCreator);
        
        // Create a simple image for the post (don't save separately - let cascade handle it)
        Image testImage = new Image();
        testImage.setPath("test-image.jpg");
        testImage.setUploadedAt(LocalDateTime.now());
        testImage.setMimetype("image/jpeg");
        testImage.setCompressed(false);
        // Don't save the image separately - let the cascade from Post handle it
        
        Post post = new Post();
        post.setUser(postCreator);
        post.setDescription("Test post for concurrent liking");
        post.setDateOfCreation(LocalDateTime.now());
        post.setNumberOfLikes(0);
        post.setDeleted(false);
        post.setMarkedForAdvertisement(false);
        post.setLocation(null); // Skip location for test simplicity
        post.setImage(testImage); // Use the created image
        post.setUsersThatLiked(new ArrayList<>());
        post.setComments(new ArrayList<>());
        
        post = postRepositoryjpa.save(post);
        testPostId = post.getId();

        // Create multiple test users who will like the post directly via repository
        for (int i = 0; i < 5; i++) {
            String username = "testuser" + i + "_" + System.currentTimeMillis();
            User user = new User();
            user.setUsername(username);
            user.setEmail(username + "@example.com");
            user.setName("Test");
            user.setSurname("User" + i);
            user.setPassword("hashedpassword");
            user.setRole(UserRole.USER);
            user.setVerified(true);
            user.setActive(true);
            userRepository.save(user);
            testUsernames.add(username);
        }
    }

    @Test
    public void testConcurrentPostLiking() throws InterruptedException {
        int threadCount = testUsernames.size();
        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(threadCount);
        AtomicInteger successCount = new AtomicInteger(0);
        AtomicInteger failureCount = new AtomicInteger(0);
        AtomicReference<Integer> maxObservedLikeCount = new AtomicReference<>(0);

        // Initial like count should be 0 for a new post
        int initialLikes = 0;

        // Concurrent liking attempts
        for (int i = 0; i < threadCount; i++) {
            final String username = testUsernames.get(i);
            executorService.submit(() -> {
                try {
                    Result<PostDto> result = postService.likePost(testPostId, username);
                    
                    if (result.isSuccess()) {
                        successCount.incrementAndGet();
                        // Track the highest like count observed
                        synchronized (maxObservedLikeCount) {
                            int currentCount = result.getData().getNumberOfLikes();
                            if (currentCount > maxObservedLikeCount.get()) {
                                maxObservedLikeCount.set(currentCount);
                            }
                        }
                    } else {
                        failureCount.incrementAndGet();
                        System.out.println("Like failed for user " + username + ": " + result.getMessage());
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

        // Verify results
        assertEquals(threadCount, successCount.get(), "All users should successfully like the post");
        assertEquals(0, failureCount.get(), "No like operations should fail");

        // Verify final like count is correct
        assertEquals(initialLikes + threadCount, maxObservedLikeCount.get(), 
                    "Final like count should be initial likes + number of users who liked");
    }

    @Test
    public void testConcurrentLikeAndUnlike() throws InterruptedException {
        // First, have 2 users like the post (leaving users at indices 2, 3, 4 available)
        List<Integer> likeCounts = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            Result<PostDto> likeResult = postService.likePost(testPostId, testUsernames.get(i));
            assertTrue(likeResult.isSuccess(), "User " + testUsernames.get(i) + " should like the post successfully");
            likeCounts.add(likeResult.getData().getNumberOfLikes());
        }

        int threadCount = 5; // 2 users will unlike, 3 users will like
        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(threadCount);
        AtomicInteger successCount = new AtomicInteger(0);
        AtomicInteger failureCount = new AtomicInteger(0);
        AtomicReference<Integer> finalLikeCount = new AtomicReference<>(0);

        // Get initial like count from the last like operation
        int initialLikes = likeCounts.get(likeCounts.size() - 1);

        // Unlike operations for the first 2 users who already liked
        for (int i = 0; i < 2; i++) {
            final String unlikeUsername = testUsernames.get(i);
            executorService.submit(() -> {
                try {
                    Result<PostDto> result = postService.unlikePost(testPostId, unlikeUsername);
                    if (result.isSuccess()) {
                        successCount.incrementAndGet();
                        synchronized (finalLikeCount) {
                            finalLikeCount.set(result.getData().getNumberOfLikes());
                        }
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

        // Like operations for the remaining 3 users (indices 2, 3, 4)
        for (int i = 2; i < 5; i++) {
            final String likeUsername = testUsernames.get(i);
            executorService.submit(() -> {
                try {
                    Result<PostDto> result = postService.likePost(testPostId, likeUsername);
                    if (result.isSuccess()) {
                        successCount.incrementAndGet();
                        synchronized (finalLikeCount) {
                            finalLikeCount.set(result.getData().getNumberOfLikes());
                        }
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

        // Verify results
        assertEquals(threadCount, successCount.get(), "All like/unlike operations should succeed");
        assertEquals(0, failureCount.get(), "No operations should fail");

        // The final like count should be initial likes - 2 (unlikes) + 3 (likes) = initial + 1
        assertEquals(initialLikes + 1, finalLikeCount.get(), 
                    "Final like count should be initial likes + 1 (2 unlikes + 3 likes = +1 net change)");
    }

    @Test
    public void testDuplicateLikeAttempts() throws InterruptedException {
        // First, have one user like the post
        String testUsername = testUsernames.get(0);
        Result<PostDto> initialLike = postService.likePost(testPostId, testUsername);
        assertTrue(initialLike.isSuccess(), "Initial like should succeed");
        int initialLikeCount = initialLike.getData().getNumberOfLikes();

        int threadCount = 3;
        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(threadCount);
        AtomicInteger successCount = new AtomicInteger(0);
        AtomicInteger failureCount = new AtomicInteger(0);

        // Concurrent duplicate like attempts by the same user
        for (int i = 0; i < threadCount; i++) {
            executorService.submit(() -> {
                try {
                    Result<PostDto> result = postService.likePost(testPostId, testUsername);
                    
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

        // Verify results - all duplicate attempts should fail
        assertEquals(0, successCount.get(), "All duplicate like attempts should fail");
        assertEquals(threadCount, failureCount.get(), "All attempts should be rejected as duplicates");

        // Verify like count remains unchanged by trying to like with a different user
        String differentUser = testUsernames.get(1);
        Result<PostDto> verificationLike = postService.likePost(testPostId, differentUser);
        assertTrue(verificationLike.isSuccess(), "Should be able to like with a different user");
        assertEquals(initialLikeCount + 1, verificationLike.getData().getNumberOfLikes(), 
                    "Like count should only increase by 1 after the new user likes");
    }
}
