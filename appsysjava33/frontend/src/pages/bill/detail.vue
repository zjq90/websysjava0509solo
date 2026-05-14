<template>
    <view class="container">
        <view class="bill-header">
            <text class="bill-period">{{ billInfo.billPeriod }}</text>
            <text class="bill-status" :class="getStatusClass(billInfo.status)">{{ getStatusText(billInfo.status) }}</text>
        </view>

        <view class="amount-card">
            <text class="amount-label">应付金额</text>
            <text class="amount-value">¥{{ billInfo.totalAmount }}</text>
            <text class="amount-desc">含套餐费 ¥{{ billInfo.packageFee }}，增值服务费 ¥{{ billInfo.extraFee || 0 }}</text>
        </view>

        <view class="info-card">
            <view class="info-item">
                <text class="info-label">账单编号</text>
                <text class="info-value">{{ billInfo.billNo }}</text>
            </view>
            <view class="info-item">
                <text class="info-label">宽带号码</text>
                <text class="info-value">{{ billInfo.broadbandNumber }}</text>
            </view>
            <view class="info-item">
                <text class="info-label">账单周期</text>
                <text class="info-value">{{ billInfo.startDate }} 至 {{ billInfo.endDate }}</text>
            </view>
            <view class="info-item">
                <text class="info-label">缴费截止日期</text>
                <text class="info-value">{{ billInfo.dueDate }}</text>
            </view>
        </view>

        <view class="detail-card">
            <text class="card-title">费用明细</text>
            <view class="detail-item" v-for="(item, index) in feeDetails" :key="index">
                <text class="detail-name">{{ item.name }}</text>
                <text class="detail-value">¥{{ item.amount }}</text>
            </view>
        </view>

        <button class="pay-btn" v-if="billInfo.status === 0" @click="goPay">立即缴费</button>
    </view>
</template>

<script setup>
import { ref, onMounted } from 'vue'

const billInfo = ref({})
const feeDetails = ref([])

onMounted(() => {
    loadBillDetail()
})

const loadBillDetail = () => {
    billInfo.value = {
        id: 1,
        billNo: 'B20240115001',
        billPeriod: '2024年01月',
        broadbandNumber: '01012345678',
        totalAmount: 99,
        packageFee: 99,
        extraFee: 0,
        status: 0,
        startDate: '2024-01-01',
        endDate: '2024-01-31',
        dueDate: '2024-02-01'
    }
    
    feeDetails.value = [
        { name: '100M光纤宽带月费', amount: 99 },
        { name: '优惠减免', amount: 0 }
    ]
}

const getStatusText = (status) => {
    const statusMap = { 0: '待支付', 1: '已支付', 2: '已逾期', 3: '已取消' }
    return statusMap[status] || '未知'
}

const getStatusClass = (status) => {
    if (status === 1) return 'status-success'
    if (status === 2) return 'status-danger'
    return 'status-warning'
}

const goPay = () => {
    uni.navigateTo({
        url: '/pages/bill/pay?id=' + billInfo.value.id
    })
}
</script>

<style scoped>
.container {
    padding: 20rpx;
    padding-bottom: 100rpx;
}

.bill-header {
    background: linear-gradient(135deg, #007AFF 0%, #0056CC 100%);
    border-radius: 16rpx;
    padding: 32rpx;
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 20rpx;
}

.bill-period {
    font-size: 32rpx;
    font-weight: 500;
    color: #FFFFFF;
}

.bill-status {
    font-size: 24rpx;
    padding: 6rpx 16rpx;
    border-radius: 8rpx;
    background: rgba(255, 255, 255, 0.2);
    color: #FFFFFF;
}

.status-success {
    background: #E8F8ED !important;
    color: #34C759 !important;
}

.status-warning {
    background: #FFF5E6 !important;
    color: #FF9500 !important;
}

.status-danger {
    background: #FFE5E5 !important;
    color: #FF3B30 !important;
}

.amount-card {
    background: #FFFFFF;
    border-radius: 16rpx;
    padding: 32rpx;
    text-align: center;
    margin-bottom: 20rpx;
    box-shadow: 0 2rpx 12rpx rgba(0, 0, 0, 0.05);
}

.amount-label {
    display: block;
    font-size: 26rpx;
    color: #666666;
    margin-bottom: 12rpx;
}

.amount-value {
    display: block;
    font-size: 56rpx;
    font-weight: bold;
    color: #FF3B30;
    margin-bottom: 12rpx;
}

.amount-desc {
    font-size: 22rpx;
    color: #999999;
}

.info-card, .detail-card {
    background: #FFFFFF;
    border-radius: 16rpx;
    padding: 24rpx;
    margin-bottom: 20rpx;
    box-shadow: 0 2rpx 12rpx rgba(0, 0, 0, 0.05);
}

.info-item {
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 16rpx 0;
    border-bottom: 1rpx solid #F5F5F5;
}

.info-item:last-child {
    border-bottom: none;
}

.info-label {
    font-size: 26rpx;
    color: #999999;
}

.info-value {
    font-size: 26rpx;
    color: #333333;
    text-align: right;
    flex: 1;
    margin-left: 20rpx;
}

.card-title {
    display: block;
    font-size: 30rpx;
    font-weight: 500;
    color: #333333;
    margin-bottom: 20rpx;
}

.detail-item {
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 12rpx 0;
}

.detail-name {
    font-size: 26rpx;
    color: #666666;
}

.detail-value {
    font-size: 26rpx;
    color: #333333;
    font-weight: 500;
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
</style>
