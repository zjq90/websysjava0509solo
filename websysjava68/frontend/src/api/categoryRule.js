import request from '@/utils/request'

export function listCategoryRules() {
  return request({
    url: '/category-rules',
    method: 'get'
  })
}

export function listEnabledCategoryRules() {
  return request({
    url: '/category-rules/enabled',
    method: 'get'
  })
}

export function getCategoryRule(id) {
  return request({
    url: `/category-rules/${id}`,
    method: 'get'
  })
}

export function addCategoryRule(data) {
  return request({
    url: '/category-rules',
    method: 'post',
    data
  })
}

export function updateCategoryRule(data) {
  return request({
    url: '/category-rules',
    method: 'put',
    data
  })
}

export function deleteCategoryRule(id) {
  return request({
    url: `/category-rules/${id}`,
    method: 'delete'
  })
}

export function testCategoryRule(data) {
  return request({
    url: '/category-rules/test',
    method: 'post',
    data
  })
}
