<template>
  <div class="map-container">
    <div class="map-wrapper">
      <div class="map-placeholder">
        <span class="map-icon">🗺️</span>
        <span class="map-title">地图导航</span>
        <span class="map-desc">显示附近车辆和换电站位置</span>
      </div>
    </div>
    
    <div class="quick-buttons">
      <div class="quick-btn" @click="goToNearestBike">
        <span class="btn-icon">🚲</span>
        <span class="btn-text">最近车辆</span>
      </div>
      <div class="quick-btn" @click="goToNearestStation">
        <span class="btn-icon">🔋</span>
        <span class="btn-text">最近换电站</span>
      </div>
      <div class="quick-btn" @click="goToHeatMap">
        <span class="btn-icon">🔥</span>
        <span class="btn-text">热力图</span>
      </div>
      <div class="quick-btn" @click="navigateToTask">
        <span class="btn-icon">📍</span>
        <span class="btn-text">任务导航</span>
      </div>
    </div>
    
    <div class="nearby-list">
      <div class="section-header">
        <span class="section-title">附近车辆</span>
        <span class="section-more">查看更多 ›</span>
      </div>
      <div 
        class="bike-item" 
        v-for="bike in nearbyBikes" 
        :key="bike.id"
        @click="goToBikeDetail(bike)"
      >
        <div class="bike-icon">🚲</div>
        <div class="bike-info">
          <div class="bike-no">{{ bike.bikeNo }}</div>
          <div class="bike-distance">{{ bike.distance }}米 · {{ bike.batteryLevel }}%电量</div>
        </div>
        <el-button type="primary" size="small" @click.stop="navigateToBike(bike)">导航</el-button>
      </div>
    </div>
    
    <div class="nearby-list">
      <div class="section-header">
        <span class="section-title">附近换电站</span>
        <span class="section-more" @click="goToStations">查看更多 ›</span>
      </div>
      <div 
        class="station-item" 
        v-for="station in nearbyStations" 
        :key="station.id"
        @click="goToStationDetail(station)"
      >
        <div class="station-icon">🏪</div>
        <div class="station-info">
          <div class="station-name">{{ station.name }}</div>
          <div class="station-distance">{{ station.distance }}米 · 可用电池{{ station.availableBatteries }}块</div>
        </div>
        <el-button type="primary" size="small" @click.stop="navigateToStation(station)">导航</el-button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { api } from '../api'

const router = useRouter()
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
    nearbyBikes.value = [
      { id: 1, bikeNo: 'B001', distance: 150, batteryLevel: 85 },
      { id: 2, bikeNo: 'B023', distance: 230, batteryLevel: 62 },
      { id: 3, bikeNo: 'B045', distance: 380, batteryLevel: 45 }
    ]
    nearbyStations.value = [
      { id: 1, name: '中心广场换电站', distance: 500, availableBatteries: 15 },
      { id: 2, name: '地铁站A口换电站', distance: 800, availableBatteries: 8 }
    ]
  }
}

const goToNearestBike = () => {
  ElMessage.success('正在导航到最近车辆')
}

const goToNearestStation = () => {
  ElMessage.success('正在导航到最近换电站')
}

const goToHeatMap = () => {
  router.push('/heatmap')
}

const navigateToTask = () => {
  router.push('/tasks')
}

const goToBikeDetail = (bike) => {
  router.push(`/bike-detail/${bike.id}`)
}

const navigateToBike = (bike) => {
  ElMessage.success(`正在导航到车辆 ${bike.bikeNo}`)
}

const goToStationDetail = (station) => {
  router.push('/station')
}

const goToStations = () => {
  router.push('/station')
}

const navigateToStation = (station) => {
  ElMessage.success(`正在导航到 ${station.name}`)
}

onMounted(() => {
  loadNearbyData()
})
</script>

<style scoped>
.map-container {
  min-height: 100vh;
  background: #f5f5f5;
  padding-bottom: 20px;
}

.map-wrapper {
  height: 200px;
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
      font-size: 48px;
      margin-bottom: 10px;
    }
    
    .map-title {
      font-size: 18px;
      font-weight: bold;
      color: #1976d2;
      margin-bottom: 5px;
    }
    
    .map-desc {
      font-size: 12px;
      color: #666;
    }
  }
}

.quick-buttons {
  display: flex;
  justify-content: space-around;
  padding: 15px;
  background: #fff;
  margin: 10px;
  border-radius: 12px;
  
  .quick-btn {
    display: flex;
    flex-direction: column;
    align-items: center;
    cursor: pointer;
    
    .btn-icon {
      width: 50px;
      height: 50px;
      border-radius: 50%;
      background: #f5f7fa;
      display: flex;
      align-items: center;
      justify-content: center;
      font-size: 22px;
      margin-bottom: 8px;
    }
    
    .btn-text {
      font-size: 12px;
      color: #333;
    }
  }
}

.nearby-list {
  background: #fff;
  margin: 0 10px 10px;
  border-radius: 12px;
  padding: 15px;
  
  .section-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 15px;
    
    .section-title {
      font-size: 16px;
      font-weight: bold;
      color: #333;
    }
    
    .section-more {
      font-size: 12px;
      color: #667eea;
      cursor: pointer;
    }
  }
  
  .bike-item, .station-item {
    display: flex;
    align-items: center;
    padding: 12px 0;
    border-bottom: 1px solid #f0f0f0;
    
    &:last-child {
      border-bottom: none;
    }
    
    .bike-icon, .station-icon {
      width: 40px;
      height: 40px;
      border-radius: 8px;
      background: #e8f4ff;
      display: flex;
      align-items: center;
      justify-content: center;
      font-size: 18px;
      margin-right: 12px;
    }
    
    .bike-info, .station-info {
      flex: 1;
      
      .bike-no, .station-name {
        font-size: 14px;
        color: #333;
        margin-bottom: 5px;
      }
      
      .bike-distance, .station-distance {
        font-size: 12px;
        color: #999;
      }
    }
  }
}
</style>
