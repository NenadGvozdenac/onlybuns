<template>
    <div class="analytics-wrapper">
        <NavbarAuthorized v-if="isLoggedIn()" />
        <NavbarUnauthorized v-else />

        <div class="container py-4">
            <!-- Header -->
            <div class="row mb-4">
                <div class="col">
                    <div class="d-flex justify-content-between align-items-center">
                        <h1 class="display-5 fw-bold text-primary">Analytics Dashboard</h1>
                        <div class="badge bg-primary p-2">Admin Panel</div>
                    </div>
                    <hr class="my-4">
                </div>
            </div>

            <!-- Stats Cards -->
            <div class="row g-4 mb-4">
                <!-- Weekly Stats Card -->
                <div class="col-md-4">
                    <div class="card h-100 border-0 shadow-sm">
                        <div class="card-header bg-primary text-white">
                            <h5 class="card-title mb-0">Weekly Statistics</h5>
                        </div>
                        <div class="card-body">
                            <div class="d-flex justify-content-between mb-3">
                                <div class="d-flex align-items-center">
                                    <i class="bi bi-chat-dots me-2"></i>
                                    <span>Comments</span>
                                </div>
                                <h3 class="mb-0">{{ commentsPerWeek }}</h3>
                            </div>
                            <div class="d-flex justify-content-between">
                                <div class="d-flex align-items-center">
                                    <i class="bi bi-file-text me-2"></i>
                                    <span>Posts</span>
                                </div>
                                <h3 class="mb-0">{{ postsPerWeek }}</h3>
                            </div>
                        </div>
                    </div>
                </div>

                <!-- Monthly Stats Card -->
                <div class="col-md-4">
                    <div class="card h-100 border-0 shadow-sm">
                        <div class="card-header bg-success text-white">
                            <h5 class="card-title mb-0">Monthly Statistics</h5>
                        </div>
                        <div class="card-body">
                            <div class="d-flex justify-content-between mb-3">
                                <div class="d-flex align-items-center">
                                    <i class="bi bi-chat-dots me-2"></i>
                                    <span>Comments</span>
                                </div>
                                <h3 class="mb-0">{{ commentsPerMonth }}</h3>
                            </div>
                            <div class="d-flex justify-content-between">
                                <div class="d-flex align-items-center">
                                    <i class="bi bi-file-text me-2"></i>
                                    <span>Posts</span>
                                </div>
                                <h3 class="mb-0">{{ postsPerMonth }}</h3>
                            </div>
                        </div>
                    </div>
                </div>

                <!-- Yearly Stats Card -->
                <div class="col-md-4">
                    <div class="card h-100 border-0 shadow-sm">
                        <div class="card-header bg-info text-white">
                            <h5 class="card-title mb-0">Yearly Statistics</h5>
                        </div>
                        <div class="card-body">
                            <div class="d-flex justify-content-between mb-3">
                                <div class="d-flex align-items-center">
                                    <i class="bi bi-chat-dots me-2"></i>
                                    <span>Comments</span>
                                </div>
                                <h3 class="mb-0">{{ commentsPerYear }}</h3>
                            </div>
                            <div class="d-flex justify-content-between">
                                <div class="d-flex align-items-center">
                                    <i class="bi bi-file-text me-2"></i>
                                    <span>Posts</span>
                                </div>
                                <h3 class="mb-0">{{ postsPerYear }}</h3>
                            </div>
                        </div>
                    </div>
                </div>
            </div>

            <!-- User Activity Chart -->
            <div class="row">
                <div class="col">
                    <div class="card border-0 shadow-sm">
                        <div class="card-header bg-dark text-white">
                            <h5 class="card-title mb-0">User Activity Distribution</h5>
                        </div>
                        <div class="card-body">
                            <RadialChart :usersJustCommented="usersJustCommented" :usersJustPosted="usersJustPosted"
                                :usersWithoutPostsOrComments="usersWithoutPostsOrComments" />
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <Footer />
    </div>
</template>

<script>
import AuthService from '@/services/AuthService';
import AnalyticsService from '@/services/AnalyticsService';
import RadialChart from '@/components/Authorized/AdminAnalyticsRadialChart.vue';
import NavbarUnauthorized from '@/components/Unauthorized/Navbar.vue';
import NavbarAuthorized from '@/components/Authorized/Navbar.vue';
import Footer from '@/components/Unauthorized/Footer.vue';

export default {
    name: 'AppAnalytics',
    components: {
        RadialChart,
        NavbarAuthorized,
        NavbarUnauthorized,
        Footer
    },
    data() {
        return {
            commentsPerWeek: 0,
            commentsPerMonth: 0,
            commentsPerYear: 0,
            postsPerWeek: 0,
            postsPerMonth: 0,
            postsPerYear: 0,
            usersJustCommented: 0,
            usersJustPosted: 0,
            usersWithoutPostsOrComments: 0
        };
    },
    mounted() {
        this.userRole();
        this.fetchData();
    },
    methods: {
        isLoggedIn() {
            return localStorage.getItem('token') !== null;
        },
        userRole() {
            if (AuthService.getLoggedInUserRole() !== "ADMIN") {
                this.$router.push('/');
            }
        },
        async fetchData() {
            try {
                const result = await AnalyticsService.fetchAnalyticsData();
                Object.assign(this, result);

                //test
                // this.commentsPerWeek = Math.floor(Math.random() * 100); 
                // this.commentsPerMonth = Math.floor(Math.random() * 300);
                // this.commentsPerYear = Math.floor(Math.random() * 1000);
                // this.postsPerWeek = Math.floor(Math.random() * 50);
                // this.postsPerMonth = Math.floor(Math.random() * 200);
                // this.postsPerYear = Math.floor(Math.random() * 800);
                // this.usersJustCommented = Math.floor(Math.random() * 100);
                // this.usersJustPosted = Math.floor(Math.random() * 100);
                // this.usersWithoutPostsOrComments = Math.floor(Math.random() * 100);

            } catch (error) {
                console.error('Error fetching analytics data:', error);
            }
        }
    }
};
</script>

<style scoped>
.analytics-wrapper {
    background-color: #f8f9fa;
    min-height: 100vh;
}

.card {
    transition: transform 0.2s ease-in-out;
}

.card:hover {
    transform: translateY(-5px);
}

.card-header {
    border-bottom: none;
}

h3 {
    font-weight: 600;
    color: #2c3e50;
}

.bi {
    font-size: 1.2rem;
}
</style>