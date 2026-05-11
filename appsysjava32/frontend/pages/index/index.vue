<template>
    <view class="index-container" :class="{ 'elder-mode': elderMode }">
        <view class="header">
            <view class="header-top">
                <view class="user-info">
                    <text class="greeting">👋 您好</text>
                    <text class="username">{{ userInfo ? userInfo.realName || userInfo.username : '用户' }}</text>
                </view>
                <view class="header-actions">
                    <view class="elder-btn" @click="toggleElderMode">
                        {{ elderMode ? '👓' : '👵' }}
                    </view>
                </view>
            </view>
        </view>

        <view class="quick-actions card">
            <view class="action-grid">
                <view class="action-item" @click="goToApply">
                    <view class="action-icon bg-blue">📋</view>
                    <text class="action-text">入院申请</text>
                </view>
                <view class="action-item" @click="goToFee">
                    <view class="action-icon bg-green">💰</view>
                    <text class="action-text">费用查询</text>
                </view>
                <view class="action-item" @click="goToDeposit">
                    <view class="action-icon bg-orange">💳</view>
                    <text class="action-text">押金补缴</text>
                </view>
                <view class="action-item" @click="goToAdmission">
                    <view class="action-icon bg-purple">🏥</view>
                    <text class="action-text">床位查询</text>
                </view>
            </view>
        </view>

        <view class="card">
            <view class="card-header">
                <text class="card-title">住院申请状态</text>
                <text class="link-text" @click="goToAdmission">查看全部</text>
            </view>
            
            <view class="status-summary">
                <view class="status-item">
                    <text class="status-count" style="color: #FAAD14;">{{ stats.pending || 0 }}</text>
                    <text class="status-label">待审核</text>
                </view>
                <view class="status-divider"></view>
                <view class="status-item">
                    <text class="status-count" style="color: #52C41A;">{{ stats.approved || 0 }}</text>
                    <text class="status-label">已通过</text>
                </view>
                <view class="status-divider"></view>
                <view class="status-item">
                    <text class="status-count" style="color: #1890FF;">{{ stats.admitted || 0 }}</text>
                    <text class="status-label">住院中</text>
                </view>
                <view class="status-divider"></view>
                <view class="status-item">
                    <text class="status-count" style="color: #999;">{{ stats.discharged || 0 }}</text>
                    <text class="status-label">已出院</text>
                </view>
            </view>
        </view>

        <view class="card">
            <view class="card-header">
                <text class="card-title">费用概览</text>
                <text class="link-text" @click="goToFee">查看明细</text>
            </view>
            
            <view class="fee-summary">
                <view class="fee-item">
                    <text class="fee-label">总费用</text>
                    <text class="fee-value">¥{{ util.formatMoney(feeSummary.totalFee) }}</text>
                </view>
                <view class="fee-item">
                    <text class="fee-label">已缴押金</text>
                    <text class="fee-value text-success">¥{{ util.formatMoney(feeSummary.totalPaid) }}</text>
                </view>
                <view class="fee-item">
                    <text class="fee-label">余额</text>
                    <text class="fee-value" :class="balanceClass">
                        {{ feeSummary.balance >= 0 ? '+' : '' }}¥{{ util.formatMoney(feeSummary.balance) }}
                    </text>
                </view>
            </view>
        </view>

        <view class="card" v-if="recentAdmissions && recentAdmissions.length">
            <view class="card-header">
                <text class="card-title">最近申请</text>
            </view>
            
            <view 
                class="admission-item" 
                v-for="item in recentAdmissions" 
                :key="item.id"
                @click="viewDetail(item.id)"
            >
                <view class="admission-info">
                    <text class="admission-dept">{{ item.deptName }}</text>
                    <text class="admission-date">{{ formatDate(item.admissionDate) }}</text>
                </view>
                <view 
                    class="status-tag"
                    :class="getStatusClass(item.status)"
                >
                    {{ getStatusText(item.status) }}
                </view>
            </view>
        </view>

        <view class="tips-card card">
            <view class="tips-title">📢 温馨提示</view>
            <view class="tips-content">
                <text>• 入院申请提交后，请等待医生审核</text>
                <text>• 费用清单每日更新，请及时关注</text>
                <text>• 押金不足时请及时补缴</text>
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
            util,
            userInfo: null,
            elderMode: false,
            stats: {},
            feeSummary: {
                totalFee: 0,
                totalPaid: 0,
                balance: 0
            },
            recentAdmissions: []
        }
    },
    computed: {
        balanceClass() {
            return this.feeSummary.balance >= 0 ? 'text-success' : 'text-danger'
        }
    },
    onLoad() {
        this.loadData()
    },
    onShow() {
        this.elderMode = uni.getStorageSync('elderMode') === 1
        this.userInfo = uni.getStorageSync('userInfo')
        this.loadData()
    },
    onPullDownRefresh() {
        this.loadData().then(() => {
            uni.stopPullDownRefresh()
        })
    },
    methods: {
        async loadData() {
            try {
                const token = uni.getStorageSync('token')
                if (!token) {
                    uni.reLaunch({ url: '/pages/login/login' })
                    return
                }
                
                const [statsRes, feeRes, admissionRes] = await Promise.all([
                    api.admission.stats().catch(() => ({ code: 200, data: {} })),
                    api.fee.summary().catch(() => ({ code: 200, data: { totalFee: 0, totalPaid: 0, balance: 0 } })),
                    api.admission.list({ pageNum: 1, pageSize: 3 }).catch(() => ({ code: 200, data: { records: [] } }))
                ])
                
                if (statsRes.code === 200) {
                    this.stats = statsRes.data
                }
                if (feeRes.code === 200) {
                    this.feeSummary = feeRes.data
                }
                if (admissionRes.code === 200) {
                    this.recentAdmissions = admissionRes.data.records || []
                }
            } catch (e) {
                console.error('加载数据失败:', e)
            }
        },
        
        formatDate(date) {
            return util.formatDate(date, 'MM-dd')
        },
        
        getStatusText(status) {
            return util.getAdmissionStatusText(status)
        },
        
        getStatusClass(status) {
            const map = {
                0: 'status-pending',
                1: 'status-approved',
                2: 'status-admitted',
                3: 'status-discharged',
                4: 'status-cancelled'
            }
            return map[status] || 'status-pending'
        },
        
        goToApply() {
            uni.navigateTo({ url: '/pages/admission/apply' })
        },
        
        goToFee() {
            uni.switchTab({ url: '/pages/fee/list' })
        },
        
        goToDeposit() {
            uni.navigateTo({ url: '/pages/deposit/pay' })
        },
        
        goToAdmission() {
            uni.switchTab({ url: '/pages/admission/list' })
        },
        
        viewDetail(id) {
            uni.navigateTo({ url: '/pages/admission/detail?id=' + id })
        },
        
        toggleElderMode() {
            this.elderMode = !this.elderMode
            uni.setStorageSync('elderMode', this.elderMode ? 1 : 0)
            util.showToast(this.elderMode ? '已开启长辈模式' : '已关闭长辈模式')
        }
    }
}
</script>

<style scoped>
.index-container {
    padding-bottom: 40rpx;
}

.elder-mode {
    font-size: 36rpx;
}

.header {
    background: linear-gradient(135deg, #1890FF, #096DD9);
    padding: 40rpx 30rpx 60rpx;
    color: #fff;
}

.header-top {
    display: flex;
    justify-content: space-between;
    align-items: flex-start;
}

.user-info {
    display: flex;
    flex-direction: column;
}

.greeting {
    font-size: 28rpx;
    opacity: 0.9;
}

.username {
    font-size: 40rpx;
    font-weight: bold;
    margin-top: 8rpx;
}

.elder-mode .greeting {
    font-size: 32rpx;
}

.elder-mode .username {
    font-size: 48rpx;
}

.elder-btn {
    width: 80rpx;
    height: 80rpx;
    background: rgba(255, 255, 255, 0.2);
    border-radius: 50%;
    display: flex;
    align-items: center;
    justify-content: center;
    font-size: 36rpx;
}

.card {
    margin: -20rpx 20rpx 20rpx;
    padding: 30rpx;
    background: #fff;
    border-radius: 20rpx;
    box-shadow: 0 4rpx 20rpx rgba(0, 0, 0, 0.05);
    position: relative;
}

.card:first-of-type {
    margin-top: -40rpx;
}

.card-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 20rpx;
}

.card-title {
    font-size: 32rpx;
    font-weight: 600;
    color: #333;
}

.elder-mode .card-title {
    font-size: 38rpx;
}

.link-text {
    font-size: 26rpx;
    color: #1890FF;
}

.quick-actions {
    padding: 40rpx 30rpx;
}

.action-grid {
    display: flex;
    flex-wrap: wrap;
}

.action-item {
    width: 25%;
    display: flex;
    flex-direction: column;
    align-items: center;
}

.action-icon {
    width: 100rpx;
    height: 100rpx;
    border-radius: 24rpx;
    display: flex;
    align-items: center;
    justify-content: center;
    font-size: 44rpx;
    margin-bottom: 12rpx;
}

.elder-mode .action-icon {
    width: 120rpx;
    height: 120rpx;
    font-size: 52rpx;
}

.bg-blue { background: #E6F7FF; }
.bg-green { background: #F6FFED; }
.bg-orange { background: #FFF7E6; }
.bg-purple { background: #F9F0FF; }

.action-text {
    font-size: 26rpx;
    color: #666;
}

.elder-mode .action-text {
    font-size: 30rpx;
}

.status-summary {
    display: flex;
    justify-content: space-around;
    align-items: center;
}

.status-item {
    display: flex;
    flex-direction: column;
    align-items: center;
}

.status-count {
    font-size: 48rpx;
    font-weight: bold;
}

.elder-mode .status-count {
    font-size: 56rpx;
}

.status-label {
    font-size: 24rpx;
    color: #999;
    margin-top: 8rpx;
}

.elder-mode .status-label {
    font-size: 28rpx;
}

.status-divider {
    width: 1rpx;
    height: 60rpx;
    background: #EEE;
}

.fee-summary {
    display: flex;
    justify-content: space-between;
}

.fee-item {
    flex: 1;
    text-align: center;
}

.fee-label {
    display: block;
    font-size: 24rpx;
    color: #999;
    margin-bottom: 8rpx;
}

.elder-mode .fee-label {
    font-size: 28rpx;
}

.fee-value {
    font-size: 36rpx;
    font-weight: bold;
    color: #333;
}

.elder-mode .fee-value {
    font-size: 42rpx;
}

.admission-item {
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 24rpx 0;
    border-bottom: 1rpx solid #F5F5F5;
}

.admission-item:last-child {
    border-bottom: none;
}

.admission-info {
    display: flex;
    flex-direction: column;
}

.admission-dept {
    font-size: 30rpx;
    color: #333;
    font-weight: 500;
}

.elder-mode .admission-dept {
    font-size: 34rpx;
}

.admission-date {
    font-size: 24rpx;
    color: #999;
    margin-top: 8rpx;
}

.elder-mode .admission-date {
    font-size: 28rpx;
}

.tips-card {
    background: #FFFBE6;
    border: 1rpx solid #FFE58F;
}

.tips-title {
    font-size: 28rpx;
    font-weight: 600;
    color: #D48806;
    margin-bottom: 16rpx;
}

.elder-mode .tips-title {
    font-size: 32rpx;
}

.tips-content {
    display: flex;
    flex-direction: column;
    gap: 12rpx;
}

.tips-content text {
    font-size: 26rpx;
    color: #8C671F;
    line-height: 1.6;
}

.elder-mode .tips-content text {
    font-size: 30rpx;
}
</style>
