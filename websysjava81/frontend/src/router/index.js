import { createRouter, createWebHashHistory } from 'vue-router'

const routes = [
  {
    path: '/',
    redirect: '/user'
  },
  {
    path: '/user',
    name: 'UserList',
    component: () => import('../views/user/UserList.vue')
  },
  {
    path: '/user/detail/:userId',
    name: 'UserDetail',
    component: () => import('../views/user/UserDetail.vue')
  },
  {
    path: '/audit/comment',
    name: 'CommentAudit',
    component: () => import('../views/audit/CommentAudit.vue')
  },
  {
    path: '/audit/sensitive',
    name: 'SensitiveWord',
    component: () => import('../views/audit/SensitiveWord.vue')
  },
  {
    path: '/audit/report',
    name: 'ReportHandle',
    component: () => import('../views/audit/ReportHandle.vue')
  }
]

const router = createRouter({
  history: createWebHashHistory(),
  routes
})

export default router
