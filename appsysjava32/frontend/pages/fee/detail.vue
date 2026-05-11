<template>
    <view class="fee-detail" :class="{ 'elder-mode': elderMode }">
        <view class="loading-state" v-if="loading">
            <text>加载中...</text>
        </view>

        <view class="detail-container" v-else-if="detail">
            <view class="amount-card">
                <text class="amount-label">费用金额</text>
                <text class="amount-value">¥{{ formatAmount(detail.amount) }}</text>
                <view class="amount-status" :class="detail.paid ? 'status-paid' : 'status-pending'">
                    {{ detail.paid ? '已结算' : '待结算' }}
                </view>
            </view>

            <view class="info-card">
                <view class="card-header">
                    <text class="card-title">费用详情</text>
                </view>
                <view class="card-body">
                    <view class="info-row">
                        <text class="info-label">费用名称</text>
                        <text class="info-value">{{ detail.feeName }}</text>
                    </view>
                    <view class="info-row">
                        <text class="info-label">费用类型</text>
                        <text class="info-value">{{ getFeeTypeName(detail.feeType) }}</text>
                    </view>
                    <view class="info-row">
                        <text class="info-label">费用日期</text>
                        <text class="info-value">{{ formatDate(detail.feeDate, 'yyyy-MM-dd HH:mm') }}</text>
                    </view>
                    <view class="info-row" v-if="detail.quantity">
                        <text class="info-label">数量</text>
                        <text class="info-value">{{ detail.quantity }}{{ detail.unit || '项' }}</text>
                    </view>
                    <view class="info-row" v-if="detail.unitPrice">
                        <text class="info-label">单价</text>
                        <text class="info-value">¥{{ formatAmount(detail.unitPrice) }}</text>
                    </view>
                    <view class="info-row">
                        <text class="info-label">所属科室</text>
                        <text class="info-value">{{ detail.deptName || '-' }}</text>
                    </view>
                    <view class="info-row">
                        <text class="info-label">开单医生</text>
                        <text class="info-value">{{ detail.doctorName || '-' }}</text>
                    </view>
                </view>
            </view>

            <view class="info-card" v-if="detail.remark">
                <view class="card-header">
                    <text class="card-title">备注</text>
                </view>
                <view class="card-body">
                    <text class="remark-text">{{ detail.remark }}</text>
                </view>
            </view>

            <view class="info-card">
                <view class="card-header">
                    <text class="card-title">支付信息</text>
                </view>
                <view class="card-body">
                    <view class="info-row">
                        <text class="info-label">支付时间</text>
                        <text class="info-value">{{ detail.payTime ? formatDate(detail.payTime, 'yyyy-MM-dd HH:mm') : '-' }}</text>
                    </view>
                    <view class="info-row">
                        <text class="info-label">支付方式</text>
                        <text class="info-value">{{ detail.payMethod || '-' }}</text>
                    </view>
                </view>
            </view>
        </view>

        <view class="empty-state" v-else>
            <text class="empty-icon">❓</text>
            <text class="empty-text">未找到费用记录</text>
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
            id: '',
            detail: null,
            loading: false
        }
    },
    onLoad(options) {
        this.elderMode = uni.getStorageSync('elderMode') === 1
        if (options.id) {
            this.id = options.id
            this.loadData()
        }
    },
    methods: {
        async loadData() {
            this.loading = true
            try {
                const res = await api.fee.list({ pageNum: 1, pageSize: 1 })
                if (res.code === 200 && res.data.records && res.data.records.length > 0) {
                    this.detail = res.data.records.find(item => item.id == this.id) || res.data.records[0]
                }
            } catch (e) {
                console.error('加载失败:', e)
            } finally {
                this.loading = false
            }
        },

        formatDate(date, fmt) {
            return util.formatDate(date, fmt)
        },

        formatAmount(amount) {
            return util.formatAmount(amount)
        },

        getFeeTypeName(type) {
            const map = {
                'MEDICINE': '药品费',
                'EXAMINATION': '检查费',
                'TREATMENT': '治疗费',
                'SURGERY': '手术费',
                'NURSING': '护理费',
                'BED': '床位费',
                'OTHER': '其他费用'
            }
            return map[type] || '其他费用'
        }
    }
}
</script>

<style scoped>
.fee-detail {
    min-height: 100vh;
    background: #F5F5F5;
    padding: 20rpx;
}

.elder-mode {
    font-size: 36rpx;
}

.loading-state, .empty-state {
    display: flex;
    flex-direction: column;
    align-items: center;
    padding: 120rpx 0;
    color: #999;
}

.empty-icon {
    font-size: 120rpx;
    margin-bottom: 20rpx;
    opacity: 0.5;
}

.empty-text {
    font-size: 28rpx;
}

.elder-mode .empty-text {
    font-size: 32rpx;
}

.amount-card {
    background: linear-gradient(135deg, #1890FF 0%, #096DD9 100%);
    border-radius: 24rpx;
    padding: 48rpx;
    text-align: center;
    color: #fff;
    margin-bottom: 20rpx;
}

.amount-label {
    font-size: 28rpx;
    opacity: 0.9;
}

.elder-mode .amount-label {
    font-size: 32rpx;
}

.amount-value {
    font-size: 64rpx;
    font-weight: 700;
    margin: 16rpx 0;
    display: block;
}

.elder-mode .amount-value {
    font-size: 72rpx;
}

.amount-status {
    display: inline-block;
    padding: 8rpx 24rpx;
    border-radius: 24rpx;
    font-size: 24rpx;
}

.elder-mode .amount-status {
    font-size: 28rpx;
    padding: 12rpx 32rpx;
}

.status-paid {
    background: rgba(82, 196, 26, 0.2);
    color: #95DE64;
}

.status-pending {
    background: rgba(250, 173, 20, 0.2);
    color: #FFD666;
}

.info-card {
    background: #fff;
    border-radius: 20rpx;
    margin-bottom: 20rpx;
    overflow: hidden;
}

.card-header {
    padding: 28rpx;
    border-bottom: 1rpx solid #F5F5F5;
}

.card-title {
    font-size: 32rpx;
    font-weight: 600;
    color: #333;
}

.elder-mode .card-title {
    font-size: 36rpx;
}

.card-body {
    padding: 28rpx;
}

.info-row {
    display: flex;
    padding: 16rpx 0;
}

.info-label {
    width: 200rpx;
    font-size: 28rpx;
    color: #999;
    flex-shrink: 0;
}

.elder-mode .info-label {
    width: 240rpx;
    font-size: 32rpx;
}

.info-value {
    flex: 1;
    font-size: 28rpx;
    color: #333;
}

.elder-mode .info-value {
    font-size: 32rpx;
}

.remark-text {
    font-size: 28rpx;
    color: #333;
    line-height: 1.8;
}

.elder-mode .remark-text {
    font-size: 32rpx;
}
</style>
