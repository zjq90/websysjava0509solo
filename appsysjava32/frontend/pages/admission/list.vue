<template>
    <view class="admission-list" :class="{ 'elder-mode': elderMode }">
        <view class="status-tabs">
            <view 
                class="status-tab"
                :class="{ active: currentTab === 'all' }"
                @click="switchTab('all')"
            >全部</view>
            <view 
                class="status-tab"
                :class="{ active: currentTab === '0' }"
                @click="switchTab('0')"
            >
                待审核
                <text class="tab-badge" v-if="stats.pending > 0">{{ stats.pending }}</text>
            </view>
            <view 
                class="status-tab"
                :class="{ active: currentTab === '1' }"
                @click="switchTab('1')"
            >
                已通过
                <text class="tab-badge" v-if="stats.approved > 0">{{ stats.approved }}</text>
            </view>
            <view 
                class="status-tab"
                :class="{ active: currentTab === '2' }"
                @click="switchTab('2')"
            >
                住院中
                <text class="tab-badge" v-if="stats.admitted > 0">{{ stats.admitted }}</text>
            </view>
        </view>

        <view class="list-container">
            <view 
                class="admission-card"
                v-for="item in filteredList"
                :key="item.id"
                @click="viewDetail(item.id)"
            >
                <view class="card-header">
                    <view class="dept-info">
                        <text class="dept-name">{{ item.deptName }}</text>
                        <view 
                            class="status-tag"
                            :class="getStatusClass(item.status)"
                        >
                            {{ getStatusText(item.status) }}
                        </view>
                    </view>
                    <text class="apply-no">{{ item.applicationNo }}</text>
                </view>
                
                <view class="card-body">
                    <view class="info-row">
                        <text class="info-label">入院日期</text>
                        <text class="info-value">{{ formatDate(item.admissionDate) }}</text>
                    </view>
                    <view class="info-row" v-if="item.actualBedNo">
                        <text class="info-label">床位号</text>
                        <text class="info-value">{{ item.actualBedNo }}</text>
                    </view>
                    <view class="info-row" v-if="item.doctorName">
                        <text class="info-label">主治医生</text>
                        <text class="info-value">{{ item.doctorName }}</text>
                    </view>
                    <view class="info-row" v-if="item.diagnosis">
                        <text class="info-label">诊断</text>
                        <text class="info-value ellipsis-2">{{ item.diagnosis }}</text>
                    </view>
                </view>
                
                <view class="card-footer" v-if="item.status === 0">
                    <button class="btn btn-danger btn-small" @click.stop="cancelAdmission(item)">取消申请</button>
                </view>
                
                <view class="card-footer" v-else-if="item.status === 1 || item.status === 2">
                    <button class="btn btn-primary btn-small" @click.stop="goToDeposit(item)">补缴押金</button>
                    <button class="btn btn-default btn-small" @click.stop="viewDetail(item.id)">查看详情</button>
                </view>
            </view>

            <view class="empty-state" v-if="!filteredList.length && !loading">
                <text class="empty-icon">📭</text>
                <text class="empty-text">暂无入院申请</text>
                <button class="btn btn-primary btn-small mt-20" @click="goToApply">立即申请</button>
            </view>

            <view class="loading-state" v-if="loading">
                <text>加载中...</text>
            </view>
        </view>

        <view class="fab-button" @click="goToApply">
            <text class="fab-icon">+</text>
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
            currentTab: 'all',
            list: [],
            stats: {},
            loading: false
        }
    },
    computed: {
        filteredList() {
            if (this.currentTab === 'all') {
                return this.list
            }
            return this.list.filter(item => String(item.status) === this.currentTab)
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
                const [listRes, statsRes] = await Promise.all([
                    api.admission.list({ pageNum: 1, pageSize: 50 }),
                    api.admission.stats()
                ])
                
                if (listRes.code === 200) {
                    this.list = listRes.data.records || []
                }
                if (statsRes.code === 200) {
                    this.stats = statsRes.data
                }
            } catch (e) {
                console.error('加载失败:', e)
            } finally {
                this.loading = false
            }
        },
        
        switchTab(tab) {
            this.currentTab = tab
        },
        
        formatDate(date) {
            return util.formatDate(date, 'yyyy-MM-dd')
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
        
        viewDetail(id) {
            uni.navigateTo({ url: '/pages/admission/detail?id=' + id })
        },
        
        goToApply() {
            uni.navigateTo({ url: '/pages/admission/apply' })
        },
        
        async cancelAdmission(item) {
            const confirmed = await util.confirm('确定要取消该入院申请吗？')
            if (!confirmed) return
            
            try {
                util.showLoading('取消中...')
                const res = await api.admission.cancel(item.id)
                util.hideLoading()
                if (res.code === 200) {
                    util.showToast('取消成功', 'success')
                    this.loadData()
                }
            } catch (e) {
                util.hideLoading()
            }
        },
        
        goToDeposit(item) {
            uni.navigateTo({ url: '/pages/deposit/pay?admissionId=' + item.id })
        }
    }
}
</script>

<style scoped>
.admission-list {
    min-height: 100vh;
    background: #F5F5F5;
    padding-bottom: 120rpx;
}

.elder-mode {
    font-size: 36rpx;
}

.status-tabs {
    display: flex;
    background: #fff;
    padding: 0 20rpx;
    position: sticky;
    top: 0;
    z-index: 10;
}

.status-tab {
    flex: 1;
    padding: 28rpx 0;
    text-align: center;
    font-size: 28rpx;
    color: #666;
    position: relative;
}

.elder-mode .status-tab {
    font-size: 32rpx;
    padding: 32rpx 0;
}

.status-tab.active {
    color: #1890FF;
    font-weight: 600;
}

.status-tab.active::after {
    content: '';
    position: absolute;
    bottom: 0;
    left: 50%;
    transform: translateX(-50%);
    width: 60rpx;
    height: 6rpx;
    background: #1890FF;
    border-radius: 3rpx;
}

.tab-badge {
    position: absolute;
    top: 12rpx;
    right: 20rpx;
    min-width: 32rpx;
    height: 32rpx;
    padding: 0 8rpx;
    background: #FF4D4F;
    color: #fff;
    font-size: 20rpx;
    border-radius: 16rpx;
    line-height: 32rpx;
}

.list-container {
    padding: 20rpx;
}

.admission-card {
    background: #fff;
    border-radius: 20rpx;
    padding: 28rpx;
    margin-bottom: 20rpx;
    box-shadow: 0 4rpx 16rpx rgba(0, 0, 0, 0.05);
}

.card-header {
    display: flex;
    justify-content: space-between;
    align-items: flex-start;
    margin-bottom: 20rpx;
}

.dept-info {
    display: flex;
    align-items: center;
    gap: 16rpx;
}

.dept-name {
    font-size: 34rpx;
    font-weight: 600;
    color: #333;
}

.elder-mode .dept-name {
    font-size: 38rpx;
}

.apply-no {
    font-size: 24rpx;
    color: #999;
}

.elder-mode .apply-no {
    font-size: 28rpx;
}

.card-body {
    padding: 20rpx 0;
    border-top: 1rpx solid #F5F5F5;
    border-bottom: 1rpx solid #F5F5F5;
}

.info-row {
    display: flex;
    padding: 12rpx 0;
}

.info-label {
    width: 160rpx;
    font-size: 28rpx;
    color: #999;
    flex-shrink: 0;
}

.elder-mode .info-label {
    width: 200rpx;
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

.card-footer {
    display: flex;
    justify-content: flex-end;
    gap: 20rpx;
    padding-top: 20rpx;
}

.empty-state {
    display: flex;
    flex-direction: column;
    align-items: center;
    padding: 120rpx 0;
}

.empty-icon {
    font-size: 120rpx;
    margin-bottom: 20rpx;
    opacity: 0.5;
}

.empty-text {
    font-size: 28rpx;
    color: #999;
}

.loading-state {
    text-align: center;
    padding: 60rpx 0;
    color: #999;
}

.fab-button {
    position: fixed;
    right: 40rpx;
    bottom: 160rpx;
    width: 110rpx;
    height: 110rpx;
    background: linear-gradient(135deg, #1890FF, #096DD9);
    border-radius: 50%;
    display: flex;
    align-items: center;
    justify-content: center;
    box-shadow: 0 8rpx 30rpx rgba(24, 144, 255, 0.4);
}

.elder-mode .fab-button {
    width: 130rpx;
    height: 130rpx;
}

.fab-icon {
    font-size: 60rpx;
    color: #fff;
    font-weight: 300;
}

.elder-mode .fab-icon {
    font-size: 70rpx;
}
</style>
