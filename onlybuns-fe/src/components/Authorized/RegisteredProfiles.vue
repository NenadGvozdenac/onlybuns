<template>
    <Navbar />
    <div class="container my-5">
        <div class="card border-0 shadow-lg rounded-lg">
            <div class="card-header bg-primary text-white border-bottom-0 p-4">
                <h2 class="card-title mb-0 fw-bold">Verified Profiles</h2>
            </div>
            <div class="card-body p-4">
                <div class="table-responsive">
                    <table class="table table-hover table-striped table-bordered mb-0">
                        <thead class="table-light">
                            <tr>
                                <th>Name</th>
                                <th>Surname</th>
                                <th>Email</th>
                                <th>Active Posts</th>
                                <th>Following</th>
                            </tr>
                        </thead>
                        <tbody>
                            <tr v-for="profile in profiles" :key="profile.email">
                                <td>{{ profile.name }}</td>
                                <td>{{ profile.surname }}</td>
                                <td>{{ profile.email }}</td>
                                <td>{{ profile.activePosts.length }}</td>
                                <td>{{ profile.following.length }}</td>
                            </tr>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </div>
</template>

<script>
import ProfileService from '@/services/ProfileService';
import Navbar from './Navbar.vue';

export default {
    name: 'RegisteredProfiles',
    components: {
        Navbar
    },
    data() {
        return {
            profiles: []
        }
    },
    mounted() {
        this.getVerifiedProfiles();
    },
    methods: {
        async getVerifiedProfiles() {
            try {
                this.profiles = await ProfileService.getVerifiedProfiles();
                console.log(this.profiles);
            } catch (error) {
                console.error("Failed to load profiles: ", error)
            }
        }
    }
}
</script>
<style>
/* No additional styles needed as Bootstrap handles the majority of the styling */
</style>