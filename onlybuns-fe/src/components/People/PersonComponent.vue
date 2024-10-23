<template>
    <div class="person-card" @click="redirectToPerson">
        <div class="person-info">
            <i class="fas fa-user-circle profile-icon"></i> <!-- Profile Icon -->
            <div class="text-info">
                <span class="person-name">{{ name }}</span>
                <span class="person-surname">{{ surname }}</span>
            </div>
        </div>
    </div>
</template>

<script>
export default {
    name: 'PersonComponent',
    props: {
        username: {
            type: String,
            required: true
        },
        name: {
            type: String,
            required: true
        },
        surname: {
            type: String,
            required: true
        }
    },
    methods: {
        redirectToPerson() {
            this.$router.push({ name: 'PersonDetails', params: { username: this.username } });
        }
    }
}
</script>

<style scoped>
.person-card {
    background-color: #add8e6; /* Light blue background */
    padding: 15px;
    border-radius: 5px;
    margin-bottom: 15px;
    cursor: pointer;
    transition: background-color 0.3s;
}

.person-card:hover {
    background-color: #87ceeb; /* Darker blue on hover */
}

.person-info {
    display: flex; /* Use flexbox to align items in a row */
    align-items: center; /* Center items vertically */
}

.profile-icon {
    font-size: 24px; /* Adjust the size of the icon */
    margin-right: 10px; /* Space between the icon and text */
}

.text-info {
    display: flex;
    flex-direction: column; /* Stack text elements vertically */
}

.person-name,
.person-surname,
.person-username {
    margin: 0 5px; /* Space between text elements */
    font-size: 14px; /* Adjust font size as needed */
    font-weight: bold; /* Make names bold */
}
</style>
