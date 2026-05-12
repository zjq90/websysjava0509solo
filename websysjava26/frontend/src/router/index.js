import Vue from 'vue'
import Router from 'vue-router'
import Home from '../views/Home.vue'
import Patient from '../views/Patient.vue'
import Outpatient from '../views/Outpatient.vue'
import Inpatient from '../views/Inpatient.vue'
import Insurance from '../views/Insurance.vue'
import Report from '../views/Report.vue'

Vue.use(Router)

export default new Router({
  mode: 'history',
  routes: [
    {
      path: '/',
      name: 'Home',
      component: Home
    },
    {
      path: '/patients',
      name: 'Patient',
      component: Patient
    },
    {
      path: '/outpatient',
      name: 'Outpatient',
      component: Outpatient
    },
    {
      path: '/inpatient',
      name: 'Inpatient',
      component: Inpatient
    },
    {
      path: '/insurance',
      name: 'Insurance',
      component: Insurance
    },
    {
      path: '/report',
      name: 'Report',
      component: Report
    }
  ]
})
