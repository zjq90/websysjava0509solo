<template>
  <view class="container">
    <view class="search-bar">
      <input 
        class="search-input" 
        placeholder="搜索订单号..." 
        v-model="searchKeyword"
        @confirm="searchOrders"
      />
      <button class="search-btn" @click="searchOrders">搜索</button>
    </view>

    <view class="status-tabs">
      <view 
        :class="['status-tab', currentStatus === '' ? 'active' : '']" 
        @click="filterByStatus('')"
      >全部</view>
      <view 
        :class="['status-tab', currentStatus === status ? 'active' : '']" 
        v-for="status in statusList" 
        :key="status"
        @click="filterByStatus(status)"
      >{{ getStatusText(status) }}</view>
    </view>

    <view class="fab-btn" @click="createOrder">
      <text class="fab-icon">+</text>
    </view>

    <view v-if="orderList.length > 0">
      <view 
        class="order-card" 
        v-for="order in orderList" 
        :key="order.id"
        @click="goToDetail(order.id)"
      >
        <view class="order-header">
          <text class="order-no">订单号: {{ order.orderNo }}</text>
          <view :class="['status-badge', getStatusClass(order.status)]">
            {{ getStatusText(order.status) }}
          </view>
        </view>
        <view class="order-body">
          <view class="order-info">
            <text class="text-muted">客户：</text>
            <text class="text-primary">{{ order.customerName }}</text>
          </view>
          <view class="order-info">
            <text class="text-muted">商品数：</text>
            <text>{{ order.totalItems }} 件</text>
          </view>
          <view class="order-info">
            <text class="text-muted">订单时间：</text>
            <text>{{ formatDate(order.createTime) }}</text>
          </view>
        </view>
        <view class="order-footer">
          <text class="total-label">订单金额：</text>
          <text class="total-amount">¥{{ order.totalAmount }}</text>
        </view>
      </view>
    </view>
    <view v-else class="empty">暂无订单数据</view>
  </view>
</template>

<script>
import orderApi from '@/api/order.js'

export default {
  data() {
    return {
      searchKeyword: '',
      currentStatus: '',
      statusList: [
        'PENDING_CONFIRMATION',
        'PENDING_SIGNATURE',
        'SIGNED',
        'STOCK_PREPARING',
        'SHIPPED',
        'DELIVERED',
        'COMPLETED'
      ],
      orderList: [],
      allOrders: []
    }
  },
  onShow() {
    this.loadOrders()
  },
  methods: {
    async loadOrders() {
      uni.showLoading({ title: '加载中...' })
      try {
        let res
        if (this.currentStatus) {
          res = await orderApi.getOrdersByStatus(this.currentStatus)
        } else {
          res = await orderApi.getAllOrders()
        }
        this.allOrders = res
        this.orderList = res
      } catch (e) {
        console.error('加载订单失败', e)
      } finally {
        uni.hideLoading()
      }
    },
    searchOrders() {
      if (!this.searchKeyword.trim()) {
        this.orderList = this.allOrders
        return
      }
      const kw = this.searchKeyword.trim().toLowerCase()
      this.orderList = this.allOrders.filter(order => {
        return order.orderNo && order.orderNo.toLowerCase().includes(kw)
      })
    },
    filterByStatus(status) {
      this.currentStatus = status
      this.loadOrders()
    },
    getStatusText(status) {
      const map = {
        'PENDING_CONFIRMATION': '待确认',
        'PENDING_SIGNATURE': '待签署',
        'SIGNED': '已签署',
        'STOCK_PREPARING': '备货中',
        'SHIPPED': '已发货',
        'DELIVERED': '已签收',
        'COMPLETED': '已完成',
        'CANCELLED': '已取消'
      }
      return map[status] || status
    },
    getStatusClass(status) {
      const map = {
        'PENDING_CONFIRMATION': 'status-warning',
        'PENDING_SIGNATURE': 'status-primary',
        'SIGNED': 'status-success',
        'STOCK_PREPARING': 'status-info',
        'SHIPPED': 'status-shipping',
        'DELIVERED': 'status-success',
        'COMPLETED': 'status-success',
        'CANCELLED': 'status-error'
      }
      return map[status] || 'status-default'
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
    createOrder() {
      uni.navigateTo({ url: '/pages/order/create' })
    },
    goToDetail(id) {
      uni.navigateTo({ url: `/pages/order/detail?id=${id}` })
    }
  }
}
</script>

<style scoped>
.search-bar {
  display: flex;
  margin-bottom: 20rpx;
  gap: 16rpx;
}

.search-input {
  flex: 1;
  height: 72rpx;
  background: #fff;
  border-radius: 36rpx;
  padding: 0 24rpx;
  font-size: 28rpx;
  border: 2rpx solid #eee;
}

.search-btn {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: #fff;
  border-radius: 36rpx;
  height: 72rpx;
  line-height: 72rpx;
  padding: 0 32rpx;
  font-size: 28rpx;
  border: none;
}

.status-tabs {
  display: flex;
  flex-wrap: wrap;
  gap: 12rpx;
  margin-bottom: 24rpx;
}

.status-tab {
  padding: 10rpx 20rpx;
  background: #fff;
  border-radius: 24rpx;
  font-size: 24rpx;
  color: #666;
  border: 2rpx solid #eee;
}

.status-tab.active {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: #fff;
  border-color: transparent;
}

.fab-btn {
  position: fixed;
  right: 40rpx;
  bottom: 120rpx;
  width: 112rpx;
  height: 112rpx;
  border-radius: 56rpx;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  display: flex;
  align-items: center;
  justify-content: center;
  box-shadow: 0 8rpx 24rpx rgba(102, 126, 234, 0.4);
  z-index: 100;
}

.fab-icon {
  color: #fff;
  font-size: 56rpx;
  font-weight: 300;
}

.order-card {
  background: #fff;
  border-radius: 16rpx;
  margin-bottom: 20rpx;
  overflow: hidden;
  box-shadow: 0 2rpx 12rpx rgba(0, 0, 0, 0.05);
}

.order-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 24rpx;
  border-bottom: 2rpx solid #f5f5f5;
}

.order-no {
  font-size: 28rpx;
  color: #333;
  font-weight: 500;
}

.status-badge {
  font-size: 22rpx;
  padding: 6rpx 16rpx;
  border-radius: 16rpx;
}

.status-warning { background: #fff8e1; color: #ff8f00; }
.status-primary { background: #e3f2fd; color: #1565c0; }
.status-success { background: #e8f5e9; color: #2e7d32; }
.status-info { background: #f3e5f5; color: #7b1fa2; }
.status-shipping { background: #e0f7fa; color: #00838f; }
.status-error { background: #ffebee; color: #c62828; }
.status-default { background: #f5f5f5; color: #666; }

.order-body {
  padding: 20rpx 24rpx;
}

.order-info {
  font-size: 26rpx;
  margin-bottom: 12rpx;
}

.order-info:last-child {
  margin-bottom: 0;
}

.order-footer {
  display: flex;
  justify-content: flex-end;
  align-items: center;
  padding: 20rpx 24rpx;
  background: #fafafa;
}

.total-label {
  font-size: 26rpx;
  color: #666;
  margin-right: 8rpx;
}

.total-amount {
  font-size: 32rpx;
  font-weight: bold;
  color: #ff4d4f;
}
</style>
