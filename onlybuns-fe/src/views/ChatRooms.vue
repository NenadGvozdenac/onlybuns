<template>
    <div class="layout">
        <header>
            <NavbarAuthorized v-if="isLoggedIn()" />
            <NavbarUnauthorized v-else />
        </header>

        <div class="home">
            <h1>OnlyBuns Chat Rooms</h1>
            <p>Connect and chat with fellow bun enthusiasts!</p>
        </div>

        <main class="content">
            <div class="container mt-5">
                <!-- Add new room form -->
                <div class="row mb-4">
                    <div class="col-12 d-flex">
                        <input type="text" class="form-control me-2" v-model="newRoomName"
                            placeholder="Enter room name">
                        <button class="btn btn-primary" @click="createRoom">
                            Add New Room
                        </button>
                    </div>
                </div>

                <div class="row">
                    <div class="col-12 col-md-6 col-lg-4 mb-4" v-for="(card, index) in rooms" :key="index">
                        <ChatRoomCard :id="card.id" :name="card.name" :admin="card.admin" />
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
        },

        async createRoom() {
            if (this.newRoomName.trim()) {
                try {
                    await ChatService.createRoom(this.newRoomName);
                    this.newRoomName = '';
                    this.fetchMyRooms();
                } catch (error) {
                    console.error('Failed to create room:', error);
                }
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