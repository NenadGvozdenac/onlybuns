<template>
    <NavbarAuthorized v-if="isLoggedIn()" />
    <NavbarUnauthorized v-else />

    <div class="home">
        <h1>Your Following Feed!</h1>
        <p>Catch up on the latest posts from your favorite people</p>
    </div>
    <div class="d-flex">
        <div class="col-md-10 col-12">
            <FollowingFeedCardContainer />
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
import PersonContainer from '@/components/People/PersonContainer.vue';
import Footer from '@/components/Unauthorized/Footer.vue';
import AuthService from '@/services/AuthService';
import FollowingFeedCardContainer from '@/components/Cards/FollowingFeedCardContainer.vue';

export default {
    name: 'FollowingFeed',
    components: {
        NavbarAuthorized,
        NavbarUnauthorized,
        FollowingFeedCardContainer,
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