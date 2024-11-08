<template>
    <Navbar />
    <div class="container my-5">
        <div class="card border-0 shadow-lg rounded-lg">
            <div class="card-header bg-primary text-white border-bottom-0 p-4">
                <h2 class="card-title mb-0 fw-bold">Verified Profiles</h2>
            </div>
            <div class="card-body p-4">
                <!-- Filters Section -->
                <div class="mb-4">
                    <h5>Filters</h5>
                    <div class="form-group row">
                        <div class="col-md-4">
                            <label for="name" class="form-label">Name</label>
                            <input v-model="filters.name" id="name" class="form-control" type="text"
                                placeholder="Enter name" />
                        </div>
                        <div class="col-md-4">
                            <label for="surname" class="form-label">Surname</label>
                            <input v-model="filters.surname" id="surname" class="form-control" type="text"
                                placeholder="Enter surname" />
                        </div>
                        <div class="col-md-4">
                            <label for="email" class="form-label">Email</label>
                            <input v-model="filters.email" id="email" class="form-control" type="email"
                                placeholder="Enter email" />
                        </div>
                    </div>
                    <div class="form-group row mt-2">
                        <div class="col-md-4">
                            <label for="minActivePosts" class="form-label">Min Active Posts</label>
                            <input v-model="filters.minActivePosts" id="minActivePosts" class="form-control"
                                type="number" placeholder="Min posts" />
                        </div>
                        <div class="col-md-4">
                            <label for="maxActivePosts" class="form-label">Max Active Posts</label>
                            <input v-model="filters.maxActivePosts" id="maxActivePosts" class="form-control"
                                type="number" placeholder="Max posts" />
                        </div>
                        <div class="col-md-4">
                            <label for="sortBy" class="form-label">Sort By</label>
                            <select v-model="filters.sortBy" id="sortBy" class="form-control">
                                <option value="">Select Sorting</option>
                                <option value="following">Following</option>
                                <option value="email">Email</option>
                            </select>
                        </div>
                    </div>
                    <div class="mt-3 text-end">
                        <button @click="applyFilters" class="btn btn-primary">Apply Filters</button>
                    </div>
                </div>

                <!-- Profiles Table -->
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

                <!-- Pagination Controls -->
                <div class="mt-3 text-center">
                    <button class="btn btn-secondary" @click="changePage(filters.page - 1)" :disabled="filters.page <= 1">
                        Previous
                    </button>
                    <span class="mx-2">Page {{ filters.page }}</span>
                    <button class="btn btn-secondary" @click="changePage(filters.page + 1)" :disabled="noMoreProfiles">
                        Next
                    </button>
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
            profiles: [],
            filters: {
                name: '',
                surname: '',
                email: '',
                minActivePosts: null,
                maxActivePosts: null,
                sortBy: '',
                page: 1,
            }
        };
    },
    mounted() {
        this.getVerifiedProfiles();
    },
    methods: {
        // Fetch profiles with applied filters
        async getVerifiedProfiles() {
            try {
                const filters = this.filters;
                let response = await ProfileService.getVerifiedProfiles(filters);

                if(response.length === 0) {
                    this.filters.page -= 1; 
                }
                else {
                    this.profiles = response;
                }


            } catch (error) {
                console.error("Failed to load profiles: ", error);
            }
        },

        async getVerifiedProfilesFilter() {
            try {
                const filters = this.filters;
                this.profiles = await ProfileService.getVerifiedProfiles(filters);

            } catch (error) {
                console.error("Failed to load profiles: ", error);
            }
        },

        // Apply the filters and fetch updated profiles
        applyFilters() {
            this.filters.page = 1; // Reset to the first page when filters change
            this.noMoreProfiles = false; // Reset noMoreProfiles flag when filters are applied
            this.getVerifiedProfilesFilter(); // Trigger fetch with current filters
        },

        // Change the page and fetch the corresponding profiles
        changePage(page) {
            if (page < 1 || this.noMoreProfiles) return; // Stop if no more profiles to display
            this.filters.page = page;
            this.getVerifiedProfiles();
        }
    }
};
</script>


<style>
/* No additional styles needed as Bootstrap handles the majority of the styling */
</style>
