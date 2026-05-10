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
  background-color: #F5F5F5;
  font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, 'Helvetica Neue', Arial, sans-serif;
  font-size: 14px;
  color: #333333;
}

/* 通用容器 */
.container {
  padding: 20rpx;
  background-color: #F5F5F5;
  min-height: 100vh;
}

/* 卡片样式 */
.card {
  background-color: #FFFFFF;
  border-radius: 16rpx;
  padding: 30rpx;
  margin-bottom: 20rpx;
  box-shadow: 0 2rpx 12rpx rgba(0, 0, 0, 0.05);
}

/* 按钮样式 */
.btn-primary {
  background-color: #007AFF;
  color: #FFFFFF;
  border-radius: 12rpx;
  padding: 20rpx;
  text-align: center;
  font-size: 30rpx;
  font-weight: 500;
}

.btn-primary:active {
  background-color: #0056CC;
}

.btn-outline {
  background-color: #FFFFFF;
  color: #007AFF;
  border: 2rpx solid #007AFF;
  border-radius: 12rpx;
  padding: 18rpx;
  text-align: center;
  font-size: 30rpx;
  font-weight: 500;
}

.btn-danger {
  background-color: #FF3B30;
  color: #FFFFFF;
  border-radius: 12rpx;
  padding: 20rpx;
  text-align: center;
  font-size: 30rpx;
  font-weight: 500;
}

/* 表单样式 */
.form-item {
  margin-bottom: 30rpx;
}

.form-label {
  display: block;
  font-size: 28rpx;
  color: #666666;
  margin-bottom: 12rpx;
}

.form-input {
  width: 100%;
  height: 88rpx;
  background-color: #F5F5F5;
  border-radius: 12rpx;
  padding: 0 24rpx;
  font-size: 30rpx;
  color: #333333;
}

/* 列表样式 */
.list-item {
  background-color: #FFFFFF;
  padding: 30rpx;
  border-bottom: 1rpx solid #EEEEEE;
}

.list-item:last-child {
  border-bottom: none;
}

/* 文字样式 */
.text-primary {
  color: #007AFF;
}

.text-success {
  color: #34C759;
}

.text-warning {
  color: #FF9500;
}

.text-danger {
  color: #FF3B30;
}

.text-muted {
  color: #999999;
}

.text-small {
  font-size: 24rpx;
}

.text-large {
  font-size: 36rpx;
}

.text-bold {
  font-weight: 600;
}

/* Flex 布局 */
.flex {
  display: flex;
}

.flex-col {
  flex-direction: column;
}

.flex-center {
  align-items: center;
  justify-content: center;
}

.flex-between {
  justify-content: space-between;
}

.flex-1 {
  flex: 1;
}

/* 间距 */
.mt-10 { margin-top: 10rpx; }
.mt-20 { margin-top: 20rpx; }
.mt-30 { margin-top: 30rpx; }
.mb-10 { margin-bottom: 10rpx; }
.mb-20 { margin-bottom: 20rpx; }
.mb-30 { margin-bottom: 30rpx; }
</style>
