import { createRouter, createWebHistory } from 'vue-router'

const routes = [
  {
    path: '/',
    redirect: '/dashboard'
  },
  {
    path: '/dashboard',
    name: 'Dashboard',
    component: () => import('@/views/Dashboard.vue')
  },
  {
    path: '/exception-orders',
    name: 'ExceptionOrders',
    component: () => import('@/views/ExceptionOrders.vue')
  },
  {
    path: '/reconciliations',
    name: 'Reconciliations',
    component: () => import('@/views/Reconciliations.vue')
  },
  {
    path: '/invoices',
    name: 'Invoices',
    component: () => import('@/views/Invoices.vue')
  },
  {
    path: '/sales-report',
    name: 'SalesReport',
    component: () => import('@/views/SalesReport.vue')
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

export default router
