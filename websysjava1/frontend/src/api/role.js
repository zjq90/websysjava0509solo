import request from '@/utils/request'

export function getRoleList(params) {
  return request({
    url: '/role/page',
    method: 'get',
    params
  })
}

export function getAllRoles() {
  return request({
    url: '/role/list',
    method: 'get'
  })
}

export function getRole(id) {
  return request({
    url: `/role/${id}`,
    method: 'get'
  })
}

export function createRole(data) {
  return request({
    url: '/role',
    method: 'post',
    data
  })
}

export function updateRole(data) {
  return request({
    url: '/role',
    method: 'put',
    data
  })
}

export function deleteRole(id) {
  return request({
    url: `/role/${id}`,
    method: 'delete'
  })
}

export function getRoleMenus(roleId) {
  return request({
    url: `/role/${roleId}/menus`,
    method: 'get'
  })
}

export function assignRoleMenus(roleId, menuIds) {
  return request({
    url: `/role/${roleId}/menus`,
    method: 'post',
    data: { menuIds }
  })
}

export function getRolePermissions(roleId) {
  return request({
    url: `/role/${roleId}/permissions`,
    method: 'get'
  })
}

export function assignRolePermissions(roleId, permissionIds) {
  return request({
    url: `/role/${roleId}/permissions`,
    method: 'post',
    data: { permissionIds }
  })
}
