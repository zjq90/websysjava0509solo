import { createStore } from 'vuex'

const store = createStore({
  state: {
    userInfo: uni.getStorageSync('userInfo') || null,
    userId: uni.getStorageSync('userId') || 2,
    clubId: uni.getStorageSync('clubId') || 1,
    token: uni.getStorageSync('token') || '',
    unreadCount: 0
  },
  mutations: {
    SET_USER_INFO(state, userInfo) {
      state.userInfo = userInfo
      uni.setStorageSync('userInfo', userInfo)
    },
    SET_USER_ID(state, userId) {
      state.userId = userId
      uni.setStorageSync('userId', userId)
    },
    SET_CLUB_ID(state, clubId) {
      state.clubId = clubId
      uni.setStorageSync('clubId', clubId)
    },
    SET_TOKEN(state, token) {
      state.token = token
      uni.setStorageSync('token', token)
    },
    SET_UNREAD_COUNT(state, count) {
      state.unreadCount = count
    },
    LOGOUT(state) {
      state.userInfo = null
      state.token = ''
      uni.removeStorageSync('userInfo')
      uni.removeStorageSync('token')
    }
  },
  actions: {
    updateUnreadCount({ commit }, count) {
      commit('SET_UNREAD_COUNT', count)
    }
  },
  getters: {
    isLogin: state => !!state.token,
    userInfo: state => state.userInfo,
    userId: state => state.userId,
    clubId: state => state.clubId
  }
})

export default store
