import { createRouter, createWebHistory } from 'vue-router'

const routes = [
  {
    path: '/',
    redirect: '/dashboard'
  },
  {
    path: '/dashboard',
    name: 'Dashboard',
    component: () => import('@/views/Dashboard.vue'),
    meta: { title: '数据概览' }
  },
  {
    path: '/cultural-relic',
    name: 'CulturalRelic',
    component: () => import('@/views/cultural-relic/Index.vue'),
    meta: { title: '文物管理' }
  },
  {
    path: '/blockchain-node',
    name: 'BlockchainNode',
    component: () => import('@/views/blockchain-node/Index.vue'),
    meta: { title: '区块链节点' }
  },
  {
    path: '/iot-device',
    name: 'IotDevice',
    component: () => import('@/views/iot-device/Index.vue'),
    meta: { title: '物联网设备' }
  },
  {
    path: '/audit-rule',
    name: 'AuditRule',
    component: () => import('@/views/audit-rule/Index.vue'),
    meta: { title: '审核规则' }
  },
  {
    path: '/digital-twin',
    name: 'DigitalTwin',
    component: () => import('@/views/digital-twin/Index.vue'),
    meta: { title: '数字分身' }
  },
  {
    path: '/vr-scene',
    name: 'VrScene',
    component: () => import('@/views/vr-scene/Index.vue'),
    meta: { title: 'VR博物馆' }
  },
  {
    path: '/recognition-model',
    name: 'RecognitionModel',
    component: () => import('@/views/recognition-model/Index.vue'),
    meta: { title: '识别模型' }
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

export default router
