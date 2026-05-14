import axios from './axios'

export const costumeApi = {
  getAll: () => axios.get('/costumes'),
  getAvailable: () => axios.get('/costumes/available'),
  getAvailableAndClean: () => axios.get('/costumes/available-clean'),
  getById: id => axios.get(`/costumes/${id}`),
  getByType: type => axios.get(`/costumes/type/${type}`),
  getAvailableByType: type => axios.get(`/costumes/type/${type}/available`),
  getByCleaningStatus: status => axios.get(`/costumes/cleaning-status/${status}`),
  search: name => axios.get(`/costumes/search?name=${name}`),
  create: data => axios.post('/costumes', data),
  update: (id, data) => axios.put(`/costumes/${id}`, data),
  delete: id => axios.delete(`/costumes/${id}`),
  markUsed: id => axios.put(`/costumes/${id}/mark-used`),
  markCleaned: id => axios.put(`/costumes/${id}/mark-cleaned`),
  markCleaning: id => axios.put(`/costumes/${id}/mark-cleaning`)
}
