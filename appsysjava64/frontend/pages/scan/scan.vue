<template>
  <view class="scan-container">
    <view class="scan-area">
      <view class="scan-frame">
        <view class="corner corner-tl"></view>
        <view class="corner corner-tr"></view>
        <view class="corner corner-bl"></view>
        <view class="corner corner-br"></view>
        <view class="scan-line" :class="{ scanning: scanning }"></view>
      </view>
      <text class="scan-tip">将二维码放入框内，自动扫描</text>
    </view>
    
    <view class="action-buttons">
      <button class="action-btn" @click="startScan">
        <text class="btn-icon">📷</text>
        <text class="btn-text">扫码</text>
      </button>
      <button class="action-btn" @click="inputCode">
        <text class="btn-icon">⌨️</text>
        <text class="btn-text">手动输入</text>
      </button>
      <button class="action-btn" @click="openFlash">
        <text class="btn-icon">🔦</text>
        <text class="btn-text">手电筒</text>
      </button>
    </view>
    
    <view class="recent-scan" v-if="recentScans.length > 0">
      <view class="section-title">最近扫描</view>
      <view class="scan-item" v-for="(item, index) in recentScans" :key="index" @click="goToDetail(item)">
        <text class="bike-icon">🚲</text>
        <view class="scan-info">
          <text class="bike-no">{{ item.bikeNo }}</text>
          <text class="scan-time">{{ item.time }}</text>
        </view>
        <text class="arrow">›</text>
      </view>
    </view>
  </view>
</template>

<script setup>
import { ref } from 'vue'

const scanning = ref(false)
const recentScans = ref([
  { bikeNo: 'BK001', time: '10:30', id: 1 },
  { bikeNo: 'BK002', time: '09:15', id: 2 },
  { bikeNo: 'BK005', time: '昨天 16:42', id: 5 }
])

const startScan = () => {
  scanning.value = true
  uni.scanCode({
    success: (res) => {
      scanning.value = false
      uni.showToast({ title: '扫描成功', icon: 'success' })
      setTimeout(() => {
        uni.navigateTo({ url: `/pages/bike-detail/bike-detail?id=1` })
      }, 500)
    },
    fail: () => {
      scanning.value = false
    }
  })
}

const inputCode = () => {
  uni.showModal({
    title: '输入车辆编号',
    editable: true,
    placeholderText: '请输入车辆编号',
    success: (res) => {
      if (res.confirm && res.content) {
        uni.navigateTo({ url: `/pages/bike-detail/bike-detail?id=1` })
      }
    }
  })
}

const openFlash = () => {
  uni.showToast({ title: '手电筒功能', icon: 'none' })
}

const goToDetail = (item) => {
  uni.navigateTo({ url: `/pages/bike-detail/bike-detail?id=${item.id}` })
}
</script>

<style lang="scss" scoped>
.scan-container {
  min-height: 100vh;
  background: #1a1a1a;
  padding: 40rpx;
}

.scan-area {
  position: relative;
  height: 600rpx;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  
  .scan-frame {
    width: 400rpx;
    height: 400rpx;
    position: relative;
    
    .corner {
      position: absolute;
      width: 40rpx;
      height: 40rpx;
      border: 4rpx solid #07c160;
      
      &.corner-tl {
        top: 0;
        left: 0;
        border-right: none;
        border-bottom: none;
      }
      
      &.corner-tr {
        top: 0;
        right: 0;
        border-left: none;
        border-bottom: none;
      }
      
      &.corner-bl {
        bottom: 0;
        left: 0;
        border-right: none;
        border-top: none;
      }
      
      &.corner-br {
        bottom: 0;
        right: 0;
        border-left: none;
        border-top: none;
      }
    }
    
    .scan-line {
      position: absolute;
      left: 0;
      right: 0;
      height: 4rpx;
      background: linear-gradient(90deg, transparent, #07c160, transparent);
      
      &.scanning {
        animation: scan 2s linear infinite;
      }
    }
  }
  
  .scan-tip {
    color: rgba(255, 255, 255, 0.7);
    font-size: 28rpx;
    margin-top: 40rpx;
  }
}

@keyframes scan {
  0% { top: 0; }
  100% { top: 100%; }
}

.action-buttons {
  display: flex;
  justify-content: space-around;
  margin-top: 60rpx;
  
  .action-btn {
    width: 120rpx;
    height: 120rpx;
    background: rgba(255, 255, 255, 0.1);
    border-radius: 50%;
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
    border: none;
    
    .btn-icon {
      font-size: 40rpx;
      margin-bottom: 8rpx;
    }
    
    .btn-text {
      font-size: 24rpx;
      color: #fff;
    }
  }
}

.recent-scan {
  margin-top: 60rpx;
  
  .section-title {
    color: rgba(255, 255, 255, 0.7);
    font-size: 28rpx;
    margin-bottom: 20rpx;
  }
  
  .scan-item {
    display: flex;
    align-items: center;
    background: rgba(255, 255, 255, 0.1);
    border-radius: 12rpx;
    padding: 24rpx;
    margin-bottom: 16rpx;
    
    .bike-icon {
      font-size: 40rpx;
      margin-right: 20rpx;
    }
    
    .scan-info {
      flex: 1;
      
      .bike-no {
        display: block;
        font-size: 28rpx;
        color: #fff;
        margin-bottom: 8rpx;
      }
      
      .scan-time {
        font-size: 24rpx;
        color: rgba(255, 255, 255, 0.6);
      }
    }
    
    .arrow {
      color: rgba(255, 255, 255, 0.4);
      font-size: 36rpx;
    }
  }
}
</style>
