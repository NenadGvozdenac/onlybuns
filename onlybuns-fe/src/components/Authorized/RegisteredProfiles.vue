<template>
 <Navbar />
</template>

<script>

import ProfileService from '@/services/ProfileService';
import Navbar from './Navbar.vue';

export default {
    name: 'RegisteredProfiles',
    components: {
        Navbar
    },
    data() {
        return {
            
        }
    },
    mounted() {
        
    },
    methods: {
    }
}

</script>

<style>

* {
    margin: 0;
    padding: 0;
    box-sizing: border-box;
    font-family: 'Poppins', sans-serif;
}

.user-card {
    max-width: 750px;
    min-height: 300px;
    padding: 50px 20px;
    background-color: #ecf0f3;
    border-radius: 55px;
    box-shadow: 10px 10px 20px #cbced1, -10px -10px 20px #fff;
    text-align: center;
    margin: auto;
}

.user-card-logo {
    width: 60px;
    margin: 0 auto 10px;
}

.user-card-logo img {
    width: 100%;
    height: auto;
    object-fit: cover;
    border-radius: 50%;
    box-shadow: 0px 0px 3px #5f5f5f, 0px 0px 0px 5px #ecf0f3, 5px 5px 10px #a7aaa7, -5px -5px 10px #fff;
}

.user-name {
    font-weight: 600;
    font-size: 1.2rem;
    color: #555;
    margin-bottom: 15px;
}

.user-card-title {
    font-size: 1.3rem;
    font-weight: 500;
    color: #333;
    margin-bottom: 15px;
}

.user-info-table {
    display: flex;
    flex-direction: column;
    gap: 10px;
    margin-top: 20px;
    text-align: left;
}

.user-info-row {
    display: flex;
    justify-content: space-between;
    padding: 10px 5px;
    border-bottom: 1px solid #cbced1;
}

.user-info-label {
    font-weight: 500;
    color: #333;
}

.user-info-value {
    color: #666;
    text-align: right;
    flex: 1;
}
</style>