<template>
    <NavbarAuthorized v-if="isLoggedIn()" />
    <NavbarUnauthorized v-else />

    <TrendsComponent v-if="trends" :trends="trends" />

    <Footer />
</template>

<script>
import NavbarUnauthorized from '@/components/Unauthorized/Navbar.vue'
import NavbarAuthorized from '@/components/Authorized/Navbar.vue'
import Footer from '@/components/Unauthorized/Footer.vue'
import TrendsComponent from '@/components/Authorized/TrendsComponent.vue';

import TrendService from '@/services/TrendsService';

export default {
    name: 'Trends',
    components: {
        NavbarUnauthorized,
        NavbarAuthorized,
        Footer,
        TrendsComponent
    },
    data() {
        return {
            trends: null
        };
    },
    mounted() {
        this.getTrends();
    },
    methods: {
        isLoggedIn() {
            return localStorage.getItem('token') !== null;
        },
        
        getTrends() {
            TrendService.getTrends()
                .then(response => {
                    this.trends = response;
                    console.log('Trends:', this.trends);
                })
                .catch(error => {
                    console.log('Error:', error);
                });
        }
    }
}

</script>