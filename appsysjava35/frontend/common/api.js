const BASE_URL = 'http://localhost:8080/api'

class Api {
  constructor() {
    this.baseUrl = BASE_URL
  }

  request(url, method = 'GET', data = {}) {
    return new Promise((resolve, reject) => {
      uni.request({
        url: this.baseUrl + url,
        method: method,
        data: data,
        header: {
          'Content-Type': 'application/json'
        },
        success: (res) => {
          if (res.data.code === 200) {
            resolve(res.data.data)
          } else {
            uni.showToast({
              title: res.data.message || '请求失败',
              icon: 'none'
            })
            reject(res.data)
          }
        },
        fail: (err) => {
          uni.showToast({
            title: '网络请求失败',
            icon: 'none'
          })
          reject(err)
        }
      })
    })
  }

  login(username, password) {
    return this.request('/user/login', 'POST', { username, password })
  }

  getUserInfo(userId) {
    return this.request(`/user/${userId}`)
  }

  updateUser(userId, data) {
    return this.request(`/user/${userId}`, 'PUT', data)
  }

  toggleElderMode(userId, elderMode) {
    return this.request('/user/elder-mode', 'POST', { userId, elderMode })
  }

  getMemberLevels() {
    return this.request('/member/levels')
  }

  getUserMemberInfo(userId) {
    return this.request(`/member/user/${userId}`)
  }

  upgradeMemberLevel(userId, growth) {
    return this.request('/member/upgrade', 'POST', { userId, growth })
  }

  getPointGoods() {
    return this.request('/point/goods')
  }

  getPointRecords(userId) {
    return this.request(`/point/records/${userId}`)
  }

  checkIn(userId) {
    return this.request('/point/checkin', 'POST', { userId })
  }

  getCheckInStatus(userId) {
    return this.request(`/point/checkin/status?userId=${userId}`)
  }

  exchangeGoods(userId, goodsId) {
    return this.request('/point/exchange', 'POST', { userId, goodsId })
  }

  getNotifications(userId) {
    return this.request(`/notification/user/${userId}`)
  }

  getUnreadCount(userId) {
    return this.request(`/notification/unread/${userId}`)
  }

  markAsRead(id) {
    return this.request(`/notification/read/${id}`, 'POST')
  }

  markAllAsRead(userId) {
    return this.request(`/notification/read-all/${userId}`, 'POST')
  }

  getRSAPublicKey() {
    return this.request('/user/rsa-public-key')
  }
}

export default new Api()
