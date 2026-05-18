<script>
export default {
  onLaunch: function() {
    console.log('App Launch')
    this.checkElderMode()
  },
  onShow: function() {
    console.log('App Show')
  },
  onHide: function() {
    console.log('App Hide')
  },
  methods: {
    checkElderMode() {
      const elderMode = uni.getStorageSync('elderMode')
      if (elderMode === 1) {
        this.applyElderMode()
      }
    },
    applyElderMode() {
      uni.setFontSize({
        fontSize: 18
      })
    }
  },
  globalData: {
    baseUrl: '/api',
    token: '',
    userInfo: null
  }
}
</script>

<style>
/* 全局样式 */
page {
  background-color: #F8F8F8;
  font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, 'Helvetica Neue', Arial, sans-serif;
}

/* 长辈模式样式 */
.elder-mode page {
  font-size: 18px !important;
}

.elder-mode .text {
  font-size: 18px !important;
}

/* 通用样式 */
.container {
  padding: 20rpx;
}

.flex {
  display: flex;
}

.flex-center {
  display: flex;
  align-items: center;
  justify-content: center;
}

.flex-between {
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.flex-column {
  display: flex;
  flex-direction: column;
}

.text-center {
  text-align: center;
}

.text-primary {
  color: #FF6B9D;
}

.text-secondary {
  color: #999999;
}

.text-success {
  color: #52C41A;
}

.text-warning {
  color: #FAAD14;
}

.text-danger {
  color: #FF4D4F;
}

.bg-primary {
  background-color: #FF6B9D;
}

.bg-white {
  background-color: #FFFFFF;
}

.btn {
  display: flex;
  align-items: center;
  justify-content: center;
  height: 80rpx;
  border-radius: 40rpx;
  font-size: 28rpx;
  font-weight: 500;
}

.btn-primary {
  background: linear-gradient(135deg, #FF6B9D 0%, #FF8E53 100%);
  color: #FFFFFF;
}

.btn-default {
  background-color: #F5F5F5;
  color: #333333;
}

.card {
  background-color: #FFFFFF;
  border-radius: 16rpx;
  padding: 24rpx;
  margin-bottom: 20rpx;
  box-shadow: 0 2rpx 12rpx rgba(0, 0, 0, 0.05);
}

.image {
  width: 100%;
  height: 100%;
}

.price {
  color: #FF4D4F;
  font-weight: bold;
}

.original-price {
  color: #999999;
  text-decoration: line-through;
  font-size: 24rpx;
  margin-left: 10rpx;
}

/* 长辈模式大字样式 */
.elder-mode .btn {
  height: 100rpx;
  font-size: 32rpx;
}

.elder-mode .card {
  padding: 32rpx;
}

.elder-mode .price {
  font-size: 36rpx;
}
</style>