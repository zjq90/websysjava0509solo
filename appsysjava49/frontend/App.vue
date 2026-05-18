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
        document.documentElement.style.setProperty('--font-size', '18px')
        document.documentElement.style.setProperty('--line-height', '1.8')
      }
    }
  }
}
</script>

<style>
:root {
  --font-size: 14px;
  --line-height: 1.5;
}

* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
}

page {
  font-size: var(--font-size);
  line-height: var(--line-height);
  background-color: #f5f5f5;
  font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, 'Helvetica Neue', Arial, sans-serif;
}

.elder-mode {
  --font-size: 18px;
  --line-height: 1.8;
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
  background: linear-gradient(135deg, #409EFF, #67C23A);
  color: #fff;
  border: none;
  border-radius: 12rpx;
  padding: 20rpx 40rpx;
  font-size: 28rpx;
  font-weight: 500;
}

.btn-primary:active {
  opacity: 0.8;
}

.btn-outline {
  background: #fff;
  color: #409EFF;
  border: 2rpx solid #409EFF;
  border-radius: 12rpx;
  padding: 18rpx 36rpx;
  font-size: 26rpx;
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

.text-gray {
  color: #909399;
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
</style>
