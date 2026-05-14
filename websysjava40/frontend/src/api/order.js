import axios from './axios'

export const orderApi = {
  getAll: () => axios.get('/orders'),
  getById: id => axios.get(`/orders/${id}`),
  getByNo: orderNo => axios.get(`/orders/no/${orderNo}`),
  getByStatus: status => axios.get(`/orders/status/${status}`),
  search: customerName => axios.get(`/orders/search?customerName=${customerName}`),
  create: data => axios.post('/orders', data),
  update: (id, data) => axios.put(`/orders/${id}`, data),
  delete: id => axios.delete(`/orders/${id}`),
  updateStatus: (id, status) => axios.put(`/orders/${id}/status/${status}`),
  complete: id => axios.put(`/orders/${id}/complete`),
  delivering: id => axios.put(`/orders/${id}/delivering`),
  cancel: id => axios.put(`/orders/${id}/cancel`)
}
