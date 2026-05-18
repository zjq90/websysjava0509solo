<template>
  <view class="detail-container">
    <view class="status-section">
      <view class="status-icon">
        <text>{{ statusIcon }}</text>
      </view>
      <view class="status-info">
        <text class="status-text">{{ order.statusText }}</text>
        <text class="status-desc">{{ statusDesc }}</text>
      </view>
    </view>

    <view class="address-section" v-if="order.address">
      <view class="section-title">收货信息</view>
      <view class="address-info">
        <text class="address-name">{{ order.address.name }} {{ order.address.phone }}</text>
        <text class="address-detail">{{ order.address.detail }}</text>
      </view>
    </view>

    <view class="product-section">
      <view class="section-title">商品信息</view>
      <view class="product-card">
        <image class="product-img" :src="order.productImage" mode="aspectFill"></image>
        <view class="product-detail">
          <text class="product-name">{{ order.productName }}</text>
          <text class="product-desc">{{ order.productDesc }}</text>
          <view class="price-row">
            <text class="product-price">¥{{ order.price }}</text>
            <text class="product-count">x1</text>
          </view>
        </view>
      </view>
    </view>

    <view class="order-section">
      <view class="section-title">订单信息</view>
      <view class="info-row">
        <text class="label">订单编号</text>
        <text class="value">{{ order.orderNo }}</text>
      </view>
      <view class="info-row">
        <text class="label">创建时间</text>
        <text class="value">{{ order.createTime }}</text>
      </view>
      <view class="info-row">
        <text class="label">支付方式</text>
        <text class="value">{{ order.payMethod || '待支付' }}</text>
      </view>
    </view>

    <view class="logistics-section" v-if="order.logistics">
      <view class="section-title">物流信息</view>
      <view class="logistics-info">
        <text class="logistics-company">{{ order.logistics.company }}</text>
        <text class="logistics-no">运单号：{{ order.logistics.no }}</text>
      </view>
      <view class="logistics-trace">
        <view 
          class="trace-item" 
          v-for="(item, idx) in order.logistics.trace" 
          :key="idx"
          :class="{ active: idx === 0 }"
        >
          <view class="trace-dot"></view>
          <view class="trace-content">
            <text class="trace-text">{{ item.content }}</text>
            <text class="trace-time">{{ item.time }}</text>
          </view>
        </view>
      </view>
    </view>

    <view class="footer-section">
      <view class="total-row">
        <text class="total-label">商品总额</text>
        <text class="total-value">¥{{ order.price }}</text>
      </view>
      <view class="total-row">
        <text class="total-label">运费</text>
        <text class="total-value">¥0.00</text>
      </view>
      <view class="total-row final">
        <text class="total-label">实付款</text>
        <text class="total-value final-price">¥{{ order.price }}</text>
      </view>
    </view>

    <view class="action-section">
      <button class="action-btn secondary" v-if="order.status === 'pending_payment'" @click="cancelOrder">取消订单</button>
      <button class="action-btn primary" v-if="order.status === 'pending_payment'" @click="payOrder">立即支付</button>
      <button class="action-btn primary" v-if="order.status === 'shipped'" @click="confirmReceive">确认收货</button>
      <button class="action-btn secondary" v-if="order.status === 'completed'" @click="contactSeller">联系卖家</button>
      <button class="action-btn primary" v-if="order.status === 'completed'" @click="goToReview">去评价</button>
    </view>
  </view>
</template>

<script>
export default {
  data() {
    return {
      order: {
        id: 1,
        orderNo: '202401150001',
        status: 'shipped',
        statusText: '待收货',
        productName: 'MacBook Pro 14寸 M2',
        productDesc: '99新 在保到2025年',
        productImage: 'https://picsum.photos/200/200?random=21',
        price: 12999,
        createTime: '2024-01-14 15:20',
        payMethod: '微信支付',
        address: {
          name: '张三',
          phone: '138****8888',
          detail: '北京市朝阳区建国路88号SOHO现代城A座1001室'
        },
        logistics: {
          company: '顺丰速运',
          no: 'SF1234567890',
          trace: [
            { content: '快递已到达【北京朝阳集散中心】', time: '2024-01-15 14:30' },
            { content: '快递已从【深圳集散中心】发出', time: '2024-01-15 08:00' },
            { content: '快递已到达【深圳集散中心】', time: '2024-01-14 22:00' },
            { content: '卖家已发货', time: '2024-01-14 18:00' }
          ]
        }
      }
    }
  },
  computed: {
    statusIcon() {
      const iconMap = {
        pending_payment: '💳',
        pending_ship: '📦',
        shipped: '🚚',
        completed: '✅'
      }
      return iconMap[this.order.status] || '📋'
    },
    statusDesc() {
      const descMap = {
        pending_payment: '请在24小时内完成支付，超时订单将自动取消',
        pending_ship: '卖家正在准备发货，请耐心等待',
        shipped: '商品正在配送中，请注意查收',
        completed: '交易已完成，感谢您的购买'
      }
      return descMap[this.order.status] || ''
    }
  },
  methods: {
    cancelOrder() {
      uni.showModal({
        title: '确认取消',
        content: '确定要取消该订单吗？',
        success: (res) => {
          if (res.confirm) {
            uni.showToast({
              title: '已取消',
              icon: 'success'
            })
            setTimeout(() => {
              uni.navigateBack()
            }, 1500)
          }
        }
      })
    },
    payOrder() {
      uni.navigateTo({
        url: `/pages/pay/index?orderId=${this.order.id}`
      })
    },
    confirmReceive() {
      uni.showModal({
        title: '确认收货',
        content: '请确认已收到商品且商品无误',
        success: (res) => {
          if (res.confirm) {
            this.order.status = 'completed'
            this.order.statusText = '已完成'
            uni.showToast({
              title: '确认成功',
              icon: 'success'
            })
          }
        }
      })
    },
    contactSeller() {
      uni.showToast({
        title: '联系卖家',
        icon: 'none'
      })
    },
    goToReview() {
      uni.showToast({
        title: '去评价',
        icon: 'none'
      })
    }
  }
}
</script>

<style scoped>
.detail-container {
  min-height: 100vh;
  background-color: #f5f5f5;
  padding-bottom: 200rpx;
}

.status-section {
  background: linear-gradient(135deg, #409EFF, #67C23A);
  padding: 50rpx 30rpx;
  display: flex;
  align-items: center;
  gap: 30rpx;
}

.status-icon {
  width: 100rpx;
  height: 100rpx;
  background-color: rgba(255, 255, 255, 0.2);
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 50rpx;
}

.status-info {
  flex: 1;
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
  color: rgba(255, 255, 255, 0.8);
  line-height: 1.4;
}

.address-section,
.product-section,
.order-section,
.logistics-section,
.footer-section {
  background-color: #fff;
  margin: 20rpx;
  border-radius: 20rpx;
  padding: 30rpx;
}

.section-title {
  font-size: 30rpx;
  color: #333;
  font-weight: bold;
  margin-bottom: 20rpx;
}

.address-info {
  padding-left: 10rpx;
}

.address-name {
  display: block;
  font-size: 30rpx;
  color: #333;
  font-weight: bold;
  margin-bottom: 10rpx;
}

.address-detail {
  display: block;
  font-size: 26rpx;
  color: #666;
  line-height: 1.5;
}

.product-card {
  display: flex;
  gap: 20rpx;
}

.product-img {
  width: 180rpx;
  height: 180rpx;
  border-radius: 15rpx;
  background-color: #f5f5f5;
  flex-shrink: 0;
}

.product-detail {
  flex: 1;
  display: flex;
  flex-direction: column;
  justify-content: space-between;
}

.product-name {
  font-size: 30rpx;
  color: #333;
  font-weight: bold;
  line-height: 1.4;
}

.product-desc {
  font-size: 26rpx;
  color: #999;
}

.price-row {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.product-price {
  font-size: 32rpx;
  color: #ff4d4f;
  font-weight: bold;
}

.product-count {
  font-size: 26rpx;
  color: #999;
}

.info-row {
  display: flex;
  justify-content: space-between;
  padding: 15rpx 0;
}

.label {
  font-size: 28rpx;
  color: #999;
}

.value {
  font-size: 28rpx;
  color: #333;
}

.logistics-info {
  padding: 20rpx;
  background-color: #f9f9f9;
  border-radius: 15rpx;
  margin-bottom: 20rpx;
}

.logistics-company {
  display: block;
  font-size: 30rpx;
  color: #333;
  font-weight: bold;
  margin-bottom: 10rpx;
}

.logistics-no {
  display: block;
  font-size: 26rpx;
  color: #666;
}

.logistics-trace {
  padding-left: 10rpx;
}

.trace-item {
  display: flex;
  gap: 20rpx;
  padding: 20rpx 0;
  position: relative;
}

.trace-item:not(:last-child)::after {
  content: '';
  position: absolute;
  left: 10rpx;
  top: 50rpx;
  width: 2rpx;
  height: calc(100% - 30rpx);
  background-color: #e8e8e8;
}

.trace-dot {
  width: 20rpx;
  height: 20rpx;
  border-radius: 50%;
  background-color: #e8e8e8;
  flex-shrink: 0;
  margin-top: 8rpx;
}

.trace-item.active .trace-dot {
  background-color: #409EFF;
  box-shadow: 0 0 0 6rpx rgba(64, 158, 255, 0.2);
}

.trace-content {
  flex: 1;
}

.trace-text {
  display: block;
  font-size: 28rpx;
  color: #333;
  margin-bottom: 8rpx;
}

.trace-item.active .trace-text {
  color: #409EFF;
  font-weight: bold;
}

.trace-time {
  display: block;
  font-size: 24rpx;
  color: #999;
}

.total-row {
  display: flex;
  justify-content: space-between;
  padding: 15rpx 0;
}

.total-row.final {
  padding-top: 20rpx;
  margin-top: 10rpx;
  border-top: 1rpx solid #f5f5f5;
}

.total-label {
  font-size: 28rpx;
  color: #666;
}

.total-value {
  font-size: 28rpx;
  color: #333;
}

.final-price {
  font-size: 36rpx;
  color: #ff4d4f;
  font-weight: bold;
}

.action-section {
  position: fixed;
  bottom: 0;
  left: 0;
  right: 0;
  display: flex;
  justify-content: flex-end;
  gap: 20rpx;
  padding: 25rpx 30rpx;
  background-color: #fff;
  box-shadow: 0 -4rpx 12rpx rgba(0, 0, 0, 0.05);
}

.action-btn {
  padding: 20rpx 50rpx;
  border-radius: 40rpx;
  font-size: 28rpx;
  border: none;
}

.action-btn.primary {
  background-color: #409EFF;
  color: #fff;
}

.action-btn.secondary {
  background-color: #f5f5f5;
  color: #666;
}
</style>
