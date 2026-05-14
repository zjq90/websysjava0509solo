<script>
export default {
  onLaunch: function() {
    console.log('App Launch')
    this.initApp()
  },
  onShow: function() {
    console.log('App Show')
  },
  onHide: function() {
    console.log('App Hide')
  },
  methods: {
    initApp() {
      const elderMode = uni.getStorageSync('elderMode')
      if (elderMode) {
        this.$elderMode = elderMode
      }
      const userId = uni.getStorageSync('userId')
      if (!userId) {
        uni.reLaunch({
          url: '/pages/login/login'
        })
      }
    }
  }
}
</script>

<style>
@import url('./common/uni.css');
@import url('./common/custom.css');

page {
  background-color: #f5f5f5;
  font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, 'Helvetica Neue', Arial, sans-serif;
}

.elder-mode page {
  font-size: 18px;
}

.elder-mode .text-sm {
  font-size: 16px !important;
}

.elder-mode .text-base {
  font-size: 20px !important;
}

.elder-mode .text-lg {
  font-size: 24px !important;
}

.elder-mode button {
  min-height: 60px !important;
  font-size: 18px !important;
}
</style>
