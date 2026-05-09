const BASE_URL = 'http://localhost:8080'

const request = (options) => {
  return new Promise((resolve, reject) => {
    uni.request({
      url: BASE_URL + options.url,
      method: options.method || 'GET',
      data: options.data || {},
      header: {
        'Content-Type': 'application/json'
      },
      success: (res) => {
        if (res.statusCode === 200) {
          if (res.data.code === 200) {
            resolve(res.data)
          } else {
            uni.showToast({
              title: res.data.message || '请求失败',
              icon: 'none'
            })
            reject(res.data)
          }
        } else {
          uni.showToast({
            title: '网络错误',
            icon: 'none'
          })
          reject(res)
        }
      },
      fail: (err) => {
        uni.showToast({
          title: '网络连接失败',
          icon: 'none'
        })
        reject(err)
      }
    })
  })
}

const api = {
  getUsers: () => request({ url: '/api/users' }),
  getUserById: (id) => request({ url: `/api/users/${id}` }),
  createUser: (data) => request({ url: '/api/users', method: 'POST', data }),
  updateUser: (id, data) => request({ url: `/api/users/${id}`, method: 'PUT', data }),
  deleteUser: (id) => request({ url: `/api/users/${id}`, method: 'DELETE' }),

  getBatches: () => request({ url: '/api/batches' }),
  getBatchById: (id) => request({ url: `/api/batches/${id}` }),
  getBatchByNo: (batchNo) => request({ url: `/api/batches/batchNo/${batchNo}` }),
  createBatch: (data) => request({ url: '/api/batches', method: 'POST', data }),
  updateBatch: (id, data) => request({ url: `/api/batches/${id}`, method: 'PUT', data }),
  deleteBatch: (id) => request({ url: `/api/batches/${id}`, method: 'DELETE' }),

  getStagesByBatch: (batchId) => request({ url: `/api/stages/batch/${batchId}` }),
  getStageByCode: (batchId, stageCode) => request({ url: `/api/stages/batch/${batchId}/stage/${stageCode}` }),
  startStage: (data) => request({ url: '/api/stages/start', method: 'POST', data }),
  completeStage: (data) => request({ url: '/api/stages/complete', method: 'POST', data }),
  checkTimeout: () => request({ url: '/api/stages/check-timeout', method: 'POST' }),

  getInspectionsByBatch: (batchId) => request({ url: `/api/inspections/batch/${batchId}` }),
  getInspectionByCode: (batchId, stageCode) => request({ url: `/api/inspections/batch/${batchId}/stage/${stageCode}` }),
  submitInspection: (data) => request({ url: '/api/inspections', method: 'POST', data }),

  getReportByBatch: (batchId) => request({ url: `/api/reports/batch/${batchId}` }),
  getReportByNo: (batchNo) => request({ url: `/api/reports/batchNo/${batchNo}` }),
  generateReport: (batchId, userId) => request({ url: `/api/reports/generate/${batchId}?userId=${userId || 1}`, method: 'POST' }),

  getAlertsBySupervisor: (supervisorId) => request({ url: `/api/alerts/supervisor/${supervisorId}` }),
  getAlertsByBatch: (batchId) => request({ url: `/api/alerts/batch/${batchId}` }),
  markAlertRead: (id) => request({ url: `/api/alerts/read/${id}`, method: 'POST' })
}

export default api
