<template>
    <view class="container">
        <view class="order-list" v-if="orders.length > 0">
            <view class="order-item" v-for="order in orders" :key="order.id" @click="goDetail(order.id)">
                <view class="order-header">
                    <text class="order-title">{{ order.title }}</text>
                    <text class="order-status" :class="getStatusClass(order.status)">{{ getStatusText(order.status) }}</text>
                </view>
                <view class="order-info">
                    <text class="info-text">工单编号：{{ order.orderNo }}</text>
                    <text class="info-text">创建时间：{{ order.createTime }}</text>
                </view>
            </view>
        </view>
        
        <view class="empty-state" v-else>
            <text class="empty-icon">📋</text>
            <text class="empty-text">暂无工单记录</text>
        </view>
    </view>
</template>

<script setup>
import { ref, onMounted } from 'vue'

const orders = ref([])

onMounted(() => {
    loadOrders()
})

const loadOrders = () => {
    orders.value = [
        { id: 1, orderNo: 'WO20240115001', title: '宽带新装办理', status: 2, createTime: '2024-01-15 10:30' },
        { id: 2, orderNo: 'WO20240114002', title: '宽带移机办理', status: 3, createTime: '2024-01-14 15:20' },
        { id: 3, orderNo: 'WO20240113003', title: '套餐变更申请', status: 1, createTime: '2024-01-13 09:15' }
    ]
}

const getStatusText = (status) => {
    const statusMap = {
        0: '待审核',
        1: '待处理',
        2: '处理中',
        3: '已完成',
        4: '已取消',
        5: '已驳回'
    }
    return statusMap[status] || '未知'
}

const getStatusClass = (status) => {
    if (status === 3) return 'status-success'
    if (status === 2) return 'status-warning'
    return 'status-info'
}

const goDetail = (id) => {
    uni.navigateTo({
        url: '/pages/order/detail?id=' + id
    })
}
</script>

<style scoped>
.container {
    padding: 20rpx;
}

.order-list {
    display: flex;
    flex-direction: column;
    gap: 20rpx;
}

.order-item {
    background: #FFFFFF;
    border-radius: 16rpx;
    padding: 24rpx;
    box-shadow: 0 2rpx 12rpx rgba(0, 0, 0, 0.05);
}

.order-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 16rpx;
}

.order-title {
    font-size: 30rpx;
    font-weight: 500;
    color: #333333;
}

.order-status {
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

.status-info {
    background: #E8F3FF;
    color: #007AFF;
}

.order-info {
    display: flex;
    flex-direction: column;
    gap: 8rpx;
}

.info-text {
    font-size: 24rpx;
    color: #999999;
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
