import { createStore } from 'vuex'

const store = createStore({
    state: {
        userInfo: uni.getStorageSync('userInfo') || null,
        token: uni.getStorageSync('token') || '',
        currentClub: null
    },
    mutations: {
        SET_USER_INFO(state, userInfo) {
            state.userInfo = userInfo
            uni.setStorageSync('userInfo', userInfo)
        },
        SET_TOKEN(state, token) {
            state.token = token
            uni.setStorageSync('token', token)
            if (token) {
                uni.$u.http.setConfig({
                    header: {
                        Authorization: 'Bearer ' + token
                    }
                })
            }
        },
        SET_CURRENT_CLUB(state, club) {
            state.currentClub = club
        },
        LOGOUT(state) {
            state.userInfo = null
            state.token = ''
            state.currentClub = null
            uni.removeStorageSync('userInfo')
            uni.removeStorageSync('token')
        }
    },
    actions: {
        async login({ commit }, { username, password }) {
            try {
                const res = await uni.$u.http.post('/auth/login', {
                    username,
                    password
                })
                commit('SET_TOKEN', res.token)
                commit('SET_USER_INFO', res.user)
                return res
            } catch (e) {
                throw e
            }
        },
        async getCurrentUser({ commit }) {
            try {
                const res = await uni.$u.http.get('/auth/me')
                commit('SET_USER_INFO', res)
                return res
            } catch (e) {
                throw e
            }
        },
        logout({ commit }) {
            commit('LOGOUT')
            uni.reLaunch({
                url: '/pages/login/login'
            })
        }
    },
    getters: {
        isLoggedIn: state => !!state.token,
        isAdmin: state => state.userInfo?.role === 'ADMIN',
        isClubAdmin: state => state.userInfo?.role === 'CLUB_ADMIN',
        isStudent: state => state.userInfo?.role === 'STUDENT'
    }
})

export default store
