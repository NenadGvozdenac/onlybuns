import { createRouter, createWebHistory } from 'vue-router'
import HomeView from '../views/Home.vue'
import LoginView from '../views/Login.vue'
import RegisterView from '../views/Register.vue'
import EmailVerifyView from '../views/EmailVerified.vue'
import MyProfile from '@/components/Authorized/MyProfile.vue'
import Trends from '@/views/Trends.vue'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'home',
      component: HomeView
    },
    {
      path: '/login',
      name: 'login',
      component: LoginView
    },
    {
      path: '/register',
      name: 'register',
      component: RegisterView
    },
    {
      path: '/email-authentications/:token',
      name: 'email-verify',
      component: EmailVerifyView
    },
    {
      path: '/profile',
      name: 'profile',
      component: MyProfile
    },
    {
      path: '/trends',
      name: 'trends',
      component: Trends
    }
  ]
})

export default router
