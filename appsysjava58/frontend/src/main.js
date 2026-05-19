import { createSSRApp } from 'vue'
import App from './App.vue'

export function createApp() {
  const app = createSSRApp(App)
  
  app.config.globalProperties.$baseUrl = 'http://localhost:8080/api'
  
  app.config.globalProperties.$request = function(url, method = 'GET', data = {}) {
    return new Promise((resolve, reject) => {
      uni.request({
        url: this.$baseUrl + url,
        method: method,
        data: data,
        header: {
          'Content-Type': 'application/json'
        },
        success: (res) => {
          if (res.statusCode === 200) {
            resolve(res.data)
          } else {
            reject(res)
          }
        },
        fail: (err) => {
          reject(err)
        }
      })
    })
  }
  
  app.config.globalProperties.$showToast = function(title, icon = 'none') {
    uni.showToast({
      title: title,
      icon: icon,
      duration: 2000
    })
  }
  
  return {
    app
  }
}