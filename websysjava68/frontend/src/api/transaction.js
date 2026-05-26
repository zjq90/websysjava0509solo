import request from '@/utils/request'

export function pageTransactions(params) {
  return request({
    url: '/transactions/page',
    method: 'get',
    params
  })
}

export function listTransactions(params) {
  return request({
    url: '/transactions',
    method: 'get',
    params
  })
}

export function getTransaction(id) {
  return request({
    url: `/transactions/${id}`,
    method: 'get'
  })
}

export function addTransaction(data) {
  return request({
    url: '/transactions',
    method: 'post',
    data
  })
}

export function updateTransaction(data) {
  return request({
    url: '/transactions',
    method: 'put',
    data
  })
}

export function deleteTransaction(id) {
  return request({
    url: `/transactions/${id}`,
    method: 'delete'
  })
}

export function applyCategoryRule(data) {
  return request({
    url: '/transactions/apply-rule',
    method: 'post',
    data
  })
}
