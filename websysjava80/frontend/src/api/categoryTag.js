import request from '@/utils/request'

export function getAllCategories() {
  return request({
    url: '/category-tag/categories',
    method: 'get'
  })
}

export function saveCategory(data) {
  return request({
    url: '/category-tag/categories',
    method: 'post',
    data
  })
}

export function updateCategory(id, data) {
  return request({
    url: `/category-tag/categories/${id}`,
    method: 'put',
    data
  })
}

export function deleteCategory(id) {
  return request({
    url: `/category-tag/categories/${id}`,
    method: 'delete'
  })
}

export function getAllTags() {
  return request({
    url: '/category-tag/tags',
    method: 'get'
  })
}

export function saveTag(data) {
  return request({
    url: '/category-tag/tags',
    method: 'post',
    data
  })
}

export function updateTag(id, data) {
  return request({
    url: `/category-tag/tags/${id}`,
    method: 'put',
    data
  })
}

export function deleteTag(id) {
  return request({
    url: `/category-tag/tags/${id}`,
    method: 'delete'
  })
}

export function mergeTags(targetId, sourceIds) {
  return request({
    url: '/category-tag/tags/merge',
    method: 'post',
    params: { targetId, sourceIds: sourceIds.join(',') }
  })
}

export function cleanLowUsageTags(minGameCount) {
  return request({
    url: '/category-tag/tags/clean',
    method: 'post',
    params: { minGameCount }
  })
}
