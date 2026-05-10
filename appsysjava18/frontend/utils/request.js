/**
 * API请求工具类
 * 封装uni.request，统一处理请求和响应
 */

const BASE_URL = 'http://localhost:8080'

// 获取Token
function getToken() {
  return uni.getStorageSync('token') || ''
}

// 显示加载提示
function showLoading() {
  uni.showLoading({
    title: '加载中...',
    mask: true
  })
}

// 隐藏加载提示
function hideLoading() {
  uni.hideLoading()
}

// 显示错误提示
function showError(message) {
  uni.showToast({
    title: message || '请求失败',
    icon: 'none',
    duration: 2000
  })
}

/**
 * 统一请求方法
 * @param {Object} options 请求配置
 * @param {Boolean} showLoadingFlag 是否显示加载提示
 */
function request(options, showLoadingFlag = true) {
  return new Promise((resolve, reject) => {
    if (showLoadingFlag) {
      showLoading()
    }

    uni.request({
      url: BASE_URL + options.url,
      method: options.method || 'GET',
      data: options.data,
      header: {
        'Content-Type': 'application/json',
        'Authorization': 'Bearer ' + getToken()
      },
      success: (res) => {
        if (showLoadingFlag) {
          hideLoading()
        }

        if (res.statusCode === 200) {
          const data = res.data
          if (data.code === 200) {
            resolve(data.data)
          } else if (data.code === 401) {
            // Token过期，清除缓存并跳转到登录页
            uni.removeStorageSync('token')
            uni.removeStorageSync('userInfo')
            showError('登录已过期，请重新登录')
            setTimeout(() => {
              uni.reLaunch({
                url: '/pages/login/login'
              })
            }, 1500)
            reject(data)
          } else {
            showError(data.message || '操作失败')
            reject(data)
          }
        } else if (res.statusCode === 401) {
          uni.removeStorageSync('token')
          uni.removeStorageSync('userInfo')
          showError('登录已过期，请重新登录')
          setTimeout(() => {
            uni.reLaunch({
              url: '/pages/login/login'
            })
          }, 1500)
          reject(res)
        } else {
          showError('请求失败，状态码：' + res.statusCode)
          reject(res)
        }
      },
      fail: (err) => {
        if (showLoadingFlag) {
          hideLoading()
        }
        showError('网络请求失败，请检查网络连接')
        reject(err)
      }
    })
  })
}

// 导出常用方法
export default {
  get: (url, data, showLoading = true) => request({ url, method: 'GET', data }, showLoading),
  post: (url, data, showLoading = true) => request({ url, method: 'POST', data }, showLoading),
  put: (url, data, showLoading = true) => request({ url, method: 'PUT', data }, showLoading),
  delete: (url, data, showLoading = true) => request({ url, method: 'DELETE', data }, showLoading),
  BASE_URL
}
