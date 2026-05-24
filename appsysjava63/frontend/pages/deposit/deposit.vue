<template>
  <view class="deposit-container">
    <view class="deposit-card">
      <view class="deposit-header">
        <text class="title">押金管理</text>
      </view>

      <view class="status-section" v-if="hasPaid">
        <view class="status-icon paid">✓</view>
        <text class="status-text">已缴纳押金</text>
        <text class="deposit-amount">¥199.00</text>
      </view>

      <view class="status-section" v-else>
        <view class="status-icon unpaid">💰</view>
        <text class="status-text">未缴纳押金</text>
        <text class="deposit-amount">¥199.00</text>
      </view>

      <view class="credit-section">
        <view class="credit-header">
          <text class="credit-title">信用免押</text>
          <text class="credit-score">芝麻信用 {{ creditScore }}分</text>
        </view>
        <view class="credit-bar">
          <view class="credit-progress" :style="{ width: (creditScore / 950 * 100) + '%' }"></view>
          <view class="credit-marker" :style="{ left: '68.4%' }">
            <text class="marker-text">650分免押</text>
          </view>
        </view>
        <view class="credit-benefit" v-if="creditScore >= 650">
          <text class="benefit-icon">🎁</text>
          <text class="benefit-text">恭喜！您可享受信用免押金</text>
        </view>
      </view>

      <view class="payment-methods" v-if="!hasPaid && creditScore < 650">
        <text class="section-title">支付方式</text>
        <view class="method-list">
          <view 
            class="method-item" 
            :class="{ active: selectedMethod === 'wechat' }"
            @click="selectedMethod = 'wechat'"
          >
            <text class="method-icon wechat">💬</text>
            <text class="method-name">微信支付</text>
            <text class="method-check" v-if="selectedMethod === 'wechat'">✓</text>
          </view>
          <view 
            class="method-item" 
            :class="{ active: selectedMethod === 'alipay' }"
            @click="selectedMethod = 'alipay'"
          >
            <text class="method-icon alipay">💰</text>
            <text class="method-name">支付宝</text>
            <text class="method-check" v-if="selectedMethod === 'alipay'">✓</text>
          </view>
        </view>
      </view>

      <view class="deposit-actions">
        <button 
          class="action-btn primary" 
          v-if="!hasPaid && creditScore < 650"
          :disabled="!selectedMethod"
          @click="payDeposit"
        >
          缴纳押金 ¥199
        </button>
        <button 
          class="action-btn primary" 
          v-else-if="!hasPaid && creditScore >= 650"
          @click="useFreeDeposit"
        >
          信用免押
        </button>
        <button 
          class="action-btn secondary" 
          v-if="hasPaid"
          @click="refundDeposit"
        >
          申请退还押金
        </button>
      </view>

      <view class="deposit-tips">
        <text class="tip-item">• 押金可随时申请退还</text>
        <text class="tip-item">• 退还押金后无法继续使用车辆</text>
        <text class="tip-item">• 如有未完成订单，需先完成后退还</text>
      </view>
    </view>
  </view>
</template>

<script setup>
import { ref } from 'vue'

const hasPaid = ref(false)
const creditScore = ref(680)
const selectedMethod = ref('')

const payDeposit = () => {
  uni.showLoading({ title: '支付中...' })
  setTimeout(() => {
    uni.hideLoading()
    hasPaid.value = true
    uni.showToast({ title: '支付成功', icon: 'success' })
  }, 2000)
}

const useFreeDeposit = () => {
  uni.showLoading({ title: '授权中...' })
  setTimeout(() => {
    uni.hideLoading()
    hasPaid.value = true
    uni.showToast({ title: '免押授权成功', icon: 'success' })
  }, 1500)
}

const refundDeposit = () => {
  uni.showModal({
    title: '退还押金',
    content: '确定要申请退还押金吗？退还后将无法使用车辆。',
    success: (res) => {
      if (res.confirm) {
        uni.showLoading({ title: '申请中...' })
        setTimeout(() => {
          uni.hideLoading()
          hasPaid.value = false
          uni.showToast({ title: '申请成功，押金将在1-3个工作日内退回', icon: 'none' })
        }, 1500)
      }
    }
  })
}
</script>

<style lang="scss" scoped>
.deposit-container {
  min-height: 100vh;
  background: #f5f5f5;
  padding: 16px;
}

.deposit-card {
  background: white;
  border-radius: 16px;
  padding: 24px;
}

.deposit-header {
  margin-bottom: 24px;
}

.title {
  font-size: 20px;
  font-weight: bold;
  color: #333;
}

.status-section {
  text-align: center;
  padding: 32px 0;
  border-bottom: 1px solid #f0f0f0;
  margin-bottom: 24px;
}

.status-icon {
  width: 64px;
  height: 64px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 28px;
  margin: 0 auto 16px;

  &.paid {
    background: #e8f5e9;
    color: #4caf50;
  }

  &.unpaid {
    background: #fff3e0;
    color: #ff9800;
  }
}

.status-text {
  display: block;
  font-size: 16px;
  color: #666;
  margin-bottom: 8px;
}

.deposit-amount {
  font-size: 36px;
  font-weight: bold;
  color: #00A862;
}

.credit-section {
  margin-bottom: 24px;
}

.credit-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 12px;
}

.credit-title {
  font-size: 16px;
  font-weight: bold;
  color: #333;
}

.credit-score {
  font-size: 14px;
  color: #666;
}

.credit-bar {
  height: 8px;
  background: #f0f0f0;
  border-radius: 4px;
  position: relative;
  margin-bottom: 20px;
  overflow: visible;
}

.credit-progress {
  height: 100%;
  background: linear-gradient(90deg, #00A862 0%, #00c874 100%);
  border-radius: 4px;
}

.credit-marker {
  position: absolute;
  top: 12px;
  transform: translateX(-50%);
}

.marker-text {
  font-size: 10px;
  color: #ff9800;
  white-space: nowrap;
}

.credit-benefit {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 12px;
  background: #e8f5e9;
  border-radius: 8px;
}

.benefit-icon {
  font-size: 20px;
}

.benefit-text {
  font-size: 14px;
  color: #2e7d32;
}

.payment-methods {
  margin-bottom: 24px;
}

.section-title {
  display: block;
  font-size: 16px;
  font-weight: bold;
  color: #333;
  margin-bottom: 16px;
}

.method-list {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.method-item {
  display: flex;
  align-items: center;
  padding: 16px;
  border: 1px solid #e0e0e0;
  border-radius: 12px;

  &.active {
    border-color: #00A862;
    background: #f0f9f4;
  }
}

.method-icon {
  width: 36px;
  height: 36px;
  border-radius: 8px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 20px;
  margin-right: 12px;

  &.wechat {
    background: #07c160;
  }

  &.alipay {
    background: #1677ff;
  }
}

.method-name {
  flex: 1;
  font-size: 15px;
  color: #333;
}

.method-check {
  font-size: 18px;
  color: #00A862;
}

.deposit-actions {
  margin-bottom: 24px;
}

.action-btn {
  width: 100%;
  height: 52px;
  border: none;
  border-radius: 26px;
  font-size: 16px;
  font-weight: bold;

  &.primary {
    background: linear-gradient(90deg, #00A862 0%, #00c874 100%);
    color: white;

    &:disabled {
      opacity: 0.6;
    }
  }

  &.secondary {
    background: #f5f5f5;
    color: #666;
  }
}

.deposit-tips {
  background: #f5f5f5;
  border-radius: 8px;
  padding: 16px;
}

.tip-item {
  display: block;
  font-size: 12px;
  color: #666;
  line-height: 2;
}
</style>
