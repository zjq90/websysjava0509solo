import Vue from 'vue'
import App from './App'
import store from './store'
import { BASE_URL, request, uploadFile } from './utils/request'

Vue.config.productionTip = false

Vue.prototype.$store = store
Vue.prototype.$request = request
Vue.prototype.$uploadFile = uploadFile
Vue.prototype.$BASE_URL = BASE_URL

App.mpType = 'app'

const app = new Vue({
    store,
    ...App
})
app.$mount()
