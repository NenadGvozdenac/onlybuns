<template>
    <div class="container mt-5">
        <div class="row">
            <div class="row">
                <div class="col-12" v-for="(person, index) in people" :key="index">
                    <PersonComponent :username="person.username" :name="person.name" :surname="person.surname" />
                </div>
            </div>
        </div>
    </div>
</template>

<script>
import PersonComponent from './PersonComponent.vue';

export default {
    name: 'PersonContainer',
    components: {
        PersonComponent,
    },
    data() {
        return {
            people: [
                { username: 'username1', name: 'name1', surname: 'surname1' },
                { username: 'username2', name: 'name2', surname: 'surname2' },
                { username: 'username3', name: 'name3', surname: 'surname3' },
                { username: 'username4', name: 'name4', surname: 'surname4' },
                { username: 'username5', name: 'name5', surname: 'surname5' }
            ]
        };
    }
}
</script>

<style>
/* Add any specific styles for CardContainer here */
</style>