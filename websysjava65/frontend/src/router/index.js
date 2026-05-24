import { createRouter, createWebHistory } from 'vue-router'

const routes = [
  {
    path: '/',
    redirect: '/dashboard'
  },
  {
    path: '/dashboard',
    name: 'Dashboard',
    component: () => import('../views/Dashboard.vue')
  },
  {
    path: '/user/blacklist',
    name: 'Blacklist',
    component: () => import('../views/user/Blacklist.vue')
  },
  {
    path: '/user/tickets',
    name: 'Tickets',
    component: () => import('../views/user/Tickets.vue')
  },
  {
    path: '/user/membership',
    name: 'Membership',
    component: () => import('../views/user/Membership.vue')
  },
  {
    path: '/finance/reconciliation',
    name: 'Reconciliation',
    component: () => import('../views/finance/Reconciliation.vue')
  },
  {
    path: '/finance/invoices',
    name: 'Invoices',
    component: () => import('../views/finance/Invoices.vue')
  },
  {
    path: '/finance/cost',
    name: 'Cost',
    component: () => import('../views/finance/Cost.vue')
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

export default router
