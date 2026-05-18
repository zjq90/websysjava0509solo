<template>
  <view class="container">
    <view class="points-header">
      <text class="points-label">我的积分</text>
      <text class="points-value">{{ userPoints }}</text>
    </view>

    <view class="task-section">
      <view class="section-title">每日任务</view>
      <view class="task-list">
        <view class="task-item">
          <view class="task-info">
            <text class="task-icon">📅</text>
            <view class="task-detail">
              <text class="task-name">每日签到</text>
              <text class="task-desc">+10积分</text>
            </view>
          </view>
          <button class="task-btn" :class="{ done: signed }" @click="signIn" :disabled="signed">
            {{ signed ? '已签到' : '立即签到' }}
          </button>
        </view>
        <view class="task-item">
          <view class="task-info">
            <text class="task-icon">📦</text>
            <view class="task-detail">
              <text class="task-name">发布商品</text>
              <text class="task-desc">+50积分</text>
            </view>
          </view>
          <button class="task-btn" @click="goPublish">去发布</button>
        </view>
        <view class="task-item">
          <view class="task-info">
            <text class="task-icon">🛒</text>
            <view class="task-detail">
              <text class="task-name">完成订单</text>
              <text class="task-desc">+20积分</text>
            </view>
          </view>
          <button class="task-btn" @click="goShopping">去购物</button>
        </view>
      </view>
    </view>

    <view class="record-section">
      <view class="section-title">积分记录</view>
      <view class="record-list" v-if="records.length > 0">
        <view class="record-item" v-for="record in records" :key="record.id">
          <view class="record-info">
            <text class="record-name">{{ record.description }}</text>
            <text class="record-time">{{ formatTime(record.createTime) }}</text>
          </view>
          <text class="record-points" :class="{ positive: record.points > 0 }">
            {{ record.points > 0 ? '+' : '' }}{{ record.points }}
          </text>
        </view>
      </view>
      <view class="empty-state" v-else>
        <text class="empty-icon">📝</text>
        <text class="empty-text">暂无积分记录</text>
      </view>
    </view>
  </view>
</template>

<script>
export default {
  data() {
    return {
      userPoints: 0,
      signed: false,
      records: []
    }
  },
  onShow() {
    this.loadData()
  },
  methods: {
    loadData() {
      const user = uni.getStorageSync('user')
      if (user) {
        this.userPoints = user.points || 0
      }
      this.checkSignStatus()
    },
    checkSignStatus() {
      const today = new Date().toDateString()
      const lastSign = uni.getStorageSync('lastSignDate')
      this.signed = lastSign === today
    },
    signIn() {
      if (this.signed) return
      
      const today = new Date().toDateString()
      uni.setStorageSync('lastSignDate', today)
      
      this.userPoints += 10
      this.signed = true
      
      const user = uni.getStorageSync('user')
      if (user) {
        user.points = this.userPoints
        uni.setStorageSync('user', user)
      }
      
      this.records.unshift({
        id: Date.now(),
        description: '每日签到',
        points: 10,
        createTime: new Date().toISOString()
      })
      
      uni.showToast({
        title: '+10积分',
        icon: 'success'
      })
    },
    goPublish() {
      uni.navigateTo({
        url: '/pages/product/publish'
      })
    },
    goShopping() {
      uni.switchTab({
        url: '/pages/index/index'
      })
    },
    formatTime(time) {
      if (!time) return ''
      const date = new Date(time)
      const month = String(date.getMonth() + 1).padStart(2, '0')
      const day = String(date.getDate()).padStart(2, '0')
      const hour = String(date.getHours()).padStart(2, '0')
      const minute = String(date.getMinutes()).padStart(2, '0')
      return `${month}-${day} ${hour}:${minute}`
    }
  }
}
</script>

<style scoped>
.container {
  min-height: 100vh;
  background: #f5f5f5;
}

.points-header {
  background: linear-gradient(135deg, #ff6b6b 0%, #ff4757 100%);
  padding: 80rpx 30rpx;
  text-align: center;
}

.points-label {
  display: block;
  font-size: 28rpx;
  color: rgba(255,255,255,0.8);
  margin-bottom: 20rpx;
}

.points-value {
  display: block;
  font-size: 72rpx;
  font-weight: bold;
  color: #fff;
}

.task-section {
  background: #fff;
  margin: 20rpx;
  border-radius: 16rpx;
  padding: 30rpx;
}

.section-title {
  font-size: 32rpx;
  font-weight: bold;
  color: #333;
  margin-bottom: 30rpx;
}

.task-list {
  display: flex;
  flex-direction: column;
  gap: 20rpx;
}

.task-item {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 20rpx;
  background: #f8f9fa;
  border-radius: 12rpx;
}

.task-info {
  display: flex;
  align-items: center;
}

.task-icon {
  font-size: 40rpx;
  margin-right: 20rpx;
}

.task-detail {
  display: flex;
  flex-direction: column;
}

.task-name {
  font-size: 28rpx;
  color: #333;
  font-weight: 500;
}

.task-desc {
  font-size: 22rpx;
  color: #ff6b6b;
  margin-top: 5rpx;
}

.task-btn {
  width: 160rpx;
  height: 60rpx;
  line-height: 60rpx;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: #fff;
  border-radius: 30rpx;
  font-size: 24rpx;
  border: none;
}

.task-btn.done {
  background: #ccc;
}

.record-section {
  background: #fff;
  margin: 20rpx;
  border-radius: 16rpx;
  padding: 30rpx;
}

.record-list {
  display: flex;
  flex-direction: column;
}

.record-item {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 25rpx 0;
  border-bottom: 1rpx solid #f0f0f0;
}

.record-item:last-child {
  border-bottom: none;
}

.record-info {
  display: flex;
  flex-direction: column;
}

.record-name {
  font-size: 28rpx;
  color: #333;
  margin-bottom: 8rpx;
}

.record-time {
  font-size: 22rpx;
  color: #999;
}

.record-points {
  font-size: 32rpx;
  font-weight: bold;
  color: #ff4d4f;
}

.record-points.positive {
  color: #52c41a;
}

.empty-state {
  padding: 80rpx 0;
  text-align: center;
}

.empty-icon {
  display: block;
  font-size: 80rpx;
  margin-bottom: 20rpx;
}

.empty-text {
  font-size: 28rpx;
  color: #999;
}
</style>
