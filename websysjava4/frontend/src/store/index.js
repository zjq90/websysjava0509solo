import { createStore } from 'vuex'

export default createStore({
  state: {
    currentRoute: 'dashboard'
  },
  mutations: {
    setCurrentRoute(state, route) {
      state.currentRoute = route
    }
  },
  actions: {
    updateRoute({ commit }, route) {
      commit('setCurrentRoute', route)
    }
  },
  modules: {
  }
})
