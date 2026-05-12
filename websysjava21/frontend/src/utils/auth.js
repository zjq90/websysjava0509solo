import Cookies from 'js-cookie'

const TokenKey = 'hospital_token'
const UserInfoKey = 'hospital_userinfo'

export function getToken() {
  return Cookies.get(TokenKey)
}

export function setToken(token) {
  return Cookies.set(TokenKey, token, { expires: 1 })
}

export function removeToken() {
  return Cookies.remove(TokenKey)
}

export function getUserInfo() {
  const userInfo = Cookies.get(UserInfoKey)
  return userInfo ? JSON.parse(userInfo) : null
}

export function setUserInfo(userInfo) {
  return Cookies.set(UserInfoKey, JSON.stringify(userInfo), { expires: 1 })
}

export function removeUserInfo() {
  return Cookies.remove(UserInfoKey)
}
