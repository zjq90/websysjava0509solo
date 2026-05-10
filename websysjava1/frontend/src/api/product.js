import request from '@/utils/request'

export function getProductList(params) {
  return request({
    url: '/product/page',
    method: 'get',
    params
  })
}

export function getAllProducts() {
  return request({
    url: '/product/list',
    method: 'get'
  })
}

export function getProduct(id) {
  return request({
    url: `/product/${id}`,
    method: 'get'
  })
}

export function createProduct(data) {
  return request({
    url: '/product',
    method: 'post',
    data
  })
}

export function updateProduct(data) {
  return request({
    url: '/product',
    method: 'put',
    data
  })
}

export function deleteProduct(id) {
  return request({
    url: `/product/${id}`,
    method: 'delete'
  })
}

export function updateProductStatus(id, status) {
  return request({
    url: `/product/${id}/status/${status}`,
    method: 'put'
  })
}
