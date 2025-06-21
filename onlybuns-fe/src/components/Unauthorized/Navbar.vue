<template>
  <!-- Bootstrap Version -->
  <nav class="navbar navbar-expand-md navbar-light bg-light border-bottom">
    <div class="container-fluid">
      <!-- Brand -->
      <a class="navbar-brand d-flex align-items-center" href="/">
        <img src="https://flowbite.com/docs/images/logo.svg" alt="OnlyBuns Logo" class="h-8 me-2">
        <span class="h5 mb-0">OnlyBuns</span>
      </a>

      <!-- Toggler for mobile view -->
      <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarSupportedContent"
        aria-controls="navbarSupportedContent" aria-expanded="false" aria-label="Toggle navigation">
        <span class="navbar-toggler-icon"></span>
      </button>

      <!-- Navbar links and dropdown -->
      <div class="collapse navbar-collapse" id="navbarSupportedContent">
        <ul class="navbar-nav ms-auto mb-2 mb-md-0">
          <!-- Home link -->
          <li class="nav-item">
            <a class="nav-link active" aria-current="page" href="#">Home</a>
          </li>

          <!-- Login button -->
          <li v-if="userNotLoggedIn()" class="nav-item">
            <a class="btn btn-outline-primary ms-md-3" href="/login">Login</a>
          </li>

          <!-- Spam button -->
          <!-- <li class="nav-item">
            <button class="btn btn-warning ms-md-3" @click="sendSpam">Send Spam</button>
          </li> -->
        </ul>
      </div>
    </div>
  </nav>
</template>

<script>
import AuthService from '@/services/AuthService';
import EmailService from '@/services/EmailService';
export default {
  name: 'NavbarUnauthorized',
  methods: {
    userNotLoggedIn() {
      return !localStorage.getItem('token');
    },
    logout() {
      AuthService.logout();
      // Refresh page
      location.reload();
    },
    sendSpam() {
      // Placeholder for spam function
      EmailService.sendSpam()
        .then(response => {
          console.log('Spam sent successfully:', response);
        })
        .catch(error => {
          console.error('Error sending spam:', error);
        });
    }
  }
}
</script>

<style>
.navbar .nav-link:hover {
  color: #15b4d7 !important;
  /* Lighter blue hover */
}
</style>