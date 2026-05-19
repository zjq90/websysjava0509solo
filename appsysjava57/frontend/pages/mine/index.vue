<template>
  <view class="container" :class="{ 'elder-mode': isElderMode }">
    <view class="user-card">
      <view class="user-avatar">
        <text class="avatar-text">用</text>
      </view>
      <view class="user-info">
        <text class="user-name">{{ userInfo.realName || '用户' }}</text>
        <text class="user-phone">{{ userInfo.phone || '未绑定手机号' }}</text>
      </view>
    </view>

    <view class="menu-section">
      <view class="menu-item" @click="goToAppointments">
        <view class="menu-icon">📅</view>
        <text class="menu-text">我的预约</text>
        <view class="menu-arrow">›</view>
      </view>
      <view class="menu-item" @click="goToOrders">
        <view class="menu-icon">📦</view>
        <text class="menu-text">我的订单</text>
        <view class="menu-arrow">›</view>
      </view>
      <view class="menu-item" @click="goToReminders">
        <view class="menu-icon">⏰</view>
        <text class="menu-text">用药提醒</text>
        <view class="menu-arrow">›</view>
      </view>
    </view>

    <view class="setting-section">
      <view class="menu-item" @click="toggleElderMode">
        <view class="menu-icon">👓</view>
        <text class="menu-text">长辈模式</text>
        <switch :checked="isElderMode" color="#4CAF50" style="transform: scale(0.8)" />
      </view>
      <view class="menu-item" @click="toggleVoiceInput">
        <view class="menu-icon">🎤</view>
        <text class="menu-text">语音输入</text>
        <switch :checked="voiceInput" color="#4CAF50" style="transform: scale(0.8)" />
      </view>
      <view class="menu-item" @click="goToSettings">
        <view class="menu-icon">⚙️</view>
        <text class="menu-text">设置</text>
        <view class="menu-arrow">›</view>
      </view>
    </view>

    <view class="help-section">
      <view class="menu-item" @click="goToHelp">
        <view class="menu-icon">❓</view>
        <text class="menu-text">帮助中心</text>
        <view class="menu-arrow">›</view>
      </view>
      <view class="menu-item" @click="goToAbout">
        <view class="menu-icon">ℹ️</view>
        <text class="menu-text">关于我们</text>
        <view class="menu-arrow">›</view>
      </view>
    </view>
  </view>
</template>

<script>
export default {
  data() {
    return {
      isElderMode: false,
      voiceInput: false,
      userInfo: {}
    }
  },
  onShow() {
    this.checkElderMode()
    this.loadUserInfo()
  },
  methods: {
    checkElderMode() {
      this.isElderMode = uni.getStorageSync('elderMode') || false
      this.voiceInput = uni.getStorageSync('voiceInput') || false
    },
    async loadUserInfo() {
      try {
        const res = await uni.request({
          url: 'http://localhost:8080/api/users/1',
          method: 'GET'
        })
        if (res.data.code === 200) {
          this.userInfo = res.data.data
        }
      } catch (e) {
        console.log('加载用户信息失败', e)
        this.userInfo = {
          realName: '张三',
          phone: '138****8000'
        }
      }
    },
    toggleElderMode() {
      this.isElderMode = !this.isElderMode
      uni.setStorageSync('elderMode', this.isElderMode)
      uni.showToast({
        title: this.isElderMode ? '已开启长辈模式' : '已关闭长辈模式',
        icon: 'none'
      })
    },
    toggleVoiceInput() {
      this.voiceInput = !this.voiceInput
      uni.setStorageSync('voiceInput', this.voiceInput)
      uni.showToast({
        title: this.voiceInput ? '语音输入已开启' : '语音输入已关闭',
        icon: 'none'
      })
    },
    goToAppointments() {
      uni.showToast({ title: '预约列表开发中', icon: 'none' })
    },
    goToOrders() {
      uni.showToast({ title: '订单列表开发中', icon: 'none' })
    },
    goToReminders() {
      uni.navigateTo({ url: '/pages/medicine/reminder' })
    },
    goToSettings() {
      uni.showToast({ title: '设置页面开发中', icon: 'none' })
    },
    goToHelp() {
      uni.showToast({ title: '帮助中心开发中', icon: 'none' })
    },
    goToAbout() {
      uni.showToast({ title: '关于我们开发中', icon: 'none' })
    }
  }
}
</script>

<style scoped>
.user-card {
  background: linear-gradient(135deg, #4CAF50, #45a049);
  border-radius: 16rpx;
  padding: 40rpx;
  display: flex;
  align-items: center;
  margin-bottom: 30rpx;
}

.user-avatar {
  width: 120rpx;
  height: 120rpx;
  border-radius: 50%;
  background: rgba(255, 255, 255, 0.3);
  display: flex;
  align-items: center;
  justify-content: center;
  margin-right: 24rpx;
}

.avatar-text {
  color: #fff;
  font-size: 48rpx;
  font-weight: bold;
}

.user-info {
  flex: 1;
}

.user-name {
  display: block;
  font-size: 36rpx;
  font-weight: bold;
  color: #fff;
  margin-bottom: 8rpx;
}

.user-phone {
  display: block;
  font-size: 26rpx;
  color: rgba(255, 255, 255, 0.9);
}

.menu-section,
.setting-section,
.help-section {
  background: #fff;
  border-radius: 16rpx;
  margin-bottom: 20rpx;
  overflow: hidden;
}

.menu-item {
  display: flex;
  align-items: center;
  padding: 28rpx 24rpx;
  border-bottom: 1rpx solid #f0f0f0;
}

.menu-item:last-child {
  border-bottom: none;
}

.menu-icon {
  font-size: 36rpx;
  margin-right: 20rpx;
  width: 48rpx;
  text-align: center;
}

.menu-text {
  flex: 1;
  font-size: 30rpx;
  color: #333;
}

.menu-arrow {
  font-size: 40rpx;
  color: #ccc;
}

.elder-mode .menu-text {
  font-size: 34rpx;
}

.elder-mode .menu-item {
  padding: 36rpx 24rpx;
}
</style>
