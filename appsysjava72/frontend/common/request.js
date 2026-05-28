import config from './config'

const request = (options) => {
  return new Promise((resolve, reject) => {
    const token = uni.getStorageSync(config.tokenKey)
    
    uni.request({
      url: config.baseUrl + options.url,
      method: options.method || 'GET',
      data: options.data || {},
      timeout: config.timeout,
      header: {
        'Content-Type': options.contentType || 'application/json',
        'Authorization': token ? 'Bearer ' + token : ''
      },
      success: (res) => {
        if (res.statusCode === 200) {
          if (res.data.code === 200) {
            resolve(res.data)
          } else if (res.data.code === 401) {
            uni.removeStorageSync(config.tokenKey)
            uni.removeStorageSync(config.userKey)
            uni.showToast({
              title: '登录已过期，请重新登录',
              icon: 'none'
            })
            setTimeout(() => {
              uni.reLaunch({
                url: '/pages/login/login'
              })
            }, 1500)
            reject(res.data)
          } else {
            uni.showToast({
              title: res.data.message || '请求失败',
              icon: 'none'
            })
            reject(res.data)
          }
        } else {
          uni.showToast({
            title: '网络错误: ' + res.statusCode,
            icon: 'none'
          })
          reject(res)
        }
      },
      fail: (err) => {
        if (err.errMsg.indexOf('timeout') !== -1) {
          uni.showToast({
            title: '请求超时，请检查网络',
            icon: 'none'
          })
        } else {
          uni.showToast({
            title: '网络连接失败',
            icon: 'none'
          })
        }
        reject(err)
      }
    })
  })
}

export default request
