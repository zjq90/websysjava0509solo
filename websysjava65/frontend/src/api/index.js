import request from '../utils/request'

export const getDashboard = () => request.get('/dashboard')

export const getBlacklist = () => request.get('/users/blacklist')

export const addToBlacklist = (data) => request.post('/users/blacklist', data)

export const removeFromBlacklist = (userId) => request.delete(`/users/blacklist/${userId}`)

export const getTickets = (status) => request.get('/users/tickets', { params: { status } })

export const getTicket = (id) => request.get(`/users/tickets/${id}`)

export const handleTicket = (id, data) => request.put(`/users/tickets/${id}`, data)

export const getHighFrequencyUsers = (minRides) => request.get('/users/high-frequency', { params: { minRides } })

export const giftMembership = (data) => request.post('/users/gift-membership', data)

export const getMemberships = (userId) => request.get(`/users/${userId}/memberships`)

export const getPayments = (paymentType) => request.get('/finance/payments', { params: { paymentType } })

export const getReconciliations = (status) => request.get('/finance/reconciliations', { params: { status } })

export const doReconciliation = (data) => request.post('/finance/reconciliation', data)

export const getInvoices = (status) => request.get('/finance/invoices', { params: { status } })

export const createInvoice = (data) => request.post('/finance/invoice', data)

export const batchCreateInvoices = (data) => request.post('/finance/invoices/batch', data)

export const getCostAnalysis = (startDate) => request.get('/finance/cost-analysis', { params: { startDate } })

export const getCosts = (costType) => request.get('/finance/costs', { params: { costType } })
