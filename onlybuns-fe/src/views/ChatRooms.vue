<template>
    <div class="layout">
        <header>
            <NavbarAuthorized v-if="isLoggedIn()" />
            <NavbarUnauthorized v-else />
        </header>

        <main class="content">
            <div class="container mt-5">
                <div class="row">
                    <!-- Bootstrap grid: col-12 for mobile, col-md-6 for tablets, col-lg-4 for desktops -->
                    <div class="col-12 col-md-6 col-lg-4 mb-4" v-for="(card, index) in rooms" :key="index">
                        <ChatRoomCard :id="card.id" :name="card.name" :admin="card.admin"/>
                    </div>
                </div>
            </div>
        </main>

        <Footer />
    </div>
</template>

<script>
import ChatService from '@/services/ChatService';

import ChatRoomCard from '@/components/Cards/ChatRoomCard.vue';
import NavbarUnauthorized from '@/components/Unauthorized/Navbar.vue'
import NavbarAuthorized from '@/components/Authorized/Navbar.vue'
import Footer from '@/components/Unauthorized/Footer.vue'

export default {
    name: 'ChatRooms',
    components: {
        ChatRoomCard,
        NavbarAuthorized,
        NavbarUnauthorized,
        Footer
    },
    data() {
        return {
            rooms: []
        };
    },
    mounted() {
        this.fetchMyRooms();
    },
    methods: {
        isLoggedIn() {
            return localStorage.getItem('token') !== null;
        },

        async fetchMyRooms() {
            try {
                this.rooms = await ChatService.fetchMyRooms();
                console.log(this.rooms);
            } catch (error) {
                console.log(error);
            }
        }

    }
}

</script>
<style scoped>
.layout {
    display: flex;
    flex-direction: column;
    min-height: 100vh;
}

.content {
    flex-grow: 1;
}

footer {
    width: 100%;
    background-color: #fff;
}
</style>