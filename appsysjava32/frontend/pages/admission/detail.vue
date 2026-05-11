<template>
    <view class="admission-detail" :class="{ 'elder-mode': elderMode }">
        <view class="loading-state" v-if="loading">
            <text>加载中...</text>
        </view>

        <view class="detail-container" v-else-if="detail">
            <view class="info-card">
                <view class="card-header">
                    <text class="card-title">申请信息</text>
                    <view class="status-tag" :class="getStatusClass(detail.status)">
                        {{ getStatusText(detail.status) }}
                    </view>
                </view>
                <view class="card-body">
                    <view class="info-row">
                        <text class="info-label">申请单号</text>
                        <text class="info-value">{{ detail.applicationNo }}</text>
                    </view>
                    <view class="info-row">
                        <text class="info-label">申请科室</text>
                        <text class="info-value">{{ detail.deptName }}</text>
                    </view>
                    <view class="info-row">
                        <text class="info-label">预约入院日期</text>
                        <text class="info-value">{{ formatDate(detail.admissionDate) }}</text>
                    </view>
                    <view class="info-row" v-if="detail.actualBedNo">
                        <text class="info-label">床位号</text>
                        <text class="info-value">{{ detail.actualBedNo }}</text>
                    </view>
                    <view class="info-row" v-if="detail.doctorName">
                        <text class="info-label">主治医生</text>
                        <text class="info-value">{{ detail.doctorName }}</text>
                    </view>
                    <view class="info-row" v-if="detail.diagnosis">
                        <text class="info-label">初步诊断</text>
                        <text class="info-value">{{ detail.diagnosis }}</text>
                    </view>
                    <view class="info-row">
                        <text class="info-label">申请时间</text>
                        <text class="info-value">{{ formatDate(detail.createTime, 'yyyy-MM-dd HH:mm') }}</text>
                    </view>
                </view>
            </view>

            <view class="info-card" v-if="detail.reason">
                <view class="card-header">
                    <text class="card-title">入院原因</text>
                </view>
                <view class="card-body">
                    <text class="reason-text">{{ detail.reason }}</text>
                </view>
            </view>

            <view class="info-card" v-if="detail.remark">
                <view class="card-header">
                    <text class="card-title">备注</text>
                </view>
                <view class="card-body">
                    <text class="reason-text">{{ detail.remark }}</text>
                </view>
            </view>

            <view class="info-card" v-if="detail.auditRemark">
                <view class="card-header">
                    <text class="card-title">审核意见</text>
                </view>
                <view class="card-body">
                    <text class="reason-text">{{ detail.auditRemark }}</text>
                </view>
            </view>

            <view class="timeline" v-if="timeline.length">
                <view class="card-header">
                    <text class="card-title">进度记录</text>
                </view>
                <view class="timeline-list">
                    <view class="timeline-item" v-for="(item, index) in timeline" :key="index">
                        <view class="timeline-dot"></view>
                        <view class="timeline-content">
                            <text class="timeline-title">{{ item.title }}</text>
                            <text class="timeline-time">{{ item.time }}</text>
                            <text class="timeline-remark" v-if="item.remark">{{ item.remark }}</text>
                        </view>
                    </view>
                </view>
            </view>
        </view>

        <view class="empty-state" v-else>
            <text class="empty-icon">❓</text>
            <text class="empty-text">未找到申请记录</text>
        </view>

        <view class="bottom-actions" v-if="detail">
            <button class="btn btn-primary btn-block" v-if="detail.status === 1 || detail.status === 2" @click="goToDeposit">
                补缴押金
            </button>
            <button class="btn btn-danger btn-block" v-if="detail.status === 0" @click="cancelAdmission">
                取消申请
            </button>
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
            loading: false,
            timeline: []
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
                const res = await api.admission.detail(this.id)
                if (res.code === 200) {
                    this.detail = res.data
                    this.buildTimeline()
                }
            } catch (e) {
                console.error('加载失败:', e)
            } finally {
                this.loading = false
            }
        },

        buildTimeline() {
            const list = []
            const d = this.detail
            
            if (d.createTime) {
                list.push({
                    title: '提交入院申请',
                    time: this.formatDate(d.createTime, 'yyyy-MM-dd HH:mm'),
                    remark: d.reason || '等待审核'
                })
            }
            
            if (d.auditTime) {
                list.push({
                    title: d.status === 4 ? '审核拒绝' : '审核通过',
                    time: this.formatDate(d.auditTime, 'yyyy-MM-dd HH:mm'),
                    remark: d.auditRemark || (d.status === 4 ? '申请未通过' : '申请已通过，准备入院')
                })
            }
            
            if (d.actualAdmissionDate) {
                list.push({
                    title: '办理入院',
                    time: this.formatDate(d.actualAdmissionDate, 'yyyy-MM-dd HH:mm'),
                    remark: `床位：${d.actualBedNo || '已安排'}`
                })
            }
            
            if (d.dischargeDate) {
                list.push({
                    title: '办理出院',
                    time: this.formatDate(d.dischargeDate, 'yyyy-MM-dd HH:mm'),
                    remark: '祝您早日康复'
                })
            }
            
            if (d.cancelTime) {
                list.push({
                    title: '已取消',
                    time: this.formatDate(d.cancelTime, 'yyyy-MM-dd HH:mm'),
                    remark: '申请已取消'
                })
            }
            
            this.timeline = list.reverse()
        },

        formatDate(date, fmt) {
            return util.formatDate(date, fmt)
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

        async cancelAdmission() {
            const confirmed = await util.confirm('确定要取消该入院申请吗？')
            if (!confirmed) return
            
            try {
                util.showLoading('取消中...')
                const res = await api.admission.cancel(this.id)
                util.hideLoading()
                if (res.code === 200) {
                    util.showToast('取消成功', 'success')
                    this.loadData()
                }
            } catch (e) {
                util.hideLoading()
            }
        },

        goToDeposit() {
            uni.navigateTo({ url: '/pages/deposit/pay?admissionId=' + this.id })
        }
    }
}
</script>

<style scoped>
.admission-detail {
    min-height: 100vh;
    background: #F5F5F5;
    padding: 20rpx;
    padding-bottom: 180rpx;
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

.detail-container {
    padding-bottom: 20rpx;
}

.info-card {
    background: #fff;
    border-radius: 20rpx;
    margin-bottom: 20rpx;
    overflow: hidden;
}

.card-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
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
    word-break: break-all;
}

.elder-mode .info-value {
    font-size: 32rpx;
}

.reason-text {
    font-size: 28rpx;
    color: #333;
    line-height: 1.8;
}

.elder-mode .reason-text {
    font-size: 32rpx;
}

.timeline-list {
    padding: 0 28rpx 28rpx;
}

.timeline-item {
    display: flex;
    padding-left: 40rpx;
    position: relative;
}

.timeline-item:last-child::before {
    display: none;
}

.timeline-item::before {
    content: '';
    position: absolute;
    left: 18rpx;
    top: 36rpx;
    bottom: 0;
    width: 2rpx;
    background: #E8E8E8;
}

.timeline-dot {
    position: absolute;
    left: 0;
    top: 12rpx;
    width: 36rpx;
    height: 36rpx;
    background: #1890FF;
    border-radius: 50%;
    border: 6rpx solid #E6F7FF;
    box-sizing: border-box;
}

.timeline-content {
    flex: 1;
    padding-bottom: 32rpx;
}

.timeline-title {
    font-size: 30rpx;
    color: #333;
    font-weight: 600;
    display: block;
}

.elder-mode .timeline-title {
    font-size: 34rpx;
}

.timeline-time {
    font-size: 26rpx;
    color: #999;
    margin-top: 8rpx;
    display: block;
}

.elder-mode .timeline-time {
    font-size: 30rpx;
}

.timeline-remark {
    font-size: 28rpx;
    color: #666;
    margin-top: 8rpx;
    display: block;
}

.elder-mode .timeline-remark {
    font-size: 32rpx;
}

.bottom-actions {
    position: fixed;
    bottom: 0;
    left: 0;
    right: 0;
    padding: 20rpx;
    background: #fff;
    box-shadow: 0 -4rpx 20rpx rgba(0, 0, 0, 0.08);
    padding-bottom: calc(20rpx + constant(safe-area-inset-bottom));
    padding-bottom: calc(20rpx + env(safe-area-inset-bottom));
}

.elder-mode .bottom-actions {
    padding: 28rpx;
}
</style>
