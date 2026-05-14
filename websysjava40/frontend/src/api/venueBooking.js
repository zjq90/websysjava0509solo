import axios from './axios'

export const venueBookingApi = {
  getAll: () => axios.get('/venue-bookings'),
  getById: id => axios.get(`/venue-bookings/${id}`),
  getByVenue: venueId => axios.get(`/venue-bookings/venue/${venueId}`),
  getByDate: date => axios.get(`/venue-bookings/date/${date}`),
  getVenueByDate: (venueId, date) => axios.get(`/venue-bookings/venue/${venueId}/date/${date}`),
  getByStatus: status => axios.get(`/venue-bookings/status/${status}`),
  create: data => axios.post('/venue-bookings', data),
  update: (id, data) => axios.put(`/venue-bookings/${id}`, data),
  delete: id => axios.delete(`/venue-bookings/${id}`),
  complete: id => axios.put(`/venue-bookings/${id}/complete`),
  cancel: id => axios.put(`/venue-bookings/${id}/cancel`),
  checkConflict: data => axios.post('/venue-bookings/check-conflict', data)
}
