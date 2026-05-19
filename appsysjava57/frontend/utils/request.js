/**
 * 统一API请求封装
 * 处理baseURL、请求拦截、响应拦截、错误处理、中文编码等问题
 */

// API基础地址 - 与后端context-path一致
const BASE_URL = 'http://localhost:8080/api'

/**
 * 统一请求方法
 * @param {Object} options - 请求配置
 * @param {String} options.url - 请求地址（不需要包含baseURL）
 * @param {String} options.method - 请求方法（GET/POST/PUT/DELETE等）
 * @param {Object} options.data - 请求数据
 * @param {Object} options.header - 请求头
 * @param {Boolean} options.showLoading - 是否显示加载提示
 * @param {String} options.loadingText - 加载提示文字
 * @param {Boolean} options.showError - 是否显示错误提示
 */
const request = (options = {}) => {
  const { 
    url, 
    method = 'GET', 
    data = {}, 
    header = {}, 
    showLoading = true, 
    loadingText = '加载中...', 
    showError = true 
  } = options

  // 显示加载提示
  if (showLoading) {
    uni.showLoading({
      title: loadingText,
      mask: true
    })
  }

  // 构建完整URL - 自动处理中文编码
  const fullUrl = buildUrl(url, data, method)

  return new Promise((resolve, reject) => {
    uni.request({
      url: fullUrl,
      method: method,
      data: method !== 'GET' ? data : {},
      header: {
        'Content-Type': 'application/json',
        ...header
      },
      success: (res) => {
        uni.hideLoading()
        
        // 响应成功处理
        if (res.statusCode >= 200 && res.statusCode < 300) {
          const result = res.data
          // 业务状态码处理
          if (result.code === 200 || result.code === 0) {
            resolve(result)
          } else {
            if (showError) {
              uni.showToast({
                title: result.message || '请求失败',
                icon: 'none',
                duration: 2000
              })
            }
            reject(result)
          }
        } else {
          const errorMsg = getErrorMsg(res.statusCode)
          if (showError) {
            uni.showToast({
              title: errorMsg,
              icon: 'none',
              duration: 2000
            })
          }
          reject({ code: res.statusCode, message: errorMsg })
        }
      },
      fail: (err) => {
        uni.hideLoading()
        console.error('请求失败:', err)
        
        let errorMsg = '网络请求失败'
        if (err.errMsg) {
          if (err.errMsg.includes('timeout')) {
            errorMsg = '请求超时，请检查网络'
          } else if (err.errMsg.includes('fail')) {
            errorMsg = '连接服务器失败'
          }
        }
        
        if (showError) {
          uni.showToast({
            title: errorMsg,
            icon: 'none',
            duration: 2000
          })
        }
        reject({ code: -1, message: errorMsg, error: err })
      }
    })
  })
}

/**
 * 构建完整URL - 处理中文编码
 */
function buildUrl(url, data, method) {
  // 确保不以 / 开头，避免路径问题
  const cleanUrl = url.startsWith('/') ? url.substring(1) : url
  let fullUrl = `${BASE_URL}/${cleanUrl}`

  // GET请求处理查询参数
  if (method === 'GET' && Object.keys(data).length > 0) {
    const queryString = Object.keys(data)
      .map(key => {
        const value = data[key]
        // 对参数值进行编码，避免中文乱码
        return `${encodeURIComponent(key)}=${encodeURIComponent(value)}`
      })
      .join('&')
    fullUrl += (fullUrl.includes('?') ? '&' : '?') + queryString
  }

  console.log('请求URL:', fullUrl)
  return fullUrl
}

/**
 * 根据状态码获取错误信息
 */
function getErrorMsg(statusCode) {
  const errorMap = {
    400: '请求参数错误',
    401: '未授权，请重新登录',
    403: '拒绝访问',
    404: '请求的资源不存在',
    405: '请求方法不允许',
    408: '请求超时',
    500: '服务器内部错误',
    501: '服务未实现',
    502: '网关错误',
    503: '服务不可用',
    504: '网关超时'
  }
  return errorMap[statusCode] || `请求错误 (${statusCode})`
}

/**
 * 封装常用请求方法
 */
const http = {
  get(url, data = {}, options = {}) {
    return request({
      url,
      method: 'GET',
      data,
      ...options
    })
  },

  post(url, data = {}, options = {}) {
    return request({
      url,
      method: 'POST',
      data,
      ...options
    })
  },

  put(url, data = {}, options = {}) {
    return request({
      url,
      method: 'PUT',
      data,
      ...options
    })
  },

  delete(url, data = {}, options = {}) {
    return request({
      url,
      method: 'DELETE',
      data,
      ...options
    })
  }
}

export default http
export { request, BASE_URL }
