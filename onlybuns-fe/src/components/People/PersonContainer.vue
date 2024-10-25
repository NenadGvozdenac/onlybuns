<template>
    <div class="profile-content">
        <div class="row">
            <div class="col-xl-12">
                <div class="tab-content p-0">
                    <div class="tab-pane fade active show" id="profile-followers">
                        <div class="list-group">
                            <PersonComponent
                                v-for="(person, index) in people"
                                :key="index"
                                :person="person"
                            />
                        </div>
                        <div class="text-center p-3">
                            <a href="#" class="text-dark text-decoration-none">Show more <b class="caret"></b></a>
                        </div>
                    </div>
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
                { name: 'Ethel Wilkes', location: 'North Raundspic', image: 'https://bootdey.com/img/Content/avatar/avatar1.png', link: '#' },
                { name: 'Shanaya Hansen', location: 'North Raundspic', image: 'https://bootdey.com/img/Content/avatar/avatar2.png', link: '#' },
                { name: 'James Allman', location: 'North Raundspic', image: 'https://bootdey.com/img/Content/avatar/avatar3.png', link: '#' },
                { name: 'Marie Welsh', location: 'Crencheporford', image: 'https://bootdey.com/img/Content/avatar/avatar4.png', link: '#' },
                { name: 'Lamar Kirkland', location: 'Prince Ewoodswan', image: 'https://bootdey.com/img/Content/avatar/avatar5.png', link: '#' },
                { name: 'Bentley Osborne', location: 'Red Suvern', image: 'https://bootdey.com/img/Content/avatar/avatar6.png', link: '#' },
                { name: 'Ollie Goulding', location: 'Doa', image: 'https://bootdey.com/img/Content/avatar/avatar7.png', link: '#' },
                { name: 'Hiba Calvert', location: 'Stemunds', image: 'https://bootdey.com/img/Content/avatar/avatar8.png', link: '#' },
                { name: 'Rivka Redfern', location: 'Fallnee', image: 'https://bootdey.com/img/Content/avatar/avatar3.png', link: '#' },
                { name: 'Roshni Fernandez', location: 'Mount Lerdo', image: 'https://bootdey.com/img/Content/avatar/avatar1.png', link: '#' },
            ],
        };
    },
};
</script>

<style>
</style>