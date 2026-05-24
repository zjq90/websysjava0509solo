import { createRouter, createWebHistory } from 'vue-router'
import Layout from '../views/Layout.vue'

const routes = [
  {
    path: '/',
    component: Layout,
    redirect: '/dashboard',
    children: [
      {
        path: 'dashboard',
        name: 'Dashboard',
        component: () => import('../views/Dashboard.vue'),
        meta: { title: '实时监控大屏', icon: 'Monitor' }
      },
      {
        path: 'records',
        name: 'Records',
        component: () => import('../views/Records.vue'),
        meta: { title: '历史记录查询', icon: 'Document' }
      },
      {
        path: 'blacklist',
        name: 'Blacklist',
        component: () => import('../views/Blacklist.vue'),
        meta: { title: '黑名单管理', icon: 'Warning' }
      },
      {
        path: 'cameras',
        name: 'Cameras',
        component: () => import('../views/Cameras.vue'),
        meta: { title: '摄像头管理', icon: 'VideoCamera' }
      }
    ]
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

export default router
