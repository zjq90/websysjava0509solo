import Vue from 'vue'
import Vuex from 'vuex'

Vue.use(Vuex)

const store = new Vuex.Store({
	state: {
		token: '',
		userInfo: null,
		currentClubId: null,
		managedClubs: []
	},
	mutations: {
		SET_TOKEN(state, token) {
			state.token = token
			uni.setStorageSync('token', token)
		},
		SET_USER_INFO(state, userInfo) {
			state.userInfo = userInfo
			uni.setStorageSync('userInfo', JSON.stringify(userInfo))
		},
		SET_CURRENT_CLUB(state, clubId) {
			state.currentClubId = clubId
		},
		SET_MANAGED_CLUBS(state, clubs) {
			state.managedClubs = clubs
		},
		LOGOUT(state) {
			state.token = ''
			state.userInfo = null
			state.currentClubId = null
			state.managedClubs = []
			uni.removeStorageSync('token')
			uni.removeStorageSync('userInfo')
		}
	},
	actions: {
		login({ commit }, data) {
			commit('SET_TOKEN', data.token)
			commit('SET_USER_INFO', data)
		},
		logout({ commit }) {
			commit('LOGOUT')
		}
	},
	getters: {
		isLoggedIn: state => !!state.token,
		userId: state => state.userInfo ? state.userInfo.userId : null,
		username: state => state.userInfo ? state.userInfo.username : '',
		realName: state => state.userInfo ? state.userInfo.realName : '',
		avatar: state => state.userInfo ? state.userInfo.avatar : '',
		userRole: state => state.userInfo ? state.userInfo.role : ''
	}
})

export default store
