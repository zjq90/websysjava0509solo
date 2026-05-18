<template>
  <view class="user-page">
    <view class="header">
      <view class="user-info" v-if="user">
        <image :src="user.avatar || '/static/default-avatar.png'" class="avatar" mode="aspectFill" />
        <view class="info">
          <text class="nickname">{{ user.nickname || user.username }}</text>
          <view class="credit-score">
            <text class="score-label">信用分：</text>
            <view class="stars">
              <text v-for="i in 5" :key="i" class="star" :class="{ active: i <= Math.round(user.creditScore) }">
                ★
              </text>
            </view>
            <text class="score-value">{{ user.creditScore }}</text>
          </view>
        </view>
        <view class="auth-badge" v-if="user.isVerified">
          <text>✓ 已实名认证</text>
        </view>
      </view>
      <view class="login-prompt" v-else @click="goToLogin">
        <text class="prompt-icon">👤</text>
        <text class="prompt-text">点击登录</text>
      </view>
    </view>

    <view class="menu-list" v-if="user">
      <view class="menu-section">
        <view class="menu-item" @click="goToOrders">
          <text class="menu-icon">📦</text>
          <text class="menu-text">我的订单</text>
          <text class="arrow">›</text>
        </view>
        <view class="menu-item" @click="goToMyProducts">
          <text class="menu-icon">🏷️</text>
          <text class="menu-text">我的商品</text>
          <text class="arrow">›</text>
        </view>
        <view class="menu-item" @click="goToFavorites">
          <text class="menu-icon">❤️</text>
          <text class="menu-text">我的收藏</text>
          <text class="arrow">›</text>
        </view>
      </view>

      <view class="menu-section">
        <view class="menu-item" @click="goToProfile">
          <text class="menu-icon">⚙️</text>
          <text class="menu-text">编辑资料</text>
          <text class="arrow">›</text>
        </view>
        <view class="menu-item" @click="goToAuth">
          <text class="menu-icon">🪪</text>
          <text class="menu-text">实名认证</text>
          <text class="arrow">›</text>
        </view>
        <view class="menu-item" @click="goToReviews">
          <text class="menu-icon">💬</text>
          <text class="menu-text">我的评价</text>
          <text class="arrow">›</text>
        </view>
      </view>

      <view class="menu-section">
        <view class="menu-item" @click="toggleElderMode">
          <text class="menu-icon">👵</text>
          <text class="menu-text">长辈模式</text>
          <switch :checked="elderMode" color="#409EFF" />
        </view>
        <view class="menu-item" @click="togglePush">
          <text class="menu-icon">🔔</text>
          <text class="menu-text">消息推送</text>
          <switch :checked="pushEnabled" color="#409EFF" />
        </view>
      </view>

      <view class="menu-section">
        <view class="menu-item" @click="logout">
          <text class="menu-icon text-danger">🚪</text>
          <text class="menu-text text-danger">退出登录</text>
        </view>
      </view>
    </view>

    <view class="auth-reminder" v-if="user && !user.isVerified">
      <text class="reminder-icon">⚠️</text>
      <text class="reminder-text">完成实名认证，享受更多交易保障</text>
      <text class="reminder-btn" @click="goToAuth">去认证</text>
    </view>
  </view>
</template>

<script>
import { mapState, mapActions } from 'vuex'

export default {
  computed: {
    ...mapState(['user', 'elderMode'])
  },
  data() {
    return {
      pushEnabled: true
    }
  },
  onShow() {
    if (!this.user) {
      uni.navigateTo({
        url: '/pages/login/index'
      })
    }
  },
  methods: {
    ...mapActions(['toggleElderMode', 'logout']),
    
    togglePush() {
      this.pushEnabled = !this.pushEnabled
      uni.showToast({
        title: this.pushEnabled ? '推送已开启' : '推送已关闭',
        icon: 'none'
      })
    },

    goToLogin() {
      uni.navigateTo({
        url: '/pages/login/index'
      })
    },

    goToOrders() {
      uni.navigateTo({
        url: '/pages/order/list'
      })
    },

    goToMyProducts() {
      uni.showToast({
        title: '我的商品',
        icon: 'none'
      })
    },

    goToFavorites() {
      uni.showToast({
        title: '我的收藏',
        icon: 'none'
      })
    },

    goToProfile() {
      uni.navigateTo({
        url: '/pages/user/profile'
      })
    },

    goToAuth() {
      uni.navigateTo({
        url: '/pages/user/auth'
      })
    },

    goToReviews() {
      uni.navigateTo({
        url: '/pages/user/reviews'
      })
    }
  }
}
</script>

<style scoped>
.user-page {
  min-height: 100vh;
  background: #f5f5f5;
}

.header {
  background: linear-gradient(135deg, #409EFF 0%, #67C23A 100%);
  padding: 60rpx 40rpx 80rpx;
  position: relative;
}

.user-info {
  display: flex;
  align-items: center;
}

.avatar {
  width: 120rpx;
  height: 120rpx;
  border-radius: 50%;
  border: 4rpx solid rgba(255, 255, 255, 0.5);
  background: #fff;
  margin-right: 30rpx;
}

.info {
  flex: 1;
}

.nickname {
  font-size: 36rpx;
  color: #fff;
  font-weight: bold;
  display: block;
  margin-bottom: 16rpx;
}

.credit-score {
  display: flex;
  align-items: center;
}

.score-label {
  font-size: 24rpx;
  color: rgba(255, 255, 255, 0.8);
  margin-right: 12rpx;
}

.stars {
  display: flex;
  margin-right: 12rpx;
}

.star {
  font-size: 28rpx;
  color: rgba(255, 255, 255, 0.4);
  margin-right: 4rpx;
}

.star.active {
  color: #FFD700;
}

.score-value {
  font-size: 24rpx;
  color: #fff;
  font-weight: bold;
}

.auth-badge {
  background: rgba(255, 255, 255, 0.2);
  padding: 8rpx 16rpx;
  border-radius: 20rpx;
  font-size: 22rpx;
  color: #fff;
}

.login-prompt {
  display: flex;
  align-items: center;
  justify-content: center;
  flex-direction: column;
  padding: 40rpx 0;
}

.prompt-icon {
  font-size: 80rpx;
  margin-bottom: 20rpx;
}

.prompt-text {
  font-size: 30rpx;
  color: #fff;
}

.menu-list {
  margin-top: -40rpx;
  padding: 0 20rpx;
}

.menu-section {
  background: #fff;
  border-radius: 16rpx;
  margin-bottom: 20rpx;
  overflow: hidden;
  box-shadow: 0 2rpx 12rpx rgba(0, 0, 0, 0.08);
}

.menu-item {
  display: flex;
  align-items: center;
  padding: 30rpx;
  border-bottom: 1rpx solid #f5f5f5;
}

.menu-item:last-child {
  border-bottom: none;
}

.menu-icon {
  font-size: 40rpx;
  margin-right: 24rpx;
}

.menu-text {
  flex: 1;
  font-size: 30rpx;
  color: #333;
}

.arrow {
  font-size: 36rpx;
  color: #ccc;
}

.text-danger {
  color: #F56C6C;
}

.auth-reminder {
  margin: 20rpx;
  padding: 30rpx;
  background: #fff7e6;
  border-radius: 16rpx;
  display: flex;
  align-items: center;
}

.reminder-icon {
  font-size: 36rpx;
  margin-right: 16rpx;
}

.reminder-text {
  flex: 1;
  font-size: 26rpx;
  color: #666;
}

.reminder-btn {
  font-size: 26rpx;
  color: #409EFF;
  font-weight: 500;
}
</style>
