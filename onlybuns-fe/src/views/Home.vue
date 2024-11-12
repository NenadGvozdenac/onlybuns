<template>
    <NavbarAuthorized v-if="isLoggedIn()" />
    <NavbarUnauthorized v-else />

    <div class="home">
        <h1>Welcome to OnlyBuns!</h1>
        <p>Your one-stop for the best buns in town!</p>
    </div>
    <div class="d-flex">
        <div class="col-md-10 col-12">
            <CardContainer />
        </div>
        <div class="col-md-2">
            <PersonContainer />
        </div>
    </div>
    <Footer />
</template>

<script>
import NavbarUnauthorized from '@/components/Unauthorized/Navbar.vue';
import NavbarAuthorized from '@/components/Authorized/Navbar.vue';

import CardContainer from '@/components/Cards/CardContainer.vue';
import PersonContainer from '@/components/People/PersonContainer.vue';
import Footer from '@/components/Unauthorized/Footer.vue';

import AuthService from '@/services/AuthService';

export default {
    name: 'Home',
    components: {
        NavbarAuthorized,
        NavbarUnauthorized,
        CardContainer,
        PersonContainer,
        Footer,
    },
    methods: {
        isLoggedIn() {
            return AuthService.isLoggedIn();
        },
    },
}
</script>

<style scoped>
.home {
    text-align: center;
    margin-top: 50px;
}

h1 {
    font-size: 2.5em;
    color: #333;
}

p {
    font-size: 1.2em;
    color: #666;
}
</style>