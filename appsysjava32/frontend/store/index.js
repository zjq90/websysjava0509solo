import Vue from 'vue'
import Vuex from 'vuex'

Vue.use(Vuex)

const store = new Vuex.Store({
    state: {
        token: uni.getStorageSync('token') || '',
        userInfo: uni.getStorageSync('userInfo') || null,
        elderMode: uni.getStorageSync('elderMode') === 1
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
                if (userInfo.elderMode !== undefined) {
                    state.elderMode = userInfo.elderMode === 1
                    uni.setStorageSync('elderMode', userInfo.elderMode)
                }
            } else {
                uni.removeStorageSync('userInfo')
            }
        },
        SET_ELDER_MODE(state, mode) {
            state.elderMode = mode === 1
            uni.setStorageSync('elderMode', mode)
            if (state.userInfo) {
                state.userInfo.elderMode = mode
                uni.setStorageSync('userInfo', state.userInfo)
            }
        },
        LOGOUT(state) {
            state.token = ''
            state.userInfo = null
            uni.removeStorageSync('token')
            uni.removeStorageSync('userInfo')
        }
    },
    actions: {
        async login({ commit }, loginData) {
            const res = await uni.request({
                url: '/api/auth/login',
                method: 'POST',
                data: loginData,
                header: { 'Content-Type': 'application/json' }
            })
            if (res[1].data.code === 200) {
                commit('SET_TOKEN', res[1].data.data.token)
                commit('SET_USER_INFO', res[1].data.data.user)
            }
            return res[1].data
        },
        async logout({ commit }) {
            commit('LOGOUT')
            uni.reLaunch({ url: '/pages/login/login' })
        }
    },
    getters: {
        isLogin: state => !!state.token,
        elderMode: state => state.elderMode
    }
})

export default store
