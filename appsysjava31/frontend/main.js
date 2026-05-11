import Vue from 'vue'
import App from './App'

Vue.config.productionTip = false

Vue.prototype.$baseUrl = 'http://localhost:8080/api'

Vue.prototype.$request = function(options) {
    return new Promise((resolve, reject) => {
        const token = uni.getStorageSync('token')
        
        uni.request({
            url: Vue.prototype.$baseUrl + options.url,
            method: options.method || 'GET',
            data: options.data || {},
            header: {
                'Content-Type': 'application/json',
                'Authorization': token ? 'Bearer ' + token : ''
            },
            success: (res) => {
                if (res.statusCode === 200) {
                    if (res.data.code === 200) {
                        resolve(res.data)
                    } else if (res.statusCode === 401 || res.data.code === 401) {
                        uni.removeStorageSync('token')
                        uni.removeStorageSync('userInfo')
                        uni.reLaunch({
                            url: '/pages/login/login'
                        })
                        reject(new Error('请重新登录'))
                    } else {
                        uni.showToast({
                            title: res.data.message || '请求失败',
                            icon: 'none'
                        })
                        reject(new Error(res.data.message))
                    }
                } else {
                    reject(new Error('请求失败'))
                }
            },
            fail: (err) => {
                uni.showToast({
                    title: '网络异常，请检查网络',
                    icon: 'none'
                })
                reject(err)
            }
        })
    })
}

Vue.prototype.$showLoading = function(title) {
    uni.showLoading({
        title: title || '加载中',
        mask: true
    })
}

Vue.prototype.$hideLoading = function() {
    uni.hideLoading()
}

App.mpType = 'app'

const app = new Vue({
    ...App
})
app.$mount()
