import { createStore } from 'vuex'
import config from '../common/config'
import api from '../common/api'

const store = createStore({
  state: {
    token: uni.getStorageSync(config.tokenKey) || '',
    userInfo: uni.getStorageSync(config.userKey) || null,
    unreadMessageCount: 0,
    currentClub: null
  },
  mutations: {
    SET_TOKEN(state, token) {
      state.token = token
      uni.setStorageSync(config.tokenKey, token)
    },
    SET_USER_INFO(state, userInfo) {
      state.userInfo = userInfo
      uni.setStorageSync(config.userKey, userInfo)
    },
    CLEAR_TOKEN(state) {
      state.token = ''
      state.userInfo = null
      uni.removeStorageSync(config.tokenKey)
      uni.removeStorageSync(config.userKey)
    },
    SET_UNREAD_COUNT(state, count) {
      state.unreadMessageCount = count
    },
    SET_CURRENT_CLUB(state, club) {
      state.currentClub = club
    }
  },
  actions: {
    login({ commit }, loginData) {
      return new Promise((resolve, reject) => {
        api.login(loginData).then(res => {
          commit('SET_TOKEN', res.data.token)
          commit('SET_USER_INFO', res.data.userVO)
          resolve(res.data)
        }).catch(err => {
          reject(err)
        })
      })
    },
    register({ commit }, registerData) {
      return new Promise((resolve, reject) => {
        api.register(registerData).then(res => {
          commit('SET_TOKEN', res.data.token)
          commit('SET_USER_INFO', res.data.userVO)
          resolve(res.data)
        }).catch(err => {
          reject(err)
        })
      })
    },
    logout({ commit }) {
      return new Promise((resolve, reject) => {
        api.logout().then(() => {
          commit('CLEAR_TOKEN')
          resolve()
        }).catch(err => {
          reject(err)
        })
      })
    },
    getUserInfo({ commit, state }) {
      return new Promise((resolve, reject) => {
        if (!state.token) {
          reject(new Error('未登录'))
          return
        }
        api.getUserInfo().then(res => {
          commit('SET_USER_INFO', res.data)
          resolve(res.data)
        }).catch(err => {
          reject(err)
        })
      })
    },
    updateUserInfo({ commit }, userData) {
      return new Promise((resolve, reject) => {
        api.updateUser(userData).then(res => {
          commit('SET_USER_INFO', res.data)
          resolve(res.data)
        }).catch(err => {
          reject(err)
        })
      })
    },
    fetchUnreadCount({ commit }) {
      return new Promise((resolve, reject) => {
        api.getUnreadCount().then(res => {
          commit('SET_UNREAD_COUNT', res.data)
          resolve(res.data)
        }).catch(err => {
          reject(err)
        })
      })
    }
  },
  getters: {
    isLogin: state => !!state.token,
    userInfo: state => state.userInfo,
    userId: state => state.userInfo ? state.userInfo.id : null,
    unreadCount: state => state.unreadMessageCount,
    currentClub: state => state.currentClub
  }
})

export default store
