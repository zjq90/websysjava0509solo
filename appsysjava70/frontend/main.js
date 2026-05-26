import Vue from 'vue'
import App from './App'
import api from './common/api.js'
import storage from './common/storage.js'

Vue.config.productionTip = false

Vue.prototype.$api = api
Vue.prototype.$storage = storage

Vue.filter('formatMoney', function(value) {
    if (!value && value !== 0) return '0.00'
    return parseFloat(value).toFixed(2)
})

Vue.filter('formatDate', function(value, format) {
    if (!value) return ''
    const date = new Date(value)
    const y = date.getFullYear()
    const m = String(date.getMonth() + 1).padStart(2, '0')
    const d = String(date.getDate()).padStart(2, '0')
    const h = String(date.getHours()).padStart(2, '0')
    const min = String(date.getMinutes()).padStart(2, '0')
    if (format === 'yyyy-MM-dd') {
        return `${y}-${m}-${d}`
    } else if (format === 'MM-dd') {
        return `${m}-${d}`
    } else if (format === 'HH:mm') {
        return `${h}:${min}`
    } else {
        return `${y}-${m}-${d} ${h}:${min}`
    }
})

Vue.filter('formatBillType', function(value) {
    return value === 'INCOME' ? '收入' : '支出'
})

Vue.filter('formatSyncStatus', function(value) {
    const statusMap = {
        'SYNCED': '已同步',
        'PENDING': '待同步',
        'FAILED': '同步失败'
    }
    return statusMap[value] || value
})

App.mpType = 'app'

const app = new Vue({
    ...App
})
app.$mount()
