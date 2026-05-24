import axios from 'axios'
import { ElMessage } from 'element-plus'

const BASE_URL = '/api'

const request = axios.create({
  baseURL: BASE_URL,
  timeout: 10000
})

request.interceptors.response.use(
  response => {
    if (response.data.code === 200) {
      return response.data.data
    } else {
      ElMessage.error(response.data.message || '请求失败')
      return Promise.reject(response.data)
    }
  },
  error => {
    ElMessage.error('网络错误')
    return Promise.reject(error)
  }
)

export const api = {
  login: (phone, password) => request.post('/staff/login', { phone, password }),
  
  getTasks: () => request.get('/dispatch/tasks/sorted'),
  getTaskDetail: (id) => request.get(`/dispatch/tasks/${id}`),
  acceptTask: (id, staffId, staffName) => 
    request.post(`/dispatch/tasks/${id}/accept`, null, { params: { staffId, staffName } }),
  completeTask: (id) => request.post(`/dispatch/tasks/${id}/complete`),
  
  getRepairOrders: () => request.get('/maintenance/orders/sorted'),
  getRepairOrderDetail: (id) => request.get(`/maintenance/orders/${id}`),
  acceptRepairOrder: (id, staffId, staffName) => 
    request.post(`/maintenance/orders/${id}/accept`, null, { params: { staffId, staffName } }),
  startRepair: (id) => request.post(`/maintenance/orders/${id}/start`),
  completeRepair: (id, description, usedParts, cost) => 
    request.post(`/maintenance/orders/${id}/complete`, null, { params: { description, usedParts, cost } }),
  
  getBikeDetail: (id) => request.get(`/bike/${id}/detail`),
  getBikeByQrCode: (qrCode) => request.get(`/bike/qrcode/${qrCode}`),
  getBikes: () => request.get('/bike/list'),
  
  getHeatPoints: () => request.get('/dispatch/heatpoints'),
  getDispatchSuggestions: () => request.get('/dispatch/suggestions'),
  
  getBatteries: () => request.get('/battery/batteries'),
  getLowBatteryWarning: () => request.get('/battery/warnings'),
  getSwapStations: () => request.get('/battery/stations'),
  getBatteryLifeCycle: () => request.get('/battery/lifecycle-stats'),
  swapBattery: (bikeId, stationId, staffId, staffName) => 
    request.post('/battery/swap', null, { params: { bikeId, stationId, operatorId: staffId, operatorName: staffName } }),
  
  getAreas: () => request.get('/area/list'),
  getStaffs: () => request.get('/staff/list'),
  
  getSpareParts: () => request.get('/maintenance/parts'),
  getLowStockParts: () => request.get('/maintenance/parts/low-stock')
}
