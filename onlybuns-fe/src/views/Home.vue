<template>
    <Navbar />
    <div class="home">
        <h1>Welcome to OnlyBuns!</h1>
        <p>Your one-stop for the best buns in town!</p>
    </div>
    <div class="d-flex">
        <div class="col-10">
            <CardContainer />
        </div>
        <div class="col-2">
            <PersonContainer />
        </div>
    </div>
    <Footer />
</template>

<script>
import Navbar from '@/components/Unauthorized/Navbar.vue';
import CardContainer from '@/components/Cards/CardContainer.vue';
import PersonContainer from '@/components/People/PersonContainer.vue';
import Footer from '@/components/Unauthorized/Footer.vue';

export default {
    name: 'Home',
    components: {
        Navbar,
        CardContainer,
        PersonContainer,
        Footer,
    }
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