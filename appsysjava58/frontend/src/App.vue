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
        this.applyElderMode()
      }
    },
    applyElderMode() {
      uni.setStorageSync('fontSize', 'large')
    }
  }
}
</script>

<style>
@import url('./static/css/common.css');

page {
  background-color: #f5f5f5;
  font-size: 28rpx;
  color: #333;
}

.elder-mode page {
  font-size: 36rpx;
}

.container {
  padding: 20rpx;
}

.card {
  background: #fff;
  border-radius: 16rpx;
  padding: 30rpx;
  margin-bottom: 20rpx;
  box-shadow: 0 4rpx 12rpx rgba(0,0,0,0.08);
}

.btn-primary {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: #fff;
  border-radius: 50rpx;
  padding: 20rpx 40rpx;
  border: none;
  font-size: 28rpx;
}

.elder-mode .btn-primary {
  font-size: 34rpx;
  padding: 24rpx 48rpx;
}

.status-completed {
  color: #52c41a;
}

.status-pending {
  color: #faad14;
}

.status-overdue {
  color: #f5222d;
}
</style>