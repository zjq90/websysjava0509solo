import { createRouter, createWebHistory } from 'vue-router'

const routes = [
  {
    path: '/',
    redirect: '/dashboard'
  },
  {
    path: '/dashboard',
    name: 'Dashboard',
    component: () => import('@/views/Dashboard.vue'),
    meta: { title: '数据概览', icon: 'DataAnalysis' }
  },
  {
    path: '/transactions',
    name: 'Transaction',
    component: () => import('@/views/Transaction.vue'),
    meta: { title: '收支录入', icon: 'Money' }
  },
  {
    path: '/accounts',
    name: 'Account',
    component: () => import('@/views/Account.vue'),
    meta: { title: '账户管理', icon: 'Wallet' }
  },
  {
    path: '/categories',
    name: 'Category',
    component: () => import('@/views/Category.vue'),
    meta: { title: '分类管理', icon: 'Menu' }
  },
  {
    path: '/tags',
    name: 'Tag',
    component: () => import('@/views/Tag.vue'),
    meta: { title: '账单标签', icon: 'PriceTag' }
  },
  {
    path: '/transfers',
    name: 'Transfer',
    component: () => import('@/views/Transfer.vue'),
    meta: { title: '转账记录', icon: 'Swap' }
  },
  {
    path: '/category-rules',
    name: 'CategoryRule',
    component: () => import('@/views/CategoryRule.vue'),
    meta: { title: '分类规则', icon: 'Setting' }
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

export default router
