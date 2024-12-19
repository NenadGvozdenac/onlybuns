<template>
    <div class="container-fluid vh-100 d-flex flex-column">
        <NavbarAuthorized v-if="isLoggedIn()" />
        <NavbarUnauthorized v-else />

        <!-- Notifications Container -->
        <div class="notifications-container">
            <transition-group name="notification">
                <div v-for="(notification, index) in activeNotifications" :key="notification.id"
                    class="notification alert alert-info d-inline-block py-2 px-3 rounded-pill shadow-sm"
                    :style="{ top: `${index * 60 + 20}px` }">
                    <i class="bi bi-info-circle me-2"></i>
                    {{ notification.message }}
                </div>
            </transition-group>
        </div>

        <!-- Enhanced Chat Room Header -->
        <div class="row bg-gradient-primary text-white p-3 align-items-center shadow-sm">
            <div class="col d-flex align-items-center">
                <i class="bi bi-chat-dots-fill me-2 fs-4"></i>
                <div>
                    <h3 class="mb-0 fw-bold">{{ chatRoom.name }}</h3>
                    <small class="opacity-75">
                        <i class="bi bi-person-circle me-1"></i>
                        Admin: {{ chatRoom.admin }}
                    </small>
                </div>
            </div>
            <div class="col-auto">
                <button v-if="chatRoom.admin === myUsername"
                    class="btn btn-light mb-2 btn-lg shadow-sm d-flex align-items-center" @click="openAddUserModal">
                    <i class="bi bi-person-plus-fill me-2"></i> Add User
                </button>
                <button v-if="chatRoom.admin === myUsername"
                    class="btn btn-danger btn-lg shadow-sm d-flex align-items-center" @click="openRemoveUserModal">
                    <i class="bi bi-person-dash-fill me-2"></i> Remove User
                </button>
            </div>
        </div>

        <!-- Chat Messages Area -->
        <div class="row flex-grow-1 overflow-hidden position-relative">
            <div class="col p-0 d-flex flex-column position-absolute top-0 bottom-0 start-0 end-0">
                <div ref="messagesContainer" class="flex-grow-1 overflow-auto p-3 chat-bg"
                    style="background: linear-gradient(to bottom, #f8f9fa, #e9ecef);">
                    <div class="d-flex flex-column-reverse">
                        <!-- Only Chat Messages -->
                        <div class="mb-3" v-for="(msg, index) in messages" :key="index">
                            <div
                                :class="['d-flex', msg.username === myUsername ? 'justify-content-end' : 'justify-content-start']">
                                <div
                                    :class="['message-bubble', msg.username === myUsername ? 'message-mine' : 'message-other']">
                                    <div class="message-header d-flex justify-content-between align-items-center mb-1">
                                        <strong :class="msg.username === myUsername ? 'text-primary' : 'text-success'">
                                            {{ msg.username }}
                                        </strong>
                                        <small class="text-muted ms-2">
                                            <i class="bi bi-clock me-1"></i>
                                            {{ msg.timestamp }}
                                        </small>
                                    </div>
                                    <p class="mb-0 message-text">{{ msg.message }}</p>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div ref="scrollAnchor"></div>
                </div>

                <!-- Enhanced Message Input -->
                <div class="bg-white border-top p-3 shadow-lg">
                    <div class="input-group input-group-lg">
                        <input type="text" class="form-control border-2 shadow-none" placeholder="Type your message..."
                            v-model="messageInput" @keyup.enter="sendMessage">
                        <button class="btn btn-primary px-4 shadow-none" @click="sendMessage"
                            :disabled="!messageInput.trim()">
                            <i class="bi bi-send-fill"></i>
                        </button>
                    </div>
                </div>
            </div>
        </div>

        <!-- Enhanced Add User Modal -->
        <div class="modal fade" id="addUserModal" tabindex="-1">
            <div class="modal-dialog modal-dialog-centered">
                <div class="modal-content border-0 shadow-lg">
                    <div class="modal-header bg-primary text-white">
                        <h5 class="modal-title">
                            <i class="bi bi-person-plus-fill me-2"></i>
                            Add User to Chat
                        </h5>
                        <button type="button" class="btn-close btn-close-white" data-bs-dismiss="modal"></button>
                    </div>
                    <div class="modal-body p-4">
                        <div class="form-floating">
                            <input type="text" class="form-control" id="usernameInput" placeholder="Enter username">
                            <label for="usernameInput">Username</label>
                        </div>
                    </div>
                    <div class="modal-footer bg-light">
                        <button type="button" class="btn btn-light" data-bs-dismiss="modal">Cancel</button>
                        <button type="button" class="btn btn-primary px-4" @click="addUser">
                            <i class="bi bi-plus-lg me-2"></i>Add
                        </button>
                    </div>
                </div>
            </div>
        </div>

        <div class="modal fade" id="removeUserModal" tabindex="-1">
            <div class="modal-dialog modal-dialog-centered">
                <div class="modal-content border-0 shadow-lg">
                    <div class="modal-header">
                        <h5 class="modal-title">Remove User from Chat Room</h5>
                        <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                    </div>
                    <div class="modal-body">
                        <div class="modal-body p-4">
                            <div class="form-floating">
                                <input type="text" class="form-control" id="removeUsernameInput"
                                    placeholder="Enter username">
                                <label for="removeUsernameInput">Username</label>
                            </div>
                        </div>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancel</button>
                        <button type="button" class="btn btn-danger" @click="removeUser">Remove User</button>
                    </div>
                </div>
            </div>
        </div>

        <div class="toast-container position-fixed bottom-0 end-0 p-4">
            <div id="successToast" class="toast align-items-center border-0 shadow-lg" role="alert"
                style="background-color: #d4edda; color: #155724;">
                <div class="d-flex">
                    <div class="toast-body d-flex align-items-center">
                        <i class="bi bi-check-circle-fill me-2" style="font-size: 1.2rem;"></i>
                        <span></span>
                    </div>
                    <button type="button" class="btn-close me-2 m-auto" data-bs-dismiss="toast"></button>
                </div>
            </div>

            <div id="errorToast" class="toast align-items-center border-0 shadow-lg" role="alert"
                style="background-color: #f8d7da; color: #721c24;">
                <div class="d-flex">
                    <div class="toast-body d-flex align-items-center">
                        <i class="bi bi-exclamation-circle-fill me-2" style="font-size: 1.2rem;"></i>
                        <span></span>
                    </div>
                    <button type="button" class="btn-close me-2 m-auto" data-bs-dismiss="toast"></button>
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
        let token = localStorage.getItem('token');
        let myUser = '';

        if (token) {
            try {
                myUser = JSON.parse(atob(token.split('.')[1])).sub;
            } catch (error) {
                console.error("Failed to parse token:", error);
                myUser = '';
            }
        }

        return {
            socket: null,
            chatRoom: {},
            messages: [],
            messageInput: '',
            activeNotifications: [], // Replace infoMessages with activeNotifications
            myUsername: myUser,
            isTyping: false,
            notificationId: 0,
            selectedUserToRemove: '',
            removeUserModal: null,
            successToast: null,
            errorToast: null
        };
    },
    methods: {
        isLoggedIn() {
            return localStorage.getItem('token') !== null;
        },

        showNotification(message, duration = 3000) {
            const notification = {
                id: this.notificationId++,
                message: message
            };
            this.activeNotifications.push(notification);

            // Remove notification after duration
            setTimeout(() => {
                this.activeNotifications = this.activeNotifications.filter(n => n.id !== notification.id);
            }, duration);
        },

        connectSocket() {
            this.socket = new WebSocket('ws://localhost:8080/ws');

            this.socket.onopen = () => {
                console.log('Connected to server');
                const joinMessage = JSON.stringify({
                    type: "join_room",
                    roomId: this.$route.params.id,
                    username: this.myUsername,
                });
                this.socket.send(joinMessage);
            };

            this.socket.onmessage = (event) => {
                const message = JSON.parse(event.data);

                if (message.type === 'info') {
                    this.showNotification(message.message);
                } else if (message.type === 'chat') {
                    this.messages.unshift({
                        username: message.sender,
                        message: message.text,
                        timestamp: new Date().toLocaleString(),
                    });
                }
                this.scrollToBottom();
            };

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
                this.$router.push('/');
            }
        },

        async fetchMessages() {
            try {
                this.messages = await ChatService.fetchMessages(this.$route.params.id);
                console.log(this.messages);
            } catch (error) {
                console.error('Error fetching messages:', error);
            }
        },

        sendMessage() {
            if (this.messageInput.trim()) {
                const messagePayload = {
                    type: 'new_message',
                    roomId: this.$route.params.id,
                    username: this.myUsername,
                    text: this.messageInput,
                };

                this.socket.send(JSON.stringify(messagePayload));
                this.messageInput = '';
                this.scrollToBottom();
            }
        },

        openAddUserModal() {
            const modalElement = document.getElementById('addUserModal');
            const modal = new bootstrap.Modal(modalElement);
            modal.show();
        },

        openRemoveUserModal() {
            const modalElement = document.getElementById('removeUserModal');
            const modal = new bootstrap.Modal(modalElement);
            modal.show();
        },

        async addUser() {
            const usernameInput = document.querySelector('#addUserModal input[type="text"]');
            const username = usernameInput.value.trim();
            const roomId = this.chatRoom.id;

            if (username) {
                try {
                    await ChatService.addUserToRoom(roomId, username);
                    usernameInput.value = '';
                    const modalElement = document.getElementById('addUserModal');
                    const modalInstance = bootstrap.Modal.getInstance(modalElement);
                    modalInstance.hide();
                    this.showToast('User successfully added to the chat room!');
                } catch (error) {
                    this.showToast('Failed to add user to the chat room.', false);
                    console.error('Error adding user:', error);
                }
            }
        },

        async removeUser() {
            const removeUsernameInput = document.querySelector('#removeUserModal input[type="text"]');
            const username = removeUsernameInput.value.trim();
            const roomId = this.chatRoom.id;

            if (username) {
                try {
                    console.log('Removing user:', username);
                    await ChatService.removeUserFromRoom(roomId, username);
                    removeUsernameInput.value = '';
                    const modalElement = document.getElementById('removeUserModal');
                    const modalInstance = bootstrap.Modal.getInstance(modalElement);
                    modalInstance.hide();
                    this.showToast('User successfully removed from the chat room!');
                } catch (error) {
                    this.showToast('Failed to remove user from the chat room.', false);
                    console.error('Error removing user:', error);
                }
            }
        },

        showToast(message, isSuccess = true) {
            const toastEl = document.querySelector(isSuccess ? '#successToast' : '#errorToast');
            const toast = new bootstrap.Toast(toastEl);
            toastEl.querySelector('.toast-body').textContent = message;
            toast.show();
        },

        scrollToBottom() {
            this.$nextTick(() => {
                const scrollAnchor = this.$refs.scrollAnchor;
                scrollAnchor?.scrollIntoView({ behavior: 'smooth' });
            });
        },

    },
    mounted() {
        this.fetchChatRoom();
        this.fetchMessages();
        this.connectSocket();
    },
    watch: {
        messages: {
            handler() {
                this.scrollToBottom();
            },
            deep: true
        },
        infoMessages: {
            handler() {
                this.scrollToBottom();
            },
            deep: true
        }
    }
};
</script>

<style scoped>
.bg-gradient-primary {
    background: linear-gradient(135deg, #0d6efd 0%, #0a58ca 100%);
}

.chat-bg {
    background-image:
        radial-gradient(circle at 100% 100%, rgba(13, 110, 253, 0.05) 0%, transparent 50%),
        radial-gradient(circle at 0% 0%, rgba(13, 110, 253, 0.05) 0%, transparent 50%);
}

.message-bubble {
    max-width: 80%;
    padding: 0.75rem 1rem;
    border-radius: 1rem;
    margin-bottom: 0.5rem;
    box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.message-mine {
    background: #e3f2fd;
    border-bottom-right-radius: 0.25rem;
}

.message-other {
    background: white;
    border-bottom-left-radius: 0.25rem;
}

.message-text {
    word-break: break-word;
}

.form-control:focus {
    border-color: #0d6efd;
    box-shadow: 0 0 0 0.25rem rgba(13, 110, 253, 0.25);
}

.btn {
    transition: all 0.2s ease;
}

.modal.fade .modal-dialog {
    transition: transform 0.2s ease-out;
}

.notifications-container {
    position: fixed;
    top: 0;
    right: 20px;
    z-index: 1050;
    pointer-events: none;
}

.notification {
    position: absolute;
    right: 0;
    background-color: rgba(255, 255, 255, 0.95);
    min-width: 200px;
    pointer-events: none;
}

/* Notification animations */
.notification-enter-active,
.notification-leave-active {
    transition: all 0.3s ease;
}

.notification-enter-from {
    opacity: 0;
    transform: translateX(100%);
}

.notification-leave-to {
    opacity: 0;
    transform: translateX(100%);
}

/* Keep your existing styles */
.bg-gradient-primary {
    background: linear-gradient(135deg, #0d6efd 0%, #0a58ca 100%);
}

.chat-bg {
    background-image:
        radial-gradient(circle at 100% 100%, rgba(13, 110, 253, 0.05) 0%, transparent 50%),
        radial-gradient(circle at 0% 0%, rgba(13, 110, 253, 0.05) 0%, transparent 50%);
}


@media (max-width: 768px) {
    .message-bubble {
        max-width: 90%;
    }

    .modal-dialog {
        margin: 0.5rem;
    }
}

/* Animation for new messages */
@keyframes fadeIn {
    from {
        opacity: 0;
        transform: translateY(10px);
    }

    to {
        opacity: 1;
        transform: translateY(0);
    }
}

.message-bubble {
    animation: fadeIn 0.3s ease-out;
}

.toast {
    transition: all 0.3s ease;
    opacity: 0;
}

.toast.show {
    opacity: 1;
}

.toast-container {
    z-index: 1056;
}


</style>