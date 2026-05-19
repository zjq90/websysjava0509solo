import request from '@/utils/request'

export const getDashboardStats = () => request({ url: '/statistics/dashboard', method: 'get' })

export const getDepartments = () => request({ url: '/hospital/departments', method: 'get' })
export const createDepartment = (data) => request({ url: '/hospital/departments', method: 'post', data })
export const updateDepartment = (id, data) => request({ url: `/hospital/departments/${id}`, method: 'put', data })
export const deleteDepartment = (id) => request({ url: `/hospital/departments/${id}`, method: 'delete' })

export const getSchedules = (params) => request({ url: '/hospital/schedules', method: 'get', params })
export const createSchedule = (data) => request({ url: '/hospital/schedules', method: 'post', data })
export const updateSchedule = (id, data) => request({ url: `/hospital/schedules/${id}`, method: 'put', data })
export const deleteSchedule = (id) => request({ url: `/hospital/schedules/${id}`, method: 'delete' })
export const importSchedule = (file) => {
  const formData = new FormData()
  formData.append('file', file)
  return request({ url: '/hospital/schedules/import', method: 'post', data: formData, headers: { 'Content-Type': 'multipart/form-data' } })
}

export const getMedicines = () => request({ url: '/hospital/medicines', method: 'get' })
export const getWarningMedicines = () => request({ url: '/hospital/medicines/warning', method: 'get' })
export const createMedicine = (data) => request({ url: '/hospital/medicines', method: 'post', data })
export const updateMedicine = (id, data) => request({ url: `/hospital/medicines/${id}`, method: 'put', data })
export const deleteMedicine = (id) => request({ url: `/hospital/medicines/${id}`, method: 'delete' })

export const getPetOwners = () => request({ url: '/user/owners', method: 'get' })
export const getPetOwnersByAuditStatus = (status) => request({ url: `/user/owners/audit-status/${status}`, method: 'get' })
export const createPetOwner = (data) => request({ url: '/user/owners', method: 'post', data })
export const updatePetOwner = (id, data) => request({ url: `/user/owners/${id}`, method: 'put', data })
export const deletePetOwner = (id) => request({ url: `/user/owners/${id}`, method: 'delete' })
export const auditPetOwner = (id, status) => request({ url: `/user/owners/${id}/audit`, method: 'put', params: { status } })

export const uploadFile = (file) => {
  const formData = new FormData()
  formData.append('file', file)
  return request({ url: '/file/upload', method: 'post', data: formData, headers: { 'Content-Type': 'multipart/form-data' } })
}

export const getDoctors = () => request({ url: '/user/doctors', method: 'get' })
export const getDoctorById = (id) => request({ url: `/user/doctors/${id}`, method: 'get' })
export const getDoctorsByAuditStatus = (status) => request({ url: `/user/doctors/audit-status/${status}`, method: 'get' })
export const createDoctor = (data) => request({ url: '/user/doctors', method: 'post', data })
export const updateDoctor = (id, data) => request({ url: `/user/doctors/${id}`, method: 'put', data })
export const deleteDoctor = (id) => request({ url: `/user/doctors/${id}`, method: 'delete' })
export const auditDoctor = (id, auditStatus, auditRemark) => request({ url: `/user/doctors/${id}/audit`, method: 'put', params: { auditStatus, auditRemark } })

export const getCreditRecords = (userId, userType) => request({ url: '/user/credit/records', method: 'get', params: { userId, userType } })
export const adjustCreditScore = (params) => request({ url: '/user/credit/adjust', method: 'put', params })

export const getDiseaseHeatmap = (params) => request({ url: '/statistics/disease-heatmap', method: 'get', params })
export const getUserGrowth = (params) => request({ url: '/statistics/user-growth', method: 'get', params })
export const getHospitalOperation = (params) => request({ url: '/statistics/hospital-operation', method: 'get', params })
export const exportConsultation = (params) => request({ url: '/statistics/export-consultation', method: 'get', params, responseType: 'blob' })
