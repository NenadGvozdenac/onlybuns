<template>
    <NavbarAuthorized v-if="isLoggedIn()" />
    <NavbarUnauthorized v-else />

    <TrendsComponent :trends="trends" />

    <Footer />
</template>

<script>
import NavbarUnauthorized from '@/components/Unauthorized/Navbar.vue'
import NavbarAuthorized from '@/components/Authorized/Navbar.vue'
import Footer from '@/components/Unauthorized/Footer.vue'
import TrendsComponent from '@/components/Authorized/TrendsComponent.vue';

import TrendService from '@/services/TrendsService';

export default {
    name: 'Trends',
    components: {
        NavbarUnauthorized,
        NavbarAuthorized,
        Footer,
        TrendsComponent
    },
    data() {
        return {
            trends: {
                numberOfUsers: 0,
                numberOfPosts: 0,
                numberOfPostsInTheLastMonth: 0,
                mostPopularPostsLastSevenDays: [
                    {
                        id: 0,
                        image: {
                            data: "string",
                            mimeType: "string",
                            uploadedAt: [
                                2024,
                                10,
                                26,
                                7,
                                15
                            ]
                        },
                        dateOfCreation: [
                            2024,
                            10,
                            26,
                            7,
                            15
                        ],
                        description: "string",
                        numberOfLikes: 0,
                        comments: [
                            {
                                id: 0,
                                comment: "string",
                                commentedAt: [
                                    2024,
                                    10,
                                    26,
                                    7,
                                    15
                                ]
                            }
                        ],
                        users: [
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
                        ]
                    }
                ],
                mostPopularPosts: [
                    {
                        id: 0,
                        image: {
                            data: "string",
                            mimeType: "string",
                            uploadedAt: [
                                2024,
                                10,
                                26,
                                7,
                                15
                            ]
                        },
                        dateOfCreation: [
                            2024,
                            10,
                            26,
                            7,
                            15
                        ],
                        description: "string",
                        numberOfLikes: 0,
                        comments: [
                            {
                                id: 0,
                                comment: "string",
                                commentedAt: [
                                    2024,
                                    10,
                                    26,
                                    7,
                                    15
                                ]
                            }
                        ],
                        users: [
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
                        ]
                    }
                ],
                usersThatMostLiked: [
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
                ]
            }
        };
    },
    mounted() {
        this.getTrends();
    },
    methods: {
        isLoggedIn() {
            return localStorage.getItem('token') !== null;
        },
        
        getTrends() {
            TrendService.getTrends()
                .then(response => {
                    this.trends = response;
                })
                .catch(error => {
                    console.log('Error:', error);
                });
        }
    }
}

</script>