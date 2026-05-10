import request from '../utils/request'

export const getProducts = () => request.get('/products')
export const getProductById = (id) => request.get(`/products/${id}`)
export const searchProducts = (keyword) => request.get('/products/search', { params: { keyword } })
export const createProduct = (data, categoryId) => request.post('/products', data, { params: { categoryId } })
export const updateProduct = (id, data, categoryId) => request.put(`/products/${id}`, data, { params: { categoryId } })
export const deleteProduct = (id) => request.delete(`/products/${id}`)
export const batchUpdateStatus = (ids, active) => request.post('/products/batch-status', { ids, active })

export const getCategories = () => request.get('/categories')
export const createCategory = (data) => request.post('/categories', data)
export const updateCategory = (id, data) => request.put(`/categories/${id}`, data)
export const deleteCategory = (id) => request.delete(`/categories/${id}`)
