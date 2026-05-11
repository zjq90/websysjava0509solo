<template>
  <view class="page-container" :class="{ 'elder-mode-enabled': elderMode }">
    <view class="payment-section" v-if="appointment">
      <view class="order-info">
        <view class="info-item">
          <text class="label">就诊人</text>
          <text class="value">{{ appointment.patientName }}</text>
        </view>
        <view class="info-item">
          <text class="label">医生</text>
          <text class="value">{{ appointment.doctorName }}</text>
        </view>
        <view class="info-item">
          <text class="label">科室</text>
          <text class="value">{{ appointment.deptName }}</text>
        </view>
        <view class="info-item">
          <text class="label">就诊时间</text>
          <text class="value">{{ appointment.appointmentDate }} {{ appointment.startTime }}-{{ appointment.endTime }}</text>
        </view>
      </view>

      <view class="price-info">
        <view class="price-row">
          <text class="label">挂号费</text>
          <text class="value">¥{{ appointment.totalAmount }}</text>
        </view>
        <view class="price-row" v-if="useInsurance && appointment.insuranceAmount > 0">
          <text class="label">医保报销</text>
          <text class="value refund">-¥{{ appointment.insuranceAmount }}</text>
        </view>
        <view class="price-row total">
          <text class="label">应付金额</text>
          <text class="value total-price">¥{{ appointment.payableAmount }}</text>
        </view>
      </view>

      <view class="payment-method">
        <view class="method-item" :class="{ active: paymentMethod === 'WECHAT' }">
          <view class="method-left">
            <text class="method-icon">💚</text>
            <text class="method-name">微信支付</text>
          </view>
          <view class="radio" :class="{ checked: paymentMethod === 'WECHAT' }" @click="paymentMethod = 'WECHAT'">
            <text v-if="paymentMethod === 'WECHAT'">✓</text>
          </view>
        </view>

        <view class="method-item" :class="{ active: paymentMethod === 'ALIPAY' }">
          <view class="method-left">
            <text class="method-icon">💙</text>
            <text class="method-name">支付宝</text>
          </view>
          <view class="radio" :class="{ checked: paymentMethod === 'ALIPAY' }" @click="paymentMethod = 'ALIPAY'">
            <text v-if="paymentMethod === 'ALIPAY'">✓</text>
          </view>
        </view>

        <view class="method-item" :class="{ active: paymentMethod === 'UNIONPAY' }">
          <view class="method-left">
            <text class="method-icon">💳</text>
            <text class="method-name">银联云闪付</text>
          </view>
          <view class="radio" :class="{ checked: paymentMethod === 'UNIONPAY' }" @click="paymentMethod = 'UNIONPAY'">
            <text v-if="paymentMethod === 'UNIONPAY'">✓</text>
          </view>
        </view>
      </view>

      <view class="lock-tip">
        <text class="lock-icon">🔒</text>
        <text class="lock-text">支付将自动释放号源锁定</text>
      </view>
    </view>

    <view class="bottom-bar">
      <view class="pay-btn" @click="handlePayment" :class="{ disabled: paying }">
        <text v-if="!paying">立即支付 ¥{{ appointment?.payableAmount || 0 }}</text>
        <text v-else>支付中...</text>
      </view>
    </view>

    <view class="result-popup" v-if="showResult">
      <view class="result-content" :class="{ success: paySuccess }">
        <text class="result-icon">{{ paySuccess ? '✅' : '❌' }}</text>
        <text class="result-title">{{ paySuccess ? '支付成功' : '支付失败' }}</text>
        <text class="result-desc" v-if="paySuccess">预约已确认，请按时就诊</text>
        <text class="result-desc" v-else>号源已释放，请重新预约</text>
        <view class="result-actions">
          <view class="action-btn" @click="goToAppointments">
            <text>查看预约</text>
          </view>
          <view class="action-btn primary" @click="goToIndex" v-if="!paySuccess">
            <text>返回首页</text>
          </view>
        </view>
      </view>
    </view>
  </view>
</template>

<script setup>
import { ref, computed, onMounted, onLoad } from 'vue'
import { useStore } from 'vuex'
import { appointmentApi } from '@/utils/api'

const store = useStore()

const appointmentId = ref(null)
const appointment = ref(null)
const paymentMethod = ref('WECHAT')
const useInsurance = ref(false)
const paying = ref(false)
const showResult = ref(false)
const paySuccess = ref(false)

const elderMode = computed(() => store.getters.elderMode)

onLoad((options) => {
  appointmentId.value = options.id
  paymentMethod.value = options.payment || 'WECHAT'
  useInsurance.value = options.insurance === 'true'
})

const loadAppointment = async () => {
  try {
    appointment.value = await appointmentApi.getDetail(appointmentId.value)
  } catch (e) {
    console.error('加载预约信息失败:', e)
  }
}

const handlePayment = async () => {
  if (paying.value) return

  paying.value = true
  try {
    await appointmentApi.pay(appointmentId.value, {
      paymentMethod: paymentMethod.value,
      useInsurance: useInsurance.value
    })
    
    paySuccess.value = true
  } catch (e) {
    paySuccess.value = false
  } finally {
    paying.value = false
    showResult.value = true
  }
}

const goToAppointments = () => {
  uni.switchTab({ url: '/pages/appointment/appointment-list' })
}

const goToIndex = () => {
  uni.switchTab({ url: '/pages/index/index' })
}

onMounted(() => {
  loadAppointment()
})
</script>

<style lang="scss" scoped>
.page-container {
  min-height: 100vh;
  background: #f5f5f5;
  padding-bottom: 140rpx;
}

.payment-section {
  background: #fff;
  margin: 20rpx;
  border-radius: 16rpx;
  padding: 24rpx;
}

.order-info {
  padding-bottom: 24rpx;
  border-bottom: 2rpx solid #f0f0f0;
}

.info-item {
  display: flex;
  justify-content: space-between;
  padding: 16rpx 0;
}

.label {
  font-size: 26rpx;
  color: #999;
}

.value {
  font-size: 26rpx;
  color: #333;
}

.price-info {
  padding: 24rpx 0;
  border-bottom: 2rpx solid #f0f0f0;
}

.price-row {
  display: flex;
  justify-content: space-between;
  padding: 12rpx 0;
}

.price-row.total {
  padding-top: 20rpx;
  margin-top: 12rpx;
  border-top: 2rpx dashed #f0f0f0;
}

.value.refund {
  color: #52c41a;
}

.value.total-price {
  font-size: 40rpx;
  font-weight: bold;
  color: #ff4d4f;
}

.payment-method {
  padding-top: 24rpx;
}

.method-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 24rpx;
  margin-bottom: 16rpx;
  background: #fafafa;
  border-radius: 12rpx;
  border: 2rpx solid transparent;
}

.method-item.active {
  border-color: #1890ff;
  background: #e6f7ff;
}

.method-left {
  display: flex;
  align-items: center;
  gap: 16rpx;
}

.method-icon {
  font-size: 44rpx;
}

.method-name {
  font-size: 28rpx;
  color: #333;
}

.radio {
  width: 40rpx;
  height: 40rpx;
  border: 2rpx solid #d9d9d9;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 24rpx;
  color: #fff;
}

.radio.checked {
  background: #1890ff;
  border-color: #1890ff;
}

.lock-tip {
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 20rpx;
  margin-top: 24rpx;
  background: #e6f7ff;
  border-radius: 12rpx;
}

.lock-icon {
  font-size: 28rpx;
  margin-right: 8rpx;
}

.lock-text {
  font-size: 24rpx;
  color: #1890ff;
}

.bottom-bar {
  position: fixed;
  bottom: 0;
  left: 0;
  right: 0;
  background: #fff;
  padding: 20rpx 24rpx;
  padding-bottom: calc(20rpx + env(safe-area-inset-bottom));
  box-shadow: 0 -4rpx 16rpx rgba(0, 0, 0, 0.05);
}

.pay-btn {
  height: 88rpx;
  line-height: 88rpx;
  background: #1890ff;
  color: #fff;
  text-align: center;
  border-radius: 44rpx;
  font-size: 32rpx;
  font-weight: bold;
}

.pay-btn.disabled {
  opacity: 0.6;
}

.result-popup {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0, 0, 0, 0.5);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 100;
}

.result-content {
  width: 600rpx;
  background: #fff;
  border-radius: 24rpx;
  padding: 60rpx 40rpx;
  text-align: center;
}

.result-icon {
  font-size: 100rpx;
  display: block;
  margin-bottom: 30rpx;
}

.result-title {
  display: block;
  font-size: 36rpx;
  font-weight: bold;
  color: #333;
  margin-bottom: 16rpx;
}

.result-desc {
  display: block;
  font-size: 28rpx;
  color: #999;
  margin-bottom: 40rpx;
}

.result-actions {
  display: flex;
  gap: 20rpx;
}

.action-btn {
  flex: 1;
  height: 80rpx;
  line-height: 80rpx;
  background: #f5f5f5;
  border-radius: 40rpx;
  font-size: 28rpx;
  color: #666;
}

.action-btn.primary {
  background: #1890ff;
  color: #fff;
}

.elder-mode-enabled {
  .total-price {
    font-size: 44rpx;
  }
  
  .method-name {
    font-size: 32rpx;
  }
}
</style>
