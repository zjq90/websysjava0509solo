<script setup>
import { onLaunch, onShow, onHide } from '@dcloudio/uni-app'
import { useUserStore } from './store/index.js'

const userStore = useUserStore()

onLaunch(() => {
  console.log('App Launch')
  const elderMode = uni.getStorageSync('elderMode')
  if (elderMode) {
    userStore.elderMode = elderMode
  }
})

onShow(() => {
  console.log('App Show')
})

onHide(() => {
  console.log('App Hide')
})
</script>

<style>
@import url('./static/css/common.css');

page {
  background-color: #f5f5f5;
  font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif;
}

.elder-mode page {
  font-size: 32rpx !important;
}
</style>
