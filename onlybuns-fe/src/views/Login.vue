<template>
    <div class="wrapper">
        <div class="logo">
            <img src="https://flowbite.com/docs/images/logo.svg" alt="Twitter Logo" class="h-8 me-2">
        </div>
        <div class="text-center mt-4 name">
            Onlybuns
        </div>
        <form class="p-3 mt-3" @submit.prevent="handleLogin">
            <div class="form-field d-flex align-items-center">
                <input type="text" v-model="username" id="username" placeholder="Username" required>
            </div>
            <div class="form-field d-flex align-items-center">
                <input type="password" v-model="password" id="pwd" placeholder="Password" required>
            </div>
            <button class="btn mt-3" type="submit">Login</button>
        </form>
        <div class="text-center fs-6">
            <a href="/register">Don't have an account? Register</a>
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
                console.log('Error:', error)
                if (error.response.status === 401 || error.response.status === 404) {
                    this.modalTitle = 'Login Failed';
                    this.modalMessage = 'Invalid username or password';
                    this.showModal = true;
                } else if (error.response.status === 403) {
                    this.modalTitle = 'Login Failed';
                    this.modalMessage = 'You aren\'t verified yet!';
                    this.showModal = true;
                } else if( error.response.status === 429) {
                    this.modalTitle = 'Login Failed';
                    this.modalMessage = 'Too many requests. Please try again later.';
                    this.showModal = true;
                } else {
                    this.modalTitle = 'Login Failed';
                    this.modalMessage = 'An error occurred. Please try again later.';
                    this.showModal = true;
                }
            }
        },

        handleModalClose() {
            this.showModal = false;
        }
    }
}
</script>

<style scoped>
/* Importing fonts from Google */
@import url('https://fonts.googleapis.com/css2?family=Poppins:wght@300;400;500;600;700;800;900&display=swap');

/* Reseting */
* {
    margin: 0;
    padding: 0;
    box-sizing: border-box;
    font-family: 'Poppins', sans-serif;
}

body {
    background: #ecf0f3;
}

.wrapper {
    max-width: 350px;
    min-height: 500px;
    margin: 100px auto;
    padding: 40px 30px 30px 30px;
    background-color: #ecf0f3;
    border-radius: 15px;
    box-shadow: 13px 13px 20px #cbced1, -13px -13px 20px #fff;
}

.logo {
    width: 80px;
    margin: auto;
}

.logo img {
    width: 100%;
    height: 80px;
    object-fit: cover;
    border-radius: 50%;
    box-shadow: 0px 0px 3px #5f5f5f,
        0px 0px 0px 5px #ecf0f3,
        8px 8px 15px #a7aaa7,
        -8px -8px 15px #fff;
}

.wrapper .name {
    font-weight: 600;
    font-size: 1.4rem;
    letter-spacing: 1.3px;
    padding-left: 10px;
    color: #555;
}

.wrapper .form-field input {
    width: 100%;
    display: block;
    border: none;
    outline: none;
    background: none;
    font-size: 1.2rem;
    color: #666;
    padding: 10px 15px 10px 10px;
}

.wrapper .form-field {
    padding-left: 10px;
    margin-bottom: 20px;
    border-radius: 20px;
    box-shadow: inset 8px 8px 8px #cbced1, inset -8px -8px 8px #fff;
}

.wrapper .form-field .fas {
    color: #555;
}

.wrapper .btn {
    box-shadow: none;
    width: 100%;
    height: 40px;
    background-color: #03A9F4;
    color: #fff;
    border-radius: 25px;
    box-shadow: 3px 3px 3px #b1b1b1,
        -3px -3px 3px #fff;
    letter-spacing: 1.3px;
}

.wrapper .btn:hover {
    background-color: #039BE5;
}

.wrapper a {
    text-decoration: none;
    font-size: 0.8rem;
    color: #03A9F4;
}

.wrapper a:hover {
    color: #039BE5;
}

@media(max-width: 380px) {
    .wrapper {
        margin: 30px 20px;
        padding: 40px 15px 15px 15px;
    }
}
</style>
