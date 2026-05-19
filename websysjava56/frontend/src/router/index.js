import Vue from 'vue'
import VueRouter from 'vue-router'
import Home from '../views/Home.vue'
import Heritage from '../views/Heritage.vue'
import DataGovernance from '../views/DataGovernance.vue'
import DataAnalysis from '../views/DataAnalysis.vue'
import RiskAlert from '../views/RiskAlert.vue'
import TestData from '../views/TestData.vue'

Vue.use(VueRouter)

const routes = [
  {
    path: '/',
    name: 'Home',
    component: Home
  },
  {
    path: '/heritage',
    name: 'Heritage',
    component: Heritage
  },
  {
    path: '/data-governance',
    name: 'DataGovernance',
    component: DataGovernance
  },
  {
    path: '/data-analysis',
    name: 'DataAnalysis',
    component: DataAnalysis
  },
  {
    path: '/risk-alert',
    name: 'RiskAlert',
    component: RiskAlert
  },
  {
    path: '/test-data',
    name: 'TestData',
    component: TestData
  }
]

const router = new VueRouter({
  mode: 'history',
  base: process.env.BASE_URL,
  routes
})

export default router
