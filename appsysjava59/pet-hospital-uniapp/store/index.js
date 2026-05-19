import { createPinia, defineStore } from 'pinia'

const pinia = createPinia()

export const useUserStore = defineStore('user', {
  state: () => ({
    userInfo: null,
    token: '',
    elderMode: false
  }),
  actions: {
    setUserInfo(info) {
      this.userInfo = info
      uni.setStorageSync('userInfo', info)
    },
    setToken(token) {
      this.token = token
      uni.setStorageSync('token', token)
    },
    setElderMode(mode) {
      this.elderMode = mode
      uni.setStorageSync('elderMode', mode)
      if (mode) {
        uni.showToast({ title: '已开启长辈模式', icon: 'success' })
      } else {
        uni.showToast({ title: '已关闭长辈模式', icon: 'success' })
      }
    },
    logout() {
      this.userInfo = null
      this.token = ''
      uni.removeStorageSync('userInfo')
      uni.removeStorageSync('token')
      uni.reLaunch({ url: '/pages/login/login' })
    }
  }
})

export default pinia
