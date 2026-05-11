<template>
    <view class="doctor-detail-page" :class="{ 'elderly-mode': elderlyMode }">
        <view class="doctor-header card">
            <view class="doctor-avatar">
                <text>👨‍⚕️</text>
            </view>
            <view class="doctor-info">
                <view class="doctor-name-row">
                    <text class="doctor-name">{{ doctor.realName }}</text>
                    <text class="doctor-title">{{ doctor.title }}</text>
                </view>
                <text class="doctor-department">{{ doctor.departmentName }}</text>
                <view class="doctor-stats">
                    <view class="stat-item">
                        <text class="star">⭐</text>
                        <text class="stat-value">{{ doctor.rating }}</text>
                    </view>
                    <view class="stat-item">
                        <text class="stat-label">评价</text>
                        <text class="stat-value">{{ doctor.reviewCount }}</text>
                    </view>
                    <view class="stat-item">
                        <text class="stat-label">就诊</text>
                        <text class="stat-value">{{ doctor.visitCount }}</text>
                    </view>
                </view>
            </view>
            <view class="doctor-fee">
                <text class="fee-label">挂号费</text>
                <text class="fee-value">¥{{ doctor.consultationFee }}</text>
            </view>
        </view>

        <view class="section card" v-if="doctor.specialty">
            <text class="section-title">擅长领域</text>
            <text class="section-content">{{ doctor.specialty }}</text>
        </view>

        <view class="section card" v-if="doctor.introduction">
            <text class="section-title">个人简介</text>
            <text class="section-content">{{ doctor.introduction }}</text>
        </view>

        <view class="section card" v-if="doctor.schedules && doctor.schedules.length > 0">
            <text class="section-title">出诊时间</text>
            <view class="schedule-list">
                <view 
                    class="schedule-item" 
                    v-for="schedule in doctor.schedules" 
                    :key="schedule.id"
                    :class="{ disabled: schedule.availableSlots <= 0 }"
                    @click="selectSchedule(schedule)"
                >
                    <view class="schedule-date">
                        <text class="date-text">{{ schedule.scheduleDate }}</text>
                        <text class="slot-text">{{ getSlotText(schedule.timeSlot) }}</text>
                    </view>
                    <view class="schedule-availability">
                        <text class="available" v-if="schedule.availableSlots > 0">
                            余{{ schedule.availableSlots }}号
                        </text>
                        <text class="full" v-else>已约满</text>
                    </view>
                </view>
            </view>
        </view>

        <view class="section card" v-if="doctor.reviews && doctor.reviews.length > 0">
            <text class="section-title">患者评价</text>
            <view class="review-list">
                <view class="review-item" v-for="review in doctor.reviews" :key="review.id">
                    <view class="review-header">
                        <text class="review-patient">{{ review.patientName || '匿名用户' }}</text>
                        <view class="review-rating">
                            <text>⭐</text>
                            <text>{{ review.rating }}</text>
                        </view>
                    </view>
                    <text class="review-content">{{ review.comment }}</text>
                    <text class="review-time">{{ review.createdAt }}</text>
                </view>
            </view>
        </view>

        <view class="bottom-bar" v-if="selectedSchedule">
            <view class="schedule-info">
                <text class="selected-text">已选：{{ selectedSchedule.scheduleDate }} {{ getSlotText(selectedSchedule.timeSlot) }}</text>
            </view>
            <view class="book-btn" @click="goToBook">
                立即预约
            </view>
        </view>
        <view class="bottom-bar" v-else>
            <view class="book-btn disabled" @click="selectFirstSchedule">
                请选择出诊时间
            </view>
        </view>
    </view>
</template>

<script>
import { getDoctorDetail } from '@/api/doctor'

export default {
    data() {
        return {
            doctorId: null,
            doctor: {},
            selectedSchedule: null,
            elderlyMode: false
        }
    },
    onLoad(options) {
        this.doctorId = options.id
        this.elderlyMode = uni.getStorageSync('elderlyMode') || false
        this.loadDoctor()
    },
    methods: {
        async loadDoctor() {
            try {
                const res = await getDoctorDetail(this.doctorId)
                this.doctor = res.data.data || {}
            } catch (e) {
                console.error(e)
            }
        },
        getSlotText(slot) {
            const map = {
                'MORNING': '上午',
                'AFTERNOON': '下午',
                'EVENING': '晚上'
            }
            return map[slot] || slot
        },
        selectSchedule(schedule) {
            if (schedule.availableSlots <= 0) {
                uni.showToast({ title: '该时段已约满', icon: 'none' })
                return
            }
            this.selectedSchedule = schedule
        },
        selectFirstSchedule() {
            if (this.doctor.schedules && this.doctor.schedules.length > 0) {
                const available = this.doctor.schedules.find(s => s.availableSlots > 0)
                if (available) {
                    this.selectedSchedule = available
                } else {
                    uni.showToast({ title: '暂无可用号源', icon: 'none' })
                }
            }
        },
        goToBook() {
            const params = {
                doctorId: this.doctorId,
                doctorName: this.doctor.realName,
                departmentName: this.doctor.departmentName,
                fee: this.doctor.consultationFee,
                scheduleId: this.selectedSchedule.id,
                scheduleDate: this.selectedSchedule.scheduleDate,
                timeSlot: this.getSlotText(this.selectedSchedule.timeSlot)
            }
            
            uni.navigateTo({
                url: '/pages/patient/patient?mode=select&params=' + encodeURIComponent(JSON.stringify(params))
            })
        }
    }
}
</script>

<style scoped>
.doctor-detail-page {
    min-height: 100vh;
    background-color: #f5f7fa;
    padding-bottom: 150rpx;
}

.doctor-header {
    display: flex;
    align-items: center;
    padding: 40rpx 30rpx;
    margin: 20rpx 30rpx;
}

.doctor-avatar {
    width: 140rpx;
    height: 140rpx;
    background: #e6f4ff;
    border-radius: 50%;
    display: flex;
    align-items: center;
    justify-content: center;
    font-size: 70rpx;
    margin-right: 30rpx;
}

.doctor-info {
    flex: 1;
}

.doctor-name-row {
    display: flex;
    align-items: center;
    margin-bottom: 10rpx;
}

.doctor-name {
    font-size: 36rpx;
    font-weight: bold;
    margin-right: 15rpx;
}

.doctor-title {
    font-size: 26rpx;
    color: #1677ff;
    background: #e6f4ff;
    padding: 4rpx 12rpx;
    border-radius: 4rpx;
}

.doctor-department {
    font-size: 28rpx;
    color: #666666;
    margin-bottom: 15rpx;
}

.doctor-stats {
    display: flex;
    gap: 30rpx;
}

.stat-item {
    display: flex;
    align-items: center;
    font-size: 26rpx;
}

.star {
    margin-right: 5rpx;
}

.stat-label {
    color: #999999;
    margin-right: 5rpx;
}

.stat-value {
    color: #333333;
}

.doctor-fee {
    text-align: right;
}

.fee-label {
    font-size: 24rpx;
    color: #999999;
    display: block;
}

.fee-value {
    font-size: 36rpx;
    color: #ff4d4f;
    font-weight: bold;
}

.section {
    margin: 20rpx 30rpx;
    padding: 30rpx;
}

.section-title {
    font-size: 30rpx;
    font-weight: bold;
    color: #333333;
    display: block;
    margin-bottom: 15rpx;
}

.section-content {
    font-size: 28rpx;
    color: #666666;
    line-height: 1.8;
}

.schedule-list {
    display: flex;
    flex-wrap: wrap;
    gap: 20rpx;
}

.schedule-item {
    width: calc(50% - 10rpx);
    background: #f5f7fa;
    border-radius: 12rpx;
    padding: 20rpx;
    display: flex;
    justify-content: space-between;
    align-items: center;
}

.schedule-item.disabled {
    opacity: 0.5;
}

.date-text {
    font-size: 28rpx;
    color: #333333;
    display: block;
}

.slot-text {
    font-size: 24rpx;
    color: #666666;
}

.available {
    font-size: 24rpx;
    color: #52c41a;
}

.full {
    font-size: 24rpx;
    color: #ff4d4f;
}

.review-list {
    gap: 20rpx;
}

.review-item {
    padding: 20rpx 0;
    border-bottom: 1rpx solid #f0f0f0;
}

.review-item:last-child {
    border-bottom: none;
}

.review-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 10rpx;
}

.review-patient {
    font-size: 28rpx;
    font-weight: 500;
}

.review-rating {
    font-size: 26rpx;
    color: #faad14;
}

.review-content {
    font-size: 26rpx;
    color: #666666;
    line-height: 1.6;
    margin-bottom: 10rpx;
}

.review-time {
    font-size: 24rpx;
    color: #999999;
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

.schedule-info {
    flex: 1;
}

.selected-text {
    font-size: 26rpx;
    color: #666666;
}

.book-btn {
    background: #1677ff;
    color: #ffffff;
    padding: 24rpx 60rpx;
    border-radius: 48rpx;
    font-size: 32rpx;
}

.book-btn.disabled {
    background: #d9d9d9;
}
</style>
