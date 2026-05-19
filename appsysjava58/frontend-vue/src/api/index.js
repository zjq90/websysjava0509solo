import request from './request'

export const healthApi = {
  getHealthCards: (petId) => request.get(`/health/cards/${petId}`),
  addDewormingRecord: (data) => request.post('/health/deworming', data),
  getDewormingHistory: (petId) => request.get(`/health/deworming/history/${petId}`),
  getCheckupHistory: (petId) => request.get(`/health/checkup/history/${petId}`),
  addCheckupRecord: (data) => request.post('/health/checkup', data)
}

export const dietApi = {
  generateSuggestion: (petId) => request.post(`/diet/generate/${petId}`),
  getHistory: (petId) => request.get(`/diet/history/${petId}`)
}

export const exerciseApi = {
  getTodayStats: (petId) => request.get(`/exercise/stats/${petId}`),
  syncThirdPartyData: (data) => request.post('/exercise/sync', data),
  getSupportedDevices: () => request.get('/exercise/devices'),
  addManualRecord: (data) => request.post('/exercise/manual', data),
  getHistory: (petId) => request.get(`/exercise/history/${petId}`)
}

export const circleApi = {
  getPosts: (params) => request.get('/circle/posts', { params }),
  getPendingPosts: (params) => request.get('/circle/posts/pending', { params }),
  createPost: (data) => request.post('/circle/post', data),
  auditPost: (postId, data) => request.post(`/circle/audit/${postId}`, null, { params: data }),
  likePost: (postId) => request.post(`/circle/like/${postId}`),
  incrementView: (postId) => request.post(`/circle/view/${postId}`),
  getPostDetail: (postId) => request.get(`/circle/post/${postId}`)
}

export const hospitalApi = {
  getNearby: (params) => request.get('/hospital/nearby', { params }),
  getDetail: (hospitalId) => request.get(`/hospital/${hospitalId}`),
  submitRating: (data) => request.post('/hospital/rating', data),
  getRatings: (hospitalId) => request.get(`/hospital/ratings/${hospitalId}`),
  getEmergency: () => request.get('/hospital/emergency')
}

export const petApi = {
  getList: (userId) => request.get(`/pet/list/${userId}`),
  getDetail: (petId) => request.get(`/pet/${petId}`),
  add: (data) => request.post('/pet', data),
  update: (petId, data) => request.put(`/pet/${petId}`, data),
  delete: (petId) => request.delete(`/pet/${petId}`)
}

export const userApi = {
  getInfo: (userId) => request.get(`/user/${userId}`),
  update: (data) => request.put('/user', data),
  getElderMode: () => request.get('/user/elder-mode'),
  toggleElderMode: (enabled) => request.post('/user/elder-mode', null, { params: { enabled } })
}