import request from '@/utils/request'

export function listTags() {
  return request({
    url: '/tags',
    method: 'get'
  })
}

export function getTag(id) {
  return request({
    url: `/tags/${id}`,
    method: 'get'
  })
}

export function addTag(data) {
  return request({
    url: '/tags',
    method: 'post',
    data
  })
}

export function updateTag(data) {
  return request({
    url: '/tags',
    method: 'put',
    data
  })
}

export function deleteTag(id) {
  return request({
    url: `/tags/${id}`,
    method: 'delete'
  })
}

export function listTagsByTransactionId(transactionId) {
  return request({
    url: `/tags/transaction/${transactionId}`,
    method: 'get'
  })
}
