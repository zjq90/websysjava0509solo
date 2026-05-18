import { createStore } from 'vuex'

const store = createStore({
  state: {
    user: null,
    token: '',
    elderMode: false,
    cart: [],
    unreadCount: 0
  },
  mutations: {
    SET_USER(state, user) {
      state.user = user
      if (user) {
        uni.setStorageSync('user', user)
      } else {
        uni.removeStorageSync('user')
      }
    },
    SET_TOKEN(state, token) {
      state.token = token
      if (token) {
        uni.setStorageSync('token', token)
      } else {
        uni.removeStorageSync('token')
      }
    },
    SET_ELDER_MODE(state, mode) {
      state.elderMode = mode
      uni.setStorageSync('elderMode', mode)
      if (mode) {
        document.documentElement.classList.add('elder-mode')
      } else {
        document.documentElement.classList.remove('elder-mode')
      }
    },
    SET_UNREAD_COUNT(state, count) {
      state.unreadCount = count
    },
    LOGOUT(state) {
      state.user = null
      state.token = ''
      state.cart = []
      uni.removeStorageSync('user')
      uni.removeStorageSync('token')
    }
  },
  actions: {
    login({ commit }, { user, token }) {
      commit('SET_USER', user)
      commit('SET_TOKEN', token)
    },
    logout({ commit }) {
      commit('LOGOUT')
    },
    toggleElderMode({ commit, state }) {
      commit('SET_ELDER_MODE', !state.elderMode)
    }
  },
  getters: {
    isLoggedIn: state => !!state.token,
    userName: state => state.user?.nickname || state.user?.username || '未登录'
  }
})

const storedUser = uni.getStorageSync('user')
const storedToken = uni.getStorageSync('token')
const storedElderMode = uni.getStorageSync('elderMode')

if (storedUser) {
  store.commit('SET_USER', storedUser)
}
if (storedToken) {
  store.commit('SET_TOKEN', storedToken)
}
if (storedElderMode) {
  store.commit('SET_ELDER_MODE', storedElderMode)
}

export default store
