<script>
export default {
  onLaunch: function() {
    console.log('App Launch')
    const token = uni.getStorageSync('token')
    if (token) {
      this.checkLoginStatus()
    }
  },
  onShow: function() {
    console.log('App Show')
  },
  onHide: function() {
    console.log('App Hide')
  },
  methods: {
    checkLoginStatus() {
      // 可以在这里检查token是否有效
    }
  }
}
</script>

<style>
/* @import "uview-plus/index.scss"; */

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
  box-shadow: 0 2rpx 12rpx rgba(0, 0, 0, 0.05);
}

.btn-primary {
  background: linear-gradient(135deg, #667eea 0%, #5677fc 100%);
  border-radius: 48rpx;
  color: #fff;
  border: none;
}

.btn-primary:active {
  opacity: 0.9;
}

.text-primary {
  color: #5677fc;
}

.text-gray {
  color: #999;
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

.avatar {
  width: 80rpx;
  height: 80rpx;
  border-radius: 50%;
  background: #f0f0f0;
}

.avatar-sm {
  width: 60rpx;
  height: 60rpx;
  border-radius: 50%;
}

.avatar-lg {
  width: 120rpx;
  height: 120rpx;
  border-radius: 50%;
}
</style>
