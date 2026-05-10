import request from '@/utils/request.js'

const logisticsApi = {
  getLogisticsById(id) {
    return request({
      url: `/logistics/${id}`,
      method: 'GET'
    })
  },

  getTrackingByOrderId(orderId) {
    return request({
      url: `/logistics/order/${orderId}`,
      method: 'GET'
    })
  },

  getLogisticsByOrderId(orderId) {
    return this.getTrackingByOrderId(orderId)
  },

  getLogisticsByTrackingNo(trackingNo) {
    return request({
      url: `/logistics/tracking/${trackingNo}`,
      method: 'GET'
    })
  },

  createLogistics(data) {
    return request({
      url: '/logistics',
      method: 'POST',
      data
    })
  },

  updateTracking(orderId) {
    return request({
      url: `/logistics/${orderId}/update`,
      method: 'POST'
    })
  },

  updateLogistics(id, data) {
    return request({
      url: `/logistics/${id}`,
      method: 'PUT',
      data
    })
  },

  updateLogisticsStatus(id, status, location) {
    return request({
      url: `/logistics/${id}/status`,
      method: 'PUT',
      data: { status, location }
    })
  },

  fetchFromApi(trackingNo) {
    return request({
      url: `/logistics/${trackingNo}/fetch`,
      method: 'POST'
    })
  },

  deleteLogistics(id) {
    return request({
      url: `/logistics/${id}`,
      method: 'DELETE'
    })
  }
}

export default logisticsApi
