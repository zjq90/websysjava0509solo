<template>
  <view class="container">
    <view class="user-card card bg-gradient">
      <view class="user-avatar">
        <text class="avatar-text">{{ userInfo.realName ? userInfo.realName.charAt(0) : userInfo.username ? userInfo.username.charAt(0) : 'U' }}</text>
      </view>
      <view class="user-info">
        <text class="user-name">{{ userInfo.realName || userInfo.username || '用户' }}</text>
        <view class="member-badge" :class="getMemberBadgeClass(userInfo.memberLevel?.level)">
          {{ userInfo.memberLevel?.name || '普通会员' }}
        </view>
      </view>
    </view>

    <view class="stats-card card" v-if="userInfo.accountManager">
      <view class="stat-item">
        <text class="stat-label">专属客户经理</text>
        <text class="stat-value">{{ userInfo.accountManager }}</text>
      </view>
    </view>

    <view class="settings-card card">
      <view class="setting-item" @click="toggleElderMode">
        <view class="setting-left">
          <text class="setting-icon" style="color: #ff976a;">👓</text>
          <view class="setting-info">
            <text class="setting-name">长辈模式</text>
            <text class="setting-desc">放大字体，简化界面</text>
          </view>
        </view>
        <view class="switch" :class="{ 'switch-active': elderMode }">
          <view class="switch-circle"></view>
        </view>
      </view>
    </view>

    <view class="menu-card card">
      <view class="menu-item" @click="goToMember">
        <view class="menu-left">
          <text class="menu-icon" style="color: #1989fa;">👑</text>
          <text class="menu-name">会员权益</text>
        </view>
        <text class="menu-arrow">›</text>
      </view>
      
      <view class="menu-item" @click="goToPoints">
        <view class="menu-left">
          <text class="menu-icon" style="color: #07c160;">🎁</text>
          <text class="menu-name">积分商城</text>
        </view>
        <text class="menu-arrow">›</text>
      </view>
      
      <view class="menu-item" @click="goToNotifications">
        <view class="menu-left">
          <text class="menu-icon" style="color: #ff976a;">🔔</text>
          <text class="menu-name">消息通知</text>
        </view>
        <view class="menu-badge" v-if="unreadCount > 0">{{ unreadCount }}</view>
        <text class="menu-arrow">›</text>
      </view>
    </view>

    <view class="about-card card">
      <view class="about-item">
        <text class="about-label">版本号</text>
        <text class="about-value">1.0.0</text>
      </view>
      <view class="about-item">
        <text class="about-label">关于我们</text>
        <text class="menu-arrow">›</text>
      </view>
      <view class="about-item">
        <text class="about-label">帮助中心</text>
        <text class="menu-arrow">›</text>
      </view>
    </view>

    <button class="btn btn-primary logout-btn" @click="handleLogout">退出登录</button>
  </view>
</template>

<script>
import api from '../../common/api.js'

export default {
  data() {
    return {
      userInfo: {},
      elderMode: false,
      unreadCount: 0
    }
  },
  onShow() {
    this.loadUserInfo()
    this.loadUnreadCount()
    this.elderMode = uni.getStorageSync('elderMode') || false
  },
  methods: {
    async loadUserInfo() {
      const userId = uni.getStorageSync('userId')
      if (userId) {
        try {
          const user = await api.getUserMemberInfo(userId)
          this.userInfo = user
        } catch (e) {
          console.error(e)
        }
      }
    },

    async loadUnreadCount() {
      const userId = uni.getStorageSync('userId')
      if (userId) {
        try {
          this.unreadCount = await api.getUnreadCount(userId)
        } catch (e) {
          console.error(e)
        }
      }
    },

    async toggleElderMode() {
      const userId = uni.getStorageSync('userId')
      if (!userId) return

      try {
        await api.toggleElderMode(userId, !this.elderMode)
        this.elderMode = !this.elderMode
        uni.setStorageSync('elderMode', this.elderMode)
        uni.showToast({
          title: this.elderMode ? '已开启长辈模式' : '已关闭长辈模式',
          icon: 'success'
        })
      } catch (e) {
        console.error(e)
      }
    },

    getMemberBadgeClass(level) {
      const classMap = {
        1: 'tag-blue',
        2: 'tag-orange',
        3: 'tag-green',
        4: 'tag-red'
      }
      return classMap[level] || 'tag-blue'
    },

    goToMember() {
      uni.switchTab({ url: '/pages/member/member' })
    },

    goToPoints() {
      uni.switchTab({ url: '/pages/points/points' })
    },

    goToNotifications() {
      uni.switchTab({ url: '/pages/notification/notification' })
    },

    handleLogout() {
      uni.showModal({
        title: '提示',
        content: '确定要退出登录吗？',
        success: (res) => {
          if (res.confirm) {
            uni.removeStorageSync('userId')
            uni.removeStorageSync('userInfo')
            uni.removeStorageSync('elderMode')
            uni.reLaunch({
              url: '/pages/login/login'
            })
          }
        }
      })
    }
  }
}
</script>

<style scoped>
.user-card {
  color: #ffffff;
  display: flex;
  align-items: center;
}

.user-avatar {
  width: 120rpx;
  height: 120rpx;
  border-radius: 50%;
  background: rgba(255, 255, 255, 0.2);
  display: flex;
  align-items: center;
  justify-content: center;
  margin-right: 24rpx;
}

.avatar-text {
  font-size: 48rpx;
  font-weight: bold;
}

.user-info {
  flex: 1;
}

.user-name {
  font-size: 36rpx;
  font-weight: 600;
  display: block;
  margin-bottom: 8rpx;
}

.member-badge {
  display: inline-block;
  padding: 4rpx 16rpx;
  border-radius: 20rpx;
  font-size: 22rpx;
  background: rgba(255, 255, 255, 0.2);
}

.tag-blue { background: rgba(25, 137, 250, 0.8); }
.tag-orange { background: rgba(255, 151, 106, 0.8); }
.tag-green { background: rgba(7, 193, 96, 0.8); }
.tag-red { background: rgba(238, 10, 36, 0.8); }

.stats-card {
  margin-top: 20rpx;
}

.stat-item {
  padding: 16rpx 0;
}

.stat-label {
  font-size: 24rpx;
  color: #999;
  display: block;
  margin-bottom: 8rpx;
}

.stat-value {
  font-size: 28rpx;
  color: #333;
  font-weight: 500;
}

.settings-card {
  margin-top: 20rpx;
}

.setting-item {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 20rpx 0;
}

.setting-left {
  display: flex;
  align-items: center;
  flex: 1;
}

.setting-icon {
  font-size: 36rpx;
  margin-right: 20rpx;
  width: 40rpx;
  text-align: center;
}

.setting-info {
  flex: 1;
}

.setting-name {
  font-size: 28rpx;
  color: #333;
  display: block;
  margin-bottom: 4rpx;
}

.setting-desc {
  font-size: 22rpx;
  color: #999;
}

.switch {
  width: 88rpx;
  height: 48rpx;
  background: #e5e5e5;
  border-radius: 24rpx;
  position: relative;
  transition: background 0.3s;
}

.switch.switch-active {
  background: #07c160;
}

.switch-circle {
  width: 44rpx;
  height: 44rpx;
  border-radius: 50%;
  background: #ffffff;
  position: absolute;
  top: 2rpx;
  left: 2rpx;
  transition: transform 0.3s;
  box-shadow: 0 2rpx 8rpx rgba(0, 0, 0, 0.1);
}

.switch-active .switch-circle {
  transform: translateX(40rpx);
}

.menu-card {
  margin-top: 20rpx;
}

.menu-item {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 28rpx 0;
  border-bottom: 1rpx solid #f0f0f0;
}

.menu-item:last-child {
  border-bottom: none;
}

.menu-left {
  display: flex;
  align-items: center;
  flex: 1;
}

.menu-icon {
  font-size: 36rpx;
  margin-right: 20rpx;
  width: 40rpx;
  text-align: center;
}

.menu-name {
  font-size: 28rpx;
  color: #333;
}

.menu-arrow {
  font-size: 36rpx;
  color: #ccc;
}

.menu-badge {
  min-width: 36rpx;
  height: 36rpx;
  line-height: 36rpx;
  text-align: center;
  font-size: 20rpx;
  color: #ffffff;
  background: #ee0a24;
  border-radius: 18rpx;
  padding: 0 8rpx;
  margin-right: 12rpx;
}

.about-card {
  margin-top: 20rpx;
}

.about-item {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 24rpx 0;
  border-bottom: 1rpx solid #f0f0f0;
}

.about-item:last-child {
  border-bottom: none;
}

.about-label {
  font-size: 28rpx;
  color: #333;
}

.about-value {
  font-size: 28rpx;
  color: #999;
}

.logout-btn {
  width: 100%;
  height: 88rpx;
  margin-top: 40rpx;
  background: #ffeef0;
  color: #ee0a24;
  border: none;
}
</style>
