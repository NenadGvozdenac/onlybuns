<template>
    <Navbar />
    <div class="container-xl py-5">
        <div class="row g-4">
            <!-- Filters Panel -->
            <div class="col-12 col-lg-3">
                <div class="card border-0 shadow-sm h-100">
                    <div class="card-body p-4">
                        <div class="d-flex align-items-center mb-4">
                            <div class="bg-warning bg-opacity-10 p-2 rounded-circle me-3">
                                <i class="bi bi-funnel text-warning"></i>
                            </div>
                            <h5 class="mb-0">Search & Filter</h5>
                        </div>

                        <div class="vstack gap-3">
                            <div>
                                <label class="form-label small text-muted fw-medium">First Name</label>
                                <div class="input-group input-group-custom">
                                    <span class="input-group-text border-end-0 bg-transparent">
                                        <i class="bi bi-person-badge text-muted"></i>
                                    </span>
                                    <input v-model="filters.name" type="text" 
                                           class="form-control border-start-0 ps-0 shadow-none" 
                                           placeholder="Enter first name">
                                </div>
                            </div>

                            <div>
                                <label class="form-label small text-muted fw-medium">Last Name</label>
                                <div class="input-group input-group-custom">
                                    <span class="input-group-text border-end-0 bg-transparent">
                                        <i class="bi bi-person text-muted"></i>
                                    </span>
                                    <input v-model="filters.surname" type="text" 
                                           class="form-control border-start-0 ps-0 shadow-none" 
                                           placeholder="Enter last name">
                                </div>
                            </div>

                            <div>
                                <label class="form-label small text-muted fw-medium">Email</label>
                                <div class="input-group input-group-custom">
                                    <span class="input-group-text border-end-0 bg-transparent">
                                        <i class="bi bi-envelope text-muted"></i>
                                    </span>
                                    <input v-model="filters.email" type="email" 
                                           class="form-control border-start-0 ps-0 shadow-none" 
                                           placeholder="user@example.com">
                                </div>
                            </div>

                            <hr class="text-muted opacity-25">

                            <div>
                                <label class="form-label small text-muted fw-medium">Posts Range</label>
                                <div class="row g-2">
                                    <div class="col">
                                        <input v-model="filters.minActivePosts" type="number" 
                                               class="form-control shadow-none" placeholder="Min">
                                    </div>
                                    <div class="col-auto d-flex align-items-center">
                                        <span class="text-muted">—</span>
                                    </div>
                                    <div class="col">
                                        <input v-model="filters.maxActivePosts" type="number" 
                                               class="form-control shadow-none" placeholder="Max">
                                    </div>
                                </div>
                            </div>

                            <div>
                                <label class="form-label small text-muted fw-medium">Sort By</label>
                                <select v-model="filters.sortBy" class="form-select shadow-none">
                                    <option value="">Default order</option>
                                    <option value="following">Following count</option>
                                    <option value="email">Email address</option>
                                </select>
                            </div>

                            <button @click="applyFilters" 
                                    class="btn btn-primary w-100 mt-2 d-flex align-items-center justify-content-center gap-2">
                                <i class="bi bi-search"></i>
                                Apply Filters
                            </button>
                        </div>
                    </div>
                </div>
            </div>

            <!-- Main Content -->
            <div class="col-12 col-lg-9">
                <div class="card border-0 shadow-sm">
                    <div class="card-header border-0 bg-white pt-4 pb-3 px-4">
                        <div class="d-flex justify-content-between align-items-center flex-wrap gap-3">
                            <div>
                                <h4 class="mb-1">Verified Profiles Directory</h4>
                                <p class="text-muted mb-0 small">Browse and filter verified user profiles</p>
                            </div>
                            <div class="d-flex align-items-center gap-2">
                                <span class="badge bg-success-subtle text-success rounded-pill px-3 py-2">
                                    <i class="bi bi-shield-check me-1"></i>
                                    All Verified
                                </span>
                            </div>
                        </div>
                    </div>

                    <div class="table-responsive">
                        <table class="table align-middle mb-0">
                            <thead>
                                <tr class="bg-light">
                                    <th class="border-0 ps-4">
                                        <div class="text-muted small fw-medium">First Name</div>
                                    </th>
                                    <th class="border-0">
                                        <div class="text-muted small fw-medium">Last Name</div>
                                    </th>
                                    <th class="border-0">
                                        <div class="text-muted small fw-medium">Email</div>
                                    </th>
                                    <th class="border-0 text-center">
                                        <div class="text-muted small fw-medium">Active Posts</div>
                                    </th>
                                    <th class="border-0 text-center pe-4">
                                        <div class="text-muted small fw-medium">Following</div>
                                    </th>
                                </tr>
                            </thead>
                            <tbody class="border-top-0">
                                <tr v-for="profile in profiles" :key="profile.email" 
                                    class="border-bottom hover-bg-light">
                                    <td class="ps-4">
                                        <div class="d-flex align-items-center">
                                            <div class="bg-primary bg-opacity-10 rounded-circle p-2 me-3">
                                                <i class="bi bi-person-circle text-primary"></i>
                                            </div>
                                            {{ profile.name }}
                                        </div>
                                    </td>
                                    <td>{{ profile.surname }}</td>
                                    <td>
                                        <div class="d-flex align-items-center text-muted">
                                            <i class="bi bi-envelope-fill me-2 text-muted opacity-50"></i>
                                            {{ profile.email }}
                                        </div>
                                    </td>
                                    <td class="text-center">
                                        <span class="badge bg-success-subtle text-success rounded-pill px-2 py-1">
                                            <i class="bi bi-file-text me-1"></i>
                                            {{ profile.activePosts.length }}
                                        </span>
                                    </td>
                                    <td class="text-center pe-4">
                                        <span class="badge bg-info-subtle text-info rounded-pill px-2 py-1">
                                            <i class="bi bi-people me-1"></i>
                                            {{ profile.following.length }}
                                        </span>
                                    </td>
                                </tr>
                            </tbody>
                        </table>
                    </div>

                    <div class="card-footer border-0 bg-white px-4 py-3">
                        <div class="d-flex justify-content-between align-items-center">
                            <span class="text-muted small">
                                <i class="bi bi-info-circle me-1"></i>
                                Page {{ filters.page }}
                            </span>
                            <div class="btn-group shadow-sm">
                                <button @click="changePage(filters.page - 1)" 
                                        :disabled="filters.page <= 1"
                                        class="btn btn-light btn-sm px-3">
                                    <i class="bi bi-chevron-left me-1"></i>
                                    Previous
                                </button>
                                <button @click="changePage(filters.page + 1)" 
                                        :disabled="noMoreProfiles"
                                        class="btn btn-light btn-sm px-3">
                                    Next
                                    <i class="bi bi-chevron-right ms-1"></i>
                                </button>
                            </div>
                        </div>
                    </div>
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
