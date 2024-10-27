<template>
    <div class="trends-container p-4">
        <div class="stats-container mb-4">
            <div class="row g-4">
                <div class="col-md-4">
                    <div class="card stat-card h-100">
                        <div class="card-body">
                            <div class="d-flex align-items-center">
                                <div class="stat-icon me-3">
                                    <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" fill="currentColor" class="bi bi-people" viewBox="0 0 16 16">
                                        <path d="M15 14s1 0 1-1-1-4-5-4-5 3-5 4 1 1 1 1zm-7.978-1A.261.261 0 0 1 7 12.996c.001-.264.167-1.03.76-1.72C8.312 10.629 9.282 10 11 10c1.717 0 2.687.63 3.24 1.276.593.69.758 1.457.76 1.72l-.008.002a.274.274 0 0 1-.014.002H7.022ZM11 7a2 2 0 1 0 0-4 2 2 0 0 0 0 4m3-2a3 3 0 1 1-6 0 3 3 0 0 1 6 0M6.936 9.28a5.88 5.88 0 0 0-1.23-.247A7.35 7.35 0 0 0 5 9c-4 0-5 3-5 4 0 .667.333 1 1 1h4.216A2.238 2.238 0 0 1 5 13c0-1.01.377-2.042 1.09-2.904.243-.294.526-.569.846-.816M4.92 10A5.493 5.493 0 0 0 4 13H1c0-.26.164-1.03.76-1.724.545-.636 1.492-1.256 3.16-1.275ZM1.5 5.5a3 3 0 1 1 6 0 3 3 0 0 1-6 0m3-2a2 2 0 1 0 0 4 2 2 0 0 0 0-4"/>
                                    </svg>
                                </div>
                                <div>
                                    <h6 class="mb-1">Total Users</h6>
                                    <h3 class="mb-0">{{ trends.numberOfUsers }}</h3>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="col-md-4">
                    <div class="card stat-card h-100">
                        <div class="card-body">
                            <div class="d-flex align-items-center">
                                <div class="stat-icon me-3">
                                    <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" fill="currentColor" class="bi bi-image" viewBox="0 0 16 16">
                                        <path d="M6.002 5.5a1.5 1.5 0 1 1-3 0 1.5 1.5 0 0 1 3 0"/>
                                        <path d="M2.002 1a2 2 0 0 0-2 2v10a2 2 0 0 0 2 2h12a2 2 0 0 0 2-2V3a2 2 0 0 0-2-2h-12zm12 1a1 1 0 0 1 1 1v6.5l-3.777-1.947a.5.5 0 0 0-.577.093l-3.71 3.71-2.66-1.772a.5.5 0 0 0-.63.062L1.002 12V3a1 1 0 0 1 1-1h12z"/>
                                    </svg>
                                </div>
                                <div>
                                    <h6 class="mb-1">Total Posts</h6>
                                    <h3 class="mb-0">{{ trends.numberOfPosts }}</h3>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="col-md-4">
                    <div class="card stat-card h-100">
                        <div class="card-body">
                            <div class="d-flex align-items-center">
                                <div class="stat-icon me-3">
                                    <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" fill="currentColor" class="bi bi-calendar-check" viewBox="0 0 16 16">
                                        <path d="M10.854 7.146a.5.5 0 0 1 0 .708l-3 3a.5.5 0 0 1-.708 0l-1.5-1.5a.5.5 0 1 1 .708-.708L7.5 9.793l2.646-2.647a.5.5 0 0 1 .708 0z"/>
                                        <path d="M3.5 0a.5.5 0 0 1 .5.5V1h8V.5a.5.5 0 0 1 1 0V1h1a2 2 0 0 1 2 2v11a2 2 0 0 1-2 2H2a2 2 0 0 1-2-2V3a2 2 0 0 1 2-2h1V.5a.5.5 0 0 1 .5-.5M1 4v10a1 1 0 0 0 1 1h12a1 1 0 0 0 1-1V4z"/>
                                    </svg>
                                </div>
                                <div>
                                    <h6 class="mb-1">Posts This Month</h6>
                                    <h3 class="mb-0">{{ trends.numberOfPostsInTheLastMonth }}</h3>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <div class="popular-posts mb-4">
            <h5 class="mb-3">Popular This Week</h5>
            <div class="row">
                <div v-for="post in trends.mostPopularPostsLastSevenDays" :key="post.id" class="col-3">
                    <CardComponent 
                        :id="post.id"
                        :image="post.image"
                        :likesCount="post.numberOfLikes"
                        :description="post.description"
                        :commentsCount="post.comments.length"
                        :username="post.username"
                        :dateOfCreation="post.dateOfCreation"
                        :usersThatLike="post.users"
                        :comments="post.comments"
                                                @refresh-page="refreshPage"
                    />
                </div>
            </div>
        </div>

        <div class="popular-posts mb-4">
            <h5 class="mb-3">All-Time Popular</h5>
            <div class="row">
                <div v-for="post in trends.mostPopularPosts" :key="post.id" class="col-3">
                    <CardComponent 
                        :id="post.id"
                        :image="post.image"
                        :likesCount="post.numberOfLikes"
                        :description="post.description"
                        :commentsCount="post.comments.length"
                        :username="post.username"
                        :dateOfCreation="post.dateOfCreation"
                        :usersThatLike="post.users"
                        :comments="post.comments"
                        @refresh-page="refreshPage"
                    />
                </div>
            </div>
        </div>

        <div class="top-users">
            <h5 class="mb-3">Most Active Users</h5>
            <div class="row g-3">
                <div v-for="(user, index) in trends.usersThatMostLiked" :key="user.username" class="col-md-2">
                    <div class="card user-card">
                        <div class="card-body">
                            <div class="d-flex align-items-center">
                                <div class="user-rank me-3">
                                    <span class="badge bg-primary rounded-circle p-2">{{ index + 1 }}</span>
                                </div>
                                <div>
                                    <h6 class="mb-1">@{{ user.username }}</h6>
                                    <p class="small text-muted mb-0">{{ user.name }} {{ user.surname }}</p>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</template>

<script>
import CardComponent from '@/components/Cards/CardComponent.vue'

export default {
    name: 'TrendsComponent',
    components: {
        CardComponent
    },
    props: {
        trends: {
            type: Object,
            required: true
        }
    },
    methods: {
        refreshPage() {
            window.location.reload();
        }
    }
}
</script>

<style scoped>
.trends-container {
    background-color: #f8f9fa;
}

.stat-card {
    transition: transform 0.3s ease;
    border: none;
    box-shadow: 0 2px 4px rgba(0,0,0,0.05);
}

.stat-card:hover {
    transform: translateY(-5px);
}

.stat-icon {
    color: #0d6efd;
}

.user-card {
    transition: transform 0.3s ease;
    border: none;
    box-shadow: 0 2px 4px rgba(0,0,0,0.05);
}

.user-card:hover {
    transform: translateY(-5px);
}

.user-rank .badge {
    width: 30px;
    height: 30px;
    display: flex;
    align-items: center;
    justify-content: center;
}

h5 {
    color: #495057;
    font-weight: 600;
}
</style>