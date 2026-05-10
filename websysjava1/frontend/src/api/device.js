import request from '@/utils/request'

export function getDeviceList(params) {
  return request({
    url: '/device/page',
    method: 'get',
    params
  })
}

export function getAllDevices() {
  return request({
    url: '/device/list',
    method: 'get'
  })
}

export function getDevice(id) {
  return request({
    url: `/device/${id}`,
    method: 'get'
  })
}

export function createDevice(data) {
  return request({
    url: '/device',
    method: 'post',
    data
  })
}

export function updateDevice(data) {
  return request({
    url: '/device',
    method: 'put',
    data
  })
}

export function deleteDevice(id) {
  return request({
    url: `/device/${id}`,
    method: 'delete'
  })
}

export function getDeviceStatistics() {
  return request({
    url: '/device/statistics',
    method: 'get'
  })
}

export function getDeviceLatestStatus(id) {
  return request({
    url: `/device-status/latest/device/${id}`,
    method: 'get'
  })
}

export function restartDevice(id) {
  return request({
    url: `/device-status/${id}/restart`,
    method: 'post'
  })
}

export function unlockDevice(id) {
  return request({
    url: `/device-status/${id}/unlock`,
    method: 'post'
  })
}

export function adjustTemperature(id, targetTemperature) {
  return request({
    url: `/device-status/${id}/temperature`,
    method: 'post',
    data: { targetTemperature }
  })
}

export function setAdvertContent(id, advertContent) {
  return request({
    url: `/device-status/${id}/advert`,
    method: 'post',
    data: { advertContent }
  })
}

export function firmwareUpgrade(deviceIds) {
  return request({
    url: '/device-status/firmware-upgrade',
    method: 'post',
    data: { deviceIds }
  })
}
