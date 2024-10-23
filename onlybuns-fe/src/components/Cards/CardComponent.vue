<template>
    <div class="card mb-3">
        <img :src="image" class="card-img-top" alt="Card image" />
        <div class="card-body">
            <div class="d-flex justify-content-between">
                <div>
                    <button class="btn btn-primary">Button</button>
                    <button class="btn btn-secondary">Comment</button>
                </div>
                <div class="people-space">
                    <span class="badge bg-info">{{ peopleCount }} people</span>
                </div>
            </div>
        </div>
    </div>
</template>

<script>
export default {
    name: 'CardComponent',
    props: {
        image: {
            type: String,
            required: true
        },
        peopleCount: {
            type: Number,
            default: 0
        }
    }
}
</script>

<style scoped>
.card {
    width: 100%;
    /* Ensures the card takes the full width of its container */
}

.people-space {
    align-self: center;
    /* Centers the people badge vertically */
}
</style>