import request from '@/utils/request'

export function getUserList(params) {
  return request({
    url: '/user/page',
    method: 'get',
    params
  })
}

export function getUser(id) {
  return request({
    url: `/user/${id}`,
    method: 'get'
  })
}

export function createUser(data) {
  return request({
    url: '/user',
    method: 'post',
    data
  })
}

export function updateUser(data) {
  return request({
    url: '/user',
    method: 'put',
    data
  })
}

export function deleteUser(id) {
  return request({
    url: `/user/${id}`,
    method: 'delete'
  })
}

export function updateUserStatus(id, status) {
  return request({
    url: `/user/${id}/status/${status}`,
    method: 'put'
  })
}

export function login(data) {
  return request({
    url: '/user/login',
    method: 'post',
    data
  })
}

export function getUserMenus(userId) {
  return request({
    url: `/user/${userId}/menus`,
    method: 'get'
  })
}

export function getUserPermissions(userId) {
  return request({
    url: `/user/${userId}/permissions`,
    method: 'get'
  })
}
