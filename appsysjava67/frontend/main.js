import Vue from 'vue'
import App from './app.vue'

Vue.config.productionTip = false

Vue.prototype.$baseUrl = 'http://localhost:8080/api'

Vue.prototype.$request = function(options) {
    return new Promise((resolve, reject) => {
        uni.request({
            url: Vue.prototype.$baseUrl + options.url,
            method: options.method || 'GET',
            data: options.data || {},
            header: {
                'Content-Type': 'application/json'
            },
            success: (res) => {
                if (res.data.code === 200) {
                    resolve(res.data)
                } else {
                    uni.showToast({
                        title: res.data.message || '请求失败',
                        icon: 'none'
                    })
                    reject(res.data)
                }
            },
            fail: (err) => {
                const unsyncedRecords = uni.getStorageSync('unsyncedRecords') || []
                if (options.method !== 'GET') {
                    unsyncedRecords.push({
                        url: options.url,
                        method: options.method,
                        data: options.data,
                        timestamp: Date.now()
                    })
                    uni.setStorageSync('unsyncedRecords', unsyncedRecords)
                    uni.showToast({
                        title: '网络异常，数据已缓存',
                        icon: 'none'
                    })
                }
                reject(err)
            }
        })
    })
}

Vue.prototype.$syncData = function() {
    return new Promise((resolve, reject) => {
        const unsyncedRecords = uni.getStorageSync('unsyncedRecords') || []
        if (unsyncedRecords.length === 0) {
            resolve()
            return
        }

        let successCount = 0
        const failRecords = []

        unsyncedRecords.forEach((record, index) => {
            uni.request({
                url: Vue.prototype.$baseUrl + record.url,
                method: record.method,
                data: record.data,
                success: () => {
                    successCount++
                },
                fail: () => {
                    failRecords.push(record)
                },
                complete: () => {
                    if (index === unsyncedRecords.length - 1) {
                        uni.setStorageSync('unsyncedRecords', failRecords)
                        if (failRecords.length === 0) {
                            uni.showToast({
                                title: `已同步${successCount}条记录`,
                                icon: 'success'
                            })
                            resolve()
                        } else {
                            uni.showToast({
                                title: `${failRecords.length}条记录同步失败`,
                                icon: 'none'
                            })
                            reject()
                        }
                    }
                }
            })
        })
    })
}

App.mpType = 'app'

const app = new Vue({
    ...App
})
app.$mount()
