const BASE_URL = '/api'

const request = (options) => {
  return new Promise((resolve, reject) => {
    const token = uni.getStorageSync('token')
    
    const header = {
      'Content-Type': 'application/json'
    }
    
    if (token) {
      header['Authorization'] = `Bearer ${token}`
    }
    
    uni.request({
      url: BASE_URL + options.url,
      method: options.method || 'GET',
      data: options.data,
      header: header,
      success: (res) => {
        if (res.statusCode === 200) {
          const data = res.data
          if (data.code === 200) {
            resolve(data.data)
          } else if (data.code === 401) {
            uni.removeStorageSync('token')
            uni.removeStorageSync('userInfo')
            uni.reLaunch({
              url: '/pages/login/login'
            })
            reject(new Error('登录已过期'))
          } else {
            uni.showToast({
              title: data.message || '请求失败',
              icon: 'none'
            })
            reject(new Error(data.message))
          }
        } else {
          uni.showToast({
            title: '网络异常，请稍后重试',
            icon: 'none'
          })
          reject(new Error('网络异常'))
        }
      },
      fail: (err) => {
        console.error('请求失败:', err)
        uni.showToast({
          title: '网络异常，请稍后重试',
          icon: 'none'
        })
        reject(err)
      }
    })
  })
}

export const http = {
  get: (url, data) => request({ url, method: 'GET', data }),
  post: (url, data) => request({ url, method: 'POST', data }),
  put: (url, data) => request({ url, method: 'PUT', data }),
  delete: (url, data) => request({ url, method: 'DELETE', data })
}

export default request
