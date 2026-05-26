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
    meta: { title: '仪表盘' }
  },
  {
    path: '/debts',
    name: 'Debts',
    component: () => import('@/views/DebtList.vue'),
    meta: { title: '债务管理' }
  },
  {
    path: '/assets',
    name: 'Assets',
    component: () => import('@/views/AssetList.vue'),
    meta: { title: '资产管理' }
  },
  {
    path: '/investments',
    name: 'Investments',
    component: () => import('@/views/InvestmentList.vue'),
    meta: { title: '投资组合' }
  },
  {
    path: '/net-worth',
    name: 'NetWorth',
    component: () => import('@/views/NetWorth.vue'),
    meta: { title: '净资产统计' }
  },
  {
    path: '/families',
    name: 'Families',
    component: () => import('@/views/FamilyList.vue'),
    meta: { title: '家庭管理' }
  },
  {
    path: '/budgets',
    name: 'Budgets',
    component: () => import('@/views/BudgetList.vue'),
    meta: { title: '预算管理' }
  },
  {
    path: '/virtual-accounts',
    name: 'VirtualAccounts',
    component: () => import('@/views/VirtualAccountList.vue'),
    meta: { title: '虚拟账户' }
  },
  {
    path: '/notifications',
    name: 'Notifications',
    component: () => import('@/views/NotificationList.vue'),
    meta: { title: '消息通知' }
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

router.beforeEach((to, from, next) => {
  document.title = to.meta.title ? `${to.meta.title} - 个人记账系统` : '个人记账系统'
  next()
})

export default router
