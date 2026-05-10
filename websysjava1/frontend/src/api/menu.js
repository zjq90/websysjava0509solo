import request from '@/utils/request'

export function getMenuList() {
  return request({
    url: '/menu/list',
    method: 'get'
  })
}

export function getMenu(id) {
  return request({
    url: `/menu/${id}`,
    method: 'get'
  })
}

export function createMenu(data) {
  return request({
    url: '/menu',
    method: 'post',
    data
  })
}

export function updateMenu(data) {
  return request({
    url: '/menu',
    method: 'put',
    data
  })
}

export function deleteMenu(id) {
  return request({
    url: `/menu/${id}`,
    method: 'delete'
  })
}

export function getPermissionList() {
  return request({
    url: '/permission/list',
    method: 'get'
  })
}

export function createPermission(data) {
  return request({
    url: '/permission',
    method: 'post',
    data
  })
}

export function updatePermission(data) {
  return request({
    url: '/permission',
    method: 'put',
    data
  })
}

export function deletePermission(id) {
  return request({
    url: `/permission/${id}`,
    method: 'delete'
  })
}
