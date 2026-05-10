import axios from 'axios'
import { Message } from 'element-ui'
import store from '@/store'
import router from '@/router'

const request = axios.create({
  baseURL: '/',
  timeout: 30000
})

request.interceptors.request.use(
  config => {
    const token = store.getters.token
    if (token) {
      config.headers['Authorization'] = 'Bearer ' + token
    }
    return config
  },
  error => {
    return Promise.reject(error)
  }
)

request.interceptors.response.use(
  response => {
    return response.data
  },
  error => {
    if (error.response) {
      const status = error.response.status
      if (status === 401) {
        store.dispatch('Logout')
        router.push('/login')
        Message.error('登录已过期，请重新登录')
      } else if (status === 403) {
        Message.error('没有权限访问')
      } else if (status === 500) {
        const data = error.response.data
        Message.error(data.message || '服务器内部错误')
      } else {
        Message.error(error.message)
      }
    } else {
      Message.error('网络错误，请稍后重试')
    }
    return Promise.reject(error)
  }
)

export default request
