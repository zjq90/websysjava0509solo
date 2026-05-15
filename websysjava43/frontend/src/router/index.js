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
    path: '/register',
    name: 'Register',
    component: () => import('@/views/Register.vue'),
    meta: { title: '注册' }
  },
  {
    path: '/',
    name: 'Layout',
    component: () => import('@/views/Layout.vue'),
    redirect: '/dashboard',
    children: [
      {
        path: 'dashboard',
        name: 'Dashboard',
        component: () => import('@/views/Dashboard.vue'),
        meta: { title: '首页', requiresAuth: true }
      },
      {
        path: 'profile',
        name: 'Profile',
        component: () => import('@/views/user/Profile.vue'),
        meta: { title: '个人中心', requiresAuth: true }
      },
      {
        path: 'courses',
        name: 'Courses',
        component: () => import('@/views/course/CourseList.vue'),
        meta: { title: '课程列表', requiresAuth: true }
      },
      {
        path: 'courses/:id',
        name: 'CourseDetail',
        component: () => import('@/views/course/CourseDetail.vue'),
        meta: { title: '课程详情', requiresAuth: true }
      },
      {
        path: 'my-courses',
        name: 'MyCourses',
        component: () => import('@/views/course/MyCourses.vue'),
        meta: { title: '我的课程', requiresAuth: true }
      },
      {
        path: 'messages',
        name: 'Messages',
        component: () => import('@/views/message/MessageList.vue'),
        meta: { title: '消息中心', requiresAuth: true }
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
  document.title = to.meta.title ? `${to.meta.title} - 教学辅助系统` : '教学辅助系统'
  
  if (to.meta.requiresAuth) {
    const token = store.state.user.token
    if (!token) {
      next('/login')
    } else {
      next()
    }
  } else {
    next()
  }
})

export default router
