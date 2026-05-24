<template>
  <view class="ride-container">
    <view class="map-container">
      <view class="map-placeholder">
        <view class="route-line"></view>
        <view class="start-point">
          <text class="point-icon">📍</text>
        </view>
        <view class="current-point">
          <text class="point-icon">🚲</text>
        </view>
      </view>
    </view>

    <view class="ride-info">
      <view class="info-header">
        <text class="bike-no">{{ bikeNo }}</text>
        <view class="ride-status">
          <text class="status-dot"></text>
          <text class="status-text">骑行中</text>
        </view>
      </view>

      <view class="stats-grid">
        <view class="stat-item">
          <text class="stat-value">{{ formatTime(duration) }}</text>
          <text class="stat-label">骑行时长</text>
        </view>
        <view class="stat-item">
          <text class="stat-value">{{ distance.toFixed(1) }}</text>
          <text class="stat-label">里程(km)</text>
        </view>
        <view class="stat-item">
          <text class="stat-value">¥{{ currentCost.toFixed(2) }}</text>
          <text class="stat-label">当前费用</text>
        </view>
      </view>

      <view class="price-tip">
        <text class="tip-text">计费规则：起步价1.5元/30分钟，超时0.5元/15分钟</text>
      </view>
    </view>

    <view class="action-buttons">
      <view class="action-btn nav-btn" @click="openNavigation">
        <text class="btn-icon">🧭</text>
        <text class="btn-text">导航</text>
      </view>
      <view class="action-btn lock-btn" @click="lockBike">
        <text class="btn-icon">🔒</text>
        <text class="btn-text">锁车</text>
      </view>
      <view class="action-btn fault-btn" @click="reportFault">
        <text class="btn-icon">⚠️</text>
        <text class="btn-text">报修</text>
      </view>
    </view>

    <view class="lock-modal" v-if="showLockModal" @click="showLockModal = false">
      <view class="lock-content" @click.stop>
        <view class="lock-icon">🔒</view>
        <text class="lock-title">确认锁车？</text>
        <text class="lock-desc">锁车后将结束本次骑行并结算费用</text>
        <view class="lock-actions">
          <button class="cancel-btn" @click="showLockModal = false">取消</button>
          <button class="confirm-btn" @click="confirmLock">确认锁车</button>
        </view>
      </view>
    </view>
  </view>
</template>

<script setup>
import { ref, onMounted, onUnmounted } from 'vue'

const bikeNo = ref('E012')
const duration = ref(0)
const distance = ref(2.3)
const currentCost = ref(1.5)
const showLockModal = ref(false)

let timer = null

const formatTime = (seconds) => {
  const mins = Math.floor(seconds / 60)
  const secs = seconds % 60
  return `${mins.toString().padStart(2, '0')}:${secs.toString().padStart(2, '0')}`
}

const openNavigation = () => {
  uni.showToast({ title: '正在打开导航...', icon: 'none' })
}

const lockBike = () => {
  showLockModal.value = true
}

const reportFault = () => {
  uni.navigateTo({ url: '/pages/fault/fault' })
}

const confirmLock = () => {
  showLockModal.value = false
  uni.showLoading({ title: '锁车中...' })
  
  setTimeout(() => {
    uni.hideLoading()
    uni.showModal({
      title: '骑行结束',
      content: `本次骑行${formatTime(duration.value)}，费用¥${currentCost.value.toFixed(2)}`,
      showCancel: false,
      success: () => {
        uni.switchTab({ url: '/pages/index/index' })
      }
    })
  }, 2000)
}

onMounted(() => {
  timer = setInterval(() => {
    duration.value++
    if (duration.value > 0 && duration.value % 60 === 0) {
      distance.value += 0.2
    }
    if (duration.value > 1800) {
      currentCost.value = 1.5 + Math.floor((duration.value - 1800) / 900) * 0.5
    }
  }, 1000)
})

onUnmounted(() => {
  if (timer) {
    clearInterval(timer)
  }
})
</script>

<style lang="scss" scoped>
.ride-container {
  height: 100vh;
  display: flex;
  flex-direction: column;
}

.map-container {
  flex: 1;
  position: relative;
  background: linear-gradient(135deg, #e8f5e9 0%, #c8e6c9 100%);
}

.map-placeholder {
  width: 100%;
  height: 100%;
  position: relative;
}

.route-line {
  position: absolute;
  top: 30%;
  left: 20%;
  width: 60%;
  height: 4px;
  background: #00A862;
  border-radius: 2px;
  transform: rotate(-15deg);
}

.start-point {
  position: absolute;
  top: 25%;
  left: 15%;
}

.current-point {
  position: absolute;
  top: 45%;
  left: 65%;
  animation: pulse 2s infinite;
}

.point-icon {
  font-size: 32px;
}

@keyframes pulse {
  0%, 100% { transform: scale(1); }
  50% { transform: scale(1.1); }
}

.ride-info {
  background: white;
  padding: 20px 16px;
  border-radius: 24px 24px 0 0;
  margin-top: -20px;
  position: relative;
  z-index: 10;
}

.info-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.bike-no {
  font-size: 18px;
  font-weight: bold;
  color: #333;
}

.ride-status {
  display: flex;
  align-items: center;
  gap: 6px;
}

.status-dot {
  width: 8px;
  height: 8px;
  background: #4caf50;
  border-radius: 50%;
  animation: blink 1s infinite;
}

@keyframes blink {
  0%, 100% { opacity: 1; }
  50% { opacity: 0.5; }
}

.status-text {
  font-size: 14px;
  color: #4caf50;
}

.stats-grid {
  display: flex;
  justify-content: space-around;
  margin-bottom: 20px;
}

.stat-item {
  text-align: center;
}

.stat-value {
  display: block;
  font-size: 28px;
  font-weight: bold;
  color: #333;
  margin-bottom: 4px;
}

.stat-label {
  font-size: 12px;
  color: #999;
}

.price-tip {
  background: #f5f5f5;
  padding: 12px;
  border-radius: 8px;
}

.tip-text {
  font-size: 12px;
  color: #666;
}

.action-buttons {
  background: white;
  padding: 20px 16px;
  display: flex;
  justify-content: space-around;
  border-top: 1px solid #f0f0f0;
}

.action-btn {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 8px;
  padding: 16px;
  border-radius: 16px;
  min-width: 80px;
}

.nav-btn {
  background: #e3f2fd;
}

.lock-btn {
  background: linear-gradient(135deg, #00A862 0%, #00c874 100%);
  transform: scale(1.1);
}

.fault-btn {
  background: #fff3e0;
}

.btn-icon {
  font-size: 28px;
}

.btn-text {
  font-size: 14px;
  color: #333;
}

.lock-btn .btn-text {
  color: white;
  font-weight: bold;
}

.lock-modal {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0,0,0,0.5);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 100;
}

.lock-content {
  width: 80%;
  background: white;
  border-radius: 20px;
  padding: 32px 24px;
  text-align: center;
}

.lock-icon {
  font-size: 64px;
  margin-bottom: 16px;
}

.lock-title {
  display: block;
  font-size: 20px;
  font-weight: bold;
  color: #333;
  margin-bottom: 8px;
}

.lock-desc {
  font-size: 14px;
  color: #666;
  margin-bottom: 24px;
}

.lock-actions {
  display: flex;
  gap: 12px;
}

.cancel-btn {
  flex: 1;
  height: 48px;
  background: #f5f5f5;
  color: #666;
  border: none;
  border-radius: 12px;
  font-size: 16px;
}

.confirm-btn {
  flex: 1;
  height: 48px;
  background: #00A862;
  color: white;
  border: none;
  border-radius: 12px;
  font-size: 16px;
}
</style>
