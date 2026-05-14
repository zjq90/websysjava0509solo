<template>
    <view class="container">
        <view class="amount-card">
            <text class="amount-label">支付金额</text>
            <text class="amount-value">¥{{ payAmount }}</text>
            <text class="amount-desc">{{ billInfo.billPeriod }} 宽带费</text>
        </view>

        <view class="method-card">
            <text class="card-title">选择支付方式</text>
            <view class="method-item" v-for="method in payMethods" :key="method.id" :class="{ active: selectedMethod === method.id }" @click="selectedMethod = method.id">
                <text class="method-icon">{{ method.icon }}</text>
                <text class="method-name">{{ method.name }}</text>
                <radio :checked="selectedMethod === method.id" color="#007AFF" />
            </view>
        </view>

        <button class="pay-btn" :disabled="!selectedMethod" @click="handlePay">确认支付</button>
    </view>
</template>

<script setup>
import { ref, onMounted } from 'vue'

const payAmount = ref(99)
const selectedMethod = ref(null)
const billInfo = ref({})

const payMethods = ref([
    { id: 1, name: '微信支付', icon: '💬' },
    { id: 2, name: '支付宝', icon: '💰' },
    { id: 3, name: '银联支付', icon: '💳' },
    { id: 4, name: '对公转账', icon: '🏦' }
])

onMounted(() => {
    loadBillInfo()
})

const loadBillInfo = () => {
    billInfo.value = {
        id: 1,
        billPeriod: '2024年01月',
        totalAmount: 99
    }
    payAmount.value = billInfo.value.totalAmount
}

const handlePay = () => {
    uni.showModal({
        title: '确认支付',
        content: '确认支付 ¥' + payAmount.value + ' 吗？',
        success: (res) => {
            if (res.confirm) {
                uni.showLoading({ title: '支付中...' })
                setTimeout(() => {
                    uni.hideLoading()
                    uni.showToast({
                        title: '支付成功',
                        icon: 'success'
                    })
                    setTimeout(() => {
                        uni.navigateBack()
                    }, 1500)
                }, 1500)
            }
        }
    })
}
</script>

<style scoped>
.container {
    padding: 20rpx;
    padding-bottom: 100rpx;
}

.amount-card {
    background: linear-gradient(135deg, #007AFF 0%, #0056CC 100%);
    border-radius: 16rpx;
    padding: 48rpx 32rpx;
    text-align: center;
    margin-bottom: 30rpx;
}

.amount-label {
    display: block;
    font-size: 26rpx;
    color: rgba(255, 255, 255, 0.8);
    margin-bottom: 16rpx;
}

.amount-value {
    display: block;
    font-size: 64rpx;
    font-weight: bold;
    color: #FFFFFF;
    margin-bottom: 12rpx;
}

.amount-desc {
    font-size: 24rpx;
    color: rgba(255, 255, 255, 0.6);
}

.method-card {
    background: #FFFFFF;
    border-radius: 16rpx;
    padding: 24rpx;
    margin-bottom: 40rpx;
    box-shadow: 0 2rpx 12rpx rgba(0, 0, 0, 0.05);
}

.card-title {
    display: block;
    font-size: 30rpx;
    font-weight: 500;
    color: #333333;
    margin-bottom: 20rpx;
}

.method-item {
    display: flex;
    align-items: center;
    padding: 24rpx 0;
    border-bottom: 1rpx solid #F5F5F5;
}

.method-item:last-child {
    border-bottom: none;
}

.method-item.active {
    background: #E8F3FF;
    margin: 0 -24rpx;
    padding: 24rpx;
    border-radius: 12rpx;
}

.method-icon {
    font-size: 40rpx;
    margin-right: 20rpx;
}

.method-name {
    flex: 1;
    font-size: 28rpx;
    color: #333333;
}

.pay-btn {
    width: 100%;
    background: linear-gradient(135deg, #007AFF 0%, #0056CC 100%);
    color: #FFFFFF;
    border-radius: 48rpx;
    padding: 28rpx;
    font-size: 32rpx;
    font-weight: 500;
}

.pay-btn[disabled] {
    background: #CCCCCC;
}
</style>
