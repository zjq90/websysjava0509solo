import { createRouter, createWebHistory } from 'vue-router'

const routes = [
  { path: '/', name: 'Home', component: () => import('../views/Home.vue') },
  { path: '/discover', name: 'Discover', component: () => import('../views/Discover.vue') },
  { path: '/upload', name: 'Upload', component: () => import('../views/Upload.vue') },
  { path: '/category', name: 'Category', component: () => import('../views/Category.vue') },
  { path: '/search', name: 'Search', component: () => import('../views/Search.vue') },
  { path: '/copyright', name: 'Copyright', component: () => import('../views/Copyright.vue') },
  { path: '/music/:id', name: 'MusicDetail', component: () => import('../views/MusicDetail.vue') },
  { path: '/login', name: 'Login', component: () => import('../views/Login.vue') },
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

export default router
