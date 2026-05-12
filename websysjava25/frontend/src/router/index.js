import Vue from 'vue'
import VueRouter from 'vue-router'
import Home from '../views/Home.vue'
import ApplicationList from '../views/application/List.vue'
import TechnicianTasks from '../views/technician/Tasks.vue'
import ResultList from '../views/result/List.vue'
import ReportList from '../views/report/List.vue'
import ReportAudit from '../views/report/Audit.vue'
import DepartmentList from '../views/basic/Department.vue'
import ItemList from '../views/basic/Item.vue'
import PatientList from '../views/basic/Patient.vue'

Vue.use(VueRouter)

const routes = [
  {
    path: '/',
    name: 'Home',
    component: Home
  },
  {
    path: '/applications',
    name: 'ApplicationList',
    component: ApplicationList
  },
  {
    path: '/technician',
    name: 'TechnicianTasks',
    component: TechnicianTasks
  },
  {
    path: '/results',
    name: 'ResultList',
    component: ResultList
  },
  {
    path: '/reports',
    name: 'ReportList',
    component: ReportList
  },
  {
    path: '/reports/audit',
    name: 'ReportAudit',
    component: ReportAudit
  },
  {
    path: '/departments',
    name: 'DepartmentList',
    component: DepartmentList
  },
  {
    path: '/items',
    name: 'ItemList',
    component: ItemList
  },
  {
    path: '/patients',
    name: 'PatientList',
    component: PatientList
  }
]

const router = new VueRouter({
  mode: 'history',
  routes
})

export default router
