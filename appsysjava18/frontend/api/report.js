/**
 * 报表相关API
 */
import request from '@/utils/request'

// 获取综合报表
export function getReport() {
  return request.get('/api/report')
}
