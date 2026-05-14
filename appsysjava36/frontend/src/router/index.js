import Vue from 'vue'
import Router from 'vue-router'
import Home from '../views/Home.vue'
import WorkOrder from '../views/WorkOrder.vue'
import User from '../views/User.vue'
import Technician from '../views/Technician.vue'
import Analytics from '../views/Analytics.vue'
import Marketing from '../views/Marketing.vue'

Vue.use(Router)

export default new Router({
  mode: 'hash',
  routes: [
    {
      path: '/',
      name: 'Home',
      component: Home
    },
    {
      path: '/workorders',
      name: 'WorkOrder',
      component: WorkOrder
    },
    {
      path: '/users',
      name: 'User',
      component: User
    },
    {
      path: '/technicians',
      name: 'Technician',
      component: Technician
    },
    {
      path: '/analytics',
      name: 'Analytics',
      component: Analytics
    },
    {
      path: '/marketing',
      name: 'Marketing',
      component: Marketing
    }
  ]
})
