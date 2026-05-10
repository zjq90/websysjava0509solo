const baseUrl = 'http://localhost:8080'

function request(options) {
  return new Promise((resolve, reject) => {
    uni.showLoading({ title: '加载中...' })
    
    uni.request({
      url: baseUrl + options.url,
      method: options.method || 'GET',
      data: options.data,
      header: {
        'Content-Type': options.contentType || 'application/json',
        ...options.header
      },
      success: (res) => {
        uni.hideLoading()
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
  
  post(url, data, contentType) {
    return request({
      url,
      method: 'POST',
      data,
      contentType
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
