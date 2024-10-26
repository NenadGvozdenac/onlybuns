<template>
    <Navbar />

    <div class="container mt-4">
        <div class="row">
            <!-- Left Column: User Details -->
            <div class="col-12">
                <div class="user-card shadow-lg mt-3">
                    <div class="d-flex justify-content-end">
                        <button class="btn btn-primary" data-bs-toggle="modal" data-bs-target="#editProfileModal">
                            Update Profile
                        </button>
                    </div>
                    <div class="user-card-logo">
                        <img src="https://flowbite.com/docs/images/logo.svg" alt="Twitter Logo" class="h-8 me-2">
                    </div>
                    <div class="text-center mt-3 user-name">
                        Onlybuns User
                    </div>
                    <h4 class="user-card-title">{{ user.name }} {{ user.surname }}</h4>
                    <div class="user-info-table">
                        <div class="user-info-row">
                            <span class="user-info-label">Username:</span>
                            <span class="user-info-value">{{ user.username }}</span>
                        </div>
                        <div class="user-info-row">
                            <span class="user-info-label">Email:</span>
                            <span class="user-info-value">{{ user.email }}</span>
                        </div>
                        <div class="user-info-row">
                            <span class="user-info-label">Address:</span>
                            <span class="user-info-value">
                                {{ user.address.street }} {{ user.address.number }}, {{ user.address.city }}, {{
                                    user.address.country }}
                            </span>
                        </div>
                        <div class="user-info-row">
                            <span class="user-info-label">Following:</span>
                            <span class="user-info-value">{{ user.following.length }}</span>
                        </div>
                        <div class="user-info-row">
                            <span class="user-info-label">Followers:</span>
                            <span class="user-info-value">{{ user.followers.length }}</span>
                        </div>
                    </div>
                </div>
            </div>


            <!-- Right Column: User Posts -->
            <div>
                <div v-if="user.activePosts.length" class="d-flex col-12 gap-3">
                    <div class="row">
                        <div class="col-md-4">
                            <div v-for="(card, index) in user.activePosts.filter((_, i) => i % 3 === 0)" :key="index">
                                <ProfileCardsComponent :key="card.id" :id="card.id" :image="card.image"
                                    :likesCount="card.numberOfLikes" :description="card.description"
                                    :commentsCount="card.comments.length" :username="user.username"
                                    :dateOfCreation="card.dateOfCreation" :usersThatLike="card.users"
                                    :comments="card.comments" />
                            </div>
                        </div>

                        <div class="col-md-4">
                            <div v-for="(card, index) in user.activePosts.filter((_, i) => i % 3 === 1)" :key="index">
                                <ProfileCardsComponent :key="card.id" :id="card.id" :image="card.image"
                                    :likesCount="card.numberOfLikes" :description="card.description"
                                    :commentsCount="card.comments.length" :username="user.username"
                                    :dateOfCreation="card.dateOfCreation" :usersThatLike="card.users"
                                    :comments="card.comments" />
                            </div>
                        </div>
                        <div class="col-md-4">
                            <div v-for="(card, index) in user.activePosts.filter((_, i) => i % 3 === 2)" :key="index">
                                <ProfileCardsComponent :key="card.id" :id="card.id" :image="card.image"
                                    :likesCount="card.numberOfLikes" :description="card.description"
                                    :commentsCount="card.comments.length" :username="user.username"
                                    :dateOfCreation="card.dateOfCreation" :usersThatLike="card.users"
                                    :comments="card.comments" />
                            </div>
                        </div>
                    </div>
                </div>
                <div v-else>
                    <p>No posts available</p>
                </div>
            </div>
        </div>
    </div>

    <!-- Add the Edit Profile Modal -->
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
                activePosts: [
                    {
                        id: 0,
                        "users": [
                            {
                                username: "string",
                                name: "string",
                                surname: "string",
                                email: "string",
                                address: {
                                    street: "string",
                                    number: 0,
                                    city: "string",
                                    country: "string"
                                }
                            }
                        ],
                        image: {
                            data: "",
                            mimeType: "",
                            uploadedAt: [
                                2024,
                                10,
                                21,
                                15,
                                30
                            ]
                        },
                        dateOfCreation: [
                            2024,
                            10,
                            19,
                            13,
                            32
                        ],
                        description: "",
                        numberOfLikes: 0,
                        comments: [
                            {
                                id: 0,
                                comment: "string",
                                commentedAt: [
                                    2024,
                                    10,
                                    19,
                                    13,
                                    32
                                ],
                            }
                        ]
                    }
                ],
                followers: [
                    {
                        username: "",
                        name: "",
                        surname: "",
                        email: "",
                        address: {
                            street: "",
                            number: 0,
                            city: "",
                            country: ""
                        }
                    }
                ],
                following: [
                    {
                        username: "",
                        name: "",
                        surname: "",
                        email: "",
                        address: {
                            street: "",
                            number: 0,
                            city: "",
                            country: ""
                        }
                    }
                ]
            }
        }
    },
    mounted() {
        this.getUser();
    },
    methods: {
        getUser() {
            ProfileService.getProfile()
                .then(response => {
                    this.user = response;
                })
                .catch(error => {
                    console.log(error);
                });
        },

        updateProfile(userData) {
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

<style>
@import url('https://fonts.googleapis.com/css2?family=Poppins:wght@300;400;500;600;700&display=swap');

* {
    margin: 0;
    padding: 0;
    box-sizing: border-box;
    font-family: 'Poppins', sans-serif;
}

.user-card {
    max-width: 750px;
    min-height: 300px;
    padding: 50px 20px;
    background-color: #ecf0f3;
    border-radius: 55px;
    box-shadow: 10px 10px 20px #cbced1, -10px -10px 20px #fff;
    text-align: center;
    margin: auto;
}

.user-card-logo {
    width: 60px;
    margin: 0 auto 10px;
}

.user-card-logo img {
    width: 100%;
    height: auto;
    object-fit: cover;
    border-radius: 50%;
    box-shadow: 0px 0px 3px #5f5f5f, 0px 0px 0px 5px #ecf0f3, 5px 5px 10px #a7aaa7, -5px -5px 10px #fff;
}

.user-name {
    font-weight: 600;
    font-size: 1.2rem;
    color: #555;
    margin-bottom: 15px;
}

.user-card-title {
    font-size: 1.3rem;
    font-weight: 500;
    color: #333;
    margin-bottom: 15px;
}

.user-info-table {
    display: flex;
    flex-direction: column;
    gap: 10px;
    margin-top: 20px;
    text-align: left;
}

.user-info-row {
    display: flex;
    justify-content: space-between;
    padding: 10px 5px;
    border-bottom: 1px solid #cbced1;
}

.user-info-label {
    font-weight: 500;
    color: #333;
}

.user-info-value {
    color: #666;
    text-align: right;
    flex: 1;
}
</style>