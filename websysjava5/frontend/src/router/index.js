import Vue from 'vue'
import VueRouter from 'vue-router'
import store from '@/store'

Vue.use(VueRouter)

const routes = [
  {
    path: '/login',
    name: 'Login',
    component: () => import('@/views/Login.vue'),
    meta: { title: '登录' }
  },
  {
    path: '/',
    component: () => import('@/views/Layout.vue'),
    redirect: '/dashboard',
    children: [
      {
        path: 'dashboard',
        name: 'Dashboard',
        component: () => import('@/views/Dashboard.vue'),
        meta: { title: '首页' }
      },
      {
        path: 'agents',
        name: 'AgentList',
        component: () => import('@/views/agent/AgentList.vue'),
        meta: { title: '代理商管理' }
      },
      {
        path: 'logs',
        name: 'OperationLogList',
        component: () => import('@/views/log/OperationLogList.vue'),
        meta: { title: '操作日志' }
      },
      {
        path: 'configs',
        name: 'ConfigList',
        component: () => import('@/views/config/ConfigList.vue'),
        meta: { title: '系统配置' }
      }
    ]
  }
]

const router = new VueRouter({
  mode: 'history',
  base: process.env.BASE_URL,
  routes
})

router.beforeEach((to, from, next) => {
  if (to.meta.title) {
    document.title = to.meta.title + ' - Web平台管理系统'
  }

  const token = store.getters.token
  if (to.path === '/login') {
    if (token) {
      next('/')
    } else {
      next()
    }
  } else {
    if (token) {
      if (!store.getters.userInfo) {
        store.dispatch('GetCurrentUser').then(() => {
          next()
        }).catch(() => {
          store.dispatch('Logout')
          next('/login')
        })
      } else {
        next()
      }
    } else {
      next('/login')
    }
  }
})

export default router
