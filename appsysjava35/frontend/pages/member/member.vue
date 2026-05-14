<template>
  <view class="container">
    <view class="member-header card">
      <view class="current-level">
        <text class="level-icon">👑</text>
        <view class="level-info">
          <text class="level-name">{{ userInfo.memberLevel?.name || '普通会员' }}</text>
          <text class="level-desc">当前等级</text>
        </view>
      </view>
      <view class="growth-progress">
        <view class="progress-bar">
          <view class="progress-fill" :style="{ width: progressPercent + '%' }"></view>
        </view>
        <view class="progress-text">
          <text>成长值 {{ userInfo.growth || 0 }}</text>
          <text v-if="nextLevel">距 {{ nextLevel.name }} 还需 {{ nextLevel.requiredGrowth - userInfo.growth }}</text>
        </view>
      </view>
    </view>

    <view class="benefits-card card">
      <text class="card-title">会员权益</text>
      <view class="benefits-list">
        <view class="benefit-item">
          <view class="benefit-icon" style="background-color: #e8f3ff;">
            <text>✨</text>
          </view>
          <view class="benefit-content">
            <text class="benefit-name">积分倍率</text>
            <text class="benefit-desc">{{ userInfo.memberLevel?.pointMultiplier || 1.0 }}倍积分</text>
          </view>
        </view>
        <view class="benefit-item" v-if="userInfo.memberLevel?.level >= 3">
          <view class="benefit-icon" style="background-color: #e8f9f1;">
            <text>⚡</text>
          </view>
          <view class="benefit-content">
            <text class="benefit-name">优先客服</text>
            <text class="benefit-desc">专属客服通道，优先响应</text>
          </view>
        </view>
        <view class="benefit-item" v-if="userInfo.memberLevel?.level >= 4">
          <view class="benefit-icon" style="background-color: #fff3e8;">
            <text>💼</text>
          </view>
          <view class="benefit-content">
            <text class="benefit-name">专属经理</text>
            <text class="benefit-desc">一对一客户经理服务</text>
          </view>
        </view>
        <view class="benefit-item">
          <view class="benefit-icon" style="background-color: #ffeef0;">
            <text>🎂</text>
          </view>
          <view class="benefit-content">
            <text class="benefit-name">生日礼包</text>
            <text class="benefit-desc">生日当月专属礼包</text>
          </view>
        </view>
      </view>
    </view>

    <view class="all-levels card">
      <text class="card-title">全部等级</text>
      <view class="level-list">
        <view 
          class="level-item" 
          v-for="level in memberLevels" 
          :key="level.id"
          :class="{ 'level-active': level.id === userInfo.memberLevelId }"
        >
          <view class="level-header">
            <view class="level-badge" :class="getLevelClass(level.level)">
              {{ level.name }}
            </view>
            <text class="level-growth">需 {{ level.requiredGrowth }} 成长值</text>
          </view>
          <view class="level-benefits">
            <text class="benefit-tag">积分倍率 {{ level.pointMultiplier }}x</text>
            <text class="benefit-tag" v-if="level.priorityService">优先客服</text>
            <text class="benefit-tag" v-if="level.hasAccountManager">专属经理</text>
          </view>
        </view>
      </view>
    </view>

    <view class="upgrade-tips card" v-if="userInfo.memberLevel?.level < 4">
      <text class="card-title">升级攻略</text>
      <view class="tips-list">
        <view class="tip-item">
          <text class="tip-icon">📝</text>
          <view class="tip-content">
            <text class="tip-name">每日签到</text>
            <text class="tip-desc">+10~30 成长值</text>
          </view>
        </view>
        <view class="tip-item">
          <text class="tip-icon">🛒</text>
          <view class="tip-content">
            <text class="tip-name">办理业务</text>
            <text class="tip-desc">+50~200 成长值</text>
          </view>
        </view>
        <view class="tip-item">
          <text class="tip-icon">⭐</text>
          <view class="tip-content">
            <text class="tip-name">评价服务</text>
            <text class="tip-desc">+20 成长值</text>
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
      memberLevels: []
    }
  },
  computed: {
    nextLevel() {
      const currentLevel = this.userInfo.memberLevel?.level || 1
      return this.memberLevels.find(l => l.level === currentLevel + 1)
    },
    progressPercent() {
      const currentGrowth = this.userInfo.growth || 0
      const maxGrowth = this.nextLevel ? this.nextLevel.requiredGrowth : 100
      const minGrowth = this.userInfo.memberLevel?.requiredGrowth || 0
      if (maxGrowth <= minGrowth) return 100
      return Math.min(100, Math.round((currentGrowth - minGrowth) / (maxGrowth - minGrowth) * 100))
    }
  },
  onShow() {
    this.loadUserInfo()
    this.loadMemberLevels()
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

    async loadMemberLevels() {
      try {
        this.memberLevels = await api.getMemberLevels()
      } catch (e) {
        console.error(e)
      }
    },

    getLevelClass(level) {
      const classMap = {
        1: 'tag-blue',
        2: 'tag-orange',
        3: 'tag-green',
        4: 'tag-red'
      }
      return classMap[level] || 'tag-blue'
    }
  }
}
</script>

<style scoped>
.member-header {
  background: linear-gradient(135deg, #ff976a 0%, #ff6b35 100%);
  color: #ffffff;
}

.current-level {
  display: flex;
  align-items: center;
  margin-bottom: 30rpx;
}

.level-icon {
  font-size: 48rpx;
  margin-right: 20rpx;
}

.level-info {
  flex: 1;
}

.level-name {
  font-size: 32rpx;
  font-weight: 600;
  display: block;
  margin-bottom: 4rpx;
}

.level-desc {
  font-size: 24rpx;
  opacity: 0.8;
}

.growth-progress {
  margin-top: 20rpx;
}

.progress-bar {
  height: 16rpx;
  background: rgba(255, 255, 255, 0.3);
  border-radius: 8rpx;
  overflow: hidden;
  margin-bottom: 12rpx;
}

.progress-fill {
  height: 100%;
  background: #ffffff;
  border-radius: 8rpx;
  transition: width 0.3s ease;
}

.progress-text {
  display: flex;
  justify-content: space-between;
  font-size: 22rpx;
  opacity: 0.9;
}

.benefits-list {
  margin-top: 20rpx;
}

.benefit-item {
  display: flex;
  align-items: center;
  padding: 24rpx 0;
  border-bottom: 1rpx solid #f0f0f0;
}

.benefit-item:last-child {
  border-bottom: none;
}

.benefit-icon {
  width: 64rpx;
  height: 64rpx;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-right: 20rpx;
  font-size: 28rpx;
}

.benefit-content {
  flex: 1;
}

.benefit-name {
  font-size: 28rpx;
  color: #333;
  display: block;
  margin-bottom: 4rpx;
}

.benefit-desc {
  font-size: 24rpx;
  color: #999;
}

.level-list {
  margin-top: 20rpx;
}

.level-item {
  padding: 24rpx;
  border-radius: 16rpx;
  margin-bottom: 16rpx;
  border: 2rpx solid #f0f0f0;
}

.level-item.level-active {
  border-color: #ff976a;
  background: #fff8f5;
}

.level-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 12rpx;
}

.level-badge {
  padding: 4rpx 16rpx;
  border-radius: 20rpx;
  font-size: 24rpx;
  font-weight: 500;
}

.level-growth {
  font-size: 22rpx;
  color: #999;
}

.level-benefits {
  display: flex;
  flex-wrap: wrap;
  gap: 12rpx;
}

.benefit-tag {
  padding: 4rpx 12rpx;
  background: #f5f5f5;
  border-radius: 8rpx;
  font-size: 22rpx;
  color: #666;
}

.tips-list {
  margin-top: 20rpx;
}

.tip-item {
  display: flex;
  align-items: center;
  padding: 20rpx 0;
}

.tip-icon {
  font-size: 32rpx;
  margin-right: 20rpx;
  width: 40rpx;
  text-align: center;
}

.tip-content {
  flex: 1;
}

.tip-name {
  font-size: 28rpx;
  color: #333;
  display: block;
  margin-bottom: 4rpx;
}

.tip-desc {
  font-size: 24rpx;
  color: #1989fa;
}
</style>
