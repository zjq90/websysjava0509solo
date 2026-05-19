import request from '../utils/request.js'

export default {
  getPendingList(doctorId) {
    return request.get(`/api/consultation/doctor/${doctorId}/pending`)
  },
  getConsultationList(doctorId) {
    return request.get(`/api/consultation/doctor/${doctorId}`)
  },
  getConsultationDetail(id) {
    return request.get(`/api/consultation/${id}`)
  },
  getMessages(id) {
    return request.get(`/api/consultation/${id}/messages`)
  },
  sendMessage(id, data) {
    return request.post(`/api/consultation/${id}/message`, data)
  },
  updateStatus(id, status) {
    return request.put(`/api/consultation/${id}/status`, { status })
  }
}
