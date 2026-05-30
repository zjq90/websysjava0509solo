import request from '@/utils/request'

export function getTodayStats() {
  return request({
    url: '/dashboard/stats',
    method: 'get'
  })
}

export function getTrendData(days) {
  return request({
    url: '/dashboard/trend',
    method: 'get',
    params: { days }
  })
}

export function getDashboardConfigs() {
  return request({
    url: '/dashboard/configs',
    method: 'get'
  })
}

export function saveDashboardConfig(data) {
  return request({
    url: '/dashboard/configs',
    method: 'post',
    data
  })
}

export function deleteDashboardConfig(id) {
  return request({
    url: `/dashboard/configs/${id}`,
    method: 'delete'
  })
}

export function setDefaultDashboard(id) {
  return request({
    url: `/dashboard/configs/${id}/default`,
    method: 'put'
  })
}

export function getDefaultDashboard() {
  return request({
    url: '/dashboard/configs/default',
    method: 'get'
  })
}
