import Vue from 'vue'
import Vuex from 'vuex'
import { login, getCurrentUser } from '@/api/auth'

Vue.use(Vuex)

export default new Vuex.Store({
  state: {
    token: localStorage.getItem('token') || '',
    userInfo: null,
    collapsed: false
  },
  mutations: {
    SET_TOKEN(state, token) {
      state.token = token
      if (token) {
        localStorage.setItem('token', token)
      } else {
        localStorage.removeItem('token')
      }
    },
    SET_USER_INFO(state, userInfo) {
      state.userInfo = userInfo
    },
    SET_COLLAPSED(state, collapsed) {
      state.collapsed = collapsed
    },
    LOGOUT(state) {
      state.token = ''
      state.userInfo = null
      localStorage.removeItem('token')
    }
  },
  actions: {
    async Login({ commit }, loginForm) {
      const response = await login(loginForm)
      if (response.code === 200) {
        commit('SET_TOKEN', response.data.token)
      }
      return response
    },
    async GetCurrentUser({ commit }) {
      const response = await getCurrentUser()
      if (response.code === 200) {
        commit('SET_USER_INFO', response.data)
      }
      return response
    },
    Logout({ commit }) {
      commit('LOGOUT')
    },
    ToggleSidebar({ commit, state }) {
      commit('SET_COLLAPSED', !state.collapsed)
    }
  },
  getters: {
    isLoggedIn: state => !!state.token,
    userInfo: state => state.userInfo,
    token: state => state.token,
    collapsed: state => state.collapsed
  }
})
