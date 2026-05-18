import { createRouter, createWebHashHistory } from 'vue-router'

const routes = [
  {
    path: '/',
    name: 'Index',
    component: () => import('../pages/index/index.vue')
  },
  {
    path: '/login',
    name: 'Login',
    component: () => import('../pages/login/login.vue')
  },
  {
    path: '/register',
    name: 'Register',
    component: () => import('../pages/register/register.vue')
  },
  {
    path: '/counselors',
    name: 'Counselors',
    component: () => import('../pages/counselors/list.vue')
  },
  {
    path: '/emergency',
    name: 'Emergency',
    component: () => import('../pages/emergency/emergency.vue')
  },
  {
    path: '/profile',
    name: 'Profile',
    component: () => import('../pages/user/profile.vue')
  }
]

const router = createRouter({
  history: createWebHashHistory(),
  routes
})

export default router
