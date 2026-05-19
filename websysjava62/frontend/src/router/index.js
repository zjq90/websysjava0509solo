import { createRouter, createWebHistory } from 'vue-router'

const routes = [
  {
    path: '/',
    redirect: '/emergency-symptoms'
  },
  {
    path: '/emergency-symptoms',
    name: 'EmergencySymptoms',
    component: () => import('../views/EmergencySymptoms.vue')
  },
  {
    path: '/medicine',
    name: 'Medicine',
    component: () => import('../views/Medicine.vue')
  },
  {
    path: '/review-rules',
    name: 'ReviewRules',
    component: () => import('../views/ReviewRules.vue')
  },
  {
    path: '/smart-device',
    name: 'SmartDevice',
    component: () => import('../views/SmartDevice.vue')
  },
  {
    path: '/consultation',
    name: 'Consultation',
    component: () => import('../views/Consultation.vue')
  },
  {
    path: '/insurance',
    name: 'Insurance',
    component: () => import('../views/Insurance.vue')
  },
  {
    path: '/ai-diagnosis',
    name: 'AIDiagnosis',
    component: () => import('../views/AIDiagnosis.vue')
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

export default router
