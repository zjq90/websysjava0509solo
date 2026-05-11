<template>
    <view class="fee-list" :class="{ 'elder-mode': elderMode }">
        <view class="summary-card">
            <view class="summary-header">
                <text class="summary-title">费用总览</text>
            </view>
            <view class="summary-body">
                <view class="summary-item">
                    <text class="summary-value">¥{{ formatAmount(summary.totalAmount || 0) }}</text>
                    <text class="summary-label">累计费用</text>
                </view>
                <view class="summary-divider"></view>
                <view class="summary-item">
                    <text class="summary-value value-paid">¥{{ formatAmount(summary.paidAmount || 0) }}</text>
                    <text class="summary-label">已付金额</text>
                </view>
                <view class="summary-divider"></view>
                <view class="summary-item">
                    <text class="summary-value value-owe">¥{{ formatAmount(summary.oweAmount || 0) }}</text>
                    <text class="summary-label">待付金额</text>
                </view>
            </view>
        </view>

        <view class="date-filter">
            <view class="date-range">
                <picker mode="date" :value="form.startDate" @change="onStartDateChange">
                    <view class="date-picker">
                        <text class="picker-label">开始</text>
                        <text class="picker-value">{{ form.startDate || '选择日期' }}</text>
                    </view>
                </picker>
                <text class="date-separator">至</text>
                <picker mode="date" :value="form.endDate" @change="onEndDateChange">
                    <view class="date-picker">
                        <text class="picker-label">结束</text>
                        <text class="picker-value">{{ form.endDate || '选择日期' }}</text>
                    </view>
                </picker>
            </view>
            <button class="btn btn-primary btn-small" @click="search">查询</button>
        </view>

        <view class="list-header">
            <text class="header-title">费用明细</text>
            <text class="header-count">共 {{ list.length }} 条</text>
        </view>

        <view class="list-container">
            <view 
                class="fee-card"
                v-for="item in list"
                :key="item.id"
                @click="viewDetail(item)"
            >
                <view class="fee-header">
                    <view class="fee-icon" :class="getFeeTypeClass(item.feeType)">
                        {{ getFeeTypeIcon(item.feeType) }}
                    </view>
                    <view class="fee-info">
                        <text class="fee-name">{{ item.feeName }}</text>
                        <text class="fee-date">{{ formatDate(item.feeDate, 'yyyy-MM-dd') }}</text>
                    </view>
                    <view class="fee-amount">
                        <text class="amount-value">¥{{ formatAmount(item.amount) }}</text>
                        <text class="amount-status">{{ item.paid ? '已结算' : '待结算' }}</text>
                    </view>
                </view>
                <view class="fee-footer" v-if="item.quantity || item.unitPrice">
                    <text class="fee-quantity">
                        {{ item.quantity || 1 }}{{ item.unit || '项'}} × ¥{{ formatAmount(item.unitPrice || item.amount) }}
                    </text>
                    <text class="fee-remark" v-if="item.remark">{{ item.remark }}</text>
                </view>
            </view>

            <view class="empty-state" v-if="!list.length && !loading">
                <text class="empty-icon">📋</text>
                <text class="empty-text">暂无费用记录</text>
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
            list: [],
            summary: {},
            loading: false,
            form: {
                startDate: '',
                endDate: ''
            }
        }
    },
    computed: {
        
    },
    onLoad() {
        this.elderMode = uni.getStorageSync('elderMode') === 1
        this.setDefaultDate()
    },
    onShow() {
        this.loadData()
    },
    methods: {
        setDefaultDate() {
            const today = new Date()
            const lastMonth = new Date(today.getFullYear(), today.getMonth() - 1, today.getDate())
            this.form.startDate = this.formatDateObj(lastMonth)
            this.form.endDate = this.formatDateObj(today)
        },

        formatDateObj(date) {
            const y = date.getFullYear()
            const m = String(date.getMonth() + 1).padStart(2, '0')
            const d = String(date.getDate()).padStart(2, '0')
            return `${y}-${m}-${d}`
        },

        onStartDateChange(e) {
            this.form.startDate = e.detail.value
        },

        onEndDateChange(e) {
            this.form.endDate = e.detail.value
        },

        async loadData() {
            this.loading = true
            try {
                const [listRes, summaryRes] = await Promise.all([
                    api.fee.list({ pageNum: 1, pageSize: 100, ...this.form }),
                    api.fee.summary(this.form)
                ])
                
                if (listRes.code === 200) {
                    this.list = listRes.data.records || []
                }
                if (summaryRes.code === 200) {
                    this.summary = summaryRes.data
                }
            } catch (e) {
                console.error('加载失败:', e)
            } finally {
                this.loading = false
            }
        },

        search() {
            this.loadData()
        },

        formatDate(date, fmt) {
            return util.formatDate(date, fmt)
        },

        formatAmount(amount) {
            return util.formatAmount(amount)
        },

        getFeeTypeIcon(type) {
            const icons = {
                'MEDICINE': '💊',
                'EXAMINATION': '🔬',
                'TREATMENT': '💉',
                'SURGERY': '🏥',
                'NURSING': '👩‍⚕️',
                'BED': '🛏️',
                'OTHER': '📋'
            }
            return icons[type] || '📋'
        },

        getFeeTypeClass(type) {
            const classes = {
                'MEDICINE': 'type-medicine',
                'EXAMINATION': 'type-exam',
                'TREATMENT': 'type-treatment',
                'SURGERY': 'type-surgery',
                'NURSING': 'type-nursing',
                'BED': 'type-bed',
                'OTHER': 'type-other'
            }
            return classes[type] || 'type-other'
        },

        viewDetail(item) {
            uni.navigateTo({
                url: '/pages/fee/detail?id=' + item.id
            })
        }
    }
}
</script>

<style scoped>
.fee-list {
    min-height: 100vh;
    background: #F5F5F5;
    padding: 20rpx;
    padding-bottom: 40rpx;
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
    margin-bottom: 24rpx;
}

.summary-title {
    font-size: 32rpx;
    font-weight: 600;
}

.elder-mode .summary-title {
    font-size: 36rpx;
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

.value-paid {
    color: #95DE64;
}

.value-owe {
    color: #FFD666;
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

.date-filter {
    background: #fff;
    border-radius: 20rpx;
    padding: 24rpx;
    margin-bottom: 20rpx;
    display: flex;
    align-items: center;
    gap: 20rpx;
}

.date-range {
    flex: 1;
    display: flex;
    align-items: center;
    gap: 16rpx;
}

.date-picker {
    flex: 1;
    background: #f5f5f5;
    border-radius: 12rpx;
    padding: 16rpx 20rpx;
}

.picker-label {
    font-size: 22rpx;
    color: #999;
    display: block;
}

.elder-mode .picker-label {
    font-size: 26rpx;
}

.picker-value {
    font-size: 28rpx;
    color: #333;
    display: block;
    margin-top: 4rpx;
}

.elder-mode .picker-value {
    font-size: 32rpx;
}

.date-separator {
    font-size: 28rpx;
    color: #999;
}

.list-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 16rpx 8rpx;
}

.header-title {
    font-size: 30rpx;
    font-weight: 600;
    color: #333;
}

.elder-mode .header-title {
    font-size: 34rpx;
}

.header-count {
    font-size: 24rpx;
    color: #999;
}

.elder-mode .header-count {
    font-size: 28rpx;
}

.list-container {
    padding-bottom: 20rpx;
}

.fee-card {
    background: #fff;
    border-radius: 20rpx;
    padding: 28rpx;
    margin-bottom: 16rpx;
}

.fee-header {
    display: flex;
    align-items: center;
}

.fee-icon {
    width: 80rpx;
    height: 80rpx;
    border-radius: 16rpx;
    display: flex;
    align-items: center;
    justify-content: center;
    font-size: 40rpx;
    margin-right: 20rpx;
}

.elder-mode .fee-icon {
    width: 90rpx;
    height: 90rpx;
    font-size: 48rpx;
}

.type-medicine { background: #FFECE8; }
.type-exam { background: #E6F7FF; }
.type-treatment { background: #F6FFED; }
.type-surgery { background: #FFF1F0; }
.type-nursing { background: #FFFBE6; }
.type-bed { background: #F9F0FF; }
.type-other { background: #F0F0F0; }

.fee-info {
    flex: 1;
}

.fee-name {
    font-size: 30rpx;
    font-weight: 600;
    color: #333;
    display: block;
}

.elder-mode .fee-name {
    font-size: 34rpx;
}

.fee-date {
    font-size: 24rpx;
    color: #999;
    margin-top: 6rpx;
    display: block;
}

.elder-mode .fee-date {
    font-size: 28rpx;
}

.fee-amount {
    text-align: right;
}

.amount-value {
    font-size: 34rpx;
    font-weight: 700;
    color: #333;
    display: block;
}

.elder-mode .amount-value {
    font-size: 38rpx;
}

.amount-status {
    font-size: 22rpx;
    color: #52C41A;
    margin-top: 6rpx;
    display: block;
}

.elder-mode .amount-status {
    font-size: 26rpx;
}

.fee-footer {
    margin-top: 16rpx;
    padding-top: 16rpx;
    border-top: 1rpx solid #f0f0f0;
    display: flex;
    justify-content: space-between;
}

.fee-quantity {
    font-size: 26rpx;
    color: #666;
}

.elder-mode .fee-quantity {
    font-size: 30rpx;
}

.fee-remark {
    font-size: 24rpx;
    color: #999;
    max-width: 300rpx;
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: nowrap;
}

.elder-mode .fee-remark {
    font-size: 28rpx;
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
</style>
