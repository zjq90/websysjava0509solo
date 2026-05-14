import axios from './axios'

export const venueApi = {
  getAll: () => axios.get('/venues'),
  getAvailable: () => axios.get('/venues/available'),
  getById: id => axios.get(`/venues/${id}`),
  getByType: type => axios.get(`/venues/type/${type}`),
  getByStatus: status => axios.get(`/venues/status/${status}`),
  search: name => axios.get(`/venues/search?name=${name}`),
  create: data => axios.post('/venues', data),
  update: (id, data) => axios.put(`/venues/${id}`, data),
  delete: id => axios.delete(`/venues/${id}`),
  setMaintenance: (id, maintenance) => axios.put(`/venues/${id}/maintenance`, maintenance)
}
