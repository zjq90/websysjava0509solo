<template>
  <view class="record-container" :class="{ 'elderly-mode': isElderlyMode }">
    <view class="filter-tabs">
      <view 
        v-for="(tab, idx) in tabs" 
        :key="idx"
        :class="['tab-item', activeTab === idx ? 'active' : '']"
        @click="switchTab(idx)"
      >
        <text>{{ tab.name }}</text>
        <view class="tab-badge" v-if="tab.count > 0">{{ tab.count }}</view>
      </view>
    </view>
    
    <view class="record-list" v-if="filteredRecords.length > 0">
      <view 
        v-for="item in filteredRecords" 
        :key="item.id" 
        class="record-item card"
        @click="goDetail(item.registrationNo)"
      >
        <view class="item-header">
          <view class="dept-doctor">
            <text class="dept">{{ item.deptName }}</text>
            <text class="doctor">{{ item.doctorName }} {{ item.doctorTitle }}</text>
          </view>
          <view class="status-area">
            <text :class="['tag', getStatusClass(item.status)]">{{ item.statusDesc }}</text>
            <text class="tag tag-hightlight" v-if="item.statusTag">{{ item.statusTag }}</text>
          </view>
        </view>
        
        <view class="item-body">
          <view class="info-row">
            <text class="info-label">就诊日期</text>
            <text class="info-value">{{ item.visitDate }} {{ item.timeSlot }}</text>
          </view>
          <view class="info-row">
            <text class="info-label">挂号单号</text>
            <text class="info-value">{{ item.registrationNo }}</text>
          </view>
          <view class="info-row">
            <text class="info-label">费用</text>
            <text class="info-value price">¥{{ item.amount }}</text>
          </view>
          <view class="info-row" v-if="item.cancelDeadlineDesc">
            <text class="info-label">取消说明</text>
            <text :class="['info-value', item.canCancel ? '' : 'text-danger']">{{ item.cancelDeadlineDesc }}</text>
          </view>
        </view>
        
        <view class="item-footer">
          <view class="actions">
            <view 
              v-if="item.canCancel" 
              class="action-btn cancel"
              @click.stop="handleCancel(item)"
            >
              取消预约
            </view>
            <view 
              v-if="item.paymentStatus === 'UNPAID'" 
              class="action-btn pay"
              @click.stop="handlePay(item)"
            >
              去支付
            </view>
            <view 
              class="action-btn detail"
              @click="goDetail(item.registrationNo)"
            >
              查看详情
            </view>
          </view>
        </view>
      </view>
    </view>
    
    <view class="empty" v-else>
      <text class="empty-icon">📋</text>
      <text class="empty-text">暂无预约记录</text>
      <view class="btn-primary empty-btn" @click="goRegistration">去挂号</view>
    </view>
    
    <view class="payment-modal" v-if="showPayment">
      <view class="payment-mask" @click="showPayment = false"></view>
      <view class="payment-content">
        <view class="payment-header">
          <text class="payment-title">选择支付方式</text>
          <text class="close" @click="showPayment = false">×</text>
        </view>
        <view class="payment-amount">
          <text class="label">挂号费用</text>
          <text class="amount">¥{{ currentRecord && currentRecord.amount }}</text>
        </view>
        <view class="payment-methods">
          <view 
            :class="['method-item', paymentMethod === 'WECHAT' ? 'selected' : '']"
            @click="paymentMethod = 'WECHAT'"
          >
            <text class="method-icon">💚</text>
            <text class="method-name">微信支付</text>
            <text class="radio" v-if="paymentMethod === 'WECHAT'">●</text>
          </view>
          <view 
            :class="['method-item', paymentMethod === 'ALIPAY' ? 'selected' : '']"
            @click="paymentMethod = 'ALIPAY'"
          >
            <text class="method-icon">💙</text>
            <text class="method-name">支付宝</text>
            <text class="radio" v-if="paymentMethod === 'ALIPAY'">●</text>
          </view>
        </view>
        <view class="btn-primary" @click="pay">确认支付</view>
      </view>
    </view>
  </view>
</template>

<script>
import { registrationApi } from '@/utils/api.js'

export default {
  data() {
    return {
      tabs: [
        { name: '全部', value: 'ALL', count: 0 },
        { name: '待就诊', value: 'UPCOMING', count: 0 },
        { name: '已完成', value: 'COMPLETED', count: 0 },
        { name: '已取消', value: 'CANCELLED', count: 0 }
      ],
      activeTab: 0,
      records: [],
      currentRecord: null,
      paymentMethod: 'WECHAT',
      showPayment: false,
      isElderlyMode: false
    }
  },
  
  computed: {
    filteredRecords() {
      const status = this.tabs[this.activeTab].value
      if (status === 'ALL') return this.records
      if (status === 'UPCOMING') {
        return this.records.filter(r => ['BOOKED', 'VISITING'].includes(r.status))
      }
      if (status === 'COMPLETED') {
        return this.records.filter(r => r.status === 'VISITED')
      }
      if (status === 'CANCELLED') {
        return this.records.filter(r => ['CANCELLED', 'REFUNDING'].includes(r.status))
      }
      return this.records
    }
  },
  
  onShow() {
    this.isElderlyMode = uni.getStorageSync('elderlyMode') || false
    this.loadRecords()
  },
  
  methods: {
    async loadRecords() {
      uni.showLoading({ title: '加载中...' })
      try {
        const res = await registrationApi.list(0, 50)
        this.records = res.data && res.data.content ? res.data.content : []
        this.updateTabCounts()
      } catch (e) {}
      uni.hideLoading()
    },
    
    updateTabCounts() {
      this.tabs[0].count = this.records.length
      this.tabs[1].count = this.records.filter(r => ['BOOKED', 'VISITING'].includes(r.status)).length
      this.tabs[2].count = this.records.filter(r => r.status === 'VISITED').length
      this.tabs[3].count = this.records.filter(r => ['CANCELLED', 'REFUNDING'].includes(r.status)).length
    },
    
    switchTab(idx) {
      this.activeTab = idx
    },
    
    getStatusClass(status) {
      const map = {
        'BOOKED': 'tag-success',
        'VISITING': 'tag-info',
        'VISITED': 'tag-info',
        'CANCELLED': 'tag-default',
        'REFUNDING': 'tag-warning'
      }
      return map[status] || 'tag-default'
    },
    
    goDetail(registrationNo) {
      uni.navigateTo({ url: `/pages/detail/detail?registrationNo=${registrationNo}` })
    },
    
    goRegistration() {
      uni.switchTab({ url: '/pages/index/index' })
      setTimeout(() => {
        uni.navigateTo({ url: '/pages/registration/registration' })
      }, 100)
    },
    
    handleCancel(item) {
      uni.showModal({
        title: '确认取消',
        content: '确定要取消该预约吗？取消后如需再次就诊需重新预约。',
        confirmText: '确认取消',
        cancelText: '再想想',
        success: async (res) => {
          if (res.confirm) {
            uni.showLoading({ title: '处理中...' })
            try {
              await registrationApi.cancel(item.registrationNo)
              uni.hideLoading()
              uni.showToast({ title: '取消成功', icon: 'success' })
              this.loadRecords()
            } catch (e) {
              uni.hideLoading()
            }
          }
        }
      })
    },
    
    handlePay(item) {
      this.currentRecord = item
      this.showPayment = true
    },
    
    async pay() {
      uni.showLoading({ title: '支付中...' })
      try {
        await registrationApi.pay({
          registrationNo: this.currentRecord.registrationNo,
          paymentMethod: this.paymentMethod
        })
        uni.hideLoading()
        this.showPayment = false
        uni.showToast({ title: '支付成功', icon: 'success' })
        this.loadRecords()
      } catch (e) {
        uni.hideLoading()
      }
    }
  }
}
</script>

<style scoped>
.record-container {
  min-height: 100vh;
  background: #f5f5f5;
  padding-bottom: 120rpx;
}

.filter-tabs {
  display: flex;
  background: #fff;
  padding: 0 8rpx;
  position: sticky;
  top: 0;
  z-index: 100;
}

.tab-item {
  flex: 1;
  position: relative;
  padding: 28rpx 0;
  text-align: center;
  font-size: 28rpx;
  color: #666;
}

.tab-item.active {
  color: #1890ff;
  font-weight: bold;
}

.tab-item.active::after {
  content: '';
  position: absolute;
  bottom: 0;
  left: 50%;
  transform: translateX(-50%);
  width: 48rpx;
  height: 6rpx;
  background: #1890ff;
  border-radius: 3rpx;
}

.tab-badge {
  position: absolute;
  top: 16rpx;
  right: 20%;
  min-width: 32rpx;
  height: 32rpx;
  line-height: 32rpx;
  background: #ff4d4f;
  color: #fff;
  font-size: 20rpx;
  border-radius: 16rpx;
  padding: 0 8rpx;
}

.record-list {
  padding: 20rpx;
}

.record-item {
  margin-bottom: 20rpx;
}

.item-header {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  margin-bottom: 20rpx;
}

.dept-doctor {
  flex: 1;
}

.dept {
  display: block;
  font-size: 32rpx;
  font-weight: bold;
  color: #333;
  margin-bottom: 8rpx;
}

.doctor {
  font-size: 26rpx;
  color: #666;
}

.status-area {
  display: flex;
  flex-direction: column;
  align-items: flex-end;
  gap: 8rpx;
}

.item-body {
  background: #fafafa;
  border-radius: 12rpx;
  padding: 20rpx;
  margin-bottom: 20rpx;
}

.info-row {
  display: flex;
  margin-bottom: 12rpx;
}

.info-row:last-child {
  margin-bottom: 0;
}

.info-label {
  width: 140rpx;
  font-size: 26rpx;
  color: #999;
  flex-shrink: 0;
}

.info-value {
  flex: 1;
  font-size: 26rpx;
  color: #333;
}

.info-value.price {
  color: #ff4d4f;
  font-weight: bold;
}

.info-value.text-danger {
  color: #ff4d4f;
}

.item-footer {
  display: flex;
  justify-content: flex-end;
}

.actions {
  display: flex;
  gap: 16rpx;
}

.action-btn {
  padding: 12rpx 32rpx;
  border-radius: 24rpx;
  font-size: 26rpx;
}

.action-btn.cancel {
  background: #fff2f0;
  color: #ff4d4f;
  border: 2rpx solid #ffccc7;
}

.action-btn.pay {
  background: #e6f7ff;
  color: #1890ff;
  border: 2rpx solid #91d5ff;
}

.action-btn.detail {
  background: #fafafa;
  color: #666;
  border: 2rpx solid #d9d9d9;
}

.empty {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 160rpx 0;
}

.empty-icon {
  font-size: 120rpx;
  margin-bottom: 24rpx;
}

.empty-text {
  font-size: 28rpx;
  color: #999;
  margin-bottom: 40rpx;
}

.empty-btn {
  width: 240rpx;
}

.payment-modal {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  z-index: 1000;
}

.payment-mask {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0, 0, 0, 0.5);
}

.payment-content {
  position: absolute;
  bottom: 0;
  left: 0;
  right: 0;
  background: #fff;
  border-radius: 24rpx 24rpx 0 0;
  padding: 32rpx;
}

.payment-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 32rpx;
}

.payment-title {
  font-size: 32rpx;
  font-weight: bold;
}

.close {
  font-size: 48rpx;
  color: #999;
}

.payment-amount {
  text-align: center;
  padding: 40rpx 0;
  border-bottom: 2rpx solid #f0f0f0;
}

.payment-amount .label {
  display: block;
  font-size: 26rpx;
  color: #666;
  margin-bottom: 12rpx;
}

.payment-amount .amount {
  font-size: 48rpx;
  font-weight: bold;
  color: #ff4d4f;
}

.payment-methods {
  padding: 24rpx 0;
}

.method-item {
  display: flex;
  align-items: center;
  padding: 24rpx 0;
  border-bottom: 2rpx solid #f0f0f0;
}

.method-item:last-child {
  border-bottom: none;
}

.method-item.selected {
  background: #fafafa;
}

.method-icon {
  font-size: 40rpx;
  margin-right: 16rpx;
}

.method-name {
  flex: 1;
  font-size: 30rpx;
  color: #333;
}

.radio {
  color: #1890ff;
  font-size: 24rpx;
}
</style>
