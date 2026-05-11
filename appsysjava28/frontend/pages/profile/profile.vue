<template>
    <view class="profile-page" :class="{ 'elderly-mode': elderlyMode }">
        <view class="user-header">
            <view class="user-avatar">
                <text>👤</text>
            </view>
            <view class="user-info">
                <text class="user-name">{{ userInfo.nickName || '未登录用户' }}</text>
                <text class="user-phone" v-if="userInfo.phone">{{ userInfo.phone }}</text>
            </view>
            <view class="edit-btn" @click="goToEdit">
                <text>编辑</text>
            </view>
        </view>

        <view class="stats-row">
            <view class="stat-item">
                <text class="stat-value">{{ stats.appointmentCount }}</text>
                <text class="stat-label">总预约</text>
            </view>
            <view class="stat-divider"></view>
            <view class="stat-item">
                <text class="stat-value">{{ stats.patientCount }}</text>
                <text class="stat-label">就诊人</text>
            </view>
            <view class="stat-divider"></view>
            <view class="stat-item">
                <text class="stat-value">{{ stats.reviewCount }}</text>
                <text class="stat-label">评价</text>
            </view>
        </view>

        <view class="menu-section card">
            <view class="menu-item" @click="goToAppointment">
                <view class="menu-left">
                    <text class="menu-icon">📋</text>
                    <text class="menu-label">我的预约</text>
                </view>
                <text class="menu-arrow">></text>
            </view>
            <view class="menu-item" @click="goToPatient">
                <view class="menu-left">
                    <text class="menu-icon">👥</text>
                    <text class="menu-label">就诊人管理</text>
                </view>
                <text class="menu-arrow">></text>
            </view>
            <view class="menu-item" @click="goToDiagnosis">
                <view class="menu-left">
                    <text class="menu-icon">💡</text>
                    <text class="menu-label">智能导诊</text>
                </view>
                <text class="menu-arrow">></text>
            </view>
        </view>

        <view class="menu-section card">
            <view class="menu-item" @click="toggleElderlyMode">
                <view class="menu-left">
                    <text class="menu-icon">👵</text>
                    <text class="menu-label">长辈模式</text>
                </view>
                <switch 
                    :checked="elderlyMode" 
                    color="#1677ff"
                    @change="onElderlyModeChange"
                />
            </view>
            <view class="menu-item" @click="goToSettings">
                <view class="menu-left">
                    <text class="menu-icon">⚙️</text>
                    <text class="menu-label">设置</text>
                </view>
                <text class="menu-arrow">></text>
            </view>
            <view class="menu-item" @click="goToAbout">
                <view class="menu-left">
                    <text class="menu-icon">ℹ️</text>
                    <text class="menu-label">关于我们</text>
                </view>
                <text class="menu-arrow">></text>
            </view>
        </view>

        <view class="logout-section">
            <view class="logout-btn" @click="logout" v-if="isLoggedIn">
                退出登录
            </view>
            <view class="login-btn" @click="goToLogin" v-else>
                立即登录
            </view>
        </view>
    </view>
</template>

<script>
import { getCurrentUser, toggleElderlyMode as toggleElderlyModeApi } from '@/api/auth'
import { getPatientList } from '@/api/patient'
import { getAppointmentList } from '@/api/appointment'

export default {
    data() {
        return {
            userInfo: {},
            stats: {
                appointmentCount: 0,
                patientCount: 0,
                reviewCount: 0
            },
            elderlyMode: false,
            isLoggedIn: false
        }
    },
    onShow() {
        this.elderlyMode = uni.getStorageSync('elderlyMode') || false
        const token = uni.getStorageSync('token')
        this.isLoggedIn = !!token
        if (this.isLoggedIn) {
            this.loadUserInfo()
            this.loadStats()
        }
    },
    methods: {
        async loadUserInfo() {
            try {
                const res = await getCurrentUser()
                this.userInfo = res.data.data || {}
            } catch (e) {
                console.error(e)
            }
        },
        async loadStats() {
            try {
                const [patientRes, appointmentRes] = await Promise.all([
                    getPatientList(),
                    getAppointmentList('')
                ])
                this.stats.patientCount = (patientRes.data.data || []).length
                this.stats.appointmentCount = (appointmentRes.data.data || []).length
            } catch (e) {
                console.error(e)
            }
        },
        toggleElderlyMode() {
        },
        async onElderlyModeChange(e) {
            const newValue = e.detail.value
            try {
                await toggleElderlyModeApi()
                uni.setStorageSync('elderlyMode', newValue)
                this.elderlyMode = newValue
                uni.showToast({ 
                    title: newValue ? '已开启长辈模式' : '已关闭长辈模式', 
                    icon: 'none' 
                })
            } catch (e) {
                console.error(e)
            }
        },
        goToEdit() {
            if (!this.isLoggedIn) {
                uni.navigateTo({ url: '/pages/login/login' })
                return
            }
            uni.showToast({ title: '编辑功能开发中', icon: 'none' })
        },
        goToAppointment() {
            uni.switchTab({ url: '/pages/appointment/appointment' })
        },
        goToPatient() {
            uni.navigateTo({ url: '/pages/patient/patient' })
        },
        goToDiagnosis() {
            uni.navigateTo({ url: '/pages/diagnosis/diagnosis' })
        },
        goToSettings() {
            uni.navigateTo({ url: '/pages/settings/settings' })
        },
        goToAbout() {
            uni.showModal({
                title: '关于我们',
                content: '医疗预约APP v1.0\n\n一个便捷的医疗预约平台，让就医更简单。\n\n服务热线：400-888-8888',
                showCancel: false
            })
        },
        logout() {
            uni.showModal({
                title: '退出登录',
                content: '确定要退出当前账号吗？',
                success: (res) => {
                    if (res.confirm) {
                        uni.removeStorageSync('token')
                        uni.removeStorageSync('userInfo')
                        this.isLoggedIn = false
                        this.userInfo = {}
                        this.stats = { appointmentCount: 0, patientCount: 0, reviewCount: 0 }
                        uni.showToast({ title: '已退出登录', icon: 'success' })
                    }
                }
            })
        },
        goToLogin() {
            uni.navigateTo({ url: '/pages/login/login' })
        }
    }
}
</script>

<style scoped>
.profile-page {
    min-height: 100vh;
    background-color: #f5f7fa;
}

.user-header {
    background: linear-gradient(135deg, #1677ff 0%, #0958d9 100%);
    padding: 60rpx 30rpx;
    display: flex;
    align-items: center;
}

.user-avatar {
    width: 120rpx;
    height: 120rpx;
    background: rgba(255, 255, 255, 0.2);
    border-radius: 50%;
    display: flex;
    align-items: center;
    justify-content: center;
    font-size: 60rpx;
    margin-right: 25rpx;
}

.user-info {
    flex: 1;
}

.user-name {
    font-size: 36rpx;
    font-weight: bold;
    color: #ffffff;
    display: block;
    margin-bottom: 10rpx;
}

.user-phone {
    font-size: 28rpx;
    color: rgba(255, 255, 255, 0.8);
}

.edit-btn {
    padding: 15rpx 30rpx;
    background: rgba(255, 255, 255, 0.2);
    border-radius: 30rpx;
    color: #ffffff;
    font-size: 26rpx;
}

.stats-row {
    background: #ffffff;
    margin: -30rpx 30rpx 20rpx;
    border-radius: 16rpx;
    display: flex;
    align-items: center;
    padding: 30rpx 0;
    box-shadow: 0 4rpx 12rpx rgba(0, 0, 0, 0.05);
    position: relative;
    z-index: 10;
}

.stat-item {
    flex: 1;
    display: flex;
    flex-direction: column;
    align-items: center;
}

.stat-value {
    font-size: 36rpx;
    font-weight: bold;
    color: #333333;
    margin-bottom: 8rpx;
}

.stat-label {
    font-size: 24rpx;
    color: #999999;
}

.stat-divider {
    width: 1rpx;
    height: 60rpx;
    background: #f0f0f0;
}

.menu-section {
    margin: 20rpx 30rpx;
    padding: 0;
}

.menu-item {
    display: flex;
    align-items: center;
    justify-content: space-between;
    padding: 30rpx 25rpx;
    border-bottom: 1rpx solid #f0f0f0;
}

.menu-item:last-child {
    border-bottom: none;
}

.menu-left {
    display: flex;
    align-items: center;
}

.menu-icon {
    font-size: 40rpx;
    margin-right: 20rpx;
}

.menu-label {
    font-size: 30rpx;
    color: #333333;
}

.menu-arrow {
    font-size: 28rpx;
    color: #999999;
}

.logout-section {
    padding: 40rpx 30rpx;
}

.logout-btn {
    background: #ffffff;
    color: #ff4d4f;
    text-align: center;
    padding: 30rpx;
    border-radius: 12rpx;
    font-size: 30rpx;
    border: 1rpx solid #ff4d4f;
}

.login-btn {
    background: #1677ff;
    color: #ffffff;
    text-align: center;
    padding: 30rpx;
    border-radius: 12rpx;
    font-size: 30rpx;
}

.elderly-mode .user-name {
    font-size: 42rpx;
}

.elderly-mode .menu-label {
    font-size: 34rpx;
}
</style>
