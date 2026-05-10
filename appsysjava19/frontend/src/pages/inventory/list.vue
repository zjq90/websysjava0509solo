<template>
  <view class="container">
    <view class="filter-section">
      <view class="filter-tabs">
        <view 
          class="filter-tab" 
          :class="{ active: currentFilter === 'all' }"
          @click="setFilter('all')"
        >
          <text>全部</text>
        </view>
        <view 
          class="filter-tab" 
          :class="{ active: currentFilter === 'normal' }"
          @click="setFilter('normal')"
        >
          <text>正常</text>
        </view>
        <view 
          class="filter-tab" 
          :class="{ active: currentFilter === 'near' }"
          @click="setFilter('near')"
        >
          <text>近效期</text>
        </view>
        <view 
          class="filter-tab" 
          :class="{ active: currentFilter === 'low' }"
          @click="setFilter('low')"
        >
          <text>缺货</text>
        </view>
      </view>
      
      <view class="warehouse-selector" @click="showWarehousePicker = true">
        <text class="fs-26">{{ currentWarehouse ? currentWarehouse.warehouseName : '全部仓库' }}</text>
        <text class="text-muted">›</text>
      </view>
    </view>
    
    <view class="stats-bar">
      <view class="stat-item">
        <text class="stat-num">{{ filteredList.length }}</text>
        <text class="stat-label">批次</text>
      </view>
      <view class="stat-item">
        <text class="stat-num text-warning">{{ nearExpiryCount }}</text>
        <text class="stat-label">近效期</text>
      </view>
      <view class="stat-item">
        <text class="stat-num text-danger">{{ lowStockCount }}</text>
        <text class="stat-label">缺货</text>
      </view>
    </view>
    
    <view class="inventory-list">
      <view 
        class="inventory-card card" 
        v-for="item in filteredList" 
        :key="item.id"
        @click="goToDetail(item)"
      >
        <view class="card-header">
          <view class="batch-info">
            <text class="batch-no">{{ item.batchNo }}</text>
            <view :class="getStatusBadgeClass(item.status)">
              <text>{{ getStatusText(item.status) }}</text>
            </view>
          </view>
          <text class="fs-26 text-muted">{{ formatDate(item.inboundTime) }}</text>
        </view>
        
        <view class="card-body">
          <view class="info-row">
            <text class="info-label">库存</text>
            <text class="info-value text-primary fw-bold">{{ item.quantity }} {{ item.unit }}</text>
          </view>
          <view class="info-row">
            <text class="info-label">保质期</text>
            <text class="info-value" :class="getExpiryClass(item.status)">
              {{ item.expiryDate }}
            </text>
          </view>
          <view class="info-row">
            <text class="info-label">发芽率</text>
            <text class="info-value">{{ item.germinationRate }}%</text>
          </view>
          <view class="info-row" v-if="item.origin">
            <text class="info-label">产地</text>
            <text class="info-value">{{ item.origin }}</text>
          </view>
        </view>
        
        <view class="card-footer">
          <view class="action-btn action-outbound" @click.stop="goToOutbound(item)">
            <text>出库</text>
          </view>
        </view>
      </view>
      
      <view class="empty-state" v-if="filteredList.length === 0">
        <text class="fs-28 text-muted">暂无库存数据</text>
      </view>
    </view>
  </view>
</template>

<script>
import request from '@/utils/request.js'

export default {
  data() {
    return {
      inventoryList: [],
      warehouses: [],
      currentWarehouse: null,
      currentFilter: 'all',
      showWarehousePicker: false
    }
  },
  computed: {
    filteredList() {
      let list = this.inventoryList
      
      if (this.currentWarehouse) {
        list = list.filter(item => item.warehouseId === this.currentWarehouse.id)
      }
      
      switch (this.currentFilter) {
        case 'normal':
          list = list.filter(item => item.status === 'NORMAL')
          break
        case 'near':
          list = list.filter(item => item.status === 'NEAR_EXPIRY')
          break
        case 'low':
          list = list.filter(item => item.status === 'LOW_STOCK')
          break
      }
      
      return list
    },
    nearExpiryCount() {
      return this.inventoryList.filter(item => item.status === 'NEAR_EXPIRY').length
    },
    lowStockCount() {
      return this.inventoryList.filter(item => item.status === 'LOW_STOCK').length
    }
  },
  onShow() {
    this.loadData()
  },
  methods: {
    async loadData() {
      try {
        const [invRes, whRes] = await Promise.all([
          request.get('/api/inventory/list'),
          request.get('/api/warehouses')
        ])
        
        this.inventoryList = invRes.data || []
        this.warehouses = whRes.data || []
      } catch (e) {
        console.error('加载数据失败', e)
      }
    },
    
    setFilter(filter) {
      this.currentFilter = filter
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
    
    formatDate(date) {
      if (!date) return ''
      const d = new Date(date)
      return `${d.getMonth() + 1}/${d.getDate()} ${d.getHours()}:${d.getMinutes().toString().padStart(2, '0')}`
    },
    
    goToDetail(item) {
      uni.navigateTo({ url: `/pages/inventory/detail?id=${item.id}&batchNo=${item.batchNo}` })
    },
    
    goToOutbound(item) {
      uni.setStorageSync('currentBatchNo', item.batchNo)
      uni.navigateTo({ url: '/pages/scan/outbound' })
    }
  }
}
</script>

<style scoped>
.filter-section {
  background-color: #ffffff;
  padding: 20rpx;
  border-radius: 0 0 24rpx 24rpx;
  margin-bottom: 20rpx;
}

.filter-tabs {
  display: flex;
  gap: 15rpx;
  margin-bottom: 20rpx;
}

.filter-tab {
  flex: 1;
  text-align: center;
  padding: 16rpx 0;
  border-radius: 20rpx;
  background-color: #f5f7fa;
  font-size: 26rpx;
  color: #666;
}

.filter-tab.active {
  background-color: #2979FF;
  color: #ffffff;
}

.warehouse-selector {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 20rpx;
  background-color: #f5f7fa;
  border-radius: 12rpx;
}

.stats-bar {
  display: flex;
  background-color: #ffffff;
  border-radius: 16rpx;
  padding: 30rpx 0;
  margin-bottom: 20rpx;
}

.stat-item {
  flex: 1;
  display: flex;
  flex-direction: column;
  align-items: center;
  border-right: 1rpx solid #f0f0f0;
}

.stat-item:last-child {
  border-right: none;
}

.stat-num {
  font-size: 36rpx;
  font-weight: 700;
  color: #333;
}

.stat-label {
  font-size: 24rpx;
  color: #999;
  margin-top: 8rpx;
}

.inventory-card {
  margin-bottom: 20rpx;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  padding-bottom: 20rpx;
  border-bottom: 1rpx solid #f0f0f0;
  margin-bottom: 20rpx;
}

.batch-info {
  display: flex;
  align-items: center;
  gap: 15rpx;
}

.batch-no {
  font-size: 32rpx;
  font-weight: 700;
  color: #333;
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
  display: flex;
  justify-content: flex-end;
}

.action-btn {
  padding: 12rpx 30rpx;
  border-radius: 20rpx;
  font-size: 26rpx;
}

.action-outbound {
  background-color: #FFEBEE;
  color: #F44336;
}

.empty-state {
  text-align: center;
  padding: 100rpx 0;
}
</style>
