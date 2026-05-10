import request from '../utils/request';

export const customerApi = {
    getList: () => request.get('/customers'),
    getDetail: (id) => request.get('/customers/' + id),
    create: (data) => request.post('/customers', data),
    update: (id, data) => request.put('/customers/' + id, data),
    delete: (id) => request.delete('/customers/' + id)
};

export const productApi = {
    getList: () => request.get('/products'),
    getDetail: (id) => request.get('/products/' + id),
    getByBatch: (batchNumber) => request.get('/products/batch/' + batchNumber),
    create: (data) => request.post('/products', data),
    update: (id, data) => request.put('/products/' + id, data),
    delete: (id) => request.delete('/products/' + id)
};

export const employeeApi = {
    getList: () => request.get('/employees'),
    getDetail: (id) => request.get('/employees/' + id),
    create: (data) => request.post('/employees', data),
    update: (id, data) => request.put('/employees/' + id, data),
    delete: (id) => request.delete('/employees/' + id)
};

export const financeApi = {
    getPerformance: (employeeId, params = {}) => request.get('/finance/performance/' + employeeId, params),
    getOrders: () => request.get('/finance/orders'),
    createOrder: (data) => request.post('/finance/orders', data),
    getOrderDetail: (id) => request.get('/finance/orders/' + id),
    updateOrder: (id, data) => request.put('/finance/orders/' + id, data),
    deleteOrder: (id) => request.delete('/finance/orders/' + id)
};

export const dashboardApi = {
    getData: (params = {}) => request.get('/dashboard', params)
};
