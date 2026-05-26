import request from '@/utils/request'

export function listAccounts() {
  return request({
    url: '/accounts',
    method: 'get'
  })
}

export function getAccount(id) {
  return request({
    url: `/accounts/${id}`,
    method: 'get'
  })
}

export function addAccount(data) {
  return request({
    url: '/accounts',
    method: 'post',
    data
  })
}

export function updateAccount(data) {
  return request({
    url: '/accounts',
    method: 'put',
    data
  })
}

export function deleteAccount(id) {
  return request({
    url: `/accounts/${id}`,
    method: 'delete'
  })
}
