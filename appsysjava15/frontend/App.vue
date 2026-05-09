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
page {
  background-color: #f5f7fa;
  font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, 'Helvetica Neue', Arial, sans-serif;
  font-size: 28rpx;
  color: #333333;
}

.container {
  padding: 24rpx;
  min-height: 100vh;
  box-sizing: border-box;
}

.card {
  background-color: #ffffff;
  border-radius: 16rpx;
  margin-bottom: 24rpx;
  padding: 24rpx;
  box-shadow: 0 4rpx 12rpx rgba(0, 0, 0, 0.05);
}

.btn-primary {
  background-color: #409eff;
  color: #ffffff;
  border-radius: 48rpx;
  height: 88rpx;
  line-height: 88rpx;
  text-align: center;
  font-size: 30rpx;
  font-weight: 500;
}

.btn-primary:active {
  background-color: #66b1ff;
}

.btn-success {
  background-color: #67c23a;
  color: #ffffff;
  border-radius: 48rpx;
  height: 88rpx;
  line-height: 88rpx;
  text-align: center;
  font-size: 30rpx;
  font-weight: 500;
}

.btn-success:active {
  background-color: #85ce61;
}

.btn-warning {
  background-color: #e6a23c;
  color: #ffffff;
  border-radius: 48rpx;
  height: 88rpx;
  line-height: 88rpx;
  text-align: center;
  font-size: 30rpx;
  font-weight: 500;
}

.btn-warning:active {
  background-color: #ebb563;
}

.btn-danger {
  background-color: #f56c6c;
  color: #ffffff;
  border-radius: 48rpx;
  height: 88rpx;
  line-height: 88rpx;
  text-align: center;
  font-size: 30rpx;
  font-weight: 500;
}

.btn-danger:active {
  background-color: #f78989;
}

.status-pending {
  color: #909399;
  background-color: #f4f4f5;
  padding: 4rpx 16rpx;
  border-radius: 8rpx;
  font-size: 24rpx;
}

.status-progress {
  color: #409eff;
  background-color: #ecf5ff;
  padding: 4rpx 16rpx;
  border-radius: 8rpx;
  font-size: 24rpx;
}

.status-completed {
  color: #67c23a;
  background-color: #f0f9eb;
  padding: 4rpx 16rpx;
  border-radius: 8rpx;
  font-size: 24rpx;
}

.form-item {
  margin-bottom: 32rpx;
}

.form-label {
  font-size: 28rpx;
  color: #606266;
  margin-bottom: 12rpx;
  display: block;
}

.form-input {
  height: 88rpx;
  background-color: #f5f7fa;
  border-radius: 12rpx;
  padding: 0 24rpx;
  font-size: 30rpx;
  color: #333333;
}

.form-textarea {
  background-color: #f5f7fa;
  border-radius: 12rpx;
  padding: 20rpx 24rpx;
  font-size: 30rpx;
  color: #333333;
  min-height: 180rpx;
}
</style>
