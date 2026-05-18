/**
 * API请求工具类
 * 封装uni.request，支持RSA加密敏感数据
 */

import { encryptSensitiveFields } from './rsa.js'

const BASE_URL = 'http://localhost:8080/api'

/**
 * 核心请求函数
 */
async function request(options) {
  const { url, method = 'GET', data, header = {}, encrypt = true } = options
  
  // 添加token
  const token = uni.getStorageSync('token')
  if (token) {
    header['Authorization'] = `Bearer ${token}`
  }
  
  // 加密敏感数据（POST/PUT请求）
  let requestData = data
  if (encrypt && (method === 'POST' || method === 'PUT') && data) {
    requestData = await encryptSensitiveFields(data)
  }
  
  return new Promise((resolve, reject) => {
    uni.request({
      url: BASE_URL + url,
      method,
      data: requestData,
      header: {
        'Content-Type': 'application/json',
        ...header
      },
      success: (res) => {
        if (res.statusCode === 200) {
          if (res.data.success || res.data.code === 200) {
            resolve(res.data)
          } else {
            uni.showToast({
              title: res.data.message || res.data.msg || '请求失败',
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
  get(url, data, options = {}) {
    return request({ url, method: 'GET', data, ...options })
  },
  post(url, data, options = {}) {
    return request({ url, method: 'POST', data, ...options })
  },
  put(url, data, options = {}) {
    return request({ url, method: 'PUT', data, ...options })
  },
  delete(url, data, options = {}) {
    return request({ url, method: 'DELETE', data, ...options })
  },
  /**
   * 不加密的请求方法
   */
  postWithoutEncrypt(url, data) {
    return request({ url, method: 'POST', data, encrypt: false })
  }
}