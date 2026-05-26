import request from '@/utils/request'

export function listCategories() {
  return request({
    url: '/categories',
    method: 'get'
  })
}

export function listCategoriesByType(type) {
  return request({
    url: `/categories/type/${type}`,
    method: 'get'
  })
}

export function getCategory(id) {
  return request({
    url: `/categories/${id}`,
    method: 'get'
  })
}

export function addCategory(data) {
  return request({
    url: '/categories',
    method: 'post',
    data
  })
}

export function updateCategory(data) {
  return request({
    url: '/categories',
    method: 'put',
    data
  })
}

export function deleteCategory(id) {
  return request({
    url: `/categories/${id}`,
    method: 'delete'
  })
}
