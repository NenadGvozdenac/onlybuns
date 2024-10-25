<template>
  <!-- Bootstrap Version -->
  <nav class="navbar navbar-expand-md navbar-light bg-light border-bottom">
    <div class="container-fluid">
      <!-- Brand -->
      <a class="navbar-brand d-flex align-items-center" href="#">
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

          <!-- Dropdown menu -->
          <li class="nav-item dropdown">
            <a class="nav-link dropdown-toggle" href="#" id="navbarDropdown" role="button" data-bs-toggle="dropdown"
              aria-expanded="false">
              Menu
            </a>
            <ul class="dropdown-menu" aria-labelledby="navbarDropdown">
              <li><a class="dropdown-item" href="#">My Profile</a></li>
              <li>
                <hr class="dropdown-divider">
              </li>
              <li><a class="dropdown-item" href="#">Create Post</a></li>
              <li><a class="dropdown-item" href="#">View All Posts</a></li>
            </ul>
          </li>

          <!-- Home link -->
          <li class="nav-item">
            <a class="nav-link active" aria-current="page" href="#">Home</a>
          </li>

          <!-- Home link -->
          <li v-if="!userNotLoggedIn()" class="nav-item">
            <a class="nav-link active btn btn-danger text-white" aria-current="page" @click="logout()">LOGOUT</a>
          </li>

          <!-- Login button -->
          <li v-if="userNotLoggedIn()" class="nav-item">
            <a class="btn btn-outline-primary ms-md-3" href="/login">Login</a>
          </li>
        </ul>
      </div>
    </div>
  </nav>
</template>

<script>
import AuthService from '@/services/AuthService';
export default {
  name: 'Navbar',
  methods: {
    userNotLoggedIn() {
      return !localStorage.getItem('token');
    },
    logout() {
      AuthService.logout();
      // Refresh page
      location.reload();
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