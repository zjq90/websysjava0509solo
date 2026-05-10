import axios from 'axios'

const api = axios.create({
  baseURL: 'http://localhost:8080',
  timeout: 10000
})

api.interceptors.response.use(
  response => response,
  error => {
    console.error('API Error:', error)
    return Promise.reject(error)
  }
)

export default {
  // Dashboard
  getDashboardData() {
    return api.get('/api/dashboard')
  },

  // Products
  getProducts(page = 0, size = 10) {
    return api.get(`/api/products?page=${page}&size=${size}`)
  },
  getProduct(id) {
    return api.get(`/api/products/${id}`)
  },
  createProduct(data) {
    return api.post('/api/products', data)
  },
  updateProduct(id, data) {
    return api.put(`/api/products/${id}`, data)
  },
  deleteProduct(id) {
    return api.delete(`/api/products/${id}`)
  },
  getHotSellingProducts(limit = 10) {
    return api.get(`/api/products/stats/hot-selling?limit=${limit}`)
  },
  getUnsoldProducts(limit = 10) {
    return api.get(`/api/products/stats/unsold?limit=${limit}`)
  },
  getGrossMarginAnalysis() {
    return api.get('/api/products/stats/gross-margin')
  },

  // Devices
  getDevices(page = 0, size = 10) {
    return api.get(`/api/devices?page=${page}&size=${size}`)
  },
  getDevice(id) {
    return api.get(`/api/devices/${id}`)
  },
  createDevice(data) {
    return api.post('/api/devices', data)
  },
  updateDevice(id, data) {
    return api.put(`/api/devices/${id}`, data)
  },
  deleteDevice(id) {
    return api.delete(`/api/devices/${id}`)
  },
  getSingleMachineOutput(limit = 10) {
    return api.get(`/api/devices/stats/single-output?limit=${limit}`)
  },
  getFaultRateStatistics() {
    return api.get('/api/devices/stats/fault-rate')
  },
  getMaintenanceCostAnalysis() {
    return api.get('/api/devices/stats/maintenance-cost')
  },

  // Users
  getUsers(page = 0, size = 10) {
    return api.get(`/api/users?page=${page}&size=${size}`)
  },
  getUser(id) {
    return api.get(`/api/users/${id}`)
  },
  createUser(data) {
    return api.post('/api/users', data)
  },
  updateUser(id, data) {
    return api.put(`/api/users/${id}`, data)
  },
  deleteUser(id) {
    return api.delete(`/api/users/${id}`)
  },
  getActiveUsersStatistics(days = 7) {
    return api.get(`/api/users/stats/active?days=${days}`)
  },
  getRepurchaseRateStatistics() {
    return api.get('/api/users/stats/repurchase-rate')
  },
  getNewUserGrowthTrend() {
    return api.get('/api/users/stats/growth-trend')
  },
  getPurchaseTimeHeatmap(days = 7) {
    return api.get(`/api/users/stats/purchase-heatmap?days=${days}`)
  }
}
