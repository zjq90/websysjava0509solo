import { createApp } from 'vue'
import App from './App.vue'
import router from './router'

const app = createApp(App)

app.use(router)

app.config.globalProperties.$api = 'http://localhost:8080/api'

app.config.globalProperties.$request = function(options) {
  return new Promise((resolve, reject) => {
    const token = localStorage.getItem('token')
    const url = app.config.globalProperties.$api + options.url
    const headers = {
      'Content-Type': 'application/json'
    }
    if (token) {
      headers['Authorization'] = 'Bearer ' + token
    }
    
    fetch(url, {
      method: options.method || 'GET',
      headers: headers,
      body: options.data ? JSON.stringify(options.data) : undefined
    })
    .then(res => {
      if (!res.ok) {
        throw new Error(`HTTP error! status: ${res.status}`)
      }
      return res.json()
    })
    .then(data => {
      if (data.success) {
        resolve(data.data)
      } else {
        console.warn('API returned error:', data.message)
        reject(data)
      }
    })
    .catch(err => {
      console.warn('Request failed:', err)
      reject(err)
    })
  })
}

app.mount('#app')
