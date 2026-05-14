<template>
  <view class="container" :class="{ 'elder-mode': elderMode }">
    <view v-if="order" class="detail-card card">
      <view class="order-header">
        <view class="order-no">工单号：{{ order.orderNo }}</view>
        <view class="order-status" :class="getStatusClass(order.status)">{{ order.status }}</view>
      </view>

      <view class="timeline">
        <view v-for="(step, index) in steps" :key="index" class="timeline-item" :class="{ active: index < order.currentStep, current: index === order.currentStep - 1 }">
          <view class="timeline-dot"></view>
          <view class="timeline-content">
            <view class="timeline-title">{{ step }}</view>
            <view v-if="index === order.currentStep - 1" class="timeline-desc">进行中...</view>
          </view>
        </view>
      </view>

      <view v-if="order.workerName" class="worker-info card">
        <view class="card-title">装维人员信息</view>
        <view class="worker-detail">
          <view class="worker-avatar">
            <text>{{ order.workerName.charAt(0) }}</text>
          </view>
          <view class="worker-info-text">
            <view class="worker-name">{{ order.workerName }}</view>
            <view class="worker-phone">{{ order.workerPhone }}</view>
          </view>
          <button class="call-btn" @click="callWorker">
            <text>📞 联系</text>
          </button>
        </view>
      </view>

      <view v-if="order.workerLat" class="map-card card">
        <view class="card-title">实时位置</view>
        <view class="map-placeholder">
          <text class="map-icon">📍</text>
          <text class="map-text">装维人员正在赶来</text>
          <text class="map-location">纬度：{{ order.workerLat.toFixed(4) }}</text>
          <text class="map-location">经度：{{ order.workerLng.toFixed(4) }}</text>
        </view>
      </view>

      <view class="address-info">
        <view class="info-label">服务地址</view>
        <view class="info-value">{{ order.address || '北京市朝阳区' }}</view>
      </view>

      <view v-if="order.description" class="desc-info">
        <view class="info-label">服务描述</view>
        <view class="info-value">{{ order.description }}</view>
      </view>
    </view>

    <view v-else class="empty-state">
      <text>加载中...</text>
    </view>
  </view>
</template>

<script>
import { serviceOrderApi } from '@/api/index.js'

export default {
  data() {
    return {
      order: null,
      steps: ['资料审核', '装维派单', '上门安装', '已完成'],
      elderMode: false
    }
  },
  onLoad(options) {
    if (options.id) {
      this.loadOrderDetail(options.id)
    } else {
      this.loadOrderList()
    }
  },
  methods: {
    async loadOrderDetail(id) {
      try {
        this.order = await serviceOrderApi.get(id)
      } catch (e) {
        console.error(e)
      }
    },
    async loadOrderList() {
      try {
        const list = await serviceOrderApi.list(1)
        if (list.length > 0) {
          this.order = list[0]
        }
      } catch (e) {
        console.error(e)
      }
    },
    callWorker() {
      if (this.order && this.order.workerPhone) {
        uni.makePhoneCall({
          phoneNumber: this.order.workerPhone,
          fail: () => {
            uni.showToast({
              title: '拨号失败',
              icon: 'none'
            })
          }
        })
      }
    },
    getStatusClass(status) {
      if (status.includes('完成')) return 'status-completed'
      if (status.includes('审核')) return 'status-review'
      return 'status-processing'
    }
  }
}
</script>

<style scoped>
.order-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 30rpx;
  padding-bottom: 20rpx;
  border-bottom: 1rpx solid #f0f0f0;
}

.order-no {
  font-size: 26rpx;
  color: #666;
}

.order-status {
  font-size: 26rpx;
  padding: 8rpx 20rpx;
  border-radius: 24rpx;
  font-weight: 500;
}

.status-completed {
  background: #e8f5e9;
  color: #4caf50;
}

.status-review {
  background: #fff3e0;
  color: #ff9800;
}

.status-processing {
  background: #e3f2fd;
  color: #2196f3;
}

.timeline {
  padding: 20rpx 0;
}

.timeline-item {
  display: flex;
  position: relative;
  padding-bottom: 30rpx;
  padding-left: 40rpx;
}

.timeline-item::before {
  content: '';
  position: absolute;
  left: 10rpx;
  top: 30rpx;
  width: 2rpx;
  height: calc(100% - 30rpx);
  background: #e0e0e0;
}

.timeline-item:last-child::before {
  display: none;
}

.timeline-dot {
  position: absolute;
  left: 0;
  top: 6rpx;
  width: 24rpx;
  height: 24rpx;
  border-radius: 50%;
  background: #e0e0e0;
  border: 4rpx solid #fff;
  box-shadow: 0 2rpx 8rpx rgba(0, 0, 0, 0.1);
}

.timeline-item.active .timeline-dot {
  background: #667eea;
}

.timeline-item.current .timeline-dot {
  background: #667eea;
  animation: pulse 2s infinite;
}

@keyframes pulse {
  0%, 100% {
    box-shadow: 0 0 0 0 rgba(102, 126, 234, 0.4);
  }
  50% {
    box-shadow: 0 0 0 10rpx rgba(102, 126, 234, 0);
  }
}

.timeline-content {
  flex: 1;
}

.timeline-title {
  font-size: 28rpx;
  color: #999;
  margin-bottom: 8rpx;
}

.timeline-item.active .timeline-title {
  color: #333;
  font-weight: 500;
}

.timeline-item.current .timeline-title {
  color: #667eea;
  font-weight: bold;
}

.timeline-desc {
  font-size: 24rpx;
  color: #667eea;
}

.worker-detail {
  display: flex;
  align-items: center;
  gap: 20rpx;
}

.worker-avatar {
  width: 80rpx;
  height: 80rpx;
  border-radius: 50%;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  display: flex;
  align-items: center;
  justify-content: center;
  color: #fff;
  font-size: 32rpx;
  font-weight: bold;
}

.worker-info-text {
  flex: 1;
}

.worker-name {
  font-size: 30rpx;
  font-weight: 500;
  color: #333;
  margin-bottom: 6rpx;
}

.worker-phone {
  font-size: 24rpx;
  color: #666;
}

.call-btn {
  background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
  color: #fff;
  border: none;
  border-radius: 32rpx;
  padding: 12rpx 24rpx;
  font-size: 24rpx;
  line-height: 1;
}

.map-placeholder {
  height: 300rpx;
  background: linear-gradient(135deg, #e8f5e9 0%, #c8e6c9 100%);
  border-radius: 12rpx;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  gap: 10rpx;
}

.map-icon {
  font-size: 60rpx;
}

.map-text {
  font-size: 28rpx;
  color: #2e7d32;
  font-weight: 500;
}

.map-location {
  font-size: 24rpx;
  color: #666;
}

.address-info, .desc-info {
  margin-top: 20rpx;
  padding: 20rpx;
  background: #f8f9fa;
  border-radius: 12rpx;
}

.info-label {
  font-size: 24rpx;
  color: #999;
  margin-bottom: 8rpx;
}

.info-value {
  font-size: 28rpx;
  color: #333;
}

.empty-state {
  text-align: center;
  padding: 100rpx 0;
  color: #999;
  font-size: 28rpx;
}
</style>
