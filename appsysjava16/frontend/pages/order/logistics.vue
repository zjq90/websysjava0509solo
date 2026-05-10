<template>
  <view class="container">
    <view class="logistics-header" v-if="tracking">
      <view class="logistics-icon">🚚</view>
      <text class="logistics-title">物流跟踪</text>
      <text class="tracking-no">运单号: {{ tracking.waybillNumber }}</text>
    </view>

    <view class="status-card" v-if="tracking">
      <view class="status-main">
        <text class="status-text">{{ getStatusText(tracking.status) }}</text>
        <text class="status-desc">{{ getStatusDesc(tracking.status) }}</text>
      </view>
      <view class="status-info">
        <view class="info-item">
          <text class="label">物流公司</text>
          <text class="value">{{ tracking.carrierName }}</text>
        </view>
        <view class="info-item">
          <text class="label">发货时间</text>
          <text class="value">{{ formatDate(tracking.shippedTime) }}</text>
        </view>
      </view>
    </view>

    <view class="timeline-card" v-if="tracking && tracking.nodes && tracking.nodes.length > 0">
      <view class="card-title">物流详情</view>
      <view class="timeline">
        <view 
          class="timeline-item" 
          :class="{ 'first': index === 0 }"
          v-for="(node, index) in tracking.nodes" 
          :key="node.id"
        >
          <view class="timeline-dot" :class="{ 'active': index === 0 }"></view>
          <view class="timeline-line" v-if="index < tracking.nodes.length - 1"></view>
          <view class="timeline-content">
            <text class="timeline-title">{{ node.statusText }}</text>
            <text class="timeline-desc">{{ node.description }}</text>
            <text class="timeline-time">{{ formatDate(node.nodeTime) }}</text>
          </view>
        </view>
      </view>
    </view>

    <view class="action-bar">
      <button class="action-btn" @click="refresh">刷新物流</button>
      <button class="action-btn secondary" @click="goBack">返回</button>
    </view>
  </view>
</template>

<script>
import logisticsApi from '@/api/logistics.js'

export default {
  data() {
    return {
      orderId: '',
      tracking: null
    }
  },
  onLoad(options) {
    this.orderId = options.orderId
    this.loadTracking()
  },
  methods: {
    async loadTracking() {
      uni.showLoading({ title: '加载中...' })
      try {
        this.tracking = await logisticsApi.getTrackingByOrderId(this.orderId)
      } catch (e) {
        console.error('加载物流失败', e)
      } finally {
        uni.hideLoading()
      }
    },
    async refresh() {
      uni.showLoading({ title: '刷新中...' })
      try {
        await logisticsApi.updateTracking(this.orderId)
        await this.loadTracking()
        uni.showToast({ title: '已刷新', icon: 'success' })
      } catch (e) {
        console.error('刷新失败', e)
      } finally {
        uni.hideLoading()
      }
    },
    getStatusText(status) {
      const map = {
        'PENDING': '待发货',
        'SHIPPED': '已发货',
        'IN_TRANSIT': '运输中',
        'OUT_FOR_DELIVERY': '派送中',
        'DELIVERED': '已签收',
        'RETURNED': '已退回'
      }
      return map[status] || status
    },
    getStatusDesc(status) {
      const map = {
        'PENDING': '商品等待发货',
        'SHIPPED': '商品已发出',
        'IN_TRANSIT': '商品正在运输中',
        'OUT_FOR_DELIVERY': '快递员正在派送',
        'DELIVERED': '商品已签收',
        'RETURNED': '商品已退回'
      }
      return map[status] || ''
    },
    formatDate(date) {
      if (!date) return ''
      const d = new Date(date)
      const y = d.getFullYear()
      const m = String(d.getMonth() + 1).padStart(2, '0')
      const day = String(d.getDate()).padStart(2, '0')
      const h = String(d.getHours()).padStart(2, '0')
      const min = String(d.getMinutes()).padStart(2, '0')
      return `${y}-${m}-${day} ${h}:${min}`
    },
    goBack() {
      uni.navigateBack()
    }
  }
}
</script>

<style scoped>
.logistics-header {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  padding: 40rpx;
  border-radius: 16rpx;
  margin-bottom: 24rpx;
  text-align: center;
}

.logistics-icon {
  font-size: 64rpx;
  margin-bottom: 12rpx;
}

.logistics-title {
  font-size: 36rpx;
  font-weight: bold;
  color: #fff;
  display: block;
  margin-bottom: 8rpx;
}

.tracking-no {
  font-size: 26rpx;
  color: rgba(255, 255, 255, 0.8);
}

.status-card {
  background: #fff;
  border-radius: 16rpx;
  padding: 32rpx;
  margin-bottom: 20rpx;
}

.status-main {
  text-align: center;
  padding-bottom: 24rpx;
  border-bottom: 2rpx solid #f5f5f5;
}

.status-text {
  font-size: 40rpx;
  font-weight: bold;
  color: #667eea;
  display: block;
  margin-bottom: 8rpx;
}

.status-desc {
  font-size: 26rpx;
  color: #666;
}

.status-info {
  display: flex;
  padding-top: 24rpx;
}

.info-item {
  flex: 1;
  text-align: center;
}

.info-item .label {
  font-size: 24rpx;
  color: #999;
  display: block;
  margin-bottom: 8rpx;
}

.info-item .value {
  font-size: 28rpx;
  color: #333;
}

.timeline-card {
  background: #fff;
  border-radius: 16rpx;
  padding: 24rpx;
  margin-bottom: 24rpx;
}

.card-title {
  font-size: 30rpx;
  font-weight: 500;
  color: #333;
  margin-bottom: 24rpx;
  padding-bottom: 12rpx;
  border-bottom: 2rpx solid #f5f5f5;
}

.timeline {
  position: relative;
  padding-left: 40rpx;
}

.timeline-item {
  position: relative;
  padding-bottom: 32rpx;
}

.timeline-item:last-child {
  padding-bottom: 0;
}

.timeline-dot {
  position: absolute;
  left: -40rpx;
  top: 8rpx;
  width: 20rpx;
  height: 20rpx;
  border-radius: 50%;
  background: #e0e0e0;
}

.timeline-dot.active {
  background: #667eea;
  box-shadow: 0 0 0 6rpx rgba(102, 126, 234, 0.2);
}

.timeline-line {
  position: absolute;
  left: -31rpx;
  top: 28rpx;
  width: 2rpx;
  height: calc(100% - 16rpx);
  background: #e0e0e0;
}

.timeline-content {
  display: flex;
  flex-direction: column;
  gap: 6rpx;
}

.timeline-title {
  font-size: 28rpx;
  color: #333;
  font-weight: 500;
}

.timeline-item.first .timeline-title {
  color: #667eea;
}

.timeline-desc {
  font-size: 26rpx;
  color: #666;
}

.timeline-time {
  font-size: 24rpx;
  color: #999;
  margin-top: 4rpx;
}

.action-bar {
  display: flex;
  gap: 20rpx;
}

.action-btn {
  flex: 1;
  height: 88rpx;
  line-height: 88rpx;
  border-radius: 44rpx;
  font-size: 30rpx;
  border: none;
}

.action-btn {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: #fff;
}

.action-btn.secondary {
  background: #f5f5f5;
  color: #666;
}
</style>
