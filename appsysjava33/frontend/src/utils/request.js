const BASE_URL = 'http://localhost:8080/api'

const request = (options) => {
    return new Promise((resolve, reject) => {
        const token = uni.getStorageSync('token')
        uni.request({
            url: BASE_URL + options.url,
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
                    } else {
                        uni.showToast({
                            title: res.data.message || '请求失败',
                            icon: 'none'
                        })
                        reject(res.data)
                    }
                } else if (res.statusCode === 401) {
                    uni.removeStorageSync('token')
                    uni.removeStorageSync('userInfo')
                    uni.redirectTo({
                        url: '/pages/login/login'
                    })
                    reject(res)
                } else {
                    uni.showToast({
                        title: '网络错误',
                        icon: 'none'
                    })
                    reject(res)
                }
            },
            fail: (err) => {
                console.error('请求失败:', err)
                uni.showToast({
                    title: '网络连接失败',
                    icon: 'none'
                })
                reject(err)
            }
        })
    })
}

export default request
