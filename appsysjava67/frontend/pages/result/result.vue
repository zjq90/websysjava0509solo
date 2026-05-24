<template>
    <view class="container">
        <view class="result-card" :class="result.legal ? 'legal' : 'illegal'">
            <view class="result-icon">
                <text v-if="result.legal">✓</text>
                <text v-else>!</text>
            </view>
            <view class="result-status">
                <text class="status-text">{{ result.message }}</text>
                <text class="plate-number">{{ result.plateNumber }}</text>
            </view>
        </view>

        <view class="info-card" v-if="result.brand">
            <view class="card-title">车辆信息</view>
            <view class="info-row">
                <text class="info-label">品牌型号</text>
                <text class="info-value">{{ result.brand }} {{ result.model }}</text>
            </view>
            <view class="info-row">
                <text class="info-label">车身颜色</text>
                <text class="info-value">{{ result.color }}</text>
            </view>
            <view class="info-row" v-if="result.ownerName">
                <text class="info-label">车主姓名</text>
                <text class="info-value">{{ result.ownerName }}</text>
            </view>
        </view>

        <view class="violation-card" v-if="!result.legal">
            <view class="card-title">违规详情</view>
            <view class="violation-badge" :class="result.riskLevel">
                {{ riskText }}
            </view>
            <view class="violation-info">
                <view class="violation-row">
                    <text class="violation-label">违规类型</text>
                    <text class="violation-value">{{ result.violationReason }}</text>
                </view>
                <view class="violation-row">
                    <text class="violation-label">详细描述</text>
                    <text class="violation-value">{{ result.violationDescription }}</text>
                </view>
            </view>
        </view>

        <view class="action-buttons">
            <button class="btn btn-secondary" @click="goBack">返回查验</button>
            <button class="btn btn-primary" @click="goEnforcement" v-if="!result.legal">
                现场执法
            </button>
        </view>
    </view>
</template>

<script>
export default {
    data() {
        return {
            result: {
                plateNumber: '',
                legal: true,
                message: '',
                riskLevel: 'LOW',
                violationReason: '',
                violationDescription: '',
                brand: '',
                model: '',
                color: '',
                ownerName: ''
            }
        }
    },
    computed: {
        riskText() {
            const map = {
                'LOW': '低风险',
                'MEDIUM': '中风险',
                'HIGH': '高风险',
                'CRITICAL': '极高风险'
            }
            return map[this.result.riskLevel] || '未知'
        }
    },
    onLoad(options) {
        if (options.data) {
            this.result = JSON.parse(decodeURIComponent(options.data))
        }
        if (!this.result.legal) {
            this.playAlarm()
        }
    },
    methods: {
        playAlarm() {
            uni.vibrateLong({
                success: () => {
                    console.log('震动提醒')
                }
            })
        },
        goBack() {
            uni.switchTab({
                url: '/pages/scan/scan'
            })
        },
        goEnforcement() {
            uni.navigateTo({
                url: `/pages/enforcement/enforcement?plateNumber=${this.result.plateNumber}`
            })
        }
    }
}
</script>

<style scoped>
.container {
    padding: 20rpx;
    min-height: 100vh;
}

.result-card {
    border-radius: 24rpx;
    padding: 60rpx 40rpx;
    display: flex;
    flex-direction: column;
    align-items: center;
    margin-bottom: 30rpx;
}

.result-card.legal {
    background: linear-gradient(135deg, #34a853, #1e7e34);
}

.result-card.illegal {
    background: linear-gradient(135deg, #ea4335, #c5221f);
}

.result-icon {
    width: 120rpx;
    height: 120rpx;
    border-radius: 50%;
    background: rgba(255, 255, 255, 0.2);
    display: flex;
    align-items: center;
    justify-content: center;
    font-size: 64rpx;
    color: white;
    font-weight: bold;
    margin-bottom: 30rpx;
}

.result-status {
    text-align: center;
}

.status-text {
    font-size: 36rpx;
    color: white;
    font-weight: bold;
    display: block;
    margin-bottom: 16rpx;
}

.plate-number {
    font-size: 48rpx;
    color: white;
    font-weight: bold;
    letter-spacing: 4rpx;
}

.info-card, .violation-card {
    background: var(--card-bg);
    border-radius: 16rpx;
    padding: 30rpx;
    margin-bottom: 30rpx;
}

.card-title {
    font-size: 32rpx;
    font-weight: bold;
    color: var(--text-color);
    margin-bottom: 24rpx;
    display: block;
}

.info-row, .violation-row {
    display: flex;
    justify-content: space-between;
    padding: 16rpx 0;
    border-bottom: 1rpx solid var(--border-color);
}

.info-row:last-child, .violation-row:last-child {
    border-bottom: none;
}

.info-label, .violation-label {
    font-size: 28rpx;
    color: var(--text-secondary);
}

.info-value, .violation-value {
    font-size: 28rpx;
    color: var(--text-color);
}

.violation-badge {
    display: inline-block;
    padding: 12rpx 30rpx;
    border-radius: 30rpx;
    font-size: 26rpx;
    font-weight: bold;
    margin-bottom: 20rpx;
}

.violation-badge.LOW {
    background: #e6f4ea;
    color: #34a853;
}

.violation-badge.MEDIUM {
    background: #fef7e0;
    color: #f57c00;
}

.violation-badge.HIGH {
    background: #fce8e6;
    color: #ea4335;
}

.violation-badge.CRITICAL {
    background: #fce8e6;
    color: #d93025;
}

.action-buttons {
    display: flex;
    gap: 20rpx;
    margin-top: 40rpx;
}

.btn {
    flex: 1;
    height: 88rpx;
    line-height: 88rpx;
    border-radius: 50rpx;
    font-size: 32rpx;
    border: none;
}

.btn-secondary {
    background: var(--card-bg);
    color: var(--text-color);
    border: 2rpx solid var(--border-color);
}

.btn-primary {
    background: linear-gradient(135deg, #1a73e8, #0d47a1);
    color: white;
}
</style>
