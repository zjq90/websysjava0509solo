<template>
  <view class="recharge-container">
    <view class="balance-card">
      <text class="balance-label">当前余额</text>
      <text class="balance-value">¥{{ balance }}</text>
    </view>

    <view class="amount-card">
      <text class="card-title">选择充值金额</text>
      <view class="amount-grid">
        <view 
          class="amount-item" 
          v-for="amount in amounts" 
          :key="amount.value"
          :class="{ active: selectedAmount === amount.value, recommended: amount.recommended }"
          @click="selectedAmount = amount.value"
        >
          <text class="recommended-tag" v-if="amount.recommended">推荐</text>
          <text class="amount-value">¥{{ amount.value }}</text>
          <text class="amount-gift" v-if="amount.gift">送¥{{ amount.gift }}</text>
        </view>
      </view>
    </view>

    <view class="custom-card">
      <text class="card-title">自定义金额</text>
      <view class="custom-input">
        <text class="currency">¥</text>
        <input 
          class="input" 
          type="digit" 
          v-model="customAmount" 
          placeholder="请输入金额"
        />
      </view>
    </view>

    <view class="payment-card">
      <text class="card-title">支付方式</text>
      <view class="payment-list">
        <view 
          class="payment-item" 
          :class="{ active: selectedPayment === 'wechat' }"
          @click="selectedPayment = 'wechat'"
        >
          <text class="payment-icon wechat">💬</text>
          <text class="payment-name">微信支付</text>
          <text class="payment-check" v-if="selectedPayment === 'wechat'">✓</text>
        </view>
        <view 
          class="payment-item" 
          :class="{ active: selectedPayment === 'alipay' }"
          @click="selectedPayment = 'alipay'"
        >
          <text class="payment-icon alipay">💰</text>
          <text class="payment-name">支付宝</text>
          <text class="payment-check" v-if="selectedPayment === 'alipay'">✓</text>
        </view>
      </view>
    </view>

    <view class="agreement">
      <checkbox :checked="agreed" @click="agreed = !agreed" color="#00A862" />
      <text class="agreement-text">
        我已阅读并同意
        <text class="link">《充值服务协议》</text>
      </text>
    </view>

    <view class="bottom-bar">
      <view class="pay-info">
        <text class="pay-label">支付金额</text>
        <text class="pay-amount">¥{{ totalAmount }}</text>
      </view>
      <button 
        class="pay-btn" 
        :disabled="!canPay"
        @click="handleRecharge"
      >
        立即充值
      </button>
    </view>
  </view>
</template>

<script setup>
import { ref, computed } from 'vue'

const balance = ref('99.50')
const selectedAmount = ref(50)
const customAmount = ref('')
const selectedPayment = ref('wechat')
const agreed = ref(false)

const amounts = ref([
  { value: 10, gift: 0 },
  { value: 30, gift: 2 },
  { value: 50, gift: 5, recommended: true },
  { value: 100, gift: 12 },
  { value: 200, gift: 30 }
])

const totalAmount = computed(() => {
  return customAmount.value ? parseFloat(customAmount.value) || 0 : selectedAmount.value
})

const canPay = computed(() => {
  return totalAmount.value > 0 && agreed.value
})

const handleRecharge = () => {
  if (!canPay.value) {
    if (!agreed.value) {
      uni.showToast({ title: '请同意服务协议', icon: 'none' })
    } else {
      uni.showToast({ title: '请选择充值金额', icon: 'none' })
    }
    return
  }

  uni.showLoading({ title: '充值中...' })
  setTimeout(() => {
    uni.hideLoading()
    uni.showModal({
      title: '充值成功',
      content: `¥${totalAmount.value}已成功充值到您的账户`,
      showCancel: false,
      success: () => {
        uni.navigateBack()
      }
    })
  }, 2000)
}
</script>

<style lang="scss" scoped>
.recharge-container {
  min-height: 100vh;
  background: #f5f5f5;
  padding-bottom: 100px;
}

.balance-card {
  background: linear-gradient(135deg, #00A862 0%, #00c874 100%);
  padding: 40px 20px;
  text-align: center;
}

.balance-label {
  display: block;
  font-size: 14px;
  color: rgba(255,255,255,0.8);
  margin-bottom: 8px;
}

.balance-value {
  font-size: 40px;
  font-weight: bold;
  color: white;
}

.amount-card, .custom-card, .payment-card {
  background: white;
  margin: 12px;
  border-radius: 12px;
  padding: 16px;
}

.card-title {
  display: block;
  font-size: 16px;
  font-weight: bold;
  color: #333;
  margin-bottom: 16px;
}

.amount-grid {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 12px;
}

.amount-item {
  position: relative;
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 16px 8px;
  border: 1px solid #e0e0e0;
  border-radius: 12px;
  background: #fafafa;

  &.active {
    border-color: #00A862;
    background: #f0f9f4;
  }

  &.recommended {
    border-color: #ff9800;
  }
}

.recommended-tag {
  position: absolute;
  top: -8px;
  right: -4px;
  background: #ff9800;
  color: white;
  font-size: 10px;
  padding: 2px 8px;
  border-radius: 8px;
}

.amount-value {
  font-size: 20px;
  font-weight: bold;
  color: #333;
}

.amount-gift {
  font-size: 12px;
  color: #ff9800;
  margin-top: 4px;
}

.custom-input {
  display: flex;
  align-items: center;
  border: 1px solid #e0e0e0;
  border-radius: 12px;
  padding: 0 16px;
}

.currency {
  font-size: 20px;
  font-weight: bold;
  color: #333;
}

.input {
  flex: 1;
  height: 48px;
  border: none;
  font-size: 20px;
  font-weight: bold;
  padding: 0 8px;
}

.payment-list {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.payment-item {
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

.payment-icon {
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

.payment-name {
  flex: 1;
  font-size: 15px;
  color: #333;
}

.payment-check {
  font-size: 18px;
  color: #00A862;
}

.agreement {
  display: flex;
  align-items: flex-start;
  gap: 8px;
  padding: 0 16px;
  margin-top: 16px;
}

.agreement-text {
  flex: 1;
  font-size: 12px;
  color: #666;
  line-height: 1.5;
}

.link {
  color: #00A862;
}

.bottom-bar {
  position: fixed;
  bottom: 0;
  left: 0;
  right: 0;
  background: white;
  padding: 12px 16px;
  display: flex;
  align-items: center;
  box-shadow: 0 -2px 10px rgba(0,0,0,0.1);
}

.pay-info {
  flex: 1;
}

.pay-label {
  font-size: 12px;
  color: #666;
}

.pay-amount {
  font-size: 24px;
  font-weight: bold;
  color: #00A862;
}

.pay-btn {
  width: 140px;
  height: 48px;
  background: linear-gradient(90deg, #00A862 0%, #00c874 100%);
  color: white;
  border: none;
  border-radius: 24px;
  font-size: 16px;
  font-weight: bold;

  &:disabled {
    opacity: 0.6;
  }
}
</style>
