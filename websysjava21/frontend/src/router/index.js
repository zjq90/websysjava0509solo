import Vue from 'vue'
import VueRouter from 'vue-router'
import Login from '@/views/Login.vue'
import Layout from '@/views/Layout.vue'
import store from '@/store'

Vue.use(VueRouter)

const routes = [
  {
    path: '/login',
    name: 'Login',
    component: Login,
    meta: { title: '登录' }
  },
  {
    path: '/',
    component: Layout,
    redirect: '/dashboard',
    children: [
      {
        path: 'dashboard',
        name: 'Dashboard',
        component: () => import('@/views/Dashboard.vue'),
        meta: { title: '首页' }
      },
      {
        path: 'system/user',
        name: 'UserManage',
        component: () => import('@/views/system/User.vue'),
        meta: { title: '用户管理' }
      },
      {
        path: 'system/role',
        name: 'RoleManage',
        component: () => import('@/views/system/Role.vue'),
        meta: { title: '角色管理' }
      },
      {
        path: 'system/dict',
        name: 'DictManage',
        component: () => import('@/views/system/Dict.vue'),
        meta: { title: '字典管理' }
      },
      {
        path: 'system/log',
        name: 'LogManage',
        component: () => import('@/views/system/Log.vue'),
        meta: { title: '日志管理' }
      },
      {
        path: 'system/config',
        name: 'ConfigManage',
        component: () => import('@/views/system/Config.vue'),
        meta: { title: '系统配置' }
      },
      {
        path: 'system/permission',
        name: 'PermissionManage',
        component: () => import('@/views/system/Permission.vue'),
        meta: { title: '权限管理' }
      },
      {
        path: 'doctor/patient',
        name: 'DoctorPatient',
        component: () => import('@/views/doctor/Patient.vue'),
        meta: { title: '患者管理' }
      },
      {
        path: 'nurse/ward',
        name: 'NurseWard',
        component: () => import('@/views/nurse/Ward.vue'),
        meta: { title: '病房管理' }
      },
      {
        path: 'finance/charge',
        name: 'FinanceCharge',
        component: () => import('@/views/finance/Charge.vue'),
        meta: { title: '收费管理' }
      }
    ]
  }
]

const router = new VueRouter({
  mode: 'history',
  base: process.env.BASE_URL,
  routes
})

// 路由守卫
router.beforeEach((to, from, next) => {
  const token = store.getters.token
  if (to.path !== '/login' && !token) {
    next('/login')
  } else if (to.path === '/login' && token) {
    next('/')
  } else {
    next()
  }
})

export default router
