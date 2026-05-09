
export const BASE_URL = 'http://localhost:8080/api'

export function request(options) {
    return new Promise((resolve, reject) => {
        const token = uni.getStorageSync('token')
        
        uni.showLoading({ title: '加载中...' })
        
        uni.request({
            url: BASE_URL + options.url,
            method: options.method || 'GET',
            data: options.data || {},
            header: {
                'Content-Type': 'application/json',
                ...(token ? { 'Authorization': 'Bearer ' + token } : {})
            },
            success: (res) => {
                uni.hideLoading()
                
                if (res.statusCode === 200) {
                    if (res.data.code === 200) {
                        resolve(res.data.data)
                    } else {
                        uni.showToast({
                            title: res.data.message || '请求失败',
                            icon: 'none'
                        })
                        reject(res.data)
                    }
                } else if (res.statusCode === 401) {
                    uni.showToast({
                        title: '请重新登录',
                        icon: 'none'
                    })
                    uni.removeStorageSync('token')
                    uni.removeStorageSync('userInfo')
                    setTimeout(() => {
                        uni.navigateTo({ url: '/pages/login/login' })
                    }, 1500)
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
                uni.hideLoading()
                uni.showToast({
                    title: '网络请求失败',
                    icon: 'none'
                })
                reject(err)
            }
        })
    })
}

export function uploadFile(options) {
    return new Promise((resolve, reject) => {
        const token = uni.getStorageSync('token')
        
        uni.showLoading({ title: '上传中...' })
        
        uni.uploadFile({
            url: BASE_URL + options.url,
            filePath: options.filePath,
            name: options.name || 'file',
            formData: options.formData || {},
            header: {
                ...(token ? { 'Authorization': 'Bearer ' + token } : {})
            },
            success: (res) => {
                uni.hideLoading()
                try {
                    const data = JSON.parse(res.data)
                    if (data.code === 200) {
                        resolve(data.data)
                    } else {
                        uni.showToast({
                            title: data.message || '上传失败',
                            icon: 'none'
                        })
                        reject(data)
                    }
                } catch (e) {
                    reject(res)
                }
            },
            fail: (err) => {
                uni.hideLoading()
                uni.showToast({
                    title: '上传失败',
                    icon: 'none'
                })
                reject(err)
            }
        })
    })
}
