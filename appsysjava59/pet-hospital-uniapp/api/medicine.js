import request from '../utils/request.js'

export default {
  getAllMedicines() {
    return request.get('/api/medicine')
  },
  searchMedicine(keyword) {
    return request.get(`/api/medicine/search?keyword=${encodeURIComponent(keyword)}`)
  },
  getMedicineDetail(id) {
    return request.get(`/api/medicine/${id}`)
  },
  checkStock(id, quantity) {
    return request.post('/api/medicine/check-stock', { id, quantity })
  }
}
