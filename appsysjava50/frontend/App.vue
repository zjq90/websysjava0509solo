<script>
export default {
  onLaunch: function() {
    console.log('App Launch')
    this.checkLogin()
  },
  onShow: function() {
    console.log('App Show')
  },
  onHide: function() {
    console.log('App Hide')
  },
  methods: {
    checkLogin() {
      const token = uni.getStorageSync('token')
      if (!token) {
        uni.reLaunch({
          url: '/pages/user/login'
        })
      }
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

.elder-mode {
  font-size: 34rpx !important;
}

.elder-mode button {
  font-size: 32rpx !important;
  height: 100rpx !important;
  line-height: 100rpx !important;
}

.elder-mode .text-large {
  font-size: 36rpx !important;
}
</style>
