import request from './request.js'

export default {
    auth: {
        login(data) {
            return request.post('/auth/login', data)
        },
        register(data) {
            return request.post('/auth/register', data)
        },
        logout() {
            return request.post('/auth/logout')
        },
        getCurrentUser() {
            return request.get('/auth/me')
        }
    },
    admission: {
        list(data) {
            return request.get('/admission/my', data)
        },
        detail(id) {
            return request.get('/admission/' + id)
        },
        create(data) {
            return request.post('/admission', data)
        },
        cancel(id) {
            return request.post('/admission/cancel/' + id)
        },
        stats() {
            return request.get('/admission/stats')
        }
    },
    fee: {
        list(data) {
            return request.get('/hospital-fee/my', data)
        },
        summary(data) {
            return request.get('/hospital-fee/summary', data)
        },
        byDate(data) {
            return request.get('/hospital-fee/by-date', data)
        }
    },
    deposit: {
        list(data) {
            return request.get('/deposit/my', data)
        },
        create(data) {
            return request.post('/deposit', data)
        },
        pay(orderNo) {
            return request.post('/deposit/pay/' + orderNo)
        },
        cancel(orderNo) {
            return request.post('/deposit/cancel/' + orderNo)
        },
        detail(orderNo) {
            return request.get('/deposit/' + orderNo)
        }
    },
    department: {
        list() {
            return request.get('/department/list')
        },
        all() {
            return request.get('/department/all')
        }
    },
    user: {
        updateProfile(data) {
            return request.put('/user/profile', data)
        },
        toggleElderMode(mode) {
            return request.put('/user/elder-mode?mode=' + mode)
        },
        getElderMode() {
            return request.get('/user/elder-mode')
        },
        changePassword(data) {
            return request.put('/user/password', data)
        }
    },
    audit: {
        myLogs(data) {
            return request.get('/audit-log/my', data)
        }
    },
    public: {
        health() {
            return request.get('/public/health')
        },
        config() {
            return request.get('/public/config')
        },
        errorMessages() {
            return request.get('/public/error-messages')
        }
    }
}
