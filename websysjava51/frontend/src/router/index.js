import Vue from 'vue'
import VueRouter from 'vue-router'
import Layout from '@/layout/Index.vue'

Vue.use(VueRouter)

const routes = [
  {
    path: '/login',
    name: 'Login',
    component: () => import('@/views/login/Index.vue'),
    hidden: true
  },
  {
    path: '/',
    component: Layout,
    redirect: '/dashboard',
    children: [
      {
        path: 'dashboard',
        name: 'Dashboard',
        component: () => import('@/views/dashboard/Index.vue'),
        meta: { title: '首页', icon: 'el-icon-s-home' }
      }
    ]
  },
  {
    path: '/product',
    component: Layout,
    redirect: '/product/list',
    name: 'Product',
    meta: { title: '商品管理', icon: 'el-icon-goods' },
    children: [
      {
        path: 'list',
        name: 'ProductList',
        component: () => import('@/views/product/List.vue'),
        meta: { title: '商品列表', icon: 'el-icon-s-grid' }
      },
      {
        path: 'low-stock',
        name: 'LowStock',
        component: () => import('@/views/product/LowStock.vue'),
        meta: { title: '库存预警', icon: 'el-icon-warning' }
      }
    ]
  },
  {
    path: '/order',
    component: Layout,
    redirect: '/order/list',
    name: 'Order',
    meta: { title: '订单管理', icon: 'el-icon-s-order' },
    children: [
      {
        path: 'list',
        name: 'OrderList',
        component: () => import('@/views/order/List.vue'),
        meta: { title: '订单列表', icon: 'el-icon-document' }
      },
      {
        path: 'abnormal',
        name: 'AbnormalOrder',
        component: () => import('@/views/order/Abnormal.vue'),
        meta: { title: '异常订单', icon: 'el-icon-warning-outline' }
      }
    ]
  },
  {
    path: '/dispute',
    component: Layout,
    redirect: '/dispute/list',
    name: 'Dispute',
    meta: { title: '纠纷管理', icon: 'el-icon-s-cooperation' },
    children: [
      {
        path: 'list',
        name: 'DisputeList',
        component: () => import('@/views/dispute/List.vue'),
        meta: { title: '纠纷列表', icon: 'el-icon-s-custom' }
      }
    ]
  },
  {
    path: '/statistics',
    component: Layout,
    redirect: '/statistics/transaction',
    name: 'Statistics',
    meta: { title: '数据统计', icon: 'el-icon-s-data' },
    children: [
      {
        path: 'transaction',
        name: 'TransactionStatistics',
        component: () => import('@/views/statistics/Transaction.vue'),
        meta: { title: '交易额统计', icon: 'el-icon-finance' }
      },
      {
        path: 'product-ranking',
        name: 'ProductRanking',
        component: () => import('@/views/statistics/ProductRanking.vue'),
        meta: { title: '商品排行', icon: 'el-icon-trophy' }
      },
      {
        path: 'user-growth',
        name: 'UserGrowth',
        component: () => import('@/views/statistics/UserGrowth.vue'),
        meta: { title: '用户增长', icon: 'el-icon-user-solid' }
      }
    ]
  },
  {
    path: '/system',
    component: Layout,
    redirect: '/system/role',
    name: 'System',
    meta: { title: '系统管理', icon: 'el-icon-s-tools' },
    children: [
      {
        path: 'role',
        name: 'RoleManagement',
        component: () => import('@/views/system/Role.vue'),
        meta: { title: '角色管理', icon: 'el-icon-s-custom' }
      },
      {
        path: 'user',
        name: 'UserManagement',
        component: () => import('@/views/system/User.vue'),
        meta: { title: '用户管理', icon: 'el-icon-user' }
      },
      {
        path: 'operation-log',
        name: 'OperationLog',
        component: () => import('@/views/system/OperationLog.vue'),
        meta: { title: '操作日志', icon: 'el-icon-document-copy' }
      }
    ]
  }
]

const router = new VueRouter({
  mode: 'hash',
  base: process.env.BASE_URL,
  routes
})

router.beforeEach((to, from, next) => {
  next()
})

export default router