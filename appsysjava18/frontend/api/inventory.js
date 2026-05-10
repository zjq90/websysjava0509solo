/**
 * 库存相关API
 */
import request from '@/utils/request'

// 获取库存列表
export function getInventoryList(params) {
  return request.get('/api/inventory', params)
}

// 获取库存详情
export function getInventoryDetail(id) {
  return request.get('/api/inventory/' + id)
}

// 新增库存
export function createInventory(data) {
  return request.post('/api/inventory', data)
}

// 更新库存
export function updateInventory(id, data) {
  return request.put('/api/inventory/' + id, data)
}

// 删除库存
export function deleteInventory(id) {
  return request.delete('/api/inventory/' + id)
}

// 获取种子列表
export function getSeedList() {
  return request.get('/api/seed')
}

// 获取种子详情
export function getSeedDetail(id) {
  return request.get('/api/seed/' + id)
}

// 新增种子
export function createSeed(data) {
  return request.post('/api/seed', data)
}

// 更新种子
export function updateSeed(id, data) {
  return request.put('/api/seed/' + id, data)
}

// 删除种子
export function deleteSeed(id) {
  return request.delete('/api/seed/' + id)
}
