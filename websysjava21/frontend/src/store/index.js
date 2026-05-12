import Vue from 'vue'
import Vuex from 'vuex'
import { getToken, setToken, removeToken, getUserInfo, setUserInfo, removeUserInfo } from '@/utils/auth'

Vue.use(Vuex)

export default new Vuex.Store({
  state: {
    token: getToken(),
    userInfo: getUserInfo()
  },
  getters: {
    token: state => state.token,
    userInfo: state => state.userInfo
  },
  mutations: {
    SET_TOKEN(state, token) {
      state.token = token
    },
    SET_USER_INFO(state, userInfo) {
      state.userInfo = userInfo
    }
  },
  actions: {
    setToken({ commit }, token) {
      commit('SET_TOKEN', token)
      setToken(token)
    },
    setUserInfo({ commit }, userInfo) {
      commit('SET_USER_INFO', userInfo)
      setUserInfo(userInfo)
    },
    logout({ commit }) {
      commit('SET_TOKEN', '')
      commit('SET_USER_INFO', null)
      removeToken()
      removeUserInfo()
    }
  },
  modules: {}
})
