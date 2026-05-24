<template>
  <view class="battery-container">
    <view class="header">
      <view class="header-title">电池管理</view>
      <view class="stats-row">
        <view class="stat-card warning">
          <text class="stat-icon">⚠️</text>
          <text class="stat-value">{{ warningCount }}</text>
          <text class="stat-label">低电量预警</text>
        </view>
        <view class="stat-card normal">
          <text class="stat-icon">🔋</text>
          <text class="stat-value">{{ totalBatteries }}</text>
          <text class="stat-label">电池总数</text>
        </view>
        <view class="stat-card success">
          <text class="stat-icon">✅</text>
          <text class="stat-value">{{ healthyCount }}</text>
          <text class="stat-label">健康电池</text>
        </view>
      </view>
    </view>
    
    <view class="section">
      <view class="section-header">
        <text class="section-title">低电量预警车辆</text>
        <text class="section-more" @click="showAllWarnings">查看全部</text>
      </view>
      <view class="warning-list">
        <view 
          class="warning-item" 
          v-for="item in lowBatteryList" 
          :key="item.id"
          @click="goToBikeDetail(item)"
        >
          <view class="bike-icon">🚲</view>
          <view class="warning-info">
            <view class="bike-no">{{ item.bikeNo }}</view>
            <view class="bike-location">{{ item.location }}</view>
          </view>
          <view class="battery-level low">
            <text class="level-icon">🔋</text>
            <text class="level-value">{{ item.batteryLevel }}%</text>
          </view>
        </view>
      </view>
    </view>
    
    <view class="section">
      <view class="section-header">
        <text class="section-title">电池健康度统计</text>
      </view>
      <view class="health-stats">
        <view class="health-item">
          <view class="health-bar">
            <view class="health-fill excellent" :style="{ width: '65%' }"></view>
          </view>
          <view class="health-label">优秀 (≥80%)</view>
          <view class="health-count">65块</view>
        </view>
        <view class="health-item">
          <view class="health-bar">
            <view class="health-fill good" :style="{ width: '25%' }"></view>
          </view>
          <view class="health-label">良好 (60-79%)</view>
          <view class="health-count">25块</view>
        </view>
        <view class="health-item">
          <view class="health-bar">
            <view class="health-fill average" :style="{ width: '8%' }"></view>
          </view>
          <view class="health-label">一般 (40-59%)</view>
          <view class="health-count">8块</view>
        </view>
        <view class="health-item">
          <view class="health-bar">
            <view class="health-fill poor" :style="{ width: '2%' }"></view>
          </view>
          <view class="health-label">较差 (<40%)</view>
          <view class="health-count">2块</view>
        </view>
      </view>
    </view>
    
    <view class="section">
      <view class="section-header">
        <text class="section-title">充电次数分布</text>
      </view>
      <view class="charge-distribution">
        <view class="dist-item">
          <view class="dist-bar">
            <view class="dist-fill" :style="{ height: '80%' }"></view>
          </view>
          <view class="dist-label">0-100</view>
        </view>
        <view class="dist-item">
          <view class="dist-bar">
            <view class="dist-fill" :style="{ height: '60%' }"></view>
          </view>
          <view class="dist-label">101-200</view>
        </view>
        <view class="dist-item">
          <view class="dist-bar">
            <view class="dist-fill" :style="{ height: '45%' }"></view>
          </view>
          <view class="dist-label">201-300</view>
        </view>
        <view class="dist-item">
          <view class="dist-bar">
            <view class="dist-fill" :style="{ height: '30%' }"></view>
          </view>
          <view class="dist-label">301-400</view>
        </view>
        <view class="dist-item">
          <view class="dist-bar">
            <view class="dist-fill" :style="{ height: '15%' }"></view>
          </view>
          <view class="dist-label">400+</view>
        </view>
      </view>
    </view>
    
    <view class="quick-actions">
      <view class="action-btn" @click="goToStations">
        <text class="action-icon">🏪</text>
        <text class="action-text">换电站管理</text>
      </view>
      <view class="action-btn" @click="goToBatteryDetail">
        <text class="action-icon">📊</text>
        <text class="action-text">电池详情</text>
      </view>
    </view>
  </view>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { api } from '@/api'

const batteries = ref([])
const lowBatteryList = ref([])

const warningCount = computed(() => lowBatteryList.value.length)
const totalBatteries = computed(() => batteries.value.length)
const healthyCount = computed(() => batteries.value.filter(b => b.healthLevel >= 80).length)

const loadData = async () => {
  try {
    const [batList, warnings] = await Promise.all([
      api.getBatteries(),
      api.getLowBatteryWarning()
    ])
    batteries.value = batList || []
    lowBatteryList.value = (warnings || []).slice(0, 5)
  } catch (e) {
    console.error(e)
    lowBatteryList.value = [
      { id: 1, bikeNo: 'B001', location: '地铁站A出口', batteryLevel: 15 },
      { id: 2, bikeNo: 'B023', location: '商圈B入口', batteryLevel: 18 },
      { id: 3, bikeNo: 'B045', location: '小区C门口', batteryLevel: 12 }
    ]
  }
}

const goToBikeDetail = (item) => {
  uni.navigateTo({ url: `/pages/bike-detail/bike-detail?id=${item.bikeId || item.id}` })
}

const showAllWarnings = () => {
  uni.showToast({ title: '查看全部低电量车辆', icon: 'none' })
}

const goToStations = () => {
  uni.navigateTo({ url: '/pages/station/station' })
}

const goToBatteryDetail = () => {
  uni.showToast({ title: '电池详情页面', icon: 'none' })
}

onMounted(() => {
  loadData()
})
</script>

<style lang="scss" scoped>
.battery-container {
  min-height: 100vh;
  background: #f5f5f5;
  padding-bottom: 120rpx;
}

.header {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  padding: 40rpx 30rpx 30rpx;
  
  .header-title {
    font-size: 36rpx;
    font-weight: bold;
    color: #fff;
    margin-bottom: 30rpx;
  }
  
  .stats-row {
    display: flex;
    gap: 20rpx;
    
    .stat-card {
      flex: 1;
      background: rgba(255, 255, 255, 0.15);
      border-radius: 16rpx;
      padding: 24rpx 16rpx;
      text-align: center;
      
      .stat-icon {
        font-size: 40rpx;
        display: block;
        margin-bottom: 8rpx;
      }
      
      .stat-value {
        display: block;
        font-size: 40rpx;
        font-weight: bold;
        color: #fff;
      }
      
      .stat-label {
        display: block;
        font-size: 22rpx;
        color: rgba(255, 255, 255, 0.8);
        margin-top: 8rpx;
      }
    }
  }
}

.section {
  background: #fff;
  margin: 20rpx;
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
      color: #667eea;
    }
  }
}

.warning-list {
  .warning-item {
    display: flex;
    align-items: center;
    padding: 20rpx 0;
    border-bottom: 1rpx solid #f0f0f0;
    
    &:last-child {
      border-bottom: none;
    }
    
    .bike-icon {
      width: 60rpx;
      height: 60rpx;
      border-radius: 12rpx;
      background: #fff3e0;
      display: flex;
      align-items: center;
      justify-content: center;
      font-size: 28rpx;
      margin-right: 20rpx;
    }
    
    .warning-info {
      flex: 1;
      
      .bike-no {
        font-size: 28rpx;
        color: #333;
        margin-bottom: 8rpx;
      }
      
      .bike-location {
        font-size: 24rpx;
        color: #999;
      }
    }
    
    .battery-level {
      display: flex;
      align-items: center;
      gap: 8rpx;
      padding: 8rpx 16rpx;
      border-radius: 20rpx;
      
      &.low {
        background: #fff0f0;
        
        .level-value {
          color: #e64340;
          font-weight: bold;
        }
      }
      
      .level-icon {
        font-size: 24rpx;
      }
      
      .level-value {
        font-size: 26rpx;
      }
    }
  }
}

.health-stats {
  .health-item {
    display: flex;
    align-items: center;
    padding: 16rpx 0;
    
    .health-bar {
      width: 200rpx;
      height: 16rpx;
      background: #f0f0f0;
      border-radius: 8rpx;
      overflow: hidden;
      margin-right: 20rpx;
      
      .health-fill {
        height: 100%;
        border-radius: 8rpx;
        
        &.excellent { background: #67c23a; }
        &.good { background: #409eff; }
        &.average { background: #e6a23c; }
        &.poor { background: #f56c6c; }
      }
    }
    
    .health-label {
      flex: 1;
      font-size: 26rpx;
      color: #666;
    }
    
    .health-count {
      font-size: 26rpx;
      color: #333;
      font-weight: 500;
    }
  }
}

.charge-distribution {
  display: flex;
  justify-content: space-around;
  align-items: flex-end;
  height: 200rpx;
  padding: 20rpx 0;
  
  .dist-item {
    display: flex;
    flex-direction: column;
    align-items: center;
    
    .dist-bar {
      width: 50rpx;
      height: 150rpx;
      background: #f0f0f0;
      border-radius: 8rpx;
      position: relative;
      overflow: hidden;
      margin-bottom: 12rpx;
      
      .dist-fill {
        position: absolute;
        bottom: 0;
        left: 0;
        right: 0;
        background: linear-gradient(180deg, #667eea 0%, #764ba2 100%);
        border-radius: 8rpx;
      }
    }
    
    .dist-label {
      font-size: 20rpx;
      color: #999;
    }
  }
}

.quick-actions {
  display: flex;
  gap: 20rpx;
  padding: 0 20rpx;
  
  .action-btn {
    flex: 1;
    background: #fff;
    border-radius: 16rpx;
    padding: 30rpx 20rpx;
    display: flex;
    flex-direction: column;
    align-items: center;
    
    .action-icon {
      font-size: 48rpx;
      margin-bottom: 12rpx;
    }
    
    .action-text {
      font-size: 26rpx;
      color: #333;
    }
  }
}
</style>
