const BASE_URL = '/api'

function request(options) {
    return new Promise((resolve, reject) => {
        const token = uni.getStorageSync('token')
        const header = {
            'Content-Type': 'application/json'
        }
        if (token) {
            header['Authorization'] = 'Bearer ' + token
        }

        uni.request({
            url: BASE_URL + options.url,
            method: options.method || 'GET',
            data: options.data || {},
            header: header,
            timeout: 30000,
            success: (res) => {
                if (res.statusCode === 200) {
                    if (res.data.code === 200) {
                        resolve(res.data)
                    } else if (res.data.code === 401) {
                        uni.removeStorageSync('token')
                        uni.removeStorageSync('userInfo')
                        uni.reLaunch({ url: '/pages/login/login' })
                        reject(new Error('未授权'))
                    } else if (res.data.code === 403) {
                        uni.showToast({
                            title: '权限不足',
                            icon: 'none'
                        })
                        reject(new Error(res.data.message || '权限不足'))
                    } else {
                        uni.showToast({
                            title: res.data.message || '操作失败',
                            icon: 'none'
                        })
                        reject(new Error(res.data.message || '操作失败'))
                    }
                } else {
                    showError(res.statusCode)
                    reject(new Error('请求失败'))
                }
            },
            fail: (err) => {
                if (err.errMsg && err.errMsg.includes('timeout')) {
                    uni.showToast({
                        title: '请求超时，请检查网络',
                        icon: 'none'
                    })
                } else {
                    uni.showToast({
                        title: '网络连接异常',
                        icon: 'none'
                    })
                }
                reject(err)
            }
        })
    })
}

function showError(statusCode) {
    const messages = {
        400: '请求参数错误',
        401: '请重新登录',
        403: '没有权限访问',
        404: '请求的资源不存在',
        500: '服务器内部错误',
        502: '服务器暂时不可用',
        503: '服务器维护中'
    }
    const message = messages[statusCode] || '网络请求失败，请稍后重试'
    uni.showToast({
        title: message,
        icon: 'none'
    })
}

export default {
    get(url, data) {
        return request({ url, method: 'GET', data })
    },
    post(url, data) {
        return request({ url, method: 'POST', data })
    },
    put(url, data) {
        return request({ url, method: 'PUT', data })
    },
    delete(url, data) {
        return request({ url, method: 'DELETE', data })
    }
}
