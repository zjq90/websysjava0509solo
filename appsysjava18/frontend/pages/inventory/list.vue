<template>
  <view class="container">
    <view class="search-bar">
      <view class="search-input-wrapper">
        <text class="search-icon">🔍</text>
        <input 
          class="search-input" 
          type="text" 
          v-model="searchKeyword" 
          placeholder="搜索种子名称或批次号"
          placeholder-class="placeholder"
          @confirm="loadData"
        />
      </view>
      <view class="search-btn" @click="loadData">搜索</view>
    </view>

    <view class="stats-row">
      <view class="stat-item">
        <text class="stat-value">{{ inventoryList.length }}</text>
        <text class="stat-label">库存记录</text>
      </view>
      <view class="stat-item">
        <text class="stat-value">{{ totalQuantity }}</text>
        <text class="stat-label">总数量</text>
      </view>
    </view>

    <view class="list-section" v-if="inventoryList.length > 0">
      <view 
        class="inventory-card" 
        v-for="item in inventoryList" 
        :key="item.id"
        @click="showDetail(item)"
      >
        <view class="card-header">
          <view class="seed-info">
            <text class="seed-name">{{ item.seedName || '未知品种' }}</text>
            <text class="batch-number">{{ item.batchNo }}</text>
          </view>
          <view class="status-badge" :class="getStatusClass(item)">
            {{ getStatusText(item) }}
          </view>
        </view>
        <view class="card-body">
          <view class="info-row">
            <text class="info-label">入库数量</text>
            <text class="info-value">{{ item.quantity }} 公斤</text>
          </view>
          <view class="info-row">
            <text class="info-label">入库日期</text>
            <text class="info-value">{{ formatDate(item.createdTime) }}</text>
          </view>
          <view class="info-row">
            <text class="info-label">保质期</text>
            <text class="info-value">{{ item.expiryDate || '-' }}</text>
          </view>
        </view>
        <view class="card-actions">
          <view class="action-btn edit" @click.stop="editItem(item)">编辑</view>
          <view class="action-btn delete" @click.stop="deleteItem(item)">删除</view>
        </view>
      </view>
    </view>

    <view class="empty-state" v-else>
      <text class="empty-icon">📦</text>
      <text class="empty-text">暂无库存记录</text>
    </view>

    <view class="add-btn" @click="addInventory">
      <text class="add-icon">+</text>
    </view>
  </view>
</template>

<script>
import { getInventoryList, deleteInventory } from '@/api/inventory'

export default {
  data() {
    return {
      searchKeyword: '',
      inventoryList: []
    }
  },

  computed: {
    totalQuantity() {
      return this.inventoryList.reduce((sum, item) => sum + (item.quantity || 0), 0)
    }
  },

  onShow() {
    this.loadData()
  },

  methods: {
    // 加载数据
    async loadData() {
      try {
        const params = {
          page: 0,
          size: 100
        }
        const res = await getInventoryList(params)
        this.inventoryList = res.content || []
      } catch (e) {
        console.error('加载库存列表失败', e)
      }
    },

    // 格式化日期
    formatDate(dateStr) {
      if (!dateStr) return '-'
      const date = new Date(dateStr)
      const year = date.getFullYear()
      const month = String(date.getMonth() + 1).padStart(2, '0')
      const day = String(date.getDate()).padStart(2, '0')
      return `${year}-${month}-${day}`
    },

    // 获取状态样式类
    getStatusClass(item) {
      const shelfLife = item.shelfLife
      if (!shelfLife) return 'status-normal'
      
      const shelfDate = new Date(shelfLife)
      const now = new Date()
      const diffMonths = (shelfDate - now) / (1000 * 60 * 60 * 24 * 30)
      
      if (diffMonths < 0) return 'status-expired'
      if (diffMonths < 3) return 'status-warning'
      return 'status-normal'
    },

    // 获取状态文本
    getStatusText(item) {
      const statusClass = this.getStatusClass(item)
      switch (statusClass) {
        case 'status-expired':
          return '已过期'
        case 'status-warning':
          return '即将过期'
        default:
          return '正常'
      }
    },

    // 显示详情
    showDetail(item) {
      uni.showModal({
        title: item.seedName || '库存详情',
        content: `
          批次号: ${item.batchNumber}
          数量: ${item.quantity} 公斤
          入库日期: ${this.formatDate(item.createdTime)}
          保质期: ${item.shelfLife || '-'}
          备注: ${item.remark || '无'}
        `,
        showCancel: false
      })
    },

    // 新增库存
    addInventory() {
      uni.navigateTo({
        url: '/pages/inventory/form'
      })
    },

    // 编辑库存
    editItem(item) {
      uni.navigateTo({
        url: '/pages/inventory/form?id=' + item.id
      })
    },

    // 删除库存
    deleteItem(item) {
      uni.showModal({
        title: '提示',
        content: '确定要删除该库存记录吗？',
        success: async (res) => {
          if (res.confirm) {
            try {
              await deleteInventory(item.id)
              uni.showToast({ title: '删除成功', icon: 'success' })
              this.loadData()
            } catch (e) {
              console.error('删除失败', e)
            }
          }
        }
      })
    }
  }
}
</script>

<style scoped>
.container {
  padding: 30rpx;
  padding-bottom: 150rpx;
  min-height: 100vh;
  background: #F5F5F5;
}

.search-bar {
  display: flex;
  gap: 20rpx;
  margin-bottom: 30rpx;
}

.search-input-wrapper {
  flex: 1;
  display: flex;
  align-items: center;
  background: #FFFFFF;
  border-radius: 16rpx;
  padding: 0 24rpx;
  height: 88rpx;
}

.search-icon {
  font-size: 32rpx;
  margin-right: 16rpx;
}

.search-input {
  flex: 1;
  font-size: 28rpx;
}

.placeholder {
  color: #999999;
}

.search-btn {
  background: #007AFF;
  color: #FFFFFF;
  padding: 0 30rpx;
  border-radius: 16rpx;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 28rpx;
}

.stats-row {
  display: flex;
  gap: 20rpx;
  margin-bottom: 30rpx;
}

.stat-item {
  flex: 1;
  background: #FFFFFF;
  border-radius: 20rpx;
  padding: 30rpx;
  text-align: center;
  box-shadow: 0 4rpx 12rpx rgba(0, 0, 0, 0.05);
}

.stat-value {
  display: block;
  font-size: 44rpx;
  font-weight: 600;
  color: #007AFF;
  margin-bottom: 8rpx;
}

.stat-label {
  font-size: 26rpx;
  color: #999999;
}

.inventory-card {
  background: #FFFFFF;
  border-radius: 20rpx;
  padding: 30rpx;
  margin-bottom: 20rpx;
  box-shadow: 0 4rpx 12rpx rgba(0, 0, 0, 0.05);
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  margin-bottom: 20rpx;
}

.seed-info {
  display: flex;
  flex-direction: column;
}

.seed-name {
  font-size: 32rpx;
  font-weight: 600;
  color: #333333;
  margin-bottom: 6rpx;
}

.batch-number {
  font-size: 24rpx;
  color: #999999;
}

.status-badge {
  padding: 6rpx 16rpx;
  border-radius: 8rpx;
  font-size: 24rpx;
}

.status-normal {
  background: #34C7591A;
  color: #34C759;
}

.status-warning {
  background: #FF95001A;
  color: #FF9500;
}

.status-expired {
  background: #FF3B301A;
  color: #FF3B30;
}

.card-body {
  background: #F5F5F5;
  border-radius: 12rpx;
  padding: 20rpx;
  margin-bottom: 20rpx;
}

.info-row {
  display: flex;
  justify-content: space-between;
  padding: 10rpx 0;
}

.info-label {
  font-size: 26rpx;
  color: #999999;
}

.info-value {
  font-size: 26rpx;
  color: #333333;
}

.card-actions {
  display: flex;
  gap: 20rpx;
}

.action-btn {
  flex: 1;
  padding: 20rpx;
  border-radius: 12rpx;
  text-align: center;
  font-size: 28rpx;
}

.action-btn.edit {
  background: #007AFF1A;
  color: #007AFF;
}

.action-btn.delete {
  background: #FF3B301A;
  color: #FF3B30;
}

.empty-state {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 100rpx 0;
}

.empty-icon {
  font-size: 100rpx;
  margin-bottom: 20rpx;
}

.empty-text {
  font-size: 28rpx;
  color: #999999;
}

.add-btn {
  position: fixed;
  right: 40rpx;
  bottom: 60rpx;
  width: 110rpx;
  height: 110rpx;
  background: linear-gradient(135deg, #007AFF 0%, #34C759 100%);
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  box-shadow: 0 8rpx 24rpx rgba(0, 122, 255, 0.4);
}

.add-icon {
  font-size: 60rpx;
  color: #FFFFFF;
}
</style>
