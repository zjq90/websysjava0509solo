import request from '@/utils/request'

export const API = {
  currentUserId: 1,
  currentFamilyId: 1
}

export const userApi = {
  list: () => request.get('/users'),
  get: (id) => request.get(`/users/${id}`),
  create: (data) => request.post('/users', data),
  update: (id, data) => request.put(`/users/${id}`, data),
  delete: (id) => request.delete(`/users/${id}`)
}

export const debtApi = {
  list: (userId) => request.get(`/debts/user/${userId}`),
  listActive: (userId) => request.get(`/debts/user/${userId}/active`),
  get: (id) => request.get(`/debts/${id}`),
  create: (data) => request.post('/debts', data),
  update: (id, data) => request.put(`/debts/${id}`, data),
  delete: (id) => request.delete(`/debts/${id}`),
  makePayment: (debtId, data) => request.post(`/debts/${debtId}/payments`, data),
  getPayments: (debtId) => request.get(`/debts/${debtId}/payments`),
  simulate: (data) => request.post('/debts/simulate', data),
  getReminders: () => request.get('/debts/reminders')
}

export const assetApi = {
  list: (userId) => request.get(`/assets/user/${userId}`),
  listByType: (userId, type) => request.get(`/assets/user/${userId}/type/${type}`),
  get: (id) => request.get(`/assets/${id}`),
  create: (data) => request.post('/assets', data),
  update: (id, data) => request.put(`/assets/${id}`, data),
  delete: (id) => request.delete(`/assets/${id}`),
  getTotal: (userId) => request.get(`/assets/user/${userId}/total`)
}

export const investmentApi = {
  list: (userId) => request.get(`/investments/user/${userId}`),
  listByType: (userId, type) => request.get(`/investments/user/${userId}/type/${type}`),
  get: (id) => request.get(`/investments/${id}`),
  create: (data) => request.post('/investments', data),
  update: (id, data) => request.put(`/investments/${id}`, data),
  delete: (id) => request.delete(`/investments/${id}`),
  getTotalValue: (userId) => request.get(`/investments/user/${userId}/total-value`),
  getTotalProfit: (userId) => request.get(`/investments/user/${userId}/total-profit`)
}

export const netWorthApi = {
  getPersonal: (userId) => request.get(`/net-worth/user/${userId}`),
  getFamily: (familyId, operatorId) => request.get(`/net-worth/family/${familyId}?operatorId=${operatorId}`)
}

export const familyApi = {
  listByUser: (userId) => request.get(`/families/user/${userId}`),
  get: (id) => request.get(`/families/${id}`),
  create: (data, creatorId) => request.post(`/families?creatorId=${creatorId}`, data),
  update: (id, data) => request.put(`/families/${id}`, data),
  delete: (id) => request.delete(`/families/${id}`),
  getMembers: (familyId) => request.get(`/families/${familyId}/members`),
  addMember: (familyId, userId, role, operatorId) => 
    request.post(`/families/${familyId}/members?userId=${userId}&role=${role}&operatorId=${operatorId}`),
  removeMember: (familyId, userId, operatorId) =>
    request.delete(`/families/${familyId}/members/${userId}?operatorId=${operatorId}`),
  updateMemberRole: (familyId, userId, role, operatorId) =>
    request.put(`/families/${familyId}/members/${userId}/role?role=${role}&operatorId=${operatorId}`),
  isAdmin: (familyId, userId) => request.get(`/families/${familyId}/members/${userId}/is-admin`)
}

export const budgetApi = {
  listByFamily: (familyId, month) => {
    const url = month ? `/budgets/family/${familyId}?month=${month}` : `/budgets/family/${familyId}`
    return request.get(url)
  },
  get: (id) => request.get(`/budgets/${id}`),
  create: (data, operatorId) => request.post(`/budgets?operatorId=${operatorId}`, data),
  update: (id, data, operatorId) => request.put(`/budgets/${id}?operatorId=${operatorId}`, data),
  delete: (id, operatorId) => request.delete(`/budgets/${id}?operatorId=${operatorId}`),
  getExpenses: (budgetId) => request.get(`/budgets/${budgetId}/expenses`),
  getFamilyExpenses: (familyId) => request.get(`/budgets/family/${familyId}/expenses`),
  getUserExpenses: (userId) => request.get(`/budgets/user/${userId}/expenses`),
  addExpense: (data, operatorId) => request.post(`/budgets/expenses?operatorId=${operatorId}`, data),
  deleteExpense: (expenseId, operatorId) => request.delete(`/budgets/expenses/${expenseId}?operatorId=${operatorId}`)
}

export const virtualAccountApi = {
  listByUser: (userId) => request.get(`/virtual-accounts/user/${userId}`),
  listByFamily: (familyId) => request.get(`/virtual-accounts/family/${familyId}`),
  listByUserAndFamily: (userId, familyId) => request.get(`/virtual-accounts/user/${userId}/family/${familyId}`),
  get: (id) => request.get(`/virtual-accounts/${id}`),
  create: (data, operatorId) => request.post(`/virtual-accounts?operatorId=${operatorId}`, data),
  update: (id, data, operatorId) => request.put(`/virtual-accounts/${id}?operatorId=${operatorId}`, data),
  delete: (id, operatorId) => request.delete(`/virtual-accounts/${id}?operatorId=${operatorId}`),
  getTransactions: (accountId) => request.get(`/virtual-accounts/${accountId}/transactions`),
  createTransaction: (accountId, data, operatorId) => 
    request.post(`/virtual-accounts/${accountId}/transactions?operatorId=${operatorId}`, data),
  deleteTransaction: (transactionId, operatorId) =>
    request.delete(`/virtual-accounts/transactions/${transactionId}?operatorId=${operatorId}`)
}

export const notificationApi = {
  list: (userId) => request.get(`/notifications/user/${userId}`),
  listUnread: (userId) => request.get(`/notifications/user/${userId}/unread`),
  getUnreadCount: (userId) => request.get(`/notifications/user/${userId}/unread-count`),
  markAsRead: (id) => request.put(`/notifications/${id}/read`),
  markAllAsRead: (userId) => request.put(`/notifications/user/${userId}/read-all`),
  delete: (id) => request.delete(`/notifications/${id}`)
}
