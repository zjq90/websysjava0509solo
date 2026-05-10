/**
 * 用户状态管理
 */
import { defineStore } from 'pinia'
import { login, logout, getCurrentUser } from '@/api/auth'

export const useUserStore = defineStore('user', {
  state: () => ({
    token: uni.getStorageSync('token') || '',
    userInfo: uni.getStorageSync('userInfo') || null
  }),

  getters: {
    // 是否登录
    isLoggedIn: (state) => !!state.token,
    
    // 用户角色
    roles: (state) => state.userInfo?.roles || [],
    
    // 用户权限
    permissions: (state) => state.userInfo?.permissions || [],
    
    // 角色名称映射
    roleNames: (state) => {
      const roleMap = {
        'ROLE_WAREHOUSE_KEEPER': '仓管员',
        'ROLE_SALESMAN': '销售员',
        'ROLE_AGRICULTURAL_TECHNICIAN': '农技员',
        'ROLE_MANAGER': '管理层',
        'ROLE_ADMIN': '系统管理员'
      }
      return (state.userInfo?.roles || []).map(role => roleMap[role] || role)
    },
    
    // 是否是管理员
    isAdmin: (state) => (state.userInfo?.roles || []).includes('ROLE_ADMIN'),
    
    // 是否是管理层
    isManager: (state) => (state.userInfo?.roles || []).includes('ROLE_MANAGER'),
    
    // 是否有权限访问库存
    canAccessInventory: (state) => {
      const roles = state.userInfo?.roles || []
      return roles.some(role => 
        ['ROLE_WAREHOUSE_KEEPER', 'ROLE_MANAGER', 'ROLE_ADMIN'].includes(role)
      )
    },
    
    // 是否有权限访问订单
    canAccessOrder: (state) => {
      const roles = state.userInfo?.roles || []
      return roles.some(role => 
        ['ROLE_SALESMAN', 'ROLE_MANAGER', 'ROLE_ADMIN'].includes(role)
      )
    },
    
    // 是否有权限访问田间记录
    canAccessField: (state) => {
      const roles = state.userInfo?.roles || []
      return roles.some(role => 
        ['ROLE_AGRICULTURAL_TECHNICIAN', 'ROLE_MANAGER', 'ROLE_ADMIN'].includes(role)
      )
    }
  },

  actions: {
    // 登录
    async loginAction(credentials) {
      const res = await login(credentials)
      this.token = res.token
      this.userInfo = res
      uni.setStorageSync('token', res.token)
      uni.setStorageSync('userInfo', res)
      return res
    },

    // 获取当前用户信息
    async fetchUserInfo() {
      if (!this.token) return null
      try {
        const res = await getCurrentUser()
        this.userInfo = res
        uni.setStorageSync('userInfo', res)
        return res
      } catch (e) {
        return null
      }
    },

    // 登出
    async logoutAction() {
      try {
        await logout()
      } catch (e) {
        // 忽略登出API错误
      }
      this.token = ''
      this.userInfo = null
      uni.removeStorageSync('token')
      uni.removeStorageSync('userInfo')
    },

    // 清除用户信息
    clearUser() {
      this.token = ''
      this.userInfo = null
      uni.removeStorageSync('token')
      uni.removeStorageSync('userInfo')
    }
  }
})
