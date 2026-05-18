<template>
  <view class="order-container">
    <view class="tabs">
      <view 
        class="tab" 
        :class="{ active: activeTab === item.value }"
        v-for="item in tabList" 
        :key="item.value"
        @click="activeTab = item.value"
      >
        {{ item.label }}
      </view>
    </view>

    <view class="order-list">
      <view 
        class="order-item" 
        v-for="order in filteredOrders" 
        :key="order.id"
        @click="goToDetail(order.id)"
      >
        <view class="order-header">
          <text class="order-no">订单号：{{ order.orderNo }}</text>
          <text class="order-status" :class="order.status">{{ order.statusText }}</text>
        </view>
        
        <view class="product-info">
          <image class="product-img" :src="order.productImage" mode="aspectFill"></image>
          <view class="product-detail">
            <text class="product-name">{{ order.productName }}</text>
            <text class="product-desc">{{ order.productDesc }}</text>
            <text class="product-price">¥{{ order.price }}</text>
          </view>
        </view>

        <view class="order-footer">
          <text class="order-total">共1件商品 合计：<text class="total-price">¥{{ order.price }}</text></text>
          <view class="order-actions">
            <button class="action-btn secondary" v-if="order.status === 'pending_payment'" @click.stop="cancelOrder(order)">取消订单</button>
            <button class="action-btn primary" v-if="order.status === 'pending_payment'" @click.stop="payOrder(order)">立即支付</button>
            <button class="action-btn primary" v-if="order.status === 'shipped'" @click.stop="confirmReceive(order)">确认收货</button>
            <button class="action-btn secondary" v-if="order.status === 'completed'" @click.stop="goToReview(order)">去评价</button>
          </view>
        </view>
      </view>
    </view>

    <view class="empty-state" v-if="filteredOrders.length === 0">
      <text class="empty-icon">📦</text>
      <text class="empty-text">暂无订单</text>
    </view>
  </view>
</template>

<script>
export default {
  data() {
    return {
      activeTab: 'all',
      tabList: [
        { label: '全部', value: 'all' },
        { label: '待付款', value: 'pending_payment' },
        { label: '待发货', value: 'pending_ship' },
        { label: '待收货', value: 'shipped' },
        { label: '已完成', value: 'completed' }
      ],
      orders: [
        {
          id: 1,
          orderNo: '202401150001',
          status: 'pending_payment',
          statusText: '待付款',
          productName: 'iPhone 13 128G 蓝色',
          productDesc: '95新 原装正品 无拆无修',
          productImage: 'https://picsum.photos/200/200?random=20',
          price: 3599,
          createTime: '2024-01-15 10:30'
        },
        {
          id: 2,
          orderNo: '202401140002',
          status: 'shipped',
          statusText: '待收货',
          productName: 'MacBook Pro 14寸 M2',
          productDesc: '99新 在保到2025年',
          productImage: 'https://picsum.photos/200/200?random=21',
          price: 12999,
          createTime: '2024-01-14 15:20'
        },
        {
          id: 3,
          orderNo: '202401100003',
          status: 'completed',
          statusText: '已完成',
          productName: 'AirPods Pro 2代',
          productDesc: '全新未拆封 官网正品',
          productImage: 'https://picsum.photos/200/200?random=22',
          price: 1599,
          createTime: '2024-01-10 09:00'
        }
      ]
    }
  },
  computed: {
    filteredOrders() {
      if (this.activeTab === 'all') {
        return this.orders
      }
      return this.orders.filter(order => order.status === this.activeTab)
    }
  },
  methods: {
    goToDetail(id) {
      uni.navigateTo({
        url: `/pages/order/detail?id=${id}`
      })
    },
    cancelOrder(order) {
      uni.showModal({
        title: '确认取消',
        content: '确定要取消该订单吗？',
        success: (res) => {
          if (res.confirm) {
            const index = this.orders.findIndex(o => o.id === order.id)
            if (index > -1) {
              this.orders.splice(index, 1)
            }
            uni.showToast({
              title: '已取消',
              icon: 'success'
            })
          }
        }
      })
    },
    payOrder(order) {
      uni.navigateTo({
        url: `/pages/pay/index?orderId=${order.id}`
      })
    },
    confirmReceive(order) {
      uni.showModal({
        title: '确认收货',
        content: '请确认已收到商品且商品无误',
        success: (res) => {
          if (res.confirm) {
            order.status = 'completed'
            order.statusText = '已完成'
            uni.showToast({
              title: '确认成功',
              icon: 'success'
            })
          }
        }
      })
    },
    goToReview(order) {
      uni.showToast({
        title: '去评价',
        icon: 'none'
      })
    }
  }
}
</script>

<style scoped>
.order-container {
  min-height: 100vh;
  background-color: #f5f5f5;
  padding-bottom: 120rpx;
}

.tabs {
  display: flex;
  background-color: #fff;
  padding: 0 10rpx;
  position: sticky;
  top: 0;
  z-index: 100;
  overflow-x: auto;
}

.tab {
  flex-shrink: 0;
  padding: 30rpx 25rpx;
  font-size: 28rpx;
  color: #666;
  border-bottom: 4rpx solid transparent;
}

.tab.active {
  color: #409EFF;
  border-bottom-color: #409EFF;
  font-weight: bold;
}

.order-list {
  padding: 20rpx;
}

.order-item {
  background-color: #fff;
  border-radius: 20rpx;
  padding: 30rpx;
  margin-bottom: 20rpx;
  box-shadow: 0 4rpx 12rpx rgba(0, 0, 0, 0.05);
}

.order-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding-bottom: 20rpx;
  border-bottom: 1rpx solid #f5f5f5;
  margin-bottom: 20rpx;
}

.order-no {
  font-size: 26rpx;
  color: #999;
}

.order-status {
  font-size: 28rpx;
  font-weight: bold;
}

.order-status.pending_payment {
  color: #ff4d4f;
}

.order-status.pending_ship {
  color: #faad14;
}

.order-status.shipped {
  color: #409EFF;
}

.order-status.completed {
  color: #52c41a;
}

.product-info {
  display: flex;
  gap: 20rpx;
  margin-bottom: 20rpx;
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
  overflow: hidden;
  text-overflow: ellipsis;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
}

.product-desc {
  font-size: 26rpx;
  color: #999;
}

.product-price {
  font-size: 32rpx;
  color: #ff4d4f;
  font-weight: bold;
}

.order-footer {
  padding-top: 20rpx;
  border-top: 1rpx solid #f5f5f5;
}

.order-total {
  font-size: 26rpx;
  color: #666;
  text-align: right;
  margin-bottom: 20rpx;
  display: block;
}

.total-price {
  color: #ff4d4f;
  font-weight: bold;
}

.order-actions {
  display: flex;
  justify-content: flex-end;
  gap: 20rpx;
}

.action-btn {
  padding: 15rpx 40rpx;
  border-radius: 40rpx;
  font-size: 26rpx;
  border: none;
  line-height: 1.2;
}

.action-btn.primary {
  background-color: #409EFF;
  color: #fff;
}

.action-btn.secondary {
  background-color: #f5f5f5;
  color: #666;
}

.empty-state {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 200rpx 0;
}

.empty-icon {
  font-size: 120rpx;
  margin-bottom: 30rpx;
}

.empty-text {
  font-size: 30rpx;
  color: #999;
}
</style>
