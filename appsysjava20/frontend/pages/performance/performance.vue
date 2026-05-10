<template>
    <view class="performance-page">
        <view class="header-card">
            <view class="period-selector">
                <view class="selector-content">
                    <text class="period-label">统计周期</text>
                    <view class="period-inputs">
                        <picker mode="selector" :range="years" @change="onYearChange">
                            <view class="picker-item">
                                <text class="picker-value">{{ selectedYear }}年</text>
                                <text class="picker-arrow">▼</text>
                            </view>
                        </picker>
                        <picker mode="selector" :range="months" @change="onMonthChange">
                            <view class="picker-item">
                                <text class="picker-value">{{ selectedMonth }}月</text>
                                <text class="picker-arrow">▼</text>
                            </view>
                        </picker>
                    </view>
                </view>
            </view>
        </view>
        
        <view class="stats-cards">
            <view class="stat-card blue">
                <view class="stat-icon">💰</view>
                <view class="stat-content">
                    <text class="stat-label">销售业绩</text>
                    <text class="stat-number">¥{{ formatNumber(performanceData.totalSalesAmount) }}</text>
                </view>
            </view>
            
            <view class="stat-card green">
                <view class="stat-icon">💳</view>
                <view class="stat-content">
                    <text class="stat-label">已回款</text>
                    <text class="stat-number">¥{{ formatNumber(performanceData.totalReceivedAmount) }}</text>
                </view>
            </view>
            
            <view class="stat-card purple">
                <view class="stat-icon">💎</view>
                <view class="stat-content">
                    <text class="stat-label">提成收入</text>
                    <text class="stat-number">¥{{ formatNumber(performanceData.commissionAmount) }}</text>
                </view>
            </view>
            
            <view class="stat-card orange">
                <view class="stat-icon">⏳</view>
                <view class="stat-content">
                    <text class="stat-label">待回款</text>
                    <text class="stat-number">¥{{ formatNumber(performanceData.pendingAmount) }}</text>
                </view>
            </view>
        </view>
        
        <view class="progress-card card">
            <view class="card-title">📊 回款进度</view>
            <view class="progress-info flex-between mb-20">
                <text class="progress-label">回款率</text>
                <text class="progress-percent">{{ paymentRate }}%</text>
            </view>
            <view class="progress-bar">
                <view class="progress-fill" :style="{ width: paymentRate + '%' }"></view>
            </view>
            <view class="progress-details flex-between mt-20">
                <text class="text-muted">已回款: ¥{{ formatNumber(performanceData.totalReceivedAmount) }}</text>
                <text class="text-muted">总金额: ¥{{ formatNumber(performanceData.totalSalesAmount) }}</text>
            </view>
        </view>
        
        <view class="orders-card card">
            <view class="card-title flex-between">
                <text>🧾 订单明细</text>
                <text class="order-count">{{ orderList.length }} 笔订单</text>
            </view>
            
            <view class="orders-list" v-if="orderList.length > 0">
                <view class="order-item" v-for="(order, index) in orderList" :key="index">
                    <view class="order-header flex-between">
                        <text class="order-no">{{ order.orderNo }}</text>
                        <text class="status-tag" :class="'status-' + order.status.toLowerCase()">
                            {{ getStatusText(order.status) }}
                        </text>
                    </view>
                    <view class="order-body">
                        <view class="order-amount">
                            <text class="amount-label">销售金额</text>
                            <text class="amount-value text-primary">¥{{ formatNumber(order.totalAmount) }}</text>
                        </view>
                        <view class="order-paid">
                            <text class="amount-label">已回款</text>
                            <text class="amount-value text-success">¥{{ formatNumber(order.paidAmount) }}</text>
                        </view>
                        <view class="order-commission" v-if="order.commissionAmount > 0">
                            <text class="amount-label">提成</text>
                            <text class="amount-value text-warning">¥{{ formatNumber(order.commissionAmount) }}</text>
                        </view>
                    </view>
                </view>
            </view>
            
            <view class="empty-state" v-else>
                <text class="empty-icon">📋</text>
                <text class="empty-text">本月暂无订单</text>
            </view>
        </view>
    </view>
</template>

<script>
import { financeApi } from '../../api/index';

export default {
    data() {
        return {
            currentUser: { id: 2 },
            years: [],
            months: [1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12],
            selectedYear: new Date().getFullYear(),
            selectedMonth: new Date().getMonth() + 1,
            performanceData: {
                totalSalesAmount: '0.00',
                totalReceivedAmount: '0.00',
                commissionAmount: '0.00',
                pendingAmount: '0.00',
                orders: []
            },
            orderList: []
        };
    },
    computed: {
        paymentRate() {
            const sales = Number(this.performanceData.totalSalesAmount) || 0;
            const received = Number(this.performanceData.totalReceivedAmount) || 0;
            if (sales === 0) return 0;
            return Math.round((received / sales) * 100);
        }
    },
    onShow() {
        const user = uni.getStorageSync('currentUser');
        if (user) {
            this.currentUser = user;
        }
        this.initYears();
        this.loadData();
    },
    methods: {
        initYears() {
            const currentYear = new Date().getFullYear();
            this.years = [currentYear - 1, currentYear, currentYear + 1];
        },
        
        onYearChange(e) {
            this.selectedYear = this.years[e.detail.value];
            this.loadData();
        },
        
        onMonthChange(e) {
            this.selectedMonth = this.months[e.detail.value];
            this.loadData();
        },
        
        async loadData() {
            try {
                const res = await financeApi.getPerformance(
                    this.currentUser.id,
                    { year: this.selectedYear, month: this.selectedMonth }
                );
                if (res.success && res.data) {
                    this.performanceData = res.data;
                    this.orderList = res.data.orders || [];
                }
            } catch (e) {
                console.error('加载业绩数据失败', e);
            }
        },
        
        formatNumber(num) {
            if (!num) return '0.00';
            const n = Number(num);
            return n.toLocaleString('zh-CN', { minimumFractionDigits: 2, maximumFractionDigits: 2 });
        },
        
        getStatusText(status) {
            const map = {
                'COMPLETED': '已完成',
                'PARTIAL': '部分回款',
                'PENDING': '待回款'
            };
            return map[status] || status;
        }
    }
};
</script>

<style scoped>
.performance-page {
    padding: 30rpx;
}

.header-card {
    background: linear-gradient(135deg, #3c9cff, #5ba8ff);
    border-radius: 20rpx;
    padding: 30rpx;
    margin-bottom: 30rpx;
}

.period-selector {
    background: rgba(255, 255, 255, 0.15);
    border-radius: 16rpx;
    padding: 24rpx;
}

.period-label {
    font-size: 24rpx;
    color: rgba(255, 255, 255, 0.8);
    margin-bottom: 16rpx;
    display: block;
}

.period-inputs {
    display: flex;
    gap: 20rpx;
}

.picker-item {
    background: #fff;
    border-radius: 12rpx;
    padding: 20rpx 30rpx;
    display: flex;
    align-items: center;
    gap: 12rpx;
}

.picker-value {
    font-size: 30rpx;
    font-weight: bold;
    color: #3c9cff;
}

.picker-arrow {
    font-size: 20rpx;
    color: #999;
}

.stats-cards {
    display: grid;
    grid-template-columns: repeat(2, 1fr);
    gap: 20rpx;
    margin-bottom: 30rpx;
}

.stat-card {
    background: #fff;
    border-radius: 20rpx;
    padding: 30rpx;
    display: flex;
    align-items: center;
    gap: 20rpx;
    box-shadow: 0 4rpx 20rpx rgba(0, 0, 0, 0.05);
}

.stat-icon {
    width: 80rpx;
    height: 80rpx;
    border-radius: 20rpx;
    display: flex;
    align-items: center;
    justify-content: center;
    font-size: 40rpx;
}

.stat-card.blue .stat-icon { background: #ecf5ff; }
.stat-card.green .stat-icon { background: #f0f9eb; }
.stat-card.purple .stat-icon { background: #f9f0ff; }
.stat-card.orange .stat-icon { background: #fdf6ec; }

.stat-label {
    display: block;
    font-size: 24rpx;
    color: #999;
    margin-bottom: 8rpx;
}

.stat-number {
    display: block;
    font-size: 32rpx;
    font-weight: bold;
    color: #333;
}

.stat-card.blue .stat-number { color: #3c9cff; }
.stat-card.green .stat-number { color: #67c23a; }
.stat-card.purple .stat-number { color: #909399; }
.stat-card.orange .stat-number { color: #e6a23c; }

.progress-info {
    margin-bottom: 16rpx;
}

.progress-label {
    font-size: 28rpx;
    color: #666;
}

.progress-percent {
    font-size: 32rpx;
    font-weight: bold;
    color: #3c9cff;
}

.order-count {
    font-size: 26rpx;
    color: #999;
}

.order-item {
    padding: 24rpx 0;
    border-bottom: 1rpx solid #f0f0f0;
}

.order-item:last-child {
    border-bottom: none;
}

.order-header {
    margin-bottom: 16rpx;
}

.order-no {
    font-size: 28rpx;
    font-weight: bold;
    color: #333;
}

.order-body {
    display: grid;
    grid-template-columns: repeat(3, 1fr);
    gap: 16rpx;
}

.amount-label {
    display: block;
    font-size: 22rpx;
    color: #999;
    margin-bottom: 4rpx;
}

.amount-value {
    font-size: 26rpx;
    font-weight: bold;
}
</style>
