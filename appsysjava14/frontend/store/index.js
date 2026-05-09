import Vue from 'vue'
import Vuex from 'vuex'

Vue.use(Vuex)

const store = new Vuex.Store({
    state: {
        userInfo: null,
        token: null,
        offlineRecords: []
    },
    
    mutations: {
        SET_USER_INFO(state, userInfo) {
            state.userInfo = userInfo
        },
        
        SET_TOKEN(state, token) {
            state.token = token
        },
        
        CLEAR_USER_INFO(state) {
            state.userInfo = null
            state.token = null
        },
        
        ADD_OFFLINE_RECORD(state, record) {
            state.offlineRecords.push({
                ...record,
                id: Date.now(),
                status: 'pending'
            })
            uni.setStorageSync('offlineRecords', state.offlineRecords)
        },
        
        REMOVE_OFFLINE_RECORD(state, id) {
            state.offlineRecords = state.offlineRecords.filter(item => item.id !== id)
            uni.setStorageSync('offlineRecords', state.offlineRecords)
        },
        
        LOAD_OFFLINE_RECORDS(state) {
            const records = uni.getStorageSync('offlineRecords')
            state.offlineRecords = records || []
        }
    },
    
    actions: {
        login({ commit }, { userInfo, token }) {
            commit('SET_USER_INFO', userInfo)
            commit('SET_TOKEN', token)
            uni.setStorageSync('userInfo', userInfo)
            uni.setStorageSync('token', token)
        },
        
        logout({ commit }) {
            commit('CLEAR_USER_INFO')
            uni.removeStorageSync('userInfo')
            uni.removeStorageSync('token')
        },
        
        saveOfflineRecord({ commit, state }, record) {
            commit('ADD_OFFLINE_RECORD', record)
            uni.showToast({
                title: '已保存到离线',
                icon: 'success'
            })
        },
        
        syncOfflineRecords({ commit, state }) {
            const records = uni.getStorageSync('offlineRecords') || []
            return Promise.all(records.map(record => {
                return new Promise((resolve, reject) => {
                    setTimeout(() => {
                        commit('REMOVE_OFFLINE_RECORD', record.id)
                        resolve()
                    }, 500)
                })
            }))
        }
    },
    
    getters: {
        isLoggedIn: state => !!state.userInfo,
        userInfo: state => state.userInfo,
        offlineRecordCount: state => state.offlineRecords.length
    }
})

export default store
