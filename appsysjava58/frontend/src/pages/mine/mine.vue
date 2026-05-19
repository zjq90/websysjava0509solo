<template>
  <view class="container">
    <view class="mine-header">
      <view class="user-info">
        <view class="avatar">
          <text class="avatar-icon">👤</text>
        </view>
        <view class="info-text">
          <text class="nickname">测试用户</text>
          <text class="phone">138****8000</text>
        </view>
      </view>
    </view>

    <view class="pet-section">
      <view class="section-header">
        <text class="section-title">我的宠物</text>
        <text class="section-add">+ 添加</text>
      </view>
      <view class="pet-list">
        <view class="pet-card" v-for="pet in pets" :key="pet.id">
          <text class="pet-emoji">{{ pet.type === 'dog' ? '🐕' : '🐱' }}</text>
          <text class="pet-name">{{ pet.name }}</text>
          <text class="pet-breed">{{ pet.breed }}</text>
        </view>
      </view>
    </view>

    <view class="menu-list">
      <view class="menu-item" @click="goToHealthRecords">
        <text class="menu-icon">📋</text>
        <text class="menu-text">健康记录</text>
        <text class="menu-arrow">›</text>
      </view>
      <view class="menu-item" @click="goToMyPosts">
        <text class="menu-icon">📝</text>
        <text class="menu-text">我的帖子</text>
        <text class="menu-arrow">›</text>
      </view>
      <view class="menu-item" @click="goToFavorites">
        <text class="menu-icon">⭐</text>
        <text class="menu-text">收藏的医院</text>
        <text class="menu-arrow">›</text>
      </view>
    </view>

    <view class="settings-section">
      <view class="section-header">
        <text class="section-title">设置</text>
      </view>
      <view class="menu-list">
        <view class="menu-item" @click="goToElderMode">
          <text class="menu-icon">👓</text>
          <text class="menu-text">长辈模式</text>
          <view class="menu-switch">
            <switch :checked="elderMode" color="#667eea" />
          </view>
        </view>
        <view class="menu-item">
          <text class="menu-icon">🔔</text>
          <text class="menu-text">消息通知</text>
          <text class="menu-arrow">›</text>
        </view>
        <view class="menu-item">
          <text class="menu-icon">⚙️</text>
          <text class="menu-text">通用设置</text>
          <text class="menu-arrow">›</text>
        </view>
      </view>
    </view>

    <view class="about-section">
      <view class="menu-item">
        <text class="menu-icon">ℹ️</text>
        <text class="menu-text">关于我们</text>
        <text class="menu-arrow">›</text>
      </view>
      <view class="menu-item">
        <text class="menu-icon">💬</text>
        <text class="menu-text">意见反馈</text>
        <text class="menu-arrow">›</text>
      </view>
    </view>
  </view>
</template>

<script>
export default {
  data() {
    return {
      pets: [
        { id: 1, name: '旺财', type: 'dog', breed: '金毛' },
        { id: 2, name: '咪咪', type: 'cat', breed: '英短' }
      ],
      elderMode: false
    }
  },
  onLoad() {
    this.elderMode = uni.getStorageSync('elderMode') || false
  },
  methods: {
    goToHealthRecords() {
      uni.navigateTo({ url: '/pages/health/deworming-history' })
    },
    goToMyPosts() {
      uni.navigateTo({ url: '/pages/circle/posts' })
    },
    goToFavorites() {
      this.$showToast('功能开发中')
    },
    goToElderMode() {
      uni.navigateTo({ url: '/pages/mine/elder-mode' })
    }
  }
}
</script>

<style scoped>
.mine-header {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  padding: 50rpx 30rpx 80rpx;
  margin: -20rpx -20rpx 30rpx;
  border-radius: 0 0 30rpx 30rpx;
}

.user-info {
  display: flex;
  align-items: center;
}

.avatar {
  width: 120rpx;
  height: 120rpx;
  background: rgba(255,255,255,0.2);
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-right: 30rpx;
}

.avatar-icon {
  font-size: 60rpx;
}

.info-text {
  flex: 1;
  color: #fff;
}

.nickname {
  display: block;
  font-size: 36rpx;
  font-weight: bold;
  margin-bottom: 10rpx;
}

.phone {
  font-size: 26rpx;
  opacity: 0.8;
}

.pet-section,
.settings-section,
.about-section {
  background: #fff;
  border-radius: 16rpx;
  padding: 30rpx;
  margin-bottom: 20rpx;
  box-shadow: 0 4rpx 12rpx rgba(0,0,0,0.08);
}

.section-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20rpx;
}

.section-title {
  font-size: 30rpx;
  font-weight: bold;
  color: #333;
}

.section-add {
  font-size: 26rpx;
  color: #667eea;
}

.pet-list {
  display: flex;
  gap: 20rpx;
}

.pet-card {
  flex: 1;
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 25rpx;
  background: #f8f9fa;
  border-radius: 12rpx;
}

.pet-emoji {
  font-size: 50rpx;
  margin-bottom: 10rpx;
}

.pet-name {
  font-size: 28rpx;
  font-weight: bold;
  color: #333;
  margin-bottom: 5rpx;
}

.pet-breed {
  font-size: 22rpx;
  color: #999;
}

.menu-list {
  display: flex;
  flex-direction: column;
}

.menu-item {
  display: flex;
  align-items: center;
  padding: 25rpx 0;
  border-bottom: 1rpx solid #f0f0f0;
}

.menu-item:last-child {
  border-bottom: none;
}

.menu-icon {
  font-size: 36rpx;
  margin-right: 20rpx;
  width: 50rpx;
}

.menu-text {
  flex: 1;
  font-size: 28rpx;
  color: #333;
}

.menu-arrow {
  font-size: 32rpx;
  color: #ccc;
}

.menu-switch {
  margin-right: 10rpx;
}
</style>