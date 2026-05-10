import request from '@/utils/request'

export function getAgentPage(params) {
  return request({
    url: '/api/agents',
    method: 'get',
    params
  })
}

export function getAgentById(id) {
  return request({
    url: `/api/agents/${id}`,
    method: 'get'
  })
}

export function createAgent(data) {
  return request({
    url: '/api/agents',
    method: 'post',
    data
  })
}

export function updateAgent(data) {
  return request({
    url: '/api/agents',
    method: 'put',
    data
  })
}

export function deleteAgent(id) {
  return request({
    url: `/api/agents/${id}`,
    method: 'delete'
  })
}

export function getTopLevelAgents() {
  return request({
    url: '/api/agents/top-level',
    method: 'get'
  })
}

export function getChildAgents(parentId) {
  return request({
    url: `/api/agents/children/${parentId}`,
    method: 'get'
  })
}

export function getDescendantIds(agentId) {
  return request({
    url: `/api/agents/descendants/${agentId}`,
    method: 'get'
  })
}
