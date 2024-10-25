<template>
    <div class="wrapper">
        <div class="logo">
            <img src="https://flowbite.com/docs/images/logo.svg" alt="Twitter Logo" class="h-8 me-2">
        </div>
        <div class="text-center mt-4 name">
            Register
        </div>
        <form class="mt-3 d-flex justify-content-center gap-5" @submit.prevent="handleRegister">
            <!-- General Information Section -->
            <fieldset class="form-section">
                <legend>General Information</legend>
                <div class="form-field d-flex align-items-center">
                    <input type="text" v-model="name" placeholder="Name" required>
                </div>
                <div class="form-field d-flex align-items-center">
                    <input type="text" v-model="surname" placeholder="Surname" required>
                </div>
                <div class="form-field d-flex align-items-center">
                    <input type="text" v-model="username" placeholder="Username" required>
                </div>
                <div class="form-field d-flex align-items-center">
                    <input type="email" v-model="email" placeholder="Email" required>
                </div>
                <div class="form-field d-flex align-items-center">
                    <input type="password" v-model="password" placeholder="Password" required>
                </div>
                <div class="form-field d-flex align-items-center">
                    <input type="password" v-model="confirmPassword" placeholder="Confirm Password" required>
                </div>
            </fieldset>

            <!-- Address Information Section -->
            <fieldset class="form-section">
                <legend>Address Information</legend>
                <div class="form-field d-flex align-items-center">
                    <input type="text" v-model="address.street" placeholder="Street" required>
                </div>
                <div class="form-field d-flex align-items-center">
                    <input type="number" v-model="address.number" placeholder="Number" required>
                </div>
                <div class="form-field d-flex align-items-center">
                    <input type="text" v-model="address.city" placeholder="City" required>
                </div>
                <div class="form-field d-flex align-items-center">
                    <input type="text" v-model="address.country" placeholder="Country" required>
                </div>
            </fieldset>
        </form>
        <button class="btn mt-3" type="submit" @click="handleRegister()">Register</button>
        <div class="text-center fs-6">
            <a href="/login">Already have an account? Login</a>
        </div>
    </div>
    
    <Modal :show="showModal" :title="modalTitle" :message="modalMessage" @close="handleModalClose" />
</template>

<script>
import Modal from '@/components/Layout/Modal.vue';
import axios from 'axios';

export default {
    name: 'Register',
    components: {
        Modal
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
                country: ''
            },
            showModal: false,
            modalTitle: '',
            modalMessage: '',
            successfulRegistration: false
        };
    },
    methods: {
        async handleRegister() {
            if (this.password !== this.confirmPassword) {
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

                // If code is 409, email or username already exists
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
    max-width: 700px;
    min-height: 600px;
    /* Increased height for more fields */
    margin: 80px auto;
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

.form-section {
    margin-bottom: 20px;
    /* Spacing between sections */
    padding: 10px;
    /* Padding inside the section */
    border-radius: 10px;
    /* Rounded corners */
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

.wrapper .form-field .fas,
.wrapper .form-field .far {
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
