<template>
  <view class="records-container">
    <view class="stats-header">
      <view class="stat-item">
        <text class="stat-value">{{ totalRides }}</text>
        <text class="stat-label">总次数</text>
      </view>
      <view class="stat-item">
        <text class="stat-value">{{ totalDistance }}km</text>
        <text class="stat-label">总里程</text>
      </view>
      <view class="stat-item">
        <text class="stat-value">¥{{ totalCost }}</text>
        <text class="stat-label">总花费</text>
      </view>
    </view>

    <view class="filter-tabs">
      <text 
        class="tab-item" 
        :class="{ active: activeTab === 'all' }"
        @click="activeTab = 'all'"
      >全部</text>
      <text 
        class="tab-item" 
        :class="{ active: activeTab === 'month' }"
        @click="activeTab = 'month'"
      >本月</text>
      <text 
        class="tab-item" 
        :class="{ active: activeTab === 'week' }"
        @click="activeTab = 'week'"
      >本周</text>
    </view>

    <view class="record-list">
      <view class="record-item" v-for="record in records" :key="record.id">
        <view class="record-header">
          <view class="bike-info">
            <text class="bike-type">{{ record.type }}</text>
            <text class="bike-no">{{ record.bikeNo }}</text>
          </view>
          <text class="record-cost">¥{{ record.cost }}</text>
        </view>
        <view class="record-detail">
          <view class="detail-item">
            <text class="detail-label">时长</text>
            <text class="detail-value">{{ record.duration }}</text>
          </view>
          <view class="detail-item">
            <text class="detail-label">里程</text>
            <text class="detail-value">{{ record.distance }}km</text>
          </view>
          <view class="detail-item">
            <text class="detail-label">时间</text>
            <text class="detail-value">{{ record.date }}</text>
          </view>
        </view>
        <view class="record-route">
          <text class="route-start">{{ record.startAddress }}</text>
          <text class="route-arrow">→</text>
          <text class="route-end">{{ record.endAddress }}</text>
        </view>
      </view>
    </view>

    <view class="empty-state" v-if="records.length === 0">
      <text class="empty-icon">📋</text>
      <text class="empty-text">暂无骑行记录</text>
    </view>
  </view>
</template>

<script setup>
import { ref } from 'vue'

const activeTab = ref('all')
const totalRides = ref(28)
const totalDistance = ref('126.5')
const totalCost = ref('89.50')

const records = ref([
  {
    id: 1,
    type: '电动车',
    bikeNo: 'E012',
    cost: '3.50',
    duration: '45分钟',
    distance: '3.2',
    date: '2024-01-15 18:30',
    startAddress: '科技园A座',
    endAddress: '地铁站B口'
  },
  {
    id: 2,
    type: '普通单车',
    bikeNo: 'B023',
    cost: '1.50',
    duration: '20分钟',
    distance: '1.8',
    date: '2024-01-15 08:45',
    startAddress: '小区东门',
    endAddress: '科技园B座'
  },
  {
    id: 3,
    type: '助力车',
    bikeNo: 'A008',
    cost: '4.00',
    duration: '50分钟',
    distance: '5.6',
    date: '2024-01-14 19:20',
    startAddress: '商场北门',
    endAddress: '小区南门'
  },
  {
    id: 4,
    type: '电动车',
    bikeNo: 'E015',
    cost: '2.50',
    duration: '30分钟',
    distance: '2.8',
    date: '2024-01-14 09:10',
    startAddress: '地铁站A口',
    endAddress: '公司楼下'
  }
])
</script>

<style lang="scss" scoped>
.records-container {
  min-height: 100vh;
  background: #f5f5f5;
}

.stats-header {
  background: linear-gradient(135deg, #00A862 0%, #00c874 100%);
  padding: 40px 20px 30px;
  display: flex;
  justify-content: space-around;
}

.stat-item {
  text-align: center;
}

.stat-value {
  display: block;
  font-size: 24px;
  font-weight: bold;
  color: white;
  margin-bottom: 4px;
}

.stat-label {
  font-size: 12px;
  color: rgba(255,255,255,0.8);
}

.filter-tabs {
  display: flex;
  background: white;
  padding: 12px 0;
  border-bottom: 1px solid #f0f0f0;
}

.tab-item {
  flex: 1;
  text-align: center;
  font-size: 14px;
  color: #666;
  padding: 8px 0;
  position: relative;

  &.active {
    color: #00A862;
    font-weight: bold;

    &::after {
      content: '';
      position: absolute;
      bottom: 0;
      left: 50%;
      transform: translateX(-50%);
      width: 40px;
      height: 2px;
      background: #00A862;
      border-radius: 1px;
    }
  }
}

.record-list {
  padding: 12px;
}

.record-item {
  background: white;
  border-radius: 12px;
  padding: 16px;
  margin-bottom: 12px;
}

.record-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 12px;
}

.bike-info {
  display: flex;
  align-items: center;
  gap: 8px;
}

.bike-type {
  font-size: 16px;
  font-weight: bold;
  color: #333;
}

.bike-no {
  font-size: 12px;
  color: #999;
}

.record-cost {
  font-size: 18px;
  font-weight: bold;
  color: #00A862;
}

.record-detail {
  display: flex;
  gap: 24px;
  margin-bottom: 12px;
  padding-bottom: 12px;
  border-bottom: 1px dashed #f0f0f0;
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
  font-size: 14px;
  color: #333;
}

.record-route {
  display: flex;
  align-items: center;
  gap: 8px;
}

.route-start, .route-end {
  flex: 1;
  font-size: 13px;
  color: #666;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.route-start {
  text-align: left;
}

.route-end {
  text-align: right;
}

.route-arrow {
  font-size: 14px;
  color: #00A862;
}

.empty-state {
  padding: 80px 20px;
  text-align: center;
}

.empty-icon {
  display: block;
  font-size: 64px;
  margin-bottom: 16px;
}

.empty-text {
  font-size: 14px;
  color: #999;
}
</style>
