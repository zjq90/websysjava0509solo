const BASE_URL = 'http://localhost:8080/api'

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
      data: options.data,
      header: header,
      success: (res) => {
        if (res.statusCode === 200) {
          if (res.data.code === 200) {
            resolve(res.data)
          } else if (res.data.code === 401) {
            uni.removeStorageSync('token')
            uni.removeStorageSync('userInfo')
            uni.showToast({
              title: '请重新登录',
              icon: 'none'
            })
            setTimeout(() => {
              uni.reLaunch({
                url: '/pages/login/login'
              })
            }, 1000)
            reject(res.data)
          } else {
            uni.showToast({
              title: res.data.message || '操作失败',
              icon: 'none'
            })
            reject(res.data)
          }
        } else if (res.statusCode === 401) {
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
          }, 1000)
          reject(res)
        } else if (res.statusCode === 403) {
          uni.showToast({
            title: '权限不足',
            icon: 'none'
          })
          reject(res)
        } else {
          uni.showToast({
            title: '网络异常，请稍后重试',
            icon: 'none'
          })
          reject(res)
        }
      },
      fail: (err) => {
        console.error('请求失败:', err)
        uni.showToast({
          title: '网络连接失败，请检查网络',
          icon: 'none'
        })
        reject(err)
      }
    })
  })
}

export default request
