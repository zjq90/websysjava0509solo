<template>
    <view class="container">
        <view class="order-header">
            <text class="order-title">{{ orderInfo.title }}</text>
            <text class="order-status" :class="getStatusClass(orderInfo.status)">{{ getStatusText(orderInfo.status) }}</text>
        </view>
        
        <view class="info-card">
            <view class="info-item">
                <text class="info-label">工单编号</text>
                <text class="info-value">{{ orderInfo.orderNo }}</text>
            </view>
            <view class="info-item">
                <text class="info-label">宽带号码</text>
                <text class="info-value">{{ orderInfo.broadbandNumber }}</text>
            </view>
            <view class="info-item">
                <text class="info-label">安装地址</text>
                <text class="info-value">{{ orderInfo.address }}</text>
            </view>
            <view class="info-item">
                <text class="info-label">预约时间</text>
                <text class="info-value">{{ orderInfo.appointmentTime }}</text>
            </view>
            <view class="info-item">
                <text class="info-label">创建时间</text>
                <text class="info-value">{{ orderInfo.createTime }}</text>
            </view>
        </view>

        <view class="process-card">
            <text class="card-title">处理进度</text>
            <view class="process-list">
                <view class="process-item" v-for="(item, index) in processList" :key="index">
                    <view class="process-dot" :class="{ active: item.done }"></view>
                    <view class="process-line" v-if="index < processList.length - 1" :class="{ active: item.done }"></view>
                    <view class="process-content">
                        <text class="process-title">{{ item.title }}</text>
                        <text class="process-time">{{ item.time }}</text>
                    </view>
                </view>
            </view>
        </view>

        <button class="btn-secondary" v-if="orderInfo.status < 3" @click="cancelOrder">取消工单</button>
    </view>
</template>

<script setup>
import { ref, onMounted } from 'vue'

const orderInfo = ref({})
const processList = ref([])

onMounted(() => {
    loadOrderDetail()
})

const loadOrderDetail = () => {
    orderInfo.value = {
        id: 1,
        orderNo: 'WO20240115001',
        title: '宽带新装办理',
        status: 2,
        broadbandNumber: '01012345678',
        address: '北京市朝阳区XXX小区X号楼X单元XXX室',
        appointmentTime: '2024-01-16 14:00-16:00',
        createTime: '2024-01-15 10:30'
    }
    
    processList.value = [
        { title: '提交申请', time: '2024-01-15 10:30', done: true },
        { title: '审核通过', time: '2024-01-15 11:00', done: true },
        { title: '分配工程师', time: '2024-01-15 14:00', done: true },
        { title: '上门安装', time: '', done: false },
        { title: '完成开通', time: '', done: false }
    ]
}

const getStatusText = (status) => {
    const statusMap = { 0: '待审核', 1: '待处理', 2: '处理中', 3: '已完成', 4: '已取消', 5: '已驳回' }
    return statusMap[status] || '未知'
}

const getStatusClass = (status) => {
    if (status === 3) return 'status-success'
    if (status === 2) return 'status-warning'
    return 'status-info'
}

const cancelOrder = () => {
    uni.showModal({
        title: '提示',
        content: '确定要取消该工单吗？',
        success: (res) => {
            if (res.confirm) {
                orderInfo.value.status = 4
                uni.showToast({
                    title: '工单已取消',
                    icon: 'success'
                })
            }
        }
    })
}
</script>

<style scoped>
.container {
    padding: 20rpx;
}

.order-header {
    background: linear-gradient(135deg, #007AFF 0%, #0056CC 100%);
    border-radius: 16rpx;
    padding: 32rpx;
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 20rpx;
}

.order-title {
    font-size: 32rpx;
    font-weight: 500;
    color: #FFFFFF;
}

.order-status {
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

.status-info {
    background: #E8F3FF !important;
    color: #007AFF !important;
}

.info-card {
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

.process-card {
    background: #FFFFFF;
    border-radius: 16rpx;
    padding: 24rpx;
    margin-bottom: 30rpx;
    box-shadow: 0 2rpx 12rpx rgba(0, 0, 0, 0.05);
}

.card-title {
    display: block;
    font-size: 30rpx;
    font-weight: 500;
    color: #333333;
    margin-bottom: 24rpx;
}

.process-list {
    padding-left: 20rpx;
}

.process-item {
    display: flex;
    position: relative;
    padding-bottom: 32rpx;
}

.process-item:last-child {
    padding-bottom: 0;
}

.process-dot {
    width: 20rpx;
    height: 20rpx;
    border-radius: 50%;
    background: #E5E5E5;
    position: relative;
    z-index: 1;
}

.process-dot.active {
    background: #34C759;
}

.process-line {
    position: absolute;
    left: 9rpx;
    top: 20rpx;
    width: 2rpx;
    height: calc(100% - 20rpx);
    background: #E5E5E5;
}

.process-line.active {
    background: #34C759;
}

.process-content {
    margin-left: 20rpx;
    flex: 1;
}

.process-title {
    display: block;
    font-size: 26rpx;
    color: #666666;
    margin-bottom: 4rpx;
}

.process-time {
    font-size: 22rpx;
    color: #999999;
}

.btn-secondary {
    width: 100%;
    background: #F5F7FA;
    color: #666666;
    border-radius: 48rpx;
    padding: 24rpx;
    font-size: 30rpx;
    font-weight: 500;
}
</style>
