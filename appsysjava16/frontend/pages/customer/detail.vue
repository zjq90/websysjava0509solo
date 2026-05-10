<template>
  <view class="container" v-if="customer">
    <view class="customer-header">
      <view class="customer-avatar">
        <text class="avatar-text">{{ customer.name.charAt(0) }}</text>
      </view>
      <view class="customer-basic">
        <text class="customer-name">{{ customer.name }}</text>
        <view class="customer-tags">
          <view :class="['level-tag', getLevelClass(customer.level)]">
            {{ getLevelText(customer.level) }}
          </view>
          <view v-if="customer.isTemporary" class="temp-tag">临时客户</view>
        </view>
      </view>
    </view>

    <view class="info-card">
      <view class="card-title">客户信息</view>
      <view class="info-row">
        <text class="label">客户ID</text>
        <text class="value">{{ customer.id }}</text>
      </view>
      <view class="info-row">
        <text class="label">联系电话</text>
        <text class="value">{{ customer.phone }}</text>
      </view>
      <view class="info-row">
        <text class="label">地址</text>
        <text class="value">{{ customer.address || '未填写' }}</text>
      </view>
      <view class="info-row">
        <text class="label">信用额度</text>
        <text class="value">¥{{ customer.creditLimit || 0 }}</text>
      </view>
      <view class="info-row">
        <text class="label">客户折扣</text>
        <text class="value highlight">{{ (getDiscountRate() * 100) }}%</text>
      </view>
      <view class="info-row">
        <text class="label">累计消费</text>
        <text class="value price">¥{{ customer.totalPurchaseAmount || 0 }}</text>
      </view>
    </view>

    <view class="info-card">
      <view class="card-title">历史订单</view>
      <view v-if="customerOrders.length > 0">
        <view 
          class="order-item" 
          v-for="order in customerOrders" 
          :key="order.id"
          @click="goToOrderDetail(order.id)"
        >
          <view class="order-info">
            <text class="order-no">{{ order.orderNo }}</text>
            <view :class="['status-badge', getStatusClass(order.status)]">
              {{ getStatusText(order.status) }}
            </view>
          </view>
          <view class="order-footer">
            <text class="order-time">{{ formatDate(order.createTime) }}</text>
            <text class="order-amount">¥{{ order.totalAmount }}</text>
          </view>
        </view>
      </view>
      <view v-else class="empty-card">暂无订单记录</view>
    </view>

    <view class="info-card">
      <view class="card-title">回访记录</view>
      <view v-if="customer.visitRecords && customer.visitRecords.length > 0">
        <view 
          class="visit-item" 
          v-for="record in customer.visitRecords" 
          :key="record.id"
        >
          <view class="visit-header">
            <text class="visit-time">{{ formatDate(record.visitTime) }}</text>
            <text class="visit-type">{{ getVisitTypeText(record.visitType) }}</text>
          </view>
          <text class="visit-content">{{ record.content }}</text>
        </view>
      </view>
      <view v-else class="empty-card">暂无回访记录</view>
    </view>

    <view class="action-bar">
      <button class="action-btn" @click="addVisit">添加回访</button>
      <button class="action-btn primary" @click="createOrderForCustomer">创建订单</button>
    </view>
  </view>
  <view v-else class="empty">加载中...</view>

  <view v-if="showVisitDialog" class="dialog-mask" @click="showVisitDialog = false">
    <view class="dialog-content" @click.stop>
      <view class="dialog-title">添加回访记录</view>
      <picker :value="visitTypeIndex" :range="visitTypeOptions" range-key="label" @change="onVisitTypeChange">
        <view class="picker-value">回访类型：{{ visitTypeOptions[visitTypeIndex].label }}</view>
      </picker>
      <textarea 
        class="dialog-textarea" 
        placeholder="请输入回访内容" 
        v-model="visitContent"
      ></textarea>
      <view class="dialog-actions">
        <button class="dialog-btn cancel" @click="showVisitDialog = false">取消</button>
        <button class="dialog-btn confirm" @click="submitVisit">确定</button>
      </view>
    </view>
  </view>
</template>

<script>
import customerApi from '@/api/customer.js'
import orderApi from '@/api/order.js'

export default {
  data() {
    return {
      customerId: '',
      customer: null,
      customerOrders: [],
      showVisitDialog: false,
      visitTypeIndex: 0,
      visitTypeOptions: [
        { value: 'PHONE', label: '电话回访' },
        { value: 'VISIT', label: '上门拜访' },
        { value: 'MESSAGE', label: '短信/微信' },
        { value: 'OTHER', label: '其他方式' }
      ],
      visitContent: ''
    }
  },
  onLoad(options) {
    this.customerId = options.id
    this.loadCustomer()
  },
  methods: {
    async loadCustomer() {
      uni.showLoading({ title: '加载中...' })
      try {
        this.customer = await customerApi.getCustomerById(this.customerId)
        this.customerOrders = await customerApi.getCustomerOrders(this.customerId)
      } catch (e) {
        console.error('加载客户失败', e)
      } finally {
        uni.hideLoading()
      }
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
    getLevelClass(level) {
      const map = {
        'TEMPORARY': 'level-temp',
        'NORMAL': 'level-normal',
        'VIP': 'level-vip',
        'SVIP': 'level-svip',
        'DIAMOND': 'level-diamond'
      }
      return map[level] || 'level-normal'
    },
    getDiscountRate() {
      const map = {
        'TEMPORARY': 1.00,
        'NORMAL': 0.95,
        'VIP': 0.90,
        'SVIP': 0.85,
        'DIAMOND': 0.80
      }
      return map[this.customer?.level] || 1.00
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
    getVisitTypeText(type) {
      const map = {
        'PHONE': '电话回访',
        'VISIT': '上门拜访',
        'MESSAGE': '短信/微信',
        'OTHER': '其他方式'
      }
      return map[type] || type
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
    onVisitTypeChange(e) {
      this.visitTypeIndex = e.detail.value
    },
    addVisit() {
      this.visitTypeIndex = 0
      this.visitContent = ''
      this.showVisitDialog = true
    },
    async submitVisit() {
      if (!this.visitContent.trim()) {
        uni.showToast({ title: '请输入回访内容', icon: 'none' })
        return
      }
      uni.showLoading({ title: '提交中...' })
      try {
        await customerApi.addVisitRecord(this.customerId, {
          visitType: this.visitTypeOptions[this.visitTypeIndex].value,
          content: this.visitContent
        })
        uni.showToast({ title: '添加成功', icon: 'success' })
        this.showVisitDialog = false
        this.loadCustomer()
      } catch (e) {
        console.error('添加回访失败', e)
      } finally {
        uni.hideLoading()
      }
    },
    goToOrderDetail(id) {
      uni.navigateTo({ url: `/pages/order/detail?id=${id}` })
    },
    createOrderForCustomer() {
      uni.navigateTo({ url: `/pages/order/create?customerId=${this.customerId}` })
    }
  }
}
</script>

<style scoped>
.customer-header {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  padding: 40rpx;
  border-radius: 16rpx;
  margin-bottom: 24rpx;
  display: flex;
  align-items: center;
}

.customer-avatar {
  width: 120rpx;
  height: 120rpx;
  border-radius: 50%;
  background: rgba(255, 255, 255, 0.2);
  display: flex;
  align-items: center;
  justify-content: center;
  margin-right: 24rpx;
}

.avatar-text {
  font-size: 48rpx;
  font-weight: bold;
  color: #fff;
}

.customer-basic {
  flex: 1;
}

.customer-name {
  font-size: 36rpx;
  font-weight: bold;
  color: #fff;
  display: block;
  margin-bottom: 12rpx;
}

.customer-tags {
  display: flex;
  gap: 12rpx;
}

.level-tag,
.temp-tag {
  font-size: 22rpx;
  padding: 6rpx 16rpx;
  border-radius: 8rpx;
}

.level-temp { background: #f5f5f5; color: #999; }
.level-normal { background: #e3f2fd; color: #1565c0; }
.level-vip { background: #fff8e1; color: #ff8f00; }
.level-svip { background: #fce4ec; color: #c2185b; }
.level-diamond { background: #e8eaf6; color: #283593; }

.temp-tag {
  background: #fff3e0;
  color: #e65100;
}

.info-card {
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

.info-row .value.highlight {
  color: #ff4d4f;
}

.info-row .value.price {
  font-size: 32rpx;
  font-weight: bold;
  color: #ff4d4f;
}

.order-item {
  padding: 20rpx 0;
  border-bottom: 2rpx solid #f5f5f5;
}

.order-item:last-child {
  border-bottom: none;
}

.order-info {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 12rpx;
}

.order-no {
  font-size: 28rpx;
  color: #333;
}

.status-badge {
  font-size: 22rpx;
  padding: 4rpx 12rpx;
  border-radius: 8rpx;
}

.status-warning { background: #fff8e1; color: #ff8f00; }
.status-primary { background: #e3f2fd; color: #1565c0; }
.status-success { background: #e8f5e9; color: #2e7d32; }
.status-info { background: #f3e5f5; color: #7b1fa2; }
.status-shipping { background: #e0f7fa; color: #00838f; }
.status-error { background: #ffebee; color: #c62828; }
.status-default { background: #f5f5f5; color: #666; }

.order-footer {
  display: flex;
  justify-content: space-between;
}

.order-time {
  font-size: 24rpx;
  color: #999;
}

.order-amount {
  font-size: 28rpx;
  color: #ff4d4f;
  font-weight: 500;
}

.visit-item {
  padding: 20rpx 0;
  border-bottom: 2rpx solid #f5f5f5;
}

.visit-item:last-child {
  border-bottom: none;
}

.visit-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 12rpx;
}

.visit-time {
  font-size: 26rpx;
  color: #999;
}

.visit-type {
  font-size: 22rpx;
  color: #667eea;
  background: #f5f7ff;
  padding: 4rpx 12rpx;
  border-radius: 8rpx;
}

.visit-content {
  font-size: 28rpx;
  color: #666;
  line-height: 1.6;
}

.empty-card {
  text-align: center;
  padding: 40rpx;
  color: #999;
  font-size: 28rpx;
}

.action-bar {
  display: flex;
  gap: 20rpx;
  margin-top: 24rpx;
}

.action-btn {
  flex: 1;
  height: 88rpx;
  line-height: 88rpx;
  border-radius: 44rpx;
  font-size: 30rpx;
  border: none;
  background: #f5f5f5;
  color: #666;
}

.action-btn.primary {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: #fff;
}

.dialog-mask {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0, 0, 0, 0.5);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 999;
}

.dialog-content {
  width: 600rpx;
  background: #fff;
  border-radius: 16rpx;
  padding: 40rpx;
}

.dialog-title {
  font-size: 32rpx;
  font-weight: bold;
  text-align: center;
  margin-bottom: 30rpx;
  color: #333;
}

.picker-value {
  height: 80rpx;
  line-height: 80rpx;
  background: #f5f5f5;
  border-radius: 12rpx;
  padding: 0 24rpx;
  font-size: 28rpx;
  margin-bottom: 20rpx;
}

.dialog-textarea {
  width: 100%;
  min-height: 200rpx;
  background: #f5f5f5;
  border-radius: 12rpx;
  padding: 20rpx;
  font-size: 28rpx;
  box-sizing: border-box;
}

.dialog-actions {
  display: flex;
  gap: 20rpx;
  margin-top: 30rpx;
}

.dialog-btn {
  flex: 1;
  height: 80rpx;
  line-height: 80rpx;
  border-radius: 40rpx;
  font-size: 28rpx;
  border: none;
}

.dialog-btn.cancel {
  background: #f5f5f5;
  color: #666;
}

.dialog-btn.confirm {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: #fff;
}
</style>
