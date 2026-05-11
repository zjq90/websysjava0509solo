import { createStore } from 'vuex'

const store = createStore({
  state() {
    return {
      token: '',
      userInfo: null,
      elderMode: false,
      currentAppointment: null,
      currentPatient: null
    }
  },
  mutations: {
    SET_TOKEN(state, token) {
      state.token = token
      if (token) {
        uni.setStorageSync('token', token)
      } else {
        uni.removeStorageSync('token')
      }
    },
    SET_USER_INFO(state, userInfo) {
      state.userInfo = userInfo
      if (userInfo) {
        uni.setStorageSync('userInfo', userInfo)
      } else {
        uni.removeStorageSync('userInfo')
      }
    },
    SET_ELDER_MODE(state, enabled) {
      state.elderMode = enabled
      uni.setStorageSync('elderMode', enabled ? '1' : '0')
    },
    SET_CURRENT_APPOINTMENT(state, appointment) {
      state.currentAppointment = appointment
    },
    SET_CURRENT_PATIENT(state, patient) {
      state.currentPatient = patient
    },
    LOGOUT(state) {
      state.token = ''
      state.userInfo = null
      state.currentAppointment = null
      state.currentPatient = null
      uni.removeStorageSync('token')
      uni.removeStorageSync('userInfo')
    }
  },
  actions: {
    login({ commit }, data) {
      commit('SET_TOKEN', data.token)
      commit('SET_USER_INFO', {
        userId: data.userId,
        username: data.username,
        realName: data.realName
      })
      if (data.elderMode) {
        commit('SET_ELDER_MODE', data.elderMode === 1)
      }
    },
    logout({ commit }) {
      commit('LOGOUT')
    },
    toggleElderMode({ commit, state }) {
      commit('SET_ELDER_MODE', !state.elderMode)
    }
  },
  getters: {
    isLoggedIn: (state) => !!state.token,
    token: (state) => state.token,
    userInfo: (state) => state.userInfo,
    elderMode: (state) => state.elderMode,
    currentAppointment: (state) => state.currentAppointment,
    currentPatient: (state) => state.currentPatient
  }
})

export default store
