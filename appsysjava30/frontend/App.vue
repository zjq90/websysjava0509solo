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
  box-shadow: 0 2rpx 8rpx rgba(0, 0, 0, 0.06);
}

.btn-primary {
  background: linear-gradient(135deg, #1890ff 0%, #096dd9 100%);
  color: #fff;
  border: none;
  border-radius: 48rpx;
  padding: 24rpx;
  font-size: 32rpx;
  text-align: center;
}

.btn-primary:active {
  opacity: 0.8;
}

.btn-danger {
  background: linear-gradient(135deg, #ff4d4f 0%, #cf1322 100%);
  color: #fff;
  border: none;
  border-radius: 48rpx;
  padding: 24rpx;
  font-size: 32rpx;
  text-align: center;
}

.btn-default {
  background: #fff;
  color: #333;
  border: 2rpx solid #d9d9d9;
  border-radius: 48rpx;
  padding: 24rpx;
  font-size: 32rpx;
  text-align: center;
}

.tag {
  display: inline-block;
  padding: 4rpx 16rpx;
  border-radius: 8rpx;
  font-size: 24rpx;
}

.tag-success {
  background: #f6ffed;
  color: #52c41a;
  border: 1rpx solid #b7eb8f;
}

.tag-warning {
  background: #fffbe6;
  color: #faad14;
  border: 1rpx solid #ffe58f;
}

.tag-danger {
  background: #fff2f0;
  color: #ff4d4f;
  border: 1rpx solid #ffccc7;
}

.tag-info {
  background: #e6f7ff;
  color: #1890ff;
  border: 1rpx solid #91d5ff;
}

.tag-hightlight {
  background: linear-gradient(135deg, #ff7a45 0%, #fa541c 100%);
  color: #fff;
  animation: pulse 2s infinite;
}

@keyframes pulse {
  0%, 100% {
    opacity: 1;
  }
  50% {
    opacity: 0.7;
  }
}

.elderly-mode {
  font-size: 36rpx !important;
}

.elderly-mode .card {
  padding: 32rpx;
  margin-bottom: 24rpx;
}

.elderly-mode .btn-primary,
.elderly-mode .btn-danger,
.elderly-mode .btn-default {
  padding: 32rpx;
  font-size: 40rpx;
}

.elderly-mode view,
.elderly-mode text {
  font-size: 36rpx !important;
}
</style>
