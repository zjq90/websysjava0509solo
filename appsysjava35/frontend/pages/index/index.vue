<template>
  <view class="container">
    <view class="user-card card bg-gradient">
      <view class="user-info">
        <view class="avatar">
          <text class="avatar-text">{{ userInfo.realName ? userInfo.realName.charAt(0) : 'U' }}</text>
        </view>
        <view class="user-detail">
          <text class="user-name">{{ userInfo.realName || userInfo.username }}</text>
          <view class="member-badge" :class="getMemberBadgeClass(userInfo.memberLevel?.level)">
            {{ userInfo.memberLevel?.name || '普通会员' }}
          </view>
        </view>
      </view>
      <view class="points-section">
        <view class="points-item">
          <text class="points-value">{{ userInfo.points || 0 }}</text>
          <text class="points-label">积分</text>
        </view>
        <view class="points-item">
          <text class="points-value">{{ userInfo.growth || 0 }}</text>
          <text class="points-label">成长值</text>
        </view>
      </view>
    </view>

    <view class="checkin-card card">
      <view class="flex-between">
        <text class="card-title">每日签到</text>
        <view v-if="hasCheckedIn" class="status-tag tag-green">已签到</view>
        <view v-else class="status-tag tag-orange">未签到</view>
      </view>
      <view class="checkin-content">
        <view class="checkin-info">
          <text class="checkin-desc">签到可获得积分奖励，连续签到奖励更多</text>
        </view>
        <button 
          class="btn btn-primary checkin-btn" 
          :disabled="hasCheckedIn"
          @click="handleCheckIn"
        >
          {{ hasCheckedIn ? '已签到' : '立即签到' }}
        </button>
      </view>
    </view>

    <view class="quick-menu card">
      <text class="card-title">快捷服务</text>
      <view class="grid-container">
        <view class="grid-item" @click="goToPage('/pages/member/member')">
          <view class="grid-icon" style="background-color: #e8f3ff;">
            <text style="color: #1989fa;">👑</text>
          </view>
          <text class="grid-text">会员权益</text>
        </view>
        <view class="grid-item" @click="goToPage('/pages/points/points')">
          <view class="grid-icon" style="background-color: #e8f9f1;">
            <text style="color: #07c160;">🎁</text>
          </view>
          <text class="grid-text">积分商城</text>
        </view>
        <view class="grid-item" @click="goToPage('/pages/notification/notification')">
          <view class="grid-icon" style="background-color: #fff3e8;">
            <text style="color: #ff976a;">🔔</text>
          </view>
          <text class="grid-text">消息通知</text>
        </view>
        <view class="grid-item" @click="goToPage('/pages/user/user')">
          <view class="grid-icon" style="background-color: #ffeef0;">
            <text style="color: #ee0a24;">👤</text>
          </view>
          <text class="grid-text">个人中心</text>
        </view>
      </view>
    </view>

    <view class="activities card">
      <view class="flex-between">
        <text class="card-title">热门活动</text>
        <text class="text-primary">查看更多 ></text>
      </view>
      <view class="activity-list">
        <view class="activity-item">
          <view class="activity-img" style="background-color: #e8f3ff;">
            <text>🎊</text>
          </view>
          <view class="activity-content">
            <text class="activity-title">新用户专享福利</text>
            <text class="activity-desc">注册即送100积分</text>
          </view>
        </view>
        <view class="activity-item">
          <view class="activity-img" style="background-color: #e8f9f1;">
            <text>🔥</text>
          </view>
          <view class="activity-content">
            <text class="activity-title">积分双倍活动</text>
            <text class="activity-desc">本周末积分兑换享双倍</text>
          </view>
        </view>
      </view>
    </view>
  </view>
</template>

<script>
import api from '../../common/api.js'

export default {
  data() {
    return {
      userInfo: {},
      hasCheckedIn: false
    }
  },
  onShow() {
    this.loadUserInfo()
    this.checkCheckInStatus()
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

    async checkCheckInStatus() {
      const userId = uni.getStorageSync('userId')
      if (userId) {
        try {
          this.hasCheckedIn = await api.getCheckInStatus(userId)
        } catch (e) {
          console.error(e)
        }
      }
    },

    async handleCheckIn() {
      const userId = uni.getStorageSync('userId')
      if (!userId) return

      uni.showLoading({ title: '签到中...' })

      try {
        await api.checkIn(userId)
        uni.hideLoading()
        this.hasCheckedIn = true
        this.loadUserInfo()
        uni.showToast({
          title: '签到成功',
          icon: 'success'
        })
      } catch (e) {
        uni.hideLoading()
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

    goToPage(url) {
      uni.switchTab({ url })
    }
  }
}
</script>

<style scoped>
.user-card {
  color: #ffffff;
  margin-bottom: 20rpx;
}

.user-info {
  display: flex;
  align-items: center;
  margin-bottom: 30rpx;
}

.avatar {
  width: 100rpx;
  height: 100rpx;
  border-radius: 50%;
  background: rgba(255, 255, 255, 0.2);
  display: flex;
  align-items: center;
  justify-content: center;
  margin-right: 24rpx;
}

.avatar-text {
  font-size: 40rpx;
  font-weight: bold;
  color: #ffffff;
}

.user-detail {
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
}

.points-section {
  display: flex;
  justify-content: space-around;
}

.points-item {
  text-align: center;
}

.points-value {
  font-size: 48rpx;
  font-weight: bold;
  display: block;
  margin-bottom: 8rpx;
}

.points-label {
  font-size: 24rpx;
  opacity: 0.8;
}

.checkin-content {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-top: 20rpx;
}

.checkin-info {
  flex: 1;
}

.checkin-desc {
  font-size: 26rpx;
  color: #666;
  line-height: 1.5;
}

.checkin-btn {
  width: 180rpx;
  height: 72rpx;
  line-height: 72rpx;
  padding: 0;
  font-size: 26rpx;
}

.grid-container {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 20rpx;
  margin-top: 20rpx;
}

.activity-list {
  margin-top: 20rpx;
}

.activity-item {
  display: flex;
  align-items: center;
  padding: 20rpx 0;
  border-bottom: 1rpx solid #f0f0f0;
}

.activity-item:last-child {
  border-bottom: none;
}

.activity-img {
  width: 80rpx;
  height: 80rpx;
  border-radius: 16rpx;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-right: 20rpx;
  font-size: 36rpx;
}

.activity-content {
  flex: 1;
}

.activity-title {
  font-size: 28rpx;
  color: #333;
  display: block;
  margin-bottom: 8rpx;
}

.activity-desc {
  font-size: 24rpx;
  color: #999;
}
</style>
