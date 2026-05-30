import axios from 'axios'

const request = axios.create({
  baseURL: '/api',
  timeout: 10000
})

request.interceptors.response.use(
  response => {
    return response.data
  },
  error => {
    return Promise.reject(error)
  }
)

export const adSlotApi = {
  list: () => request.get('/ad-slots'),
  get: (id) => request.get(`/ad-slots/${id}`),
  create: (data) => request.post('/ad-slots', data),
  update: (id, data) => request.put(`/ad-slots/${id}`, data),
  delete: (id) => request.delete(`/ad-slots/${id}`)
}

export const advertisementApi = {
  list: () => request.get('/advertisements'),
  listBySlot: (slotId) => request.get(`/advertisements/slot/${slotId}`),
  get: (id) => request.get(`/advertisements/${id}`),
  create: (data) => request.post('/advertisements', data),
  update: (id, data) => request.put(`/advertisements/${id}`, data),
  delete: (id) => request.delete(`/advertisements/${id}`),
  toggle: (id) => request.put(`/advertisements/${id}/toggle`)
}

export const revenueStatsApi = {
  list: () => request.get('/revenue-stats'),
  listBySlot: (slotId) => request.get(`/revenue-stats/slot/${slotId}`),
  summary: (params) => request.get('/revenue-stats/summary', { params }),
  export: (params) => request.get('/revenue-stats/export', { params, responseType: 'blob' })
}

export const gameApi = {
  list: () => request.get('/games'),
  get: (id) => request.get(`/games/${id}`),
  create: (data) => request.post('/games', data),
  update: (id, data) => request.put(`/games/${id}`, data),
  delete: (id) => request.delete(`/games/${id}`),
  updateRewardVideo: (id, data) => request.patch(`/games/${id}/reward-video`, data)
}

export const messageTemplateApi = {
  list: () => request.get('/message-templates'),
  listSystem: () => request.get('/message-templates/system'),
  get: (id) => request.get(`/message-templates/${id}`),
  create: (data) => request.post('/message-templates', data),
  update: (id, data) => request.put(`/message-templates/${id}`, data),
  delete: (id) => request.delete(`/message-templates/${id}`)
}

export const pushTaskApi = {
  list: () => request.get('/push-tasks'),
  get: (id) => request.get(`/push-tasks/${id}`),
  create: (data) => request.post('/push-tasks', data),
  update: (id, data) => request.put(`/push-tasks/${id}`, data),
  delete: (id) => request.delete(`/push-tasks/${id}`),
  send: (id) => request.post(`/push-tasks/${id}/send`)
}

export const popupAnnouncementApi = {
  list: () => request.get('/popup-announcements'),
  listActive: () => request.get('/popup-announcements/active'),
  get: (id) => request.get(`/popup-announcements/${id}`),
  create: (data) => request.post('/popup-announcements', data),
  update: (id, data) => request.put(`/popup-announcements/${id}`, data),
  delete: (id) => request.delete(`/popup-announcements/${id}`),
  toggle: (id) => request.put(`/popup-announcements/${id}/toggle`)
}

export const messageApi = {
  listByTask: (taskId) => request.get(`/messages/task/${taskId}`),
  listByUser: (userId) => request.get(`/messages/user/${userId}`)
}

export default request
