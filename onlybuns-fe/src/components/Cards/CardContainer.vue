<template>
    <div class="container mt-5">
        <div class="row">
            <!-- First Column: 1st, 4th, 7th, 10th, etc. -->
            <div class="col-md-4">
                <div v-for="(card, index) in cards.filter((_, i) => i % 3 === 0)" :key="index">
                    <CardComponent
                        :id="card.id" 
                        :image="card.image.data" 
                        :likesCount="card.numberOfLikes" 
                        :description="card.description" 
                        :commentsCount="card.comments.length" 
                        :username="card.username" 
                        :dateOfCreation="card.dateOfCreation"
                        :usersThatLike="card.users"  
                    />
                </div>
            </div>

            <!-- Second Column: 2nd, 5th, 8th, 11th, etc. -->
            <div class="col-md-4">
                <div v-for="(card, index) in cards.filter((_, i) => i % 3 === 1)" :key="index">
                    <CardComponent
                        :id="card.id"  
                        :image="card.image.data" 
                        :likesCount="card.numberOfLikes" 
                        :description="card.description"
                        :commentsCount="card.comments.length"
                        :username="card.username"
                        :dateOfCreation="card.dateOfCreation"
                        :usersThatLike="card.users"    
                    />
                </div>
            </div>

            <!-- Third Column: 3rd, 6th, 9th, etc. -->
            <div class="col-md-4">
                <div v-for="(card, index) in cards.filter((_, i) => i % 3 === 2)" :key="index">
                    <CardComponent
                        :id="card.id"  
                        :image="card.image.data" 
                        :likesCount="card.numberOfLikes" 
                        :description="card.description"
                        :commentsCount="card.comments.length"
                        :username="card.username"
                        :dateOfCreation="card.dateOfCreation"
                        :usersThatLike="card.users"  
                    />
                </div>
            </div>
        </div>
    </div>
</template>


<script>
import CardService from '@/services/CardService';
import CardComponent from './CardComponent.vue';

export default {
    name: 'CardContainer',
    components: {
        CardComponent
    },
    data() {
        return {
            cards: []
        };
    },
    mounted(){
        this.getPosts();
    },
    methods: {
        async getPosts(){
            
            try{
                this.cards = await CardService.fetchPosts();        
            }catch(error){
                console.error("Failed to load cards: ", error)
            }
        }
    }
}
</script>

<style>
/* Add any specific styles for CardContainer here */
</style>