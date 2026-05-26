import request from '@/utils/request'

/**
 * API接口封装
 * 集中管理所有后端API接口
 */

// 仪表盘相关API
export const dashboardApi = {
  // 获取仪表盘概览数据
  getOverview: () => request.get('/dashboard/overview'),
  
  // 获取月度消费趋势
  getMonthlyTrend: (months = 12) => request.get('/dashboard/monthly-trend', { params: { months } }),
  
  // 获取异常交易
  getAbnormalTransactions: () => request.get('/dashboard/abnormal-transactions'),
  
  // 检测异常交易
  detectAbnormal: () => request.post('/dashboard/detect-abnormal'),
  
  // 获取支出热力图
  getHeatmap: (days = 90) => request.get('/dashboard/heatmap', { params: { days } }),
  
  // 获取分类对比
  getCategoryComparison: (months = 3) => request.get('/dashboard/category-comparison', { params: { months } }),
  
  // 获取资产趋势
  getAssetTrend: (months = 12) => request.get('/dashboard/asset-trend', { params: { months } }),
  
  // 获取最烧钱分类
  getTopSpending: (months = 3) => request.get('/dashboard/top-spending', { params: { months } })
}

// 交易记录相关API
export const transactionApi = {
  // 分页查询
  getList: (params) => request.get('/transactions', { params }),
  
  // 按时间范围查询
  getByRange: (start, end) => request.get('/transactions/range', { params: { start, end } }),
  
  // 获取详情
  getDetail: (id) => request.get(`/transactions/${id}`),
  
  // 创建
  create: (data) => request.post('/transactions', data),
  
  // 更新
  update: (id, data) => request.put(`/transactions/${id}`, data),
  
  // 删除
  delete: (id) => request.delete(`/transactions/${id}`),
  
  // 批量删除
  batchDelete: (ids) => request.delete('/transactions/batch', { data: ids }),
  
  // 批量更新分类
  batchUpdateCategory: (data) => request.post('/transactions/batch/update-category', data),
  
  // 批量更新标签
  batchUpdateTags: (data) => request.post('/transactions/batch/update-tags', data),
  
  // 复制交易记录
  copy: (id) => request.post(`/transactions/${id}/copy`)
}

// 分类相关API
export const categoryApi = {
  // 获取所有分类
  getAll: () => request.get('/categories'),
  getAllCategories: () => request.get('/categories'),
  
  // 按类型获取分类
  getByType: (type) => request.get(`/categories/type/${type}`),
  
  // 获取详情
  getDetail: (id) => request.get(`/categories/${id}`),
  
  // 创建
  create: (data) => request.post('/categories', data),
  
  // 更新
  update: (id, data) => request.put(`/categories/${id}`, data),
  
  // 删除
  delete: (id) => request.delete(`/categories/${id}`)
}

// 账户相关API
export const accountApi = {
  // 获取所有账户
  getAll: () => request.get('/accounts'),
  
  // 获取详情
  getDetail: (id) => request.get(`/accounts/${id}`),
  
  // 获取总资产
  getTotalAssets: () => request.get('/accounts/total-assets'),
  
  // 创建
  create: (data) => request.post('/accounts', data),
  
  // 更新
  update: (id, data) => request.put(`/accounts/${id}`, data),
  
  // 删除
  delete: (id) => request.delete(`/accounts/${id}`),
  
  // 调整余额
  adjustBalance: (id, newBalance) => request.put(`/accounts/${id}/adjust-balance`, null, { params: { newBalance } })
}

// 预算相关API
export const budgetApi = {
  // 获取月度预算列表
  getByMonth: (month) => request.get(`/budgets/${month}`),
  
  // 获取预算进度
  getProgress: (month) => request.get(`/budgets/${month}/progress`),
  getBudgetProgress: (month) => request.get(`/budgets/${month}/progress`),
  
  // 设置预算
  set: (params) => request.post('/budgets', null, { params }),
  createBudget: (params) => request.post('/budgets', null, { params }),
  updateBudget: (id, params) => request.post('/budgets', null, { params }),
  
  // 删除预算
  delete: (id) => request.delete(`/budgets/${id}`),
  deleteBudget: (id) => request.delete(`/budgets/${id}`),
  
  // 复制上月预算
  copyLastMonth: (targetMonth) => request.post('/budgets/copy-last-month', null, { params: { targetMonth } })
}

// 储蓄目标相关API
export const savingGoalApi = {
  // 获取所有目标
  getAll: () => request.get('/saving-goals'),
  getAllSavingGoals: () => request.get('/saving-goals'),
  
  // 获取进行中的目标
  getActive: () => request.get('/saving-goals/active'),
  
  // 获取已完成的目标
  getCompleted: () => request.get('/saving-goals/completed'),
  
  // 获取详情
  getDetail: (id) => request.get(`/saving-goals/${id}`),
  
  // 创建
  create: (data) => request.post('/saving-goals', data),
  createSavingGoal: (data) => request.post('/saving-goals', data),
  
  // 更新
  update: (id, data) => request.put(`/saving-goals/${id}`, data),
  updateSavingGoal: (id, data) => request.put(`/saving-goals/${id}`, data),
  
  // 删除
  delete: (id) => request.delete(`/saving-goals/${id}`),
  
  // 存款
  deposit: (id, amount) => request.post(`/saving-goals/${id}/deposit`, null, { params: { amount } }),
  updateProgress: (id, { amount }) => request.post(`/saving-goals/${id}/deposit`, null, { params: { amount } }),
  
  // 取款
  withdraw: (id, amount) => request.post(`/saving-goals/${id}/withdraw`, null, { params: { amount } }),
  
  // 获取进度百分比
  getProgress: (id) => request.get(`/saving-goals/${id}/progress`)
}

// Excel相关API
export const excelApi = {
  // 导出Excel
  export: (start, end) => request.get('/excel/export', { 
    params: { start, end },
    responseType: 'blob'
  }),
  
  // 预览导入数据
  previewImport: (file, defaultAccountId) => {
    const formData = new FormData()
    formData.append('file', file)
    return request.post('/excel/import/preview', formData, { 
      params: { defaultAccountId },
      headers: { 'Content-Type': 'multipart/form-data' }
    })
  },
  
  // 确认导入
  confirmImport: (data) => request.post('/excel/import', data)
}

// 看板模板相关API
export const templateApi = {
  // 获取所有模板
  getAll: () => request.get('/dashboard-templates'),
  
  // 获取默认模板
  getDefault: () => request.get('/dashboard-templates/default'),
  
  // 获取详情
  getDetail: (id) => request.get(`/dashboard-templates/${id}`),
  
  // 保存模板
  save: (data) => request.post('/dashboard-templates', data),
  
  // 更新模板
  update: (id, data) => request.put(`/dashboard-templates/${id}`, data),
  
  // 删除模板
  delete: (id) => request.delete(`/dashboard-templates/${id}`),
  
  // 设为默认
  setDefault: (id) => request.put(`/dashboard-templates/${id}/set-default`)
}

// 报表相关API
export const reportApi = {
  // 获取资产趋势
  getAssetTrend: (months = 12) => request.get('/dashboard/asset-trend', { params: { months } }),
  
  // 获取支出热力图
  getExpenseHeatmap: (days = 90) => request.get('/dashboard/heatmap', { params: { days } }),
  
  // 导出报表
  exportReport: (params) => request.get('/excel/export', { 
    params,
    responseType: 'blob'
  }),
  
  // 获取分类对比
  getCategoryComparison: (months = 3) => request.get('/dashboard/category-comparison', { params: { months } }),
  
  // 获取最烧钱分类
  getTopSpending: (months = 3) => request.get('/dashboard/top-spending', { params: { months } })
}
