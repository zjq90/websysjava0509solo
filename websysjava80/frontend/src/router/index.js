import { createRouter, createWebHistory } from 'vue-router'

const routes = [
  {
    path: '/',
    component: () => import('@/layout/MainLayout.vue'),
    redirect: '/dashboard',
    children: [
      {
        path: 'dashboard',
        name: 'Dashboard',
        component: () => import('@/views/dashboard/Dashboard.vue'),
        meta: { title: '数据总览', icon: 'DataLine' }
      },
      {
        path: 'games',
        name: 'Games',
        component: () => import('@/views/game/GameList.vue'),
        meta: { title: '游戏管理', icon: 'Grid' }
      },
      {
        path: 'audit',
        name: 'Audit',
        component: () => import('@/views/audit/AuditQueue.vue'),
        meta: { title: '审核管理', icon: 'Check' }
      },
      {
        path: 'recommendation',
        name: 'Recommendation',
        component: () => import('@/views/recommendation/Recommendation.vue'),
        meta: { title: '推荐管理', icon: 'Star' }
      },
      {
        path: 'category-tag',
        name: 'CategoryTag',
        component: () => import('@/views/categoryTag/CategoryTag.vue'),
        meta: { title: '分类标签', icon: 'Collection' }
      }
    ]
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

router.beforeEach((to, from, next) => {
  document.title = to.meta.title ? `${to.meta.title} - 小游戏管理平台` : '小游戏管理平台'
  next()
})

export default router
