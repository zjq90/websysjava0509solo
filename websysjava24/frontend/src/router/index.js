import Vue from 'vue'
import VueRouter from 'vue-router'

Vue.use(VueRouter)

const routes = [
  {
    path: '/',
    redirect: '/medicine/list'
  },
  {
    path: '/medicine/list',
    name: 'MedicineList',
    component: () => import('@/views/medicine/MedicineList.vue')
  },
  {
    path: '/medicine/inventory',
    name: 'MedicineInventory',
    component: () => import('@/views/medicine/MedicineInventory.vue')
  },
  {
    path: '/material/list',
    name: 'MaterialList',
    component: () => import('@/views/material/MaterialList.vue')
  },
  {
    path: '/material/inventory',
    name: 'MaterialInventory',
    component: () => import('@/views/material/MaterialInventory.vue')
  }
]

const router = new VueRouter({
  mode: 'history',
  base: process.env.BASE_URL,
  routes
})

export default router
