import Vue from 'vue'
import VueRouter from 'vue-router'
import Home from '../views/Home.vue'
import OperationList from '../views/operation/List.vue'
import OperationStatistics from '../views/operation/Statistics.vue'
import MedicalQualityList from '../views/medical-quality/List.vue'
import MedicalQualityStatistics from '../views/medical-quality/Statistics.vue'
import CostBenefitList from '../views/cost-benefit/List.vue'
import CostBenefitStatistics from '../views/cost-benefit/Statistics.vue'
import CustomReportsList from '../views/custom-reports/List.vue'
import CustomReportsCreate from '../views/custom-reports/Create.vue'

Vue.use(VueRouter)

const routes = [
  {
    path: '/',
    name: 'Home',
    component: Home
  },
  {
    path: '/operation/list',
    name: 'OperationList',
    component: OperationList
  },
  {
    path: '/operation/statistics',
    name: 'OperationStatistics',
    component: OperationStatistics
  },
  {
    path: '/medical-quality/list',
    name: 'MedicalQualityList',
    component: MedicalQualityList
  },
  {
    path: '/medical-quality/statistics',
    name: 'MedicalQualityStatistics',
    component: MedicalQualityStatistics
  },
  {
    path: '/cost-benefit/list',
    name: 'CostBenefitList',
    component: CostBenefitList
  },
  {
    path: '/cost-benefit/statistics',
    name: 'CostBenefitStatistics',
    component: CostBenefitStatistics
  },
  {
    path: '/custom-reports/list',
    name: 'CustomReportsList',
    component: CustomReportsList
  },
  {
    path: '/custom-reports/create',
    name: 'CustomReportsCreate',
    component: CustomReportsCreate
  }
]

const router = new VueRouter({
  mode: 'history',
  base: process.env.BASE_URL,
  routes
})

export default router
