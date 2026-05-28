import Vue from 'vue'
import App from './App'
import store from './store'

Vue.config.productionTip = false

Vue.prototype.$store = store

Vue.filter('formatDate', function(date) {
	if (!date) return ''
	const d = new Date(date)
	const year = d.getFullYear()
	const month = String(d.getMonth() + 1).padStart(2, '0')
	const day = String(d.getDate()).padStart(2, '0')
	return `${year}-${month}-${day}`
})

Vue.filter('formatDateTime', function(date) {
	if (!date) return ''
	const d = new Date(date)
	const year = d.getFullYear()
	const month = String(d.getMonth() + 1).padStart(2, '0')
	const day = String(d.getDate()).padStart(2, '0')
	const hour = String(d.getHours()).padStart(2, '0')
	const minute = String(d.getMinutes()).padStart(2, '0')
	return `${year}-${month}-${day} ${hour}:${minute}`
})

Vue.filter('formatFileSize', function(bytes) {
	if (!bytes) return '0B'
	if (bytes < 1024) return bytes + 'B'
	if (bytes < 1024 * 1024) return (bytes / 1024).toFixed(1) + 'KB'
	return (bytes / (1024 * 1024)).toFixed(1) + 'MB'
})

App.mpType = 'app'

const app = new Vue({
	store,
	...App
})
app.$mount()
