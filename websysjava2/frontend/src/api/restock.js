import request from '../utils/request'

export const getRestocks = () => request.get('/restocks')
export const getRestockById = (id) => request.get(`/restocks/${id}`)
export const autoGenerateRestocks = () => request.post('/restocks/auto-generate')
export const createManualRestock = (data) => request.post('/restocks/manual', data)
export const completeRestock = (id, operator) => request.post(`/restocks/${id}/complete`, { operator })
export const cancelRestock = (id, reason) => request.post(`/restocks/${id}/cancel`, { reason })
export const markNotified = (id) => request.post(`/restocks/${id}/mark-notified`)
export const getUnnotifiedRestocks = () => request.get('/restocks/unnotified')
export const getPendingCount = () => request.get('/restocks/pending-count')
