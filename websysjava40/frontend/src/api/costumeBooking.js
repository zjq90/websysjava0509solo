import axios from './axios'

export const costumeBookingApi = {
  getAll: () => axios.get('/costume-bookings'),
  getById: id => axios.get(`/costume-bookings/${id}`),
  getByCostume: costumeId => axios.get(`/costume-bookings/costume/${costumeId}`),
  getByStatus: status => axios.get(`/costume-bookings/status/${status}`),
  create: data => axios.post('/costume-bookings', data),
  update: (id, data) => axios.put(`/costume-bookings/${id}`, data),
  delete: id => axios.delete(`/costume-bookings/${id}`),
  returnCostume: id => axios.put(`/costume-bookings/${id}/return`),
  cancel: id => axios.put(`/costume-bookings/${id}/cancel`)
}
