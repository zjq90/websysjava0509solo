import request from './request'

export const getMusicList = (page = 0, size = 20, keyword = '') => {
  return request.get('/music/list', {
    params: { page, size, keyword }
  })
}

export const getMusicDetail = (id) => {
  return request.get(`/music/${id}`)
}

export const getHotMusic = (limit = 10) => {
  return request.get('/music/hot', {
    params: { limit }
  })
}

export const getLyrics = (musicId) => {
  return request.get(`/music/lyrics/${musicId}`)
}

export const uploadMusic = (formData) => {
  return request.post('/music', formData, {
    headers: { 'Content-Type': 'multipart/form-data' }
  })
}

export const batchImportMusic = (musicList) => {
  return request.post('/music/batch-import', musicList)
}

export const updateMusic = (id, music) => {
  return request.put(`/music/${id}`, music)
}

export const deleteMusic = (id) => {
  return request.delete(`/music/${id}`)
}

export const getMusicStreamUrl = (id, quality = '128') => {
  return `/api/music/stream/${id}/${quality}`
}

export const canDownload = (musicId, quality) => {
  return request.get(`/download/check/${musicId}/${quality}`)
}

export const downloadMusic = (musicId, quality) => {
  return request.post(`/download/${musicId}/${quality}`)
}

export const getDownloadUrl = (musicId, quality) => {
  return `/api/download/file/${musicId}/${quality}`
}

export const getMyDownloads = (page = 0, size = 20) => {
  return request.get('/download/my', {
    params: { page, size }
  })
}
