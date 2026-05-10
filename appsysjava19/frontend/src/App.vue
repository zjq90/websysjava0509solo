<script>
export default {
  onLaunch: function() {
    console.log('App Launch')
    uni.setStorageSync('baseUrl', 'http://localhost:8080')
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
page {
  background-color: #f5f7fa;
  font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, 'Helvetica Neue', Arial, sans-serif;
}

.container {
  padding: 20rpx;
}

.card {
  background-color: #ffffff;
  border-radius: 16rpx;
  padding: 30rpx;
  margin-bottom: 20rpx;
  box-shadow: 0 2rpx 12rpx rgba(0, 0, 0, 0.08);
}

.btn-primary {
  background-color: #2979FF;
  color: #ffffff;
  border-radius: 40rpx;
  padding: 24rpx;
  text-align: center;
  font-size: 30rpx;
  font-weight: 500;
}

.btn-primary:active {
  background-color: #1565C0;
}

.btn-success {
  background-color: #00C853;
  color: #ffffff;
  border-radius: 40rpx;
  padding: 24rpx;
  text-align: center;
  font-size: 30rpx;
  font-weight: 500;
}

.btn-danger {
  background-color: #FF5252;
  color: #ffffff;
  border-radius: 40rpx;
  padding: 24rpx;
  text-align: center;
  font-size: 30rpx;
  font-weight: 500;
}

.badge-normal {
  background-color: #E3F2FD;
  color: #1976D2;
  padding: 6rpx 16rpx;
  border-radius: 20rpx;
  font-size: 24rpx;
}

.badge-warning {
  background-color: #FFF3E0;
  color: #FF9800;
  padding: 6rpx 16rpx;
  border-radius: 20rpx;
  font-size: 24rpx;
}

.badge-danger {
  background-color: #FFEBEE;
  color: #F44336;
  padding: 6rpx 16rpx;
  border-radius: 20rpx;
  font-size: 24rpx;
}

.badge-success {
  background-color: #E8F5E9;
  color: #4CAF50;
  padding: 6rpx 16rpx;
  border-radius: 20rpx;
  font-size: 24rpx;
}

.text-primary {
  color: #2979FF;
}

.text-success {
  color: #00C853;
}

.text-warning {
  color: #FF9800;
}

.text-danger {
  color: #FF5252;
}

.text-muted {
  color: #999999;
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

.mt-20 {
  margin-top: 20rpx;
}

.mb-20 {
  margin-bottom: 20rpx;
}

.p-20 {
  padding: 20rpx;
}

.fs-24 {
  font-size: 24rpx;
}

.fs-28 {
  font-size: 28rpx;
}

.fs-32 {
  font-size: 32rpx;
}

.fs-36 {
  font-size: 36rpx;
}

.fw-bold {
  font-weight: 600;
}
</style>
