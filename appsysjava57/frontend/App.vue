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
      if (elderMode) {
        uni.setStorageSync('fontSize', 'large')
      }
    }
  }
}
</script>

<style>
/*每个页面公共css */

page {
  background-color: #f5f5f5;
  font-size: 28rpx;
  color: #333;
}

.container {
  padding: 20rpx;
}

.card {
  background: #fff;
  border-radius: 16rpx;
  padding: 24rpx;
  margin-bottom: 20rpx;
  box-shadow: 0 2rpx 12rpx rgba(0, 0, 0, 0.08);
}

.btn-primary {
  background: linear-gradient(135deg, #4CAF50, #45a049);
  color: #fff;
  border-radius: 50rpx;
  border: none;
  padding: 24rpx;
  font-size: 32rpx;
  font-weight: 500;
}

.btn-primary:active {
  opacity: 0.8;
}

.elder-mode {
  font-size: 36rpx !important;
}

.elder-mode .btn-primary {
  font-size: 40rpx;
  padding: 32rpx;
}
</style>
