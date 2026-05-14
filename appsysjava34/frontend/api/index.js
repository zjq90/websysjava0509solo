import request from '@/utils/request.js'

export const userApi = {
  login: (username, password) => request({
    url: '/user/login',
    method: 'POST',
    data: { username, password }
  }),
  getById: (id) => request({
    url: `/user/${id}`
  }),
  toggleElderMode: (id) => request({
    url: `/user/${id}/elder-mode`,
    method: 'PUT'
  })
}

export const serviceOrderApi = {
  list: (userId) => request({
    url: `/service-order/user/${userId}`
  }),
  get: (id) => request({
    url: `/service-order/${id}`
  })
}

export const faultReportApi = {
  list: (userId) => request({
    url: `/fault-report/user/${userId}`
  }),
  create: (data) => request({
    url: '/fault-report/create',
    method: 'POST',
    data
  }),
  diagnose: (faultType) => request({
    url: '/fault-report/diagnose',
    method: 'POST',
    params: { faultType }
  })
}

export const fileApi = {
  upload: (filePath) => {
    return new Promise((resolve, reject) => {
      uni.uploadFile({
        url: '/api/file/upload',
        filePath: filePath,
        name: 'file',
        success: (res) => {
          const data = JSON.parse(res.data)
          if (data.code === 200) {
            resolve(data.data)
          } else {
            reject(new Error(data.message))
          }
        },
        fail: (err) => {
          reject(err)
        }
      })
    })
  },
  batchUpload: async (filePaths) => {
    const results = []
    for (const path of filePaths) {
      try {
        const result = await new Promise((resolve, reject) => {
          uni.uploadFile({
            url: '/api/file/upload',
            filePath: path,
            name: 'file',
            success: (res) => {
              const data = JSON.parse(res.data)
              if (data.code === 200) {
                resolve(data.data)
              } else {
                reject(new Error(data.message))
              }
            },
            fail: (err) => {
              reject(err)
            }
          })
        })
        results.push(result)
      } catch (e) {
        console.error('上传失败:', e)
      }
    }
    return results
  }
}

export const networkApi = {
  getDevices: (userId) => request({
    url: `/network/devices/${userId}`
  }),
  toggleDeviceBlock: (deviceId) => request({
    url: `/network/device/${deviceId}/block`,
    method: 'PUT'
  }),
  setSpeedLimit: (deviceId, limit) => request({
    url: `/network/device/${deviceId}/speed-limit`,
    method: 'PUT',
    data: { limit }
  }),
  getWiFiSetting: (userId) => request({
    url: `/network/wifi/${userId}`
  }),
  updateWiFiPassword: (userId, password) => request({
    url: `/network/wifi/${userId}/password`,
    method: 'PUT',
    data: { password }
  }),
  toggleWiFiVisibility: (userId) => request({
    url: `/network/wifi/${userId}/visibility`,
    method: 'PUT'
  }),
  getChildGuards: (userId) => request({
    url: `/network/child-guard/${userId}`
  })
}

export const chatApi = {
  getHistory: (sessionId) => request({
    url: `/chat/history/session/${sessionId}`
  }),
  send: (userId, sessionId, content) => request({
    url: '/chat/send',
    method: 'POST',
    data: { userId, sessionId, content }
  })
}
