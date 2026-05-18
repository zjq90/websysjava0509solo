import { createRouter, createWebHistory } from 'vue-router'
import Layout from '@/layouts/Layout.vue'

const routes = [
  {
    path: '/login',
    name: 'Login',
    component: () => import('@/views/Login.vue'),
    meta: { title: '登录' }
  },
  {
    path: '/',
    component: Layout,
    redirect: '/home',
    children: [
      {
        path: 'home',
        name: 'Home',
        component: () => import('@/views/Home.vue'),
        meta: { title: '首页', icon: 'House' }
      },
      {
        path: 'community',
        name: 'Community',
        component: () => import('@/views/Community.vue'),
        meta: { title: '藏友圈', icon: 'ChatDotRound' }
      },
      {
        path: 'market',
        name: 'Market',
        component: () => import('@/views/Market.vue'),
        meta: { title: '交易市场', icon: 'ShoppingBag' }
      },
      {
        path: 'activity',
        name: 'Activity',
        component: () => import('@/views/Activity.vue'),
        meta: { title: '线下活动', icon: 'Calendar' }
      },
      {
        path: 'mine',
        name: 'Mine',
        component: () => import('@/views/Mine.vue'),
        meta: { title: '我的', icon: 'User' }
      }
    ]
  },
  {
    path: '/post/:id',
    name: 'PostDetail',
    component: () => import('@/views/PostDetail.vue'),
    meta: { title: '帖子详情' }
  },
  {
    path: '/heritage/:id',
    name: 'HeritageDetail',
    component: () => import('@/views/HeritageDetail.vue'),
    meta: { title: '文物详情' }
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

router.beforeEach((to, from, next) => {
  document.title = to.meta.title + ' - 文物收藏APP'
  next()
})

export default router
