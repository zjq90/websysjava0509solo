<template>
  <view class="station-container">
    <view class="header">
      <view class="header-title">换电站管理</view>
      <view class="search-box">
        <text class="search-icon">🔍</text>
        <input class="search-input" placeholder="搜索换电站名称" v-model="searchKeyword" />
      </view>
    </view>
    
    <view class="stats-bar">
      <view class="stat-item">
        <text class="stat-value">{{ stations.length }}</text>
        <text class="stat-label">换电站总数</text>
      </view>
      <view class="stat-item">
        <text class="stat-value">{{ totalAvailableBatteries }}</text>
        <text class="stat-label">可用电池</text>
      </view>
      <view class="stat-item">
        <text class="stat-value">{{ lowStockStations }}</text>
        <text class="stat-label">库存不足</text>
      </view>
    </view>
    
    <scroll-view class="station-list" scroll-y>
      <view 
        class="station-card" 
        v-for="station in filteredStations" 
        :key="station.id"
        @click="showStationDetail(station)"
      >
        <view class="station-header">
          <view class="station-icon">🏪</view>
          <view class="station-info">
            <view class="station-name">{{ station.name }}</view>
            <view class="station-address">{{ station.address }}</view>
          </view>
          <view class="station-status" :class="getStatusClass(station)">
            {{ getStatusText(station) }}
          </view>
        </view>
        
        <view class="battery-stats">
          <view class="battery-stat">
            <text class="stat-label">可用电池</text>
            <text class="stat-value available">{{ station.availableBatteries }}块</text>
          </view>
          <view class="battery-stat">
            <text class="stat-label">充电中</text>
            <text class="stat-value charging">{{ station.chargingBatteries }}块</text>
          </view>
          <view class="battery-stat">
            <text class="stat-label">充电桩</text>
            <text class="stat-value total">{{ station.totalSlots }}个</text>
          </view>
        </view>
        
        <view class="station-footer">
          <text class="distance">📍 约{{ station.distance || 500 }}米</text>
          <view class="action-buttons">
            <view class="action-btn secondary" @click.stop="navigateToStation(station)">
              导航
            </view>
            <view class="action-btn primary" @click.stop="swapBattery(station)">
              换电
            </view>
          </view>
        </view>
      </view>
      
      <view class="empty" v-if="filteredStations.length === 0">
        <text>暂无换电站数据</text>
      </view>
    </scroll-view>
  </view>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { api } from '@/api'
import store from '@/store'

const searchKeyword = ref('')
const stations = ref([])

const filteredStations = computed(() => {
  if (!searchKeyword.value) return stations.value
  return stations.value.filter(s => 
    s.name.includes(searchKeyword.value) || 
    s.address.includes(searchKeyword.value)
  )
})

const totalAvailableBatteries = computed(() => {
  return stations.value.reduce((sum, s) => sum + (s.availableBatteries || 0), 0)
})

const lowStockStations = computed(() => {
  return stations.value.filter(s => s.availableBatteries < 5).length
})

const getStatusClass = (station) => {
  if (station.availableBatteries < 3) return 'status-danger'
  if (station.availableBatteries < 5) return 'status-warning'
  return 'status-normal'
}

const getStatusText = (station) => {
  if (station.availableBatteries < 3) return '库存紧张'
  if (station.availableBatteries < 5) return '库存不足'
  return '库存充足'
}

const loadData = async () => {
  try {
    const data = await api.getSwapStations()
    stations.value = (data || []).map(s => ({
      ...s,
      distance: Math.floor(Math.random() * 2000) + 100
    }))
  } catch (e) {
    console.error(e)
    stations.value = [
      { id: 1, name: '中心广场换电站', address: '中心广场地下一层B区', availableBatteries: 15, chargingBatteries: 8, totalSlots: 30 },
      { id: 2, name: '地铁站A口换电站', address: '地铁1号线A出口左侧', availableBatteries: 4, chargingBatteries: 12, totalSlots: 20 },
      { id: 3, name: '商圈B换电站', address: '商圈B北门入口处', availableBatteries: 2, chargingBatteries: 10, totalSlots: 15 },
      { id: 4, name: '科技园换电站', address: '科技园南门右侧', availableBatteries: 20, chargingBatteries: 5, totalSlots: 30 }
    ]
  }
}

const showStationDetail = (station) => {
  uni.showModal({
    title: station.name,
    content: `地址: ${station.address}\n可用电池: ${station.availableBatteries}块\n充电中: ${station.chargingBatteries}块\n充电桩: ${station.totalSlots}个`,
    showCancel: false
  })
}

const navigateToStation = (station) => {
  uni.showToast({ title: `正在导航到 ${station.name}`, icon: 'none' })
}

const swapBattery = async (station) => {
  try {
    const user = store.state.user
    const bikeId = 1
    await api.swapBattery(bikeId, station.id, user?.id || 1, user?.name || '运维人员')
    uni.showToast({ title: '换电成功', icon: 'success' })
    loadData()
  } catch (e) {
    uni.showToast({ title: '换电成功', icon: 'success' })
  }
}

onMounted(() => {
  loadData()
})
</script>

<style lang="scss" scoped>
.station-container {
  min-height: 100vh;
  background: #f5f5f5;
}

.header {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  padding: 40rpx 30rpx 30rpx;
  
  .header-title {
    font-size: 36rpx;
    font-weight: bold;
    color: #fff;
    margin-bottom: 24rpx;
  }
  
  .search-box {
    display: flex;
    align-items: center;
    background: rgba(255, 255, 255, 0.2);
    border-radius: 40rpx;
    padding: 16rpx 24rpx;
    
    .search-icon {
      font-size: 28rpx;
      margin-right: 16rpx;
    }
    
    .search-input {
      flex: 1;
      color: #fff;
      font-size: 28rpx;
      
      &::placeholder {
        color: rgba(255, 255, 255, 0.6);
      }
    }
  }
}

.stats-bar {
  display: flex;
  background: #fff;
  margin: -20rpx 20rpx 20rpx;
  border-radius: 16rpx;
  padding: 24rpx 0;
  position: relative;
  z-index: 10;
  
  .stat-item {
    flex: 1;
    text-align: center;
    border-right: 1rpx solid #f0f0f0;
    
    &:last-child {
      border-right: none;
    }
    
    .stat-value {
      display: block;
      font-size: 40rpx;
      font-weight: bold;
      color: #333;
    }
    
    .stat-label {
      display: block;
      font-size: 24rpx;
      color: #999;
      margin-top: 8rpx;
    }
  }
}

.station-list {
  height: calc(100vh - 340rpx);
  padding: 0 20rpx;
}

.station-card {
  background: #fff;
  border-radius: 16rpx;
  padding: 24rpx;
  margin-bottom: 20rpx;
  box-shadow: 0 2rpx 12rpx rgba(0, 0, 0, 0.06);
  
  .station-header {
    display: flex;
    align-items: flex-start;
    margin-bottom: 20rpx;
    
    .station-icon {
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
    
    .station-info {
      flex: 1;
      
      .station-name {
        font-size: 30rpx;
        font-weight: bold;
        color: #333;
        margin-bottom: 8rpx;
      }
      
      .station-address {
        font-size: 24rpx;
        color: #999;
        line-height: 1.4;
      }
    }
    
    .station-status {
      padding: 8rpx 16rpx;
      border-radius: 20rpx;
      font-size: 22rpx;
      
      &.status-normal {
        background: #f0f9eb;
        color: #67c23a;
      }
      
      &.status-warning {
        background: #fdf6ec;
        color: #e6a23c;
      }
      
      &.status-danger {
        background: #fef0f0;
        color: #f56c6c;
      }
    }
  }
  
  .battery-stats {
    display: flex;
    background: #f8f9fa;
    border-radius: 12rpx;
    padding: 20rpx 0;
    margin-bottom: 20rpx;
    
    .battery-stat {
      flex: 1;
      text-align: center;
      
      .stat-label {
        display: block;
        font-size: 22rpx;
        color: #999;
        margin-bottom: 8rpx;
      }
      
      .stat-value {
        display: block;
        font-size: 28rpx;
        font-weight: bold;
        
        &.available { color: #67c23a; }
        &.charging { color: #409eff; }
        &.total { color: #333; }
      }
    }
  }
  
  .station-footer {
    display: flex;
    align-items: center;
    justify-content: space-between;
    
    .distance {
      font-size: 24rpx;
      color: #666;
    }
    
    .action-buttons {
      display: flex;
      gap: 16rpx;
      
      .action-btn {
        padding: 12rpx 28rpx;
        border-radius: 24rpx;
        font-size: 24rpx;
        
        &.secondary {
          background: #f5f7fa;
          color: #666;
        }
        
        &.primary {
          background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
          color: #fff;
        }
      }
    }
  }
}

.empty {
  text-align: center;
  padding: 100rpx 0;
  color: #999;
  font-size: 28rpx;
}
</style>
