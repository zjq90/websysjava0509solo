import { createRouter, createWebHistory } from 'vue-router'

const routes = [
  {
    path: '/',
    redirect: '/home'
  },
  {
    path: '/home',
    name: 'Home',
    component: () => import('@/views/Home.vue')
  },
  {
    path: '/health',
    name: 'Health',
    component: () => import('@/views/Health.vue')
  },
  {
    path: '/health/deworming',
    name: 'Deworming',
    component: () => import('@/views/DewormingHistory.vue')
  },
  {
    path: '/diet',
    name: 'Diet',
    component: () => import('@/views/Diet.vue')
  },
  {
    path: '/exercise',
    name: 'Exercise',
    component: () => import('@/views/Exercise.vue')
  },
  {
    path: '/circle',
    name: 'Circle',
    component: () => import('@/views/Circle.vue')
  },
  {
    path: '/circle/post/:id',
    name: 'PostDetail',
    component: () => import('@/views/PostDetail.vue')
  },
  {
    path: '/hospital',
    name: 'Hospital',
    component: () => import('@/views/Hospital.vue')
  },
  {
    path: '/hospital/:id',
    name: 'HospitalDetail',
    component: () => import('@/views/HospitalDetail.vue')
  },
  {
    path: '/mine',
    name: 'Mine',
    component: () => import('@/views/Mine.vue')
  },
  {
    path: '/elder-mode',
    name: 'ElderMode',
    component: () => import('@/views/ElderMode.vue')
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

export default router