import axios from 'axios'

const request = axios.create({
  baseURL: '/api',
  timeout: 10000
})

request.interceptors.response.use(
  response => {
    return response.data
  },
  error => {
    console.error('请求错误:', error)
    return Promise.reject(error)
  }
)

export const appointmentApi = {
  getAll: () => request.get('/appointment'),
  getById: (id) => request.get(`/appointment/${id}`),
  getByDate: (date) => request.get(`/appointment/date/${date}`),
  getByStatus: (status) => request.get(`/appointment/status/${status}`),
  create: (data) => request.post('/appointment', data),
  update: (data) => request.put('/appointment', data),
  delete: (id) => request.delete(`/appointment/${id}`),
  callNumber: (id) => request.post(`/appointment/call/${id}`),
  complete: (id) => request.post(`/appointment/complete/${id}`)
}

export const doctorApi = {
  getAll: () => request.get('/doctor'),
  getById: (id) => request.get(`/doctor/${id}`),
  getByName: (name) => request.get(`/doctor/search?name=${name}`)
}

export default request
