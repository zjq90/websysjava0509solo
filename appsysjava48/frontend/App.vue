<script>
export default {
  onLaunch: function() {
    console.log('App Launch')
  },
  onShow: function() {
    console.log('App Show')
  },
  onHide: function() {
    console.log('App Hide')
  }
}
</script>

<style>
/* 全局样式 */
page {
  background-color: #f5f7fa;
  font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', 'PingFang SC', 'Hiragino Sans GB', 'Microsoft YaHei', sans-serif;
}

/* 通用容器样式 */
.container {
  padding: 20rpx;
  min-height: 100vh;
  box-sizing: border-box;
}

/* 按钮样式 */
.btn {
  border-radius: 50rpx;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 28rpx;
  font-weight: 500;
  transition: all 0.3s ease;
}

.btn.primary {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: #fff;
}

.btn.secondary {
  background: #fff;
  color: #667eea;
  border: 2rpx solid #667eea;
}

.btn:active {
  transform: scale(0.98);
  opacity: 0.9;
}

/* 卡片样式 */
.card {
  background: #fff;
  border-radius: 16rpx;
  padding: 24rpx;
  margin-bottom: 20rpx;
  box-shadow: 0 4rpx 12rpx rgba(0, 0, 0, 0.06);
}

/* 长辈模式样式 */
.elder.page {
  font-size: 32rpx !important;
}

.elder .container {
  padding: 30rpx;
}

.elder .btn {
  font-size: 36rpx;
  padding: 30rpx;
  min-height: 100rpx;
}

.elder .card {
  padding: 30rpx;
  margin-bottom: 30rpx;
}

.elder .title {
  font-size: 40rpx !important;
}

.elder .text {
  font-size: 32rpx !important;
}

.elder .subtitle {
  font-size: 28rpx !important;
}
</style>
