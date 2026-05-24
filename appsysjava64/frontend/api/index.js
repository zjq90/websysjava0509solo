const BASE_URL = 'http://localhost:8080/api'

const request = (url, method = 'GET', data = {}) => {
  return new Promise((resolve, reject) => {
    uni.request({
      url: BASE_URL + url,
      method: method,
      data: data,
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
  login: (phone, password) => request('/staff/login', 'POST', { phone, password }),
  
  getTasks: () => request('/dispatch/tasks/sorted'),
  getTaskDetail: (id) => request(`/dispatch/tasks/${id}`),
  acceptTask: (id, staffId, staffName) => 
    request(`/dispatch/tasks/${id}/accept?staffId=${staffId}&staffName=${staffName}`, 'POST'),
  completeTask: (id) => request(`/dispatch/tasks/${id}/complete`, 'POST'),
  
  getRepairOrders: () => request('/maintenance/orders/sorted'),
  getRepairOrderDetail: (id) => request(`/maintenance/orders/${id}`),
  acceptRepairOrder: (id, staffId, staffName) => 
    request(`/maintenance/orders/${id}/accept?staffId=${staffId}&staffName=${staffName}`, 'POST'),
  startRepair: (id) => request(`/maintenance/orders/${id}/start`, 'POST'),
  completeRepair: (id, description, usedParts, cost) => 
    request(`/maintenance/orders/${id}/complete?description=${description}&usedParts=${usedParts || ''}&cost=${cost || 0}`, 'POST'),
  
  getBikeDetail: (id) => request(`/bike/${id}/detail`),
  getBikeByQrCode: (qrCode) => request(`/bike/qrcode/${qrCode}`),
  getBikes: () => request('/bike/list'),
  
  getHeatPoints: () => request('/dispatch/heatpoints'),
  getDispatchSuggestions: () => request('/dispatch/suggestions'),
  
  getBatteries: () => request('/battery/batteries'),
  getLowBatteryWarning: () => request('/battery/warnings'),
  getSwapStations: () => request('/battery/stations'),
  getBatteryLifeCycle: () => request('/battery/lifecycle-stats'),
  swapBattery: (bikeId, stationId, staffId, staffName) => 
    request(`/battery/swap?bikeId=${bikeId}&stationId=${stationId}&operatorId=${staffId}&operatorName=${staffName}`, 'POST'),
  
  getAreas: () => request('/area/list'),
  getStaffs: () => request('/staff/list'),
  
  getSpareParts: () => request('/maintenance/parts'),
  getLowStockParts: () => request('/maintenance/parts/low-stock')
}
