import axios from './axios'

export const expressApi = {
  getAll: () => axios.get('/express'),
  getById: id => axios.get(`/express/${id}`),
  getByOrder: orderId => axios.get(`/express/order/${orderId}`),
  getByTrackingNo: trackingNo => axios.get(`/express/tracking/${trackingNo}`),
  getByStatus: status => axios.get(`/express/status/${status}`),
  getUnSigned: () => axios.get('/express/un-signed'),
  create: data => axios.post('/express', data),
  update: (id, data) => axios.put(`/express/${id}`, data),
  delete: id => axios.delete(`/express/${id}`),
  updateStatus: (id, status, trackingInfo) => axios.put(`/express/${id}/status/${status}`, {}, { params: { trackingInfo } }),
  simulateTracking: id => axios.put(`/express/${id}/simulate-tracking`),
  sign: id => axios.put(`/express/${id}/sign`)
}
