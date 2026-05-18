<template>
  <view class="container" :class="{ 'elder-mode': isElderMode }">
    <view class="user-header">
      <view class="user-info" @click="checkLogin">
        <image :src="userInfo.avatar || '/static/default-avatar.png'" class="user-avatar" />
        <view class="user-detail">
          <text class="nickname">{{ userInfo.nickname || '点击登录' }}</text>
          <view class="points-row" v-if="userInfo.points !== undefined">
            <text class="points-icon">💰</text>
            <text class="points-text">{{ userInfo.points }} 积分</text>
          </view>
        </view>
      </view>
      <view class="setting-btn" @click="toggleElderMode" v-if="userInfo.id">
        <text>{{ isElderMode ? '👴' : '👤' }}</text>
      </view>
    </view>

    <view class="quick-actions" v-if="userInfo.id">
      <view class="action-item" @click="goToPage('/pages/order/list?tab=all')">
        <text class="action-icon">📋</text>
        <text class="action-text">全部订单</text>
      </view>
      <view class="action-item" @click="goToPage('/pages/order/list?tab=PENDING')">
        <text class="action-icon">💳</text>
        <text class="action-text">待支付</text>
      </view>
      <view class="action-item" @click="goToPage('/pages/order/list?tab=SHIPPED')">
        <text class="action-icon">🚚</text>
        <text class="action-text">待收货</text>
      </view>
      <view class="action-item" @click="goToPage('/pages/product/publish')">
        <text class="action-icon">📦</text>
        <text class="action-text">发布商品</text>
      </view>
    </view>

    <view class="menu-section" v-if="userInfo.id">
      <view class="menu-item" @click="goToPage('/pages/user/points')">
        <text class="menu-icon">💎</text>
        <text class="menu-text">积分中心</text>
        <text class="menu-arrow">›</text>
      </view>
      <view class="menu-item" @click="goToPage('/pages/coupon/index')">
        <text class="menu-icon">🎫</text>
        <text class="menu-text">优惠券</text>
        <text class="menu-arrow">›</text>
      </view>
      <view class="menu-item" @click="goToPage('/pages/groupbuy/index')">
        <text class="menu-icon">👥</text>
        <text class="menu-text">我的拼团</text>
        <text class="menu-arrow">›</text>
      </view>
      <view class="menu-item" @click="goToPage('/pages/bargain/index')">
        <text class="menu-icon">💰</text>
        <text class="menu-text">我的砍价</text>
        <text class="menu-arrow">›</text>
      </view>
      <view class="menu-item" @click="goToPage('/pages/chat/index')">
        <text class="menu-icon">💬</text>
        <text class="menu-text">联系客服</text>
        <text class="menu-arrow">›</text>
      </view>
    </view>

    <view class="login-tip" v-if="!userInfo.id" @click="goToLogin">
      <text class="tip-icon">👋</text>
      <text class="tip-text">登录后享受更多功能</text>
    </view>

    <view class="logout-btn" v-if="userInfo.id" @click="logout">
      <text>退出登录</text>
    </view>
  </view>
</template>

<script>
export default {
  data() {
    return {
      userInfo: {},
      isElderMode: false
    }
  },
  onShow() {
    this.loadUserInfo()
  },
  methods: {
    loadUserInfo() {
      const user = uni.getStorageSync('user')
      if (user) {
        this.userInfo = user
        this.isElderMode = user.elderMode || false
      } else {
        this.userInfo = {}
      }
    },
    checkLogin() {
      if (!this.userInfo.id) {
        this.goToLogin()
      }
    },
    goToLogin() {
      uni.navigateTo({
        url: '/pages/user/login'
      })
    },
    goToPage(url) {
      uni.navigateTo({ url })
    },
    toggleElderMode() {
      this.isElderMode = !this.isElderMode
      const user = uni.getStorageSync('user') || {}
      user.elderMode = this.isElderMode
      uni.setStorageSync('user', user)
      uni.showToast({
        title: this.isElderMode ? '已开启长辈模式' : '已关闭长辈模式',
        icon: 'success'
      })
    },
    logout() {
      uni.showModal({
        title: '提示',
        content: '确定要退出登录吗？',
        success: (res) => {
          if (res.confirm) {
            uni.removeStorageSync('token')
            uni.removeStorageSync('user')
            this.userInfo = {}
            uni.showToast({
              title: '已退出',
              icon: 'success'
            })
          }
        }
      })
    }
  }
}
</script>

<style scoped>
.container {
  min-height: 100vh;
  background: #f5f5f5;
}

.elder-mode {
  font-size: 34rpx;
}

.user-header {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  padding: 80rpx 30rpx 60rpx;
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.user-info {
  display: flex;
  align-items: center;
  flex: 1;
}

.user-avatar {
  width: 120rpx;
  height: 120rpx;
  border-radius: 60rpx;
  border: 4rpx solid rgba(255,255,255,0.3);
}

.user-detail {
  margin-left: 20rpx;
}

.nickname {
  display: block;
  font-size: 32rpx;
  color: #fff;
  font-weight: bold;
  margin-bottom: 10rpx;
}

.points-row {
  display: flex;
  align-items: center;
  background: rgba(255,255,255,0.2);
  padding: 8rpx 20rpx;
  border-radius: 20rpx;
}

.points-icon {
  font-size: 24rpx;
  margin-right: 8rpx;
}

.points-text {
  font-size: 24rpx;
  color: #fff;
}

.setting-btn {
  font-size: 40rpx;
  padding: 10rpx;
}

.quick-actions {
  display: flex;
  background: #fff;
  margin: 20rpx;
  border-radius: 16rpx;
  padding: 30rpx 0;
}

.action-item {
  flex: 1;
  display: flex;
  flex-direction: column;
  align-items: center;
}

.action-icon {
  font-size: 48rpx;
  margin-bottom: 10rpx;
}

.action-text {
  font-size: 24rpx;
  color: #666;
}

.menu-section {
  background: #fff;
  margin: 20rpx;
  border-radius: 16rpx;
  overflow: hidden;
}

.menu-item {
  display: flex;
  align-items: center;
  padding: 30rpx;
  border-bottom: 1rpx solid #f0f0f0;
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
  font-size: 28rpx;
  color: #333;
}

.menu-arrow {
  font-size: 36rpx;
  color: #ccc;
}

.login-tip {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 100rpx 0;
  background: #fff;
  margin: 20rpx;
  border-radius: 16rpx;
}

.tip-icon {
  font-size: 80rpx;
  margin-bottom: 20rpx;
}

.tip-text {
  font-size: 28rpx;
  color: #999;
}

.logout-btn {
  margin: 60rpx 40rpx;
  height: 80rpx;
  line-height: 80rpx;
  text-align: center;
  background: #fff;
  color: #ff4d4f;
  border-radius: 40rpx;
  font-size: 28rpx;
}
</style>
