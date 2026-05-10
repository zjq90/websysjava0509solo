<template>
  <view class="container">
    <view class="scan-result card" v-if="scanResult">
      <view class="result-header">
        <text class="fs-32 fw-bold">扫码结果</text>
        <text class="fs-24 text-primary" @click="clearResult">清除</text>
      </view>
      
      <view class="result-info" v-if="scanResult.inventory">
        <view class="info-row">
          <text class="info-label">批次号</text>
          <text class="info-value fw-bold">{{ scanResult.inventory.batchNo }}</text>
        </view>
        <view class="info-row">
          <text class="info-label">当前库存</text>
          <text class="info-value text-primary">{{ scanResult.inventory.quantity }} {{ scanResult.inventory.unit }}</text>
        </view>
        <view class="info-row">
          <text class="info-label">保质期</text>
          <text class="info-value" :class="getStatusClass(scanResult.inventory.status)">
            {{ scanResult.inventory.expiryDate }}
          </text>
        </view>
        <view class="info-row">
          <text class="info-label">发芽率</text>
          <text class="info-value">{{ scanResult.inventory.germinationRate }}%</text>
        </view>
        <view class="info-row">
          <text class="info-label">状态</text>
          <view :class="getStatusBadgeClass(scanResult.inventory.status)">
            <text>{{ getStatusText(scanResult.inventory.status) }}</text>
          </view>
        </view>
        
        <view class="action-buttons mt-20">
          <view class="btn-danger flex-1 mr-10" @click="goToOutbound">
            <text>出库</text>
          </view>
          <view class="btn-success flex-1 ml-10" @click="goToInbound">
            <text>入库</text>
          </view>
        </view>
      </view>
      
      <view class="empty-result" v-else>
        <text class="text-muted">未找到该批次的库存记录</text>
      </view>
    </view>
    
    <view class="scan-actions">
      <view class="scan-btn-primary" @click="startScan">
        <text class="scan-icon">📷</text>
        <text class="fs-30 fw-bold">扫描二维码/条形码</text>
        <text class="fs-24 text-muted mt-10">扫描批次号快速查询库存</text>
      </view>
    </view>
    
    <view class="manual-input card">
      <view class="section-title">
        <text class="fs-32 fw-bold">手动输入</text>
      </view>
      <view class="input-group">
        <input 
          class="input-field" 
          v-model="manualBatchNo" 
          placeholder="请输入8位批次号（数字+字母组合）"
          maxlength="8"
        />
      </view>
      <view class="btn-primary mt-20" @click="queryByBatchNo">
        <text>查询</text>
      </view>
    </view>
    
    <view class="quick-batches card">
      <view class="section-title">
        <text class="fs-32 fw-bold">测试批次号</text>
      </view>
      <view class="batch-list">
        <view class="batch-item" v-for="batch in testBatches" :key="batch" @click="selectBatch(batch)">
          <text class="fs-28">{{ batch }}</text>
        </view>
      </view>
    </view>
  </view>
</template>

<script>
import request from '@/utils/request.js'

export default {
  data() {
    return {
      scanResult: null,
      manualBatchNo: '',
      testBatches: ['WH01AB1234', 'WH02CD5678', 'WH03EF9012', 'WH04GH3456', 'WH05IJ7890']
    }
  },
  methods: {
    startScan() {
      uni.scanCode({
        success: async (res) => {
          await this.queryInventory(res.result)
        },
        fail: (err) => {
          uni.showToast({
            title: '扫码失败：' + err.errMsg,
            icon: 'none'
          })
        }
      })
    },
    
    async queryByBatchNo() {
      if (!this.manualBatchNo) {
        uni.showToast({ title: '请输入批次号', icon: 'none' })
        return
      }
      await this.queryInventory(this.manualBatchNo)
    },
    
    async queryInventory(batchNo) {
      try {
        const res = await request.get(`/api/inventory/scan/${batchNo}`)
        this.scanResult = res.data
        uni.setStorageSync('currentBatchNo', batchNo)
      } catch (e) {
        this.scanResult = { inventory: null, records: [] }
      }
    },
    
    selectBatch(batch) {
      this.manualBatchNo = batch
    },
    
    clearResult() {
      this.scanResult = null
      this.manualBatchNo = ''
    },
    
    getStatusClass(status) {
      const map = {
        'NORMAL': 'text-success',
        'NEAR_EXPIRY': 'text-warning',
        'EXPIRED': 'text-danger',
        'LOW_STOCK': 'text-warning'
      }
      return map[status] || ''
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
    
    goToOutbound() {
      if (this.scanResult && this.scanResult.inventory) {
        uni.setStorageSync('currentBatchNo', this.scanResult.inventory.batchNo)
      }
      uni.navigateTo({ url: '/pages/scan/outbound' })
    },
    
    goToInbound() {
      uni.navigateTo({ url: '/pages/scan/inbound' })
    }
  }
}
</script>

<style scoped>
.scan-actions {
  margin-bottom: 30rpx;
}

.scan-btn-primary {
  background: linear-gradient(135deg, #2979FF 0%, #667eea 100%);
  border-radius: 24rpx;
  padding: 60rpx 30rpx;
  display: flex;
  flex-direction: column;
  align-items: center;
  color: #ffffff;
}

.scan-icon {
  font-size: 80rpx;
  margin-bottom: 20rpx;
}

.result-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 30rpx;
}

.result-info {
  display: flex;
  flex-direction: column;
  gap: 20rpx;
}

.info-row {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 15rpx 0;
  border-bottom: 1rpx solid #f0f0f0;
}

.info-row:last-child {
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

.action-buttons {
  display: flex;
  margin-top: 20rpx;
}

.flex-1 {
  flex: 1;
}

.mr-10 {
  margin-right: 10rpx;
}

.ml-10 {
  margin-left: 10rpx;
}

.btn-danger, .btn-success {
  padding: 24rpx;
  text-align: center;
  font-size: 30rpx;
  font-weight: 500;
  border-radius: 40rpx;
  color: #ffffff;
}

.empty-result {
  text-align: center;
  padding: 40rpx 0;
}

.input-group {
  margin-bottom: 20rpx;
}

.input-field {
  width: 100%;
  height: 88rpx;
  background-color: #f5f7fa;
  border-radius: 12rpx;
  padding: 0 24rpx;
  font-size: 28rpx;
}

.batch-list {
  display: flex;
  flex-wrap: wrap;
  gap: 15rpx;
}

.batch-item {
  background-color: #E3F2FD;
  color: #1976D2;
  padding: 16rpx 24rpx;
  border-radius: 12rpx;
  font-size: 26rpx;
}
</style>
