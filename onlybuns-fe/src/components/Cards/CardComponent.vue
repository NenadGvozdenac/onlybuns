<template>
    <!-- Bootstrap Version -->
    <div class="d-flex justify-content-center align-items-center vh-75 bg-light my-3">
        <div class="container card shadow-lg rounded-lg hover-scale">
            <div class="d-flex align-items-center mt-2 mb-2">
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
            <!-- Image -->
            <img class="card-img-top" :src="'data:' + image.mimeType + ';base64,' + image.data"
                :alt="`Post image did not load`" />

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
                        <button class="icon-button" @click="checkIfLoggedLike">
                            <svg v-if="hasLiked" xmlns="http://www.w3.org/2000/svg" width="24" height="24"
                                fill="currentColor" class="text-danger" viewBox="0 0 20 20">
                                <path fill-rule="evenodd"
                                    d="M3.172 5.172a4 4 0 015.656 0L10 6.343l1.172-1.171a4 4 0 115.656 5.656L10 17.657l-6.828-6.829a4 4 0 010-5.656z"
                                    clip-rule="evenodd" />
                            </svg>
                            <svg v-else xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor"
                                class="bi bi-heart" viewBox="0 0 16 16">
                                <path
                                    d="m8 2.748-.717-.737C5.6.281 2.514.878 1.4 3.053c-.523 1.023-.641 2.5.314 4.385.92 1.815 2.834 3.989 6.286 6.357 3.452-2.368 5.365-4.542 6.286-6.357.955-1.886.838-3.362.314-4.385C13.486.878 10.4.28 8.717 2.01zM8 15C-7.333 4.868 3.279-3.04 7.824 1.143q.09.083.176.171a3 3 0 0 1 .176-.17C12.72-3.042 23.333 4.867 8 15" />
                            </svg>
                        </button>
                        <span>{{ likes }}</span>
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
                            <div class="flex-grow-1 overflow-y-auto" style="scrollbar-width: thin;">
                                <div class="comments-section h-100">
                                    <h6 class="text-muted small px-3 pt-3 mb-2 sticky-top bg-white">Comments:</h6>
                                    <ul class="list-unstyled mb-0">
                                        <li v-for="(comment, index) in comments" :key="index"
                                            class="border-bottom px-3 py-2 hover-bg-light">
                                            <div class="d-flex" style="gap: 8px;">
                                                <span class="text-muted text-break"
                                                    style="word-wrap: break-word; min-width: 0;">
                                                    {{ comment.comment }}
                                                </span>
                                                <span class="text-muted small flex-shrink-0">@{{ comment.author
                                                    }}</span>
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
                                    <button class="btn btn-link text-decoration-none flex-shrink-0">Post</button>
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

</template>

<script>
import CardService from '@/services/CardService';

export default {
    name: 'CardComponent',
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
            hasLiked: false
        };
    },
    created() {

        this.likes = this.likesCount;
        this.hasLiked = this.usersThatLike.some(user => user.username === this.myUsername);
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
        async likePost() {
            if (this.hasLiked) {
                console.log("Already liked this post");
                return;
            }

            try {
                const data = await CardService.likePost(this.id);
                this.likes += 1;
                this.hasLiked = true;
                console.log(`Likes count updated to: ${this.likes}`);
            } catch (error) {
                console.error('Error liking post:', error);
            }
        },
        showModal() {
            const modal = new bootstrap.Modal(document.getElementById(`commentModal-${this.id}`));
            modal.show();
        },
        showLoginPrompt() {
            const modal = new bootstrap.Modal(document.getElementById('loginPromptModal'));
            modal.show();
        },
        goToLogin() {
            window.location.href = '/login';
        },
        goToRegister() {
            window.location.href = '/register';
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
</style>