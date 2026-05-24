import { get, post } from '../utils/request'

export const submitFaultReport = (data) => {
  return post('/fault/report', data)
}

export const getMyFaultReports = (pageNum, pageSize) => {
  return get('/fault/my-reports', { pageNum, pageSize })
}

export const getFaultReportDetail = (reportId) => {
  return get(`/fault/report/${reportId}`)
}
