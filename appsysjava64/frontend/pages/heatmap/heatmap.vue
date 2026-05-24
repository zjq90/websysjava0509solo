<template>
  <view class="heatmap-container">
    <view class="header">
      <view class="header-title">热力图分析</view>
      <view class="time-filter">
        <view 
          class="filter-item" 
          :class="{ active: timeFilter === 'today' }"
          @click="setTimeFilter('today')"
        >今日</view>
        <view 
          class="filter-item" 
          :class="{ active: timeFilter === 'week' }"
          @click="setTimeFilter('week')"
        >本周</view>
        <view 
          class="filter-item" 
          :class="{ active: timeFilter === 'month' }"
          @click="setTimeFilter('month')"
        >本月</view>
      </view>
    </view>
    
    <view class="map-wrapper">
      <view class="map-placeholder">
        <text class="map-icon">🔥</text>
        <text class="map-title">需求热力图</text>
        <text class="map-desc">显示高需求区域分布</text>
      </view>
      <view class="heat-legend">
        <view class="legend-item">
          <view class="legend-color low"></view>
          <text class="legend-text">低需求</text>
        </view>
        <view class="legend-item">
          <view class="legend-color medium"></view>
          <text class="legend-text">中需求</text>
        </view>
        <view class="legend-item">
          <view class="legend-color high"></view>
          <text class="legend-text">高需求</text>
        </view>
        <view class="legend-item">
          <view class="legend-color extreme"></view>
          <text class="legend-text">极高</text>
        </view>
      </view>
    </view>
    
    <view class="hot-areas">
      <view class="section-header">
        <text class="section-title">热门区域TOP5</text>
        <text class="section-desc">按需求热度排序</text>
      </view>
      <view class="area-list">
        <view 
          class="area-item" 
          v-for="(area, index) in hotAreas" 
          :key="area.id"
          @click="goToAreaDetail(area)"
        >
          <view class="area-rank" :class="'rank-' + (index + 1)">{{ index + 1 }}</view>
          <view class="area-info">
            <view class="area-name">{{ area.name }}</view>
            <view class="area-desc">{{ area.description }}</view>
          </view>
          <view class="area-heat">
            <view class="heat-bar">
              <view class="heat-fill" :style="{ width: area.heatLevel + '%' }"></view>
            </view>
            <text class="heat-value">{{ area.heatLevel }}%</text>
          </view>
        </view>
      </view>
    </view>
    
    <view class="dispatch-suggestions">
      <view class="section-header">
        <text class="section-title">调度建议</text>
      </view>
      <view class="suggestion-list">
        <view 
          class="suggestion-item" 
          v-for="suggestion in suggestions" 
          :key="suggestion.id"
        >
          <view class="suggestion-icon">💡</view>
          <view class="suggestion-content">
            <view class="suggestion-title">{{ suggestion.title }}</view>
            <view class="suggestion-desc">{{ suggestion.description }}</view>
          </view>
          <view class="suggestion-action" @click="createDispatchTask(suggestion)">
            生成任务
          </view>
        </view>
      </view>
    </view>
    
    <view class="stats-cards">
      <view class="stat-card">
        <text class="stat-icon">🚲</text>
        <text class="stat-value">{{ totalBikes }}</text>
        <text class="stat-label">总车辆数</text>
      </view>
      <view class="stat-card">
        <text class="stat-icon">📍</text>
        <text class="stat-value">{{ dispatchAreas }}</text>
        <text class="stat-label">调度区域</text>
      </view>
      <view class="stat-card">
        <text class="stat-icon">📈</text>
        <text class="stat-value">{{ demandGrowth }}%</text>
        <text class="stat-label">需求增长</text>
      </view>
    </view>
  </view>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { api } from '@/api'

const timeFilter = ref('today')
const hotAreas = ref([])
const suggestions = ref([])
const totalBikes = ref(520)
const dispatchAreas = ref(12)
const demandGrowth = ref(15)

const setTimeFilter = (filter) => {
  timeFilter.value = filter
  loadData()
}

const loadData = async () => {
  try {
    const heatPoints = await api.getHeatPoints()
    const dispatchSuggestions = await api.getDispatchSuggestions()
    
    hotAreas.value = (heatPoints || []).slice(0, 5).map((h, i) => ({
      ...h,
      heatLevel: h.intensity || Math.floor(Math.random() * 40) + 60
    }))
    
    suggestions.value = dispatchSuggestions || []
  } catch (e) {
    console.error(e)
    hotAreas.value = [
      { id: 1, name: '地铁站A出口', description: '早高峰需求集中区域', heatLevel: 95 },
      { id: 2, name: '商圈B入口', description: '周末人流密集区', heatLevel: 88 },
      { id: 3, name: '科技园C区', description: '上下班高峰需求大', heatLevel: 82 },
      { id: 4, name: '小区D门口', description: '早间出行需求高', heatLevel: 75 },
      { id: 5, name: '学校E东门', description: '学生出行集中', heatLevel: 68 }
    ]
    suggestions.value = [
      { id: 1, title: 'A区车辆过剩', description: '建议调度20辆车至B区', action: 'dispatch' },
      { id: 2, title: 'C区车辆不足', description: '需补充15辆电动车', action: 'dispatch' },
      { id: 3, title: 'D区电池告警', description: '8辆车电池低于20%', action: 'battery' }
    ]
  }
}

const goToAreaDetail = (area) => {
  uni.showToast({ title: `查看${area.name}详情`, icon: 'none' })
}

const createDispatchTask = (suggestion) => {
  uni.showModal({
    title: '生成调度任务',
    content: `确定要生成任务：${suggestion.title}？`,
    success: (res) => {
      if (res.confirm) {
        uni.showToast({ title: '任务已生成', icon: 'success' })
      }
    }
  })
}

onMounted(() => {
  loadData()
})
</script>

<style lang="scss" scoped>
.heatmap-container {
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
  
  .time-filter {
    display: flex;
    background: rgba(255, 255, 255, 0.2);
    border-radius: 40rpx;
    padding: 8rpx;
    
    .filter-item {
      flex: 1;
      text-align: center;
      padding: 16rpx 0;
      font-size: 26rpx;
      color: rgba(255, 255, 255, 0.8);
      border-radius: 32rpx;
      transition: all 0.3s;
      
      &.active {
        background: #fff;
        color: #667eea;
        font-weight: 500;
      }
    }
  }
}

.map-wrapper {
  height: 400rpx;
  margin: 20rpx;
  border-radius: 16rpx;
  background: #fff;
  position: relative;
  overflow: hidden;
  
  .map-placeholder {
    height: 100%;
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
    background: linear-gradient(180deg, #fff3e0 0%, #ffe0b2 100%);
    
    .map-icon {
      font-size: 80rpx;
      margin-bottom: 16rpx;
    }
    
    .map-title {
      font-size: 32rpx;
      font-weight: bold;
      color: #e65100;
      margin-bottom: 8rpx;
    }
    
    .map-desc {
      font-size: 24rpx;
      color: #999;
    }
  }
  
  .heat-legend {
    position: absolute;
    bottom: 20rpx;
    left: 20rpx;
    right: 20rpx;
    display: flex;
    justify-content: space-around;
    background: rgba(255, 255, 255, 0.9);
    border-radius: 12rpx;
    padding: 16rpx;
    
    .legend-item {
      display: flex;
      align-items: center;
      gap: 8rpx;
      
      .legend-color {
        width: 24rpx;
        height: 24rpx;
        border-radius: 4rpx;
        
        &.low { background: #90ee90; }
        &.medium { background: #ffd700; }
        &.high { background: #ff8c00; }
        &.extreme { background: #ff4500; }
      }
      
      .legend-text {
        font-size: 20rpx;
        color: #666;
      }
    }
  }
}

.hot-areas, .dispatch-suggestions {
  background: #fff;
  margin: 20rpx;
  border-radius: 16rpx;
  padding: 24rpx;
  
  .section-header {
    margin-bottom: 20rpx;
    
    .section-title {
      font-size: 30rpx;
      font-weight: bold;
      color: #333;
    }
    
    .section-desc {
      font-size: 24rpx;
      color: #999;
      margin-top: 8rpx;
    }
  }
}

.area-list {
  .area-item {
    display: flex;
    align-items: center;
    padding: 20rpx 0;
    border-bottom: 1rpx solid #f0f0f0;
    
    &:last-child {
      border-bottom: none;
    }
    
    .area-rank {
      width: 48rpx;
      height: 48rpx;
      border-radius: 12rpx;
      display: flex;
      align-items: center;
      justify-content: center;
      font-size: 24rpx;
      font-weight: bold;
      color: #fff;
      margin-right: 20rpx;
      
      &.rank-1 { background: linear-gradient(135deg, #ff6b6b, #ee5a5a); }
      &.rank-2 { background: linear-gradient(135deg, #ffa502, #ff7f00); }
      &.rank-3 { background: linear-gradient(135deg, #ffd93d, #f0c419); }
      &.rank-4, &.rank-5 { background: #909399; }
    }
    
    .area-info {
      flex: 1;
      
      .area-name {
        font-size: 28rpx;
        color: #333;
        margin-bottom: 8rpx;
      }
      
      .area-desc {
        font-size: 24rpx;
        color: #999;
      }
    }
    
    .area-heat {
      display: flex;
      flex-direction: column;
      align-items: flex-end;
      width: 160rpx;
      
      .heat-bar {
        width: 100%;
        height: 12rpx;
        background: #f0f0f0;
        border-radius: 6rpx;
        overflow: hidden;
        margin-bottom: 8rpx;
        
        .heat-fill {
          height: 100%;
          background: linear-gradient(90deg, #4caf50, #ff9800, #f44336);
          border-radius: 6rpx;
        }
      }
      
      .heat-value {
        font-size: 24rpx;
        color: #666;
      }
    }
  }
}

.suggestion-list {
  .suggestion-item {
    display: flex;
    align-items: center;
    padding: 20rpx 0;
    border-bottom: 1rpx solid #f0f0f0;
    
    &:last-child {
      border-bottom: none;
    }
    
    .suggestion-icon {
      font-size: 40rpx;
      margin-right: 20rpx;
    }
    
    .suggestion-content {
      flex: 1;
      
      .suggestion-title {
        font-size: 28rpx;
        color: #333;
        margin-bottom: 8rpx;
      }
      
      .suggestion-desc {
        font-size: 24rpx;
        color: #999;
      }
    }
    
    .suggestion-action {
      padding: 12rpx 24rpx;
      background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
      color: #fff;
      border-radius: 24rpx;
      font-size: 24rpx;
    }
  }
}

.stats-cards {
  display: flex;
  gap: 20rpx;
  padding: 0 20rpx 20rpx;
  
  .stat-card {
    flex: 1;
    background: #fff;
    border-radius: 16rpx;
    padding: 24rpx 16rpx;
    text-align: center;
    
    .stat-icon {
      font-size: 40rpx;
      display: block;
      margin-bottom: 12rpx;
    }
    
    .stat-value {
      display: block;
      font-size: 36rpx;
      font-weight: bold;
      color: #333;
    }
    
    .stat-label {
      display: block;
      font-size: 22rpx;
      color: #999;
      margin-top: 8rpx;
    }
  }
}
</style>
