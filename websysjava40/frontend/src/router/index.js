import { createRouter, createWebHistory } from 'vue-router'

const routes = [
  {
    path: '/',
    name: 'Dashboard',
    component: () => import('../views/Dashboard.vue')
  },
  {
    path: '/employees',
    name: 'Employees',
    component: () => import('../views/Employees.vue')
  },
  {
    path: '/schedules',
    name: 'Schedules',
    component: () => import('../views/Schedules.vue')
  },
  {
    path: '/transfer-requests',
    name: 'TransferRequests',
    component: () => import('../views/TransferRequests.vue')
  },
  {
    path: '/venues',
    name: 'Venues',
    component: () => import('../views/Venues.vue')
  },
  {
    path: '/venue-bookings',
    name: 'VenueBookings',
    component: () => import('../views/VenueBookings.vue')
  },
  {
    path: '/costumes',
    name: 'Costumes',
    component: () => import('../views/Costumes.vue')
  },
  {
    path: '/costume-bookings',
    name: 'CostumeBookings',
    component: () => import('../views/CostumeBookings.vue')
  },
  {
    path: '/orders',
    name: 'Orders',
    component: () => import('../views/Orders.vue')
  },
  {
    path: '/express',
    name: 'Express',
    component: () => import('../views/Express.vue')
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

export default router
