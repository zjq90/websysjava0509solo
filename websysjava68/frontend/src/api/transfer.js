import request from '@/utils/request'

export function pageTransfers(params) {
  return request({
    url: '/transfers/page',
    method: 'get',
    params
  })
}

export function listTransfers() {
  return request({
    url: '/transfers',
    method: 'get'
  })
}

export function getTransfer(id) {
  return request({
    url: `/transfers/${id}`,
    method: 'get'
  })
}

export function addTransfer(data) {
  return request({
    url: '/transfers',
    method: 'post',
    data
  })
}

export function deleteTransfer(id) {
  return request({
    url: `/transfers/${id}`,
    method: 'delete'
  })
}
