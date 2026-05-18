<template>
  <view class="container">
    <view class="tabs">
      <view 
        class="tab-item" 
        :class="{ active: currentTab === tab.value }" 
        v-for="tab in tabs" 
        :key="tab.value"
        @click="changeTab(tab.value)"
      >
        {{ tab.label }}
      </view>
    </view>

    <view class="order-list" v-if="orders.length > 0">
      <view class="order-item" v-for="order in orders" :key="order.id" @click="goToDetail(order.orderNo)">
        <view class="order-header">
          <text class="order-no">订单号：{{ order.orderNo }}</text>
          <text class="order-status" :class="getStatusClass(order.status)">{{ getStatusText(order.status) }}</text>
        </view>
        <view class="order-content">
          <image :src="order.product ? (order.product.images ? order.product.images.split(',')[0] : '/static/placeholder.png') : '/static/placeholder.png'" class="product-image" mode="aspectFill" />
          <view class="product-info">
            <text class="product-title">{{ order.product ? order.product.title : '商品信息' }}</text>
            <view class="product-price">
              <text class="price-symbol">¥</text>
              <text class="price-value">{{ order.amount }}</text>
            </view>
          </view>
        </view>
        <view class="order-footer">
          <text class="create-time">{{ formatTime(order.createTime) }}</text>
          <view class="order-actions">
            <button class="action-btn" v-if="order.status === 'PENDING'" @click.stop="cancelOrder(order.orderNo)">取消</button>
            <button class="action-btn primary" v-if="order.status === 'PENDING'" @click.stop="payOrder(order.orderNo)">支付</button>
            <button class="action-btn primary" v-if="order.status === 'PAID' || order.status === 'SHIPPED'" @click.stop="confirmReceive(order.orderNo)">确认收货</button>
            <button class="action-btn" v-if="order.status === 'PAID' || order.status === 'SHIPPED'" @click.stop="applyRefund(order.orderNo)">申请退款</button>
          </view>
        </view>
      </view>
    </view>

    <view class="empty-state" v-else>
      <text class="empty-icon">📋</text>
      <text class="empty-text">暂无订单</text>
      <button class="go-shopping-btn" @click="goShopping">去逛逛</button>
    </view>
  </view>
</template>

<script>
export default {
  data() {
    return {
      currentTab: 'all',
      tabs: [
        { label: '全部', value: 'all' },
        { label: '待支付', value: 'PENDING' },
        { label: '待发货', value: 'PAID' },
        { label: '待收货', value: 'SHIPPED' },
        { label: '已完成', value: 'COMPLETED' }
      ],
      orders: []
    }
  },
  onLoad() {
    this.loadOrders()
  },
  onShow() {
    this.loadOrders()
  },
  methods: {
    changeTab(tab) {
      this.currentTab = tab
      this.loadOrders()
    },
    async loadOrders() {
      uni.showLoading({
        title: '加载中...'
      })

      try {
        const res = await this.$request({
          url: '/order/my'
        })
        
        if (this.currentTab === 'all') {
          this.orders = res.data
        } else {
          this.orders = res.data.filter(order => order.status === this.currentTab)
        }
      } catch (e) {
        console.error(e)
      } finally {
        uni.hideLoading()
      }
    },
    getStatusText(status) {
      const statusMap = {
        'PENDING': '待支付',
        'PAID': '已支付',
        'SHIPPED': '已发货',
        'COMPLETED': '已完成',
        'CANCELLED': '已取消',
        'REFUNDING': '退款中',
        'REFUNDED': '已退款'
      }
      return statusMap[status] || status
    },
    getStatusClass(status) {
      const classMap = {
        'PENDING': 'status-pending',
        'PAID': 'status-paid',
        'SHIPPED': 'status-shipped',
        'COMPLETED': 'status-completed',
        'CANCELLED': 'status-cancelled',
        'REFUNDING': 'status-refunding',
        'REFUNDED': 'status-refunded'
      }
      return classMap[status] || ''
    },
    formatTime(time) {
      if (!time) return ''
      const date = new Date(time)
      const year = date.getFullYear()
      const month = String(date.getMonth() + 1).padStart(2, '0')
      const day = String(date.getDate()).padStart(2, '0')
      const hour = String(date.getHours()).padStart(2, '0')
      const minute = String(date.getMinutes()).padStart(2, '0')
      return `${year}-${month}-${day} ${hour}:${minute}`
    },
    goToDetail(orderNo) {
      uni.navigateTo({
        url: `/pages/order/detail?orderNo=${orderNo}`
      })
    },
    async payOrder(orderNo) {
      try {
        await this.$request({
          url: `/order/pay/${orderNo}`,
          method: 'POST'
        })
        uni.showToast({
          title: '支付成功',
          icon: 'success'
        })
        this.loadOrders()
      } catch (e) {
        console.error(e)
      }
    },
    async cancelOrder(orderNo) {
      uni.showModal({
        title: '提示',
        content: '确认取消该订单吗？',
        success: async (res) => {
          if (res.confirm) {
            try {
              await this.$request({
                url: `/order/cancel/${orderNo}`,
                method: 'POST'
              })
              uni.showToast({
                title: '取消成功',
                icon: 'success'
              })
              this.loadOrders()
            } catch (e) {
              console.error(e)
            }
          }
        }
      })
    },
    async confirmReceive(orderNo) {
      uni.showModal({
        title: '提示',
        content: '确认收到商品吗？',
        success: async (res) => {
          if (res.confirm) {
            try {
              await this.$request({
                url: `/order/confirm/${orderNo}`,
                method: 'POST'
              })
              uni.showToast({
                title: '确认成功',
                icon: 'success'
              })
              this.loadOrders()
            } catch (e) {
              console.error(e)
            }
          }
        }
      })
    },
    async applyRefund(orderNo) {
      uni.showModal({
        title: '提示',
        content: '确认申请退款吗？',
        success: async (res) => {
          if (res.confirm) {
            try {
              await this.$request({
                url: `/order/refund/${orderNo}`,
                method: 'POST'
              })
              uni.showToast({
                title: '申请成功',
                icon: 'success'
              })
              this.loadOrders()
            } catch (e) {
              console.error(e)
            }
          }
        }
      })
    },
    goShopping() {
      uni.switchTab({
        url: '/pages/index/index'
      })
    }
  }
}
</script>

<style scoped>
.container {
  min-height: 100vh;
  background: #f5f5f5;
}

.tabs {
  display: flex;
  background: #fff;
  padding: 0 20rpx;
  overflow-x: auto;
}

.tab-item {
  flex-shrink: 0;
  padding: 30rpx 30rpx;
  font-size: 28rpx;
  color: #666;
  position: relative;
}

.tab-item.active {
  color: #ff4d4f;
  font-weight: bold;
}

.tab-item.active::after {
  content: '';
  position: absolute;
  bottom: 0;
  left: 50%;
  transform: translateX(-50%);
  width: 40rpx;
  height: 4rpx;
  background: #ff4d4f;
  border-radius: 2rpx;
}

.order-list {
  padding: 20rpx;
}

.order-item {
  background: #fff;
  border-radius: 16rpx;
  margin-bottom: 20rpx;
  overflow: hidden;
}

.order-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 20rpx 30rpx;
  border-bottom: 1rpx solid #f0f0f0;
}

.order-no {
  font-size: 24rpx;
  color: #999;
}

.order-status {
  font-size: 26rpx;
}

.status-pending {
  color: #ff9800;
}

.status-paid {
  color: #409eff;
}

.status-shipped {
  color: #9c27b0;
}

.status-completed {
  color: #4caf50;
}

.status-cancelled {
  color: #999;
}

.status-refunding {
  color: #ff5722;
}

.status-refunded {
  color: #673ab7;
}

.order-content {
  display: flex;
  padding: 30rpx;
}

.product-image {
  width: 160rpx;
  height: 160rpx;
  border-radius: 12rpx;
  flex-shrink: 0;
}

.product-info {
  flex: 1;
  margin-left: 20rpx;
  display: flex;
  flex-direction: column;
  justify-content: space-between;
}

.product-title {
  font-size: 28rpx;
  color: #333;
  display: -webkit-box;
  -webkit-box-orient: vertical;
  -webkit-line-clamp: 2;
  overflow: hidden;
  line-height: 1.4;
}

.product-price {
  display: flex;
  align-items: baseline;
}

.price-symbol {
  font-size: 24rpx;
  color: #ff4d4f;
}

.price-value {
  font-size: 32rpx;
  font-weight: bold;
  color: #ff4d4f;
}

.order-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 20rpx 30rpx;
  border-top: 1rpx solid #f0f0f0;
}

.create-time {
  font-size: 22rpx;
  color: #999;
}

.order-actions {
  display: flex;
  gap: 20rpx;
}

.action-btn {
  height: 60rpx;
  line-height: 60rpx;
  padding: 0 30rpx;
  background: #f5f5f5;
  color: #666;
  border-radius: 30rpx;
  font-size: 24rpx;
  border: none;
}

.action-btn.primary {
  background: linear-gradient(135deg, #ff6b6b 0%, #ff4757 100%);
  color: #fff;
}

.empty-state {
  padding: 150rpx 0;
  text-align: center;
}

.empty-icon {
  display: block;
  font-size: 120rpx;
  margin-bottom: 30rpx;
}

.empty-text {
  display: block;
  font-size: 32rpx;
  color: #666;
  margin-bottom: 40rpx;
}

.go-shopping-btn {
  width: 240rpx;
  height: 70rpx;
  line-height: 70rpx;
  background: linear-gradient(135deg, #ff6b6b 0%, #ff4757 100%);
  color: #fff;
  border-radius: 35rpx;
  font-size: 28rpx;
  border: none;
}
</style>
