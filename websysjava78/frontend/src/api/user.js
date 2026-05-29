import request from './request'

export const login = (username, password) => {
  return request.post('/auth/login', { username, password })
}

export const register = (data) => {
  return request.post('/auth/register', data)
}

export const getCurrentUser = () => {
  return request.get('/user/me')
}

export const updateUser = (data) => {
  return request.put('/user/me', data)
}

export const upgradeToPremium = (userId) => {
  return request.post(`/user/${userId}/upgrade`)
}

export const getRemainingDownloads = () => {
  return request.get('/user/me/downloads/remaining')
}
