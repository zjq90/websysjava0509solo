const BASE_URL = 'http://localhost:8080/api'

export const request = (options) => {
  return new Promise((resolve, reject) => {
    uni.request({
      url: BASE_URL + options.url,
      method: options.method || 'GET',
      data: options.data || {},
      header: {
        'Content-Type': 'application/json'
      },
      success: (res) => {
        if (res.data.code === 200) {
          resolve(res.data.data)
        } else {
          uni.showToast({
            title: res.data.message || '请求失败',
            icon: 'none'
          })
          reject(res.data)
        }
      },
      fail: (err) => {
        uni.showToast({
          title: '网络错误',
          icon: 'none'
        })
        reject(err)
      }
    })
  })
}

export const api = {
  getCounselors: () => request({ url: '/counselor' }),
  getSeniorCounselors: () => request({ url: '/counselor/senior' }),
  getCounselor: (id) => request({ url: `/counselor/${id}` }),
  
  getSchedule: (counselorId, date) => request({ url: `/schedule/counselor/${counselorId}/date/${date}` }),
  getAvailableSchedules: (counselorId, date) => request({ url: `/schedule/counselor/${counselorId}/available/${date}` }),
  createSchedule: (data) => request({ url: '/schedule', method: 'POST', data }),
  
  getAppointmentsByCounselor: (counselorId) => request({ url: `/appointment/counselor/${counselorId}` }),
  getAppointmentsByUser: (userId) => request({ url: `/appointment/user/${userId}` }),
  createAppointment: (data) => request({ url: '/appointment', method: 'POST', data }),
  updateAppointmentStatus: (id, status) => request({ url: `/appointment/${id}/status?status=${status}`, method: 'PUT' }),
  
  getRecordsByCounselor: (counselorId) => request({ url: `/record/counselor/${counselorId}` }),
  getRecordsByUser: (userId) => request({ url: `/record/user/${userId}` }),
  createRecord: (data) => request({ url: '/record', method: 'POST', data }),
  updateRecord: (id, data) => request({ url: `/record/${id}`, method: 'PUT', data }),
  getRecord: (id) => request({ url: `/record/${id}` }),
  
  getAllAlerts: () => request({ url: '/crisis' }),
  getAlertsByStatus: (status) => request({ url: `/crisis/status/${status}` }),
  handleAlert: (id, result) => request({ url: `/crisis/${id}/handle?result=${result}`, method: 'PUT' }),
  
  getCourses: () => request({ url: '/course' })
}
