import { get, post, put } from '../utils/request'

export const sendSmsCode = (phone) => {
  return post('/user/send-sms-code', null, { params: { phone } })
}

export const register = (data) => {
  return post('/user/register', data)
}

export const login = (data) => {
  return post('/user/login', data)
}

export const thirdPartyLogin = (platform, openid, nickname, avatar) => {
  return post('/user/third-party-login', null, { 
    params: { platform, openid, nickname, avatar } 
  })
}

export const getUserInfo = () => {
  return get('/user/info')
}

export const updateUserInfo = (data) => {
  return put('/user/info', data)
}

export const realNameVerify = (data) => {
  return post('/user/real-name-verify', data)
}

export const payDeposit = (paymentMethod) => {
  return post('/user/deposit/pay', null, { params: { paymentMethod } })
}

export const creditExempt = (authSource, creditScore) => {
  return post('/user/deposit/credit-exempt', null, { params: { authSource, creditScore } })
}

export const refundDeposit = () => {
  return post('/user/deposit/refund')
}

export const recharge = (amount, paymentMethod) => {
  return post('/user/recharge', null, { params: { amount, paymentMethod } })
}
