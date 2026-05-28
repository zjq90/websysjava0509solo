import request from '../utils/request'

export const getFundRecordList = (params) => {
  return request({
    url: '/fund/record/list',
    method: 'GET',
    data: params
  })
}

export const addFundRecord = (data) => {
  return request({
    url: '/fund/record/add',
    method: 'POST',
    data
  })
}

export const updateFundRecord = (data) => {
  return request({
    url: '/fund/record/update',
    method: 'PUT',
    data
  })
}

export const deleteFundRecord = (id) => {
  return request({
    url: `/fund/record/${id}`,
    method: 'DELETE'
  })
}

export const getFundRecordDetail = (id) => {
  return request({
    url: `/fund/record/${id}`,
    method: 'GET'
  })
}

export const getFundSummary = (clubId) => {
  return request({
    url: '/fund/summary',
    method: 'GET',
    data: { clubId }
  })
}

export const getReimbursementList = (params) => {
  return request({
    url: '/fund/reimbursement/list',
    method: 'GET',
    data: params
  })
}

export const getMyReimbursementList = (params) => {
  return request({
    url: '/fund/reimbursement/my',
    method: 'GET',
    data: params
  })
}

export const submitReimbursement = (data) => {
  return request({
    url: '/fund/reimbursement/submit',
    method: 'POST',
    data
  })
}

export const auditReimbursement = (data) => {
  return request({
    url: '/fund/reimbursement/audit',
    method: 'POST',
    data
  })
}

export const getReimbursementDetail = (id) => {
  return request({
    url: `/fund/reimbursement/${id}`,
    method: 'GET'
  })
}

export const getFundStatistics = (params) => {
  return request({
    url: '/fund/statistics',
    method: 'GET',
    data: params
  })
}

export const getFundStatisticsList = (params) => {
  return request({
    url: '/fund/statistics/list',
    method: 'GET',
    data: params
  })
}

export const generateFundStatistics = (data) => {
  return request({
    url: '/fund/statistics/generate',
    method: 'POST',
    data
  })
}
