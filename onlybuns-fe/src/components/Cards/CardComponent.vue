<template>
    <!-- Bootstrap Version -->
    <div class="d-flex justify-content-center align-items-center vh-75 bg-light my-3">
        <div class="container card shadow-lg rounded-lg hover-scale">
            <div class="d-flex align-items-center mt-2 mb-2">
                <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" class="bi bi-calendar" viewBox="0 0 16 16">
                    <path d="M3.5 0a.5.5 0 0 1 .5.5V1h8V.5a.5.5 0 0 1 1 0V1h1a2 2 0 0 1 2 2v11a2 2 0 0 1-2 2H2a2 2 0 0 1-2-2V3a2 2 0 0 1 2-2h1V.5a.5.5 0 0 1 .5-.5M1 4v10a1 1 0 0 0 1 1h12a1 1 0 0 0 1-1V4z"/>
                </svg>
                <p class="mb-0 ms-1">{{ formattedDate }}</p>
                <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" class="bi bi-clock ms-2" viewBox="0 0 16 16">
                    <path d="M8 3.5a.5.5 0 0 0-1 0V9a.5.5 0 0 0 .252.434l3.5 2a.5.5 0 0 0 .496-.868L8 8.71z"/>
                    <path d="M8 16A8 8 0 1 0 8 0a8 8 0 0 0 0 16m7-8A7 7 0 1 1 1 8a7 7 0 0 1 14 0"/>
                </svg>
                <p class="mb-0 ms-1">{{ formattedTime }}</p>
            </div>
            
            <img class="card-img-top"
                :src="image" 
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
                        <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" fill="none" viewBox="0 0 24 24"
                            stroke="currentColor" class="me-1 text-secondary">
                            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                                d="M8 12h.01M12 12h.01M16 12h.01M21 12c0 4.418-4.03 8-9 8a9.863 9.863 0 01-4.255-.949L3 20l1.395-3.72C3.512 15.042 3 13.574 3 12c0-4.418 4.03-8 9-8s9 3.582 9 8z" />
                        </svg>
                        <span>{{ commentsCount }}</span>
                    </div>

                    <!-- Likes -->
                    <div class="d-flex align-items-center">
                        <button class="icon-button" @click="likePost">
                            <svg v-if="hasLiked" xmlns="http://www.w3.org/2000/svg" width="24" height="24" fill="currentColor"
                                class="text-danger me-1" viewBox="0 0 20 20">
                                <path fill-rule="evenodd"
                                    d="M3.172 5.172a4 4 0 015.656 0L10 6.343l1.172-1.171a4 4 0 115.656 5.656L10 17.657l-6.828-6.829a4 4 0 010-5.656z"
                                    clip-rule="evenodd" />
                            </svg>
                            <svg v-else xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" class="bi bi-heart" viewBox="0 0 16 16">
                                <path d="m8 2.748-.717-.737C5.6.281 2.514.878 1.4 3.053c-.523 1.023-.641 2.5.314 4.385.92 1.815 2.834 3.989 6.286 6.357 3.452-2.368 5.365-4.542 6.286-6.357.955-1.886.838-3.362.314-4.385C13.486.878 10.4.28 8.717 2.01zM8 15C-7.333 4.868 3.279-3.04 7.824 1.143q.09.083.176.171a3 3 0 0 1 .176-.17C12.72-3.042 23.333 4.867 8 15"/>
                            </svg>
                        </button>
                        <span>{{ likes }}</span>
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
            type: String,
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
        }
    },
    data() {
        return {
            likes: 0, // Initialize with a default value
            myUsername: JSON.parse(atob(localStorage.getItem('token').split('.')[1])).sub,
            hasLiked: false
        };
    },
    created() {
        // Set `likes` to `likesCount` in the `created` hook to ensure it's defined
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
background: transparent; /* Remove background */
border: none; /* Remove border */
cursor: pointer; /* Change cursor to pointer */
transition: transform 0.2s, color 0.2s; /* Animation for scale and color */
}

.icon-button:hover {
    transform: scale(1.1); /* Scale up slightly */
    color: #007bff; /* Change color on hover (you can customize this) */
}
</style>