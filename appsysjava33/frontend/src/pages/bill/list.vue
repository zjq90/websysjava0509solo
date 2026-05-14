<template>
    <view class="container">
        <view class="bill-list" v-if="bills.length > 0">
            <view class="bill-item" v-for="bill in bills" :key="bill.id" @click="goDetail(bill.id)">
                <view class="bill-header">
                    <text class="bill-period">{{ bill.billPeriod }}</text>
                    <text class="bill-status" :class="getStatusClass(bill.status)">{{ getStatusText(bill.status) }}</text>
                </view>
                <view class="bill-info">
                    <text class="info-text">账单编号：{{ bill.billNo }}</text>
                    <text class="info-text">宽带号码：{{ bill.broadbandNumber }}</text>
                </view>
                <view class="bill-amount">
                    <text class="amount-label">应付金额：</text>
                    <text class="amount-value">¥{{ bill.totalAmount }}</text>
                </view>
                <view class="bill-action" v-if="bill.status === 0">
                    <button class="pay-btn" @click.stop="goPay(bill.id)">立即缴费</button>
                </view>
            </view>
        </view>
        
        <view class="empty-state" v-else>
            <text class="empty-icon">📄</text>
            <text class="empty-text">暂无账单记录</text>
        </view>
    </view>
</template>

<script setup>
import { ref, onMounted } from 'vue'

const bills = ref([])

onMounted(() => {
    loadBills()
})

const loadBills = () => {
    bills.value = [
        { id: 1, billNo: 'B20240115001', billPeriod: '2024年01月', broadbandNumber: '01012345678', totalAmount: 99, status: 0, dueDate: '2024-01-20' },
        { id: 2, billNo: 'B20231215002', billPeriod: '2023年12月', broadbandNumber: '01012345678', totalAmount: 99, status: 1, dueDate: '2023-12-20' },
        { id: 3, billNo: 'B20231115003', billPeriod: '2023年11月', broadbandNumber: '01012345678', totalAmount: 159, status: 1, dueDate: '2023-11-20' }
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

const goDetail = (id) => {
    uni.navigateTo({
        url: '/pages/bill/detail?id=' + id
    })
}

const goPay = (id) => {
    uni.navigateTo({
        url: '/pages/bill/pay?id=' + id
    })
}
</script>

<style scoped>
.container {
    padding: 20rpx;
}

.bill-list {
    display: flex;
    flex-direction: column;
    gap: 20rpx;
}

.bill-item {
    background: #FFFFFF;
    border-radius: 16rpx;
    padding: 24rpx;
    box-shadow: 0 2rpx 12rpx rgba(0, 0, 0, 0.05);
}

.bill-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 16rpx;
}

.bill-period {
    font-size: 30rpx;
    font-weight: 500;
    color: #333333;
}

.bill-status {
    font-size: 24rpx;
    padding: 4rpx 12rpx;
    border-radius: 8rpx;
}

.status-success {
    background: #E8F8ED;
    color: #34C759;
}

.status-warning {
    background: #FFF5E6;
    color: #FF9500;
}

.status-danger {
    background: #FFE5E5;
    color: #FF3B30;
}

.bill-info {
    display: flex;
    flex-direction: column;
    gap: 8rpx;
    margin-bottom: 16rpx;
}

.info-text {
    font-size: 24rpx;
    color: #999999;
}

.bill-amount {
    display: flex;
    align-items: center;
    margin-bottom: 16rpx;
}

.amount-label {
    font-size: 26rpx;
    color: #666666;
}

.amount-value {
    font-size: 32rpx;
    font-weight: bold;
    color: #FF3B30;
}

.bill-action {
    text-align: right;
}

.pay-btn {
    background: linear-gradient(135deg, #007AFF 0%, #0056CC 100%);
    color: #FFFFFF;
    border-radius: 32rpx;
    padding: 12rpx 32rpx;
    font-size: 24rpx;
    margin: 0;
    display: inline-block;
}

.empty-state {
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
    padding: 120rpx 0;
}

.empty-icon {
    font-size: 100rpx;
    margin-bottom: 20rpx;
}

.empty-text {
    font-size: 28rpx;
    color: #999999;
}
</style>
