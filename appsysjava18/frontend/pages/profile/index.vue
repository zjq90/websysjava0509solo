<template>
  <view class="container">
    <view class="user-card">
      <view class="user-avatar">
        <text class="avatar-text">{{ (userStore.userInfo?.realName || '用')[0] }}</text>
      </view>
      <view class="user-info">
        <text class="user-name">{{ userStore.userInfo?.realName || '用户' }}</text>
        <text class="user-role">{{ userStore.roleNames.join('、') }}</text>
      </view>
    </view>

    <view class="menu-list">
      <view class="menu-item" @click="navigateToReport">
        <view class="menu-icon" style="background: #AF52DE1A;">📊</view>
        <text class="menu-label">报表查看</text>
        <text class="menu-arrow">›</text>
      </view>

      <view class="menu-item" @click="showAbout">
        <view class="menu-icon" style="background: #007AFF1A;">ℹ️</view>
        <text class="menu-label">关于</text>
        <text class="menu-arrow">›</text>
      </view>
    </view>

    <view class="logout-section">
      <view class="logout-btn" @click="handleLogout">
        <text class="logout-text">退出登录</text>
      </view>
    </view>

    <view class="version-info">
      <text class="version-text">版本 v1.0.0</text>
    </view>
  </view>
</template>

<script>
import { useUserStore } from '@/stores/user'

export default {
  computed: {
    userStore() {
      return useUserStore()
    }
  },

  methods: {
    // 导航到报表
    navigateToReport() {
      uni.navigateTo({ url: '/pages/report/index' })
    },

    // 显示关于
    showAbout() {
      uni.showModal({
        title: '关于',
        content: '农业应用系统 v1.0.0\n\n智慧农业，高效管理',
        showCancel: false
      })
    },

    // 退出登录
    handleLogout() {
      uni.showModal({
        title: '提示',
        content: '确定要退出登录吗？',
        success: async (res) => {
          if (res.confirm) {
            await this.userStore.logoutAction()
            uni.reLaunch({ url: '/pages/login/login' })
          }
        }
      })
    }
  }
}
</script>

<style scoped>
.container {
  padding: 30rpx;
  min-height: 100vh;
  background: #F5F5F5;
}

.user-card {
  background: linear-gradient(135deg, #007AFF 0%, #34C759 100%);
  border-radius: 24rpx;
  padding: 50rpx 40rpx;
  display: flex;
  align-items: center;
  margin-bottom: 30rpx;
  box-shadow: 0 8rpx 24rpx rgba(0, 122, 255, 0.2);
}

.user-avatar {
  width: 120rpx;
  height: 120rpx;
  background: rgba(255, 255, 255, 0.2);
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-right: 30rpx;
}

.avatar-text {
  font-size: 56rpx;
  color: #FFFFFF;
  font-weight: bold;
}

.user-info {
  display: flex;
  flex-direction: column;
}

.user-name {
  font-size: 36rpx;
  color: #FFFFFF;
  font-weight: 600;
  margin-bottom: 8rpx;
}

.user-role {
  font-size: 26rpx;
  color: rgba(255, 255, 255, 0.8);
}

.menu-list {
  background: #FFFFFF;
  border-radius: 20rpx;
  overflow: hidden;
  margin-bottom: 40rpx;
  box-shadow: 0 4rpx 12rpx rgba(0, 0, 0, 0.05);
}

.menu-item {
  display: flex;
  align-items: center;
  padding: 30rpx;
  border-bottom: 1rpx solid #F5F5F5;
}

.menu-item:last-child {
  border-bottom: none;
}

.menu-icon {
  width: 70rpx;
  height: 70rpx;
  border-radius: 16rpx;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 32rpx;
  margin-right: 24rpx;
}

.menu-label {
  flex: 1;
  font-size: 30rpx;
  color: #333333;
}

.menu-arrow {
  font-size: 32rpx;
  color: #CCCCCC;
}

.logout-section {
  padding: 0 20rpx;
}

.logout-btn {
  background: #FFFFFF;
  border-radius: 20rpx;
  padding: 30rpx;
  text-align: center;
  box-shadow: 0 4rpx 12rpx rgba(0, 0, 0, 0.05);
}

.logout-text {
  font-size: 32rpx;
  color: #FF3B30;
  font-weight: 500;
}

.version-info {
  text-align: center;
  padding: 40rpx;
}

.version-text {
  font-size: 24rpx;
  color: #999999;
}
</style>
