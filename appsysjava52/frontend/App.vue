<script>
export default {
  onLaunch: function() {
    console.log('App Launch')
    // 初始化长辈模式
    this.initElderMode()
  },
  onShow: function() {
    console.log('App Show')
  },
  onHide: function() {
    console.log('App Hide')
  },
  methods: {
    initElderMode() {
      const elderMode = uni.getStorageSync('elderMode') || 0
      if (elderMode === 1) {
        // 长辈模式：放大字体
        uni.setStorageSync('fontSize', 'large')
      }
    }
  }
}
</script>

<style>
/* 全局样式 */
page {
  background-color: #f5f5f5;
  font-size: 28rpx;
  color: #333;
}

/* 长辈模式样式 */
.elder-mode page {
  font-size: 36rpx;
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
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: #fff;
  border: none;
  border-radius: 50rpx;
  padding: 20rpx 40rpx;
  font-size: 28rpx;
}

.btn-primary:active {
  opacity: 0.8;
}

.text-muted {
  color: #999;
  font-size: 24rpx;
}

.text-primary {
  color: #667eea;
}

.text-danger {
  color: #ff4757;
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

.flex-column {
  display: flex;
  flex-direction: column;
}

/* 长辈模式适配 */
.elder-mode .card {
  padding: 32rpx;
}

.elder-mode .btn-primary {
  padding: 28rpx 56rpx;
  font-size: 36rpx;
}

.elder-mode .text-muted {
  font-size: 30rpx;
}
</style>