import axios from 'axios'

const api = axios.create({
  baseURL: '/api',
  timeout: 30000
})

api.interceptors.response.use(
  response => response.data,
  error => {
    console.error('API Error:', error)
    return Promise.reject(error)
  }
)

export default {
  user: {
    register: (data) => api.post('/users/register', data),
    login: (data) => api.post('/users/login', data),
    get: (id) => api.get(`/users/${id}`),
    update: (id, data) => api.put(`/users/${id}`, data),
  },
  music: {
    upload: (formData) => api.post('/music/upload', formData, {
      headers: { 'Content-Type': 'multipart/form-data' }
    }),
    update: (id, formData) => api.put(`/music/${id}`, formData, {
      headers: { 'Content-Type': 'multipart/form-data' }
    }),
    delete: (id) => api.delete(`/music/${id}`),
    get: (id) => api.get(`/music/${id}`),
    list: (page = 0, size = 20) => api.get('/music/list', { params: { page, size } }),
    getCategories: (id) => api.get(`/music/${id}/categories`),
    getTags: (id) => api.get(`/music/${id}/tags`),
    assignCategories: (id, categoryIds) => api.post(`/music/${id}/categories`, { categoryIds }),
    assignTags: (id, tagIds) => api.post(`/music/${id}/tags`, { tagIds }),
  },
  category: {
    all: () => api.get('/categories'),
    byType: (type) => api.get(`/categories/type/${type}`),
    create: (data) => api.post('/categories', data),
    delete: (id) => api.delete(`/categories/${id}`),
    music: (id) => api.get(`/categories/${id}/music`),
  },
  tag: {
    all: () => api.get('/tags'),
    search: (name) => api.get('/tags/search', { params: { name } }),
    userTags: (userId) => api.get(`/tags/user/${userId}`),
    music: (id) => api.get(`/tags/${id}/music`),
    create: (data) => api.post('/tags', data),
    delete: (id) => api.delete(`/tags/${id}`),
  },
  search: {
    search: (keyword, userId, page = 0, size = 20) =>
      api.get('/search', { params: { keyword, userId, page, size } }),
    hot: () => api.get('/search/hot'),
    history: (userId) => api.get(`/search/history/${userId}`),
    clearHistory: (userId) => api.delete(`/search/history/${userId}`),
  },
  recommend: {
    personalized: (userId, limit = 10) => api.get('/recommend/personalized', { params: { userId, limit } }),
    hotChart: () => api.get('/recommend/hot-chart'),
    newChart: () => api.get('/recommend/new-chart'),
    risingChart: () => api.get('/recommend/rising-chart'),
    scenePlaylists: () => api.get('/recommend/scene-playlists'),
    playlistMusic: (id) => api.get(`/recommend/playlist/${id}/music`),
    recordBehavior: (data) => api.post('/recommend/behavior', data),
    removeBehavior: (data) => api.delete('/recommend/behavior', { data }),
    checkBehavior: (userId, musicId, type) =>
      api.get('/recommend/behavior/check', { params: { userId, musicId, type } }),
    userLiked: (userId) => api.get(`/recommend/user/${userId}/liked`),
    userCollected: (userId) => api.get(`/recommend/user/${userId}/collected`),
    userPlaylists: (userId) => api.get(`/recommend/user/${userId}/playlists`),
    createPlaylist: (data) => api.post('/recommend/playlist', data),
  },
  copyright: {
    declare: (data) => api.post('/copyright/declare', data),
    getByMusic: (musicId) => api.get(`/copyright/music/${musicId}`),
    report: (data) => api.post('/copyright/report', data),
    pendingReports: () => api.get('/copyright/reports/pending'),
    reportsByMusic: (musicId) => api.get(`/copyright/reports/music/${musicId}`),
    updateReportStatus: (id, status) => api.put(`/copyright/reports/${id}/status`, { status }),
  }
}
