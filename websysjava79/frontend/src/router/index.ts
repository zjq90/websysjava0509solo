import { createRouter, createWebHistory } from 'vue-router'
import { useUserStore } from '../stores/user'

const routes = [
  {
    path: '/login',
    name: 'Login',
    component: () => import('../views/Login.vue'),
    meta: { requiresAuth: false }
  },
  {
    path: '/register',
    name: 'Register',
    component: () => import('../views/Register.vue'),
    meta: { requiresAuth: false }
  },
  {
    path: '/',
    name: 'Home',
    component: () => import('../views/Home.vue'),
    meta: { requiresAuth: true }
  },
  {
    path: '/user/:id',
    name: 'UserProfile',
    component: () => import('../views/UserProfile.vue'),
    meta: { requiresAuth: true }
  },
  {
    path: '/music/:id',
    name: 'MusicDetail',
    component: () => import('../views/MusicDetail.vue'),
    meta: { requiresAuth: true }
  },
  {
    path: '/playlist/:id',
    name: 'PlaylistDetail',
    component: () => import('../views/PlaylistDetail.vue'),
    meta: { requiresAuth: true }
  },
  {
    path: '/search',
    name: 'Search',
    component: () => import('../views/Search.vue'),
    meta: { requiresAuth: true }
  },
  {
    path: '/upload',
    name: 'Upload',
    component: () => import('../views/Upload.vue'),
    meta: { requiresAuth: true },
    beforeEnter: (to, from, next) => {
      const userStore = useUserStore()
      if (userStore.user?.role === 'ARTIST' || userStore.user?.role === 'ADMIN') {
        next()
      } else {
        next('/')
      }
    }
  },
  {
    path: '/artist',
    name: 'Artist',
    component: () => import('../views/ArtistDashboard.vue'),
    meta: { requiresAuth: true },
    beforeEnter: (to, from, next) => {
      const userStore = useUserStore()
      if (userStore.user?.role === 'ARTIST' || userStore.user?.role === 'ADMIN') {
        next()
      } else {
        next('/')
      }
    }
  },
  {
    path: '/admin',
    name: 'Admin',
    component: () => import('../views/AdminDashboard.vue'),
    meta: { requiresAuth: true },
    beforeEnter: (to, from, next) => {
      const userStore = useUserStore()
      if (userStore.user?.role === 'ADMIN') {
        next()
      } else {
        next('/')
      }
    }
  },
  {
    path: '/my-playlists',
    name: 'MyPlaylists',
    component: () => import('../views/MyPlaylists.vue'),
    meta: { requiresAuth: true }
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

router.beforeEach((to, from, next) => {
  const userStore = useUserStore()
  if (to.meta.requiresAuth && !userStore.isLoggedIn) {
    next('/login')
  } else if (to.path === '/login' && userStore.isLoggedIn) {
    next('/')
  } else {
    next()
  }
})

export default router
