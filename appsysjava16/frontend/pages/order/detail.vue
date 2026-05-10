<template>
  <view class="container" v-if="order">
    <view class="status-header">
      <text class="status-text">{{ getStatusText(order.status) }}</text>
      <text class="status-desc">{{ getStatusDesc(order.status) }}</text>
    </view>

    <view class="info-card">
      <view class="card-title">订单信息</view>
      <view class="info-row">
        <text class="label">订单号</text>
        <text class="value">{{ order.orderNo }}</text>
      </view>
      <view class="info-row">
        <text class="label">创建时间</text>
        <text class="value">{{ formatDate(order.createTime) }}</text>
      </view>
      <view class="info-row" v-if="order.notes">
        <text class="label">备注</text>
        <text class="value">{{ order.notes }}</text>
      </view>
    </view>

    <view class="info-card">
      <view class="card-title">客户信息</view>
      <view class="info-row">
        <text class="label">客户名称</text>
        <text class="value">{{ order.customerName }}</text>
      </view>
      <view class="info-row">
        <text class="label">客户等级</text>
        <text class="value">{{ getLevelText(order.customerLevel) }}</text>
      </view>
    </view>

    <view class="info-card">
      <view class="card-title">商品明细</view>
      <view class="item-row" v-for="item in order.orderItems" :key="item.id">
        <view class="item-info">
          <text class="item-name">{{ item.productName }}</text>
          <text class="item-spec">单价: ¥{{ item.unitPrice }}</text>
        </view>
        <view class="item-qty">
          <text>x{{ item.quantity }}</text>
          <text class="item-total">¥{{ (item.unitPrice * item.quantity).toFixed(2) }}</text>
        </view>
      </view>
      <view class="divider"></view>
      <view class="total-row">
        <text class="label">订单合计</text>
        <text class="total-price">¥{{ order.totalAmount }}</text>
      </view>
    </view>

    <view class="timeline-card" v-if="order.contractSignedTime || order.erpSyncTime || order.stockPrepareTime">
      <view class="card-title">订单流程</view>
      <view class="timeline">
        <view class="timeline-item" v-if="order.createTime">
          <view class="timeline-dot done"></view>
          <view class="timeline-content">
            <text class="timeline-title">订单创建</text>
            <text class="timeline-time">{{ formatDate(order.createTime) }}</text>
          </view>
        </view>
        <view class="timeline-item" v-if="order.contractSignedTime">
          <view class="timeline-dot done"></view>
          <view class="timeline-content">
            <text class="timeline-title">合同签署</text>
            <text class="timeline-time">{{ formatDate(order.contractSignedTime) }}</text>
          </view>
        </view>
        <view class="timeline-item" v-if="order.erpSyncTime">
          <view class="timeline-dot done"></view>
          <view class="timeline-content">
            <text class="timeline-title">ERP同步</text>
            <text class="timeline-time">{{ formatDate(order.erpSyncTime) }}</text>
          </view>
        </view>
        <view class="timeline-item" v-if="order.stockPrepareTime">
          <view class="timeline-dot done"></view>
          <view class="timeline-content">
            <text class="timeline-title">仓库备货</text>
            <text class="timeline-time">{{ formatDate(order.stockPrepareTime) }}</text>
          </view>
        </view>
        <view class="timeline-item" v-if="order.shippedTime">
          <view class="timeline-dot done"></view>
          <view class="timeline-content">
            <text class="timeline-title">商品发货</text>
            <text class="timeline-time">{{ formatDate(order.shippedTime) }}</text>
          </view>
        </view>
      </view>
    </view>

    <view class="action-buttons">
      <button 
        v-if="order.status === 'PENDING_CONFIRMATION'" 
        class="action-btn primary" 
        @click="confirmOrder"
      >确认订单</button>
      <button 
        v-if="order.status === 'PENDING_SIGNATURE'" 
        class="action-btn primary" 
        @click="goToSign"
      >去签署合同</button>
      <button 
        v-if="order.status === 'SIGNED'" 
        class="action-btn primary" 
        @click="syncErp"
      >同步ERP</button>
      <button 
        v-if="order.status === 'STOCK_PREPARING'" 
        class="action-btn primary" 
        @click="shipOrder"
      >已发货</button>
      <button 
        v-if="order.status === 'SHIPPED'" 
        class="action-btn primary" 
        @click="deliverOrder"
      >已签收</button>
      <button 
        v-if="order.status === 'DELIVERED'" 
        class="action-btn primary" 
        @click="completeOrder"
      >完成订单</button>
      <button 
        v-if="order.status === 'SHIPPED'" 
        class="action-btn secondary" 
        @click="goToLogistics"
      >物流跟踪</button>
    </view>
  </view>
  <view v-else class="empty">加载中...</view>
</template>

<script>
import orderApi from '@/api/order.js'

export default {
  data() {
    return {
      orderId: '',
      order: null
    }
  },
  onLoad(options) {
    this.orderId = options.id
    this.loadOrder()
  },
  methods: {
    async loadOrder() {
      uni.showLoading({ title: '加载中...' })
      try {
        this.order = await orderApi.getOrderById(this.orderId)
      } catch (e) {
        console.error('加载订单失败', e)
      } finally {
        uni.hideLoading()
      }
    },
    getStatusText(status) {
      const map = {
        'PENDING_CONFIRMATION': '待确认',
        'PENDING_SIGNATURE': '待签署合同',
        'SIGNED': '已签署',
        'STOCK_PREPARING': '备货中',
        'SHIPPED': '已发货',
        'DELIVERED': '已签收',
        'COMPLETED': '已完成',
        'CANCELLED': '已取消'
      }
      return map[status] || status
    },
    getStatusDesc(status) {
      const map = {
        'PENDING_CONFIRMATION': '请确认订单信息',
        'PENDING_SIGNATURE': '请客户签署合同',
        'SIGNED': '合同已签署，等待同步ERP',
        'STOCK_PREPARING': '仓库正在备货中',
        'SHIPPED': '商品已发出，等待签收',
        'DELIVERED': '客户已签收',
        'COMPLETED': '订单已完成',
        'CANCELLED': '订单已取消'
      }
      return map[status] || ''
    },
    getLevelText(level) {
      const map = {
        'TEMPORARY': '临时',
        'NORMAL': '普通',
        'VIP': 'VIP',
        'SVIP': 'SVIP',
        'DIAMOND': '钻石'
      }
      return map[level] || level
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
    async confirmOrder() {
      uni.showLoading({ title: '确认中...' })
      try {
        this.order = await orderApi.confirmOrder(this.orderId)
        uni.showToast({ title: '确认成功', icon: 'success' })
      } catch (e) {
        console.error('确认失败', e)
      } finally {
        uni.hideLoading()
      }
    },
    goToSign() {
      uni.navigateTo({ url: `/pages/order/sign?id=${this.orderId}` })
    },
    async syncErp() {
      uni.showLoading({ title: '同步中...' })
      try {
        this.order = await orderApi.syncToErp(this.orderId)
        uni.showToast({ title: 'ERP同步成功', icon: 'success' })
      } catch (e) {
        console.error('ERP同步失败', e)
      } finally {
        uni.hideLoading()
      }
    },
    async shipOrder() {
      uni.showLoading({ title: '处理中...' })
      try {
        this.order = await orderApi.shipOrder(this.orderId)
        uni.showToast({ title: '发货成功', icon: 'success' })
      } catch (e) {
        console.error('发货失败', e)
      } finally {
        uni.hideLoading()
      }
    },
    async deliverOrder() {
      uni.showLoading({ title: '处理中...' })
      try {
        this.order = await orderApi.deliverOrder(this.orderId)
        uni.showToast({ title: '签收成功', icon: 'success' })
      } catch (e) {
        console.error('签收失败', e)
      } finally {
        uni.hideLoading()
      }
    },
    async completeOrder() {
      uni.showLoading({ title: '处理中...' })
      try {
        this.order = await orderApi.completeOrder(this.orderId)
        uni.showToast({ title: '订单完成', icon: 'success' })
      } catch (e) {
        console.error('完成失败', e)
      } finally {
        uni.hideLoading()
      }
    },
    goToLogistics() {
      uni.navigateTo({ url: `/pages/order/logistics?orderId=${this.orderId}` })
    }
  }
}
</script>

<style scoped>
.status-header {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  padding: 40rpx;
  border-radius: 16rpx;
  margin-bottom: 24rpx;
}

.status-text {
  font-size: 40rpx;
  font-weight: bold;
  color: #fff;
  display: block;
  margin-bottom: 8rpx;
}

.status-desc {
  font-size: 26rpx;
  color: rgba(255, 255, 255, 0.8);
}

.info-card,
.timeline-card {
  background: #fff;
  border-radius: 16rpx;
  padding: 24rpx;
  margin-bottom: 20rpx;
}

.card-title {
  font-size: 30rpx;
  font-weight: 500;
  color: #333;
  margin-bottom: 20rpx;
  padding-bottom: 12rpx;
  border-bottom: 2rpx solid #f5f5f5;
}

.info-row {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 16rpx 0;
}

.info-row .label {
  font-size: 28rpx;
  color: #666;
}

.info-row .value {
  font-size: 28rpx;
  color: #333;
}

.item-row {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 16rpx 0;
}

.item-info {
  display: flex;
  flex-direction: column;
  gap: 8rpx;
}

.item-name {
  font-size: 28rpx;
  color: #333;
}

.item-spec {
  font-size: 24rpx;
  color: #999;
}

.item-qty {
  display: flex;
  flex-direction: column;
  align-items: flex-end;
  gap: 8rpx;
}

.item-qty text {
  font-size: 26rpx;
  color: #666;
}

.item-total {
  font-size: 28rpx;
  color: #ff4d4f;
  font-weight: 500;
}

.divider {
  height: 2rpx;
  background: #f5f5f5;
  margin: 16rpx 0;
}

.total-row {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding-top: 8rpx;
}

.total-row .label {
  font-size: 30rpx;
  color: #333;
  font-weight: 500;
}

.total-price {
  font-size: 36rpx;
  font-weight: bold;
  color: #ff4d4f;
}

.timeline {
  position: relative;
}

.timeline-item {
  display: flex;
  align-items: flex-start;
  padding: 16rpx 0;
}

.timeline-dot {
  width: 20rpx;
  height: 20rpx;
  border-radius: 50%;
  background: #e0e0e0;
  margin-top: 8rpx;
  margin-right: 16rpx;
  flex-shrink: 0;
}

.timeline-dot.done {
  background: #667eea;
}

.timeline-content {
  flex: 1;
  display: flex;
  flex-direction: column;
  gap: 4rpx;
}

.timeline-title {
  font-size: 28rpx;
  color: #333;
}

.timeline-time {
  font-size: 24rpx;
  color: #999;
}

.action-buttons {
  display: flex;
  gap: 16rpx;
  margin-top: 24rpx;
}

.action-btn {
  flex: 1;
  height: 88rpx;
  line-height: 88rpx;
  border-radius: 44rpx;
  font-size: 30rpx;
  border: none;
}

.action-btn.primary {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: #fff;
}

.action-btn.secondary {
  background: #f5f5f5;
  color: #666;
}
</style>
