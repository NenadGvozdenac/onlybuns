<template>
    <Navbar />
    <ReadOnlyMapComponent />
</template>

<script>
import ReadOnlyMapComponent from '@/components/Layout/ReadOnlyMapComponent.vue';
import Navbar from './Navbar.vue';

export default {
    name: 'NearYou',
    components: {
        ReadOnlyMapComponent,
        Navbar
    }
}
</script>

<style>
</style>