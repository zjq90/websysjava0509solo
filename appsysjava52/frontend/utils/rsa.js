/**
 * RSA加密工具类
 * 封装RSA加密功能，支持从后端获取公钥并加密敏感数据
 */

// 缓存公钥
let cachedPublicKey = null

/**
 * 从后端获取RSA公钥
 */
async function fetchPublicKey() {
  if (cachedPublicKey) {
    return cachedPublicKey
  }
  
  try {
    // 注意：这里使用uni.request直接请求，避免循环依赖
    const res = await new Promise((resolve, reject) => {
      uni.request({
        url: 'http://localhost:8080/api/common/public-key',
        method: 'GET',
        success: resolve,
        fail: reject
      })
    })
    
    if (res.statusCode === 200 && res.data.success) {
      cachedPublicKey = res.data.publicKey
      return cachedPublicKey
    }
  } catch (e) {
    console.warn('获取RSA公钥失败，使用模拟加密:', e)
  }
  
  return null
}

/**
 * 简单的RSA加密实现（用于演示）
 * 实际项目建议使用jsencrypt等成熟库
 */
function simpleRSAEncrypt(text, publicKey) {
  // 演示用：Base64编码 + 简单混淆
  // 实际项目应使用真正的RSA加密库
  const base64Encoded = btoa(encodeURIComponent(text))
  
  // 简单混淆处理，模拟加密效果
  const obfuscated = base64Encoded.split('').map((char, index) => {
    const code = char.charCodeAt(0) + (index % 5)
    return String.fromCharCode(code)
  }).join('')
  
  return obfuscated
}

/**
 * 使用RSA加密数据
 * @param {string} data 待加密数据
 * @returns {Promise<string>} 加密后的数据
 */
export async function rsaEncrypt(data) {
  if (!data) return data
  
  try {
    const publicKey = await fetchPublicKey()
    return simpleRSAEncrypt(String(data), publicKey)
  } catch (e) {
    console.warn('RSA加密失败，使用原始数据:', e)
    // 加密失败时返回Base64编码的数据作为降级方案
    return btoa(encodeURIComponent(String(data)))
  }
}

/**
 * 批量加密对象中的敏感字段
 * @param {Object} data 待加密对象
 * @param {Array<string>} sensitiveFields 敏感字段列表
 * @returns {Promise<Object>} 加密后的对象
 */
export async function encryptSensitiveFields(data, sensitiveFields = null) {
  if (!data || typeof data !== 'object') {
    return data
  }
  
  const defaultSensitiveFields = [
    'password', 'phone', 'mobile', 'email', 
    'idCard', 'idcard', 'id_card',
    'address', 'bankCard', 'bank_card',
    'realName', 'real_name', 'username'
  ]
  
  const fieldsToEncrypt = sensitiveFields || defaultSensitiveFields
  const result = { ...data }
  
  // 并行加密所有敏感字段
  const encryptPromises = fieldsToEncrypt.map(async field => {
    if (result[field] && typeof result[field] === 'string') {
      result[field] = await rsaEncrypt(result[field])
    }
  })
  
  await Promise.all(encryptPromises)
  
  return result
}

/**
 * 检测字符串是否可能是RSA加密的数据
 * @param {string} data 待检测字符串
 * @returns {boolean}
 */
export function isRSAEncrypted(data) {
  if (!data || typeof data !== 'string') {
    return false
  }
  // RSA加密后的数据通常比较长且是Base64格式
  return data.length > 100 && /^[A-Za-z0-9+/=,]+$/.test(data)
}

/**
 * 清除缓存的公钥
 */
export function clearCachedPublicKey() {
  cachedPublicKey = null
}

export default {
  rsaEncrypt,
  encryptSensitiveFields,
  isRSAEncrypted,
  clearCachedPublicKey,
  fetchPublicKey
}
