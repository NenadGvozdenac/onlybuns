<template>
    <nav class="navbar navbar-expand-lg navbar-light" style="background-color: #007bff;">
      <div class="container-fluid">
        <a class="navbar-brand text-white" href="#">Onlybuns</a>
        <button
          class="navbar-toggler"
          type="button"
          data-bs-toggle="collapse"
          data-bs-target="#navbarNav"
          aria-controls="navbarNav"
          aria-expanded="false"
          aria-label="Toggle navigation"
        >
          <span class="navbar-toggler-icon"></span>
        </button>
        <div class="collapse navbar-collapse" id="navbarNav">
          <ul class="navbar-nav me-auto mb-2 mb-lg-0">
            <!-- Additional links can be added here if needed -->
          </ul>
          <ul class="navbar-nav ms-auto mb-2 mb-lg-0">
            <li class="nav-item">
              <a class="nav-link text-white" href="/login">Login</a>
            </li>
          </ul>
        </div>
      </div>
    </nav>
  </template>
  
  <script>
  export default {
    name: 'Navbar'
  }
  </script>
  
  <style>
  .navbar .nav-link:hover {
    color: #d1ecf1 !important; /* Lighter blue hover */
  }
  </style>
  