<template>
  <view class="container" :class="{ 'elder-mode': elderMode }">
    <view class="user-header">
      <view class="user-avatar">
        <text>{{ user.realName ? user.realName.charAt(0) : '用' }}</text>
      </view>
      <view class="user-info">
        <text class="user-name">{{ user.realName || '用户' }}</text>
        <text class="user-phone">{{ user.phone }}</text>
      </view>
    </view>

    <view class="setting-card">
      <view class="setting-item">
        <view class="setting-left">
          <text class="setting-icon">👓</text>
          <text class="setting-name">长辈模式</text>
        </view>
        <switch :checked="elderMode" @change="toggleElderMode" color="#667eea" />
      </view>
      <view class="setting-item" @click="goToService">
        <view class="setting-left">
          <text class="setting-icon">🔧</text>
          <text class="setting-name">服务进度</text>
        </view>
        <text class="arrow">></text>
      </view>
      <view class="setting-item" @click="goToFault">
        <view class="setting-left">
          <text class="setting-icon">⚠️</text>
          <text class="setting-name">报修记录</text>
        </view>
        <text class="arrow">></text>
      </view>
      <view class="setting-item" @click="goToNetwork">
        <view class="setting-left">
          <text class="setting-icon">📶</text>
          <text class="setting-name">网络管理</text>
        </view>
        <text class="arrow">></text>
      </view>
      <view class="setting-item" @click="goToChat">
        <view class="setting-left">
          <text class="setting-icon">💬</text>
          <text class="setting-name">联系客服</text>
        </view>
        <text class="arrow">></text>
      </view>
    </view>

    <view class="about-card">
      <view class="setting-item">
        <view class="setting-left">
          <text class="setting-icon">ℹ️</text>
          <text class="setting-name">关于我们</text>
        </view>
        <text class="arrow">></text>
      </view>
      <view class="setting-item">
        <view class="setting-left">
          <text class="setting-icon">📋</text>
          <text class="setting-name">意见反馈</text>
        </view>
        <text class="arrow">></text>
      </view>
    </view>

    <view class="logout-area">
      <button class="logout-btn" @click="logout">退出登录</button>
    </view>
  </view>
</template>

<script>
import { userApi } from '@/api/index.js'

export default {
  data() {
    return {
      user: {},
      elderMode: false
    }
  },
  onLoad() {
    this.loadUserInfo()
  },
  methods: {
    async loadUserInfo() {
      try {
        this.user = await userApi.getById(1)
        this.elderMode = this.user.elderMode || false
      } catch (e) {
        console.error(e)
      }
    },
    async toggleElderMode(e) {
      this.elderMode = e.detail.value
      try {
        await userApi.toggleElderMode(1)
        uni.showToast({
          title: this.elderMode ? '已开启长辈模式' : '已关闭长辈模式',
          icon: 'success'
        })
      } catch (e) {
        console.error(e)
      }
    },
    goToService() {
      uni.navigateTo({ url: '/pages/service-order/index' })
    },
    goToFault() {
      uni.navigateTo({ url: '/pages/fault-report/index' })
    },
    goToNetwork() {
      uni.navigateTo({ url: '/pages/network/index' })
    },
    goToChat() {
      uni.navigateTo({ url: '/pages/chat/index' })
    },
    logout() {
      uni.showModal({
        title: '提示',
        content: '确定要退出登录吗？',
        success: (res) => {
          if (res.confirm) {
            uni.showToast({ title: '已退出', icon: 'success' })
          }
        }
      })
    }
  }
}
</script>

<style scoped>
.user-header {
  padding: 40rpx 30rpx;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  display: flex;
  align-items: center;
  gap: 20rpx;
}

.user-avatar {
  width: 120rpx;
  height: 120rpx;
  border-radius: 50%;
  background: rgba(255, 255, 255, 0.3);
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 48rpx;
  color: #fff;
  font-weight: bold;
}

.user-info {
  flex: 1;
  display: flex;
  flex-direction: column;
  gap: 8rpx;
}

.user-name {
  font-size: 36rpx;
  font-weight: bold;
  color: #fff;
}

.user-phone {
  font-size: 26rpx;
  color: rgba(255, 255, 255, 0.8);
}

.setting-card, .about-card {
  margin: 20rpx;
  background: #fff;
  border-radius: 16rpx;
  overflow: hidden;
}

.setting-item {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 30rpx;
  border-bottom: 1rpx solid #f5f5f5;
}

.setting-item:last-child {
  border-bottom: none;
}

.setting-left {
  display: flex;
  align-items: center;
  gap: 16rpx;
}

.setting-icon {
  font-size: 32rpx;
}

.setting-name {
  font-size: 28rpx;
  color: #333;
}

.arrow {
  font-size: 28rpx;
  color: #999;
}

.logout-area {
  padding: 40rpx 20rpx;
}

.logout-btn {
  width: 100%;
  height: 88rpx;
  background: #fff;
  color: #f44336;
  border: none;
  border-radius: 16rpx;
  font-size: 30rpx;
  font-weight: 500;
}
</style>
