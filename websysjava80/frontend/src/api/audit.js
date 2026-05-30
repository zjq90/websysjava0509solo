import request from '@/utils/request'

export function auditGame(data) {
  return request({
    url: '/audit',
    method: 'post',
    data
  })
}

export function autoAudit(gameId) {
  return request({
    url: `/audit/auto/${gameId}`,
    method: 'post'
  })
}

export function getAuditLogs(params) {
  return request({
    url: '/audit/logs',
    method: 'get',
    params
  })
}

export function getAutoAuditRules() {
  return request({
    url: '/audit/rules',
    method: 'get'
  })
}

export function saveAutoAuditRule(data) {
  return request({
    url: '/audit/rules',
    method: 'post',
    data
  })
}

export function deleteAutoAuditRule(id) {
  return request({
    url: `/audit/rules/${id}`,
    method: 'delete'
  })
}

export function updateAutoAuditRuleStatus(id, status) {
  return request({
    url: `/audit/rules/${id}/status`,
    method: 'put',
    params: { status }
  })
}
