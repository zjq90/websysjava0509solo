import { http } from './request'

export const authApi = {
  login: (data) => http.post('/auth/login', data),
  register: (data) => http.post('/auth/register', data),
  getUserInfo: () => http.get('/auth/me'),
  updateUserInfo: (data) => http.put('/auth/me', data),
  toggleElderMode: (elderMode) => http.post('/auth/elder-mode', { elderMode }),
  changePassword: (oldPassword, newPassword) => http.post('/auth/password', { oldPassword, newPassword })
}

export const departmentApi = {
  getAll: () => http.get('/departments'),
  getById: (id) => http.get(`/departments/${id}`)
}

export const doctorApi = {
  getAll: (params) => http.get('/doctors', params),
  getById: (id) => http.get(`/doctors/${id}`),
  getByDept: (deptId) => http.get(`/doctors/departments/${deptId}`)
}

export const scheduleApi = {
  getByDate: (params) => http.get('/schedules', params),
  getAvailableDates: () => http.get('/schedules/dates'),
  getByDoctor: (doctorId, date) => http.get(`/schedules/doctor/${doctorId}`, { date }),
  getSlots: (scheduleId) => http.get(`/slots/${scheduleId}`),
  lockSlot: (slotId) => http.post(`/slots/${slotId}/lock`),
  unlockSlot: (slotId) => http.post(`/slots/${slotId}/unlock`)
}

export const appointmentApi = {
  create: (data) => http.post('/appointments', data),
  getList: (status) => http.get('/appointments', { status }),
  getDetail: (id) => http.get(`/appointments/${id}`),
  cancel: (id, reason) => http.post(`/appointments/${id}/cancel`, { reason }),
  pay: (id, data) => http.post(`/appointments/${id}/pay`, data)
}

export const patientApi = {
  getList: () => http.get('/patients'),
  getById: (id) => http.get(`/patients/${id}`),
  add: (data) => http.post('/patients', data),
  update: (id, data) => http.put(`/patients/${id}`, data),
  delete: (id) => http.delete(`/patients/${id}`),
  setDefault: (id) => http.post(`/patients/${id}/default`)
}

export default {
  authApi,
  departmentApi,
  doctorApi,
  scheduleApi,
  appointmentApi,
  patientApi
}
