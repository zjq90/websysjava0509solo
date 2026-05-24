<template>
  <view class="detail-container">
    <view class="bike-header card">
      <view class="bike-icon">🚲</view>
      <view class="bike-info">
        <text class="bike-no">{{ bike?.bikeNo }}</text>
        <view class="bike-status" :class="'status-' + bike?.status?.toLowerCase()">
          {{ getStatusText(bike?.status) }}
        </view>
      </view>
    </view>
    
    <view class="card" v-if="bike">
      <view class="section-title">基本信息</view>
      
      <view class="info-row">
        <text class="label">车辆类型</text>
        <text class="value">{{ bike.type === 'ELECTRIC' ? '电动单车' : '普通单车' }}</text>
      </view>
      
      <view class="info-row" v-if="bike.isElectric">
        <text class="label">电池电量</text>
        <view class="battery-bar">
          <view class="battery-fill" :style="{ width: bike.batteryLevel + '%' }"></view>
          <text class="battery-text">{{ bike.batteryLevel }}%</text>
        </view>
      </view>
      
      <view class="info-row">
        <text class="label">当前位置</text>
        <text class="value">{{ bike.location || '未知' }}</text>
      </view>
      
      <view class="info-row" v-if="bike.faultType">
        <text class="label">故障类型</text>
        <text class="value fault">{{ bike.faultType }}</text>
      </view>
      
      <view class="info-row" v-if="bike.faultDescription">
        <text class="label">故障描述</text>
        <text class="value">{{ bike.faultDescription }}</text>
      </view>
      
      <view class="info-row">
        <text class="label">总骑行次数</text>
        <text class="value">{{ bike.totalRideCount }} 次</text>
      </view>
      
      <view class="info-row">
        <text class="label">总里程</text>
        <text class="value">{{ bike.totalMileage }} km</text>
      </view>
      
      <view class="info-row">
        <text class="label">最后维护时间</text>
        <text class="value">{{ formatTime(bike.lastMaintenanceTime) || '暂无' }}</text>
      </view>
    </view>
    
    <view class="card" v-if="faultRecords && faultRecords.length > 0">
      <view class="section-title">故障记录</view>
      <view class="record-item" v-for="(record, index) in faultRecords" :key="index">
        <view class="record-header">
          <text class="fault-type">{{ record.faultType }}</text>
          <text class="record-status" :class="'status-' + record.status.toLowerCase()">{{ record.status }}</text>
        </view>
        <view class="record-desc">{{ record.faultDescription }}</view>
        <view class="record-time">{{ formatTime(record.reportTime) }}</view>
      </view>
    </view>
    
    <view class="card" v-if="repairOrders && repairOrders.length > 0">
      <view class="section-title">维修记录</view>
      <view class="record-item" v-for="(order, index) in repairOrders" :key="index">
        <view class="record-header">
          <text class="fault-type">{{ order.faultType }}</text>
          <text class="record-status" :class="'status-' + order.status.toLowerCase()">{{ getStatusText(order.status) }}</text>
        </view>
        <view class="record-desc">{{ order.repairDescription || order.faultDescription }}</view>
        <view class="record-footer">
          <text>维修人: {{ order.staffName || '未分配' }}</text>
          <text v-if="order.repairDuration">用时: {{ order.repairDuration }}分钟</text>
        </view>
      </view>
    </view>
    
    <view class="action-bar">
      <button class="btn btn-secondary" @click="reportFault">上报故障</button>
      <button class="btn btn-primary" @click="goToNav">导航前往</button>
    </view>
  </view>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { api } from '@/api'

const bike = ref(null)
const faultRecords = ref([])
const repairOrders = ref([])

const getStatusText = (s) => {
  const map = { 
    AVAILABLE: '可用', 
    IN_USE: '使用中', 
    FAULT: '故障', 
    MAINTENANCE: '维护中',
    PENDING: '待处理',
    PROCESSING: '处理中',
    RESOLVED: '已解决',
    COMPLETED: '已完成',
    ACCEPTED: '已接单'
  }
  return map[s] || s
}

const formatTime = (time) => {
  if (!time) return ''
  const d = new Date(time)
  return `${d.getFullYear()}-${(d.getMonth()+1).toString().padStart(2,'0')}-${d.getDate().toString().padStart(2,'0')}`
}

const reportFault = () => {
  uni.showActionSheet({
    itemList: ['刹车故障', '轮胎故障', '链条故障', '电池故障', '其他故障'],
    success: (res) => {
      uni.showToast({ title: '故障已上报', icon: 'success' })
    }
  })
}

const goToNav = () => {
  if (bike.value?.longitude && bike.value?.latitude) {
    uni.openLocation({
      latitude: bike.value.latitude,
      longitude: bike.value.longitude,
      name: bike.value.location || '车辆位置',
      address: bike.value.location || '车辆位置'
    })
  } else {
    uni.navigateTo({ url: '/pages/map/map' })
  }
}

const loadData = async () => {
  const pages = getCurrentPages()
  const currentPage = pages[pages.length - 1]
  const options = currentPage.options
  
  try {
    const res = await api.getBikeDetail(options.id)
    bike.value = res.bike
    faultRecords.value = res.faultRecords || []
    repairOrders.value = res.repairOrders || []
  } catch (e) {
    console.error(e)
  }
}

onMounted(() => {
  loadData()
})
</script>

<style lang="scss" scoped>
.detail-container {
  min-height: 100vh;
  background: #f5f5f5;
  padding: 20rpx 20rpx 140rpx;
}

.card {
  background: #fff;
  border-radius: 16rpx;
  padding: 30rpx;
  margin-bottom: 20rpx;
}

.bike-header {
  display: flex;
  align-items: center;
  gap: 24rpx;
  
  .bike-icon {
    width: 100rpx;
    height: 100rpx;
    background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
    border-radius: 50%;
    display: flex;
    align-items: center;
    justify-content: center;
    font-size: 50rpx;
  }
  
  .bike-info {
    flex: 1;
    
    .bike-no {
      display: block;
      font-size: 36rpx;
      font-weight: bold;
      color: #333;
      margin-bottom: 10rpx;
    }
    
    .bike-status {
      display: inline-block;
      padding: 8rpx 20rpx;
      border-radius: 20rpx;
      font-size: 24rpx;
      
      &.status-available { background: #f0f9eb; color: #67c23a; }
      &.status-in_use { background: #e6f7ff; color: #1890ff; }
      &.status-fault { background: #ffebee; color: #e64340; }
      &.status-maintenance { background: #fff7e6; color: #ff976a; }
    }
  }
}

.section-title {
  font-size: 32rpx;
  font-weight: bold;
  color: #333;
  margin-bottom: 20rpx;
}

.info-row {
  display: flex;
  align-items: center;
  padding: 20rpx 0;
  border-bottom: 1rpx solid #f0f0f0;
  
  &:last-child {
    border-bottom: none;
  }
  
  .label {
    width: 180rpx;
    color: #999;
    font-size: 28rpx;
  }
  
  .value {
    flex: 1;
    color: #333;
    font-size: 28rpx;
    
    &.fault {
      color: #e64340;
    }
  }
  
  .battery-bar {
    flex: 1;
    height: 32rpx;
    background: #f0f0f0;
    border-radius: 16rpx;
    position: relative;
    overflow: hidden;
    
    .battery-fill {
      height: 100%;
      background: linear-gradient(90deg, #67c23a, #85ce61);
      border-radius: 16rpx;
      transition: width 0.3s;
    }
    
    .battery-text {
      position: absolute;
      top: 50%;
      left: 50%;
      transform: translate(-50%, -50%);
      font-size: 22rpx;
      color: #333;
      font-weight: bold;
    }
  }
}

.record-item {
  padding: 20rpx;
  background: #f9f9f9;
  border-radius: 12rpx;
  margin-bottom: 16rpx;
  
  &:last-child {
    margin-bottom: 0;
  }
  
  .record-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 12rpx;
    
    .fault-type {
      font-size: 28rpx;
      font-weight: 500;
      color: #333;
    }
    
    .record-status {
      padding: 4rpx 12rpx;
      border-radius: 8rpx;
      font-size: 22rpx;
      
      &.status-reported, &.status-pending { background: #f4f4f5; color: #909399; }
      &.status-processing, &.status-accepted { background: #e6f7ff; color: #1890ff; }
      &.status-resolved, &.status-completed { background: #f0f9eb; color: #67c23a; }
    }
  }
  
  .record-desc {
    font-size: 26rpx;
    color: #666;
    margin-bottom: 12rpx;
    line-height: 1.5;
  }
  
  .record-time {
    font-size: 24rpx;
    color: #999;
  }
  
  .record-footer {
    display: flex;
    justify-content: space-between;
    font-size: 24rpx;
    color: #999;
  }
}

.action-bar {
  position: fixed;
  bottom: 0;
  left: 0;
  right: 0;
  background: #fff;
  padding: 20rpx;
  display: flex;
  gap: 20rpx;
  box-shadow: 0 -4rpx 20rpx rgba(0, 0, 0, 0.1);
  
  .btn {
    flex: 1;
    height: 88rpx;
    border-radius: 12rpx;
    font-size: 30rpx;
    border: none;
    
    &.btn-secondary {
      background: #f5f5f5;
      color: #666;
    }
    
    &.btn-primary {
      background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
      color: #fff;
    }
  }
}
</style>
