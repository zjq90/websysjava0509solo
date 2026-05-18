<template>
  <view class="pay-container">
    <view class="order-section">
      <view class="section-title">订单信息</view>
      <view class="product-card">
        <image class="product-img" :src="order.productImage" mode="aspectFill"></image>
        <view class="product-detail">
          <text class="product-name">{{ order.productName }}</text>
          <text class="product-price">¥{{ order.price }}</text>
        </view>
      </view>
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

    <view class="pay-method-section">
      <view class="section-title">选择支付方式</view>
      <view 
        class="pay-method-item" 
        v-for="method in payMethods" 
        :key="method.id"
        @click="selectedMethod = method.id"
      >
        <view class="method-icon" :class="method.id">
          <text>{{ method.icon }}</text>
        </view>
        <view class="method-info">
          <text class="method-name">{{ method.name }}</text>
          <text class="method-desc" v-if="method.desc">{{ method.desc }}</text>
        </view>
        <view class="radio" :class="{ checked: selectedMethod === method.id }"></view>
      </view>
    </view>

    <view class="agreement-section">
      <view class="checkbox" :class="{ checked: agreed }" @click="agreed = !agreed"></view>
      <text class="agreement-text">我已阅读并同意</text>
      <text class="agreement-link">《二手交易支付服务协议》</text>
    </view>

    <view class="pay-footer">
      <view class="pay-amount">
        <text class="amount-label">支付金额：</text>
        <text class="amount-value">¥{{ order.price }}</text>
      </view>
      <button class="pay-btn" :disabled="!canPay" @click="confirmPay">确认支付</button>
    </view>
  </view>
</template>

<script>
export default {
  data() {
    return {
      selectedMethod: 'wechat',
      agreed: true,
      order: {
        id: 1,
        productName: 'iPhone 13 128G 蓝色',
        productImage: 'https://picsum.photos/200/200?random=20',
        price: 3599
      },
      payMethods: [
        { id: 'wechat', name: '微信支付', icon: '💚', desc: '推荐使用' },
        { id: 'alipay', name: '支付宝', icon: '💙', desc: '' },
        { id: 'balance', name: '余额支付', icon: '💰', desc: '可用余额：¥5000.00' }
      ]
    }
  },
  computed: {
    canPay() {
      return this.agreed && this.selectedMethod
    }
  },
  methods: {
    confirmPay() {
      uni.showLoading({
        title: '支付中...'
      })

      setTimeout(() => {
        uni.hideLoading()
        uni.showModal({
          title: '支付成功',
          content: '您的订单已支付成功',
          showCancel: false,
          success: () => {
            uni.redirectTo({
              url: '/pages/order/list'
            })
          }
        })
      }, 2000)
    }
  }
}
</script>

<style scoped>
.pay-container {
  min-height: 100vh;
  background-color: #f5f5f5;
  padding-bottom: 200rpx;
}

.order-section,
.pay-method-section {
  background-color: #fff;
  margin: 20rpx;
  border-radius: 20rpx;
  padding: 30rpx;
}

.section-title {
  font-size: 30rpx;
  color: #333;
  font-weight: bold;
  margin-bottom: 25rpx;
}

.product-card {
  display: flex;
  gap: 20rpx;
  padding-bottom: 25rpx;
  border-bottom: 1rpx solid #f5f5f5;
  margin-bottom: 20rpx;
}

.product-img {
  width: 150rpx;
  height: 150rpx;
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
  font-size: 28rpx;
  color: #333;
  font-weight: bold;
  line-height: 1.4;
}

.product-price {
  font-size: 32rpx;
  color: #ff4d4f;
  font-weight: bold;
}

.total-row {
  display: flex;
  justify-content: space-between;
  padding: 12rpx 0;
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

.pay-method-item {
  display: flex;
  align-items: center;
  gap: 20rpx;
  padding: 25rpx 0;
  border-bottom: 1rpx solid #f5f5f5;
}

.pay-method-item:last-child {
  border-bottom: none;
}

.method-icon {
  width: 80rpx;
  height: 80rpx;
  border-radius: 15rpx;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 40rpx;
  flex-shrink: 0;
}

.method-icon.wechat {
  background-color: #07c160;
}

.method-icon.alipay {
  background-color: #1677ff;
}

.method-icon.balance {
  background-color: #ff7d00;
}

.method-info {
  flex: 1;
}

.method-name {
  display: block;
  font-size: 30rpx;
  color: #333;
  font-weight: bold;
  margin-bottom: 5rpx;
}

.method-desc {
  display: block;
  font-size: 24rpx;
  color: #999;
}

.radio {
  width: 40rpx;
  height: 40rpx;
  border: 2rpx solid #ddd;
  border-radius: 50%;
  flex-shrink: 0;
  position: relative;
}

.radio.checked {
  border-color: #409EFF;
  background-color: #409EFF;
}

.radio.checked::after {
  content: '';
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  width: 16rpx;
  height: 16rpx;
  background-color: #fff;
  border-radius: 50%;
}

.agreement-section {
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 30rpx;
  gap: 10rpx;
}

.checkbox {
  width: 36rpx;
  height: 36rpx;
  border: 2rpx solid #ddd;
  border-radius: 6rpx;
  flex-shrink: 0;
  position: relative;
}

.checkbox.checked {
  border-color: #409EFF;
  background-color: #409EFF;
}

.checkbox.checked::after {
  content: '✓';
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  color: #fff;
  font-size: 24rpx;
  font-weight: bold;
}

.agreement-text {
  font-size: 26rpx;
  color: #666;
}

.agreement-link {
  font-size: 26rpx;
  color: #409EFF;
}

.pay-footer {
  position: fixed;
  bottom: 0;
  left: 0;
  right: 0;
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 25rpx 30rpx;
  background-color: #fff;
  box-shadow: 0 -4rpx 12rpx rgba(0, 0, 0, 0.05);
}

.pay-amount {
  display: flex;
  align-items: baseline;
}

.amount-label {
  font-size: 28rpx;
  color: #666;
}

.amount-value {
  font-size: 40rpx;
  color: #ff4d4f;
  font-weight: bold;
}

.pay-btn {
  width: 280rpx;
  height: 80rpx;
  background: linear-gradient(135deg, #409EFF, #67C23A);
  color: #fff;
  border: none;
  border-radius: 40rpx;
  font-size: 30rpx;
  font-weight: bold;
}

.pay-btn[disabled] {
  background: #ccc;
}
</style>
