import axios from './axios'

export const scheduleApi = {
  getAll: () => axios.get('/schedules'),
  getById: id => axios.get(`/schedules/${id}`),
  getByEmployee: employeeId => axios.get(`/schedules/employee/${employeeId}`),
  getByDate: date => axios.get(`/schedules/date/${date}`),
  getByDateRange: (startDate, endDate) => axios.get(`/schedules/range?startDate=${startDate}&endDate=${endDate}`),
  getByStatus: status => axios.get(`/schedules/status/${status}`),
  getUnreminded: () => axios.get('/schedules/unreminded'),
  create: data => axios.post('/schedules', data),
  update: (id, data) => axios.put(`/schedules/${id}`, data),
  delete: id => axios.delete(`/schedules/${id}`),
  complete: id => axios.put(`/schedules/${id}/complete`),
  cancel: id => axios.put(`/schedules/${id}/cancel`),
  remind: id => axios.put(`/schedules/${id}/remind`),
  checkConflict: data => axios.post('/schedules/check-conflict', data)
}
