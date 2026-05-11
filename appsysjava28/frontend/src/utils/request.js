const BASE_URL = 'http://localhost:8080/api'

function request(options) {
    return new Promise((resolve, reject) => {
        const token = uni.getStorageSync('token')
        let header = {
            'Content-Type': 'application/json'
        }
        
        if (token) {
            header['Authorization'] = 'Bearer ' + token
        }

        uni.request({
            url: BASE_URL + options.url,
            method: options.method || 'GET',
            data: options.data,
            header: header,
            timeout: 10000,
            success: (res) => {
                if (res.statusCode === 200) {
                    const data = res.data
                    if (data.code === 200) {
                        resolve(data)
                    } else if (data.code === 401) {
                        uni.removeStorageSync('token')
                        uni.removeStorageSync('userInfo')
                        uni.reLaunch({
                            url: '/pages/login/login'
                        })
                        reject(new Error('登录已过期，请重新登录'))
                    } else {
                        uni.showToast({
                            title: data.message || '请求失败',
                            icon: 'none'
                        })
                        reject(new Error(data.message))
                    }
                } else if (res.statusCode === 401) {
                    uni.removeStorageSync('token')
                    uni.removeStorageSync('userInfo')
                    uni.reLaunch({
                        url: '/pages/login/login'
                    })
                    reject(new Error('登录已过期，请重新登录'))
                } else {
                    uni.showToast({
                        title: '网络请求失败',
                        icon: 'none'
                    })
                    reject(new Error('网络请求失败'))
                }
            },
            fail: (err) => {
                console.error('请求失败:', err)
                uni.showToast({
                    title: '网络异常，请检查网络连接',
                    icon: 'none'
                })
                reject(err)
            }
        })
    })
}

export default {
    get(url, data) {
        return request({
            url,
            method: 'GET',
            data
        })
    },
    post(url, data) {
        return request({
            url,
            method: 'POST',
            data
        })
    },
    put(url, data) {
        return request({
            url,
            method: 'PUT',
            data
        })
    },
    delete(url, data) {
        return request({
            url,
            method: 'DELETE',
            data
        })
    }
}
