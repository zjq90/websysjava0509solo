import Vue from 'vue'
import axios from 'axios'

const service = axios.create({
  baseURL: '/api',
  timeout: 10000
})

service.interceptors.response.use(
  response => {
    const res = response.data
    if (res.code !== 200) {
      Vue.prototype.$message.error(res.message || '请求失败')
      return Promise.reject(new Error(res.message || '请求失败'))
    }
    return res.data
  },
  error => {
    Vue.prototype.$message.error(error.message || '网络错误')
    return Promise.reject(error)
  }
)

export const api = {
  getUserStats(params) {
    return service.get('/analytics/users', { params })
  },
  getGameStats(params) {
    return service.get('/analytics/games', { params })
  },
  getRevenueStats(params) {
    return service.get('/analytics/revenue', { params })
  },
  getGameDetailStats(gameId, params) {
    return service.get(`/analytics/games/${gameId}`, { params })
  },
  compareGames(gameIds, params) {
    return service.post('/analytics/games/compare', gameIds, { params })
  },
  searchGames(keyword) {
    return service.get('/analytics/games/search', { params: { keyword } })
  },
  getSystemConfig() {
    return service.get('/system-config')
  },
  updateSystemConfig(data) {
    return service.put('/system-config', data)
  }
}

Vue.prototype.$api = api

export default service
