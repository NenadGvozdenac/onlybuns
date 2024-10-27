<template>
    <div class="container min-vh-100 d-flex align-items-center justify-content-center">
        <div class="text-center">
            <!-- Rabbit Logo -->
            <div class="hop-animation mx-auto" :class="{ 'fast-hop': isHopping }" @click="triggerHop">
                <div class="rabbit-ears">
                    <div class="ear left"></div>
                    <div class="ear right"></div>
                </div>
                <div class="rabbit-face">
                    <div class="eye left"></div>
                    <div class="eye right"></div>
                    <div class="nose"></div>
                </div>
            </div>

            <h1 class="display-1 fw-bold text-primary mt-4">{{ errorCode }}</h1>
            <h2 class="h3 mb-4">{{ mainMessage }}</h2>
            <p class="text-muted mb-4">{{ subMessage }}</p>
            <router-link to="/" class="btn btn-primary btn-lg px-4">
                {{ buttonText }}
            </router-link>

            <!-- Decorative grass -->
            <div class="grass mt-5"></div>
        </div>
    </div>
</template>

<script>
export default {
    name: 'NotFound',
    data() {
        return {
            errorCode: '404',
            mainMessage: 'Oops! This page has hopped away',
            subMessage: "The page you're looking for seems to have gone down the rabbit hole!",
            buttonText: 'Hop Back Home',
            isHopping: false
        }
    },
    methods: {
        triggerHop() {
            this.isHopping = true;
            setTimeout(() => {
                this.isHopping = false;
            }, 1000);
        }
    }
}
</script>

<style scoped>
.hop-animation {
    animation: hop 2s infinite;
    cursor: pointer;
}

.fast-hop {
    animation: hop 0.5s;
}

@keyframes hop {

    0%,
    100% {
        transform: translateY(0);
    }

    50% {
        transform: translateY(-20px);
    }
}

.rabbit-ears {
    height: 120px;
    width: 80px;
    position: relative;
}

.ear {
    background: #f8f9fa;
    width: 25px;
    height: 70px;
    border-radius: 50% 50% 0 0;
    position: absolute;
    top: 0;
    transition: transform 0.3s;
}

.ear.left {
    left: 15px;
    transform: rotate(-15deg);
}

.ear.right {
    right: 15px;
    transform: rotate(15deg);
}

.hop-animation:hover .ear.left {
    transform: rotate(-25deg);
}

.hop-animation:hover .ear.right {
    transform: rotate(25deg);
}

.rabbit-face {
    background: #f8f9fa;
    width: 60px;
    height: 60px;
    border-radius: 50%;
    position: relative;
    margin-top: -10px;
}

.eye {
    background: #212529;
    width: 8px;
    height: 8px;
    border-radius: 50%;
    position: absolute;
    top: 20px;
}

.eye.left {
    left: 15px;
}

.eye.right {
    right: 15px;
}

.nose {
    background: pink;
    width: 12px;
    height: 8px;
    border-radius: 50%;
    position: absolute;
    bottom: 20px;
    left: 24px;
}

.grass {
    height: 50px;
    background: linear-gradient(to right, #90EE90 0%, #98FB98 100%);
    border-radius: 50% 50% 0 0;
    margin-top: -25px;
}
</style>