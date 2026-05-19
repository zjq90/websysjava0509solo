/**
 * API接口统一管理
 * 所有接口定义在此处，便于统一维护和修改
 */

import http from './request.js'

/**
 * 宠物档案管理接口
 */
export const petApi = {
  // 获取用户宠物列表
  getUserPets(userId) {
    return http.get(`pets/user/${userId}`)
  },

  // 获取宠物详情
  getPetById(petId) {
    return http.get(`pets/${petId}`)
  },

  // 添加宠物
  addPet(data) {
    return http.post('pets', data)
  },

  // 更新宠物信息
  updatePet(petId, data) {
    return http.put(`pets/${petId}`, data)
  },

  // 删除宠物
  deletePet(petId) {
    return http.delete(`pets/${petId}`)
  },

  // 获取宠物疫苗记录
  getVaccineRecords(petId) {
    return http.get(`pets/${petId}/vaccines`)
  },

  // 添加疫苗记录
  addVaccineRecord(petId, data) {
    return http.post(`pets/${petId}/vaccines`, data)
  },

  // 获取疫苗到期提醒
  getVaccineReminders(petId) {
    return http.get(`pets/${petId}/vaccine-reminders`)
  }
}

/**
 * 预约挂号接口
 */
export const appointmentApi = {
  // 获取科室列表
  getDepartments() {
    return http.get('appointments/departments')
  },

  // 获取科室医生列表
  getDoctorsByDepartment(department) {
    return http.get(`appointments/doctors/${encodeURIComponent(department)}`)
  },

  // 获取医生排班
  getDoctorSchedules(doctorId) {
    return http.get(`appointments/schedules/${doctorId}`)
  },

  // 创建预约
  createAppointment(data) {
    return http.post('appointments', data)
  },

  // 取消预约
  cancelAppointment(appointmentId, reason) {
    return http.put(`appointments/${appointmentId}/cancel`, { reason })
  },

  // 改签预约
  rescheduleAppointment(appointmentId, newTime) {
    return http.put(`appointments/${appointmentId}/reschedule`, { newTime })
  },

  // 获取用户预约列表
  getUserAppointments(userId) {
    return http.get(`appointments/user/${userId}`)
  },

  // 获取预约详情
  getAppointmentById(appointmentId) {
    return http.get(`appointments/${appointmentId}`)
  }
}

/**
 * 在线问诊接口
 */
export const consultationApi = {
  // 获取症状类型列表
  getSymptomTypes() {
    return http.get('consultations/symptom-types')
  },

  // 检查紧急症状
  checkEmergency(symptoms) {
    return http.post('consultations/check-emergency', { symptoms })
  },

  // 获取24小时医院列表
  get24hHospitals(latitude, longitude) {
    return http.get('consultations/hospitals/24h', { latitude, longitude })
  },

  // 创建问诊
  createConsultation(data) {
    return http.post('consultations', data)
  },

  // 获取用户问诊记录
  getUserConsultations(userId) {
    return http.get(`consultations/user/${userId}`)
  },

  // 获取问诊详情
  getConsultationById(consultationId) {
    return http.get(`consultations/${consultationId}`)
  },

  // 获取视频房间信息
  getVideoRoom(consultationId) {
    return http.get(`consultations/${consultationId}/video-room`)
  }
}

/**
 * 药品购买接口
 */
export const medicineApi = {
  // 获取药品分类
  getCategories() {
    return http.get('medicines/categories')
  },

  // 获取药品列表
  getMedicineList(params = {}) {
    return http.get('medicines', params)
  },

  // 获取药品详情
  getMedicineById(medicineId) {
    return http.get(`medicines/${medicineId}`)
  },

  // 获取处方列表
  getPrescriptions(userId) {
    return http.get(`medicines/prescriptions/user/${userId}`)
  },

  // 获取处方详情
  getPrescriptionById(prescriptionId) {
    return http.get(`medicines/prescriptions/${prescriptionId}`)
  },

  // 验证处方
  verifyPrescription(prescriptionNo) {
    return http.post('medicines/prescriptions/verify', { prescriptionNo })
  },

  // 创建订单
  createOrder(data) {
    return http.post('medicines/orders', data)
  },

  // 获取用户订单
  getUserOrders(userId) {
    return http.get(`medicines/orders/user/${userId}`)
  },

  // 获取用药提醒
  getMedicationReminders(userId) {
    return http.get(`medicines/reminders/user/${userId}`)
  },

  // 添加用药提醒
  addMedicationReminder(data) {
    return http.post('medicines/reminders', data)
  },

  // 更新用药提醒
  updateMedicationReminder(reminderId, data) {
    return http.put(`medicines/reminders/${reminderId}`, data)
  },

  // 删除用药提醒
  deleteMedicationReminder(reminderId) {
    return http.delete(`medicines/reminders/${reminderId}`)
  }
}

export default {
  petApi,
  appointmentApi,
  consultationApi,
  medicineApi
}
