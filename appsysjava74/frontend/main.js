import { createSSRApp } from 'vue'
import App from './App.vue'
import uviewPlus from 'uview-plus'
import 'uview-plus/index.scss'
import store from './store'

export function createApp() {
    const app = createSSRApp(App)
    
    app.use(store)
    app.use(uviewPlus)
    
    uviewPlus.setConfig({
        config: {
            unit: 'rpx'
        }
    })
    
    uni.$u.http.setConfig({
        baseUrl: 'http://localhost:8080/api',
        method: 'GET',
        dataType: 'json',
        showLoading: true,
        loadingText: '请求中...',
        loadingTime: 800,
        originalData: false,
        header: {
            'Content-Type': 'application/json'
        }
    })
    
    uni.$u.http.interceptors.request.use((config) => {
        const token = uni.getStorageSync('token')
        if (token) {
            config.header.Authorization = 'Bearer ' + token
        }
        return config
    }, (config) => {
        return Promise.reject(config)
    })
    
    uni.$u.http.interceptors.response.use((response) => {
        const data = response.data
        if (data.code === 401) {
            uni.removeStorageSync('token')
            uni.removeStorageSync('userInfo')
            uni.showToast({
                title: '登录已过期，请重新登录',
                icon: 'none'
            })
            setTimeout(() => {
                uni.reLaunch({
                    url: '/pages/login/login'
                })
            }, 1500)
            return Promise.reject(data)
        }
        if (data.code !== 200 && data.code !== 0) {
            uni.showToast({
                title: data.message || '请求失败',
                icon: 'none'
            })
            return Promise.reject(data)
        }
        return data.data || data
    }, (response) => {
        uni.showToast({
            title: '网络请求失败',
            icon: 'none'
        })
        return Promise.reject(response)
    })
    
    return {
        app
    }
}
