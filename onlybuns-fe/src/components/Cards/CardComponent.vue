<template>
    <!-- Bootstrap Version -->
    <div class="d-flex justify-content-center align-items-center vh-75 bg-light my-3">
        <div class="container card shadow-lg rounded-lg hover-scale">
            <div class="card-body">
                <span class="badge bg-success text-white mb-3">Bunny</span>
                <h1 class="h4 font-weight-bold text-dark">Lampara Look</h1>
                <p class="text-muted small">#by Saca Tuerca</p>
            </div>

            <img class="card-img-top"
                src="https://images.unsplash.com/photo-1525268771113-32d9e9021a97?ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&ixlib=rb-1.2.1&auto=format&fit=crop&w=800&q=80"
                alt="Lampara Look Image" />

            <div class="d-flex justify-content-between align-items-center p-3">
                <!-- Author Info -->
                <div class="d-flex align-items-center">
                    <img class="rounded-circle me-2"
                        src="https://d2qp0siotla746.cloudfront.net/img/use-cases/profile-picture/template_3.jpg"
                        alt="sara" width="40" />
                    <h2 class="h6 mb-0"># Felipe Sacudon</h2>
                </div>

                <!-- Interaction Icons -->
                <div class="d-flex">
                    <!-- Comments -->
                    <div class="d-flex align-items-center me-3">
                        <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" fill="none" viewBox="0 0 24 24"
                            stroke="currentColor" class="me-1 text-secondary">
                            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                                d="M8 12h.01M12 12h.01M16 12h.01M21 12c0 4.418-4.03 8-9 8a9.863 9.863 0 01-4.255-.949L3 20l1.395-3.72C3.512 15.042 3 13.574 3 12c0-4.418 4.03-8 9-8s9 3.582 9 8z" />
                        </svg>
                        <span>22</span>
                    </div>

                    <!-- Likes -->
                    <div class="d-flex align-items-center">
                        <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" fill="currentColor"
                            class="text-danger me-1" viewBox="0 0 20 20">
                            <path fill-rule="evenodd"
                                d="M3.172 5.172a4 4 0 015.656 0L10 6.343l1.172-1.171a4 4 0 115.656 5.656L10 17.657l-6.828-6.829a4 4 0 010-5.656z"
                                clip-rule="evenodd" />
                        </svg>
                        <span>20</span>
                    </div>
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
  .hover-scale {
    transition: transform 0.5s, box-shadow 0.5s;
  }
  .hover-scale:hover {
    transform: scale(1.05);
    box-shadow: 0 8px 16px rgba(0, 0, 0, 0.2);
  }
</style>