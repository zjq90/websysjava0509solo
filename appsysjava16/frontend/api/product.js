import request from '@/utils/request.js'

const productApi = {
  getAllProducts() {
    return request({
      url: '/products',
      method: 'GET'
    })
  },

  getProductById(id) {
    return request({
      url: `/products/${id}`,
      method: 'GET'
    })
  },

  getProductByBatchNumber(batchNumber) {
    return request({
      url: `/products/batch/${batchNumber}`,
      method: 'GET'
    })
  },

  searchProducts(name) {
    return request({
      url: '/products/search',
      method: 'GET',
      params: { name }
    })
  },

  getAvailableProducts() {
    return request({
      url: '/products/available',
      method: 'GET'
    })
  },

  getProductsByCategory(category) {
    return request({
      url: `/products/category/${category}`,
      method: 'GET'
    })
  },

  createProduct(data) {
    return request({
      url: '/products',
      method: 'POST',
      data
    })
  },

  updateProduct(id, data) {
    return request({
      url: `/products/${id}`,
      method: 'PUT',
      data
    })
  },

  deleteProduct(id) {
    return request({
      url: `/products/${id}`,
      method: 'DELETE'
    })
  },

  updateStock(id, quantity) {
    return request({
      url: `/products/${id}/stock`,
      method: 'PUT',
      params: { quantity }
    })
  }
}

export default productApi
