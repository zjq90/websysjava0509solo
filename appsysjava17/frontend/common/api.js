/**
 * API请求封装
 * 统一处理所有API请求，包括：
 * - 基础URL配置
 * - 请求拦截
 * - 响应拦截
 * - 错误处理
 */

// 基础URL（可从本地存储读取）
const getBaseUrl = () => {
    return uni.getStorageSync('apiBaseUrl') || 'http://localhost:8080/api';
};

/**
 * 通用请求方法
 * @param {string} url - 请求路径
 * @param {string} method - 请求方法
 * @param {object} data - 请求数据
 * @param {object} header - 请求头
 */
const request = (url, method = 'GET', data = {}, header = {}) => {
    return new Promise((resolve, reject) => {
        const baseUrl = getBaseUrl();
        const fullUrl = baseUrl + url;
        
        console.log(`[API] ${method} ${fullUrl}`);
        console.log('[API] Request Data:', data);
        
        uni.request({
            url: fullUrl,
            method: method,
            data: data,
            header: {
                'Content-Type': 'application/json',
                ...header
            },
            success: (res) => {
                console.log('[API] Response:', res);
                
                if (res.statusCode === 200) {
                    // 检查业务状态码
                    if (res.data.code === 200) {
                        resolve(res.data);
                    } else {
                        uni.showToast({
                            title: res.data.message || '请求失败',
                            icon: 'none',
                            duration: 2000
                        });
                        reject(res.data);
                    }
                } else if (res.statusCode === 404) {
                    uni.showToast({
                        title: '资源不存在',
                        icon: 'none',
                        duration: 2000
                    });
                    reject(res);
                } else if (res.statusCode === 500) {
                    uni.showToast({
                        title: '服务器错误',
                        icon: 'none',
                        duration: 2000
                    });
                    reject(res);
                } else {
                    uni.showToast({
                        title: '请求失败，状态码：' + res.statusCode,
                        icon: 'none',
                        duration: 2000
                    });
                    reject(res);
                }
            },
            fail: (err) => {
                console.error('[API] Error:', err);
                uni.showToast({
                    title: '网络错误，请检查网络连接',
                    icon: 'none',
                    duration: 2000
                });
                reject(err);
            }
        });
    });
};

/**
 * 溯源查询API
 */
export const traceApi = {
    // 查询溯源信息
    queryTrace: (batchCode) => {
        return request(`/trace/query/${batchCode}`, 'GET');
    },
    
    // 导出PDF报告
    exportPdf: (batchCode) => {
        const baseUrl = getBaseUrl();
        return `${baseUrl}/trace/export/${batchCode}`;
    }
};

/**
 * 批次管理API
 */
export const batchApi = {
    // 获取所有批次
    getAllBatches: () => {
        return request('/batches', 'GET');
    },
    
    // 根据ID获取批次
    getBatchById: (id) => {
        return request(`/batches/${id}`, 'GET');
    },
    
    // 根据批次号获取批次
    getBatchByCode: (batchCode) => {
        return request(`/batches/code/${batchCode}`, 'GET');
    },
    
    // 检查批次号是否存在
    checkBatchCode: (batchCode) => {
        return request(`/batches/check-code/${batchCode}`, 'GET');
    },
    
    // 创建批次
    createBatch: (data) => {
        return request('/batches', 'POST', data);
    },
    
    // 更新批次
    updateBatch: (id, data) => {
        return request(`/batches/${id}`, 'PUT', data);
    },
    
    // 删除批次
    deleteBatch: (id) => {
        return request(`/batches/${id}`, 'DELETE');
    }
};

export default {
    request,
    traceApi,
    batchApi
};
