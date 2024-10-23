<template>
    <div class="container mt-5">
        <div class="row">
            <div class="row">
                <div class="col-6" v-for="(card, index) in cards" :key="index">
                    <CardComponent :image="card.image" :peopleCount="card.peopleCount" />
                </div>
            </div>
        </div>
    </div>
</template>

<script>
import CardComponent from './CardComponent.vue';

export default {
    name: 'CardContainer',
    components: {
        CardComponent
    },
    data() {
        return {
            cards: [
                { image: 'https://via.placeholder.com/150', peopleCount: 5 },
                { image: 'https://via.placeholder.com/150', peopleCount: 3 },
                { image: 'https://via.placeholder.com/150', peopleCount: 8 },
                { image: 'https://via.placeholder.com/150', peopleCount: 2 }
            ]
        };
    }
}
</script>

<style>
/* Add any specific styles for CardContainer here */
</style>