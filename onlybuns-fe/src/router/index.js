import { createRouter, createWebHistory } from 'vue-router'
import HomeView from '../views/Home.vue'
import LoginView from '../views/Login.vue'
import RegisterView from '../views/Register.vue'
import EmailVerifyView from '../views/EmailVerified.vue'
import MyProfile from '@/components/Authorized/MyProfile.vue'
import UserProfile from '@/views/UserProfile.vue'
import Trends from '@/views/Trends.vue'
import NotFound from '@/views/NotFound.vue'
import RegisteredProfiles from '@/components/Authorized/RegisteredProfiles.vue'
import NearYou from '@/components/Authorized/NearYou.vue'
import CreatePost from '@/components/Authorized/CreatePost.vue'
import FollowingFeed from '@/views/FollowingFeed.vue'
import ChatRooms from '@/views/ChatRooms.vue'
import Chat from '@/views/Chat.vue'
import AppAnalytics from '@/views/AppAnalytics.vue'

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
      path: '/createpost',
      name: 'createpost',
      component: CreatePost
    },
    {
      path: '/profile',
      name: 'profile',
      component: MyProfile
    },
    {
      path: '/nearyou',
      name: 'nearyou',
      component: NearYou
    },
    {
      path: '/user-profile',
      name: 'user-profile',
      component: UserProfile
    },
    {
      path: '/trends',
      name: 'trends',
      component: Trends
    },
    {
      path: '/not-found',
      name: 'not-found',
      component: NotFound
    },
    {
      path: '/registeredProfiles',
      name: 'registeredProfiles',
      component: RegisteredProfiles
    },
    {
      path: '/followingFeed',
      name: 'followingFeed',
      component: FollowingFeed
    },
    {
      path: '/chatsRooms',
      name: 'chatsRooms',
      component: ChatRooms
    },
    {
      path: '/chat/:id',
      name: 'chat',
      component: Chat
    },
    {
      path: '/analytics',
      name: 'AppAnalytics',
      component: AppAnalytics
    },
  ]
})

export default router
