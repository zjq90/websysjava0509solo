import { createRouter, createWebHashHistory } from 'vue-router'

const routes = [
  {
    path: '/login',
    name: 'Login',
    component: () => import('../views/Login.vue')
  },
  {
    path: '/',
    name: 'Layout',
    component: () => import('../views/Layout.vue'),
    redirect: '/tasks',
    children: [
      {
        path: 'tasks',
        name: 'Tasks',
        component: () => import('../views/Tasks.vue')
      },
      {
        path: 'map',
        name: 'Map',
        component: () => import('../views/Map.vue')
      },
      {
        path: 'scan',
        name: 'Scan',
        component: () => import('../views/Scan.vue')
      },
      {
        path: 'battery',
        name: 'Battery',
        component: () => import('../views/Battery.vue')
      },
      {
        path: 'profile',
        name: 'Profile',
        component: () => import('../views/Profile.vue')
      }
    ]
  },
  {
    path: '/task-detail/:id',
    name: 'TaskDetail',
    component: () => import('../views/TaskDetail.vue')
  },
  {
    path: '/bike-detail/:id',
    name: 'BikeDetail',
    component: () => import('../views/BikeDetail.vue')
  },
  {
    path: '/station',
    name: 'Station',
    component: () => import('../views/Station.vue')
  },
  {
    path: '/heatmap',
    name: 'HeatMap',
    component: () => import('../views/HeatMap.vue')
  }
]

const router = createRouter({
  history: createWebHashHistory(),
  routes
})

router.beforeEach((to, from, next) => {
  const token = localStorage.getItem('token')
  if (to.path !== '/login' && !token) {
    next('/login')
  } else {
    next()
  }
})

export default router
