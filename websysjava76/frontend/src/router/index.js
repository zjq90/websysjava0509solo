import { createRouter, createWebHistory } from 'vue-router'

const routes = [
  {
    path: '/',
    redirect: '/dashboard'
  },
  {
    path: '/',
    component: () => import('@/layout/Index.vue'),
    children: [
      {
        path: 'dashboard',
        name: 'Dashboard',
        component: () => import('@/views/Dashboard.vue'),
        meta: { title: '数据概览' }
      },
      {
        path: 'club/list',
        name: 'ClubList',
        component: () => import('@/views/club/List.vue'),
        meta: { title: '社团列表' }
      },
      {
        path: 'club/application',
        name: 'ClubApplication',
        component: () => import('@/views/club/Application.vue'),
        meta: { title: '成立申请' }
      },
      {
        path: 'club/registration',
        name: 'ClubRegistration',
        component: () => import('@/views/club/AnnualRegistration.vue'),
        meta: { title: '年度注册' }
      },
      {
        path: 'club/violation',
        name: 'ClubViolation',
        component: () => import('@/views/club/Violation.vue'),
        meta: { title: '违规处理' }
      },
      {
        path: 'activity/list',
        name: 'ActivityList',
        component: () => import('@/views/activity/List.vue'),
        meta: { title: '活动列表' }
      },
      {
        path: 'activity/approval',
        name: 'ActivityApproval',
        component: () => import('@/views/activity/Approval.vue'),
        meta: { title: '活动审批' }
      },
      {
        path: 'activity/review',
        name: 'ActivityReview',
        component: () => import('@/views/activity/Review.vue'),
        meta: { title: '内容审查' }
      },
      {
        path: 'statistics/club',
        name: 'ClubStatistics',
        component: () => import('@/views/statistics/Club.vue'),
        meta: { title: '社团数据' }
      },
      {
        path: 'statistics/finance',
        name: 'FinanceStatistics',
        component: () => import('@/views/statistics/Finance.vue'),
        meta: { title: '经费监管' }
      },
      {
        path: 'system/user',
        name: 'UserManagement',
        component: () => import('@/views/system/UserManagement.vue'),
        meta: { title: '权限管理' }
      },
      {
        path: 'system/template',
        name: 'TemplateManagement',
        component: () => import('@/views/system/MessageTemplate.vue'),
        meta: { title: '消息模板' }
      },
      {
        path: 'system/backup',
        name: 'BackupManagement',
        component: () => import('@/views/system/Backup.vue'),
        meta: { title: '数据备份' }
      }
    ]
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

export default router
