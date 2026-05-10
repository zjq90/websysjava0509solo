<template>
  <view class="container">
    <view class="order-header">
      <view class="order-status" :class="'status-' + orderData.status">
        {{ getStatusText(orderData.status) }}
      </view>
      <view class="order-number">{{ orderData.orderNumber }}</view>
    </view>

    <view class="customer-card">
      <view class="card-title">客户信息</view>
      <view class="info-row">
        <text class="info-label">客户姓名</text>
        <text class="info-value">{{ orderData.customerName || '-' }}</text>
      </view>
      <view class="info-row">
        <text class="info-label">联系电话</text>
        <text class="info-value">{{ orderData.customerPhone || '-' }}</text>
      </view>
      <view class="info-row">
        <text class="info-label">客户地址</text>
        <text class="info-value">{{ orderData.customerAddress || '-' }}</text>
      </view>
    </view>

    <view class="items-card">
      <view class="card-title">商品明细</view>
      <view class="item-list">
        <view class="item-row" v-for="(item, index) in orderData.items" :key="index">
          <view class="item-info">
            <text class="item-name">{{ item.seedName || '未知商品' }}</text>
            <text class="item-batch">{{ item.batchNumber || '-' }}</text>
          </view>
          <view class="item-quantity">
            <text>{{ item.quantity }} 公斤</text>
          </view>
          <view class="item-price">
            <text>¥{{ item.price }} × {{ item.quantity }}</text>
          </view>
          <view class="item-subtotal">
            ¥{{ (Number(item.quantity) * Number(item.price)).toFixed(2) }}
          </view>
        </view>
      </view>
    </view>

    <view class="summary-card">
      <view class="summary-row">
        <text class="summary-label">商品数量</text>
        <text class="summary-value">{{ orderData.items?.length || 0 }} 种</text>
      </view>
      <view class="summary-row">
        <text class="summary-label">总数量</text>
        <text class="summary-value">{{ totalQuantity }} 公斤</text>
      </view>
      <view class="summary-row highlight">
        <text class="summary-label">订单金额</text>
        <text class="summary-value amount">¥{{ Number(orderData.totalAmount || 0).toFixed(2) }}</text>
      </view>
    </view>

    <view class="info-card" v-if="orderData.remark">
      <view class="card-title">订单备注</view>
      <text class="remark-text">{{ orderData.remark }}</text>
    </view>

    <view class="info-card">
      <view class="card-title">订单信息</view>
      <view class="info-row">
        <text class="info-label">创建时间</text>
        <text class="info-value">{{ formatDate(orderData.createdTime) }}</text>
      </view>
      <view class="info-row">
        <text class="info-label">更新时间</text>
        <text class="info-value">{{ formatDate(orderData.updatedTime) }}</text>
      </view>
    </view>
  </view>
</template>

<script>
import { getOrderDetail } from '@/api/order'

export default {
  data() {
    return {
      orderId: null,
      orderData: {}
    }
  },

  computed: {
    totalQuantity() {
      return (this.orderData.items || []).reduce((sum, item) => sum + (Number(item.quantity) || 0), 0)
    }
  },

  onLoad(options) {
    if (options.id) {
      this.orderId = options.id
      this.loadDetail()
    }
  },

  methods: {
    // 加载详情
    async loadDetail() {
      try {
        this.orderData = await getOrderDetail(this.orderId)
      } catch (e) {
        console.error('加载订单详情失败', e)
      }
    },

    // 获取状态文本
    getStatusText(status) {
      const statusMap = {
        'PENDING': '待处理',
        'PROCESSING': '处理中',
        'COMPLETED': '已完成',
        'CANCELLED': '已取消'
      }
      return statusMap[status] || status
    },

    // 格式化日期
    formatDate(dateStr) {
      if (!dateStr) return '-'
      const date = new Date(dateStr)
      const year = date.getFullYear()
      const month = String(date.getMonth() + 1).padStart(2, '0')
      const day = String(date.getDate()).padStart(2, '0')
      const hour = String(date.getHours()).padStart(2, '0')
      const minute = String(date.getMinutes()).padStart(2, '0')
      return `${year}-${month}-${day} ${hour}:${minute}`
    }
  }
}
</script>

<style scoped>
.container {
  padding: 30rpx;
  min-height: 100vh;
  background: #F5F5F5;
}

.order-header {
  background: linear-gradient(135deg, #007AFF 0%, #34C759 100%);
  border-radius: 24rpx;
  padding: 40rpx;
  margin-bottom: 30rpx;
  text-align: center;
}

.order-status {
  display: inline-block;
  padding: 10rpx 30rpx;
  border-radius: 30rpx;
  font-size: 28rpx;
  margin-bottom: 20rpx;
}

.status-PENDING {
  background: rgba(255, 149, 0, 0.2);
  color: #FFFFFF;
}

.status-PROCESSING {
  background: rgba(0, 122, 255, 0.2);
  color: #FFFFFF;
}

.status-COMPLETED {
  background: rgba(52, 199, 89, 0.2);
  color: #FFFFFF;
}

.status-CANCELLED {
  background: rgba(142, 142, 147, 0.2);
  color: #FFFFFF;
}

.order-number {
  font-size: 26rpx;
  color: rgba(255, 255, 255, 0.8);
}

.customer-card,
.items-card,
.summary-card,
.info-card {
  background: #FFFFFF;
  border-radius: 20rpx;
  padding: 30rpx;
  margin-bottom: 20rpx;
  box-shadow: 0 4rpx 12rpx rgba(0, 0, 0, 0.05);
}

.card-title {
  font-size: 30rpx;
  font-weight: 600;
  color: #333333;
  margin-bottom: 20rpx;
  padding-bottom: 20rpx;
  border-bottom: 1rpx solid #F5F5F5;
}

.info-row {
  display: flex;
  justify-content: space-between;
  padding: 15rpx 0;
}

.info-label {
  font-size: 28rpx;
  color: #999999;
}

.info-value {
  font-size: 28rpx;
  color: #333333;
}

.item-list {
  margin-top: 10rpx;
}

.item-row {
  padding: 20rpx 0;
  border-bottom: 1rpx solid #F5F5F5;
}

.item-row:last-child {
  border-bottom: none;
}

.item-info {
  display: flex;
  justify-content: space-between;
  margin-bottom: 10rpx;
}

.item-name {
  font-size: 28rpx;
  font-weight: 500;
  color: #333333;
}

.item-batch {
  font-size: 24rpx;
  color: #999999;
}

.item-quantity {
  font-size: 26rpx;
  color: #666666;
  margin-bottom: 5rpx;
}

.item-price {
  font-size: 24rpx;
  color: #999999;
}

.item-subtotal {
  font-size: 28rpx;
  font-weight: 500;
  color: #FF3B30;
  text-align: right;
  margin-top: 10rpx;
}

.summary-row {
  display: flex;
  justify-content: space-between;
  padding: 15rpx 0;
}

.summary-row.highlight {
  padding-top: 20rpx;
  margin-top: 10rpx;
  border-top: 1rpx solid #F5F5F5;
}

.summary-label {
  font-size: 28rpx;
  color: #666666;
}

.summary-value {
  font-size: 28rpx;
  color: #333333;
}

.summary-value.amount {
  font-size: 36rpx;
  font-weight: 600;
  color: #FF3B30;
}

.remark-text {
  font-size: 28rpx;
  color: #666666;
  line-height: 1.6;
}
</style>
