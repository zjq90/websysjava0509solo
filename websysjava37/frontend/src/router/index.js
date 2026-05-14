import Vue from 'vue'
import VueRouter from 'vue-router'
import Home from '../views/Home.vue'
import CustomerList from '../views/CustomerList.vue'
import TagList from '../views/TagList.vue'
import InteractionList from '../views/InteractionList.vue'
import ReminderList from '../views/ReminderList.vue'

Vue.use(VueRouter)

const routes = [
  {
    path: '/',
    name: 'Home',
    component: Home
  },
  {
    path: '/customers',
    name: 'CustomerList',
    component: CustomerList
  },
  {
    path: '/tags',
    name: 'TagList',
    component: TagList
  },
  {
    path: '/interactions',
    name: 'InteractionList',
    component: InteractionList
  },
  {
    path: '/reminders',
    name: 'ReminderList',
    component: ReminderList
  }
]

const router = new VueRouter({
  mode: 'history',
  base: process.env.BASE_URL,
  routes
})

export default router
