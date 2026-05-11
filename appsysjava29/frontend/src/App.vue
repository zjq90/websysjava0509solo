<script>
import { onLaunch, onShow, onHide } from '@dcloudio/uni-app'
import { useStore } from 'vuex'

export default {
  onLaunch() {
    const store = useStore()
    const token = uni.getStorageSync('token')
    const userInfo = uni.getStorageSync('userInfo')
    const elderMode = uni.getStorageSync('elderMode')
    
    if (token) {
      store.commit('SET_TOKEN', token)
    }
    if (userInfo) {
      store.commit('SET_USER_INFO', userInfo)
    }
    if (elderMode) {
      store.commit('SET_ELDER_MODE', elderMode === '1')
    }
  },
  onShow() {
  },
  onHide() {
  }
}
</script>

<style lang="scss">
@import './uni.scss';

page {
  background-color: $background-color;
  font-size: $font-size-base;
  color: $text-color;
}

.container {
  padding: $spacing-base;
}

.card {
  background: $white;
  border-radius: $border-radius-base;
  padding: $spacing-base;
  margin-bottom: $spacing-base;
  box-shadow: 0 2rpx 8rpx rgba(0, 0, 0, 0.05);
}

.btn-primary {
  background: $primary-color;
  color: $white;
  border-radius: $border-radius-base;
  padding: $spacing-base $spacing-lg;
  text-align: center;
}

.btn-secondary {
  background: $white;
  color: $text-color;
  border: 2rpx solid $border-color;
  border-radius: $border-radius-base;
  padding: $spacing-base $spacing-lg;
  text-align: center;
}

.btn-danger {
  background: $error-color;
  color: $white;
  border-radius: $border-radius-base;
  padding: $spacing-base $spacing-lg;
  text-align: center;
}

.text-primary {
  color: $primary-color;
}

.text-success {
  color: $success-color;
}

.text-warning {
  color: $warning-color;
}

.text-danger {
  color: $error-color;
}

.text-secondary {
  color: $text-secondary;
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

.flex-column {
  display: flex;
  flex-direction: column;
}

.mb-sm {
  margin-bottom: $spacing-sm;
}

.mb-base {
  margin-bottom: $spacing-base;
}

.mb-lg {
  margin-bottom: $spacing-lg;
}

.mt-sm {
  margin-top: $spacing-sm;
}

.mt-base {
  margin-top: $spacing-base;
}

.mt-lg {
  margin-top: $spacing-lg;
}

.p-sm {
  padding: $spacing-sm;
}

.p-base {
  padding: $spacing-base;
}

.p-lg {
  padding: $spacing-lg;
}

.elder-mode-enabled {
  font-size: 1.2em;
  
  .card {
    padding: $spacing-lg;
  }
  
  .btn-primary, .btn-secondary, .btn-danger {
    padding: $spacing-lg;
    font-size: $font-size-lg;
  }
}
</style>
