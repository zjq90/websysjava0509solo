import request from '@/utils/request'

export function getSlotList(params) {
  return request({
    url: '/slot/page',
    method: 'get',
    params
  })
}

export function getSlotListByDeviceId(deviceId) {
  return request({
    url: `/slot/device/${deviceId}`,
    method: 'get'
  })
}

export function getSlot(id) {
  return request({
    url: `/slot/${id}`,
    method: 'get'
  })
}

export function createSlot(data) {
  return request({
    url: '/slot',
    method: 'post',
    data
  })
}

export function updateSlot(data) {
  return request({
    url: '/slot',
    method: 'put',
    data
  })
}

export function deleteSlot(id) {
  return request({
    url: `/slot/${id}`,
    method: 'delete'
  })
}

export function replenishSlot(id, quantity) {
  return request({
    url: `/slot/${id}/replenish`,
    method: 'post',
    data: { quantity }
  })
}

export function dispenseSlot(id) {
  return request({
    url: `/slot/${id}/dispense`,
    method: 'post'
  })
}
