import axios from 'axios'
import { ElMessage } from 'element-plus'

const request = axios.create({
  baseURL: '/api',
  timeout: 10000
})

request.interceptors.response.use(
  response => {
    const res = response.data
    if (res.code === 200) {
      return res.data
    } else {
      ElMessage.error(res.message || '请求失败')
      return Promise.reject(new Error(res.message || '请求失败'))
    }
  },
  error => {
    ElMessage.error(error.message || '网络错误')
    return Promise.reject(error)
  }
)

export const clubApi = {
  getList: (params) => request.get('/club/list', { params }),
  getById: (id) => request.get(`/club/${id}`),
  create: (data) => request.post('/club', data),
  update: (id, data) => request.put(`/club/${id}`, data),
  delete: (id) => request.delete(`/club/${id}`),
  getStatistics: () => request.get('/club/statistics'),
  getApplicationList: (params) => request.get('/club/application/list', { params }),
  approveApplication: (id, approved, opinion, approverId, approverName) => 
    request.post(`/club/application/${id}/approve`, null, { 
      params: { approved, opinion, approverId, approverName } 
    }),
  getRegistrationList: (params) => request.get('/club/registration/list', { params }),
  approveRegistration: (id, approved, opinion, approverId, approverName) =>
    request.post(`/club/registration/${id}/approve`, null, {
      params: { approved, opinion, approverId, approverName }
    }),
  getViolationList: (params) => request.get('/club/violation/list', { params }),
  createViolation: (data, handlerId, handlerName) =>
    request.post('/club/violation', data, { params: { handlerId, handlerName } }),
  markRectified: (id, note) =>
    request.post(`/club/violation/${id}/rectify`, null, { params: { note } }),
  getPendingCounts: () => request.get('/club/pending/counts'),
  getAnnualRegistrationList: (params) => request.get('/club/registration/list', { params }),
  approveAnnualRegistration: (id, approved, opinion, approverId, approverName) =>
    request.post(`/club/registration/${id}/approve`, null, {
      params: { approved, opinion, approverId, approverName }
    }),
  handleViolation: (id, action, handlerOpinion, handlerId, handlerName) =>
    request.post(`/club/violation/${id}/handle`, null, {
      params: { action, handlerOpinion, handlerId, handlerName }
    }),
  deleteViolation: (id) => request.delete(`/club/violation/${id}`),
  getAllClubs: () => request.get('/club/all')
}

export const activityApi = {
  getList: (params) => request.get('/activity/list', { params }),
  getById: (id) => request.get(`/activity/${id}`),
  create: (data) => request.post('/activity', data),
  update: (id, data) => request.put(`/activity/${id}`, data),
  delete: (id) => request.delete(`/activity/${id}`),
  approve: (id, approved, opinion, approverId, approverName) =>
    request.post(`/activity/${id}/approve`, null, {
      params: { approved, opinion, approverId, approverName }
    }),
  getStatistics: () => request.get('/activity/statistics'),
  getNonCompliant: () => request.get('/activity/non-compliant'),
  getReviewList: () => request.get('/activity/review/list'),
  getReviewStatistics: () => request.get('/activity/review/statistics'),
  approveReview: (id, reviewerId, reviewerName, opinion) =>
    request.post(`/activity/review/${id}/approve`, null, {
      params: { reviewerId, reviewerName, opinion }
    }),
  blockReview: (id, reviewerId, reviewerName, opinion) =>
    request.post(`/activity/review/${id}/block`, null, {
      params: { reviewerId, reviewerName, opinion }
    }),
  getPendingApprovalList: (params, approvalStage) =>
    request.get('/activity/approval/pending', { params: { ...params, approvalStage } }),
  getApprovalStatistics: () => request.get('/activity/approval/statistics')
}

export const financeApi = {
  getList: (params) => request.get('/finance/list', { params }),
  getById: (id) => request.get(`/finance/${id}`),
  getByClubId: (clubId) => request.get(`/finance/club/${clubId}`),
  create: (data) => request.post('/finance', data),
  update: (id, data) => request.put(`/finance/${id}`, data),
  delete: (id) => request.delete(`/finance/${id}`),
  markForInvestigation: (id) => request.post(`/finance/${id}/investigate`),
  completeInvestigation: (id, result) =>
    request.post(`/finance/${id}/complete-investigation`, null, { params: { result } }),
  getStatistics: () => request.get('/finance/statistics'),
  getAbnormal: () => request.get('/finance/abnormal'),
  getPendingInvestigation: () => request.get('/finance/pending-investigation')
}

export const systemApi = {
  getUserList: (params) => request.get('/system/user/list', { params }),
  getUserById: (id) => request.get(`/system/user/${id}`),
  createUser: (data) => request.post('/system/user', data),
  updateUser: (id, data) => request.put(`/system/user/${id}`, data),
  deleteUser: (id) => request.delete(`/system/user/${id}`),
  assignRole: (userId, role, clubId) =>
    request.post(`/system/user/${userId}/role`, null, { params: { role, clubId } }),
  getMessageTemplates: () => request.get('/system/template/list'),
  createMessageTemplate: (data) => request.post('/system/template', data),
  updateMessageTemplate: (id, data) => request.put(`/system/template/${id}`, data),
  deleteMessageTemplate: (id) => request.delete(`/system/template/${id}`),
  getTemplateList: (params) => request.get('/system/template/list', { params }),
  getTemplateById: (id) => request.get(`/system/template/${id}`),
  createTemplate: (data) => request.post('/system/template', data),
  updateTemplate: (id, data) => request.put(`/system/template/${id}`, data),
  deleteTemplate: (id) => request.delete(`/system/template/${id}`),
  getBackupList: (params) => request.get('/system/backup/list', { params }),
  createBackup: (params) => request.post('/system/backup/create', null, { params }),
  restoreBackup: (id) => request.post(`/system/backup/${id}/restore`),
  deleteBackup: (id) => request.delete(`/system/backup/${id}`),
  getOverview: () => request.get('/system/overview')
}

export default request
