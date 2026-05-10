<template>
  <view class="container">
    <view class="header">
      <view class="header-title">销售订单管理系统</view>
      <view class="header-subtitle">高效管理 · 智能协作</view>
    </view>

    <view class="stats-row">
      <view class="stat-card" @click="goToOrders('PENDING_CONFIRMATION')">
        <view class="stat-value">{{ stats.PENDING_CONFIRMATION || 0 }}</view>
        <view class="stat-label">待确认</view>
      </view>
      <view class="stat-card" @click="goToOrders('PENDING_SIGNATURE')">
        <view class="stat-value">{{ stats.PENDING_SIGNATURE || 0 }}</view>
        <view class="stat-label">待签署</view>
      </view>
      <view class="stat-card" @click="goToOrders('STOCKING')">
        <view class="stat-value">{{ stats.STOCKING || 0 }}</view>
        <view class="stat-label">备货中</view>
      </view>
      <view class="stat-card" @click="goToOrders('SHIPPED')">
        <view class="stat-value">{{ stats.SHIPPED || 0 }}</view>
        <view class="stat-label">已发货</view>
      </view>
    </view>

    <view class="quick-actions">
      <view class="section-title">快捷操作</view>
      <view class="action-grid">
        <view class="action-item" @click="goToCreateOrder">
          <view class="action-icon order">📝</view>
          <view class="action-label">创建订单</view>
        </view>
        <view class="action-item" @click="goToAddCustomer">
          <view class="action-icon customer">👤</view>
          <view class="action-label">添加客户</view>
        </view>
        <view class="action-item" @click="goToProducts">
          <view class="action-icon product">📦</view>
          <view class="action-label">产品列表</view>
        </view>
        <view class="action-item" @click="goToCustomers">
          <view class="action-icon team">👥</view>
          <view class="action-label">客户管理</view>
        </view>
      </view>
    </view>

    <view class="section">
      <view class="section-title">最近订单</view>
      <view v-if="recentOrders.length > 0">
        <view 
          class="order-item card" 
          v-for="order in recentOrders" 
          :key="order.id"
          @click="goToOrderDetail(order.id)"
        >
          <view class="flex-between">
            <view>
              <view class="order-no">{{ order.orderNo }}</view>
              <view class="order-time text-muted">{{ formatDate(order.createdAt) }}</view>
            </view>
            <view :class="['order-status', getStatusClass(order.status)]">
              {{ getStatusText(order.status) }}
            </view>
          </view>
          <view class="order-amount mt-10">
            <text class="text-muted">金额：</text>
            <text class="text-primary">¥{{ order.actualAmount }}</text>
          </view>
        </view>
      </view>
      <view v-else class="empty">暂无订单数据</view>
    </view>
  </view>
</template>

<script>
import orderApi from '@/api/order.js'

export default {
  data() {
    return {
      stats: {},
      recentOrders: []
    }
  },
  onShow() {
    this.loadStatistics()
    this.loadRecentOrders()
  },
  methods: {
    async loadStatistics() {
      try {
        const res = await orderApi.getOrderStatistics()
        this.stats = res
      } catch (e) {
        console.error('加载统计数据失败', e)
      }
    },
    async loadRecentOrders() {
      try {
        const res = await orderApi.getAllOrders()
        this.recentOrders = res.slice(0, 5)
      } catch (e) {
        console.error('加载订单失败', e)
      }
    },
    getStatusClass(status) {
      const map = {
        'PENDING_CONFIRMATION': 'status-pending',
        'PENDING_SIGNATURE': 'status-warning',
        'SIGNED': 'status-info',
        'STOCKING': 'status-info',
        'SHIPPED': 'status-primary',
        'DELIVERED': 'status-success',
        'COMPLETED': 'status-success',
        'CANCELLED': 'status-danger'
      }
      return map[status] || 'status-pending'
    },
    getStatusText(status) {
      const map = {
        'PENDING_CONFIRMATION': '待确认',
        'PENDING_SIGNATURE': '待签署',
        'SIGNED': '已签署',
        'STOCKING': '备货中',
        'SHIPPED': '已发货',
        'DELIVERED': '已签收',
        'COMPLETED': '已完成',
        'CANCELLED': '已取消'
      }
      return map[status] || status
    },
    formatDate(dateStr) {
      if (!dateStr) return ''
      const date = new Date(dateStr)
      const month = date.getMonth() + 1
      const day = date.getDate()
      const hour = date.getHours().toString().padStart(2, '0')
      const min = date.getMinutes().toString().padStart(2, '0')
      return `${month}-${day} ${hour}:${min}`
    },
    goToCreateOrder() {
      uni.navigateTo({ url: '/pages/order/create' })
    },
    goToAddCustomer() {
      uni.navigateTo({ url: '/pages/customer/add' })
    },
    goToProducts() {
      uni.switchTab({ url: '/pages/product/list' })
    },
    goToCustomers() {
      uni.switchTab({ url: '/pages/customer/list' })
    },
    goToOrderDetail(id) {
      uni.navigateTo({ url: `/pages/order/detail?id=${id}` })
    },
    goToOrders(status) {
      uni.navigateTo({ url: `/pages/order/list?status=${status}` })
    }
  }
}
</script>

<style scoped>
.header {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  padding: 40rpx 30rpx;
  border-radius: 24rpx;
  margin-bottom: 30rpx;
}

.header-title {
  font-size: 40rpx;
  font-weight: bold;
  color: #fff;
}

.header-subtitle {
  font-size: 26rpx;
  color: rgba(255, 255, 255, 0.8);
  margin-top: 8rpx;
}

.stats-row {
  display: flex;
  justify-content: space-between;
  margin-bottom: 30rpx;
}

.stat-card {
  flex: 1;
  background: #fff;
  border-radius: 16rpx;
  padding: 24rpx 10rpx;
  margin: 0 6rpx;
  text-align: center;
  box-shadow: 0 2rpx 12rpx rgba(0, 0, 0, 0.05);
}

.stat-value {
  font-size: 40rpx;
  font-weight: bold;
  color: #667eea;
}

.stat-label {
  font-size: 24rpx;
  color: #999;
  margin-top: 6rpx;
}

.quick-actions {
  background: #fff;
  border-radius: 16rpx;
  padding: 24rpx;
  margin-bottom: 30rpx;
  box-shadow: 0 2rpx 12rpx rgba(0, 0, 0, 0.05);
}

.section-title {
  font-size: 32rpx;
  font-weight: bold;
  color: #333;
  margin-bottom: 24rpx;
}

.action-grid {
  display: flex;
  flex-wrap: wrap;
}

.action-item {
  width: 25%;
  text-align: center;
  padding: 16rpx 0;
}

.action-icon {
  width: 96rpx;
  height: 96rpx;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  margin: 0 auto 12rpx;
  font-size: 40rpx;
}

.action-icon.order { background: #e8f0fe; }
.action-icon.customer { background: #e6fffb; }
.action-icon.product { background: #fff7e6; }
.action-icon.team { background: #f9f0ff; }

.action-label {
  font-size: 26rpx;
  color: #666;
}

.section {
  margin-bottom: 30rpx;
}

.order-item {
  padding: 24rpx;
}

.order-no {
  font-size: 30rpx;
  font-weight: 500;
  color: #333;
}

.order-time {
  font-size: 24rpx;
  margin-top: 6rpx;
}

.order-amount {
  font-size: 28rpx;
}

.order-amount .text-primary {
  font-weight: 500;
}

.order-status {
  font-size: 24rpx;
  padding: 6rpx 16rpx;
  border-radius: 20rpx;
}

.status-pending { background: #fff3e0; color: #e65100; }
.status-warning { background: #fff8e1; color: #ff8f00; }
.status-info { background: #e3f2fd; color: #1565c0; }
.status-primary { background: #e8f5e9; color: #2e7d32; }
.status-success { background: #e8f5e9; color: #2e7d32; }
.status-danger { background: #ffebee; color: #c62828; }
</style>
