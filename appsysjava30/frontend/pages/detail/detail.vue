<template>
  <view class="detail-container" :class="{ 'elderly-mode': isElderlyMode }">
    <view class="status-card card" v-if="detail">
      <view class="status-header">
        <text :class="['status-tag', getStatusClass(detail.status)]">{{ detail.statusDesc }}</text>
        <text class="tag tag-hightlight" v-if="detail.statusTag">{{ detail.statusTag }}</text>
      </view>
      <view class="qr-section" v-if="detail.qrCode">
        <image :src="'data:image/png;base64,' + detail.qrCode" class="qr-code" mode="aspectFit" />
        <text class="qr-tip">请出示此二维码供现场扫码核验</text>
      </view>
      <view class="reg-no">挂号单号：{{ detail.registrationNo }}</view>
    </view>
    
    <view class="info-card card" v-if="detail">
      <view class="card-title">就诊信息</view>
      <view class="info-row">
        <text class="info-label">就诊科室</text>
        <text class="info-value">{{ detail.deptName }}</text>
      </view>
      <view class="info-row">
        <text class="info-label">就诊医生</text>
        <text class="info-value">{{ detail.doctorName }} {{ detail.doctorTitle }}</text>
      </view>
      <view class="info-row">
        <text class="info-label">就诊日期</text>
        <text class="info-value">{{ detail.visitDate }}</text>
      </view>
      <view class="info-row">
        <text class="info-label">就诊时间</text>
        <text class="info-value">{{ detail.timeSlot }}</text>
      </view>
      <view class="info-row" v-if="detail.symptoms">
        <text class="info-label">症状描述</text>
        <text class="info-value">{{ detail.symptoms }}</text>
      </view>
    </view>
    
    <view class="info-card card" v-if="detail">
      <view class="card-title">费用信息</view>
      <view class="info-row">
        <text class="info-label">挂号费用</text>
        <text class="info-value price">¥{{ detail.amount }}</text>
      </view>
      <view class="info-row">
        <text class="info-label">支付方式</text>
        <text class="info-value">{{ getPaymentMethodText(detail.paymentMethod) }}</text>
      </view>
      <view class="info-row">
        <text class="info-label">支付状态</text>
        <text class="info-value">{{ getPaymentStatusText(detail.paymentStatus) }}</text>
      </view>
      <view class="info-row" v-if="detail.refundStatus">
        <text class="info-label">退款状态</text>
        <text class="info-value">{{ getRefundStatusText(detail.refundStatus) }}</text>
      </view>
    </view>
    
    <view class="tip-card" v-if="detail">
      <view class="card-title">温馨提示</view>
      <view class="tip-item">
        <text class="tip-dot">•</text>
        <text class="tip-text">请提前15分钟到达医院</text>
      </view>
      <view class="tip-item" v-if="detail.canCancel">
        <text class="tip-dot">•</text>
        <text class="tip-text">{{ detail.cancelDeadlineDesc }}</text>
      </view>
      <view class="tip-item">
        <text class="tip-dot">•</text>
        <text class="tip-text">如需改约，请先取消再重新预约</text>
      </view>
      <view class="tip-item" v-if="detail.refundStatus === 'REFUNDING'">
        <text class="tip-dot">•</text>
        <text class="tip-text">退款将原路退回，微信/支付宝一般1-3个工作日到账</text>
      </view>
    </view>
    
    <view class="footer-actions" v-if="detail">
      <view 
        v-if="detail.paymentStatus === 'UNPAID'" 
        class="action-btn pay"
        @click="handlePay"
      >
        去支付
      </view>
      <view 
        v-if="detail.canCancel" 
        class="action-btn cancel"
        @click="handleCancel"
      >
        取消预约
      </view>
    </view>
    
    <view class="test-actions" v-if="detail && showTestActions">
      <view class="section-title">测试辅助功能</view>
      <view class="action-btn default" @click="simulateRefund">模拟退费完成</view>
      <view class="action-btn default" @click="simulateVisitComplete">模拟就诊完成</view>
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
          <text class="amount">¥{{ detail && detail.amount }}</text>
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
import { registrationApi, testApi } from '@/utils/api.js'

export default {
  data() {
    return {
      registrationNo: '',
      detail: null,
      paymentMethod: 'WECHAT',
      showPayment: false,
      showTestActions: false,
      isElderlyMode: false
    }
  },
  
  onLoad(options) {
    this.registrationNo = options.registrationNo
    this.isElderlyMode = uni.getStorageSync('elderlyMode') || false
    this.loadDetail()
  },
  
  methods: {
    async loadDetail() {
      uni.showLoading({ title: '加载中...' })
      try {
        const res = await registrationApi.detail(this.registrationNo)
        this.detail = res.data
        this.checkShowTestActions()
      } catch (e) {}
      uni.hideLoading()
    },
    
    checkShowTestActions() {
      const userInfo = uni.getStorageSync('userInfo')
      this.showTestActions = true
    },
    
    getStatusClass(status) {
      const map = {
        'BOOKED': 'success',
        'VISITING': 'info',
        'VISITED': 'info',
        'CANCELLED': 'default',
        'REFUNDING': 'warning'
      }
      return map[status] || 'default'
    },
    
    getPaymentMethodText(method) {
      const map = {
        'WECHAT': '微信支付',
        'ALIPAY': '支付宝'
      }
      return map[method] || '-'
    },
    
    getPaymentStatusText(status) {
      const map = {
        'UNPAID': '待支付',
        'PAID': '已支付',
        'REFUNDING': '退费中',
        'REFUNDED': '已退款'
      }
      return map[status] || status
    },
    
    getRefundStatusText(status) {
      const map = {
        'REFUNDING': '退费中',
        'REFUNDED': '已退款'
      }
      return map[status] || status
    },
    
    handlePay() {
      this.showPayment = true
    },
    
    handleCancel() {
      uni.showModal({
        title: '确认取消',
        content: '确定要取消该预约吗？取消后如需再次就诊需重新预约。',
        confirmText: '确认取消',
        cancelText: '再想想',
        success: async (res) => {
          if (res.confirm) {
            uni.showLoading({ title: '处理中...' })
            try {
              await registrationApi.cancel(this.registrationNo)
              uni.hideLoading()
              uni.showToast({ title: '取消成功', icon: 'success' })
              this.loadDetail()
            } catch (e) {
              uni.hideLoading()
            }
          }
        }
      })
    },
    
    async pay() {
      uni.showLoading({ title: '支付中...' })
      try {
        await registrationApi.pay({
          registrationNo: this.registrationNo,
          paymentMethod: this.paymentMethod
        })
        uni.hideLoading()
        this.showPayment = false
        uni.showToast({ title: '支付成功', icon: 'success' })
        this.loadDetail()
      } catch (e) {
        uni.hideLoading()
      }
    },
    
    async simulateRefund() {
      uni.showLoading({ title: '处理中...' })
      try {
        await testApi.simulateRefund(this.registrationNo)
        uni.hideLoading()
        uni.showToast({ title: '模拟退费成功', icon: 'success' })
        this.loadDetail()
      } catch (e) {
        uni.hideLoading()
      }
    },
    
    async simulateVisitComplete() {
      uni.showLoading({ title: '处理中...' })
      try {
        await testApi.simulateVisitComplete(this.registrationNo)
        uni.hideLoading()
        uni.showToast({ title: '模拟就诊完成', icon: 'success' })
        this.loadDetail()
      } catch (e) {
        uni.hideLoading()
      }
    }
  }
}
</script>

<style scoped>
.detail-container {
  min-height: 100vh;
  background: #f5f5f5;
  padding: 20rpx;
  padding-bottom: 160rpx;
}

.status-card {
  text-align: center;
  padding: 40rpx;
}

.status-header {
  display: flex;
  justify-content: center;
  align-items: center;
  gap: 16rpx;
  margin-bottom: 32rpx;
}

.status-tag {
  font-size: 36rpx;
  font-weight: bold;
  padding: 8rpx 24rpx;
  border-radius: 8rpx;
}

.status-tag.success {
  background: #f6ffed;
  color: #52c41a;
}

.status-tag.info {
  background: #e6f7ff;
  color: #1890ff;
}

.status-tag.default {
  background: #fafafa;
  color: #999;
}

.status-tag.warning {
  background: #fffbe6;
  color: #faad14;
}

.qr-section {
  margin: 32rpx 0;
}

.qr-code {
  width: 320rpx;
  height: 320rpx;
  margin: 0 auto;
  background: #fff;
  border: 2rpx solid #f0f0f0;
  border-radius: 16rpx;
  padding: 20rpx;
}

.qr-tip {
  display: block;
  font-size: 24rpx;
  color: #999;
  margin-top: 16rpx;
}

.reg-no {
  font-size: 26rpx;
  color: #999;
}

.info-card {
  margin-top: 20rpx;
}

.card-title {
  font-size: 30rpx;
  font-weight: bold;
  color: #333;
  margin-bottom: 24rpx;
  padding-bottom: 16rpx;
  border-bottom: 2rpx solid #f0f0f0;
}

.info-row {
  display: flex;
  margin-bottom: 20rpx;
}

.info-row:last-child {
  margin-bottom: 0;
}

.info-label {
  width: 160rpx;
  font-size: 28rpx;
  color: #666;
  flex-shrink: 0;
}

.info-value {
  flex: 1;
  font-size: 28rpx;
  color: #333;
}

.info-value.price {
  color: #ff4d4f;
  font-weight: bold;
}

.tip-card {
  margin-top: 20rpx;
  padding: 24rpx;
  background: #fffbe6;
  border-radius: 16rpx;
}

.tip-item {
  display: flex;
  align-items: flex-start;
  margin-bottom: 16rpx;
}

.tip-item:last-child {
  margin-bottom: 0;
}

.tip-dot {
  color: #faad14;
  margin-right: 8rpx;
}

.tip-text {
  flex: 1;
  font-size: 26rpx;
  color: #faad14;
  line-height: 1.6;
}

.footer-actions {
  position: fixed;
  bottom: 0;
  left: 0;
  right: 0;
  padding: 20rpx;
  background: #fff;
  box-shadow: 0 -4rpx 12rpx rgba(0, 0, 0, 0.06);
  display: flex;
  gap: 20rpx;
}

.action-btn {
  flex: 1;
  padding: 28rpx;
  text-align: center;
  border-radius: 48rpx;
  font-size: 30rpx;
}

.action-btn.pay {
  background: linear-gradient(135deg, #1890ff 0%, #096dd9 100%);
  color: #fff;
}

.action-btn.cancel {
  background: #fff2f0;
  color: #ff4d4f;
  border: 2rpx solid #ffccc7;
}

.action-btn.default {
  background: #fafafa;
  color: #666;
  border: 2rpx solid #d9d9d9;
  margin-top: 16rpx;
}

.test-actions {
  margin-top: 32rpx;
  padding: 24rpx;
  background: #fff;
  border-radius: 16rpx;
  border: 2rpx dashed #1890ff;
}

.section-title {
  font-size: 28rpx;
  color: #1890ff;
  margin-bottom: 16rpx;
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
