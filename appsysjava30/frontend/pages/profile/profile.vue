<template>
  <view class="profile-container" :class="{ 'elderly-mode': isElderlyMode }">
    <view class="user-header">
      <view class="avatar">
        <text class="avatar-icon">👤</text>
      </view>
      <view class="user-info">
        <text class="username">{{ userInfo.realName || userInfo.username }}</text>
        <text class="phone" v-if="userInfo.phone">{{ userInfo.phone }}</text>
      </view>
    </view>
    
    <view class="menu-list">
      <view class="menu-section">
        <view class="menu-item" @click="goRecords">
          <text class="menu-icon">📋</text>
          <text class="menu-text">我的预约</text>
          <text class="menu-arrow">›</text>
        </view>
        <view class="menu-item" @click="goRegistration">
          <text class="menu-icon">🏥</text>
          <text class="menu-text">预约挂号</text>
          <text class="menu-arrow">›</text>
        </view>
      </view>
      
      <view class="menu-section">
        <view class="menu-item" @click="toggleElderlyMode">
          <text class="menu-icon">👵</text>
          <text class="menu-text">长辈模式</text>
          <switch :checked="isElderlyMode" @change="toggleElderlyMode" color="#1890ff" />
        </view>
        <view class="menu-item">
          <text class="menu-icon">📞</text>
          <text class="menu-text">联系客服</text>
          <text class="menu-value">400-123-4567</text>
        </view>
        <view class="menu-item">
          <text class="menu-icon">❓</text>
          <text class="menu-text">帮助中心</text>
          <text class="menu-arrow">›</text>
        </view>
        <view class="menu-item">
          <text class="menu-icon">ℹ️</text>
          <text class="menu-text">关于我们</text>
          <text class="menu-arrow">›</text>
        </view>
      </view>
      
      <view class="menu-section">
        <view class="menu-item" @click="handleLogout">
          <text class="menu-icon">🚪</text>
          <text class="menu-text logout">退出登录</text>
        </view>
      </view>
    </view>
    
    <view class="tips-section">
      <view class="tip-title">📌 就医须知</view>
      <view class="tip-item">
        <text class="tip-dot">•</text>
        <text class="tip-text">就诊时请携带有效身份证件</text>
      </view>
      <view class="tip-item">
        <text class="tip-dot">•</text>
        <text class="tip-text">请提前15分钟到达医院签到</text>
      </view>
      <view class="tip-item">
        <text class="tip-dot">•</text>
        <text class="tip-text">取消预约请在就诊前一日17:00前操作</text>
      </view>
    </view>
    
    <view class="version">
      <text>医疗挂号系统 v1.0.0</text>
    </view>
  </view>
</template>

<script>
export default {
  data() {
    return {
      userInfo: {},
      isElderlyMode: false
    }
  },
  
  onShow() {
    this.userInfo = uni.getStorageSync('userInfo') || {}
    this.isElderlyMode = uni.getStorageSync('elderlyMode') || false
  },
  
  methods: {
    toggleElderlyMode() {
      this.isElderlyMode = !this.isElderlyMode
      uni.setStorageSync('elderlyMode', this.isElderlyMode)
      uni.showToast({
        title: this.isElderlyMode ? '已进入长辈模式' : '已退出长辈模式',
        icon: 'none'
      })
    },
    
    goRecords() {
      uni.switchTab({ url: '/pages/record/record' })
    },
    
    goRegistration() {
      uni.switchTab({ url: '/pages/index/index' })
      setTimeout(() => {
        uni.navigateTo({ url: '/pages/registration/registration' })
      }, 100)
    },
    
    handleLogout() {
      uni.showModal({
        title: '提示',
        content: '确定要退出登录吗？',
        success: (res) => {
          if (res.confirm) {
            uni.removeStorageSync('token')
            uni.removeStorageSync('userInfo')
            uni.removeStorageSync('elderlyMode')
            uni.reLaunch({ url: '/pages/login/login' })
          }
        }
      })
    }
  }
}
</script>

<style scoped>
.profile-container {
  min-height: 100vh;
  background: #f5f5f5;
  padding-bottom: 40rpx;
}

.user-header {
  background: linear-gradient(135deg, #1890ff 0%, #096dd9 100%);
  padding: 60rpx 40rpx;
  display: flex;
  align-items: center;
}

.avatar {
  width: 120rpx;
  height: 120rpx;
  background: rgba(255, 255, 255, 0.2);
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-right: 24rpx;
}

.avatar-icon {
  font-size: 60rpx;
}

.user-info {
  flex: 1;
}

.username {
  display: block;
  font-size: 36rpx;
  font-weight: bold;
  color: #fff;
  margin-bottom: 8rpx;
}

.phone {
  font-size: 26rpx;
  color: rgba(255, 255, 255, 0.8);
}

.menu-list {
  padding: 20rpx;
}

.menu-section {
  background: #fff;
  border-radius: 16rpx;
  margin-bottom: 20rpx;
  overflow: hidden;
}

.menu-item {
  display: flex;
  align-items: center;
  padding: 28rpx 24rpx;
  border-bottom: 2rpx solid #f0f0f0;
}

.menu-item:last-child {
  border-bottom: none;
}

.menu-icon {
  font-size: 36rpx;
  margin-right: 20rpx;
}

.menu-text {
  flex: 1;
  font-size: 30rpx;
  color: #333;
}

.menu-text.logout {
  color: #ff4d4f;
}

.menu-arrow {
  font-size: 36rpx;
  color: #ccc;
}

.menu-value {
  font-size: 26rpx;
  color: #999;
  margin-right: 8rpx;
}

.tips-section {
  margin: 0 20rpx;
  padding: 24rpx;
  background: #fffbe6;
  border-radius: 16rpx;
}

.tip-title {
  font-size: 28rpx;
  font-weight: bold;
  color: #faad14;
  margin-bottom: 16rpx;
}

.tip-item {
  display: flex;
  align-items: flex-start;
  margin-bottom: 12rpx;
}

.tip-item:last-child {
  margin-bottom: 0;
}

.tip-dot {
  color: #faad14;
  margin-right: 8rpx;
}

.tip-text {
  flex: 1;
  font-size: 24rpx;
  color: #faad14;
  line-height: 1.6;
}

.version {
  text-align: center;
  margin-top: 40rpx;
  font-size: 24rpx;
  color: #999;
}
</style>
