<template>
  <view class="map-container">
    <view class="map-wrapper">
      <view class="map-placeholder">
        <text class="map-icon">🗺️</text>
        <text class="map-title">地图导航</text>
        <text class="map-desc">显示附近车辆和换电站位置</text>
      </view>
    </view>
    
    <view class="quick-buttons">
      <view class="quick-btn" @click="goToNearestBike">
        <text class="btn-icon">🚲</text>
        <text class="btn-text">最近车辆</text>
      </view>
      <view class="quick-btn" @click="goToNearestStation">
        <text class="btn-icon">🔋</text>
        <text class="btn-text">最近换电站</text>
      </view>
      <view class="quick-btn" @click="goToHeatMap">
        <text class="btn-icon">🔥</text>
        <text class="btn-text">热力图</text>
      </view>
      <view class="quick-btn" @click="navigateToTask">
        <text class="btn-icon">📍</text>
        <text class="btn-text">任务导航</text>
      </view>
    </view>
    
    <view class="nearby-list">
      <view class="section-header">
        <text class="section-title">附近车辆</text>
        <text class="section-more">查看更多 ›</text>
      </view>
      <view class="bike-item" v-for="bike in nearbyBikes" :key="bike.id" @click="goToBikeDetail(bike)">
        <view class="bike-icon">🚲</view>
        <view class="bike-info">
          <view class="bike-no">{{ bike.bikeNo }}</view>
          <view class="bike-distance">{{ bike.distance }}米 · {{ bike.batteryLevel }}%电量</view>
        </view>
        <view class="bike-nav-btn" @click.stop="navigateToBike(bike)">导航</view>
      </view>
    </view>
    
    <view class="nearby-list">
      <view class="section-header">
        <text class="section-title">附近换电站</text>
        <text class="section-more">查看更多 ›</text>
      </view>
      <view class="station-item" v-for="station in nearbyStations" :key="station.id" @click="goToStationDetail(station)">
        <view class="station-icon">🔋</view>
        <view class="station-info">
          <view class="station-name">{{ station.name }}</view>
          <view class="station-distance">{{ station.distance }}米 · 可用电池{{ station.availableBatteries }}块</view>
        </view>
        <view class="station-nav-btn" @click.stop="navigateToStation(station)">导航</view>
      </view>
    </view>
  </view>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { api } from '@/api'

const nearbyBikes = ref([])
const nearbyStations = ref([])

const loadNearbyData = async () => {
  try {
    const bikes = await api.getBikes()
    const stations = await api.getSwapStations()
    
    nearbyBikes.value = (bikes || []).slice(0, 5).map(b => ({
      ...b,
      distance: Math.floor(Math.random() * 500) + 100
    }))
    
    nearbyStations.value = (stations || []).slice(0, 3).map(s => ({
      ...s,
      distance: Math.floor(Math.random() * 1000) + 200
    }))
  } catch (e) {
    console.error(e)
  }
}

const goToNearestBike = () => {
  uni.showToast({ title: '正在导航到最近车辆', icon: 'none' })
}

const goToNearestStation = () => {
  uni.showToast({ title: '正在导航到最近换电站', icon: 'none' })
}

const goToHeatMap = () => {
  uni.navigateTo({ url: '/pages/heatmap/heatmap' })
}

const navigateToTask = () => {
  uni.navigateTo({ url: '/pages/tasks/tasks' })
}

const goToBikeDetail = (bike) => {
  uni.navigateTo({ url: `/pages/bike-detail/bike-detail?id=${bike.id}` })
}

const navigateToBike = (bike) => {
  uni.showToast({ title: `正在导航到车辆 ${bike.bikeNo}`, icon: 'none' })
}

const goToStationDetail = (station) => {
  uni.navigateTo({ url: `/pages/station/station?id=${station.id}` })
}

const navigateToStation = (station) => {
  uni.showToast({ title: `正在导航到 ${station.name}`, icon: 'none' })
}

onMounted(() => {
  loadNearbyData()
})
</script>

<style lang="scss" scoped>
.map-container {
  min-height: 100vh;
  background: #f5f5f5;
}

.map-wrapper {
  height: 400rpx;
  background: linear-gradient(180deg, #e3f2fd 0%, #bbdefb 100%);
  position: relative;
  overflow: hidden;
  
  .map-placeholder {
    height: 100%;
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
    
    .map-icon {
      font-size: 80rpx;
      margin-bottom: 16rpx;
    }
    
    .map-title {
      font-size: 32rpx;
      font-weight: bold;
      color: #1976d2;
      margin-bottom: 8rpx;
    }
    
    .map-desc {
      font-size: 24rpx;
      color: #666;
    }
  }
}

.quick-buttons {
  display: flex;
  justify-content: space-around;
  padding: 24rpx;
  background: #fff;
  margin: 20rpx;
  border-radius: 16rpx;
  
  .quick-btn {
    display: flex;
    flex-direction: column;
    align-items: center;
    
    .btn-icon {
      width: 80rpx;
      height: 80rpx;
      border-radius: 50%;
      background: #f5f7fa;
      display: flex;
      align-items: center;
      justify-content: center;
      font-size: 36rpx;
      margin-bottom: 12rpx;
    }
    
    .btn-text {
      font-size: 24rpx;
      color: #333;
    }
  }
}

.nearby-list {
  background: #fff;
  margin: 0 20rpx 20rpx;
  border-radius: 16rpx;
  padding: 24rpx;
  
  .section-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 20rpx;
    
    .section-title {
      font-size: 30rpx;
      font-weight: bold;
      color: #333;
    }
    
    .section-more {
      font-size: 24rpx;
      color: #999;
    }
  }
  
  .bike-item, .station-item {
    display: flex;
    align-items: center;
    padding: 20rpx 0;
    border-bottom: 1rpx solid #f0f0f0;
    
    &:last-child {
      border-bottom: none;
    }
    
    .bike-icon, .station-icon {
      width: 60rpx;
      height: 60rpx;
      border-radius: 12rpx;
      background: #e8f4ff;
      display: flex;
      align-items: center;
      justify-content: center;
      font-size: 28rpx;
      margin-right: 20rpx;
    }
    
    .bike-info, .station-info {
      flex: 1;
      
      .bike-no, .station-name {
        font-size: 28rpx;
        color: #333;
        margin-bottom: 8rpx;
      }
      
      .bike-distance, .station-distance {
        font-size: 24rpx;
        color: #999;
      }
    }
    
    .bike-nav-btn, .station-nav-btn {
      padding: 12rpx 24rpx;
      background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
      color: #fff;
      border-radius: 20rpx;
      font-size: 24rpx;
    }
  }
}
</style>
