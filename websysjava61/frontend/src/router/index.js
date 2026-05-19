import { createRouter, createWebHistory } from 'vue-router'

const routes = [
  {
    path: '/',
    redirect: '/dashboard'
  },
  {
    path: '/dashboard',
    name: 'Dashboard',
    component: () => import('@/views/Dashboard.vue')
  },
  {
    path: '/hospital/department',
    name: 'Department',
    component: () => import('@/views/hospital/Department.vue')
  },
  {
    path: '/hospital/doctor',
    name: 'Doctor',
    component: () => import('@/views/user/DoctorAudit.vue')
  },
  {
    path: '/hospital/schedule',
    name: 'Schedule',
    component: () => import('@/views/hospital/Schedule.vue')
  },
  {
    path: '/hospital/medicine',
    name: 'Medicine',
    component: () => import('@/views/hospital/Medicine.vue')
  },
  {
    path: '/user/owner',
    name: 'PetOwner',
    component: () => import('@/views/user/PetOwner.vue')
  },
  {
    path: '/user/doctor-audit',
    name: 'DoctorAudit',
    component: () => import('@/views/user/DoctorAudit.vue')
  },
  {
    path: '/user/credit',
    name: 'Credit',
    component: () => import('@/views/user/Credit.vue')
  },
  {
    path: '/statistics/disease',
    name: 'DiseaseHeatmap',
    component: () => import('@/views/statistics/DiseaseHeatmap.vue')
  },
  {
    path: '/statistics/user-growth',
    name: 'UserGrowth',
    component: () => import('@/views/statistics/UserGrowth.vue')
  },
  {
    path: '/statistics/operation',
    name: 'Operation',
    component: () => import('@/views/statistics/Operation.vue')
  }
]

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes
})

export default router
