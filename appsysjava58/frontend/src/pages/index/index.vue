<template>
  <view class="container">
    <view class="header">
      <text class="greeting">你好，铲屎官👋</text>
      <text class="subtitle">守护毛孩子的健康</text>
    </view>

    <view class="pet-selector">
      <view class="pet-card" v-for="pet in pets" :key="pet.id">
        <view class="pet-avatar">
          <text class="pet-emoji">{{ pet.type === 'dog' ? '🐕' : '🐱' }}</text>
        </view>
        <view class="pet-info">
          <text class="pet-name">{{ pet.name }}</text>
          <text class="pet-breed">{{ pet.breed }}</text>
        </view>
        <view class="pet-stats">
          <text class="stat-item">体重: {{ pet.weight }}kg</text>
        </view>
      </view>
    </view>

    <view class="health-section">
      <view class="section-header">
        <text class="section-title">健康提醒</text>
        <text class="section-more" @click="goToHealth">查看全部 →</text>
      </view>

      <view class="health-cards">
        <view class="health-card deworming" @click="goToDeworming">
          <view class="card-icon">💊</view>
          <view class="card-content">
            <text class="card-title">驱虫提醒</text>
            <text class="card-status" :class="'status-' + dewormingStatus">
              {{ dewormingDays > 0 ? '还有' + dewormingDays + '天' : '已到期' }}
            </text>
          </view>
          <view class="check-mark" v-if="dewormingStatus === 'completed'">✅</view>
        </view>

        <view class="health-card checkup" @click="goToCheckup">
          <view class="card-icon">🏥</view>
          <view class="card-content">
            <text class="card-title">体检提醒</text>
            <text class="card-status status-pending">下次体检: 45天后</text>
          </view>
        </view>
      </view>
    </view>

    <view class="quick-actions">
      <view class="action-item" @click="goToDiet">
        <view class="action-icon diet">🍽️</view>
        <text class="action-text">饮食建议</text>
      </view>
      <view class="action-item" @click="goToExercise">
        <view class="action-icon exercise">🏃</view>
        <text class="action-text">运动监测</text>
      </view>
      <view class="action-item" @click="goToCircle">
        <view class="action-icon circle">📱</view>
        <text class="action-text">宠物圈</text>
      </view>
      <view class="action-item" @click="goToHospital">
        <view class="action-icon hospital">🏥</view>
        <text class="action-text">附近医院</text>
      </view>
    </view>
  </view>
</template>

<script>
export default {
  data() {
    return {
      pets: [
        { id: 1, name: '旺财', type: 'dog', breed: '金毛', weight: 25.5 },
        { id: 2, name: '咪咪', type: 'cat', breed: '英短', weight: 4.2 }
      ],
      dewormingStatus: 'upcoming',
      dewormingDays: 20
    }
  },
  onLoad() {
    this.loadHealthData()
  },
  methods: {
    loadHealthData() {
      this.$request('/health/cards/1').then(res => {
        if (res.code === 200 && res.data.deworming) {
          const d = res.data.deworming
          this.dewormingStatus = d.status
          this.dewormingDays = d.daysRemaining || 0
        }
      }).catch(() => {
        console.log('使用默认数据')
      })
    },
    goToHealth() {
      uni.navigateTo({ url: '/pages/health/cards' })
    },
    goToDeworming() {
      uni.navigateTo({ url: '/pages/health/deworming-history' })
    },
    goToCheckup() {
      uni.navigateTo({ url: '/pages/health/cards' })
    },
    goToDiet() {
      uni.navigateTo({ url: '/pages/diet/suggestion' })
    },
    goToExercise() {
      uni.navigateTo({ url: '/pages/exercise/stats' })
    },
    goToCircle() {
      uni.switchTab({ url: '/pages/circle/posts' })
    },
    goToHospital() {
      uni.switchTab({ url: '/pages/hospital/list' })
    }
  }
}
</script>

<style scoped>
.header {
  padding: 40rpx 0;
}

.greeting {
  display: block;
  font-size: 48rpx;
  font-weight: bold;
  color: #333;
}

.subtitle {
  display: block;
  font-size: 28rpx;
  color: #999;
  margin-top: 10rpx;
}

.pet-selector {
  margin-bottom: 40rpx;
}

.pet-card {
  display: flex;
  align-items: center;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border-radius: 20rpx;
  padding: 30rpx;
  margin-bottom: 20rpx;
  color: #fff;
}

.pet-avatar {
  width: 100rpx;
  height: 100rpx;
  background: rgba(255,255,255,0.2);
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-right: 30rpx;
}

.pet-emoji {
  font-size: 50rpx;
}

.pet-info {
  flex: 1;
}

.pet-name {
  display: block;
  font-size: 36rpx;
  font-weight: bold;
}

.pet-breed {
  display: block;
  font-size: 24rpx;
  opacity: 0.8;
}

.pet-stats {
  text-align: right;
}

.stat-item {
  display: block;
  font-size: 24rpx;
}

.health-section {
  margin-bottom: 40rpx;
}

.section-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20rpx;
}

.section-title {
  font-size: 32rpx;
  font-weight: bold;
  color: #333;
}

.section-more {
  font-size: 24rpx;
  color: #667eea;
}

.health-cards {
  display: flex;
  flex-direction: column;
  gap: 20rpx;
}

.health-card {
  display: flex;
  align-items: center;
  background: #fff;
  border-radius: 16rpx;
  padding: 30rpx;
  box-shadow: 0 4rpx 12rpx rgba(0,0,0,0.08);
  position: relative;
}

.card-icon {
  font-size: 50rpx;
  margin-right: 30rpx;
}

.card-content {
  flex: 1;
}

.card-title {
  display: block;
  font-size: 32rpx;
  font-weight: bold;
  color: #333;
}

.card-status {
  display: block;
  font-size: 24rpx;
  margin-top: 8rpx;
}

.check-mark {
  font-size: 40rpx;
}

.quick-actions {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 20rpx;
}

.action-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  background: #fff;
  border-radius: 16rpx;
  padding: 30rpx 20rpx;
  box-shadow: 0 4rpx 12rpx rgba(0,0,0,0.08);
}

.action-icon {
  width: 80rpx;
  height: 80rpx;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 40rpx;
  margin-bottom: 15rpx;
}

.action-icon.diet {
  background: #e3f2fd;
}

.action-icon.exercise {
  background: #e8f5e9;
}

.action-icon.circle {
  background: #fff3e0;
}

.action-icon.hospital {
  background: #fce4ec;
}

.action-text {
  font-size: 24rpx;
  color: #666;
}
</style>