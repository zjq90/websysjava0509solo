import { createRouter, createWebHistory } from 'vue-router'

const routes = [
  {
    path: '/login',
    name: 'Login',
    component: () => import('../views/Login.vue')
  },
  {
    path: '/',
    redirect: '/dashboard'
  },
  {
    path: '/dashboard',
    name: 'Dashboard',
    component: () => import('../views/Layout.vue'),
    children: [
      {
        path: '',
        name: 'DashboardHome',
        component: () => import('../views/Dashboard.vue')
      },
      {
        path: 'referee',
        name: 'RefereeManage',
        component: () => import('../views/RefereeManage.vue')
      },
      {
        path: 'athlete',
        name: 'AthleteManage',
        component: () => import('../views/AthleteManage.vue')
      },
      {
        path: 'competition',
        name: 'CompetitionManage',
        component: () => import('../views/CompetitionManage.vue')
      },
      {
        path: 'score',
        name: 'ScoreManage',
        component: () => import('../views/ScoreManage.vue')
      },
      {
        path: 'my-scores',
        name: 'MyScores',
        component: () => import('../views/MyScores.vue')
      },
      {
        path: 'audit',
        name: 'ScoreAudit',
        component: () => import('../views/ScoreAudit.vue')
      },
      {
        path: 'user',
        name: 'UserManage',
        component: () => import('../views/UserManage.vue')
      }
    ]
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

router.beforeEach((to, from, next) => {
  const token = localStorage.getItem('user')
  if (to.path !== '/login' && !token) {
    next('/login')
  } else if (to.path === '/login' && token) {
    next('/dashboard')
  } else {
    next()
  }
})

export default router
