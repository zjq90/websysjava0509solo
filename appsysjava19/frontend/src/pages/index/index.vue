<template>
  <view class="container">
    <view class="header-card">
      <view class="header-title">
        <text class="fs-36 fw-bold" style="color: #2979FF;">种子库存管理</text>
        <text class="fs-24 text-muted mt-10">智能移动仓储解决方案</text>
      </view>
      <view class="unread-badge" v-if="unreadCount > 0">
        <text class="fs-24">{{ unreadCount }}</text>
      </view>
    </view>
    
    <view class="stats-row">
      <view class="stat-card" @click="goToInventory">
        <view class="stat-icon" style="background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);">
          <text class="stat-emoji">📦</text>
        </view>
        <view class="stat-info">
          <text class="stat-value">{{ stats.totalInventory }}</text>
          <text class="stat-label">库存批次</text>
        </view>
      </view>
      
      <view class="stat-card" @click="goToWarehouses">
        <view class="stat-icon" style="background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);">
          <text class="stat-emoji">🏭</text>
        </view>
        <view class="stat-info">
          <text class="stat-value">{{ stats.totalWarehouses }}</text>
          <text class="stat-label">仓库数量</text>
        </view>
      </view>
    </view>
    
    <view class="stats-row">
      <view class="stat-card" style="border-left: 4rpx solid #FF9800;" @click="goToNotifications">
        <view class="stat-icon" style="background: linear-gradient(135deg, #fa709a 0%, #fee140 100%);">
          <text class="stat-emoji">⚠️</text>
        </view>
        <view class="stat-info">
          <text class="stat-value text-warning">{{ stats.nearExpiry }}</text>
          <text class="stat-label">近效期</text>
        </view>
      </view>
      
      <view class="stat-card" style="border-left: 4rpx solid #F44336;" @click="goToNotifications">
        <view class="stat-icon" style="background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);">
          <text class="stat-emoji">📉</text>
        </view>
        <view class="stat-info">
          <text class="stat-value text-danger">{{ stats.lowStock }}</text>
          <text class="stat-label">缺货</text>
        </view>
      </view>
    </view>
    
    <view class="quick-actions card">
      <view class="section-title">
        <text class="fs-32 fw-bold">快捷操作</text>
      </view>
      <view class="action-grid">
        <view class="action-item" @click="goToScan">
          <view class="action-icon" style="background-color: #E3F2FD;">
            <text class="emoji">📷</text>
          </view>
          <text class="action-label">扫码查询</text>
        </view>
        <view class="action-item" @click="goToInbound">
          <view class="action-icon" style="background-color: #E8F5E9;">
            <text class="emoji">📥</text>
          </view>
          <text class="action-label">入库登记</text>
        </view>
        <view class="action-item" @click="goToOutbound">
          <view class="action-icon" style="background-color: #FFF3E0;">
            <text class="emoji">📤</text>
          </view>
          <text class="action-label">出库登记</text>
        </view>
        <view class="action-item" @click="goToInventory">
          <view class="action-icon" style="background-color: #F3E5F5;">
            <text class="emoji">📊</text>
          </view>
          <text class="action-label">库存查询</text>
        </view>
      </view>
    </view>
    
    <view class="warehouse-selector card">
      <view class="section-title">
        <text class="fs-32 fw-bold">当前仓库</text>
        <text class="fs-28 text-primary" @click="goToWarehouses">切换 >></text>
      </view>
      <view class="warehouse-info" v-if="currentWarehouse">
        <text class="fs-32 fw-bold">{{ currentWarehouse.warehouseName }}</text>
        <text class="fs-26 text-muted mt-10">{{ currentWarehouse.address }}</text>
        <view class="flex-between mt-20">
          <text class="fs-24">管理员：{{ currentWarehouse.manager }}</text>
          <view class="badge-success">
            <text>{{ currentWarehouse.status === 'ACTIVE' ? '正常' : '停用' }}</text>
          </view>
        </view>
      </view>
    </view>
    
    <view class="recent-operations card">
      <view class="section-title">
        <text class="fs-32 fw-bold">最近消息</text>
      </view>
      <view class="operation-list" v-if="recentNotifications.length > 0">
        <view class="operation-item" v-for="item in recentNotifications" :key="item.id" @click="goToNotifications">
          <view class="operation-icon" :class="getNotificationIconClass(item.notificationType)">
            <text class="emoji">{{ getNotificationEmoji(item.notificationType) }}</text>
          </view>
          <view class="operation-content">
            <text class="fs-30 fw-bold">{{ item.title }}</text>
            <text class="fs-24 text-muted mt-5">{{ item.content }}</text>
          </view>
          <text class="fs-22 text-muted">{{ formatTime(item.createTime) }}</text>
        </view>
      </view>
      <view class="empty-state" v-else>
        <text class="text-muted fs-28">暂无新消息</text>
      </view>
    </view>
  </view>
</template>

<script>
import request from '@/utils/request.js'

export default {
  data() {
    return {
      unreadCount: 0,
      stats: {
        totalInventory: 0,
        totalWarehouses: 0,
        nearExpiry: 0,
        lowStock: 0
      },
      currentWarehouse: null,
      warehouses: [],
      recentNotifications: []
    }
  },
  onShow() {
    this.loadData()
  },
  methods: {
    async loadData() {
      await Promise.all([
        this.loadStats(),
        this.loadWarehouses(),
        this.loadNotifications()
      ])
    },
    
    async loadStats() {
      try {
        const inventoryRes = await request.get('/api/inventory/list')
        const inventoryList = inventoryRes.data || []
        
        this.stats.totalInventory = inventoryList.length
        this.stats.nearExpiry = inventoryList.filter(i => i.status === 'NEAR_EXPIRY').length
        this.stats.lowStock = inventoryList.filter(i => i.status === 'LOW_STOCK').length
      } catch (e) {
        console.error('加载统计失败', e)
      }
    },
    
    async loadWarehouses() {
      try {
        const res = await request.get('/api/warehouses')
        this.warehouses = res.data || []
        this.stats.totalWarehouses = this.warehouses.filter(w => w.status === 'ACTIVE').length
        
        if (this.warehouses.length > 0) {
          const savedId = uni.getStorageSync('currentWarehouseId')
          if (savedId) {
            this.currentWarehouse = this.warehouses.find(w => w.id === savedId) || this.warehouses[0]
          } else {
            this.currentWarehouse = this.warehouses[0]
          }
          uni.setStorageSync('currentWarehouseId', this.currentWarehouse.id)
        }
      } catch (e) {
        console.error('加载仓库失败', e)
      }
    },
    
    async loadNotifications() {
      try {
        const res = await request.get('/api/notifications')
        const allNotifications = res.data || []
        this.recentNotifications = allNotifications.slice(0, 3)
        this.unreadCount = allNotifications.filter(n => n.status === 'UNREAD').length
      } catch (e) {
        console.error('加载通知失败', e)
      }
    },
    
    getNotificationIconClass(type) {
      const map = {
        'NEAR_EXPIRY': 'warning',
        'LOW_STOCK': 'danger',
        'SYSTEM': 'info'
      }
      return map[type] || 'info'
    },
    
    getNotificationEmoji(type) {
      const map = {
        'NEAR_EXPIRY': '⚠️',
        'LOW_STOCK': '📉',
        'SYSTEM': '🔔'
      }
      return map[type] || '📢'
    },
    
    formatTime(time) {
      if (!time) return ''
      const date = new Date(time)
      const now = new Date()
      const diff = now - date
      
      if (diff < 60000) return '刚刚'
      if (diff < 3600000) return Math.floor(diff / 60000) + '分钟前'
      if (diff < 86400000) return Math.floor(diff / 3600000) + '小时前'
      
      const month = date.getMonth() + 1
      const day = date.getDate()
      const hours = date.getHours().toString().padStart(2, '0')
      const minutes = date.getMinutes().toString().padStart(2, '0')
      return `${month}/${day} ${hours}:${minutes}`
    },
    
    goToScan() {
      uni.navigateTo({ url: '/pages/scan/scan' })
    },
    
    goToInbound() {
      uni.navigateTo({ url: '/pages/scan/inbound' })
    },
    
    goToOutbound() {
      uni.navigateTo({ url: '/pages/scan/outbound' })
    },
    
    goToInventory() {
      uni.switchTab({ url: '/pages/inventory/list' })
    },
    
    goToWarehouses() {
      uni.navigateTo({ url: '/pages/warehouse/list' })
    },
    
    goToNotifications() {
      uni.switchTab({ url: '/pages/notifications/list' })
    }
  }
}
</script>

<style scoped>
.header-card {
  background: linear-gradient(135deg, #2979FF 0%, #667eea 100%);
  border-radius: 24rpx;
  padding: 40rpx 30rpx;
  margin-bottom: 30rpx;
  position: relative;
}

.header-title .fs-36 {
  color: #ffffff !important;
}

.header-title .text-muted {
  color: rgba(255, 255, 255, 0.8);
}

.unread-badge {
  position: absolute;
  top: 20rpx;
  right: 30rpx;
  background-color: #FF5252;
  color: #ffffff;
  border-radius: 30rpx;
  padding: 8rpx 20rpx;
  min-width: 40rpx;
  text-align: center;
}

.stats-row {
  display: flex;
  gap: 20rpx;
  margin-bottom: 20rpx;
}

.stat-card {
  flex: 1;
  background-color: #ffffff;
  border-radius: 20rpx;
  padding: 30rpx;
  box-shadow: 0 4rpx 16rpx rgba(0, 0, 0, 0.06);
}

.stat-icon {
  width: 80rpx;
  height: 80rpx;
  border-radius: 20rpx;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-bottom: 20rpx;
}

.stat-emoji {
  font-size: 40rpx;
}

.stat-value {
  font-size: 40rpx;
  font-weight: 700;
  color: #333;
}

.stat-label {
  font-size: 24rpx;
  color: #999;
  margin-top: 8rpx;
  display: block;
}

.section-title {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 30rpx;
}

.quick-actions {
  margin-bottom: 20rpx;
}

.action-grid {
  display: flex;
  flex-wrap: wrap;
}

.action-item {
  width: 25%;
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 20rpx 0;
}

.action-icon {
  width: 100rpx;
  height: 100rpx;
  border-radius: 24rpx;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-bottom: 16rpx;
}

.action-icon .emoji {
  font-size: 44rpx;
}

.action-label {
  font-size: 24rpx;
  color: #666;
}

.warehouse-info {
  display: flex;
  flex-direction: column;
}

.warehouse-info .mt-10 {
  margin-top: 10rpx;
}

.operation-list {
  display: flex;
  flex-direction: column;
  gap: 20rpx;
}

.operation-item {
  display: flex;
  align-items: flex-start;
  padding: 20rpx 0;
  border-bottom: 1rpx solid #f0f0f0;
}

.operation-item:last-child {
  border-bottom: none;
}

.operation-icon {
  width: 80rpx;
  height: 80rpx;
  border-radius: 20rpx;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-right: 20rpx;
  flex-shrink: 0;
}

.operation-icon.warning {
  background-color: #FFF3E0;
}

.operation-icon.danger {
  background-color: #FFEBEE;
}

.operation-icon.info {
  background-color: #E3F2FD;
}

.operation-icon .emoji {
  font-size: 36rpx;
}

.operation-content {
  flex: 1;
  display: flex;
  flex-direction: column;
  min-width: 0;
}

.operation-content .mt-5 {
  margin-top: 5rpx;
}

.operation-content .fs-24 {
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
  max-width: 400rpx;
}

.empty-state {
  text-align: center;
  padding: 40rpx 0;
}
</style>
