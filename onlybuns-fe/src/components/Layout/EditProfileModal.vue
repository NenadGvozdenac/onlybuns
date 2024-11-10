<template>
    <div class="modal fade" id="editProfileModal" tabindex="-1" aria-labelledby="editProfileModalLabel"
        aria-hidden="true">
        <div class="modal-dialog modal-dialog-centered modal-lg">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title" id="editProfileModalLabel">Edit Profile</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close" @click="closeModal"></button>
                </div>
                <div class="modal-body">
                    <form @submit.prevent="handleSubmit" class="needs-validation" novalidate>
                        <div class="row g-4">
                            <!-- Left Column -->
                            <div class="col-12 col-md-6 border-end">
                                <h5 class="card-title mb-4">Personal Information</h5>

                                <div class="mb-3">
                                    <label class="form-label">Username</label>
                                    <input type="text" class="form-control form-control-lg" v-model="formData.username"
                                        placeholder="Enter your username" disabled>
                                </div>

                                <div class="mb-3">
                                    <label class="form-label">Email</label>
                                    <input type="email" class="form-control form-control-lg" v-model="userData.email"
                                        placeholder="Enter your email" disabled>
                                </div>

                                <div class="mb-3">
                                    <label class="form-label">Name</label>
                                    <input type="text" class="form-control form-control-lg" v-model="formData.name"
                                        placeholder="Enter your name" required>
                                </div>

                                <div class="mb-3">
                                    <label class="form-label">Last Name</label>
                                    <input type="text" class="form-control form-control-lg" v-model="formData.surname"
                                        placeholder="Enter your surname" required>
                                </div>

                                <div class="mb-3">
                                    <label class="form-label">Password</label>
                                    <input type="password" class="form-control form-control-lg"
                                        :class="{ 'is-invalid': isPasswordTouched && !doPasswordsMatch }"
                                        v-model="formData.password" @blur="passwordTouched"
                                        placeholder="Enter new password">
                                </div>

                                <div class="mb-3">
                                    <label class="form-label">Confirm Password</label>
                                    <input type="password" class="form-control form-control-lg"
                                        :class="{ 'is-invalid': isPasswordTouched && !doPasswordsMatch }"
                                        v-model="formData.confirmPassword" @blur="passwordTouched"
                                        placeholder="Confirm new password">
                                    <div class="invalid-feedback">
                                        Passwords do not match
                                    </div>
                                </div>
                            </div>

                            <!-- Right Column -->
                            <div class="col-12 col-md-6">
                                <h5 class="card-title mb-4">Location</h5>
                                <div class="map-container rounded overflow-hidden mb-3">
                                    <MapComponent
                                        v-if="formData.address && formData.address.longitude && formData.address.latitude"
                                        @location-updated="updateLocationInfo" :address="{
                                            street: formData.address.street,
                                            number: formData.address.number,
                                            city: formData.address.city,
                                            country: formData.address.country,
                                            longitude: formData.address.longitude,
                                            latitude: formData.address.latitude
                                        }" />
                                </div>

                                <!-- Location Details -->
                                <div class="location-details p-3 bg-light rounded">
                                    <div class="d-flex align-items-center mb-2">
                                        <i class="bi bi-geo-alt-fill text-primary me-2"></i>
                                        <span class="text-muted">Selected Location:</span>
                                    </div>
                                    <p class="mb-1"><small>{{ formData.address.street }} {{ formData.address.number
                                            }}</small></p>
                                    <p class="mb-1"><small>{{ formData.address.city }}, {{ formData.address.country
                                            }}</small></p>
                                </div>
                            </div>
                        </div>

                        <div class="modal-footer mt-4">
                            <button type="button" class="btn btn-secondary" @click="closeModal">Close</button>
                            <button type="submit" class="btn btn-primary" :disabled="isButtonDisabled()">
                                Save changes
                            </button>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>
</template>

<script>
import MapComponent from '@/components/Layout/MapComponent.vue';

export default {
    name: 'EditProfileModal',
    components: {
        MapComponent
    },
    props: {
        userData: {
            type: Object,
            required: true
        }
    },
    data() {
        return {
            formData: {
                username: '',
                name: '',
                surname: '',
                password: '',
                confirmPassword: '',
                address: {
                    street: '',
                    number: '',
                    city: '',
                    country: '',
                    longitude: 0,
                    latitude: 0
                }
            },
            isPasswordTouched: false
        }
    },
    computed: {
        doPasswordsMatch() {
            if (!this.formData.password && !this.formData.confirmPassword) {
                return false;
            }
            return this.formData.password === this.formData.confirmPassword;
        }
    },
    watch: {
        userData: {
            handler(newValue) {
                this.formData = {
                    username: newValue.username,
                    name: newValue.name,
                    surname: newValue.surname,
                    password: '',
                    confirmPassword: '',
                    address: { ...newValue.address }
                }
            },
            immediate: true
        }
    },
    methods: {
        passwordTouched() {
            if (this.formData.password || this.formData.confirmPassword) {
                this.isPasswordTouched = true;
            }
        },
        updateLocationInfo(locationInfo) {
            this.formData.address = {
                street: locationInfo.street,
                number: locationInfo.number,
                city: locationInfo.city,
                country: locationInfo.country,
                longitude: locationInfo.longitude,
                latitude: locationInfo.latitude
            };
        },
        isButtonDisabled() {
            return !this.formData.name ||
                !this.formData.surname ||
                !this.formData.address.street ||
                !this.formData.address.city ||
                !this.formData.address.country ||
                !this.doPasswordsMatch;
        },
        handleSubmit() {
            if (!this.doPasswordsMatch) {
                alert('Passwords do not match!');
                return;
            }

            const updateData = {
                name: this.formData.name,
                surname: this.formData.surname,
                address: { ...this.formData.address }
            };

            if (this.formData.password) {
                updateData.password = this.formData.password;
                updateData.confirmPassword = this.formData.confirmPassword;
            }

            this.$emit('update-profile', updateData);
            this.closeModal();
        },
        closeModal() {
            // Reset form data to initial values
            this.formData = {
                username: this.userData.username,
                name: this.userData.name,
                surname: this.userData.surname,
                password: '',
                confirmPassword: '',
                address: { ...this.userData.address }
            };
            this.isPasswordTouched = false;

            // Close the modal using Bootstrap
            const modal = document.getElementById('editProfileModal');
            const bootstrapModal = bootstrap.Modal.getInstance(modal);
            if (bootstrapModal) {
                bootstrapModal.hide();
            }
        }
    }
}
</script>

<style scoped>
.modal-dialog {
    max-width: 900px;
}

.form-control {
    border-radius: 0.75rem;
    padding: 0.75rem 1.25rem;
    border: 1px solid #dee2e6;
    transition: all 0.2s ease-in-out;
}

.form-control:focus {
    box-shadow: 0 0 0 0.25rem rgba(13, 110, 253, 0.15);
}

.form-control.is-invalid {
    border-color: #dc3545;
    box-shadow: none;
}

.form-control.is-invalid:focus {
    box-shadow: 0 0 0 0.25rem rgba(220, 53, 69, 0.25);
}

.map-container {
    height: 470px;
    background: #f8f9fa;
    border: 1px solid #dee2e6;
}

.location-details {
    border: 1px solid #dee2e6;
    height: 7.5rem;
}

@media (max-width: 767.98px) {
    .border-end {
        border-right: none !important;
        border-bottom: 1px solid #dee2e6;
        padding-bottom: 2rem;
        margin-bottom: 2rem;
    }
}
</style>