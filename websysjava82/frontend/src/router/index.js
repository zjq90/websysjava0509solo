import { createRouter, createWebHashHistory } from 'vue-router'

const routes = [
  {
    path: '/',
    redirect: '/dashboard'
  },
  {
    path: '/dashboard',
    component: () => import('../views/Dashboard.vue'),
    meta: { title: '数据概览' }
  },
  {
    path: '/ad-slots',
    component: () => import('../views/ad/AdSlotList.vue'),
    meta: { title: '广告位配置' }
  },
  {
    path: '/advertisements',
    component: () => import('../views/ad/AdvertisementList.vue'),
    meta: { title: '广告管理' }
  },
  {
    path: '/revenue-stats',
    component: () => import('../views/ad/RevenueStats.vue'),
    meta: { title: '收益统计' }
  },
  {
    path: '/reward-video',
    component: () => import('../views/ad/RewardVideoConfig.vue'),
    meta: { title: '激励视频配置' }
  },
  {
    path: '/push-tasks',
    component: () => import('../views/message/PushTaskList.vue'),
    meta: { title: '站内信推送' }
  },
  {
    path: '/message-templates',
    component: () => import('../views/message/MessageTemplateList.vue'),
    meta: { title: '模板管理' }
  },
  {
    path: '/popup-announcements',
    component: () => import('../views/message/PopupAnnouncementList.vue'),
    meta: { title: '弹窗公告' }
  }
]

const router = createRouter({
  history: createWebHashHistory(),
  routes
})

router.beforeEach((to, from, next) => {
  document.title = to.meta.title ? `${to.meta.title} - 游戏平台管理系统` : '游戏平台管理系统'
  next()
})

export default router
