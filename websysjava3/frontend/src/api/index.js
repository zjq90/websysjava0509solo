import request from '@/utils/request'

export const exceptionOrderApi = {
  getPage: (params) => request.get('/exception-orders/page', { params }),
  getDetail: (id) => request.get(`/exception-orders/${id}`),
  create: (data) => request.post('/exception-orders', data),
  handle: (data) => request.post('/exception-orders/handle', data),
  delete: (id) => request.delete(`/exception-orders/${id}`),
  batchDelete: (ids) => request.delete('/exception-orders/batch', { data: ids })
}

export const reconciliationApi = {
  getPage: (params) => request.get('/reconciliations/page', { params }),
  getDetail: (id) => request.get(`/reconciliations/${id}`),
  getDetails: (id) => request.get(`/reconciliations/${id}/details`),
  execute: (data) => request.post('/reconciliations/execute', data),
  delete: (id) => request.delete(`/reconciliations/${id}`),
  batchDelete: (ids) => request.delete('/reconciliations/batch', { data: ids })
}

export const invoiceApi = {
  getPage: (params) => request.get('/invoices/page', { params }),
  getDetail: (id) => request.get(`/invoices/${id}`),
  create: (data) => request.post('/invoices', data),
  audit: (data) => request.post('/invoices/audit', data),
  issue: (data) => request.post('/invoices/issue', data),
  delete: (id) => request.delete(`/invoices/${id}`),
  batchDelete: (ids) => request.delete('/invoices/batch', { data: ids })
}

export const salesReportApi = {
  getSalesReport: (params) => request.get('/sales-report', { params }),
  getDashboardStats: () => request.get('/sales-report/dashboard')
}
