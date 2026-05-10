import request from '@/utils/request'

export function getConfigPage(params) {
  return request({
    url: '/api/configs',
    method: 'get',
    params
  })
}

export function getConfigById(id) {
  return request({
    url: `/api/configs/${id}`,
    method: 'get'
  })
}

export function getConfigByKey(configKey) {
  return request({
    url: `/api/configs/key/${configKey}`,
    method: 'get'
  })
}

export function createConfig(data) {
  return request({
    url: '/api/configs',
    method: 'post',
    data
  })
}

export function updateConfig(data) {
  return request({
    url: '/api/configs',
    method: 'put',
    data
  })
}

export function deleteConfig(id) {
  return request({
    url: `/api/configs/${id}`,
    method: 'delete'
  })
}

export function getConfigsByGroup(configGroup) {
  return request({
    url: `/api/configs/group/${configGroup}`,
    method: 'get'
  })
}

export function getConfigGroups() {
  return request({
    url: '/api/configs/groups',
    method: 'get'
  })
}

export function getConfigTypes() {
  return request({
    url: '/api/configs/types',
    method: 'get'
  })
}
