import request from '@/utils/request'

export function login(data) {
  return request({
    url: '/api/user/login',
    method: 'post',
    data
  })
}

export function register(data) {
  return request({
    url: '/api/user/register',
    method: 'post',
    data
  })
}

export function getUserInfo() {
  return request({
    url: '/api/user/info',
    method: 'get'
  })
}

export function updateUserInfo(data) {
  return request({
    url: '/api/user/info',
    method: 'put',
    data
  })
}

export function changePassword(data) {
  return request({
    url: '/api/user/password',
    method: 'put',
    data
  })
}

export function sendResetCode(email) {
  return request({
    url: '/api/user/password/reset-code',
    method: 'post',
    params: { email }
  })
}

export function resetPassword(data) {
  return request({
    url: '/api/user/password/reset',
    method: 'post',
    data
  })
}

export function bindEmail(data) {
  return request({
    url: '/api/user/email/bind',
    method: 'post',
    params: data
  })
}

export function unbindEmail() {
  return request({
    url: '/api/user/email/unbind',
    method: 'post'
  })
}
