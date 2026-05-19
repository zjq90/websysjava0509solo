<script>
export default {
  globalData: {
    baseUrl: 'http://localhost:8080/api',
    elderMode: false
  },
  
  onLaunch: function() {
    console.log('App Launch')
    // 从本地存储读取长辈模式设置
    try {
      const savedElderMode = uni.getStorageSync('elderMode')
      if (savedElderMode !== '') {
        this.globalData.elderMode = savedElderMode === 'true'
      }
    } catch (e) {
      console.log('读取设置失败', e)
    }
  },
  
  onShow: function() {
    console.log('App Show')
  },
  
  onHide: function() {
    console.log('App Hide')
  },
  
  methods: {
    // 切换长辈模式
    toggleElderMode(enabled) {
      this.globalData.elderMode = enabled
      uni.setStorageSync('elderMode', enabled ? 'true' : 'false')
      uni.showToast({
        title: enabled ? '长辈模式已开启' : '长辈模式已关闭',
        icon: 'success'
      })
    }
  }
}
</script>

<style>
/* 全局样式 */
page {
  background-color: #F5F5F5;
  font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif;
  font-size: 16px;
  color: #333;
}

/* 长辈模式样式 */
.elder-mode page {
  font-size: 20px;
}

.elder-mode .text-small {
  font-size: 18px;
}

.elder-mode .text-medium {
  font-size: 22px;
}

.elder-mode .text-large {
  font-size: 26px;
}

/* 通用类 */
.container {
  padding: 20rpx;
}

.card {
  background-color: #FFFFFF;
  border-radius: 16rpx;
  padding: 30rpx;
  margin-bottom: 20rpx;
  box-shadow: 0 2rpx 12rpx rgba(0, 0, 0, 0.08);
}

.flex {
  display: flex;
}

.flex-between {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.flex-center {
  display: flex;
  justify-content: center;
  align-items: center;
}

.text-primary {
  color: #409EFF;
}

.text-success {
  color: #67C23A;
}

.text-warning {
  color: #E6A23C;
}

.text-danger {
  color: #F56C6C;
}

.text-secondary {
  color: #909399;
}

.text-small {
  font-size: 24rpx;
}

.text-medium {
  font-size: 28rpx;
}

.text-large {
  font-size: 32rpx;
}

.text-bold {
  font-weight: bold;
}

.mt-10 {
  margin-top: 10rpx;
}

.mt-20 {
  margin-top: 20rpx;
}

.mb-10 {
  margin-bottom: 10rpx;
}

.mb-20 {
  margin-bottom: 20rpx;
}

.btn {
  border-radius: 8rpx;
  padding: 20rpx 40rpx;
  text-align: center;
  font-size: 28rpx;
  border: none;
}

.btn-primary {
  background-color: #409EFF;
  color: #FFFFFF;
}

.btn-success {
  background-color: #67C23A;
  color: #FFFFFF;
}

.btn-warning {
  background-color: #E6A23C;
  color: #FFFFFF;
}

.highlight {
  color: #F56C6C;
  font-weight: bold;
  background-color: #FEF0F0;
  padding: 0 4rpx;
  border-radius: 4rpx;
}
</style>
