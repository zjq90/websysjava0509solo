<template>
    <view class="home-page" :class="{ 'elderly-mode': elderlyMode }">
        <view class="header">
            <view class="search-bar" @click="goToSearch">
                <text class="search-icon">🔍</text>
                <text class="search-placeholder">搜索科室、医生、症状...</text>
            </view>
        </view>

        <view class="quick-actions card">
            <view class="action-item" @click="goToDiagnosis">
                <text class="action-icon">💊</text>
                <text class="action-text">智能导诊</text>
            </view>
            <view class="action-item" @click="goToDepartment">
                <text class="action-icon">🏥</text>
                <text class="action-text">科室导航</text>
            </view>
            <view class="action-item" @click="goToAppointment">
                <text class="action-icon">📅</text>
                <text class="action-text">预约挂号</text>
            </view>
            <view class="action-item" @click="goToPatient">
                <text class="action-icon">👨‍👩‍👧</text>
                <text class="action-text">就诊人</text>
            </view>
        </view>

        <view class="section">
            <view class="section-header">
                <text class="section-title">热门医生</text>
                <text class="section-more" @click="goToDoctorList">更多 ></text>
            </view>
            <scroll-view scroll-x class="doctor-scroll">
                <view class="doctor-list">
                    <view 
                        class="doctor-card" 
                        v-for="doctor in popularDoctors" 
                        :key="doctor.id"
                        @click="goToDoctorDetail(doctor.id)"
                    >
                        <view class="doctor-avatar">
                            <text>👨‍⚕️</text>
                        </view>
                        <text class="doctor-name">{{ doctor.realName }}</text>
                        <text class="doctor-title">{{ doctor.title }}</text>
                        <view class="doctor-rating">
                            <text class="star">⭐</text>
                            <text class="rating-text">{{ doctor.rating }}</text>
                        </view>
                        <text class="doctor-specialty">{{ doctor.specialty }}</text>
                    </view>
                </view>
            </scroll-view>
        </view>

        <view class="section">
            <view class="section-header">
                <text class="section-title">热门症状</text>
            </view>
            <view class="symptom-tags">
                <view 
                    class="symptom-tag" 
                    v-for="(symptom, index) in hotSymptoms" 
                    :key="index"
                    @click="selectSymptom(symptom)"
                >
                    {{ symptom }}
                </view>
            </view>
        </view>

        <view class="section">
            <view class="section-header">
                <text class="section-title">我的预约</text>
                <text class="section-more" @click="goToAppointment">查看全部 ></text>
            </view>
            <view class="appointment-card card" v-if="recentAppointments.length > 0">
                <view class="appointment-info">
                    <text class="appointment-doctor">{{ recentAppointments[0].doctorName }}</text>
                    <text class="appointment-dept">{{ recentAppointments[0].departmentName }}</text>
                </view>
                <view class="appointment-time">
                    <text class="time-icon">📅</text>
                    <text>{{ recentAppointments[0].appointmentDate }} {{ recentAppointments[0].startTime }}</text>
                </view>
                <view class="appointment-status" :class="getStatusClass(recentAppointments[0].status)">
                    {{ recentAppointments[0].statusText }}
                </view>
            </view>
            <view class="empty-state" v-else>
                <text class="empty-icon">📭</text>
                <text class="empty-text">暂无预约</text>
            </view>
        </view>
    </view>
</template>

<script>
import { getPopularDoctors } from '@/api/doctor'
import { getHotSymptoms } from '@/api/diagnosis'
import { getAppointmentList } from '@/api/appointment'

export default {
    data() {
        return {
            popularDoctors: [],
            hotSymptoms: [],
            recentAppointments: [],
            elderlyMode: false
        }
    },
    onShow() {
        this.elderlyMode = uni.getStorageSync('elderlyMode') || false
        this.loadData()
    },
    methods: {
        async loadData() {
            try {
                const [doctorRes, symptomRes, appointRes] = await Promise.all([
                    getPopularDoctors(),
                    getHotSymptoms(),
                    getAppointmentList('').catch(() => ({ data: { data: [] } }))
                ])
                this.popularDoctors = doctorRes.data.data || []
                this.hotSymptoms = symptomRes.data.data || []
                this.recentAppointments = appointRes.data.data || []
            } catch (e) {
                console.error(e)
            }
        },
        goToSearch() {
            uni.navigateTo({ url: '/pages/diagnosis/diagnosis' })
        },
        goToDiagnosis() {
            uni.navigateTo({ url: '/pages/diagnosis/diagnosis' })
        },
        goToDepartment() {
            uni.switchTab({ url: '/pages/department/department' })
        },
        goToAppointment() {
            uni.switchTab({ url: '/pages/appointment/appointment' })
        },
        goToPatient() {
            uni.navigateTo({ url: '/pages/patient/patient' })
        },
        goToDoctorList() {
            uni.navigateTo({ url: '/pages/doctor/doctor' })
        },
        goToDoctorDetail(id) {
            uni.navigateTo({ url: '/pages/doctor-detail/doctor-detail?id=' + id })
        },
        selectSymptom(symptom) {
            uni.navigateTo({ url: '/pages/diagnosis/diagnosis?symptom=' + encodeURIComponent(symptom) })
        },
        getStatusClass(status) {
            const statusMap = {
                'PENDING_PAYMENT': 'status-pending',
                'PAID': 'status-paid',
                'CONFIRMED': 'status-confirmed',
                'COMPLETED': 'status-completed',
                'CANCELLED': 'status-cancelled'
            }
            return statusMap[status] || 'status-pending'
        }
    }
}
</script>

<style scoped>
.home-page {
    min-height: 100vh;
    background-color: #f5f7fa;
    padding-bottom: 120rpx;
}

.header {
    background: linear-gradient(135deg, #1677ff 0%, #4096ff 100%);
    padding: 100rpx 30rpx 50rpx;
}

.search-bar {
    display: flex;
    align-items: center;
    background: rgba(255, 255, 255, 0.95);
    border-radius: 40rpx;
    padding: 24rpx 30rpx;
}

.search-icon {
    font-size: 32rpx;
    margin-right: 15rpx;
}

.search-placeholder {
    color: #999999;
    font-size: 28rpx;
}

.quick-actions {
    display: flex;
    justify-content: space-around;
    margin-top: -40rpx;
    margin-left: 30rpx;
    margin-right: 30rpx;
}

.action-item {
    display: flex;
    flex-direction: column;
    align-items: center;
}

.action-icon {
    font-size: 48rpx;
    margin-bottom: 10rpx;
}

.action-text {
    font-size: 26rpx;
    color: #333333;
}

.section {
    margin: 30rpx;
}

.section-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 20rpx;
}

.section-title {
    font-size: 32rpx;
    font-weight: bold;
    color: #333333;
}

.section-more {
    font-size: 26rpx;
    color: #1677ff;
}

.doctor-scroll {
    white-space: nowrap;
}

.doctor-list {
    display: inline-flex;
}

.doctor-card {
    display: inline-flex;
    flex-direction: column;
    align-items: center;
    width: 240rpx;
    background: #ffffff;
    border-radius: 16rpx;
    padding: 30rpx 20rpx;
    margin-right: 20rpx;
    box-shadow: 0 2rpx 12rpx rgba(0, 0, 0, 0.05);
}

.doctor-avatar {
    width: 100rpx;
    height: 100rpx;
    background: #e6f4ff;
    border-radius: 50%;
    display: flex;
    align-items: center;
    justify-content: center;
    font-size: 50rpx;
    margin-bottom: 15rpx;
}

.doctor-name {
    font-size: 30rpx;
    font-weight: 500;
    margin-bottom: 5rpx;
}

.doctor-title {
    font-size: 24rpx;
    color: #1677ff;
    margin-bottom: 10rpx;
}

.doctor-rating {
    display: flex;
    align-items: center;
    margin-bottom: 10rpx;
}

.star {
    font-size: 24rpx;
}

.rating-text {
    font-size: 24rpx;
    color: #faad14;
    margin-left: 5rpx;
}

.doctor-specialty {
    font-size: 22rpx;
    color: #999999;
    white-space: normal;
    text-align: center;
    display: -webkit-box;
    -webkit-line-clamp: 2;
    -webkit-box-orient: vertical;
    overflow: hidden;
}

.symptom-tags {
    display: flex;
    flex-wrap: wrap;
    gap: 20rpx;
}

.symptom-tag {
    background: #ffffff;
    padding: 16rpx 30rpx;
    border-radius: 40rpx;
    font-size: 26rpx;
    color: #666666;
    border: 1rpx solid #e0e0e0;
}

.appointment-card {
    padding: 30rpx;
}

.appointment-info {
    margin-bottom: 15rpx;
}

.appointment-doctor {
    display: block;
    font-size: 32rpx;
    font-weight: 500;
    margin-bottom: 8rpx;
}

.appointment-dept {
    font-size: 26rpx;
    color: #666666;
}

.appointment-time {
    display: flex;
    align-items: center;
    font-size: 26rpx;
    color: #666666;
    margin-bottom: 15rpx;
}

.time-icon {
    margin-right: 10rpx;
}

.appointment-status {
    display: inline-block;
    padding: 8rpx 24rpx;
    border-radius: 20rpx;
    font-size: 24rpx;
}

.status-pending {
    background: #fff7e6;
    color: #fa8c16;
}

.status-paid {
    background: #e6f4ff;
    color: #1677ff;
}

.status-confirmed {
    background: #f6ffed;
    color: #52c41a;
}

.status-completed {
    background: #f5f5f5;
    color: #666666;
}

.status-cancelled {
    background: #fff1f0;
    color: #ff4d4f;
}

.empty-state {
    display: flex;
    flex-direction: column;
    align-items: center;
    padding: 60rpx 0;
    background: #ffffff;
    border-radius: 16rpx;
}

.empty-icon {
    font-size: 64rpx;
    margin-bottom: 15rpx;
}

.empty-text {
    font-size: 28rpx;
    color: #999999;
}

.elderly-mode .action-icon {
    font-size: 60rpx;
}

.elderly-mode .action-text {
    font-size: 32rpx;
}

.elderly-mode .section-title {
    font-size: 40rpx;
}

.elderly-mode .doctor-card {
    width: 300rpx;
    padding: 40rpx 25rpx;
}

.elderly-mode .doctor-name {
    font-size: 36rpx;
}
</style>
