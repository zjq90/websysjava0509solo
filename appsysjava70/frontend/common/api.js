const BASE_URL = 'http://localhost:8080/api'

function request(options) {
    return new Promise((resolve, reject) => {
        uni.showLoading({ title: '加载中...', mask: true })

        uni.request({
            url: BASE_URL + options.url,
            method: options.method || 'GET',
            data: options.data || {},
            header: {
                'Content-Type': 'application/json',
                ...options.header
            },
            success: (res) => {
                uni.hideLoading()
                if (res.statusCode === 200) {
                    if (res.data.code === 200) {
                        resolve(res.data.data)
                    } else {
                        uni.showToast({
                            title: res.data.message || '请求失败',
                            icon: 'none'
                        })
                        reject(res.data)
                    }
                } else {
                    uni.showToast({
                        title: '网络错误: ' + res.statusCode,
                        icon: 'none'
                    })
                    reject(res)
                }
            },
            fail: (err) => {
                uni.hideLoading()
                console.error('API请求失败:', err)
                reject(err)
            }
        })
    })
}

export default {
    getDashboard() {
        return request({ url: '/dashboard', method: 'GET' })
    },

    getBills() {
        return request({ url: '/bills', method: 'GET' })
    },

    getRecentBills(days) {
        return request({ url: `/bills/recent/${days}`, method: 'GET' })
    },

    getBillsByRange(startDate, endDate) {
        return request({
            url: `/bills/range?startDate=${startDate}&endDate=${endDate}`,
            method: 'GET'
        })
    },

    getBillById(id) {
        return request({ url: `/bills/${id}`, method: 'GET' })
    },

    createBill(data) {
        return request({ url: '/bills', method: 'POST', data })
    },

    updateBill(id, data) {
        return request({ url: `/bills/${id}`, method: 'PUT', data })
    },

    updateBillCategory(id, categoryId) {
        return request({
            url: `/bills/${id}/category?categoryId=${categoryId}`,
            method: 'PATCH'
        })
    },

    deleteBill(id) {
        return request({ url: `/bills/${id}`, method: 'DELETE' })
    },

    syncBills(data) {
        return request({ url: '/bills/sync', method: 'POST', data })
    },

    getCategories() {
        return request({ url: '/categories', method: 'GET' })
    },

    getCategoriesByType(type) {
        return request({ url: `/categories/type/${type}`, method: 'GET' })
    },

    createCategory(data) {
        return request({ url: '/categories', method: 'POST', data })
    },

    updateCategory(id, data) {
        return request({ url: `/categories/${id}`, method: 'PUT', data })
    },

    deleteCategory(id) {
        return request({ url: `/categories/${id}`, method: 'DELETE' })
    },

    getAccounts() {
        return request({ url: '/accounts', method: 'GET' })
    },

    getAccountById(id) {
        return request({ url: `/accounts/${id}`, method: 'GET' })
    },

    getTotalBalance() {
        return request({ url: '/accounts/total-balance', method: 'GET' })
    },

    createAccount(data) {
        return request({ url: '/accounts', method: 'POST', data })
    },

    updateAccount(id, data) {
        return request({ url: `/accounts/${id}`, method: 'PUT', data })
    },

    deleteAccount(id) {
        return request({ url: `/accounts/${id}`, method: 'DELETE' })
    },

    getTemplates() {
        return request({ url: '/templates', method: 'GET' })
    },

    getTemplateById(id) {
        return request({ url: `/templates/${id}`, method: 'GET' })
    },

    createTemplate(data) {
        return request({ url: '/templates', method: 'POST', data })
    },

    updateTemplate(id, data) {
        return request({ url: `/templates/${id}`, method: 'PUT', data })
    },

    useTemplate(id) {
        return request({ url: `/templates/${id}/use`, method: 'POST' })
    },

    deleteTemplate(id) {
        return request({ url: `/templates/${id}`, method: 'DELETE' })
    },

    getBudgets() {
        return request({ url: '/budgets', method: 'GET' })
    },

    getActiveBudgets() {
        return request({ url: '/budgets/active', method: 'GET' })
    },

    getBudgetById(id) {
        return request({ url: `/budgets/${id}`, method: 'GET' })
    },

    getBudgetUsage(id) {
        return request({ url: `/budgets/${id}/usage`, method: 'GET' })
    },

    createBudget(data) {
        return request({ url: '/budgets', method: 'POST', data })
    },

    updateBudget(id, data) {
        return request({ url: `/budgets/${id}`, method: 'PUT', data })
    },

    deleteBudget(id) {
        return request({ url: `/budgets/${id}`, method: 'DELETE' })
    },

    voiceRecognition(text) {
        return request({
            url: `/recognition/voice?text=${encodeURIComponent(text)}`,
            method: 'POST'
        })
    },

    ocrRecognition(imageUrl) {
        return request({
            url: `/recognition/ocr?imageUrl=${encodeURIComponent(imageUrl)}`,
            method: 'POST'
        })
    }
}
