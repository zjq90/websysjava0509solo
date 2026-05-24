import { ref, computed } from 'vue'
import { login, register, getUserInfo, logout } from '../api/user'

const token = ref(uni.getStorageSync('token') || '')
const userInfo = ref(uni.getStorageSync('userInfo') || null)

const isLoggedIn = computed(() => !!token.value)

const initUser = () => {
  const savedToken = uni.getStorageSync('token')
  const savedUserInfo = uni.getStorageSync('userInfo')
  if (savedToken) {
    token.value = savedToken
    userInfo.value = savedUserInfo
  }
}

const doLogin = async (loginData) => {
  const res = await login(loginData)
  if (res.code === 200) {
    token.value = res.data.token
    userInfo.value = res.data.user
    uni.setStorageSync('token', res.data.token)
    uni.setStorageSync('userInfo', res.data.user)
  }
  return res
}

const doRegister = async (registerData) => {
  const res = await register(registerData)
  return res
}

const fetchUserInfo = async () => {
  const res = await getUserInfo()
  if (res.code === 200) {
    userInfo.value = res.data
    uni.setStorageSync('userInfo', res.data)
  }
  return res
}

const doLogout = () => {
  logout()
  token.value = ''
  userInfo.value = null
  uni.removeStorageSync('token')
  uni.removeStorageSync('userInfo')
}

const updateUserInfo = (info) => {
  userInfo.value = Object.assign({}, userInfo.value, info)
  uni.setStorageSync('userInfo', userInfo.value)
}

export const useUserStore = () => {
  return {
    token,
    userInfo,
    isLoggedIn,
    initUser,
    doLogin,
    doRegister,
    fetchUserInfo,
    doLogout,
    updateUserInfo
  }
}
