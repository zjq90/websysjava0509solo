import { createRouter, createWebHistory } from 'vue-router'

const routes = [
  {
    path: '/',
    redirect: '/dashboard'
  },
  {
    path: '/',
    component: () => import('../components/Layout.vue'),
    children: [
      {
        path: 'dashboard',
        name: 'Dashboard',
        component: () => import('../views/Dashboard.vue'),
        meta: { title: '系统概览', icon: 'DataBoard' }
      },
      {
        path: 'products',
        name: 'Products',
        component: () => import('../views/Products.vue'),
        meta: { title: '商品管理', icon: 'Goods' }
      },
      {
        path: 'inventory',
        name: 'Inventory',
        component: () => import('../views/Inventory.vue'),
        meta: { title: '库存监控', icon: 'Box' }
      },
      {
        path: 'restocks',
        name: 'Restocks',
        component: () => import('../views/Restocks.vue'),
        meta: { title: '智能补货', icon: 'ShoppingCart' }
      },
      {
        path: 'orders',
        name: 'Orders',
        component: () => import('../views/Orders.vue'),
        meta: { title: '订单列表', icon: 'Document' }
      },
      {
        path: 'machines',
        name: 'Machines',
        component: () => import('../views/Machines.vue'),
        meta: { title: '设备管理', icon: 'Monitor' }
      }
    ]
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

export default router
