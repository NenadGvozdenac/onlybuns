<template>
    <div v-if="show" class="modal-overlay" @click="close">
        <div class="modal-content" @click.stop>
            <h3>{{ title }}</h3>
            <p>{{ message }}</p>
            <button @click="close">Close</button>
        </div>
    </div>
</template>

<script>
export default {
    name: 'Modal',
    props: {
        show: {
            type: Boolean,
            required: true
        },
        title: {
            type: String,
            default: 'Modal Title'
        },
        message: {
            type: String,
            default: 'This is the modal message.'
        }
    },
    methods: {
        close() {
            this.$emit('close'); // Emit an event to inform the parent component to close the modal
        }
    }
}
</script>

<style scoped>
.modal-overlay {
    position: fixed;
    top: 0;
    left: 0;
    right: 0;
    bottom: 0;
    background: rgba(0, 0, 0, 0.5);
    display: flex;
    justify-content: center;
    align-items: center;
    z-index: 1000; /* Ensure the modal is on top */
}

.modal-content {
    background: white;
    padding: 20px;
    border-radius: 10px;
    text-align: center;
    box-shadow: 0 0 10px rgba(0, 0, 0, 0.5);
    margin: 50vh;
}

.modal-content h3 {
    margin-bottom: 15px;
}

.modal-content button {
    margin-top: 20px;
    padding: 10px 20px;
    border: none;
    background-color: #03A9F4; /* Change to your preferred color */
    color: white;
    border-radius: 5px;
    cursor: pointer;
}

.modal-content button:hover {
    background-color: #039BE5; /* Change to your preferred hover color */
}
</style>
