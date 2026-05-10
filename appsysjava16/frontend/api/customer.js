import request from '@/utils/request.js'

const customerApi = {
  getAllCustomers() {
    return request({
      url: '/customers',
      method: 'GET'
    })
  },

  getCustomerById(id) {
    return request({
      url: `/customers/${id}`,
      method: 'GET'
    })
  },

  searchCustomers(name) {
    return request({
      url: '/customers/search',
      method: 'GET',
      params: { name }
    })
  },

  createCustomer(data) {
    return request({
      url: '/customers',
      method: 'POST',
      data
    })
  },

  createTemporaryCustomer(name, phone) {
    return request({
      url: '/customers/temporary',
      method: 'POST',
      params: { name, phone }
    })
  },

  updateCustomer(id, data) {
    return request({
      url: `/customers/${id}`,
      method: 'PUT',
      data
    })
  },

  deleteCustomer(id) {
    return request({
      url: `/customers/${id}`,
      method: 'DELETE'
    })
  },

  getCustomerOrders(customerId) {
    return request({
      url: `/customers/${customerId}/orders`,
      method: 'GET'
    })
  },

  getCustomerVisitRecords(customerId) {
    return request({
      url: `/customers/${customerId}/visits`,
      method: 'GET'
    })
  },

  addVisitRecord(customerId, data) {
    return request({
      url: `/customers/${customerId}/visits`,
      method: 'POST',
      data
    })
  },

  getTemporaryCustomers() {
    return request({
      url: '/customers/temporary',
      method: 'GET'
    })
  },

  getRegularCustomers() {
    return request({
      url: '/customers/regular',
      method: 'GET'
    })
  },

  getTopCustomers(limit) {
    return request({
      url: `/customers/top/${limit}`,
      method: 'GET'
    })
  }
}

export default customerApi
