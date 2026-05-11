<template>
    <view class="appointment-detail-page" :class="{ 'elderly-mode': elderlyMode }">
        <view class="status-section card" v-if="appointment">
            <view class="status-header">
                <text class="status-icon" :class="'status-' + appointment.status.toLowerCase()">
                    {{ getStatusIcon(appointment.status) }}
                </text>
                <view class="status-info">
                    <text class="status-title">{{ getStatusText(appointment.status) }}</text>
                    <text class="status-subtitle" v-if="appointment.status === 'PAID'">请按时就诊</text>
                </view>
            </view>
        </view>

        <view class="info-section card" v-if="appointment">
            <view class="section-title">就诊信息</view>
            
            <view class="info-row">
                <text class="info-label">预约编号</text>
                <text class="info-value">{{ appointment.appointmentNo || '---' }}</text>
            </view>
            <view class="info-row">
                <text class="info-label">就诊人</text>
                <text class="info-value">{{ appointment.patientName }}</text>
            </view>
            <view class="info-row">
                <text class="info-label">科室</text>
                <text class="info-value">{{ appointment.departmentName }}</text>
            </view>
            <view class="info-row">
                <text class="info-label">医生</text>
                <text class="info-value">{{ appointment.doctorName }}</text>
            </view>
            <view class="info-row">
                <text class="info-label">职称</text>
                <text class="info-value">{{ appointment.doctorTitle }}</text>
            </view>
            <view class="info-row">
                <text class="info-label">预约日期</text>
                <text class="info-value">{{ appointment.appointmentDate }}</text>
            </view>
            <view class="info-row">
                <text class="info-label">预约时段</text>
                <text class="info-value">{{ getSlotText(appointment.timeSlot) }}</text>
            </view>
            <view class="info-row">
                <text class="info-label">创建时间</text>
                <text class="info-value">{{ appointment.createdAt }}</text>
            </view>
        </view>

        <view class="fee-section card" v-if="appointment">
            <view class="section-title">费用信息</view>
            <view class="fee-row">
                <text class="fee-label">挂号费</text>
                <text class="fee-value">¥{{ appointment.fee }}</text>
            </view>
            <view class="fee-row total">
                <text class="fee-label">实付金额</text>
                <text class="fee-amount">¥{{ appointment.fee }}</text>
            </view>
        </view>

        <view class="tips card">
            <view class="section-title">温馨提示</view>
            <text class="tips-item">• 请提前15分钟到医院取号</text>
            <text class="tips-item">• 携带有效身份证件</text>
            <text class="tips-item">• 如需取消预约，请提前24小时操作</text>
            <text class="tips-item">• 如有问题，请联系客服：400-888-8888</text>
        </view>

        <view class="bottom-bar" v-if="appointment">
            <view class="action-btn cancel" v-if="appointment.status === 'PENDING'" @click="cancelAppointment">
                取消预约
            </view>
            <view class="action-btn pay" v-if="appointment.status === 'PENDING'" @click="payAppointment">
                立即支付
            </view>
            <view class="action-btn cancel" v-if="appointment.status === 'PAID'" @click="cancelAppointment">
                取消预约
            </view>
            <view class="action-btn primary" v-if="appointment.status === 'PAID'" @click="navigate">
                查看导航
            </view>
            <view class="action-btn review" v-if="appointment.status === 'COMPLETED' && !appointment.reviewed" @click="goToReview">
                写评价
            </view>
        </view>
    </view>
</template>

<script>
import { getAppointmentDetail, cancelAppointment, payAppointment } from '@/api/appointment'

export default {
    data() {
        return {
            appointmentId: null,
            appointment: null,
            elderlyMode: false
        }
    },
    onLoad(options) {
        this.appointmentId = options.id
        this.elderlyMode = uni.getStorageSync('elderlyMode') || false
        this.loadDetail()
    },
    methods: {
        async loadDetail() {
            try {
                const res = await getAppointmentDetail(this.appointmentId)
                this.appointment = res.data.data || {}
            } catch (e) {
                console.error(e)
            }
        },
        getStatusText(status) {
            const map = {
                'PENDING': '待支付',
                'PAID': '预约成功',
                'CANCELLED': '已取消',
                'COMPLETED': '已完成'
            }
            return map[status] || status
        },
        getStatusIcon(status) {
            const map = {
                'PENDING': '⏳',
                'PAID': '✅',
                'CANCELLED': '❌',
                'COMPLETED': '🎉'
            }
            return map[status] || '📋'
        },
        getSlotText(slot) {
            const map = {
                'MORNING': '上午 (08:00-12:00)',
                'AFTERNOON': '下午 (14:00-17:30)',
                'EVENING': '晚上 (18:00-20:00)'
            }
            return map[slot] || slot
        },
        async cancelAppointment() {
            uni.showModal({
                title: '取消预约',
                content: '确定要取消该预约吗？',
                success: async (res) => {
                    if (res.confirm) {
                        try {
                            await cancelAppointment(this.appointmentId)
                            uni.showToast({ title: '取消成功', icon: 'success' })
                            this.loadDetail()
                        } catch (e) {
                            console.error(e)
                        }
                    }
                }
            })
        },
        async payAppointment() {
            uni.showModal({
                title: '支付',
                content: '确认支付挂号费¥' + this.appointment.fee + '？',
                success: async (res) => {
                    if (res.confirm) {
                        try {
                            await payAppointment(this.appointmentId)
                            uni.showToast({ title: '支付成功', icon: 'success' })
                            this.loadDetail()
                        } catch (e) {
                            uni.showToast({ title: '支付失败，请重试', icon: 'none' })
                        }
                    }
                }
            })
        },
        navigate() {
            uni.showToast({ title: '导航功能开发中', icon: 'none' })
        },
        goToReview() {
            uni.showToast({ title: '评价功能开发中', icon: 'none' })
        }
    }
}
</script>

<style scoped>
.appointment-detail-page {
    min-height: 100vh;
    background-color: #f5f7fa;
    padding-bottom: 150rpx;
}

.status-section {
    margin: 20rpx 30rpx;
    padding: 30rpx;
}

.status-header {
    display: flex;
    align-items: center;
}

.status-icon {
    width: 80rpx;
    height: 80rpx;
    border-radius: 50%;
    display: flex;
    align-items: center;
    justify-content: center;
    font-size: 40rpx;
    margin-right: 20rpx;
}

.status-icon.status-pending {
    background: #fffbe6;
}

.status-icon.status-paid {
    background: #f6ffed;
}

.status-icon.status-cancelled {
    background: #f5f5f5;
}

.status-icon.status-completed {
    background: #e6f4ff;
}

.status-info {
    display: flex;
    flex-direction: column;
}

.status-title {
    font-size: 32rpx;
    font-weight: bold;
    color: #333333;
    margin-bottom: 5rpx;
}

.status-subtitle {
    font-size: 26rpx;
    color: #999999;
}

.info-section,
.fee-section,
.tips {
    margin: 20rpx 30rpx;
    padding: 25rpx;
}

.section-title {
    font-size: 28rpx;
    font-weight: bold;
    color: #333333;
    margin-bottom: 20rpx;
    padding-bottom: 15rpx;
    border-bottom: 1rpx solid #f0f0f0;
}

.info-row {
    display: flex;
    justify-content: space-between;
    margin-bottom: 15rpx;
}

.info-label {
    font-size: 28rpx;
    color: #999999;
}

.info-value {
    font-size: 28rpx;
    color: #333333;
}

.fee-row {
    display: flex;
    justify-content: space-between;
    margin-bottom: 15rpx;
}

.fee-row.total {
    padding-top: 15rpx;
    margin-top: 15rpx;
    border-top: 1rpx solid #f0f0f0;
}

.fee-label {
    font-size: 28rpx;
    color: #666666;
}

.fee-value {
    font-size: 28rpx;
    color: #333333;
}

.fee-amount {
    font-size: 36rpx;
    color: #ff4d4f;
    font-weight: bold;
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
    gap: 20rpx;
    box-shadow: 0 -2rpx 12rpx rgba(0, 0, 0, 0.05);
}

.action-btn {
    flex: 1;
    text-align: center;
    padding: 28rpx;
    border-radius: 48rpx;
    font-size: 30rpx;
}

.action-btn.cancel {
    background: #f5f5f5;
    color: #666666;
}

.action-btn.pay,
.action-btn.primary {
    background: #1677ff;
    color: #ffffff;
}

.action-btn.review {
    background: #ff4d4f;
    color: #ffffff;
}

.elderly-mode .info-label,
.elderly-mode .info-value,
.elderly-mode .section-title {
    font-size: 32rpx;
}
</style>
