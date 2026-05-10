/**
 * 田间记录相关API
 */
import request from '@/utils/request'

// 获取田间记录列表
export function getFieldList(params) {
  return request.get('/api/field', params)
}

// 获取田间记录详情
export function getFieldDetail(id) {
  return request.get('/api/field/' + id)
}

// 新增田间记录
export function createField(data) {
  return request.post('/api/field', data)
}

// 更新田间记录
export function updateField(id, data) {
  return request.put('/api/field/' + id, data)
}

// 删除田间记录
export function deleteField(id) {
  return request.delete('/api/field/' + id)
}
