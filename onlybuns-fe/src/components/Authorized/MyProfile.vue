<template>
    <Navbar />

    <div class="container mt-4">
        <div class="profile-container">
            <div class="profile-card">
                <div class="shadow-lg mt-3 border-0 rounded-4">
                    <!-- Background Banner -->
                    <div class="banner bg-primary bg-gradient text-white rounded-top-4"></div>

                    <div class="card-body p-4 position-relative">
                        <!-- Profile Header -->
                        <div class="text-center">
                            <!-- Profile Image -->
                            <img src="https://flowbite.com/docs/images/logo.svg" alt="Profile Logo"
                                class="profile-image">

                            <h3 class="fw-bold mb-1">{{ user.name }} {{ user.surname }}</h3>
                            <p class="username-text">@{{ user.username }}</p>

                            <button class="edit-button" data-bs-toggle="modal" data-bs-target="#editProfileModal">
                                <i class="bi bi-pencil-fill me-2"></i>Edit Profile
                            </button>
                        </div>

                        <!-- Stats Row -->
                        <div class="stats-container">
                            <div class="stat-item">
                                <div class="stat-content border-end">
                                    <h4 class="fw-bold mb-1">{{ user.followers.length }}</h4>
                                    <p class="stat-label">Followers</p>
                                </div>
                            </div>
                            <div class="stat-item">
                                <div class="stat-content">
                                    <h4 class="fw-bold mb-1">{{ user.following.length }}</h4>
                                    <p class="stat-label">Following</p>
                                </div>
                            </div>
                        </div>

                        <!-- User Info -->
                        <div class="user-info">
                            <div class="info-row">
                                <div class="icon-container">
                                    <i class="bi bi-envelope text-primary fs-5"></i>
                                </div>
                                <div>
                                    <div class="info-label">Email</div>
                                    <div class="info-value">{{ user.email }}</div>
                                </div>
                            </div>

                            <div class="info-row">
                                <div class="icon-container">
                                    <i class="bi bi-geo-alt text-primary fs-5"></i>
                                </div>
                                <div>
                                    <div class="info-label">Address</div>
                                    <div class="info-value">
                                        {{ user.address.street }} {{ user.address.number }}, {{ user.address.city }}, 
                                        {{ user.address.country }}
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <!-- Posts Grid -->
        <div class="posts-container">
            <div v-if="user.activePosts.length" class="posts-grid">
                <div class="row">
                    <div class="col-md-4" v-for="colIndex in 3" :key="colIndex">
                        <div v-for="(card, index) in user.activePosts.filter((_, i) => i % 3 === colIndex - 1)" :key="index">
                            <ProfileCardsComponent 
                                :key="card.id" 
                                :id="card.id" 
                                :image="card.image"
                                :likesCount="card.numberOfLikes" 
                                :description="card.description"
                                :commentsCount="card.comments.length" 
                                :username="user.username"
                                :dateOfCreation="card.dateOfCreation" 
                                :usersThatLike="card.users"
                                :comments="card.comments" 
                            />
                        </div>
                    </div>
                </div>
            </div>
            <div v-else class="no-posts">
                <p>No posts available</p>
            </div>
        </div>
    </div>

    <!-- Edit Profile Modal -->
    <EditProfileModal :userData="user" @update-profile="updateProfile" ref="editProfileModal" />

    <Footer />
</template>

<script>
import Navbar from './Navbar.vue';
import Footer from '../Unauthorized/Footer.vue';
import ProfileCardsComponent from '../Cards/ProfileCardsComponent.vue';
import EditProfileModal from '../Layout/EditProfileModal.vue';
import ProfileService from '@/services/ProfileService';

export default {
    name: 'MyProfile',
    components: {
        Navbar,
        Footer,
        ProfileCardsComponent,
        EditProfileModal
    },
    data() {
        return {
            user: {
                username: "",
                name: "",
                surname: "",
                email: "",
                address: {
                    street: "",
                    number: 0,
                    city: "",
                    country: ""
                },
                activePosts: [],
                followers: [],
                following: []
            }
        }
    },
    mounted() {
        this.getUser();
    },
    methods: {
        getUser() {
            ProfileService.getMyProfile()
                .then(response => {
                    this.user = response;
                })
                .catch(error => {
                    console.log(error);
                });
        },
        updateProfile(userData) {
            console.log(userData)
            ProfileService.updateProfile(userData)
                .then(response => {
                    this.getUser();
                    this.$refs.editProfileModal.closeModal();
                })
                .catch(error => {
                    console.log(error);
                });
        }
    }
}
</script>

<style scoped>
@import url('https://fonts.googleapis.com/css2?family=Poppins:wght@300;400;500;600;700&display=swap');
@import url("https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.0/font/bootstrap-icons.css");

.profile-container {
    display: flex;
    justify-content: center;
    width: 100%;
}

.profile-card {
    width: 100%;
    max-width: 750px;
}

.banner {
    height: 100px;
}

.profile-image {
    width: 120px;
    height: 120px;
    border-radius: 50%;
    border: 4px solid white;
    box-shadow: 0 .125rem .25rem rgba(0,0,0,.075);
    position: relative;
    margin-top: -60px;
    margin-bottom: 1rem;
}

.username-text {
    color: #6c757d;
    font-size: 0.875rem;
    margin-bottom: 1rem;
}

.edit-button {
    background-color: #0d6efd;
    color: white;
    border: none;
    padding: 0.5rem 1.5rem;
    border-radius: 50rem;
    font-weight: 500;
    box-shadow: 0 .125rem .25rem rgba(0,0,0,.075);
    transition: all 0.3s ease;
}

.edit-button:hover {
    background-color: #0b5ed7;
    transform: translateY(-2px);
}

.stats-container {
    display: flex;
    justify-content: center;
    margin: 2rem 0;
}

.stat-item {
    width: 33.333%;
    text-align: center;
}

.stat-content {
    padding: 1rem;
}

.stat-label {
    color: #6c757d;
    font-size: 0.75rem;
    margin: 0;
}

.user-info {
    background-color: #f8f9fa;
    border-radius: 0.5rem;
    padding: 1.5rem;
}

.info-row {
    display: flex;
    align-items: center;
    margin-bottom: 1rem;
}

.info-row:last-child {
    margin-bottom: 0;
}

.icon-container {
    margin-right: 1rem;
}

.info-label {
    color: #6c757d;
    font-size: 0.75rem;
}

.info-value {
    font-weight: 500;
}

.posts-container {
    margin-top: 2rem;
}

.posts-grid {
    width: 100%;
}

.no-posts {
    text-align: center;
    color: #6c757d;
    margin-top: 2rem;
}

/* Card hover effect */
:deep(.card) {
    transition: all 0.3s ease;
}

:deep(.card:hover) {
    transform: translateY(-5px);
}
</style>