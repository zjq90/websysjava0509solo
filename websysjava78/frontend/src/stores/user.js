import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import { login as apiLogin, register as apiRegister, getCurrentUser } from '@/api/user'

export const useUserStore = defineStore('user', () => {
  const user = ref(null)
  const token = ref(localStorage.getItem('token') || '')

  const isLoggedIn = computed(() => !!token.value)
  const isPremium = computed(() => user.value?.role === 'PREMIUM' || user.value?.role === 'ADMIN')

  const login = async (username, password) => {
    const result = await apiLogin(username, password)
    token.value = result.token
    localStorage.setItem('token', result.token)
    user.value = {
      id: result.userId,
      username: result.username,
      nickname: result.nickname,
      role: result.role
    }
    localStorage.setItem('user', JSON.stringify(user.value))
    return result
  }

  const register = async (data) => {
    return await apiRegister(data)
  }

  const logout = () => {
    token.value = ''
    user.value = null
    localStorage.removeItem('token')
    localStorage.removeItem('user')
  }

  const fetchCurrentUser = async () => {
    if (!token.value) return null
    try {
      const result = await getCurrentUser()
      user.value = result
      localStorage.setItem('user', JSON.stringify(result))
      return result
    } catch (e) {
      logout()
      return null
    }
  }

  const initFromStorage = () => {
    const storedUser = localStorage.getItem('user')
    if (storedUser && token.value) {
      user.value = JSON.parse(storedUser)
    }
  }

  return {
    user,
    token,
    isLoggedIn,
    isPremium,
    login,
    register,
    logout,
    fetchCurrentUser,
    initFromStorage
  }
})
