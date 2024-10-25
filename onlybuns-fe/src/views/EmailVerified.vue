<template>
    <div class="container d-flex flex-column justify-content-center align-items-center vh-100">
        <div v-if="loading" class="text-center">
            <h1 class="display-4">Verifying...</h1>
            <p class="lead">Please wait while we verify your email.</p>
        </div>
        <div v-else-if="isVerified" class="text-center">
            <h1 class="display-4">Your Email is Activated!</h1>
            <p class="lead">Thank you for verifying your email address.</p>
            <a href="/login" class="btn btn-primary btn-lg mt-3">Go to Login</a>
        </div>
        <div v-else class="text-center">
            <h1 class="display-4">Email Verification Failed!</h1>
            <p class="lead">Sorry, we are unable to verify this email address. Maybe it's already been verified?</p>
            <a href="/login" class="btn btn-primary btn-lg mt-3">Go to Login</a>
        </div>
    </div>
</template>

<script>
import EmailService from '@/services/EmailService';

export default {
    name: 'EmailVerified',
    data() {
        return {
            token: null,
            isVerified: false,
            loading: true // Add loading state
        };
    },
    methods: {
        async verifyEmail() {
            try {
                let response = await EmailService.verifyEmail(this.token);
                if (response.status === 200) {
                    this.isVerified = true;
                } else {
                    this.isVerified = false;
                }
            } catch (error) {
                console.log(error);
                this.isVerified = false;
            } finally {
                this.loading = false;
            }
        }
    },
    mounted() {
        this.token = this.$route.params.token;
        this.verifyEmail();
    }
}
</script>

<style scoped></style>
