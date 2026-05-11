<template>
    <view class="deposit-list" :class="{ 'elder-mode': elderMode }">
        <view class="summary-card">
            <view class="summary-header">
                <text class="summary-title">押金总览</text>
                <text class="summary-action" @click="goToPay">补缴押金</text>
            </view>
            <view class="summary-body">
                <view class="summary-item">
                    <text class="summary-value">¥{{ formatAmount(summary.totalDeposit || 0) }}</text>
                    <text class="summary-label">累计缴纳</text>
                </view>
                <view class="summary-divider"></view>
                <view class="summary-item">
                    <text class="summary-value value-used">¥{{ formatAmount(summary.usedDeposit || 0) }}</text>
                    <text class="summary-label">已使用</text>
                </view>
                <view class="summary-divider"></view>
                <view class="summary-item">
                    <text class="summary-value value-remain">¥{{ formatAmount(summary.remainDeposit || 0) }}</text>
                    <text class="summary-label">剩余可用</text>
                </view>
            </view>
        </view>

        <view class="list-header">
            <text class="header-title">押金记录</text>
            <view class="filter-tabs">
                <view 
                    class="filter-tab"
                    :class="{ active: currentFilter === 'all' }"
                    @click="switchFilter('all')"
                >全部</view>
                <view 
                    class="filter-tab"
                    :class="{ active: currentFilter === 'success' }"
                    @click="switchFilter('success')"
                >成功</view>
                <view 
                    class="filter-tab"
                    :class="{ active: currentFilter === 'pending' }"
                    @click="switchFilter('pending')"
                >待支付</view>
            </view>
        </view>

        <view class="list-container">
            <view 
                class="deposit-card"
                v-for="item in filteredList"
                :key="item.id"
            >
                <view class="card-header">
                    <view class="type-info">
                        <text class="type-icon">{{ getTypeIcon(item.payMethod) }}</text>
                        <text class="type-name">{{ getTypeName(item.payMethod) }}</text>
                    </view>
                    <view class="status-tag" :class="getStatusClass(item.status)">
                        {{ getStatusText(item.status) }}
                    </view>
                </view>
                <view class="card-body">
                    <view class="amount-row">
                        <text class="amount-label">补缴金额</text>
                        <text class="amount-value">+¥{{ formatAmount(item.amount) }}</text>
                    </view>
                    <view class="info-row">
                        <text class="info-label">订单号</text>
                        <text class="info-value">{{ item.orderNo }}</text>
                    </view>
                    <view class="info-row">
                        <text class="info-label">支付时间</text>
                        <text class="info-value">{{ item.payTime ? formatDate(item.payTime, 'yyyy-MM-dd HH:mm') : '-' }}</text>
                    </view>
                </view>
                <view class="card-footer" v-if="item.status === 0">
                    <button class="btn btn-default btn-small" @click="cancelOrder(item)">取消</button>
                    <button class="btn btn-primary btn-small" @click="goToPay(item)">去支付</button>
                </view>
            </view>

            <view class="empty-state" v-if="!filteredList.length && !loading">
                <text class="empty-icon">💳</text>
                <text class="empty-text">暂无押金记录</text>
                <button class="btn btn-primary btn-small mt-20" @click="goToPay">立即补缴</button>
            </view>

            <view class="loading-state" v-if="loading">
                <text>加载中...</text>
            </view>
        </view>
    </view>
</template>

<script>
import api from '@/common/js/api.js'
import util from '@/common/js/util.js'

export default {
    data() {
        return {
            elderMode: false,
            currentFilter: 'all',
            list: [],
            summary: {
                totalDeposit: 0,
                usedDeposit: 0,
                remainDeposit: 0
            },
            loading: false
        }
    },
    computed: {
        filteredList() {
            if (this.currentFilter === 'all') {
                return this.list
            }
            if (this.currentFilter === 'success') {
                return this.list.filter(item => item.status === 1)
            }
            if (this.currentFilter === 'pending') {
                return this.list.filter(item => item.status === 0)
            }
            return this.list
        }
    },
    onLoad() {
        this.elderMode = uni.getStorageSync('elderMode') === 1
    },
    onShow() {
        this.loadData()
    },
    methods: {
        async loadData() {
            this.loading = true
            try {
                const res = await api.deposit.list({ pageNum: 1, pageSize: 50 })
                if (res.code === 200) {
                    this.list = res.data.records || []
                    this.calculateSummary()
                }
            } catch (e) {
                console.error('加载失败:', e)
            } finally {
                this.loading = false
            }
        },

        calculateSummary() {
            let total = 0
            let used = 0
            this.list.forEach(item => {
                if (item.status === 1) {
                    total += parseFloat(item.amount) || 0
                }
            })
            this.summary.totalDeposit = total
            this.summary.usedDeposit = used
            this.summary.remainDeposit = total - used
        },

        switchFilter(filter) {
            this.currentFilter = filter
        },

        formatDate(date, fmt) {
            return util.formatDate(date, fmt)
        },

        formatAmount(amount) {
            return util.formatAmount(amount)
        },

        getTypeIcon(method) {
            const icons = {
                'wechat': '💚',
                'alipay': '💙',
                'balance': '💰',
                'cash': '💵'
            }
            return icons[method] || '💳'
        },

        getTypeName(method) {
            const names = {
                'wechat': '微信支付',
                'alipay': '支付宝',
                'balance': '账户余额',
                'cash': '现金支付'
            }
            return names[method] || '其他支付'
        },

        getStatusText(status) {
            const map = {
                0: '待支付',
                1: '支付成功',
                2: '已取消',
                3: '支付失败'
            }
            return map[status] || '未知'
        },

        getStatusClass(status) {
            const map = {
                0: 'status-pending',
                1: 'status-approved',
                2: 'status-cancelled',
                3: 'status-danger'
            }
            return map[status] || 'status-pending'
        },

        goToPay(item) {
            if (item) {
                uni.showToast({ title: '该订单待支付', icon: 'none' })
            } else {
                uni.navigateTo({ url: '/pages/deposit/pay' })
            }
        },

        async cancelOrder(item) {
            const confirmed = await util.confirm('确定要取消该订单吗？')
            if (!confirmed) return

            try {
                util.showLoading('取消中...')
                const res = await api.deposit.cancel(item.orderNo)
                util.hideLoading()
                if (res.code === 200) {
                    util.showToast('取消成功', 'success')
                    this.loadData()
                }
            } catch (e) {
                util.hideLoading()
            }
        }
    }
}
</script>

<style scoped>
.deposit-list {
    min-height: 100vh;
    background: #F5F5F5;
    padding: 20rpx;
}

.elder-mode {
    font-size: 36rpx;
}

.summary-card {
    background: linear-gradient(135deg, #1890FF 0%, #096DD9 100%);
    border-radius: 24rpx;
    padding: 32rpx;
    margin-bottom: 20rpx;
    color: #fff;
}

.summary-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 24rpx;
}

.summary-title {
    font-size: 32rpx;
    font-weight: 600;
}

.elder-mode .summary-title {
    font-size: 36rpx;
}

.summary-action {
    font-size: 26rpx;
    padding: 8rpx 24rpx;
    background: rgba(255, 255, 255, 0.2);
    border-radius: 30rpx;
}

.elder-mode .summary-action {
    font-size: 30rpx;
    padding: 12rpx 28rpx;
}

.summary-body {
    display: flex;
    align-items: center;
}

.summary-item {
    flex: 1;
    text-align: center;
}

.summary-value {
    font-size: 40rpx;
    font-weight: 700;
    display: block;
    margin-bottom: 8rpx;
}

.elder-mode .summary-value {
    font-size: 48rpx;
}

.value-used {
    color: #FFD666;
}

.value-remain {
    color: #95DE64;
}

.summary-label {
    font-size: 24rpx;
    opacity: 0.8;
}

.elder-mode .summary-label {
    font-size: 28rpx;
}

.summary-divider {
    width: 1rpx;
    height: 60rpx;
    background: rgba(255, 255, 255, 0.3);
}

.list-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 16rpx 8rpx;
    flex-wrap: wrap;
}

.header-title {
    font-size: 30rpx;
    font-weight: 600;
    color: #333;
    margin-bottom: 16rpx;
    width: 100%;
}

.elder-mode .header-title {
    font-size: 34rpx;
}

.filter-tabs {
    display: flex;
    gap: 16rpx;
    width: 100%;
}

.filter-tab {
    padding: 12rpx 28rpx;
    font-size: 26rpx;
    color: #666;
    background: #fff;
    border-radius: 30rpx;
    border: 1rpx solid #E8E8E8;
}

.elder-mode .filter-tab {
    font-size: 30rpx;
    padding: 16rpx 32rpx;
}

.filter-tab.active {
    color: #1890FF;
    background: #E6F7FF;
    border-color: #1890FF;
}

.list-container {
    padding-bottom: 20rpx;
}

.deposit-card {
    background: #fff;
    border-radius: 20rpx;
    padding: 28rpx;
    margin-bottom: 16rpx;
}

.card-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 20rpx;
}

.type-info {
    display: flex;
    align-items: center;
    gap: 12rpx;
}

.type-icon {
    font-size: 40rpx;
}

.elder-mode .type-icon {
    font-size: 48rpx;
}

.type-name {
    font-size: 30rpx;
    font-weight: 600;
    color: #333;
}

.elder-mode .type-name {
    font-size: 34rpx;
}

.card-body {
    padding: 20rpx 0;
    border-top: 1rpx solid #F5F5F5;
    border-bottom: 1rpx solid #F5F5F5;
}

.amount-row {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 16rpx;
}

.amount-label {
    font-size: 28rpx;
    color: #666;
}

.elder-mode .amount-label {
    font-size: 32rpx;
}

.amount-value {
    font-size: 40rpx;
    font-weight: 700;
    color: #52C41A;
}

.elder-mode .amount-value {
    font-size: 48rpx;
}

.info-row {
    display: flex;
    padding: 8rpx 0;
}

.info-label {
    width: 160rpx;
    font-size: 26rpx;
    color: #999;
    flex-shrink: 0;
}

.elder-mode .info-label {
    width: 200rpx;
    font-size: 30rpx;
}

.info-value {
    flex: 1;
    font-size: 26rpx;
    color: #666;
    word-break: break-all;
}

.elder-mode .info-value {
    font-size: 30rpx;
}

.card-footer {
    display: flex;
    justify-content: flex-end;
    gap: 20rpx;
    padding-top: 20rpx;
}

.empty-state, .loading-state {
    display: flex;
    flex-direction: column;
    align-items: center;
    padding: 80rpx 0;
    color: #999;
}

.empty-icon {
    font-size: 100rpx;
    margin-bottom: 20rpx;
    opacity: 0.5;
}

.empty-text {
    font-size: 28rpx;
}

.elder-mode .empty-text {
    font-size: 32rpx;
}

.status-danger {
    background: #FFF2F0;
    color: #D9363E;
}
</style>
