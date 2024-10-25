<template>
    <div class="list-group-item d-flex align-items-center">
        <img :src="person.image" alt="" width="50px" class="rounded-sm ml-n2" />
        <div class="flex-fill pl-3 pr-3 mx-3">
            <div><a :href="person.link" class="text-dark font-weight-600">{{ person.name }}</a></div>
            <div class="text-muted fs-13px">{{ person.location }}</div>
        </div>
        <a :href="person.link" class="btn btn-outline-primary">Follow</a>
    </div>
</template>

<script>
export default {
    name: 'PersonComponent',
    props: {
        person: {
            type: Object,
            required: true,
        },
    },
};
</script>

<style scoped>
/* Add styles specific to the PersonCard component here */
</style>