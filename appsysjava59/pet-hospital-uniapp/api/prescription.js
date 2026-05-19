import request from '../utils/request.js'

export default {
  getDoctorPrescriptions(doctorId) {
    return request.get(`/api/prescription/doctor/${doctorId}`)
  },
  getPetPrescriptions(petId) {
    return request.get(`/api/prescription/pet/${petId}`)
  },
  getPrescriptionDetail(id) {
    return request.get(`/api/prescription/${id}`)
  },
  getPrescriptionItems(id) {
    return request.get(`/api/prescription/${id}/items`)
  },
  createPrescription(data) {
    return request.post('/api/prescription', data)
  },
  issuePrescription(id) {
    return request.put(`/api/prescription/${id}/issue`)
  }
}
