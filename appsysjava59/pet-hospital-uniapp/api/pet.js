import request from '../utils/request.js'

export default {
  getAllPets() {
    return request.get('/api/pet')
  },
  getPetDetail(id) {
    return request.get(`/api/pet/${id}`)
  },
  getPetTags(id) {
    return request.get(`/api/pet/${id}/tags`)
  },
  addPetTag(id, data) {
    return request.post(`/api/pet/${id}/tags`, data)
  },
  deletePetTag(tagId) {
    return request.delete(`/api/pet/tags/${tagId}`)
  },
  getVaccineRecords(id) {
    return request.get(`/api/pet/${id}/vaccines`)
  },
  getPetOwners() {
    return request.get('/api/pet/owner')
  },
  getPetsByOwner(ownerId) {
    return request.get(`/api/pet/owner/${ownerId}`)
  }
}
