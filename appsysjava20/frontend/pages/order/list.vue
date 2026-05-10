<template>
    <view class="order-list-page">
        <view class="header-banner">
            <view class="header-content">
                <text class="header-title">🧾 订单管理</text>
                <text class="header-desc">共 {{ orderList.length }} 笔订单</text>
            </view>
            <view class="status-tabs">
                <view 
                    class="tab-item" 
                    :class="{ active: currentStatus === 'ALL' }"
                    @click="currentStatus = 'ALL'"
                >全部</view>
                <view 
                    class="tab-item" 
                    :class="{ active: currentStatus === 'COMPLETED' }"
                    @click="currentStatus = 'COMPLETED'"
                >已完成</view>
                <view 
                    class="tab-item" 
                    :class="{ active: currentStatus === 'PARTIAL' }"
                    @click="currentStatus = 'PARTIAL'"
                >部分回款</view>
                <view 
                    class="tab-item" 
                    :class="{ active: currentStatus === 'PENDING' }"
                    @click="currentStatus = 'PENDING'"
                >待回款</view>
            </view>
        </view>
        
        <view class="stats-row card">
            <view class="stat-item">
                <text class="stat-value">¥{{ formatNumber(totalSales) }}</text>
                <text class="stat-label">总销售额</text>
            </view>
            <view class="stat-item">
                <text class="stat-value">¥{{ formatNumber(totalPaid) }}</text>
                <text class="stat-label">已回款</text>
            </view>
            <view class="stat-item">
                <text class="stat-value">{{ paymentRate }}%</text>
                <text class="stat-label">回款率</text>
            </view>
        </view>
        
        <view class="order-list">
            <view 
                class="order-item card" 
                v-for="(order, index) in filteredOrders" 
                :key="order.id"
            >
                <view class="order-header flex-between">
                    <view class="order-info-left">
                        <text class="order-no">{{ order.orderNo }}</text>
                        <text class="order-date">{{ formatDate(order.createTime) }}</text>
                    </view>
                    <text class="status-tag" :class="'status-' + order.status.toLowerCase()">
                        {{ getStatusText(order.status) }}
                    </text>
                </view>
                
                <view class="order-body">
                    <view class="detail-row">
                        <text class="detail-label">销售金额</text>
                        <text class="detail-value primary">¥{{ formatNumber(order.totalAmount) }}</text>
                    </view>
                    <view class="detail-row">
                        <text class="detail-label">已回款</text>
                        <text class="detail-value success">¥{{ formatNumber(order.paidAmount) }}</text>
                    </view>
                    <view class="detail-row" v-if="order.commissionAmount > 0">
                        <text class="detail-label">提成</text>
                        <text class="detail-value warning">¥{{ formatNumber(order.commissionAmount) }}</text>
                    </view>
                    <view class="progress-section" v-if="order.status !== 'COMPLETED'">
                        <view class="progress-bar">
                            <view class="progress-fill" :style="{ width: getProgress(order) + '%' }"></view>
                        </view>
                        <text class="progress-text">回款进度 {{ getProgress(order) }}%</text>
                    </view>
                </view>
                
                <view class="order-actions">
                    <button class="action-btn outline" @click="editOrder(order)">编辑</button>
                    <button class="action-btn primary" @click="updatePayment(order)">登记回款</button>
                </view>
            </view>
        </view>
        
        <view class="empty-state" v-if="filteredOrders.length === 0">
            <text class="empty-icon">📋</text>
            <text class="empty-text">暂无订单数据</text>
        </view>
        
        <view class="fab-button" @click="goToAdd">
            <text class="fab-icon">+</text>
        </view>
    </view>
</template>

<script>
import { financeApi } from '../../api/index';

export default {
    data() {
        return {
            currentStatus: 'ALL',
            orderList: [],
            editingOrder: null,
            paymentAmount: ''
        };
    },
    computed: {
        filteredOrders() {
            if (this.currentStatus === 'ALL') return this.orderList;
            return this.orderList.filter(item => item.status === this.currentStatus);
        },
        totalSales() {
            return this.filteredOrders.reduce((sum, item) => sum + Number(item.totalAmount || 0), 0);
        },
        totalPaid() {
            return this.filteredOrders.reduce((sum, item) => sum + Number(item.paidAmount || 0), 0);
        },
        paymentRate() {
            if (this.totalSales === 0) return 0;
            return Math.round((this.totalPaid / this.totalSales) * 100);
        }
    },
    onShow() {
        this.loadOrders();
    },
    methods: {
        async loadOrders() {
            try {
                const res = await financeApi.getOrders();
                if (res.success && res.data) {
                    this.orderList = res.data;
                }
            } catch (e) {
                console.error('加载订单失败', e);
            }
        },
        
        formatNumber(num) {
            if (!num) return '0.00';
            const n = Number(num);
            return n.toLocaleString('zh-CN', { minimumFractionDigits: 2, maximumFractionDigits: 2 });
        },
        
        formatDate(dateStr) {
            if (!dateStr) return '-';
            const date = new Date(dateStr);
            return (date.getMonth() + 1) + '/' + date.getDate() + ' ' + 
                   date.getHours() + ':' + String(date.getMinutes()).padStart(2, '0');
        },
        
        getStatusText(status) {
            const map = {
                'COMPLETED': '已完成',
                'PARTIAL': '部分回款',
                'PENDING': '待回款'
            };
            return map[status] || status;
        },
        
        getProgress(order) {
            const total = Number(order.totalAmount) || 0;
            const paid = Number(order.paidAmount) || 0;
            if (total === 0) return 0;
            return Math.round((paid / total) * 100);
        },
        
        editOrder(order) {
            uni.showModal({
                title: '编辑订单',
                content: '请使用创建订单页面的编辑功能',
                showCancel: false
            });
        },
        
        updatePayment(order) {
            this.editingOrder = order;
            this.paymentAmount = '';
            
            uni.showModal({
                title: '登记回款',
                editable: true,
                placeholderText: '请输入回款金额',
                success: async (res) => {
                    if (res.confirm && res.content) {
                        const amount = Number(res.content);
                        if (isNaN(amount) || amount <= 0) {
                            uni.showToast({ title: '请输入有效金额', icon: 'none' });
                            return;
                        }
                        
                        const currentPaid = Number(order.paidAmount) || 0;
                        const totalAmount = Number(order.totalAmount) || 0;
                        const newPaid = currentPaid + amount;
                        
                        let status = 'PARTIAL';
                        if (newPaid >= totalAmount) {
                            status = 'COMPLETED';
                        }
                        
                        try {
                            await financeApi.updateOrder(order.id, {
                                paidAmount: newPaid,
                                status: status
                            });
                            uni.showToast({ title: '回款登记成功', icon: 'success' });
                            this.loadOrders();
                        } catch (e) {
                            console.error('更新失败', e);
                        }
                    }
                }
            });
        },
        
        goToAdd() {
            uni.navigateTo({ url: '/pages/order/add' });
        }
    }
};
</script>

<style scoped>
.order-list-page {
    padding-bottom: 160rpx;
}

.header-banner {
    background: linear-gradient(135deg, #667eea, #764ba2);
    padding: 40rpx;
}

.header-title {
    display: block;
    font-size: 36rpx;
    font-weight: bold;
    color: #fff;
    margin-bottom: 8rpx;
}

.header-desc {
    font-size: 26rpx;
    color: rgba(255, 255, 255, 0.8);
    margin-bottom: 24rpx;
    display: block;
}

.status-tabs {
    display: flex;
    gap: 12rpx;
    flex-wrap: wrap;
}

.tab-item {
    background: rgba(255, 255, 255, 0.15);
    color: rgba(255, 255, 255, 0.8);
    padding: 12rpx 24rpx;
    border-radius: 24rpx;
    font-size: 24rpx;
}

.tab-item.active {
    background: #fff;
    color: #667eea;
    font-weight: bold;
}

.stats-row {
    margin: -30rpx 30rpx 24rpx;
    position: relative;
    z-index: 10;
    display: flex;
}

.stats-row .stat-item {
    flex: 1;
    text-align: center;
    padding: 16rpx 0;
}

.stats-row .stat-value {
    display: block;
    font-size: 28rpx;
    font-weight: bold;
    color: #3c9cff;
    margin-bottom: 4rpx;
}

.stats-row .stat-label {
    font-size: 22rpx;
    color: #999;
}

.order-list {
    padding: 0 24rpx;
}

.order-item {
    margin-bottom: 20rpx;
}

.order-header {
    padding-bottom: 20rpx;
    border-bottom: 1rpx solid #f0f0f0;
    margin-bottom: 20rpx;
}

.order-no {
    display: block;
    font-size: 28rpx;
    font-weight: bold;
    color: #333;
    margin-bottom: 4rpx;
}

.order-date {
    font-size: 22rpx;
    color: #999;
}

.detail-row {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 12rpx;
}

.detail-label {
    font-size: 26rpx;
    color: #666;
}

.detail-value {
    font-size: 26rpx;
    font-weight: bold;
}

.detail-value.primary { color: #3c9cff; }
.detail-value.success { color: #67c23a; }
.detail-value.warning { color: #e6a23c; }

.progress-section {
    margin-top: 16rpx;
    padding-top: 16rpx;
    border-top: 1rpx solid #f5f5f5;
}

.progress-text {
    display: block;
    text-align: right;
    font-size: 22rpx;
    color: #999;
    margin-top: 8rpx;
}

.order-actions {
    display: flex;
    gap: 16rpx;
    margin-top: 20rpx;
    padding-top: 20rpx;
    border-top: 1rpx solid #f5f5f5;
}

.action-btn {
    flex: 1;
    padding: 16rpx;
    border-radius: 24rpx;
    font-size: 26rpx;
    margin: 0;
}

.action-btn.outline {
    background: #f5f5f5;
    color: #666;
}

.action-btn.primary {
    background: linear-gradient(135deg, #3c9cff, #5ba8ff);
    color: #fff;
}

.empty-state {
    text-align: center;
    padding: 160rpx 40rpx;
}

.empty-icon {
    font-size: 120rpx;
    display: block;
    margin-bottom: 20rpx;
}

.empty-text {
    display: block;
    font-size: 28rpx;
    color: #999;
}

.fab-button {
    position: fixed;
    right: 40rpx;
    bottom: 60rpx;
    width: 100rpx;
    height: 100rpx;
    border-radius: 50%;
    background: linear-gradient(135deg, #667eea, #764ba2);
    display: flex;
    align-items: center;
    justify-content: center;
    box-shadow: 0 8rpx 24rpx rgba(102, 126, 234, 0.4);
    z-index: 99;
}

.fab-icon {
    font-size: 60rpx;
    color: #fff;
    font-weight: lighter;
    line-height: 1;
}
</style>
