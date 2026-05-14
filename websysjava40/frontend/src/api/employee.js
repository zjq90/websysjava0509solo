import axios from './axios'

export const employeeApi = {
  getAll: () => axios.get('/employees'),
  getActive: () => axios.get('/employees/active'),
  getById: id => axios.get(`/employees/${id}`),
  getByPosition: position => axios.get(`/employees/position/${position}`),
  search: name => axios.get(`/employees/search?name=${name}`),
  create: data => axios.post('/employees', data),
  update: (id, data) => axios.put(`/employees/${id}`, data),
  delete: id => axios.delete(`/employees/${id}`),
  deactivate: id => axios.put(`/employees/${id}/deactivate`)
}
