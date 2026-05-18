import { createSSRApp } from 'vue'
import App from './App.vue'

export function createApp() {
  const app = createSSRApp(App)
  
  app.config.globalProperties.$api = 'http://localhost:8080/api'
  
  app.config.globalProperties.$request = function(options) {
    return new Promise((resolve, reject) => {
      const token = uni.getStorageSync('token')
      uni.request({
        url: app.config.globalProperties.$api + options.url,
        method: options.method || 'GET',
        data: options.data || {},
        header: {
          'Authorization': token || '',
          'Content-Type': 'application/json'
        },
        success: (res) => {
          if (res.data.code === 200) {
            resolve(res.data)
          } else if (res.data.code === 401) {
            uni.removeStorageSync('token')
            uni.removeStorageSync('user')
            uni.reLaunch({
              url: '/pages/user/login'
            })
            reject(res.data)
          } else {
            uni.showToast({
              title: res.data.message || '请求失败',
              icon: 'none'
            })
            reject(res.data)
          }
        },
        fail: (err) => {
          uni.showToast({
            title: '网络错误',
            icon: 'none'
          })
          reject(err)
        }
      })
    })
  }
  
  return {
    app
  }
}
