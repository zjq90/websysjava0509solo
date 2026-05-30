import Vue from 'vue'
import Vuex from 'vuex'

Vue.use(Vuex)

export default new Vuex.Store({
  state: {
    dateRange: {
      startDate: null,
      endDate: null
    }
  },
  mutations: {
    SET_DATE_RANGE(state, dateRange) {
      state.dateRange = dateRange
    }
  },
  actions: {
    setDateRange({ commit }, dateRange) {
      commit('SET_DATE_RANGE', dateRange)
    }
  },
  getters: {
    dateRange: state => state.dateRange
  }
})
