import request from '@/utils/request'

export function login(data) {
  return request({
    url: '/api/auth/login',
    method: 'post',
    data
  })
}

export function getCurrentUser() {
  return request({
    url: '/api/auth/me',
    method: 'get'
  })
}
