import { defineStore } from 'pinia'
import { ref, computed } from 'vue'

export const useAppStore = defineStore('app', () => {
  const isElderMode = ref(localStorage.getItem('elderMode') === 'true')

  const toggleElderMode = () => {
    isElderMode.value = !isElderMode.value
    localStorage.setItem('elderMode', isElderMode.value)
  }

  return {
    isElderMode,
    toggleElderMode
  }
})

export const useUserStore = defineStore('user', () => {
  const userInfo = ref(JSON.parse(localStorage.getItem('userInfo') || 'null'))
  const token = ref(localStorage.getItem('token') || '')

  const isLoggedIn = computed(() => !!userInfo.value)

  const login = (user, tokenStr) => {
    userInfo.value = user
    token.value = tokenStr
    localStorage.setItem('userInfo', JSON.stringify(user))
    localStorage.setItem('token', tokenStr)
  }

  const logout = () => {
    userInfo.value = null
    token.value = ''
    localStorage.removeItem('userInfo')
    localStorage.removeItem('token')
  }

  return {
    userInfo,
    token,
    isLoggedIn,
    login,
    logout
  }
})
