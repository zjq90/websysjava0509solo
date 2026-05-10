<template>
  <view class="container">
    <view class="header-bar">
      <text class="fs-32 fw-bold">仓库管理</text>
    </view>
    
    <view class="warehouse-list">
      <view 
        class="warehouse-card card" 
        v-for="warehouse in warehouses" 
        :key="warehouse.id"
        :class="{ active: isCurrentWarehouse(warehouse.id) }"
        @click="selectWarehouse(warehouse)"
      >
        <view class="card-header">
          <text class="fs-30 fw-bold">{{ warehouse.warehouseName }}</text>
          <view :class="warehouse.status === 'ACTIVE' ? 'badge-success' : 'badge-normal'">
            <text>{{ warehouse.status === 'ACTIVE' ? '正常' : '停用' }}</text>
          </view>
        </view>
        <view class="card-body">
          <view class="info-row">
            <text class="info-label">仓库编号</text>
            <text class="info-value">{{ warehouse.warehouseCode }}</text>
          </view>
          <view class="info-row" v-if="warehouse.address">
            <text class="info-label">地址</text>
            <text class="info-value">{{ warehouse.address }}</text>
          </view>
          <view class="info-row" v-if="warehouse.manager">
            <text class="info-label">管理员</text>
            <text class="info-value">{{ warehouse.manager }}</text>
          </view>
          <view class="info-row" v-if="warehouse.phone">
            <text class="info-label">联系电话</text>
            <text class="info-value">{{ warehouse.phone }}</text>
          </view>
        </view>
        <view class="card-footer" v-if="isCurrentWarehouse(warehouse.id)">
          <text class="text-primary fs-26">✓ 当前选中</text>
        </view>
      </view>
      
      <view class="empty-state" v-if="warehouses.length === 0">
        <text class="text-muted fs-28">暂无仓库数据</text>
      </view>
    </view>
  </view>
</template>

<script>
import request from '@/utils/request.js'

export default {
  data() {
    return {
      warehouses: [],
      currentWarehouseId: null
    }
  },
  onShow() {
    this.loadData()
  },
  methods: {
    async loadData() {
      try {
        const res = await request.get('/api/warehouses')
        this.warehouses = res.data || []
        this.currentWarehouseId = uni.getStorageSync('currentWarehouseId')
      } catch (e) {
        console.error('加载仓库失败', e)
      }
    },
    
    isCurrentWarehouse(id) {
      return this.currentWarehouseId === id
    },
    
    selectWarehouse(warehouse) {
      if (warehouse.status !== 'ACTIVE') {
        uni.showToast({ title: '该仓库已停用', icon: 'none' })
        return
      }
      
      this.currentWarehouseId = warehouse.id
      uni.setStorageSync('currentWarehouseId', warehouse.id)
      uni.showToast({ title: '已切换仓库', icon: 'success' })
      
      setTimeout(() => {
        uni.switchTab({ url: '/pages/index/index' })
      }, 1000)
    }
  }
}
</script>

<style scoped>
.header-bar {
  padding: 20rpx;
  background-color: #ffffff;
  margin-bottom: 20rpx;
}

.warehouse-card {
  margin-bottom: 20rpx;
}

.warehouse-card.active {
  border: 2rpx solid #2979FF;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20rpx;
  padding-bottom: 20rpx;
  border-bottom: 1rpx solid #f0f0f0;
}

.card-body {
  display: flex;
  flex-direction: column;
  gap: 15rpx;
}

.info-row {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.info-label {
  font-size: 26rpx;
  color: #999;
}

.info-value {
  font-size: 26rpx;
  color: #333;
}

.card-footer {
  margin-top: 20rpx;
  padding-top: 20rpx;
  border-top: 1rpx solid #f0f0f0;
}

.empty-state {
  text-align: center;
  padding: 100rpx 0;
}
</style>
