<script>
export default {
  onLaunch: function() {
    console.log('App Launch')
    uni.setStorageSync('baseUrl', 'http://localhost:8080/api')
    uni.setStorageSync('userId', 2)
    uni.setStorageSync('clubId', 1)
  },
  onShow: function() {
    console.log('App Show')
  },
  onHide: function() {
    console.log('App Hide')
  }
}
</script>

<style lang="scss">
@import 'uview-plus/index.scss';

page {
  background-color: #f5f5f5;
  font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, 'Helvetica Neue', Arial, sans-serif;
}

.container {
  padding: 20rpx;
}

.card {
  background: #fff;
  border-radius: 16rpx;
  padding: 24rpx;
  margin-bottom: 20rpx;
  box-shadow: 0 2rpx 12rpx rgba(0, 0, 0, 0.06);
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

.flex-1 {
  flex: 1;
}

.text-primary {
  color: #4A90E2;
}

.text-success {
  color: #52c41a;
}

.text-warning {
  color: #faad14;
}

.text-danger {
  color: #f5222d;
}

.text-muted {
  color: #999;
}

.text-small {
  font-size: 24rpx;
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

.p-10 {
  padding: 10rpx;
}

.p-20 {
  padding: 20rpx;
}

.bg-primary {
  background-color: #4A90E2;
  color: #fff;
}

.bg-success {
  background-color: #52c41a;
  color: #fff;
}

.bg-warning {
  background-color: #faad14;
  color: #fff;
}

.bg-danger {
  background-color: #f5222d;
  color: #fff;
}

.rounded {
  border-radius: 8rpx;
}

.rounded-lg {
  border-radius: 16rpx;
}

.avatar {
  width: 80rpx;
  height: 80rpx;
  border-radius: 50%;
}

.avatar-small {
  width: 60rpx;
  height: 60rpx;
  border-radius: 50%;
}

.avatar-large {
  width: 120rpx;
  height: 120rpx;
  border-radius: 50%;
}

.divider {
  height: 1rpx;
  background-color: #eee;
  margin: 20rpx 0;
}

.tag {
  display: inline-block;
  padding: 4rpx 16rpx;
  border-radius: 20rpx;
  font-size: 22rpx;
  margin-right: 10rpx;
}

.tag-primary {
  background-color: #e6f7ff;
  color: #4A90E2;
}

.tag-success {
  background-color: #f6ffed;
  color: #52c41a;
}

.tag-warning {
  background-color: #fffbe6;
  color: #faad14;
}

.tag-danger {
  background-color: #fff1f0;
  color: #f5222d;
}

.empty {
  padding: 100rpx 0;
  text-align: center;
  color: #999;
}

.btn-primary {
  background-color: #4A90E2;
  color: #fff;
  border-radius: 40rpx;
  border: none;
}

.btn-outline {
  background-color: transparent;
  color: #4A90E2;
  border: 2rpx solid #4A90E2;
  border-radius: 40rpx;
}
</style>
