import { createRouter, createWebHistory } from 'vue-router'

const routes = [
  {
    path: '/',
    redirect: '/appointment'
  },
  {
    path: '/appointment',
    name: 'Appointment',
    component: () => import('../views/Appointment.vue')
  },
  {
    path: '/queue',
    name: 'Queue',
    component: () => import('../views/Queue.vue')
  },
  {
    path: '/doctor',
    name: 'Doctor',
    component: () => import('../views/DoctorStation.vue')
  },
  {
    path: '/charge',
    name: 'Charge',
    component: () => import('../views/Charge.vue')
  },
  {
    path: '/pharmacy',
    name: 'Pharmacy',
    component: () => import('../views/Pharmacy.vue')
  },
  {
    path: '/query',
    name: 'Query',
    component: () => import('../views/Query.vue')
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

export default router
