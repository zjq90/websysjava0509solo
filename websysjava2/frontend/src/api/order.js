import request from '../utils/request'

export const getOrders = (page = 0, size = 10, paymentStatus = null, pickupStatus = null) => {
  const params = { page, size }
  if (paymentStatus) params.paymentStatus = paymentStatus
  if (pickupStatus) params.pickupStatus = pickupStatus
  return request.get('/orders', { params })
}
export const getOrderById = (id) => request.get(`/orders/${id}`)
export const getOrderByNo = (orderNo) => request.get(`/orders/orderNo/${orderNo}`)
export const createOrder = (data) => request.post('/orders', data)
export const payOrder = (orderId, paymentMethod) => request.post(`/orders/${orderId}/pay`, { paymentMethod })
export const confirmPickup = (orderId) => request.post(`/orders/${orderId}/pickup`)
export const cancelOrder = (orderId) => request.post(`/orders/${orderId}/cancel`)
export const getOrdersByMachine = (machineId, page = 0, size = 10) => request.get(`/orders/machine/${machineId}`, { params: { page, size } })
