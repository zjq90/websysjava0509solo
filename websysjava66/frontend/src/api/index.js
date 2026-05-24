import request from '../utils/request'

export const getDashboardStats = () => {
  return request.get('/dashboard/stats')
}

export const getCameras = () => {
  return request.get('/dashboard/cameras')
}

export const setAllNightMode = (enabled) => {
  return request.post(`/dashboard/night-mode?enabled=${enabled}`)
}

export const sendBroadcast = (content, sentBy = 'admin') => {
  return request.post('/broadcast/send', { content, sentBy })
}

export const queryRecords = (params) => {
  return request.post('/records/query', params)
}

export const deleteRecord = (id) => {
  return request.delete(`/records/${id}`)
}

export const exportCsv = (params) => {
  return request.post('/records/export/csv', params, {
    responseType: 'blob'
  })
}

export const exportExcel = (params) => {
  return request.post('/records/export/excel', params, {
    responseType: 'blob'
  })
}

export const getBlacklist = () => {
  return request.get('/blacklist')
}

export const getActiveBlacklist = () => {
  return request.get('/blacklist/active')
}

export const addBlacklist = (data) => {
  return request.post('/blacklist', data)
}

export const removeBlacklist = (id, reason) => {
  return request.post(`/blacklist/${id}/remove`, { reason })
}

export const checkBlacklisted = (plateNumber) => {
  return request.get(`/blacklist/check/${plateNumber}`)
}

export const getAllCameras = () => {
  return request.get('/cameras')
}

export const getCameraById = (id) => {
  return request.get(`/cameras/${id}`)
}

export const createCamera = (data) => {
  return request.post('/cameras', data)
}

export const updateCamera = (id, data) => {
  return request.put(`/cameras/${id}`, data)
}

export const deleteCamera = (id) => {
  return request.delete(`/cameras/${id}`)
}
