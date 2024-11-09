<template>
    <NavbarAuthorized v-if="isLoggedIn()" />
    <NavbarUnauthorized v-else />

    <div class="container mt-4">
        <div class="row d-flex justify-content-center align-items-center">
            <div class="col-7">
                <div class="card shadow-lg mt-3 border-0 rounded-4">
                    <!-- Background Banner -->
                    <div class="bg-primary bg-gradient text-white rounded-top-4" style="height: 100px;"></div>

                    <div class="card-body p-4 position-relative">
                        <!-- Profile Header -->
                        <div class="text-center">
                            <!-- Profile Image - positioned to overlap the banner -->
                            <img src="https://flowbite.com/docs/images/logo.svg" alt="Profile Logo"
                                class="rounded-circle border border-4 border-white shadow-sm position-relative mt-n5 mb-3"
                                style="width: 120px; height: 120px; margin-top: -60px;">

                            <h3 class="fw-bold mb-1">{{ user.name }} {{ user.surname }}</h3>
                            <p class="text-muted fs-6 mb-3">@{{ user.username }}</p>

                            <div v-if="!isMyProfile()">
                                <button v-if="!followedAlready()"  class="btn btn-primary px-4 py-2 rounded-pill shadow-sm" @click="followUser">
                                    <i class="bi bi-person-plus-fill"></i>Follow
                                </button>
                                <button v-else class="btn btn-danger px-4 py-2 rounded-pill shadow-sm" @click="unfollowUser">
                                    <i class="bi bi-person-dash-fill"></i>Unfollow
                                </button>
                            </div>

                        </div>

                        <!-- Stats Row -->
                        <div class="row justify-content-center text-center my-4">
                            <div class="col-4">
                                <div class="p-3 border-end">
                                    <h4 class="fw-bold mb-1">{{ user.followers.length }}</h4>
                                    <p class="text-muted mb-0 small">Followers</p>
                                </div>
                            </div>
                            <div class="col-4">
                                <div class="p-3">
                                    <h4 class="fw-bold mb-1">{{ user.following.length }}</h4>
                                    <p class="text-muted mb-0 small">Following</p>
                                </div>
                            </div>
                        </div>

                        <!-- User Info -->
                        <div class="bg-light rounded-3 p-4">
                            <div class="d-flex align-items-center mb-3">
                                <div class="me-3">
                                    <i class="bi bi-envelope text-primary fs-5"></i>
                                </div>
                                <div>
                                    <div class="text-muted small">Email</div>
                                    <div class="fw-medium">{{ user.email }}</div>
                                </div>
                            </div>

                            <div class="d-flex align-items-center">
                                <div class="me-3">
                                    <i class="bi bi-geo-alt text-primary fs-5"></i>
                                </div>
                                <div>
                                    <div class="text-muted small">Address</div>
                                    <div class="fw-medium">
                                        {{ user.address.street }} {{ user.address.number }}, {{ user.address.city }}, {{
                                        user.address.country }}
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <div>
            <div v-if="user.activePosts.length" class="d-flex col-12 gap-3">
                <div class="row">
                    <div class="col-md-4">
                        <div v-for="(card, index) in user.activePosts.filter((_, i) => i % 3 === 0)" :key="index">
                            <CardComponent :key="card.id" :id="card.id" :image="card.image"
                                :likesCount="card.numberOfLikes" :description="card.description"
                                :commentsCount="card.comments.length" :username="user.username"
                                :dateOfCreation="card.dateOfCreation" :usersThatLike="card.users"
                                :comments="card.comments" />
                        </div>
                    </div>

                    <div class="col-md-4">
                        <div v-for="(card, index) in user.activePosts.filter((_, i) => i % 3 === 1)" :key="index">
                            <CardComponent :key="card.id" :id="card.id" :image="card.image"
                                :likesCount="card.numberOfLikes" :description="card.description"
                                :commentsCount="card.comments.length" :username="user.username"
                                :dateOfCreation="card.dateOfCreation" :usersThatLike="card.users"
                                :comments="card.comments" />
                        </div>
                    </div>
                    <div class="col-md-4">
                        <div v-for="(card, index) in user.activePosts.filter((_, i) => i % 3 === 2)" :key="index">
                            <CardComponent :key="card.id" :id="card.id" :image="card.image"
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
    <Footer />
</template>

<script>
import NavbarUnauthorized from '@/components/Unauthorized/Navbar.vue';
import NavbarAuthorized from '@/components/Authorized/Navbar.vue';
import Footer from '@/components/Unauthorized/Footer.vue';
import CardComponent from '@/components/Cards/CardComponent.vue';

import ProfileService from '@/services/ProfileService';
import AuthService from '@/services/AuthService';
export default {
    name: 'UserProfile',
    components: {
        NavbarUnauthorized,
        NavbarAuthorized,
        Footer,
        CardComponent
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
            },

            username: ""
        }
    },
    mounted() {
        this.username = this.$route.query.username;
        this.getUser();
    },
    methods: {
        getUser() {
            ProfileService.getProfile(this.username)
                .then(response => {
                    this.user = response;
                })
                .catch(e => {
                    let code = e.response.status;

                    if (code === 404) {
                        this.$router.push('/not-found');
                    }
                });
        },
        isLoggedIn() {
            return localStorage.getItem('token') !== null;
        },

        followedAlready() {
            return this.user.followers.some(follower => follower.username === AuthService.getLoggedInUsername());
        },

        isMyProfile() {
            return this.user.username === AuthService.getLoggedInUsername();
        },
        followUser() {
            ProfileService.followProfile(this.username)
                .then(response => {
                    window.location.reload();
                })
                .catch(e => {
                    console.log(e);
                });
        },
        unfollowUser() {
            ProfileService.unfollowProfile(this.username)
                .then(response => {
                    window.location.reload();
                })
                .catch(e => {
                    console.log(e);
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