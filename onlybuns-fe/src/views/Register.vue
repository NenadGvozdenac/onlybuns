<template>
    <div class="register-container min-vh-100 d-flex align-items-center py-5">
        <div class="container">
            <div class="row justify-content-center">
                <div class="col-12 col-lg-10">
                    <div class="card shadow-lg border-0">
                        <div class="card-body p-4 p-lg-5">
                            <!-- Header -->
                            <div class="text-center mb-4">
                                <div class="logo-wrapper mb-4">
                                    <img src="https://flowbite.com/docs/images/logo.svg" alt="Logo" class="logo-img">
                                </div>
                                <h2 class="fw-bold text-primary mb-2">Create your account</h2>
                                <p class="text-muted">Fill in your details to get started</p>
                            </div>

                            <form @submit.prevent="handleRegister" class="needs-validation" novalidate>
                                <div class="row g-4">
                                    <!-- Left Column -->
                                    <div class="col-12 col-md-6 border-end">
                                        <h5 class="card-title mb-4">Personal Information</h5>

                                        <div class="mb-3">
                                            <label class="form-label">Name</label>
                                            <input type="text" class="form-control form-control-lg" v-model="name"
                                                placeholder="Enter your name" required>
                                        </div>

                                        <div class="mb-3">
                                            <label class="form-label">Surname</label>
                                            <input type="text" class="form-control form-control-lg" v-model="surname"
                                                placeholder="Enter your surname" required>
                                        </div>

                                        <div class="mb-3">
                                            <label class="form-label">Username</label>
                                            <input type="text" class="form-control form-control-lg" v-model="username"
                                                placeholder="Choose a username" required>
                                        </div>

                                        <div class="mb-3">
                                            <label class="form-label">Email</label>
                                            <input type="email" 
                                                class="form-control form-control-lg" 
                                                :class="{ 'is-invalid': isEmailTouched && !isValidEmail }"
                                                v-model="email"
                                                @blur="emailTouched"
                                                placeholder="Enter your email" 
                                                required>
                                            <div class="invalid-feedback">
                                                Please enter a valid email address
                                            </div>
                                        </div>

                                        <div class="mb-3">
                                            <label class="form-label">Password</label>
                                            <input type="password" 
                                                class="form-control form-control-lg"
                                                :class="{ 'is-invalid': isPasswordTouched && !doPasswordsMatch }"
                                                v-model="password" 
                                                @blur="passwordTouched"
                                                placeholder="Create a password" 
                                                required>
                                        </div>

                                        <div class="mb-3">
                                            <label class="form-label">Confirm Password</label>
                                            <input type="password" 
                                                class="form-control form-control-lg"
                                                :class="{ 'is-invalid': isPasswordTouched && !doPasswordsMatch }"
                                                v-model="confirmPassword" 
                                                @blur="passwordTouched"
                                                placeholder="Confirm your password" 
                                                required>
                                            <div class="invalid-feedback">
                                                Passwords do not match
                                            </div>
                                        </div>
                                    </div>

                                    <!-- Right Column -->
                                    <div class="col-12 col-md-6">
                                        <h5 class="card-title mb-4">Location</h5>
                                        <div class="map-container rounded overflow-hidden mb-3">
                                            <MapComponent @location-updated="updateLocationInfo" />
                                        </div>

                                        <!-- Location Details -->
                                        <div class="location-details p-3 bg-light rounded">
                                            <div class="d-flex align-items-center mb-2">
                                                <i class="bi bi-geo-alt-fill text-primary me-2"></i>
                                                <span class="text-muted">Selected Location:</span>
                                            </div>
                                            <p class="mb-1"><small>{{ address.street }} {{ address.number }}</small></p>
                                            <p class="mb-1"><small>{{ address.city }}, {{ address.country }}</small></p>
                                        </div>
                                    </div>

                                    <!-- Submit Button -->
                                    <div class="col-12 text-center mt-4">
                                        <button type="submit" class="btn btn-primary btn-lg px-5"
                                            :disabled="isButtonDisabled()">
                                            Create Account
                                        </button>
                                        <p class="mt-4 mb-0">
                                            Already have an account?
                                            <router-link to="/login" class="text-primary text-decoration-none">Sign
                                                in</router-link>
                                        </p>
                                    </div>
                                </div>
                            </form>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <!-- Modal Component -->
    <Modal :show="showModal" :title="modalTitle" :message="modalMessage" @close="handleModalClose" />
</template>

<script>
import MapComponent from '@/components/Layout/MapComponent.vue';
import Modal from '@/components/Layout/Modal.vue';
import axios from 'axios';

export default {
    name: 'Register',
    components: {
        Modal,
        MapComponent
    },
    data() {
        return {
            name: '',
            surname: '',
            username: '',
            email: '',
            password: '',
            confirmPassword: '',
            address: {
                street: '',
                city: '',
                number: '',
                country: '',
                longitude: 0,
                latitude: 0
            },
            showModal: false,
            modalTitle: '',
            modalMessage: '',
            successfulRegistration: false,
            registrationSent: false,
            isEmailTouched: false,
            isPasswordTouched: false
        };
    },
    computed: {
        isValidEmail() {
            const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
            return emailRegex.test(this.email);
        },
        doPasswordsMatch() {
            return this.password === this.confirmPassword && this.password !== '';
        }
    },
    methods: {
        emailTouched() {
            this.isEmailTouched = true;
        },
        passwordTouched() {
            this.isPasswordTouched = true;
        },
        async handleRegister() {
            if (!this.doPasswordsMatch) {
                this.showModal = true;
                this.modalTitle = "Error";
                this.modalMessage = "Passwords do not match!";
                return;
            }

            let data = {
                name: this.name,
                surname: this.surname,
                username: this.username,
                email: this.email,
                password: this.password,
                confirmPassword: this.confirmPassword,
                address: this.address
            };

            this.registrationSent = true;

            try {
                const response = await axios.post('http://localhost:8080/auth/register', data);

                console.log("Registration successful:", response.data);
                this.showModal = true;
                this.modalTitle = "Success";
                this.modalMessage = "Registration successful! Please check your email to verify your account.";

                this.successfulRegistration = true;
            } catch (error) {
                console.error("Registration failed:", error.response.data);
                this.showModal = true;
                this.modalTitle = "Error";
                this.modalMessage = "Something went wrong! Please check your credentials.";

                if (error.response.status === 409) {
                    this.modalMessage = "Email or username already exists!";
                }
            }
        },
        handleModalClose() {
            this.showModal = false;

            if (this.successfulRegistration) {
                this.$router.push('/login');
            }
        },
        isButtonDisabled() {
            return !this.name || !this.surname || !this.username || !this.isValidEmail 
                || !this.password || !this.confirmPassword || !this.address.street 
                || !this.address.city || !this.address.number || !this.address.country 
                || !this.doPasswordsMatch
                || this.registrationSent;
        },
        updateLocationInfo(locationInfo) {
            this.address.street = locationInfo.street;
            this.address.number = locationInfo.number;
            this.address.city = locationInfo.city;
            this.address.country = locationInfo.country;
            this.address.longitude = locationInfo.longitude;
            this.address.latitude = locationInfo.latitude;

            console.log("Location info updated:", this.address);
        },
    }
}
</script>

<style scoped>
.register-container {
    background: linear-gradient(135deg, #f5f7fa 0%, #c3cfe2 100%);
}

.card {
    border-radius: 1rem;
}

.logo-wrapper {
    width: 80px;
    height: 80px;
    margin: 0 auto;
    padding: 15px;
    background: white;
    border-radius: 50%;
    box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
}

.logo-img {
    width: 100%;
    height: 100%;
    object-fit: contain;
}

.map-container {
    height: 470px;
    background: #f8f9fa;
    border: 1px solid #dee2e6;
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

.btn-primary {
    border-radius: 0.75rem;
    padding: 0.75rem 2rem;
    transition: all 0.2s ease-in-out;
}

.btn-primary:hover {
    transform: translateY(-1px);
    box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
}

.location-details {
    border: 1px solid #dee2e6;
    height: 7.5rem;
}

@media (max-width: 767.98px) {
    .card-body {
        padding: 2rem !important;
    }

    .border-end {
        border-right: none !important;
        border-bottom: 1px solid #dee2e6;
        padding-bottom: 2rem;
        margin-bottom: 2rem;
    }
}
</style>