import Vue from 'vue'
import VueRouter from 'vue-router'
import Layout from '../views/Layout.vue'

Vue.use(VueRouter)

const routes = [
  {
    path: '/login',
    name: 'Login',
    component: () => import('../views/Login.vue'),
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
        component: () => import('../views/Dashboard.vue'),
        meta: { title: '首页', icon: 'home' }
      }
    ]
  },
  {
    path: '/system',
    component: Layout,
    redirect: '/system/user',
    meta: { title: '系统管理', icon: 'setting' },
    children: [
      {
        path: 'user',
        name: 'UserList',
        component: () => import('../views/system/UserList.vue'),
        meta: { title: '用户管理', icon: 'user' }
      },
      {
        path: 'role',
        name: 'RoleList',
        component: () => import('../views/system/RoleList.vue'),
        meta: { title: '角色管理', icon: 'team' }
      },
      {
        path: 'menu',
        name: 'MenuList',
        component: () => import('../views/system/MenuList.vue'),
        meta: { title: '菜单管理', icon: 'menu' }
      }
    ]
  },
  {
    path: '/device',
    component: Layout,
    redirect: '/device/list',
    meta: { title: '设备管理', icon: 'desktop' },
    children: [
      {
        path: 'list',
        name: 'DeviceList',
        component: () => import('../views/device/DeviceList.vue'),
        meta: { title: '设备列表', icon: 'appstore' }
      },
      {
        path: 'remote',
        name: 'RemoteControl',
        component: () => import('../views/device/RemoteControl.vue'),
        meta: { title: '远程控制', icon: 'control' }
      }
    ]
  },
  {
    path: '/product',
    component: Layout,
    redirect: '/product/list',
    meta: { title: '商品管理', icon: 'shopping' },
    children: [
      {
        path: 'list',
        name: 'ProductList',
        component: () => import('../views/product/ProductList.vue'),
        meta: { title: '商品列表', icon: 'shop' }
      },
      {
        path: 'slot',
        name: 'SlotList',
        component: () => import('../views/product/SlotList.vue'),
        meta: { title: '货道管理', icon: 'container' }
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
  next()
})

export default router
