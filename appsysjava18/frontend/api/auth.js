/**
 * 认证相关API
 */
import request from '@/utils/request'

// 登录
export function login(data) {
  return request.post('/api/auth/login', data)
}

// 获取当前用户信息
export function getCurrentUser() {
  return request.get('/api/auth/current')
}

// 登出
export function logout() {
  return request.post('/api/auth/logout')
}
