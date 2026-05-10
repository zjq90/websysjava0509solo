const BASE_URL = 'http://localhost:8080/api';

const request = (url, method = 'GET', data = {}) => {
    return new Promise((resolve, reject) => {
        uni.showLoading({ title: '加载中...' });
        
        uni.request({
            url: BASE_URL + url,
            method: method,
            data: data,
            header: {
                'Content-Type': 'application/json'
            },
            success: (res) => {
                uni.hideLoading();
                if (res.statusCode === 200) {
                    if (res.data && res.data.success) {
                        resolve(res.data);
                    } else {
                        uni.showToast({
                            title: res.data.message || '请求失败',
                            icon: 'none',
                            duration: 2000
                        });
                        reject(res.data);
                    }
                } else {
                    uni.showToast({
                        title: '服务器错误',
                        icon: 'none',
                        duration: 2000
                    });
                    reject(res);
                }
            },
            fail: (err) => {
                uni.hideLoading();
                uni.showToast({
                    title: '网络错误',
                    icon: 'none',
                    duration: 2000
                });
                reject(err);
            }
        });
    });
};

export default {
    get: (url, data) => request(url, 'GET', data),
    post: (url, data) => request(url, 'POST', data),
    put: (url, data) => request(url, 'PUT', data),
    delete: (url) => request(url, 'DELETE')
};
