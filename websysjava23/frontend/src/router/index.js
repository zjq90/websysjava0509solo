import Vue from 'vue'
import Router from 'vue-router'
import Home from '../views/Home.vue'
import Patient from '../views/Patient.vue'
import Hospitalization from '../views/Hospitalization.vue'
import MedicalRecord from '../views/MedicalRecord.vue'
import MedicalOrder from '../views/MedicalOrder.vue'
import VitalSigns from '../views/VitalSigns.vue'
import Bed from '../views/Bed.vue'
import Medicine from '../views/Medicine.vue'
import Fee from '../views/Fee.vue'
import Staff from '../views/Staff.vue'

Vue.use(Router)

export default new Router({
  mode: 'history',
  base: process.env.BASE_URL,
  routes: [
    {
      path: '/',
      name: 'home',
      component: Home
    },
    {
      path: '/patient',
      name: 'patient',
      component: Patient
    },
    {
      path: '/hospitalization',
      name: 'hospitalization',
      component: Hospitalization
    },
    {
      path: '/medical-record',
      name: 'medicalRecord',
      component: MedicalRecord
    },
    {
      path: '/medical-order',
      name: 'medicalOrder',
      component: MedicalOrder
    },
    {
      path: '/vital-signs',
      name: 'vitalSigns',
      component: VitalSigns
    },
    {
      path: '/bed',
      name: 'bed',
      component: Bed
    },
    {
      path: '/medicine',
      name: 'medicine',
      component: Medicine
    },
    {
      path: '/fee',
      name: 'fee',
      component: Fee
    },
    {
      path: '/staff',
      name: 'staff',
      component: Staff
    }
  ]
})
