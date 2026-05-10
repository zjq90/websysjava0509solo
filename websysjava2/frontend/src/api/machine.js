import request from '../utils/request'

export const getMachines = () => request.get('/machines')
export const getMachineById = (id) => request.get(`/machines/${id}`)
export const createMachine = (data) => request.post('/machines', data)
export const updateMachine = (id, data) => request.put(`/machines/${id}`, data)
export const deleteMachine = (id) => request.delete(`/machines/${id}`)
export const updateMachineStatus = (id, status) => request.put(`/machines/${id}/status`, { status })

export const getSlotsByMachine = (machineId) => request.get(`/slots/machine/${machineId}`)
export const getSlotById = (id) => request.get(`/slots/${id}`)
export const bindProductToSlot = (slotId, productId) => request.put(`/slots/${slotId}/bind-product`, null, { params: { productId } })
export const batchBindProduct = (machineId, slotNumbers, productId) => request.post('/slots/batch-bind', { machineId, slotNumbers, productId })
export const updateSlotStock = (slotId, stock) => request.put(`/slots/${slotId}/stock`, { stock })
export const deductSlotStock = (slotId, quantity) => request.put(`/slots/${slotId}/deduct-stock`, { quantity })
export const refillSlotStock = (slotId, quantity) => request.put(`/slots/${slotId}/refill`, { quantity })
export const getLowStockSlots = () => request.get('/slots/low-stock')
export const getLowStockSlotsByMachine = (machineId) => request.get(`/slots/low-stock/machine/${machineId}`)
