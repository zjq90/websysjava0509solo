<template>
  <view class="member-page">
    <view class="member-header">
      <view class="user-info-row">
        <image :src="userInfo.avatar || '/static/user-avatar.png'" class="user-avatar"></image>
        <view class="user-info">
          <text class="user-name">{{ userInfo.nickname || '鲜花用户' }}</text>
          <view class="member-level">
            <text class="level-tag">{{ currentLevel && currentLevel.name || '普通会员' }}</text>
          </view>
        </view>
      </view>
      
      <view class="member-stats">
        <view class="stat-item">
          <text class="stat-num">{{ userInfo.currentPoints || 0 }}</text>
          <text class="stat-label">积分</text>
        </view>
        <view class="stat-item">
          <text class="stat-num">{{ (userInfo.totalConsume / 100).toFixed(0) }}</text>
          <text class="stat-label">消费(元)</text>
        </view>
        <view class="stat-item">
          <text class="stat-num">{{ couponCount }}</text>
          <text class="stat-label">优惠券</text>
        </view>
      </view>
    </view>
    
    <view class="level-section" v-if="nextLevel">
      <view class="level-info">
        <text class="level-title">距离{{ nextLevel.name }}还需</text>
        <text class="level-amount">¥{{ ((nextLevel.upgradeAmount - userInfo.totalConsume) / 100).toFixed(2) }}</text>
      </view>
      <view class="progress-bar">
        <view class="progress-inner" :style="{ width: progressPercent + '%' }"></view>
      </view>
    </view>
    
    <view class="benefits-section">
      <view class="section-title">会员权益</view>
      <view class="benefits-grid">
        <view class="benefit-item">
          <view class="benefit-icon">🎂</view>
          <text class="benefit-name">生日折扣</text>
          <text class="benefit-desc">{{ currentLevel && currentLevel.birthdayDiscount || 100 }}折</text>
        </view>
        <view class="benefit-item">
          <view class="benefit-icon">💎</view>
          <text class="benefit-name">专属折扣</text>
          <text class="benefit-desc">{{ currentLevel && currentLevel.discountRate || 100 }}折</text>
        </view>
        <view class="benefit-item">
          <view class="benefit-icon">🚚</view>
          <text class="benefit-name">免费配送</text>
          <text class="benefit-desc">{{ currentLevel && currentLevel.freeShipping === 1 ? '是' : '否' }}</text>
        </view>
        <view class="benefit-item">
          <view class="benefit-icon">⭐</view>
          <text class="benefit-name">积分加速</text>
          <text class="benefit-desc">{{ currentLevel && currentLevel.pointAccelerator || 100 }}%</text>
        </view>
      </view>
    </view>
    
    <view class="menu-section">
      <view class="menu-item" @click="goToPoints">
        <text class="menu-icon">💰</text>
        <text class="menu-name">积分商城</text>
        <text class="menu-arrow">›</text>
      </view>
      <view class="menu-item" @click="goToCoupons">
        <text class="menu-icon">🎫</text>
        <text class="menu-name">我的优惠券</text>
        <text class="menu-arrow">›</text>
      </view>
      <view class="menu-item" @click="goToOrders">
        <text class="menu-icon">📋</text>
        <text class="menu-name">我的订单</text>
        <text class="menu-arrow">›</text>
      </view>
      <view class="menu-item" @click="goToAddress">
        <text class="menu-icon">📍</text>
        <text class="menu-name">收货地址</text>
        <text class="menu-arrow">›</text>
      </view>
    </view>
    
    <view class="level-list-section">
      <view class="section-title">会员等级</view>
      <view class="level-list">
        <view class="level-item" v-for="level in memberLevels" :key="level.id">
          <view class="level-icon" :class="{ active: level.id === userInfo.memberLevelId }">
            {{ level.id === 1 ? '🌱' : level.id === 2 ? '🌺' : '👑' }}
          </view>
          <view class="level-info">
            <text class="level-name">{{ level.name }}</text>
            <text class="level-desc">{{ level.description }}</text>
          </view>
          <text class="level-condition" v-if="level.upgradeAmount > 0">
            消费满{{ (level.upgradeAmount / 100).toFixed(0) }}元
          </text>
          <text class="level-condition" v-else>注册即享</text>
        </view>
      </view>
    </view>
  </view>
</template>

<script>
import api from '@/common/api.js'

export default {
  data() {
    return {
      userInfo: {},
      currentLevel: null,
      nextLevel: null,
      memberLevels: [],
      couponCount: 0
    }
  },
  
  computed: {
    progressPercent() {
      if (!this.nextLevel || !this.currentLevel) return 0
      const total = this.nextLevel.upgradeAmount - this.currentLevel.upgradeAmount
      const current = this.userInfo.totalConsume - this.currentLevel.upgradeAmount
      return Math.min(100, Math.max(0, (current / total) * 100))
    }
  },
  
  onLoad() {
    this.loadMemberInfo()
    this.loadMemberLevels()
    this.loadCouponCount()
  },
  
  methods: {
    async loadMemberInfo() {
      try {
        const res = await api.getUserInfo()
        if (res.code === 200) {
          this.userInfo = res.data
        }
      } catch (e) {
        console.error('获取用户信息失败', e)
        this.userInfo = {
          nickname: '鲜花用户',
          currentPoints: 520,
          totalConsume: 35000,
          memberLevelId: 2
        }
      }
    },
    
    async loadMemberLevels() {
      try {
        const res = await api.getMemberLevels()
        if (res.code === 200) {
          this.memberLevels = res.data
          this.updateLevelInfo()
        }
      } catch (e) {
        console.error('获取会员等级失败', e)
        this.memberLevels = [
          { id: 1, name: '普通会员', description: '注册即成为普通会员，享受基础权益', upgradeAmount: 0, discountRate: 100, birthdayDiscount: 95, freeShipping: 0, pointAccelerator: 100 },
          { id: 2, name: 'VIP会员', description: '累计消费满500元升级为VIP会员，享受9折优惠和免运费', upgradeAmount: 50000, discountRate: 90, birthdayDiscount: 85, freeShipping: 1, pointAccelerator: 150 },
          { id: 3, name: '高级VIP', description: '累计消费满2000元升级为高级VIP，享受8折优惠、优先抢购权和专属客服', upgradeAmount: 200000, discountRate: 80, birthdayDiscount: 85, freeShipping: 1, pointAccelerator: 200 }
        ]
        this.updateLevelInfo()
      }
    },
    
    updateLevelInfo() {
      const current = this.memberLevels.find(l => l.id === this.userInfo.memberLevelId)
      if (current) {
        this.currentLevel = current
        const currentIndex = this.memberLevels.findIndex(l => l.id === current.id)
        if (currentIndex < this.memberLevels.length - 1) {
          this.nextLevel = this.memberLevels[currentIndex + 1]
        }
      } else {
        this.currentLevel = this.memberLevels[0]
        this.nextLevel = this.memberLevels[1]
      }
    },
    
    async loadCouponCount() {
      try {
        const res = await api.getUserCoupons()
        if (res.code === 200) {
          this.couponCount = res.data.filter(c => c.status === 0).length
        }
      } catch (e) {
        console.error('获取优惠券失败', e)
        this.couponCount = 3
      }
    },
    
    goToPoints() {
      uni.navigateTo({
        url: '/pages/points/points'
      })
    },
    
    goToCoupons() {
      uni.showToast({
        title: '优惠券功能开发中',
        icon: 'none'
      })
    },
    
    goToOrders() {
      uni.switchTab({
        url: '/pages/user/user'
      })
    },
    
    goToAddress() {
      uni.showToast({
        title: '地址功能开发中',
        icon: 'none'
      })
    }
  }
}
</script>

<style scoped>
.member-page {
  min-height: 100vh;
  background-color: #F8F8F8;
}

.member-header {
  background: linear-gradient(135deg, #FF6B9D 0%, #FF8E53 100%);
  padding: 40rpx;
  padding-top: 60rpx;
}

.user-info-row {
  display: flex;
  align-items: center;
  margin-bottom: 40rpx;
}

.user-avatar {
  width: 120rpx;
  height: 120rpx;
  border-radius: 60rpx;
  border: 3rpx solid #FFFFFF;
  margin-right: 30rpx;
}

.user-info {
  flex: 1;
}

.user-name {
  display: block;
  font-size: 36rpx;
  font-weight: bold;
  color: #FFFFFF;
  margin-bottom: 15rpx;
}

.member-level {
  display: inline-block;
}

.level-tag {
  display: inline-block;
  background-color: rgba(255, 255, 255, 0.3);
  color: #FFFFFF;
  font-size: 24rpx;
  padding: 8rpx 20rpx;
  border-radius: 20rpx;
}

.member-stats {
  display: flex;
  justify-content: space-around;
  background-color: rgba(255, 255, 255, 0.15);
  border-radius: 16rpx;
  padding: 30rpx 0;
}

.stat-item {
  text-align: center;
}

.stat-num {
  display: block;
  font-size: 40rpx;
  font-weight: bold;
  color: #FFFFFF;
  margin-bottom: 8rpx;
}

.stat-label {
  font-size: 24rpx;
  color: rgba(255, 255, 255, 0.8);
}

.level-section {
  background-color: #FFFFFF;
  margin: -20rpx 30rpx 30rpx;
  border-radius: 16rpx;
  padding: 30rpx;
  position: relative;
  z-index: 10;
  box-shadow: 0 4rpx 12rpx rgba(0, 0, 0, 0.05);
}

.level-info {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20rpx;
}

.level-title {
  font-size: 28rpx;
  color: #666666;
}

.level-amount {
  font-size: 32rpx;
  font-weight: bold;
  color: #FF6B9D;
}

.progress-bar {
  height: 16rpx;
  background-color: #F0F0F0;
  border-radius: 8rpx;
  overflow: hidden;
}

.progress-inner {
  height: 100%;
  background: linear-gradient(135deg, #FF6B9D 0%, #FF8E53 100%);
  border-radius: 8rpx;
  transition: width 0.3s ease;
}

.benefits-section {
  background-color: #FFFFFF;
  margin: 0 30rpx 30rpx;
  border-radius: 16rpx;
  padding: 30rpx;
}

.section-title {
  font-size: 32rpx;
  font-weight: bold;
  color: #333333;
  margin-bottom: 30rpx;
}

.benefits-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 20rpx;
}

.benefit-item {
  text-align: center;
}

.benefit-icon {
  font-size: 48rpx;
  margin-bottom: 10rpx;
}

.benefit-name {
  display: block;
  font-size: 24rpx;
  color: #333333;
  margin-bottom: 5rpx;
}

.benefit-desc {
  font-size: 20rpx;
  color: #FF6B9D;
}

.menu-section {
  background-color: #FFFFFF;
  margin: 0 30rpx 30rpx;
  border-radius: 16rpx;
  overflow: hidden;
}

.menu-item {
  display: flex;
  align-items: center;
  padding: 30rpx;
  border-bottom: 1rpx solid #F0F0F0;
}

.menu-item:last-child {
  border-bottom: none;
}

.menu-icon {
  font-size: 40rpx;
  margin-right: 20rpx;
}

.menu-name {
  flex: 1;
  font-size: 28rpx;
  color: #333333;
}

.menu-arrow {
  font-size: 32rpx;
  color: #CCCCCC;
}

.level-list-section {
  background-color: #FFFFFF;
  margin: 0 30rpx 30rpx;
  border-radius: 16rpx;
  padding: 30rpx;
}

.level-list {
  display: flex;
  flex-direction: column;
  gap: 20rpx;
}

.level-item {
  display: flex;
  align-items: center;
  padding: 20rpx;
  background-color: #F8F8F8;
  border-radius: 12rpx;
}

.level-icon {
  width: 60rpx;
  height: 60rpx;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 32rpx;
  background-color: #FFFFFF;
  border-radius: 30rpx;
  margin-right: 20rpx;
  opacity: 0.5;
}

.level-icon.active {
  opacity: 1;
  background: linear-gradient(135deg, #FF6B9D 0%, #FF8E53 100%);
}

.level-info {
  flex: 1;
}

.level-name {
  display: block;
  font-size: 28rpx;
  font-weight: bold;
  color: #333333;
  margin-bottom: 5rpx;
}

.level-desc {
  font-size: 22rpx;
  color: #999999;
}

.level-condition {
  font-size: 22rpx;
  color: #FF6B9D;
}
</style>
