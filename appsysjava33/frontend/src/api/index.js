import request from '../utils/request.js'

export const sendSmsCode = (phone) => {
    return request({
        url: '/auth/send-code',
        method: 'POST',
        data: { phone }
    })
}

export const loginByCode = (phone, code) => {
    return request({
        url: '/auth/login',
        method: 'POST',
        data: { phone, code }
    })
}

export const getUserInfo = () => {
    return request({
        url: '/user/info',
        method: 'GET'
    })
}

export const updateUserInfo = (data) => {
    return request({
        url: '/user/update',
        method: 'PUT',
        data
    })
}

export const getPackageList = () => {
    return request({
        url: '/package/list',
        method: 'GET'
    })
}

export const getPackageDetail = (id) => {
    return request({
        url: '/package/detail?id=' + id,
        method: 'GET'
    })
}

export const getNumberList = (params) => {
    return request({
        url: '/number/list',
        method: 'GET',
        data: params
    })
}

export const createInstallOrder = (data) => {
    return request({
        url: '/order/install',
        method: 'POST',
        data
    })
}

export const createMoveOrder = (data) => {
    return request({
        url: '/order/move',
        method: 'POST',
        data
    })
}

export const createCancelOrder = (data) => {
    return request({
        url: '/order/cancel',
        method: 'POST',
        data
    })
}

export const getOrderList = (status) => {
    return request({
        url: '/order/list',
        method: 'GET',
        data: { status }
    })
}

export const getOrderDetail = (id) => {
    return request({
        url: '/order/detail?id=' + id,
        method: 'GET'
    })
}

export const getBillList = (status) => {
    return request({
        url: '/bill/list',
        method: 'GET',
        data: { status }
    })
}

export const getBillDetail = (id) => {
    return request({
        url: '/bill/detail?id=' + id,
        method: 'GET'
    })
}

export const payBill = (data) => {
    return request({
        url: '/bill/pay',
        method: 'POST',
        data
    })
}

export const submitRealName = (data) => {
    return request({
        url: '/user/realname',
        method: 'POST',
        data
    })
}

export const getRealNameStatus = () => {
    return request({
        url: '/user/realname-status',
        method: 'GET'
    })
}
