<template>
    <div class="container-fluid vh-100 d-flex flex-column">
        <NavbarAuthorized v-if="isLoggedIn()" />
        <NavbarUnauthorized v-else />

        <!-- Chat Room Header -->
        <div class="row bg-primary text-white p-3 align-items-center">
            <div class="col">
                <h3 class="mb-0">{{ chatRoom.name }}</h3>
                <small>Admin: {{ chatRoom.admin }}</small>
            </div>
            <div class="col-auto">
                <button class="btn btn-light" @click="openAddUserModal">
                    <i class="bi bi-person-add"></i> Add User
                </button>
            </div>
        </div>

        <!-- Chat Messages Area -->
        <div class="row flex-grow-1 overflow-hidden position-relative">
            <div class="col p-0 d-flex flex-column position-absolute top-0 bottom-0 start-0 end-0">
                <!-- Messages Container -->
                <div ref="messagesContainer" class="flex-grow-1 overflow-auto p-3" style="background-color: #f4f4f4; 
                           display: flex; 
                           flex-direction: column-reverse;">
                    <div class="d-flex flex-column-reverse">
                        <!-- Example Message (You'll replace with v-for) -->
                        <div class="mb-3" v-for="(msg, index) in messages" :key="index">
                            <div class="card">
                                <div class="card-body py-2 px-3">
                                    <strong>{{ msg.username }}</strong>
                                    <p class="mb-0">{{ msg.message }}</p>
                                    <small class="text-muted">{{ msg.timestamp }}</small>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>

                <!-- Message Input (Fixed at Bottom) -->
                <div class="bg-white border-top p-3">
                    <div class="input-group">
                        <input type="text" class="form-control" placeholder="Type your message..."
                            v-model="messageInput">
                        <button class="btn btn-primary" @click="sendMessage">
                            <i class="bi bi-send"></i>
                        </button>
                    </div>
                </div>
            </div>
        </div>

        <!-- Add User Modal -->
        <div class="modal fade" id="addUserModal" tabindex="-1">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title">Add User to Chat</h5>
                        <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                    </div>
                    <div class="modal-body">
                        <input type="text" class="form-control" placeholder="Enter username">
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancel</button>
                        <button type="button" class="btn btn-primary" @click="addUser">Add</button>
                    </div>
                </div>
            </div>
        </div>
    </div>
</template>

<script>
import ChatService from '@/services/ChatService';
import NavbarUnauthorized from '@/components/Unauthorized/Navbar.vue';
import NavbarAuthorized from '@/components/Authorized/Navbar.vue';

export default {
    name: 'Chats',
    components: {
        NavbarAuthorized,
        NavbarUnauthorized
    },
    data() {
        return {
            socket: null,
            chatRoom: {},
            messages: [],  // Stores chat messages
            messageInput: '', // Input for sending messages
        };
    },
    methods: {
        isLoggedIn() {
            return localStorage.getItem('token') !== null;
        },

        connectSocket() {
            // Create a WebSocket connection to the backend
            this.socket = new WebSocket('ws://localhost:8080/ws');
            console.log(this.socket);

            // Handle when the connection is open
            this.socket.onopen = () => {
                console.log('Connected to server');
                // Join the room after connecting
                this.socket.send(`join_room:${this.$route.params.id}`);
            };

            // Handle messages received from the server
            this.socket.onmessage = (event) => {
                // const message = JSON.parse(event.data);
                // if (message.type === 'new_message') {
                //     this.messages.push(message);
                // }
                console.log(event.data);
            };

            // Handle when the connection is closed
            this.socket.onclose = () => {
                console.log('Disconnected from server');
            };
        },

        async fetchChatRoom() {
            try {
                this.chatRoom = await ChatService.fetchRoom(this.$route.params.id);
                console.log(this.chatRoom);
            } catch (error) {
                console.log(error);
            }
        },

        sendMessage() {
            if (this.messageInput.trim()) {
                const message = {
                    type: 'new_message',
                    roomId: this.chatRoom.id,
                    username: 'John Doe', // Replace with actual user
                    message: this.messageInput,
                    timestamp: new Date().toLocaleTimeString()
                };

                // Send the message to the server
                this.socket.send(JSON.stringify(message));

                // Clear the input
                this.messageInput = '';
            }
        },

        openAddUserModal() {
            const modalElement = document.getElementById('addUserModal');
            const modal = new bootstrap.Modal(modalElement);
            modal.show();
        },

        async addUser() {
            const usernameInput = document.querySelector('#addUserModal input[type="text"]');
            const username = usernameInput.value.trim();
            const roomId = this.chatRoom.id;
            console.log(username);
            console.log(roomId);

            if (username) {
                console.log(`Attempting to add user: ${username} to room ID: ${roomId}`);

                await ChatService.addUserToRoom(roomId, username);

                // Reset input after attempting to add
                usernameInput.value = '';

                // Close modal (assumes Bootstrap modal is used)
                const modalElement = document.getElementById('addUserModal');
                const modalInstance = bootstrap.Modal.getInstance(modalElement);
                modalInstance.hide();
            }
        }

    },
    mounted() {
        this.fetchChatRoom();
        this.connectSocket();
    },
};
</script>

<style scoped>
/* Ensure the messages container always scrolls to the bottom */
.overflow-auto {
    scroll-behavior: smooth;
}
</style>
