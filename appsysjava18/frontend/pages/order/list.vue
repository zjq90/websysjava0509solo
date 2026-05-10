<template>
  <view class="container">
    <view class="search-bar">
      <view class="search-input-wrapper">
        <text class="search-icon">🔍</text>
        <input 
          class="search-input" 
          type="text" 
          v-model="searchKeyword" 
          placeholder="搜索订单号或客户"
          placeholder-class="placeholder"
          @confirm="loadData"
        />
      </view>
      <view class="search-btn" @click="loadData">搜索</view>
    </view>

    <view class="status-filter">
      <view 
        class="filter-item" 
        :class="{ active: currentStatus === '' }"
        @click="filterByStatus('')"
      >全部</view>
      <view 
        class="filter-item" 
        :class="{ active: currentStatus === 'PENDING' }"
        @click="filterByStatus('PENDING')"
      >待处理</view>
      <view 
        class="filter-item" 
        :class="{ active: currentStatus === 'PROCESSING' }"
        @click="filterByStatus('PROCESSING')"
      >处理中</view>
      <view 
        class="filter-item" 
        :class="{ active: currentStatus === 'COMPLETED' }"
        @click="filterByStatus('COMPLETED')"
      >已完成</view>
    </view>

    <view class="list-section" v-if="orderList.length > 0">
      <view 
        class="order-card" 
        v-for="item in orderList" 
        :key="item.id"
        @click="showDetail(item)"
      >
        <view class="card-header">
          <view class="order-info">
            <text class="order-number">{{ item.orderNumber }}</text>
            <text class="customer-name">{{ item.customerName || '未知客户' }}</text>
          </view>
          <view class="status-badge" :class="'status-' + item.status">
            {{ getStatusText(item.status) }}
          </view>
        </view>
        <view class="card-body">
          <view class="info-row">
            <text class="info-label">订单金额</text>
            <text class="info-value amount">¥{{ item.totalAmount || 0 }}</text>
          </view>
          <view class="info-row">
            <text class="info-label">商品数量</text>
            <text class="info-value">{{ item.items?.length || 0 }} 种</text>
          </view>
          <view class="info-row">
            <text class="info-label">下单时间</text>
            <text class="info-value">{{ formatDate(item.createdTime) }}</text>
          </view>
        </view>
        <view class="card-actions">
          <view class="action-btn" @click.stop="showDetail(item)">查看详情</view>
          <view class="action-btn primary" v-if="item.status !== 'COMPLETED'" @click.stop="updateStatus(item)">更新状态</view>
        </view>
      </view>
    </view>

    <view class="empty-state" v-else>
      <text class="empty-icon">📋</text>
      <text class="empty-text">暂无订单记录</text>
    </view>

    <view class="add-btn" @click="addOrder">
      <text class="add-icon">+</text>
    </view>
  </view>
</template>

<script>
import { getOrderList, updateOrderStatus, deleteOrder } from '@/api/order'

export default {
  data() {
    return {
      searchKeyword: '',
      currentStatus: '',
      orderList: []
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
        if (this.currentStatus) {
          params.status = this.currentStatus
        }
        const res = await getOrderList(params)
        this.orderList = res.content || []
      } catch (e) {
        console.error('加载订单列表失败', e)
      }
    },

    // 状态筛选
    filterByStatus(status) {
      this.currentStatus = status
      this.loadData()
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
    },

    // 显示详情
    showDetail(item) {
      uni.navigateTo({
        url: '/pages/order/detail?id=' + item.id
      })
    },

    // 新增订单
    addOrder() {
      uni.navigateTo({
        url: '/pages/order/form'
      })
    },

    // 更新状态
    updateStatus(item) {
      const statusOptions = ['待处理', '处理中', '已完成']
      const statusValues = ['PENDING', 'PROCESSING', 'COMPLETED']
      
      uni.showActionSheet({
        itemList: statusOptions,
        success: async (res) => {
          const newStatus = statusValues[res.tapIndex]
          if (newStatus && newStatus !== item.status) {
            try {
              await updateOrderStatus(item.id, newStatus)
              uni.showToast({ title: '状态更新成功', icon: 'success' })
              this.loadData()
            } catch (e) {
              console.error('更新状态失败', e)
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
  margin-bottom: 20rpx;
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

.status-filter {
  display: flex;
  gap: 16rpx;
  margin-bottom: 30rpx;
  overflow-x: auto;
}

.filter-item {
  flex-shrink: 0;
  padding: 16rpx 30rpx;
  background: #FFFFFF;
  border-radius: 30rpx;
  font-size: 26rpx;
  color: #666666;
}

.filter-item.active {
  background: #007AFF;
  color: #FFFFFF;
}

.order-card {
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
  padding-bottom: 20rpx;
  border-bottom: 1rpx solid #F5F5F5;
}

.order-info {
  display: flex;
  flex-direction: column;
}

.order-number {
  font-size: 30rpx;
  font-weight: 600;
  color: #333333;
  margin-bottom: 6rpx;
}

.customer-name {
  font-size: 24rpx;
  color: #999999;
}

.status-badge {
  padding: 6rpx 16rpx;
  border-radius: 8rpx;
  font-size: 24rpx;
}

.status-PENDING {
  background: #FF95001A;
  color: #FF9500;
}

.status-PROCESSING {
  background: #007AFF1A;
  color: #007AFF;
}

.status-COMPLETED {
  background: #34C7591A;
  color: #34C759;
}

.status-CANCELLED {
  background: #8E8E931A;
  color: #8E8E93;
}

.card-body {
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

.info-value.amount {
  font-weight: 600;
  color: #FF3B30;
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
  background: #F5F5F5;
  color: #666666;
}

.action-btn.primary {
  background: #007AFF1A;
  color: #007AFF;
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
