import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import request from '../utils/request'

export interface User {
  id: number
  username: string
  nickname: string
  avatar: string
  email: string
  role: 'USER' | 'VIP_USER' | 'ARTIST' | 'ADMIN'
  isVip: boolean
  bio: string
}

export const useUserStore = defineStore('user', () => {
  const token = ref<string | null>(localStorage.getItem('token'))
  const user = ref<User | null>(JSON.parse(localStorage.getItem('user') || 'null'))

  const isLoggedIn = computed(() => !!token.value)

  const login = async (credentials: any) => {
    const res: any = await request.post('/auth/login', credentials)
    token.value = res.token
    user.value = {
      id: res.userId,
      username: res.username,
      nickname: res.nickname,
      avatar: res.avatar,
      email: '',
      role: res.role as any,
      isVip: res.isVip,
      bio: ''
    }
    localStorage.setItem('token', res.token)
    localStorage.setItem('user', JSON.stringify(user.value))
    return res
  }

  const register = async (data: any) => {
    const res: any = await request.post('/auth/register', data)
    token.value = res.token
    user.value = {
      id: res.userId,
      username: res.username,
      nickname: res.nickname,
      avatar: res.avatar,
      email: '',
      role: res.role as any,
      isVip: res.isVip,
      bio: ''
    }
    localStorage.setItem('token', res.token)
    localStorage.setItem('user', JSON.stringify(user.value))
    return res
  }

  const logout = () => {
    token.value = null
    user.value = null
    localStorage.removeItem('token')
    localStorage.removeItem('user')
  }

  const updateUser = (newUser: User) => {
    user.value = newUser
    localStorage.setItem('user', JSON.stringify(newUser))
  }

  return {
    token,
    user,
    isLoggedIn,
    login,
    register,
    logout,
    updateUser
  }
})
