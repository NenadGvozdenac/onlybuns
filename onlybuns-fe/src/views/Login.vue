<template>
    <div class="register-container min-h-screen py-12 px-4 min-vh-100 d-flex align-items-center ">
        <div class="container">
            <div class="row justify-content-center">
                <div class="col-12 col-lg-6">
                    <div class="card shadow-lg p-2">
                        <div class="card-body p-4 p-lg-5">
                            <div class="text-center mb-4">
                                <div class="logo-wrapper mb-4">
                                    <img src="https://flowbite.com/docs/images/logo.svg" alt="Logo" class="logo-img">
                                </div>
                                <h2 class="fw-bold text-primary mb-2">Sign in the account</h2>
                                <p class="text-muted">Fill in your details to get started</p>
                            </div>
                            <form @submit.prevent="handleLogin">
                                <div class="row">
                                    <div class="mb-3">
                                        <label for="username" class="form-label">Username</label>
                                        <input type="text" class="form-control" id="username" v-model="username" required>
                                    </div>
                                    <div class="mb-3">
                                        <label for="password" class="form-label">Password</label>
                                        <input type="password" class="form-control" id="password" v-model="password" required>
                                    </div>
                                    <div class="col-12 text-center mt-4">
                                        <button type="submit" class="btn btn-primary btn-lg px-5">Hop in</button>
                                        <p class="mt-4 mb-0">
                                            Don't have an account?
                                            <router-link to="/register" class="text-primary text-decoration-none">Sign
                                                up</router-link>
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

    <Modal :show="showModal" :title="modalTitle" :message="modalMessage" @close="handleModalClose" />
</template>

<script>
import Modal from '@/components/Layout/Modal.vue';
import AuthService from '@/services/AuthService';

export default {
    name: 'Login',
    components: {
        Modal
    },
    data() {
        return {
            username: '',
            password: '',
            showModal: false,
            modalTitle: '',
            modalMessage: ''
        };
    },
    methods: {
        async handleLogin() {
            try {
                let data = {
                    username: this.username,
                    password: this.password
                };

                await AuthService.login(data);
                this.$router.push('/');
            } catch (error) {
                console.log('Error:', error);
                if (error.response.status === 401 || error.response.status === 404) {
                    this.modalTitle = 'Login Failed';
                    this.modalMessage = 'Invalid username or password';
                } else if (error.response.status === 403) {
                    this.modalTitle = 'Login Failed';
                    this.modalMessage = 'You aren\'t verified yet!';
                } else if (error.response.status === 429) {
                    this.modalTitle = 'Login Failed';
                    this.modalMessage = 'Too many requests. Please try again later.';
                } else {
                    this.modalTitle = 'Login Failed';
                    this.modalMessage = 'An error occurred. Please try again later.';
                }
                this.showModal = true;
            }
        },
        handleModalClose() {
            this.showModal = false;
            this.clearData();
        },
        clearData() {
            this.username = '';
            this.password = '';
        }
    }
};
</script>

<style scoped>
@import url('https://fonts.googleapis.com/css2?family=Poppins:wght@300;400;500;600;700&display=swap');

* {
    font-family: 'Poppins', sans-serif;
}

.register-container {
    background: linear-gradient(135deg, #f5f7fa 0%, #c3cfe2 100%);
}

.card {
    border-radius: 1rem;
    transition: transform 0.2s ease-in-out;
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

.form-control {
    border-radius: 0.75rem;
    padding: 0.75rem 1.25rem;
    border: 1px solid #dee2e6;
    transition: all 0.2s ease-in-out;
    width: 100%;
    font-size: 1rem;
    color: #495057;
    background-color: #fff;
}

.form-control:focus {
    outline: none;
    border-color: #80bdff;
    box-shadow: 0 0 0 0.25rem rgba(13, 110, 253, 0.15);
}

.btn-primary {
    border-radius: 0.75rem;
    padding: 0.75rem 2rem;
    transition: all 0.2s ease-in-out;
    background-color: #0d6efd;
    border: none;
    cursor: pointer;
}

.btn-primary:hover {
    transform: translateY(-1px);
    box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
    background-color: #0b5ed7;
}

@media (max-width: 767.98px) {
    .card {
        margin: 1rem;
    }
}
</style>