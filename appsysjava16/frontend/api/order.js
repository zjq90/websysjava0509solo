import request from '@/utils/request.js'

const orderApi = {
  getAllOrders() {
    return request({
      url: '/orders',
      method: 'GET'
    })
  },

  getOrderById(id) {
    return request({
      url: `/orders/${id}`,
      method: 'GET'
    })
  },

  getOrderByOrderNo(orderNo) {
    return request({
      url: `/orders/no/${orderNo}`,
      method: 'GET'
    })
  },

  getOrdersByStatus(status) {
    return request({
      url: `/orders/status/${status}`,
      method: 'GET'
    })
  },

  getCustomerOrders(customerId) {
    return request({
      url: `/orders/customer/${customerId}`,
      method: 'GET'
    })
  },

  getOrdersBySalesperson(salespersonId) {
    return request({
      url: `/orders/salesperson/${salespersonId}`,
      method: 'GET'
    })
  },

  getOrderStatistics() {
    return request({
      url: '/orders/statistics',
      method: 'GET'
    })
  },

  createOrder(data) {
    return request({
      url: '/orders',
      method: 'POST',
      data
    })
  },

  confirmOrder(id) {
    return request({
      url: `/orders/${id}/confirm`,
      method: 'POST'
    })
  },

  signContract(id) {
    return request({
      url: `/orders/${id}/sign`,
      method: 'POST'
    })
  },

  syncToErp(id) {
    return request({
      url: `/orders/${id}/sync-erp`,
      method: 'POST'
    })
  },

  shipOrder(id, logisticsInfo) {
    if (logisticsInfo) {
      return request({
        url: `/orders/${id}/ship`,
        method: 'POST',
        data: logisticsInfo
      })
    }
    return request({
      url: `/orders/${id}/ship`,
      method: 'POST'
    })
  },

  deliverOrder(id) {
    return request({
      url: `/orders/${id}/deliver`,
      method: 'POST'
    })
  },

  completeOrder(id) {
    return request({
      url: `/orders/${id}/complete`,
      method: 'POST'
    })
  },

  cancelOrder(id) {
    return request({
      url: `/orders/${id}/cancel`,
      method: 'POST'
    })
  }
}

export default orderApi
