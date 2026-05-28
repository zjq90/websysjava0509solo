import Vue from 'vue'
import App from './App.vue'
import store from './store'
/* import uviewPlus from 'uview-plus' */

export function createApp() {
  const app = new Vue({
    store,
    render: h => h(App)
  })
  
  /* app.use(uviewPlus) */
  
  return {
    app
  }
}
