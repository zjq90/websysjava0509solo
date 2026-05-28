import dayjs from 'dayjs'

const util = {
  formatTime: (timestamp, format = 'YYYY-MM-DD HH:mm') => {
    if (!timestamp) return ''
    return dayjs(timestamp).format(format)
  },
  
  formatDate: (timestamp, format = 'YYYY-MM-DD') => {
    if (!timestamp) return ''
    return dayjs(timestamp).format(format)
  },
  
  formatDateTime: (timestamp, format = 'YYYY-MM-DD HH:mm:ss') => {
    if (!timestamp) return ''
    return dayjs(timestamp).format(format)
  },
  
  fromNow: (timestamp) => {
    if (!timestamp) return ''
    const now = dayjs()
    const diff = now.diff(dayjs(timestamp), 'minute')
    if (diff < 1) return '刚刚'
    if (diff < 60) return diff + '分钟前'
    const hours = Math.floor(diff / 60)
    if (hours < 24) return hours + '小时前'
    const days = Math.floor(hours / 24)
    if (days < 7) return days + '天前'
    return dayjs(timestamp).format('YYYY-MM-DD')
  },
  
  getStatusText: (status) => {
    const map = {
      0: '待审核',
      1: '审核通过',
      2: '审核拒绝',
      3: '已取消'
    }
    return map[status] || '未知'
  },
  
  getStatusColor: (status) => {
    const map = {
      0: '#f0ad4e',
      1: '#4cd964',
      2: '#dd524d',
      3: '#999'
    }
    return map[status] || '#999'
  },
  
  getActivityStatusText: (status) => {
    const map = {
      0: '未开始',
      1: '进行中',
      2: '已结束'
    }
    return map[status] || '未知'
  },
  
  getActivityStatusColor: (status) => {
    const map = {
      0: '#5677fc',
      1: '#4cd964',
      2: '#999'
    }
    return map[status] || '#999'
  },
  
  getMessageTypeText: (type) => {
    const map = {
      'system': '系统消息',
      'activity': '活动通知',
      'club': '社团公告',
      'chat': '聊天消息'
    }
    return map[type] || '其他消息'
  },
  
  showToast: (title, icon = 'none', duration = 2000) => {
    uni.showToast({
      title,
      icon,
      duration
    })
  },
  
  showLoading: (title = '加载中...') => {
    uni.showLoading({
      title,
      mask: true
    })
  },
  
  hideLoading: () => {
    uni.hideLoading()
  },
  
  showModal: (title, content, options = {}) => {
    return new Promise((resolve) => {
      uni.showModal({
        title,
        content,
        confirmText: options.confirmText || '确定',
        cancelText: options.cancelText || '取消',
        confirmColor: options.confirmColor || '#5677fc',
        success: (res) => {
          resolve(res.confirm)
        }
      })
    })
  },
  
  navigateTo: (url) => {
    uni.navigateTo({ url })
  },
  
  redirectTo: (url) => {
    uni.redirectTo({ url })
  },
  
  switchTab: (url) => {
    uni.switchTab({ url })
  },
  
  reLaunch: (url) => {
    uni.reLaunch({ url })
  },
  
  goBack: (delta = 1) => {
    uni.navigateBack({ delta })
  },
  
  getStorage: (key) => {
    try {
      return uni.getStorageSync(key)
    } catch (e) {
      return null
    }
  },
  
  setStorage: (key, value) => {
    try {
      uni.setStorageSync(key, value)
      return true
    } catch (e) {
      return false
    }
  },
  
  removeStorage: (key) => {
    try {
      uni.removeStorageSync(key)
      return true
    } catch (e) {
      return false
    }
  },
  
  debounce: (func, wait) => {
    let timeout
    return function executedFunction(...args) {
      const later = () => {
        clearTimeout(timeout)
        func(...args)
      }
      clearTimeout(timeout)
      timeout = setTimeout(later, wait)
    }
  },
  
  throttle: (func, limit) => {
    let inThrottle
    return function() {
      const args = arguments
      const context = this
      if (!inThrottle) {
        func.apply(context, args)
        inThrottle = true
        setTimeout(() => inThrottle = false, limit)
      }
    }
  },
  
  getRandomColor: () => {
    const colors = ['#5677fc', '#4cd964', '#f0ad4e', '#dd524d', '#9c27b0', '#00bcd4']
    return colors[Math.floor(Math.random() * colors.length)]
  },
  
  getInitials: (name) => {
    if (!name) return '用'
    return name.substring(0, 1)
  },
  
  validatePhone: (phone) => {
    return /^1[3-9]\d{9}$/.test(phone)
  },
  
  validateIdCard: (idCard) => {
    return /(^\d{15}$)|(^\d{18}$)|(^\d{17}(\d|X|x)$)/.test(idCard)
  },
  
  validateEmail: (email) => {
    return /^[\w-]+(\.[\w-]+)*@[\w-]+(\.[\w-]+)+$/.test(email)
  },
  
  fileSizeFormat: (bytes) => {
    if (bytes === 0) return '0 B'
    const k = 1024
    const sizes = ['B', 'KB', 'MB', 'GB']
    const i = Math.floor(Math.log(bytes) / Math.log(k))
    return parseFloat((bytes / Math.pow(k, i)).toFixed(2)) + ' ' + sizes[i]
  },
  
  saveOfflineSignIn: (data) => {
    let list = uni.getStorageSync('offline_signin') || []
    list.push({
      ...data,
      offlineTime: Date.now()
    })
    uni.setStorageSync('offline_signin', list)
  },
  
  getOfflineSignIn: () => {
    return uni.getStorageSync('offline_signin') || []
  },
  
  clearOfflineSignIn: () => {
    uni.removeStorageSync('offline_signin')
  },
  
  checkNetwork: () => {
    return new Promise((resolve) => {
      uni.getNetworkType({
        success: (res) => {
          resolve(res.networkType !== 'none')
        },
        fail: () => {
          resolve(false)
        }
      })
    })
  }
}

export default util
