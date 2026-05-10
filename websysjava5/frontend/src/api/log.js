import request from '@/utils/request'

export function getLogPage(params) {
  return request({
    url: '/api/operation-logs',
    method: 'get',
    params
  })
}

export function getLogById(id) {
  return request({
    url: `/api/operation-logs/${id}`,
    method: 'get'
  })
}

export function getModules() {
  return request({
    url: '/api/operation-logs/modules',
    method: 'get'
  })
}

export function getOperationTypes() {
  return request({
    url: '/api/operation-logs/operation-types',
    method: 'get'
  })
}
