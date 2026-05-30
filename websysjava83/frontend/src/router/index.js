import Vue from 'vue'
import VueRouter from 'vue-router'
import Dashboard from '../views/Dashboard.vue'
import GameAnalytics from '../views/GameAnalytics.vue'
import SystemConfig from '../views/SystemConfig.vue'

Vue.use(VueRouter)

const routes = [
  {
    path: '/',
    redirect: '/dashboard'
  },
  {
    path: '/dashboard',
    name: 'Dashboard',
    component: Dashboard
  },
  {
    path: '/game-analytics',
    name: 'GameAnalytics',
    component: GameAnalytics
  },
  {
    path: '/system-config',
    name: 'SystemConfig',
    component: SystemConfig
  }
]

const router = new VueRouter({
  mode: 'history',
  routes
})

export default router
