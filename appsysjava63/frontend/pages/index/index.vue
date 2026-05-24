<template>
  <view class="index-container">
    <view class="map-container">
      <view class="map-placeholder">
        <view class="map-center-marker"></view>
        <view class="bike-marker bike-1" @click="selectBike(mockBikes[0])">
          <text class="bike-icon">🚲</text>
        </view>
        <view class="bike-marker bike-2" @click="selectBike(mockBikes[1])">
          <text class="bike-icon">🛵</text>
        </view>
        <view class="bike-marker bike-3" @click="selectBike(mockBikes[2])">
          <text class="bike-icon">🚲</text>
        </view>
        <view class="bike-marker bike-4" @click="selectBike(mockBikes[3])">
          <text class="bike-icon">🚴</text>
        </view>
      </view>
      <view class="map-overlay">
        <view class="search-bar">
          <text class="search-icon">🔍</text>
          <text class="search-text">搜索附近车辆</text>
        </view>
        <view class="filter-btn" @click="showFilter = true">
          <text>筛选</text>
        </view>
      </view>
    </view>

    <view class="bottom-panel">
      <view class="quick-actions">
        <view class="action-item scan-btn" @click="goToScan">
          <view class="action-icon">📷</view>
          <text class="action-text">扫码开锁</text>
        </view>
        <view class="action-item find-btn" @click="findNearbyBikes">
          <view class="action-icon">📍</view>
          <text class="action-text">附近车辆</text>
        </view>
        <view class="action-item mine-btn" @click="goToMine">
          <view class="action-icon">👤</view>
          <text class="action-text">我的</text>
        </view>
      </view>

      <view class="bike-info" v-if="selectedBike">
        <view class="bike-card">
          <view class="bike-header">
            <text class="bike-type">{{ selectedBike.typeName }}</text>
            <text class="bike-no">#{{ selectedBike.bikeNo }}</text>
          </view>
          <view class="bike-details">
            <view class="detail-item">
              <text class="detail-label">距离</text>
              <text class="detail-value">{{ selectedBike.distance }}米</text>
            </view>
            <view class="detail-item" v-if="selectedBike.type !== 1">
              <text class="detail-label">电量</text>
              <text class="detail-value">{{ selectedBike.battery }}%</text>
            </view>
            <view class="detail-item">
              <text class="detail-label">价格</text>
              <text class="detail-value">¥{{ selectedBike.price }}/30分钟</text>
            </view>
          </view>
          <view class="bike-actions">
            <button class="reserve-btn" @click="reserveBike">预约车辆</button>
            <button class="nav-btn" @click="navigateToBike">导航前往</button>
          </view>
        </view>
      </view>

      <view class="stats-bar" v-if="!selectedBike">
        <view class="stat-item">
          <text class="stat-value">{{ nearbyCount }}</text>
          <text class="stat-label">附近车辆</text>
        </view>
        <view class="stat-divider"></view>
        <view class="stat-item">
          <text class="stat-value">{{ electricCount }}</text>
          <text class="stat-label">电动车</text>
        </view>
        <view class="stat-divider"></view>
        <view class="stat-item">
          <text class="stat-value">{{ normalCount }}</text>
          <text class="stat-label">普通车</text>
        </view>
      </view>
    </view>

    <view class="filter-modal" v-if="showFilter" @click="showFilter = false">
      <view class="filter-content" @click.stop>
        <view class="filter-header">
          <text class="filter-title">筛选条件</text>
          <text class="filter-close" @click="showFilter = false">✕</text>
        </view>
        <view class="filter-section">
          <text class="section-title">车型</text>
          <view class="filter-options">
            <text class="option-item" :class="{ active: filter.type === 0 }" @click="filter.type = 0">全部</text>
            <text class="option-item" :class="{ active: filter.type === 1 }" @click="filter.type = 1">普通单车</text>
            <text class="option-item" :class="{ active: filter.type === 2 }" @click="filter.type = 2">电动车</text>
            <text class="option-item" :class="{ active: filter.type === 3 }" @click="filter.type = 3">助力车</text>
          </view>
        </view>
        <view class="filter-section">
          <text class="section-title">电量(电动车)</text>
          <view class="filter-options">
            <text class="option-item" :class="{ active: filter.battery === 0 }" @click="filter.battery = 0">全部</text>
            <text class="option-item" :class="{ active: filter.battery === 50 }" @click="filter.battery = 50">50%以上</text>
            <text class="option-item" :class="{ active: filter.battery === 80 }" @click="filter.battery = 80">80%以上</text>
          </view>
        </view>
        <view class="filter-actions">
          <button class="reset-btn" @click="resetFilter">重置</button>
          <button class="confirm-btn" @click="applyFilter">确定</button>
        </view>
      </view>
    </view>
  </view>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useUserStore } from '../../stores/user'

const userStore = useUserStore()

const showFilter = ref(false)
const selectedBike = ref(null)
const nearbyCount = ref(12)
const electricCount = ref(5)
const normalCount = ref(7)

const filter = ref({
  type: 0,
  battery: 0
})

const mockBikes = ref([
  { id: 1, bikeNo: 'B001', type: 1, typeName: '普通单车', distance: 58, price: 1.5, battery: 100 },
  { id: 2, bikeNo: 'E012', type: 2, typeName: '电动车', distance: 120, price: 2.5, battery: 85 },
  { id: 3, bikeNo: 'B023', type: 1, typeName: '普通单车', distance: 200, price: 1.5, battery: 100 },
  { id: 4, bikeNo: 'A008', type: 3, typeName: '助力车', distance: 350, price: 3.0, battery: 72 }
])

const selectBike = (bike) => {
  selectedBike.value = bike
}

const goToScan = () => {
  uni.navigateTo({ url: '/pages/scan/scan' })
}

const findNearbyBikes = () => {
  uni.showToast({ title: '正在查找附近车辆...', icon: 'loading' })
  setTimeout(() => {
    uni.showToast({ title: `找到${nearbyCount.value}辆车`, icon: 'success' })
  }, 1000)
}

const goToMine = () => {
  uni.switchTab({ url: '/pages/mine/mine' })
}

const reserveBike = () => {
  if (!userStore.isLoggedIn) {
    uni.navigateTo({ url: '/pages/login/login' })
    return
  }
  uni.showModal({
    title: '预约确认',
    content: `确定要预约车辆 #${selectedBike.value.bikeNo} 吗？\n预约将保留15分钟`,
    success: (res) => {
      if (res.confirm) {
        uni.showToast({ title: '预约成功', icon: 'success' })
      }
    }
  })
}

const navigateToBike = () => {
  uni.showToast({ title: '开始导航', icon: 'success' })
}

const resetFilter = () => {
  filter.value = { type: 0, battery: 0 }
}

const applyFilter = () => {
  showFilter.value = false
  uni.showToast({ title: '筛选已应用', icon: 'success' })
}

onMounted(() => {
  if (userStore.isLoggedIn) {
    userStore.fetchUserInfo()
  }
})
</script>

<style lang="scss" scoped>
.index-container {
  height: 100vh;
  display: flex;
  flex-direction: column;
}

.map-container {
  flex: 3;
  position: relative;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  min-height: 60vh;
}

.map-placeholder {
  width: 100%;
  height: 100%;
  position: relative;
  background: url('data:image/svg+xml,<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 100 100"><rect fill="%23e8f5e9" width="100" height="100"/><path fill="%23c8e6c9" d="M0,50 Q25,30 50,50 T100,50 L100,100 L0,100 Z"/><path fill="%23a5d6a7" d="M0,70 Q30,60 50,70 T100,70 L100,100 L0,100 Z"/></svg>');
  background-size: cover;
}

.map-center-marker {
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  width: 20px;
  height: 20px;
  background: #00A862;
  border: 3px solid white;
  border-radius: 50%;
  box-shadow: 0 2px 10px rgba(0,0,0,0.3);
  z-index: 10;
}

.bike-marker {
  position: absolute;
  width: 44px;
  height: 44px;
  background: white;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  box-shadow: 0 2px 8px rgba(0,0,0,0.2);
  cursor: pointer;
  transition: transform 0.2s;

  &:active {
    transform: scale(1.1);
  }
}

.bike-icon {
  font-size: 22px;
}

.bike-1 { top: 35%; left: 30%; }
.bike-2 { top: 25%; left: 65%; }
.bike-3 { top: 55%; left: 75%; }
.bike-4 { top: 65%; left: 25%; }

.map-overlay {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  padding: 50px 16px 16px;
  display: flex;
  gap: 12px;
  align-items: center;
}

.search-bar {
  flex: 1;
  background: white;
  border-radius: 24px;
  padding: 12px 20px;
  display: flex;
  align-items: center;
  gap: 8px;
  box-shadow: 0 2px 8px rgba(0,0,0,0.1);
}

.search-icon {
  font-size: 16px;
}

.search-text {
  color: #999;
  font-size: 14px;
}

.filter-btn {
  background: white;
  padding: 12px 16px;
  border-radius: 24px;
  font-size: 14px;
  color: #333;
  box-shadow: 0 2px 8px rgba(0,0,0,0.1);
}

.bottom-panel {
  flex: 2;
  background: white;
  border-radius: 24px 24px 0 0;
  padding: 20px 16px;
  margin-top: -20px;
  position: relative;
  z-index: 20;
}

.quick-actions {
  display: flex;
  justify-content: space-around;
  margin-bottom: 20px;
}

.action-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 8px;
}

.action-icon {
  width: 56px;
  height: 56px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 24px;
}

.scan-btn .action-icon {
  background: linear-gradient(135deg, #00A862 0%, #00c874 100%);
}

.find-btn .action-icon {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
}

.mine-btn .action-icon {
  background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
}

.action-text {
  font-size: 12px;
  color: #666;
}

.bike-info {
  margin-bottom: 16px;
}

.bike-card {
  background: #f8f9fa;
  border-radius: 16px;
  padding: 16px;
}

.bike-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 12px;
}

.bike-type {
  font-size: 18px;
  font-weight: bold;
  color: #333;
}

.bike-no {
  font-size: 14px;
  color: #999;
}

.bike-details {
  display: flex;
  gap: 24px;
  margin-bottom: 16px;
}

.detail-item {
  display: flex;
  flex-direction: column;
  gap: 4px;
}

.detail-label {
  font-size: 12px;
  color: #999;
}

.detail-value {
  font-size: 16px;
  font-weight: bold;
  color: #333;
}

.bike-actions {
  display: flex;
  gap: 12px;
}

.reserve-btn {
  flex: 1;
  background: #00A862;
  color: white;
  border: none;
  border-radius: 12px;
  padding: 12px;
  font-size: 14px;
}

.nav-btn {
  flex: 1;
  background: white;
  color: #00A862;
  border: 1px solid #00A862;
  border-radius: 12px;
  padding: 12px;
  font-size: 14px;
}

.stats-bar {
  display: flex;
  justify-content: space-around;
  align-items: center;
  padding: 16px 0;
}

.stat-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 4px;
}

.stat-value {
  font-size: 24px;
  font-weight: bold;
  color: #00A862;
}

.stat-label {
  font-size: 12px;
  color: #999;
}

.stat-divider {
  width: 1px;
  height: 40px;
  background: #eee;
}

.filter-modal {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0,0,0,0.5);
  z-index: 100;
  display: flex;
  align-items: flex-end;
}

.filter-content {
  width: 100%;
  background: white;
  border-radius: 24px 24px 0 0;
  padding: 20px 16px;
  max-height: 70vh;
}

.filter-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.filter-title {
  font-size: 18px;
  font-weight: bold;
  color: #333;
}

.filter-close {
  font-size: 20px;
  color: #999;
  padding: 8px;
}

.filter-section {
  margin-bottom: 20px;
}

.section-title {
  font-size: 14px;
  color: #666;
  margin-bottom: 12px;
  display: block;
}

.filter-options {
  display: flex;
  flex-wrap: wrap;
  gap: 12px;
}

.option-item {
  padding: 8px 16px;
  background: #f5f5f5;
  border-radius: 20px;
  font-size: 14px;
  color: #666;

  &.active {
    background: #00A862;
    color: white;
  }
}

.filter-actions {
  display: flex;
  gap: 12px;
  margin-top: 24px;
}

.reset-btn {
  flex: 1;
  background: #f5f5f5;
  color: #666;
  border: none;
  border-radius: 12px;
  padding: 14px;
  font-size: 16px;
}

.confirm-btn {
  flex: 1;
  background: #00A862;
  color: white;
  border: none;
  border-radius: 12px;
  padding: 14px;
  font-size: 16px;
}
</style>
