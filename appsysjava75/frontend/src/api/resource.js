import request from '../utils/request'

export const getResourceList = (params) => {
  return request({
    url: '/resource/share/list',
    method: 'GET',
    data: params
  })
}

export const getClubResourceList = (clubId, params) => {
  return request({
    url: `/resource/share/club/${clubId}`,
    method: 'GET',
    data: params
  })
}

export const getMyResourceList = (uploaderId, params) => {
  return request({
    url: `/resource/share/my/${uploaderId}`,
    method: 'GET',
    data: params
  })
}

export const uploadResource = (data) => {
  return request({
    url: '/resource/share/upload',
    method: 'POST',
    data
  })
}

export const getResourceDetail = (resourceId) => {
  return request({
    url: `/resource/share/${resourceId}`,
    method: 'GET'
  })
}

export const downloadResource = (resourceId) => {
  return request({
    url: `/resource/share/download/${resourceId}`,
    method: 'POST'
  })
}

export const deleteResource = (resourceId, userId) => {
  return request({
    url: `/resource/share/${resourceId}`,
    method: 'DELETE',
    data: { userId }
  })
}

export const getCooperationList = (params) => {
  return request({
    url: '/resource/cooperation/list',
    method: 'GET',
    data: params
  })
}

export const publishCooperation = (data) => {
  return request({
    url: '/resource/cooperation/publish',
    method: 'POST',
    data
  })
}

export const getCooperationDetail = (cooperationId) => {
  return request({
    url: `/resource/cooperation/${cooperationId}`,
    method: 'GET'
  })
}

export const getCooperationApplyList = (params) => {
  return request({
    url: '/resource/cooperation/apply/list',
    method: 'GET',
    data: params
  })
}

export const getMyApplyList = (applicantId, params) => {
  return request({
    url: `/resource/cooperation/apply/my/${applicantId}`,
    method: 'GET',
    data: params
  })
}

export const submitCooperationApply = (data) => {
  return request({
    url: '/resource/cooperation/apply/submit',
    method: 'POST',
    data
  })
}

export const auditCooperationApply = (data) => {
  return request({
    url: '/resource/cooperation/apply/audit',
    method: 'POST',
    data
  })
}

export const getCooperationApplyDetail = (applyId) => {
  return request({
    url: `/resource/cooperation/apply/${applyId}`,
    method: 'GET'
  })
}
