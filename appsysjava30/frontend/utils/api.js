import request from './request.js'

export const authApi = {
  login(data) {
    return request({
      url: '/auth/login',
      method: 'POST',
      data
    })
  },
  
  register(data) {
    return request({
      url: '/auth/register',
      method: 'POST',
      data
    })
  },
  
  getCurrentUser() {
    return request({
      url: '/auth/me'
    })
  },
  
  updateUser(data) {
    return request({
      url: '/auth/me',
      method: 'PUT',
      data
    })
  }
}

export const scheduleApi = {
  getDepartments() {
    return request({
      url: '/schedules/departments'
    })
  },
  
  getDoctors(deptCode) {
    return request({
      url: `/schedules/departments/${deptCode}/doctors`
    })
  },
  
  getAllDoctors() {
    return request({
      url: '/schedules/doctors'
    })
  },
  
  getSchedulesByDept(deptCode, date) {
    return request({
      url: `/schedules/departments/${deptCode}/date/${date}`
    })
  },
  
  getSchedulesByDoctor(doctorId, startDate, endDate) {
    return request({
      url: `/schedules/doctors/${doctorId}`,
      data: { startDate, endDate }
    })
  },
  
  getSchedulesByDoctorAndDate(doctorId, date) {
    return request({
      url: `/schedules/doctors/${doctorId}/date/${date}`
    })
  }
}

export const registrationApi = {
  create(data) {
    return request({
      url: '/registrations',
      method: 'POST',
      data
    })
  },
  
  pay(data) {
    return request({
      url: '/registrations/pay',
      method: 'POST',
      data
    })
  },
  
  cancel(registrationNo) {
    return request({
      url: `/registrations/${registrationNo}/cancel`,
      method: 'POST'
    })
  },
  
  list(page, size) {
    return request({
      url: '/registrations',
      data: { page, size }
    })
  },
  
  detail(registrationNo) {
    return request({
      url: `/registrations/${registrationNo}`
    })
  },
  
  today() {
    return request({
      url: '/registrations/today'
    })
  }
}

export const testApi = {
  getToken() {
    return request({
      url: '/test/token'
    })
  },
  
  simulateRefund(registrationNo) {
    return request({
      url: `/test/refund/${registrationNo}`,
      method: 'POST'
    })
  },
  
  simulateVisitComplete(registrationNo) {
    return request({
      url: `/test/visit-complete/${registrationNo}`,
      method: 'POST'
    })
  },
  
  health() {
    return request({
      url: '/test/health'
    })
  }
}
