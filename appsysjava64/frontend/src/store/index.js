import { createStore } from 'vuex'

export default createStore({
  state: {
    user: JSON.parse(localStorage.getItem('user') || 'null'),
    token: localStorage.getItem('token') || ''
  },
  mutations: {
    SET_USER(state, user) {
      state.user = user
      localStorage.setItem('user', JSON.stringify(user))
    },
    SET_TOKEN(state, token) {
      state.token = token
      localStorage.setItem('token', token)
    },
    LOGOUT(state) {
      state.user = null
      state.token = ''
      localStorage.removeItem('user')
      localStorage.removeItem('token')
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
