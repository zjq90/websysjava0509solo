<template>
    <view class="appointment-page" :class="{ 'elderly-mode': elderlyMode }">
        <view class="content" v-if="!isConfirmMode">
            <view class="tab-bar">
                <view 
                    class="tab-item" 
                    :class="{ active: activeTab === tab.value }"
                    v-for="tab in tabs"
                    :key="tab.value"
                    @click="switchTab(tab.value)"
                >
                    <text class="tab-text">{{ tab.label }}</text>
                </view>
            </view>

            <view class="appointment-list">
                <view 
                    class="appointment-item card" 
                    v-for="appointment in appointmentList" 
                    :key="appointment.id"
                    @click="goToDetail(appointment.id)"
                >
                    <view class="appointment-header">
                        <view class="doctor-info">
                            <text class="doctor-avatar">👨‍⚕️</text>
                            <view class="doctor-detail">
                                <text class="doctor-name">{{ appointment.doctorName }}</text>
                                <text class="doctor-title">{{ appointment.doctorTitle }}</text>
                            </view>
                        </view>
                        <text class="status-tag" :class="getStatusClass(appointment.status)">
                            {{ getStatusText(appointment.status) }}
                        </text>
                    </view>

                    <view class="appointment-info">
                        <view class="info-row">
                            <text class="info-label">就诊人：</text>
                            <text class="info-value">{{ appointment.patientName }}</text>
                        </view>
                        <view class="info-row">
                            <text class="info-label">科室：</text>
                            <text class="info-value">{{ appointment.departmentName }}</text>
                        </view>
                        <view class="info-row">
                            <text class="info-label">时间：</text>
                            <text class="info-value">{{ appointment.appointmentDate }} {{ appointment.timeSlotText }}</text>
                        </view>
                        <view class="info-row">
                            <text class="info-label">状态：</text>
                            <text class="info-value" :class="'status-' + appointment.status">
                                {{ getStatusText(appointment.status) }}
                            </text>
                        </view>
                    </view>

                    <view class="appointment-actions" v-if="appointment.status === 'PENDING'">
                        <view class="action-btn cancel" @click.stop="cancelAppointment(appointment.id)">
                            取消预约
                        </view>
                        <view class="action-btn pay" @click.stop="payAppointment(appointment.id)">
                            立即支付
                        </view>
                    </view>
                    <view class="appointment-actions" v-if="appointment.status === 'PAID'">
                        <view class="action-btn primary" @click.stop="viewDetail(appointment.id)">
                            查看详情
                        </view>
                    </view>
                </view>

                <view class="empty-state" v-if="appointmentList.length === 0 && !loading">
                    <text class="empty-icon">📋</text>
                    <text class="empty-text">暂无预约记录</text>
                </view>
            </view>
        </view>

        <view class="confirm-content" v-else>
            <view class="confirm-card card">
                <view class="confirm-title">确认预约信息</view>
                
                <view class="confirm-section">
                    <view class="confirm-row">
                        <text class="confirm-label">就诊人</text>
                        <text class="confirm-value">{{ params.patientName }}</text>
                    </view>
                    <view class="confirm-row">
                        <text class="confirm-label">医生</text>
                        <text class="confirm-value">{{ params.doctorName }}</text>
                    </view>
                    <view class="confirm-row">
                        <text class="confirm-label">科室</text>
                        <text class="confirm-value">{{ params.departmentName }}</text>
                    </view>
                    <view class="confirm-row">
                        <text class="confirm-label">预约时间</text>
                        <text class="confirm-value">{{ params.scheduleDate }} {{ params.timeSlot }}</text>
                    </view>
                </view>

                <view class="confirm-fee">
                    <text class="fee-label">挂号费</text>
                    <text class="fee-amount">¥{{ params.fee }}</text>
                </view>
            </view>

            <view class="tips card">
                <text class="tips-title">温馨提示</text>
                <text class="tips-item">• 请提前15分钟到达医院取号</text>
                <text class="tips-item">• 携带有效身份证件</text>
                <text class="tips-item">• 如需取消请提前24小时操作</text>
            </view>

            <view class="bottom-bar">
                <view class="total">
                    <text class="total-label">合计：</text>
                    <text class="total-amount">¥{{ params.fee }}</text>
                </view>
                <view class="pay-btn" @click="confirmAndPay">
                    确认并支付
                </view>
            </view>
        </view>
    </view>
</template>

<script>
import { getAppointmentList, cancelAppointment, createAppointment, payAppointment } from '@/api/appointment'

export default {
    data() {
        return {
            tabs: [
                { label: '全部', value: 'ALL' },
                { label: '待支付', value: 'PENDING' },
                { label: '已预约', value: 'PAID' },
                { label: '已完成', value: 'COMPLETED' }
            ],
            activeTab: 'ALL',
            appointmentList: [],
            loading: false,
            isConfirmMode: false,
            params: {},
            elderlyMode: false
        }
    },
    onLoad(options) {
        if (options.mode === 'confirm') {
            this.isConfirmMode = true
            this.params = JSON.parse(decodeURIComponent(options.params))
            uni.setNavigationBarTitle({ title: '确认预约' })
        }
        this.elderlyMode = uni.getStorageSync('elderlyMode') || false
    },
    onShow() {
        if (!this.isConfirmMode) {
            this.loadAppointments()
        }
    },
    methods: {
        async loadAppointments() {
            this.loading = true
            try {
                const status = this.activeTab === 'ALL' ? '' : this.activeTab
                const res = await getAppointmentList(status)
                this.appointmentList = res.data.data || []
            } catch (e) {
                console.error(e)
            } finally {
                this.loading = false
            }
        },
        switchTab(tab) {
            this.activeTab = tab
            this.loadAppointments()
        },
        getStatusText(status) {
            const map = {
                'PENDING': '待支付',
                'PAID': '已预约',
                'CANCELLED': '已取消',
                'COMPLETED': '已完成'
            }
            return map[status] || status
        },
        getStatusClass(status) {
            return 'status-' + status.toLowerCase()
        },
        goToDetail(id) {
            uni.navigateTo({ url: '/pages/appointment-detail/appointment-detail?id=' + id })
        },
        viewDetail(id) {
            uni.navigateTo({ url: '/pages/appointment-detail/appointment-detail?id=' + id })
        },
        async cancelAppointment(id) {
            uni.showModal({
                title: '取消预约',
                content: '确定要取消该预约吗？',
                success: async (res) => {
                    if (res.confirm) {
                        try {
                            await cancelAppointment(id)
                            uni.showToast({ title: '取消成功', icon: 'success' })
                            this.loadAppointments()
                        } catch (e) {
                            console.error(e)
                        }
                    }
                }
            })
        },
        async payAppointment(id) {
            uni.showModal({
                title: '支付',
                content: '确认支付挂号费？',
                success: async (res) => {
                    if (res.confirm) {
                        try {
                            await payAppointment(id)
                            uni.showToast({ title: '支付成功', icon: 'success' })
                            this.loadAppointments()
                        } catch (e) {
                            uni.showToast({ title: '支付失败，请重试', icon: 'none' })
                        }
                    }
                }
            })
        },
        async confirmAndPay() {
            uni.showModal({
                title: '确认预约',
                content: '确认创建预约并支付挂号费？',
                success: async (res) => {
                    if (res.confirm) {
                        try {
                            const createRes = await createAppointment({
                                doctorId: this.params.doctorId,
                                patientId: this.params.patientId,
                                scheduleId: this.params.scheduleId
                            })
                            
                            if (createRes.data && createRes.data.data) {
                                const appointmentId = createRes.data.data.id
                                await payAppointment(appointmentId)
                                uni.showToast({ title: '预约成功', icon: 'success' })
                                
                                setTimeout(() => {
                                    uni.reLaunch({ url: '/pages/appointment/appointment' })
                                }, 1500)
                            }
                        } catch (e) {
                            uni.showToast({ title: '操作失败，请重试', icon: 'none' })
                        }
                    }
                }
            })
        }
    }
}
</script>

<style scoped>
.appointment-page {
    min-height: 100vh;
    background-color: #f5f7fa;
}

.content {
    padding-bottom: 120rpx;
}

.tab-bar {
    display: flex;
    background: #ffffff;
    padding: 20rpx 30rpx;
    gap: 20rpx;
    overflow-x: auto;
    white-space: nowrap;
}

.tab-item {
    padding: 15rpx 30rpx;
    background: #f5f7fa;
    border-radius: 40rpx;
    font-size: 26rpx;
}

.tab-item.active {
    background: #1677ff;
    color: #ffffff;
}

.appointment-list {
    padding: 20rpx 30rpx;
}

.appointment-item {
    margin-bottom: 20rpx;
    padding: 25rpx;
}

.appointment-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding-bottom: 20rpx;
    border-bottom: 1rpx solid #f0f0f0;
}

.doctor-info {
    display: flex;
    align-items: center;
}

.doctor-avatar {
    font-size: 50rpx;
    margin-right: 15rpx;
}

.doctor-detail {
    display: flex;
    flex-direction: column;
}

.doctor-name {
    font-size: 30rpx;
    font-weight: 500;
}

.doctor-title {
    font-size: 24rpx;
    color: #1677ff;
}

.status-tag {
    font-size: 24rpx;
    padding: 6rpx 16rpx;
    border-radius: 4rpx;
}

.status-pending {
    color: #faad14;
    background: #fffbe6;
}

.status-paid {
    color: #52c41a;
    background: #f6ffed;
}

.status-cancelled {
    color: #999999;
    background: #f5f5f5;
}

.status-completed {
    color: #1677ff;
    background: #e6f4ff;
}

.appointment-info {
    padding: 20rpx 0;
}

.info-row {
    display: flex;
    margin-bottom: 10rpx;
}

.info-label {
    font-size: 26rpx;
    color: #999999;
    width: 120rpx;
}

.info-value {
    font-size: 26rpx;
    color: #333333;
    flex: 1;
}

.appointment-actions {
    display: flex;
    justify-content: flex-end;
    gap: 20rpx;
    padding-top: 15rpx;
    border-top: 1rpx solid #f0f0f0;
}

.action-btn {
    padding: 15rpx 40rpx;
    border-radius: 40rpx;
    font-size: 26rpx;
}

.action-btn.cancel {
    color: #666666;
    background: #f5f5f5;
}

.action-btn.pay {
    color: #ffffff;
    background: #ff4d4f;
}

.action-btn.primary {
    color: #ffffff;
    background: #1677ff;
}

.empty-state {
    display: flex;
    flex-direction: column;
    align-items: center;
    padding: 100rpx 0;
}

.empty-icon {
    font-size: 80rpx;
    margin-bottom: 20rpx;
}

.empty-text {
    font-size: 28rpx;
    color: #999999;
}

.confirm-content {
    padding: 30rpx;
    padding-bottom: 150rpx;
}

.confirm-card {
    padding: 30rpx;
}

.confirm-title {
    font-size: 32rpx;
    font-weight: bold;
    color: #333333;
    margin-bottom: 25rpx;
}

.confirm-section {
    margin-bottom: 25rpx;
}

.confirm-row {
    display: flex;
    justify-content: space-between;
    margin-bottom: 20rpx;
}

.confirm-label {
    font-size: 28rpx;
    color: #666666;
}

.confirm-value {
    font-size: 28rpx;
    color: #333333;
}

.confirm-fee {
    display: flex;
    justify-content: space-between;
    padding-top: 25rpx;
    border-top: 1rpx solid #f0f0f0;
}

.fee-label {
    font-size: 28rpx;
    color: #666666;
}

.fee-amount {
    font-size: 36rpx;
    color: #ff4d4f;
    font-weight: bold;
}

.tips {
    margin-top: 20rpx;
    padding: 25rpx;
}

.tips-title {
    font-size: 28rpx;
    font-weight: bold;
    color: #333333;
    display: block;
    margin-bottom: 15rpx;
}

.tips-item {
    font-size: 26rpx;
    color: #666666;
    display: block;
    line-height: 1.8;
}

.bottom-bar {
    position: fixed;
    bottom: 0;
    left: 0;
    right: 0;
    background: #ffffff;
    padding: 20rpx 30rpx;
    display: flex;
    align-items: center;
    box-shadow: 0 -2rpx 12rpx rgba(0, 0, 0, 0.05);
}

.total {
    flex: 1;
}

.total-label {
    font-size: 28rpx;
    color: #666666;
}

.total-amount {
    font-size: 40rpx;
    color: #ff4d4f;
    font-weight: bold;
}

.pay-btn {
    background: #ff4d4f;
    color: #ffffff;
    padding: 24rpx 60rpx;
    border-radius: 48rpx;
    font-size: 32rpx;
}

.elderly-mode .doctor-name {
    font-size: 34rpx;
}

.elderly-mode .info-label,
.elderly-mode .info-value {
    font-size: 30rpx;
}
</style>
