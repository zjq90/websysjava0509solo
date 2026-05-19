import request from '../utils/request.js'

export default {
  login(data) {
    return request.post('/api/user/login', data)
  },
  getDoctors() {
    return request.get('/api/user/doctors')
  },
  getUserInfo(id) {
    return request.get(`/api/user/${id}`)
  }
}
