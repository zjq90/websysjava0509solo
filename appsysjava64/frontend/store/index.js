import { createStore } from 'vuex'

const store = createStore({
  state: {
    user: uni.getStorageSync('user') || null,
    token: uni.getStorageSync('token') || ''
  },
  mutations: {
    SET_USER(state, user) {
      state.user = user
      uni.setStorageSync('user', user)
    },
    SET_TOKEN(state, token) {
      state.token = token
      uni.setStorageSync('token', token)
    },
    LOGOUT(state) {
      state.user = null
      state.token = ''
      uni.removeStorageSync('user')
      uni.removeStorageSync('token')
    }
  },
  actions: {
    login({ commit }, user) {
      commit('SET_USER', user)
      commit('SET_TOKEN', user.id + '_' + Date.now())
    },
    logout({ commit }) {
      commit('LOGOUT')
    }
  },
  getters: {
    isLoggedIn: (state) => !!state.user,
    userName: (state) => state.user?.name || '',
    userId: (state) => state.user?.id || null
  }
})

export default store
