import axios from './axios'

export const transferRequestApi = {
  getAll: () => axios.get('/transfer-requests'),
  getById: id => axios.get(`/transfer-requests/${id}`),
  getByRequester: requesterId => axios.get(`/transfer-requests/requester/${requesterId}`),
  getByStatus: status => axios.get(`/transfer-requests/status/${status}`),
  getPending: () => axios.get('/transfer-requests/pending'),
  create: data => axios.post('/transfer-requests', data),
  approve: (id, data) => axios.put(`/transfer-requests/${id}/approve`, data),
  cancel: id => axios.put(`/transfer-requests/${id}/cancel`),
  delete: id => axios.delete(`/transfer-requests/${id}`)
}
