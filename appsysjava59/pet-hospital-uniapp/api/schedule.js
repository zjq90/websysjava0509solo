import request from '../utils/request.js'

export default {
  getDoctorSchedule(doctorId, startDate, endDate) {
    return request.get(`/api/schedule/doctor/${doctorId}?startDate=${startDate}&endDate=${endDate}`)
  },
  getAllSchedule(startDate, endDate) {
    return request.get(`/api/schedule?startDate=${startDate}&endDate=${endDate}`)
  },
  addSchedule(data) {
    return request.post('/api/schedule', data)
  },
  deleteSchedule(id) {
    return request.delete(`/api/schedule/${id}`)
  },
  getChangeRequests(doctorId) {
    return request.get(`/api/schedule/change-request/doctor/${doctorId}`)
  },
  getPendingRequests() {
    return request.get('/api/schedule/change-request/pending')
  },
  createChangeRequest(data) {
    return request.post('/api/schedule/change-request', data)
  },
  approveRequest(id, approverId, notes) {
    return request.put(`/api/schedule/change-request/${id}/approve`, { approverId, notes })
  },
  rejectRequest(id, approverId, notes) {
    return request.put(`/api/schedule/change-request/${id}/reject`, { approverId, notes })
  },
  getDoctorHolidays(doctorId, startDate, endDate) {
    return request.get(`/api/schedule/holiday/doctor/${doctorId}?startDate=${startDate}&endDate=${endDate}`)
  },
  addHoliday(data) {
    return request.post('/api/schedule/holiday', data)
  },
  deleteHoliday(id) {
    return request.delete(`/api/schedule/holiday/${id}`)
  }
}
