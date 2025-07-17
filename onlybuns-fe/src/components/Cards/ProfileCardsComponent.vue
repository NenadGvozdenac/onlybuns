<template>
    <!-- Bootstrap Version -->
    <div class="d-flex justify-content-center align-items-center vh-75 bg-light my-3">
        <div class="container card shadow-lg rounded-lg hover-scale">
            <div class="d-flex align-items-center mt-2 mb-2 justify-content-between">
                <div class="d-flex align-items-center">
                    <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor"
                        class="bi bi-calendar" viewBox="0 0 16 16">
                        <path
                            d="M3.5 0a.5.5 0 0 1 .5.5V1h8V.5a.5.5 0 0 1 1 0V1h1a2 2 0 0 1 2 2v11a2 2 0 0 1-2 2H2a2 2 0 0 1-2-2V3a2 2 0 0 1 2-2h1V.5a.5.5 0 0 1 .5-.5M1 4v10a1 1 0 0 0 1 1h12a1 1 0 0 0 1-1V4z" />
                    </svg>
                    <p class="mb-0 ms-1">{{ formattedDate }}</p>
                    <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor"
                        class="bi bi-clock ms-2" viewBox="0 0 16 16">
                        <path d="M8 3.5a.5.5 0 0 0-1 0V9a.5.5 0 0 0 .252.434l3.5 2a.5.5 0 0 0 .496-.868L8 8.71z" />
                        <path d="M8 16A8 8 0 1 0 8 0a8 8 0 0 0 0 16m7-8A7 7 0 1 1 1 8a7 7 0 0 1 14 0" />
                    </svg>
                    <p class="mb-0 ms-1">{{ formattedTime }}</p>
                </div>
                <!-- Edit and Delete Icons -->
                <div class="d-flex align-items-center">
                    <button class="icon-button" @click="showEditModal">
                        <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor"
                            class="bi bi-pencil ms-2" viewBox="0 0 16 16">
                            <path
                                d="M12.146.146a.5.5 0 0 1 .708 0l3 3a.5.5 0 0 1 0 .708l-10 10a.5.5 0 0 1-.168.11l-5 2a.5.5 0 0 1-.65-.65l2-5a.5.5 0 0 1 .11-.168l10-10zM11.207 2.5 13.5 4.793 14.793 3.5 12.5 1.207zm1.586 3L10.5 3.207 4 9.707V10h.5a.5.5 0 0 1 .5.5v.5h.5a.5.5 0 0 1 .5.5v.5h.293zm-9.761 5.175-.106.106-1.528 3.821 3.821-1.528.106-.106A.5.5 0 0 1 5 12.5V12h-.5a.5.5 0 0 1-.5-.5V11h-.5a.5.5 0 0 1-.468-.325z" />
                        </svg>
                    </button>
                    <button class="icon-button-danger" @click="showDeleteModal">
                        <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor"
                            class="bi bi-trash ms-2" viewBox="0 0 16 16">
                            <path
                                d="M5.5 5.5A.5.5 0 0 1 6 6v6a.5.5 0 0 1-1 0V6a.5.5 0 0 1 .5-.5m2.5 0a.5.5 0 0 1 .5.5v6a.5.5 0 0 1-1 0V6a.5.5 0 0 1 .5-.5m3 .5a.5.5 0 0 0-1 0v6a.5.5 0 0 0 1 0z" />
                            <path
                                d="M14.5 3a1 1 0 0 1-1 1H13v9a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2V4h-.5a1 1 0 0 1-1-1V2a1 1 0 0 1 1-1H6a1 1 0 0 1 1-1h2a1 1 0 0 1 1 1h3.5a1 1 0 0 1 1 1zM4.118 4 4 4.059V13a1 1 0 0 0 1 1h6a1 1 0 0 0 1-1V4.059L11.882 4zM2.5 3h11V2h-11z" />
                        </svg>
                    </button>

                </div>
            </div>
            <!-- Image -->
            <img class="card-img-top" style="max-height: 70vh; overflow: hidden;"
                :src="'data:' + image.mimeType + ';base64,' + image.data" :alt="`Post image did not load`" />

            <div class="card-body">
                <p class="text-muted small">{{ truncatedDescription }}</p>
            </div>

            <div class="d-flex justify-content-between align-items-center mb-3">
                <!-- Author Info -->
                <div class="d-flex align-items-center">
                    <h2 class="h6 mb-0"># {{ username }}</h2>
                </div>

                <!-- Interaction Icons -->
                <div class="d-flex">
                    <!-- Comments -->
                    <div class="d-flex align-items-center me-3">
                        <button class="icon-button" @click="showModal">
                            <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" fill="none"
                                viewBox="0 0 24 24" stroke="currentColor" class=" text-secondary">
                                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                                    d="M8 12h.01M12 12h.01M16 12h.01M21 12c0 4.418-4.03 8-9 8a9.863 9.863 0 01-4.255-.949L3 20l1.395-3.72C3.512 15.042 3 13.574 3 12c0-4.418 4.03-8 9-8s9 3.582 9 8z" />
                            </svg>
                        </button>
                        <span>{{ commentsCount }}</span>
                    </div>

                    <!-- Likes -->
                    <div class="d-flex align-items-center">
                        <button v-if="hasLiked" class="icon-button" @click="checkIfLoggedUnlike">
                            <svg xmlns="http://www.w3.org/2000/svg" width="17" height="17" fill="currentColor"
                                class="bi bi-heart-fill text-danger" viewBox="0 0 16 16">
                                <path fill-rule="evenodd"
                                    d="M8 1.314C12.438-3.248 23.534 4.735 8 15-7.534 4.736 3.562-3.248 8 1.314" />
                            </svg>
                        </button>

                        <button v-else class="icon-button" @click="checkIfLoggedLike">
                            <svg xmlns="http://www.w3.org/2000/svg" width="18" height="18" fill="currentColor"
                                class="bi bi-heart" viewBox="0 0 16 16">
                                <path
                                    d="m8 2.748-.717-.737C5.6.281 2.514.878 1.4 3.053c-.523 1.023-.641 2.5.314 4.385.92 1.815 2.834 3.989 6.286 6.357 3.452-2.368 5.365-4.542 6.286-6.357.955-1.886.838-3.362.314-4.385C13.486.878 10.4.28 8.717 2.01zM8 15C-7.333 4.868 3.279-3.04 7.824 1.143q.09.083.176.171a3 3 0 0 1 .176-.17C12.72-3.042 23.333 4.867 8 15" />
                            </svg>
                        </button>
                        <span class="ms-1">{{ likes }}</span>
                    </div>
                </div>
            </div>
        </div>
    </div>


    <!-- Modal for full post-->
    <div class="modal fade" :id="'commentModal-' + id" tabindex="-1" aria-labelledby="commentModalLabel"
        aria-hidden="true">
        <div class="modal-dialog modal-xl" style="max-width: 1200px; margin-top: 2vh;">
            <div class="modal-content border-0" style="min-height: 96vh;">
                <div class="modal-header border-bottom p-2 d-flex align-items-center justify-content-between">
                    <div class="d-flex align-items-center">
                        <img src="https://flowbite.com/docs/images/logo.svg" alt="OnlyBuns Logo" class="h-8 me-2">
                        <h5 class="h5 mb-0">OnlyBuns</h5>
                    </div>
                    <button type="button" class="btn-close me-1" data-bs-dismiss="modal" aria-label="Close">
                    </button>
                </div>
                <div class="modal-body p-0">
                    <div class="row g-0">
                        <!-- Left side - Image -->
                        <div class="col-9 bg-black d-flex align-items-center justify-content-center border-end">
                            <img :src="'data:' + image.mimeType + ';base64,' + image.data"
                                :alt="`Post image did not load`" class="w-100 h-100"
                                style="object-fit: contain; max-height: calc(96vh - 56px);" />
                        </div>

                        <!-- Right side - Content -->
                        <div class="col-3 d-flex flex-column" style="height: calc(96vh - 56px);">
                            <!-- User info and date -->
                            <div class="p-3 border-bottom">
                                <h3 class="h6 mb-2 fw-bold text-break">{{ username }}</h3>
                                <div class="d-flex align-items-center text-muted small mb-2">
                                    <svg xmlns="http://www.w3.org/2000/svg" width="14" height="14" fill="currentColor"
                                        class="bi bi-calendar me-1" viewBox="0 0 16 16">
                                        <path
                                            d="M3.5 0a.5.5 0 0 1 .5.5V1h8V.5a.5.5 0 0 1 1 0V1h1a2 2 0 0 1 2 2v11a2 2 0 0 1-2 2H2a2 2 0 0 1-2-2V3a2 2 0 0 1 2-2h1V.5a.5.5 0 0 1 .5-.5M1 4v10a1 1 0 0 0 1 1h12a1 1 0 0 0 1-1V4z" />
                                    </svg>
                                    {{ formattedDate }}
                                    <svg xmlns="http://www.w3.org/2000/svg" width="14" height="14" fill="currentColor"
                                        class="bi bi-clock ms-2 me-1" viewBox="0 0 16 16">
                                        <path
                                            d="M8 3.5a.5.5 0 0 0-1 0V9a.5.5 0 0 0 .252.434l3.5 2a.5.5 0 0 0 .496-.868L8 8.71z" />
                                        <path d="M8 16A8 8 0 1 0 8 0a8 8 0 0 0 0 16m7-8A7 7 0 1 1 1 8a7 7 0 0 1 14 0" />
                                    </svg>
                                    {{ formattedTime }}
                                </div>
                                <p class="text-muted small mb-0 text-break">{{ truncatedDescription }}</p>
                            </div>

                            <!-- Comments section with enhanced scrolling -->
                            <div class="d-flex flex-column flex-grow-1" style="min-height: 0; overflow: hidden;">
                                <h6 class="text-muted small px-3 pt-3 mb-2 bg-white border-bottom">Comments:</h6>
                                <div class="comments-section overflow-y-auto flex-grow-1" style="max-height: calc(96vh - 250px);">
                                    <ul class="list-unstyled mb-0">
                                        <li v-for="(comment, index) in comments" :key="index"
                                            class="border-bottom px-3 py-3 hover-bg-light">
                                            <div class="comment-item">
                                                <span class="username fw-semibold text-primary me-1">@{{
                                                    comment.username }}</span>
                                                <span class="comment-text text-dark">{{ comment.comment }}</span>
                                                <div class="comment-meta text-muted small mt-1">
                                                    <svg xmlns="http://www.w3.org/2000/svg" width="12" height="12"
                                                        fill="currentColor" class="bi bi-clock me-1"
                                                        viewBox="0 0 16 16">
                                                        <path
                                                            d="M8 3.5a.5.5 0 0 0-1 0V9a.5.5 0 0 0 .252.434l3.5 2a.5.5 0 0 0 .496-.868L8 8.71z" />
                                                        <path
                                                            d="M8 16A8 8 0 1 0 8 0a8 8 0 0 0 0 16m7-8A7 7 0 1 1 1 8a7 7 0 0 1 14 0" />
                                                    </svg>
                                                    {{ formatCommentDate(comment.commentedAt) }}
                                                </div>
                                            </div>
                                        </li>
                                    </ul>
                                </div>
                            </div>

                            <!-- Comment input (always visible at bottom) -->
                            <div class="border-top p-3 mt-auto bg-white">
                                <div class="input-group">
                                    <input type="text" class="form-control bg-light border-0 text-break"
                                        placeholder="Add a comment..." aria-label="Add a comment" />
                                    <button class="btn btn-link text-decoration-none flex-shrink-0"
                                        @click="postComment">Post</button>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <!-- Modal for register and log in-->
    <div class="modal fade" id="loginPromptModal" tabindex="-1" aria-labelledby="loginPromptModalLabel"
        aria-hidden="true">
        <div class="modal-dialog modal-xl" style="max-width: 600px;">
            <div class="modal-content border-0" style="border-radius: 16px;">
                <div class="modal-header border-bottom-0 p-2">
                    <div class="ms-auto">
                        <button type="button" class="btn-close me-2" data-bs-dismiss="modal"
                            aria-label="Close"></button>
                    </div>
                </div>

                <div class="modal-body text-center px-4 pb-4 pt-0">
                    <!-- Logo and Title Section -->
                    <div class="mb-4">
                        <img src="https://flowbite.com/docs/images/logo.svg" alt="OnlyBuns Logo"
                            class="h-16 mx-auto mb-3">
                        <h4 class="fw-bold mb-1">Welcome to OnlyBuns</h4>
                        <p class="text-muted small">Your Community Awaits!</p>
                    </div>

                    <!-- Divider with style -->
                    <div class="d-flex align-items-center mb-4">
                        <div class="flex-grow-1 border-bottom"></div>
                        <div class="mx-3">
                            <img src="https://flowbite.com/docs/images/logo.svg" alt="OnlyBuns Logo" class="h-6"
                                style="height: 24px; opacity: 0.6;">
                        </div>
                        <div class="flex-grow-1 border-bottom"></div>
                    </div>

                    <!-- Message Section -->
                    <div class="mb-4">
                        <p class="mb-2" style="color: #555;">To comment, like, and engage with our community, please log
                            in or register.</p>
                        <p style="color: #666;">We value your contributions and can't wait to see you join the fun!</p>
                    </div>

                    <!-- Buttons with improved styling -->
                    <div class="d-flex justify-content-center gap-2 mt-4">
                        <button type="button" class="btn btn-primary px-4 py-2"
                            style="min-width: 120px; border-radius: 8px;" @click="goToLogin">
                            Log In
                        </button>
                        <button type="button" class="btn btn-outline-secondary px-4 py-2"
                            style="min-width: 120px; border-radius: 8px;" @click="goToRegister">
                            Register
                        </button>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <!-- Modal for editing post-->
    <div class="modal fade" :id="'editModal-' + id" tabindex="-1" aria-labelledby="editModalLabel" aria-hidden="true">
        <div class="modal-dialog modal-xl shadow-lg" style="max-width: 700px; margin-top: 2vh;">
            <div class="modal-content border-0 bg-white" style="min-height: 85vh;">
                <!-- Header -->
                <div class="modal-header bg-light py-2 px-3">
                    <div class="d-flex align-items-center gap-2">
                        <img src="https://flowbite.com/docs/images/logo.svg" alt="OnlyBuns Logo" class="h-8">
                        <h5 class="modal-title fw-semibold mb-0">OnlyBuns</h5>
                    </div>
                    <button type="button" class="btn-close shadow-none" data-bs-dismiss="modal" aria-label="Close"
                        @click="closeModal"></button>
                </div>

                <!-- Body -->
                <div class="modal-body p-0">
                    <div class="row g-0">
                        <!-- Image Section -->
                        <div class="col-12 bg-dark d-flex align-items-center justify-content-center"
                            style="max-height: 60vh;">
                            <img :src="'data:' + image.mimeType + ';base64,' + image.data" :alt="'Post image'"
                                class="h-100 w-100" style="object-fit: contain;" />
                        </div>

                        <!-- Edit Section -->
                        <div class="col-12 bg-white">
                            <div class="p-3">
                                <label for="description" class="form-label fw-semibold mb-2">Edit Description</label>
                                <textarea id="descriptionToChange" class="form-control border shadow-sm" rows="4"
                                    v-model="descriptionToChange" style="resize: none;"></textarea>
                            </div>

                            <!-- Action Button -->
                            <div class="px-3 pb-3 mt-3">
                                <button class="btn btn-primary w-100 py-2 fw-semibold" @click="updatePost">
                                    Update Post
                                </button>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <!-- Delete Confirmation Modal -->
    <div class="modal fade" :id="'deleteModal-' + id" tabindex="-1" aria-labelledby="deleteModalLabel" aria-hidden="true">
            <div class="modal-dialog modal-sm">
                <div class="modal-content border-0" style="border-radius: 8px;">
                    <!-- Modal Header -->
                    <div class="modal-header bg-danger text-white p-2">
                        <h5 class="modal-title fw-bold" id="deleteModalLabel">Confirm Deletion</h5>
                    </div>

                    <!-- Modal Body -->
                    <div class="modal-body text-center">
                        <p>Are you sure you want to delete this post? This action cannot be undone.</p>
                    </div>

                    <!-- Modal Footer -->
                    <div class="modal-footer justify-content-center">
                        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancel</button>
                        <button type="button" class="btn btn-danger" @click="deletePost">Delete</button>
                    </div>
                </div>
            </div>
        </div>

</template>

<script>
import CardService from '@/services/CardService';

export default {
    name: 'ProfileCardsComponent',
    props: {
        id: {
            type: Number,
            required: true
        },
        image: {
            type: Array,
            required: true
        },
        description: {
            type: String,
            default: ''
        },
        likesCount: {
            type: Number,
            default: 0
        },
        commentsCount: {
            type: Number,
            default: 0
        },
        username: {
            type: String,
            required: true
        },
        dateOfCreation: {
            type: Array,
            required: true
        },
        usersThatLike: {
            type: Array,
        },
        comments: {
            type: Array
        }
    },
    data() {
        let token = localStorage.getItem('token');
        let myUser = '';

        if (token) {
            try {
                myUser = JSON.parse(atob(token.split('.')[1])).sub;
            } catch (error) {
                console.error("Failed to parse token:", error);
                myUser = '';
            }
        }

        return {
            likes: 0,
            myUsername: myUser,
            hasLiked: false,
            descriptionToChange: ''
        };
    },
    created() {
        this.likes = this.likesCount;
        this.hasLiked = this.usersThatLike != null ? this.usersThatLike.some(user => user.username === this.myUsername) : false;
        this.descriptionToChange = this.description;
    },
    computed: {
        truncatedDescription() {
            return this.description.length > 150
                ? this.description.slice(0, 150) + '...'
                : this.description;
        },
        formattedDate() {
            const [year, month, day] = this.dateOfCreation;
            return `${String(day).padStart(2, '0')}.${String(month).padStart(2, '0')}.${year}`;
        },
        formattedTime() {
            const [year, month, day, hour, minute] = this.dateOfCreation;
            const formattedHour = String(hour).padStart(2, '0');
            const formattedMinute = String(minute).padStart(2, '0');
            return `${formattedHour}:${formattedMinute}`;
        },
    },
    methods: {
        checkIfLoggedLike() {
            if (this.myUsername === '') {
                this.showLoginPrompt();
                return;
            }
            this.likePost()
        },
        checkIfLoggedUnlike() {
            if (this.myUsername === '') {
                this.showLoginPrompt();
                return;
            }
            this.unlikePost()
        },
        async likePost() {
            if (this.hasLiked) {
                console.log("Already liked this post");
                return;
            }

            try {
                const data = await CardService.likePost(this.id);
                this.likes += 1;
                this.hasLiked = true;
                this.$emit('refresh-page');
            } catch (error) {
                console.error('Error liking post:', error);
            }
        },
        async unlikePost() {
            if (!this.hasLiked) {
                console.log("Already unliked this post");
                return;
            }

            try {
                const data = await CardService.unlikePost(this.id);
                this.likes -= 1;
                this.hasLiked = false;
                this.$emit('refresh-page');
            } catch (error) {
                console.error('Error unliking post:', error);
            }
        },
        deletePost() {
            try {
                CardService.deletePost(this.id);
                window.location.reload();
            } catch (error) {
                console.error('Error deleting post:', error);
            }
        },
        updatePost() {
            try {
                CardService.updatePost(this.id, this.descriptionToChange);
                window.location.reload();
            } catch (error) {
                console.error('Error updating post:', error);
            }
        },
        async postComment() {
            if (this.myUsername === '') {
                this.showLoginPrompt();
                return;
            }
            console.log("Posting comment for post ID:", this.id);
            const commentInput = document.querySelector(`#commentModal-${this.id} input[type="text"]`);

            let commentText = commentInput.value.trim();
            // Remove all leading/trailing single/double quotes and whitespace
            commentText = commentText.replace(/^["']+|["']+$/g, '').trim();

            if (!commentText) {
                console.log("Comment cannot be empty");
                return;
            }

            try {
                await CardService.postComment(this.id, commentText);
                this.$emit('refresh-page');
                commentInput.value = '';
                // Create current date array in the same format as dateOfCreation
                const now = new Date();
                const commentedAt = [
                    now.getFullYear(),
                    now.getMonth() + 1, // getMonth() returns 0-based month
                    now.getDate(),
                    now.getHours(),
                    now.getMinutes(),
                    now.getSeconds()
                ];
                this.comments.push({
                    username: this.myUsername,
                    comment: commentText,
                    commentedAt: commentedAt
                });
            } catch (error) {
                console.error('Error posting comment:', error);
            }
        },
        formatCommentDate(dateArray) {
            if (!dateArray || dateArray.length < 6) return 'Invalid date';

            const [year, month, day, hour, minute, second] = dateArray;
            const date = new Date(year, month - 1, day, hour, minute, second);

            return date.toLocaleDateString('en-US', {
                year: 'numeric',
                month: 'long',
                day: 'numeric'
            });
        },
        showModal() {
            const modal = new bootstrap.Modal(document.getElementById(`commentModal-${this.id}`));
            modal.show();
        },
        showEditModal() {
            const modal = new bootstrap.Modal(document.getElementById(`editModal-${this.id}`));
            modal.show();
        },
        showLoginPrompt() {
            const modal = new bootstrap.Modal(document.getElementById('loginPromptModal'));
            modal.show();
        },
        showDeleteModal() {
            const modal = new bootstrap.Modal(document.getElementById(`deleteModal-${this.id}`));
            modal.show();
        },
        goToLogin() {
            window.location.href = '/login';
        },
        goToRegister() {
            window.location.href = '/register';
        },
        closeModal() {
            this.descriptionToChange = this.description;
        }
    }
}
</script>

<style scoped>
.hover-scale {
    transition: transform 0.5s, box-shadow 0.5s;
}

.hover-scale:hover {
    transform: scale(1.05);
    box-shadow: 0 8px 16px rgba(0, 0, 0, 0.2);
}

.icon-button {
    background: transparent;
    border: none;
    cursor: pointer;
    transition: transform 0.2s, color 0.2s;
}

.icon-button-danger {
    background: transparent;
    border: none;
    cursor: pointer;
    transition: transform 0.2s, color 0.2s;
}

.icon-button-danger:hover {
    transform: scale(1.1);
    color: #FF0000;
    /* hover color */
}

.icon-button:hover {
    transform: scale(1.1);
    color: #007bff;
    /* hover color */
}

.overflow-y-auto::-webkit-scrollbar {
    width: 8px;
}

.overflow-y-auto::-webkit-scrollbar-track {
    background: #f1f1f1;
}

.overflow-y-auto::-webkit-scrollbar-thumb {
    background: #ddd;
    border-radius: 4px;
}

.overflow-y-auto::-webkit-scrollbar-thumb:hover {
    background: #ccc;
}

.hover-bg-light:hover {
    background-color: rgba(0, 0, 0, 0.02);
}

a {
    color: black;
    text-decoration: none;
}

a:hover {
    color: blue;
}

.hover-bg-light:hover {
    background-color: #f8f9fa !important;
}

.comment-item {
    line-height: 1.4;
}

.username {
    color: #0d6efd !important;
    font-weight: 600;
}

.comment-text {
    word-wrap: break-word;
    word-break: break-word;
}

.comment-meta {
    display: flex;
    align-items: center;
    font-size: 0.75rem;
}

.comments-section {
    scrollbar-width: thin;
    scrollbar-color: #cbd5e0 #f7fafc;
    overflow-y: auto;
    max-height: calc(96vh - 250px);
}

.comments-section::-webkit-scrollbar {
    width: 8px;
}

.comments-section::-webkit-scrollbar-track {
    background: #f7fafc;
    border-radius: 4px;
}

.comments-section::-webkit-scrollbar-thumb {
    background-color: #cbd5e0;
    border-radius: 4px;
}

.comments-section::-webkit-scrollbar-thumb:hover {
    background-color: #a0aec0;
}

.comment-item {
    line-height: 1.4;
}

.username {
    color: #0d6efd !important;
    font-weight: 600;
}

.comment-text {
    word-wrap: break-word;
    word-break: break-word;
}

.comment-meta {
    display: flex;
    align-items: center;
    font-size: 0.75rem;
}

.comments-section {
    scrollbar-width: thin;
    scrollbar-color: #cbd5e0 #f7fafc;
    overflow-y: auto;
    max-height: calc(96vh - 250px);
}

.comments-section::-webkit-scrollbar {
    width: 8px;
}

.comments-section::-webkit-scrollbar-track {
    background: #f7fafc;
    border-radius: 4px;
}

.comments-section::-webkit-scrollbar-thumb {
    background-color: #cbd5e0;
    border-radius: 4px;
}

.comments-section::-webkit-scrollbar-thumb:hover {
    background-color: #a0aec0;
}
</style>