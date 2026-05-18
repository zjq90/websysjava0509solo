<template>
  <view class="container">
    <view class="status-card" v-if="order">
      <text class="status-icon">{{ getStatusIcon(order.status) }}</text>
      <text class="status-text">{{ getStatusText(order.status) }}</text>
      <text class="status-desc" v-if="order.status === 'PENDING'">请在30分钟内完成支付</text>
    </view>

    <view class="product-card" v-if="order && order.product">
      <image :src="order.product.images ? order.product.images.split(',')[0] : '/static/placeholder.png'" class="product-image" mode="aspectFill" />
      <view class="product-info">
        <text class="product-title">{{ order.product.title }}</text>
        <view class="product-price">
          <text class="price-symbol">¥</text>
          <text class="price-value">{{ order.amount }}</text>
        </view>
      </view>
    </view>

    <view class="info-card" v-if="order">
      <view class="info-item">
        <text class="label">订单编号</text>
        <text class="value">{{ order.orderNo }}</text>
      </view>
      <view class="info-item">
        <text class="label">创建时间</text>
        <text class="value">{{ formatTime(order.createTime) }}</text>
      </view>
      <view class="info-item" v-if="order.paymentTime">
        <text class="label">支付时间</text>
        <text class="value">{{ formatTime(order.paymentTime) }}</text>
      </view>
      <view class="info-item" v-if="order.address">
        <text class="label">收货地址</text>
        <text class="value">{{ order.address }}</text>
      </view>
    </view>

    <view class="bottom-bar" v-if="order">
      <button class="action-btn" v-if="order.status === 'PENDING'" @click="cancelOrder">取消订单</button>
      <button class="action-btn primary" v-if="order.status === 'PENDING'" @click="payOrder">立即支付</button>
      <button class="action-btn primary" v-if="order.status === 'PAID' || order.status === 'SHIPPED'" @click="confirmReceive">确认收货</button>
      <button class="action-btn" v-if="order.status === 'PAID' || order.status === 'SHIPPED'" @click="applyRefund">申请退款</button>
      <button class="action-btn primary" v-if="order.status === 'COMPLETED'" @click="goToDetail">再次购买</button>
    </view>
  </view>
</template>

<script>
export default {
  data() {
    return {
      orderNo: '',
      order: null
    }
  },
  onLoad(options) {
    this.orderNo = options.orderNo
    this.loadOrderDetail()
  },
  methods: {
    async loadOrderDetail() {
      uni.showLoading({
        title: '加载中...'
      })

      try {
        const res = await this.$request({
          url: `/order/${this.orderNo}`
        })
        this.order = res.data
      } catch (e) {
        console.error(e)
      } finally {
        uni.hideLoading()
      }
    },
    getStatusIcon(status) {
      const iconMap = {
        'PENDING': '⏳',
        'PAID': '💳',
        'SHIPPED': '🚚',
        'COMPLETED': '✅',
        'CANCELLED': '❌',
        'REFUNDING': '🔄',
        'REFUNDED': '💰'
      }
      return iconMap[status] || '📦'
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
    async payOrder() {
      try {
        await this.$request({
          url: `/order/pay/${this.orderNo}`,
          method: 'POST'
        })
        uni.showToast({
          title: '支付成功',
          icon: 'success'
        })
        this.loadOrderDetail()
      } catch (e) {
        console.error(e)
      }
    },
    async cancelOrder() {
      uni.showModal({
        title: '提示',
        content: '确认取消该订单吗？',
        success: async (res) => {
          if (res.confirm) {
            try {
              await this.$request({
                url: `/order/cancel/${this.orderNo}`,
                method: 'POST'
              })
              uni.showToast({
                title: '取消成功',
                icon: 'success'
              })
              this.loadOrderDetail()
            } catch (e) {
              console.error(e)
            }
          }
        }
      })
    },
    async confirmReceive() {
      uni.showModal({
        title: '提示',
        content: '确认收到商品吗？',
        success: async (res) => {
          if (res.confirm) {
            try {
              await this.$request({
                url: `/order/confirm/${this.orderNo}`,
                method: 'POST'
              })
              uni.showToast({
                title: '确认成功',
                icon: 'success'
              })
              this.loadOrderDetail()
            } catch (e) {
              console.error(e)
            }
          }
        }
      })
    },
    async applyRefund() {
      uni.showModal({
        title: '提示',
        content: '确认申请退款吗？',
        success: async (res) => {
          if (res.confirm) {
            try {
              await this.$request({
                url: `/order/refund/${this.orderNo}`,
                method: 'POST'
              })
              uni.showToast({
                title: '申请成功',
                icon: 'success'
              })
              this.loadOrderDetail()
            } catch (e) {
              console.error(e)
            }
          }
        }
      })
    },
    goToDetail() {
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
  padding-bottom: 140rpx;
}

.status-card {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  padding: 60rpx 30rpx;
  text-align: center;
}

.status-icon {
  display: block;
  font-size: 80rpx;
  margin-bottom: 20rpx;
}

.status-text {
  display: block;
  font-size: 36rpx;
  color: #fff;
  font-weight: bold;
  margin-bottom: 10rpx;
}

.status-desc {
  display: block;
  font-size: 26rpx;
  color: rgba(255,255,255,0.8);
}

.product-card {
  display: flex;
  background: #fff;
  margin: 20rpx;
  padding: 30rpx;
  border-radius: 16rpx;
}

.product-image {
  width: 200rpx;
  height: 200rpx;
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
  font-size: 30rpx;
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
  font-size: 26rpx;
  color: #ff4d4f;
}

.price-value {
  font-size: 40rpx;
  font-weight: bold;
  color: #ff4d4f;
}

.info-card {
  background: #fff;
  margin: 20rpx;
  padding: 30rpx;
  border-radius: 16rpx;
}

.info-item {
  display: flex;
  justify-content: space-between;
  padding: 20rpx 0;
  border-bottom: 1rpx solid #f0f0f0;
}

.info-item:last-child {
  border-bottom: none;
}

.info-item .label {
  font-size: 26rpx;
  color: #999;
}

.info-item .value {
  font-size: 26rpx;
  color: #333;
  flex: 1;
  text-align: right;
}

.bottom-bar {
  position: fixed;
  bottom: 0;
  left: 0;
  right: 0;
  padding: 20rpx 30rpx;
  background: #fff;
  box-shadow: 0 -4rpx 20rpx rgba(0,0,0,0.1);
  display: flex;
  justify-content: flex-end;
  gap: 20rpx;
}

.action-btn {
  height: 80rpx;
  line-height: 80rpx;
  padding: 0 40rpx;
  background: #f5f5f5;
  color: #666;
  border-radius: 40rpx;
  font-size: 28rpx;
  border: none;
}

.action-btn.primary {
  background: linear-gradient(135deg, #ff6b6b 0%, #ff4757 100%);
  color: #fff;
}
</style>
