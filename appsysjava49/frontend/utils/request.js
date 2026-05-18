const BASE_URL = 'http://localhost:8080/api'

function request(options) {
  return new Promise((resolve, reject) => {
    const token = uni.getStorageSync('token')
    
    uni.request({
      url: BASE_URL + options.url,
      method: options.method || 'GET',
      data: options.data || {},
      header: {
        'Content-Type': 'application/json',
        'Authorization': token ? `Bearer ${token}` : ''
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
          uni.showToast({
            title: '请先登录',
            icon: 'none'
          })
          uni.navigateTo({
            url: '/pages/login/index'
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
        uni.showToast({
          title: '网络连接失败',
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
