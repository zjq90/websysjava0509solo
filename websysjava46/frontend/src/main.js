import Vue from 'vue'
import App from './App.vue'
import router from './router'
import ElementUI from 'element-ui'
import 'element-ui/lib/theme-chalk/index.css'
import axios from 'axios'

Vue.config.productionTip = false

Vue.use(ElementUI)

axios.defaults.baseURL = process.env.NODE_ENV === 'production' 
  ? 'http://localhost:8080' 
  : '/api'
axios.defaults.timeout = 10000

axios.interceptors.response.use(
  response => response,
  error => {
    console.error('API Error:', error)
    ElementUI.Message.error('网络请求失败，请检查后端服务是否启动')
    return Promise.reject(error)
  }
)

Vue.prototype.$http = axios

new Vue({
  router,
  render: h => h(App)
}).$mount('#app')
