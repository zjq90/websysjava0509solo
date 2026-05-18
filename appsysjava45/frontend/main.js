import App from './App'
import request from './utils/request.js'

// #ifndef VUE3
import Vue from 'vue'
Vue.config.productionTip = false

// 挂载全局方法
Vue.prototype.$request = request
Vue.prototype.$baseUrl = '/api'

App.mpType = 'app'
const app = new Vue({
    ...App
})
app.$mount()
// #endif

// #ifdef VUE3
import { createSSRApp } from 'vue'
export function createApp() {
  const app = createSSRApp(App)
  app.config.globalProperties.$request = request
  app.config.globalProperties.$baseUrl = '/api'
  return {
    app
  }
}
// #endif