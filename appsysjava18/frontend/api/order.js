/**
 * 订单相关API
 */
import request from '@/utils/request'

// 获取订单列表
export function getOrderList(params) {
  return request.get('/api/order', params)
}

// 获取订单详情
export function getOrderDetail(id) {
  return request.get('/api/order/' + id)
}

// 创建订单
export function createOrder(data) {
  return request.post('/api/order', data)
}

// 更新订单状态
export function updateOrderStatus(id, status) {
  return request.put('/api/order/' + id + '/status', { status })
}

// 删除订单
export function deleteOrder(id) {
  return request.delete('/api/order/' + id)
}

// 获取客户列表
export function getCustomerList() {
  return request.get('/api/customer')
}

// 获取客户详情
export function getCustomerDetail(id) {
  return request.get('/api/customer/' + id)
}

// 新增客户
export function createCustomer(data) {
  return request.post('/api/customer', data)
}

// 更新客户
export function updateCustomer(id, data) {
  return request.put('/api/customer/' + id, data)
}

// 删除客户
export function deleteCustomer(id) {
  return request.delete('/api/customer/' + id)
}
