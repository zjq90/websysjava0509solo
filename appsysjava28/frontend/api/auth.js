import request from '@/utils/request'

export const sendSmsCode = (phone, type = 'LOGIN') => {
    return request.post('/auth/sms-code', { phone, type })
}

export const loginWithSms = (phone, code) => {
    return request.post('/auth/login/sms', { phone, code })
}

export const loginWithWechat = (code, nickname, avatar) => {
    return request.post('/auth/login/wechat', { code, nickname, avatar })
}

export const getCurrentUser = () => {
    return request.get('/users/me')
}

export const updateProfile = (nickname, avatar) => {
    return request.put('/users/me', { nickname, avatar })
}

export const toggleElderlyMode = () => {
    return request.post('/users/elderly-mode')
}
