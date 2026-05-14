import Vue from 'vue'
import Router from 'vue-router'
import Dashboard from '../views/Dashboard.vue'
import ConversionFunnel from '../views/ConversionFunnel.vue'
import EmployeePerformance from '../views/EmployeePerformance.vue'
import FinanceReport from '../views/FinanceReport.vue'
import OrderManagement from '../views/OrderManagement.vue'
import ReimbursementManagement from '../views/ReimbursementManagement.vue'

Vue.use(Router)

export default new Router({
  mode: 'history',
  routes: [
    {
      path: '/',
      redirect: '/dashboard'
    },
    {
      path: '/dashboard',
      name: 'Dashboard',
      component: Dashboard
    },
    {
      path: '/conversion-funnel',
      name: 'ConversionFunnel',
      component: ConversionFunnel
    },
    {
      path: '/employee-performance',
      name: 'EmployeePerformance',
      component: EmployeePerformance
    },
    {
      path: '/finance-report',
      name: 'FinanceReport',
      component: FinanceReport
    },
    {
      path: '/orders',
      name: 'OrderManagement',
      component: OrderManagement
    },
    {
      path: '/reimbursements',
      name: 'ReimbursementManagement',
      component: ReimbursementManagement
    }
  ]
})
