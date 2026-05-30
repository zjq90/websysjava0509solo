import request from '@/utils/request'

export function getRecommendations(type, categoryId) {
  return request({
    url: '/recommendations',
    method: 'get',
    params: { type, categoryId }
  })
}

export function getHomeRecommendations() {
  return request({
    url: '/recommendations/home',
    method: 'get'
  })
}

export function getCategoryRecommendations(categoryId) {
  return request({
    url: `/recommendations/category/${categoryId}`,
    method: 'get'
  })
}

export function getPopupRecommendation() {
  return request({
    url: '/recommendations/popup',
    method: 'get'
  })
}

export function saveRecommendation(data) {
  return request({
    url: '/recommendations',
    method: 'post',
    data
  })
}

export function updateRecommendation(id, data) {
  return request({
    url: `/recommendations/${id}`,
    method: 'put',
    data
  })
}

export function deleteRecommendation(id) {
  return request({
    url: `/recommendations/${id}`,
    method: 'delete'
  })
}

export function validateRecommendations() {
  return request({
    url: '/recommendations/validate',
    method: 'post'
  })
}
