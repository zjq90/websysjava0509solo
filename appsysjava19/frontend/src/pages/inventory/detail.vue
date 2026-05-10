<template>
  <view class="container">
    <view class="header-card" v-if="inventory">
      <view class="header-top">
        <text class="batch-no">{{ inventory.batchNo }}</text>
        <view :class="getStatusBadgeClass(inventory.status)">
          <text>{{ getStatusText(inventory.status) }}</text>
        </view>
      </view>
      <text class="fs-26 text-muted mt-10">入库时间：{{ formatDateTime(inventory.inboundTime) }}</text>
    </view>
    
    <view class="info-section card" v-if="inventory">
      <view class="section-title">
        <text class="fs-32 fw-bold">基本信息</text>
      </view>
      <view class="info-list">
        <view class="info-item">
          <text class="info-label">当前库存</text>
          <text class="info-value text-primary fw-bold">{{ inventory.quantity }} {{ inventory.unit }}</text>
        </view>
        <view class="info-item">
          <text class="info-label">保质期</text>
          <text class="info-value" :class="getExpiryClass(inventory.status)">{{ inventory.expiryDate }}</text>
        </view>
        <view class="info-item">
          <text class="info-label">发芽率</text>
          <text class="info-value">{{ inventory.germinationRate }}%</text>
        </view>
        <view class="info-item" v-if="inventory.origin">
          <text class="info-label">产地</text>
          <text class="info-value">{{ inventory.origin }}</text>
        </view>
        <view class="info-item" v-if="inventory.storageLocation">
          <text class="info-label">存储位置</text>
          <text class="info-value">{{ inventory.storageLocation }}</text>
        </view>
        <view class="info-item" v-if="inventory.remark">
          <text class="info-label">备注</text>
          <text class="info-value">{{ inventory.remark }}</text>
        </view>
      </view>
    </view>
    
    <view class="records-section card">
      <view class="section-title">
        <text class="fs-32 fw-bold">出入库记录</text>
      </view>
      <view class="record-list">
        <view class="record-item" v-for="record in records" :key="record.id">
          <view class="record-header">
            <view class="record-type" :class="record.recordType === 'INBOUND' ? 'inbound' : 'outbound'">
              <text>{{ record.recordType === 'INBOUND' ? '入库' : '出库' }}</text>
            </view>
            <text class="fs-24 text-muted">{{ formatDateTime(record.recordTime) }}</text>
          </view>
          <view class="record-body">
            <view class="record-info">
              <text class="fs-26 text-muted">操作数量：</text>
              <text class="fs-26 fw-bold" :class="record.recordType === 'INBOUND' ? 'text-success' : 'text-danger'">
                {{ record.recordType === 'INBOUND' ? '+' : '-' }}{{ record.quantity }} {{ record.unit }}
              </text>
            </view>
            <view class="record-info" v-if="record.operator">
              <text class="fs-24 text-muted">操作人：{{ record.operator }}</text>
            </view>
            <view class="record-info" v-if="record.remark">
              <text class="fs-24 text-muted">备注：{{ record.remark }}</text>
            </view>
          </view>
        </view>
        
        <view class="empty-records" v-if="records.length === 0">
          <text class="text-muted fs-28">暂无出入库记录</text>
        </view>
      </view>
    </view>
    
    <view class="action-bar" v-if="inventory && inventory.status !== 'EXPIRED'">
      <view class="action-btn btn-danger" @click="goToOutbound">
        <text class="fs-30">出库</text>
      </view>
    </view>
  </view>
</template>

<script>
import request from '@/utils/request.js'

export default {
  data() {
    return {
      inventory: null,
      records: []
    }
  },
  onLoad(options) {
    if (options.batchNo) {
      this.loadData(options.batchNo)
    }
  },
  methods: {
    async loadData(batchNo) {
      try {
        const res = await request.get(`/api/inventory/scan/${batchNo}`)
        this.inventory = res.data.inventory
        this.records = res.data.records || []
      } catch (e) {
        console.error('加载详情失败', e)
      }
    },
    
    getStatusBadgeClass(status) {
      const map = {
        'NORMAL': 'badge-success',
        'NEAR_EXPIRY': 'badge-warning',
        'EXPIRED': 'badge-danger',
        'LOW_STOCK': 'badge-warning'
      }
      return map[status] || 'badge-normal'
    },
    
    getStatusText(status) {
      const map = {
        'NORMAL': '正常',
        'NEAR_EXPIRY': '近效期',
        'EXPIRED': '已过期',
        'LOW_STOCK': '缺货'
      }
      return map[status] || '未知'
    },
    
    getExpiryClass(status) {
      if (status === 'NEAR_EXPIRY' || status === 'EXPIRED') {
        return 'text-warning'
      }
      return ''
    },
    
    formatDateTime(datetime) {
      if (!datetime) return ''
      const d = new Date(datetime)
      const year = d.getFullYear()
      const month = (d.getMonth() + 1).toString().padStart(2, '0')
      const day = d.getDate().toString().padStart(2, '0')
      const hours = d.getHours().toString().padStart(2, '0')
      const minutes = d.getMinutes().toString().padStart(2, '0')
      return `${year}-${month}-${day} ${hours}:${minutes}`
    },
    
    goToOutbound() {
      uni.setStorageSync('currentBatchNo', this.inventory.batchNo)
      uni.navigateTo({ url: '/pages/scan/outbound' })
    }
  }
}
</script>

<style scoped>
.header-card {
  background: linear-gradient(135deg, #2979FF 0%, #667eea 100%);
  border-radius: 0 0 32rpx 32rpx;
  padding: 40rpx 30rpx;
  color: #ffffff;
}

.header-top {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.batch-no {
  font-size: 40rpx;
  font-weight: 700;
}

.info-list {
  display: flex;
  flex-direction: column;
  gap: 25rpx;
}

.info-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 15rpx 0;
  border-bottom: 1rpx solid #f0f0f0;
}

.info-item:last-child {
  border-bottom: none;
}

.info-label {
  font-size: 28rpx;
  color: #999;
}

.info-value {
  font-size: 28rpx;
  color: #333;
}

.record-list {
  display: flex;
  flex-direction: column;
  gap: 20rpx;
}

.record-item {
  background-color: #f9fafb;
  border-radius: 16rpx;
  padding: 25rpx;
}

.record-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 15rpx;
}

.record-type {
  padding: 6rpx 16rpx;
  border-radius: 8rpx;
  font-size: 24rpx;
}

.record-type.inbound {
  background-color: #E8F5E9;
  color: #4CAF50;
}

.record-type.outbound {
  background-color: #FFEBEE;
  color: #F44336;
}

.record-body {
  display: flex;
  flex-direction: column;
  gap: 8rpx;
}

.record-info {
  display: flex;
  align-items: center;
}

.empty-records {
  text-align: center;
  padding: 40rpx 0;
}

.action-bar {
  position: fixed;
  bottom: 0;
  left: 0;
  right: 0;
  background-color: #ffffff;
  padding: 20rpx 30rpx;
  padding-bottom: calc(20rpx + env(safe-area-inset-bottom));
  box-shadow: 0 -4rpx 20rpx rgba(0, 0, 0, 0.08);
}

.action-btn {
  padding: 28rpx;
  text-align: center;
  border-radius: 44rpx;
  color: #ffffff;
}
</style>
