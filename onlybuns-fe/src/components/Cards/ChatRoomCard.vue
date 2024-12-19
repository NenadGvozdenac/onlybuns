<template>
    <div class="d-flex justify-content-center align-items-center vh-75 bg-light my-3">
        <div class="container card shadow-lg rounded-lg hover-scale border-0" @click="cardClick"
            style="cursor: pointer;">
            <div class="card-body d-flex align-items-center p-4">
                <div class="me-4">
                    <svg xmlns="http://www.w3.org/2000/svg" width="64" height="64" viewBox="0 0 24 24" fill="none"
                        stroke="currentColor" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round"
                        class="text-primary">
                        <path d="M8 13h4" />
                        <path d="M8 9h6" />
                        <path d="M16 13h2" />
                        <path d="M11 19H6a3 3 0 0 1 -3 -3v-8a3 3 0 0 1 3 -3h12a3 3 0 0 1 3 3v4.5" />
                        <path d="M19 22v-6" />
                        <path d="M22 19l-3 -3l-3 3" />
                    </svg>
                </div>
                <div>
                    <h5 class="card-title mb-2 text-dark fw-bold">{{ name }}</h5>
                    <p class="card-text text-muted small">Chat Admin @{{ admin }}</p>
                </div>
            </div>
        </div>
    </div>
</template>

<script>

export default {
    name: 'ChatRoomCard',
    props: {
        id: {
            type: Number,
            required: true
        },
        name: {
            type: String,
            default: ''
        },
        admin: {
            type: String,
            default: ''
        }
    },
    data() {
        return {

        };
    },
    created() {

    },
    methods: {
        cardClick() {
            this.$router.push('/chat/' + this.id);
        }
    }
}
</script>

<style scoped>
.hover-scale {
    transition: transform 0.5s, box-shadow 0.5s;
}

.hover-scale:hover {
    transform: scale(1.05);
    box-shadow: 0 8px 16px rgba(0, 0, 0, 0.2);
}

.icon-button {
    background: transparent;
    border: none;
    cursor: pointer;
    transition: transform 0.2s, color 0.2s;
}

.icon-button:hover {
    transform: scale(1.1);
    color: #007bff;
    /* hover color */
}

.overflow-y-auto::-webkit-scrollbar {
    width: 8px;
}

.overflow-y-auto::-webkit-scrollbar-track {
    background: #f1f1f1;
}

.overflow-y-auto::-webkit-scrollbar-thumb {
    background: #ddd;
    border-radius: 4px;
}

.overflow-y-auto::-webkit-scrollbar-thumb:hover {
    background: #ccc;
}

.hover-bg-light:hover {
    background-color: rgba(0, 0, 0, 0.02);
}

a {
    color: black;
    text-decoration: none;
}

a:hover {
    color: blue;
}
</style>